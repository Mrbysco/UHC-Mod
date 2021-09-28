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
	
	public UHCPage3Packet(boolean timeLock , int timeLockUntil, String timeLockMode, boolean minuteMark, int minuteEvery,
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

	public void encode(PacketBuffer buf) {
		buf.writeBoolean(timeLock);
		buf.writeInt(timeLockUntil);
		buf.writeString(timeLockMode);
		buf.writeBoolean(minuteMark);
		buf.writeInt(minuteEvery);
		buf.writeBoolean(timedNames);
		buf.writeInt(timedNamesAfter);
		buf.writeBoolean(timedGlow);
		buf.writeInt(timedGlowAfter);
	}

	public static UHCPage3Packet decode(final PacketBuffer packetBuffer) {
		return new UHCPage3Packet(packetBuffer.readBoolean(), packetBuffer.readInt(), packetBuffer.readString(),
				packetBuffer.readBoolean(), packetBuffer.readInt(), packetBuffer.readBoolean(), packetBuffer.readInt(),
				packetBuffer.readBoolean(), packetBuffer.readInt());
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
						saveData.setTimeLock(timeLock);
						saveData.setTimeLockTimer(timeLockUntil);
						saveData.setTimeMode(timeLockMode);
						saveData.setMinuteMark(minuteMark);
						saveData.setMinuteMarkTime(minuteEvery);
						saveData.setTimedNames(timedNames);
						saveData.setNameTimer(timedNamesAfter);
						saveData.setTimedGlow(timedGlow);
						saveData.setGlowTime(timedGlowAfter);
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
