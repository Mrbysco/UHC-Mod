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

public class UHCPage2Packet {
	public int borderSize;
	public double centerX;
	public double centerZ;
	public boolean borderShrink;
	public int timeUntil;
	public int size;
	public int over;
	public String shrinkMode;
	
	public UHCPage2Packet(int borderSize, double centerX, double centerZ, boolean borderShrink, int timeUntil, int size, int over, String mode) {
		this.borderSize = borderSize;
		this.centerX = centerX;
		this.centerZ = centerZ;
		this.borderShrink = borderShrink;
		this.timeUntil = timeUntil;
		this.size = size;
		this.over = over;
		this.shrinkMode = mode;
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(borderSize);
		buf.writeDouble(centerX);
		buf.writeDouble(centerZ);
		buf.writeBoolean(borderShrink);
		buf.writeInt(timeUntil);
		buf.writeInt(size);
		buf.writeInt(over);
		buf.writeString(shrinkMode);
	}

	public static UHCPage2Packet decode(final PacketBuffer packetBuffer) {
		return new UHCPage2Packet(packetBuffer.readInt(), packetBuffer.readDouble(), packetBuffer.readDouble(), packetBuffer.readBoolean(),
				packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readString());
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
						saveData.setBorderSize(borderSize);
						saveData.setBorderCenterX(centerX);
						saveData.setBorderCenterZ(centerZ);
						saveData.setShrinkEnabled(borderShrink);
						saveData.setShrinkTimer(timeUntil);
						saveData.setShrinkSize(size);
						saveData.setShrinkOvertime(over);
						saveData.setShrinkMode(shrinkMode);
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
