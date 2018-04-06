package com.Mrbysco.UHC.init;

import com.blamejared.ctgui.reference.Reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class UHCTimerData extends WorldSavedData{

	private static final String DATA_NAME = Reference.MOD_ID + "_timer_data";
	
	private int shrinkTimeUntil;
	private int timeLockTimer;
	private int minuteMarkTimer;
	private int nameTimer;
	private int glowTimer;
	private boolean controlled;
	private int uhcStartTimer;
	
	public UHCTimerData(String name) {
		super(name);

		this.shrinkTimeUntil = 0;
		this.timeLockTimer = 0;
		this.minuteMarkTimer = 0;
		this.nameTimer = 0;
		this.glowTimer = 0;
		this.controlled = false;
		this.uhcStartTimer = 0;
	}

	public UHCTimerData() {
		super(DATA_NAME);

		this.shrinkTimeUntil = 0;
		this.timeLockTimer = 0;
		this.minuteMarkTimer = 0;
		this.nameTimer = 0;
		this.glowTimer = 0;
		this.controlled = false;
		this.uhcStartTimer = 0;
	}
	
	public void resetAll() {
		this.shrinkTimeUntil = 0;
		this.timeLockTimer = 0;
		this.minuteMarkTimer = 0;
		this.nameTimer = 0;
		this.glowTimer = 0;
		this.controlled = false;
		this.uhcStartTimer = 0;
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

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		shrinkTimeUntil = nbt.getInteger("shrinkTimeUntil");
		timeLockTimer = nbt.getInteger("timeLockTimer");
		minuteMarkTimer = nbt.getInteger("minuteMarkTimer");
		nameTimer = nbt.getInteger("nameTimer");
		glowTimer = nbt.getInteger("glowTimer");
		controlled = nbt.getBoolean("pointControlled");
		uhcStartTimer = nbt.getInteger("uhcStartTimer");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("shrinkTimeUntil", shrinkTimeUntil);
		compound.setInteger("timeLockTimer", timeLockTimer);
		compound.setInteger("minuteMarkTimer", minuteMarkTimer);
		compound.setInteger("nameTimer", nameTimer);
		compound.setInteger("glowTimer", glowTimer);
		compound.setBoolean("pointControlled", controlled);
		compound.setInteger("uhcStartTimer", uhcStartTimer);
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
