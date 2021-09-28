package com.mrbysco.uhc.data;

import com.mrbysco.uhc.Reference;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class UHCTimerData extends WorldSavedData{

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
	
	public UHCTimerData(String name) {
		super(name);

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

	public UHCTimerData() {
		super(DATA_NAME);

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
	public void read(CompoundNBT nbt) {
		shrinkTimeUntil = nbt.getInt("shrinkTimeUntil");
		timeLockTimer = nbt.getInt("timeLockTimer");
		minuteMarkTimer = nbt.getInt("minuteMarkTimer");
		minuteMarkAmount = nbt.getInt("minuteMarkAmount");
		nameTimer = nbt.getInt("nameTimer");
		glowTimer = nbt.getInt("glowTimer");
		controlled = nbt.getBoolean("pointControlled");
		uhcStartTimer = nbt.getInt("uhcStartTimer");
		graceTimer = nbt.getInt("graceTimer");
		twilightBossGraceTimer = nbt.getInt("twilightBossGraceTimer");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt("shrinkTimeUntil", shrinkTimeUntil);
		compound.putInt("timeLockTimer", timeLockTimer);
		compound.putInt("minuteMarkTimer", minuteMarkTimer);
		compound.putInt("minuteMarkAmount", minuteMarkAmount);
		compound.putInt("nameTimer", nameTimer);
		compound.putInt("glowTimer", glowTimer);
		compound.putBoolean("pointControlled", controlled);
		compound.putInt("uhcStartTimer", uhcStartTimer);
		compound.putInt("graceTimer", graceTimer);
		compound.putInt("twilightBossGraceTimer", twilightBossGraceTimer);
		return compound;
	}

	public static UHCTimerData get(World world) {
		if (!(world instanceof ServerWorld)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);

		DimensionSavedDataManager storage = overworld.getSavedData();
		return storage.getOrCreate(UHCTimerData::new, DATA_NAME);
	}
}
