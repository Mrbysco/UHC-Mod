package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class UHCPage6Packet {
	public final boolean graceEnabled;
	public final int graceTime;

	public UHCPage6Packet(boolean graceEnabled, int graceTime) {
		this.graceEnabled = graceEnabled;
		this.graceTime = graceTime;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(graceEnabled);
		buf.writeInt(graceTime);
	}

	public static UHCPage6Packet decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPage6Packet(packetBuffer.readBoolean(), packetBuffer.readInt());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer serverPlayer = ctx.getSender();
				ServerLevel overworld = serverPlayer.getServer().overworld();
				if (overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundTag playerData = serverPlayer.getPersistentData();

					if (playerData.getBoolean("canEditUHC")) {
						saveData.setGraceEnabled(graceEnabled);
						saveData.setGraceTime(graceTime);
						saveData.setDirty();

						UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(saveData.save(new CompoundTag())));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
