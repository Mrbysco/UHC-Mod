package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class UHCPage6Packet {
	public boolean graceEnabled;
	public int graceTime;
	
	public UHCPage6Packet(boolean graceEnabled, int graceTime) {
		this.graceEnabled = graceEnabled;
		this.graceTime = graceTime;
	}

	public void encode(PacketBuffer buf) {
		buf.writeBoolean(graceEnabled);
		buf.writeInt(graceTime);
	}

	public static UHCPage6Packet decode(final PacketBuffer packetBuffer) {
		return new UHCPage6Packet(packetBuffer.readBoolean(), packetBuffer.readInt());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayerEntity serverPlayer = ctx.getSender();
				ServerWorld overworld = serverPlayer.getServer().getWorld(World.OVERWORLD);
				if(overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundNBT playerData = serverPlayer.getPersistentData();

					if(playerData.getBoolean("canEditUHC")) {
						saveData.setGraceEnabled(graceEnabled);
						saveData.setGraceTime(graceTime);
						saveData.markDirty();

						UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(serverPlayer.getUniqueID(), saveData));
					} else {
						serverPlayer.sendMessage(new StringTextComponent("You don't have permissions to edit the UHC book").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
