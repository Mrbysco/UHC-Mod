package com.mrbysco.uhc.data;

import com.mrbysco.uhc.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class UHCTimerData extends SavedData {
	private static final String DATA_NAME = Reference.MOD_ID + "_timer_data";

	private int shrinkTimeUntil;
	private int timeLockTimer;
	private int minuteMarkTimer;
	private int minuteMarkAmount;
	private int nameTimer;
	private int glowTimer;
	private boolean controlled;
	private int uhcStartTimer;
	private int graceTimer;
	private int twilightBossGraceTimer;

	public UHCTimerData() {
		this.shrinkTimeUntil = 0;
		this.timeLockTimer = 0;
		this.minuteMarkTimer = 0;
		this.minuteMarkAmount = 0;
		this.nameTimer = 0;
		this.glowTimer = 0;
		this.controlled = false;
		this.uhcStartTimer = 0;
		this.graceTimer = 0;
		this.twilightBossGraceTimer = 0;
	}

	public void resetAll() {
		this.shrinkTimeUntil = 0;
		this.timeLockTimer = 0;
		this.minuteMarkTimer = 0;
		this.minuteMarkAmount = 0;
		this.nameTimer = 0;
		this.glowTimer = 0;
		this.controlled = false;
		this.uhcStartTimer = 0;
		this.graceTimer = 0;
		this.twilightBossGraceTimer = 0;
	}

	public int getShrinkTimeUntil() {
		return shrinkTimeUntil;
	}

	public void setShrinkTimeUntil(int shrinkTimeUntil) {
		this.shrinkTimeUntil = shrinkTimeUntil;
	}

	public int getTimeLockTimer() {
		return timeLockTimer;
	}

	public void setTimeLockTimer(int timeLockTimer) {
		this.timeLockTimer = timeLockTimer;
	}

	public int getMinuteMarkTimer() {
		return minuteMarkTimer;
	}

	public void setMinuteMarkTimer(int minuteMarkTimer) {
		this.minuteMarkTimer = minuteMarkTimer;
	}

	public int getNameTimer() {
		return nameTimer;
	}

	public void setNameTimer(int nameTimer) {
		this.nameTimer = nameTimer;
	}

	public int getGlowTimer() {
		return glowTimer;
	}

	public void setGlowTimer(int glowTimer) {
		this.glowTimer = glowTimer;
	}

	public boolean isControlled() {
		return controlled;
	}

	public void setControlled(boolean controlled) {
		this.controlled = controlled;
	}

	public int getUhcStartTimer() {
		return uhcStartTimer;
	}

	public void setUhcStartTimer(int uhcStartTimer) {
		this.uhcStartTimer = uhcStartTimer;
	}

	public int getMinuteMarkAmount() {
		return minuteMarkAmount;
	}

	public void setMinuteMarkAmount(int minuteMarkAmount) {
		this.minuteMarkAmount = minuteMarkAmount;
	}

	public int getGraceTimer() {
		return graceTimer;
	}

	public void setGraceTimer(int graceTimer) {
		this.graceTimer = graceTimer;
	}

	public int getTwilightBossGraceTimer() {
		return twilightBossGraceTimer;
	}

	public void setTwilightBossGraceTimer(int twilightBossGraceTimer) {
		this.twilightBossGraceTimer = twilightBossGraceTimer;
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		tag.putInt("shrinkTimeUntil", shrinkTimeUntil);
		tag.putInt("timeLockTimer", timeLockTimer);
		tag.putInt("minuteMarkTimer", minuteMarkTimer);
		tag.putInt("minuteMarkAmount", minuteMarkAmount);
		tag.putInt("nameTimer", nameTimer);
		tag.putInt("glowTimer", glowTimer);
		tag.putBoolean("pointControlled", controlled);
		tag.putInt("uhcStartTimer", uhcStartTimer);
		tag.putInt("graceTimer", graceTimer);
		tag.putInt("twilightBossGraceTimer", twilightBossGraceTimer);
		return tag;
	}

	public static UHCTimerData load(CompoundTag tag, HolderLookup.Provider provider) {
		UHCTimerData timerData = new UHCTimerData();

		timerData.shrinkTimeUntil = tag.getInt("shrinkTimeUntil");
		timerData.timeLockTimer = tag.getInt("timeLockTimer");
		timerData.minuteMarkTimer = tag.getInt("minuteMarkTimer");
		timerData.minuteMarkAmount = tag.getInt("minuteMarkAmount");
		timerData.nameTimer = tag.getInt("nameTimer");
		timerData.glowTimer = tag.getInt("glowTimer");
		timerData.controlled = tag.getBoolean("pointControlled");
		timerData.uhcStartTimer = tag.getInt("uhcStartTimer");
		timerData.graceTimer = tag.getInt("graceTimer");
		timerData.twilightBossGraceTimer = tag.getInt("twilightBossGraceTimer");

		return timerData;
	}

	public static UHCTimerData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

		DimensionDataStorage storage = overworld.getDataStorage();
		return storage.computeIfAbsent(new Factory<>(UHCTimerData::new, UHCTimerData::load), DATA_NAME);
	}
}
