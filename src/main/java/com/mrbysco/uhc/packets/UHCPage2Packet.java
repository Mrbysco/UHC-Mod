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

public class UHCPage2Packet {
	public final int borderSize;
	public final double centerX;
	public final double centerZ;
	public final boolean borderShrink;
	public final int timeUntil;
	public final int size;
	public final int over;
	public final String shrinkMode;

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

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(borderSize);
		buf.writeDouble(centerX);
		buf.writeDouble(centerZ);
		buf.writeBoolean(borderShrink);
		buf.writeInt(timeUntil);
		buf.writeInt(size);
		buf.writeInt(over);
		buf.writeUtf(shrinkMode);
	}

	public static UHCPage2Packet decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPage2Packet(packetBuffer.readInt(), packetBuffer.readDouble(), packetBuffer.readDouble(), packetBuffer.readBoolean(),
				packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readUtf());
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
						saveData.setBorderSize(borderSize);
						saveData.setBorderCenterX(centerX);
						saveData.setBorderCenterZ(centerZ);
						saveData.setShrinkEnabled(borderShrink);
						saveData.setShrinkTimer(timeUntil);
						saveData.setShrinkSize(size);
						saveData.setShrinkOvertime(over);
						saveData.setShrinkMode(shrinkMode);
						saveData.setDirty();

						UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(serverPlayer.getUUID(), saveData));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
