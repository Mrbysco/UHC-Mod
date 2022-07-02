package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class UHCPage3Packet {
	public boolean timeLock;
	public int timeLockUntil;
	public String timeLockMode;
	public boolean minuteMark;
	public int minuteEvery;
	public boolean timedNames;
	public int timedNamesAfter;
	public boolean timedGlow;
	public int timedGlowAfter;

	public UHCPage3Packet(boolean timeLock, int timeLockUntil, String timeLockMode, boolean minuteMark, int minuteEvery,
						  boolean timedNames, int timedNamesAfter, boolean timedGlow, int timedGlowAfter) {
		this.timeLock = timeLock;
		this.timeLockUntil = timeLockUntil;
		this.timeLockMode = timeLockMode;
		this.minuteMark = minuteMark;
		this.minuteEvery = minuteEvery;
		this.timedNames = timedNames;
		this.timedNamesAfter = timedNamesAfter;
		this.timedGlow = timedGlow;
		this.timedGlowAfter = timedGlowAfter;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(timeLock);
		buf.writeInt(timeLockUntil);
		buf.writeUtf(timeLockMode);
		buf.writeBoolean(minuteMark);
		buf.writeInt(minuteEvery);
		buf.writeBoolean(timedNames);
		buf.writeInt(timedNamesAfter);
		buf.writeBoolean(timedGlow);
		buf.writeInt(timedGlowAfter);
	}

	public static UHCPage3Packet decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPage3Packet(packetBuffer.readBoolean(), packetBuffer.readInt(), packetBuffer.readUtf(),
				packetBuffer.readBoolean(), packetBuffer.readInt(), packetBuffer.readBoolean(), packetBuffer.readInt(),
				packetBuffer.readBoolean(), packetBuffer.readInt());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer serverPlayer = ctx.getSender();
				ServerLevel overworld = serverPlayer.getServer().getLevel(Level.OVERWORLD);
				if (overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundTag playerData = serverPlayer.getPersistentData();

					if (playerData.getBoolean("canEditUHC")) {
						saveData.setTimeLock(timeLock);
						saveData.setTimeLockTimer(timeLockUntil);
						saveData.setTimeMode(timeLockMode);
						saveData.setMinuteMark(minuteMark);
						saveData.setMinuteMarkTime(minuteEvery);
						saveData.setTimedNames(timedNames);
						saveData.setNameTimer(timedNamesAfter);
						saveData.setTimedGlow(timedGlow);
						saveData.setGlowTime(timedGlowAfter);
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
