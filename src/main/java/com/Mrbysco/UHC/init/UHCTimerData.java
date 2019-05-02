package com.Mrbysco.UHC.init;

import com.Mrbysco.UHC.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
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
	public void readFromNBT(NBTTagCompound nbt) {
		shrinkTimeUntil = nbt.getInteger("shrinkTimeUntil");
		timeLockTimer = nbt.getInteger("timeLockTimer");
		minuteMarkTimer = nbt.getInteger("minuteMarkTimer");
		minuteMarkAmount = nbt.getInteger("minuteMarkAmount");
		nameTimer = nbt.getInteger("nameTimer");
		glowTimer = nbt.getInteger("glowTimer");
		controlled = nbt.getBoolean("pointControlled");
		uhcStartTimer = nbt.getInteger("uhcStartTimer");
		graceTimer = nbt.getInteger("graceTimer");
		twilightBossGraceTimer = nbt.getInteger("twilightBossGraceTimer");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("shrinkTimeUntil", shrinkTimeUntil);
		compound.setInteger("timeLockTimer", timeLockTimer);
		compound.setInteger("minuteMarkTimer", minuteMarkTimer);
		compound.setInteger("minuteMarkAmount", minuteMarkAmount);
		compound.setInteger("nameTimer", nameTimer);
		compound.setInteger("glowTimer", glowTimer);
		compound.setBoolean("pointControlled", controlled);
		compound.setInteger("uhcStartTimer", uhcStartTimer);
		compound.setInteger("graceTimer", graceTimer);
		compound.setInteger("twilightBossGraceTimer", twilightBossGraceTimer);
		return compound;
	}
	
	public static UHCTimerData getForWorld(World world) {
        MapStorage storage = world.getPerWorldStorage();
		UHCTimerData data = (UHCTimerData) storage.getOrLoadData(UHCTimerData.class, DATA_NAME);
		if (data == null) {
			data = new UHCTimerData();
			storage.setData(DATA_NAME, data);
		}
		return data;
	}
}
