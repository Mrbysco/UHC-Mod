package com.Mrbysco.UHC.init;

import com.blamejared.ctgui.reference.Reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class UHCSaveData extends WorldSavedData{

	private static final String DATA_NAME = Reference.MOD_ID + "_data";
	
	private boolean uhcOnGoing;
	
	private boolean friendlyFire;
	private boolean teamCollision;
	private boolean healthInTab;
	private boolean healthOnSide;
	private boolean healthUnderName;
	
	private boolean doDaylightCycle;
	private boolean autoCook;
	private boolean itemConversion;
	private boolean applyCustomHealth;
	private int maxHealth;
	
	private int randomTeamSize;
	private int maxTeamSize;
	
	private int difficulty;
	
	private int borderSize;
	private double borderCenterX;
	private double borderCenterZ;
	private double originalBorderCenterX;
	private double originalBorderCenterZ;
	
	private boolean shrinkEnabled;
	private int shrinkTimer;
	private int shrinkSize;
	private int shrinkOvertime;
	private String shrinkMode;

	public UHCSaveData(String name) {
		super(name);
		
		this.friendlyFire = true;
		this.teamCollision = true;
		this.healthInTab = true;
		this.healthOnSide = false;
		this.healthUnderName = false;
		
		this.uhcOnGoing = false;
		this.autoCook = false;
		this.itemConversion = false;
		this.applyCustomHealth = false;
		this.maxHealth = 20;
		
		this.randomTeamSize = 6;
		this.maxTeamSize = -1;
		this.difficulty = 3;
		
		this.borderSize = 2048;
		this.borderCenterX = -1;
		this.borderCenterZ = -1;
		this.originalBorderCenterX = -1;
		this.originalBorderCenterZ = -1;
		this.shrinkEnabled = false;
		this.shrinkTimer = 60;
		this.shrinkSize = 256;
		this.shrinkOvertime = 60;
		this.shrinkMode = "Shrink";
	}
	
	public UHCSaveData() {
		super(DATA_NAME);
		
		this.friendlyFire = true;
		this.teamCollision = true;
		this.healthInTab = true;
		this.healthOnSide = false;
		this.healthUnderName = false;

		this.uhcOnGoing = false;
		this.autoCook = false;
		this.itemConversion = false;
		this.applyCustomHealth = false;
		this.maxHealth = 20;
		
		this.randomTeamSize = 6;
		this.maxTeamSize = -1;
		this.difficulty = 3;
		
		this.borderSize = 2048;
		this.borderCenterX = -1;
		this.borderCenterZ = -1;
		this.originalBorderCenterX = -1;
		this.originalBorderCenterZ = -1;
		this.shrinkEnabled = false;
		this.shrinkTimer = 60;
		this.shrinkSize = 256;
		this.shrinkOvertime = 60;
		this.shrinkMode = "Shrink";
	}
	
	public void resetAll() {
		this.friendlyFire = true;
		this.teamCollision = true;
		this.healthInTab = true;
		this.healthOnSide = false;
		this.healthUnderName = false;

		this.uhcOnGoing = false;
		this.autoCook = false;
		this.itemConversion = false;
		this.applyCustomHealth = false;
		this.maxHealth = 20;
		
		this.randomTeamSize = 6;
		this.maxTeamSize = -1;
		this.difficulty = 3;
		
		this.borderSize = 2048;
		this.borderCenterX = -1;
		this.borderCenterZ = -1;
		this.shrinkEnabled = false;
		this.shrinkTimer = 60;
		this.shrinkSize = 256;
		this.shrinkOvertime = 60;
		this.shrinkMode = "Shrink";
	}
	public boolean isUhcOnGoing() {
		return uhcOnGoing;
	}
	
	public void setUhcOnGoing(boolean uhcOnGoing) {
		this.uhcOnGoing = uhcOnGoing;
	}
	
	public boolean isFriendlyFire() {
		return friendlyFire;
	}
	
	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}
	
	public boolean isTeamCollision() {
		return teamCollision;
	}
	
	public void setTeamCollision(boolean teamCollision) {
		this.teamCollision = teamCollision;
	}
	
	public boolean isHealthInTab() {
		return healthInTab;
	}
	
	public void setHealthInTab(boolean healthInTab) {
		this.healthInTab = healthInTab;
	}
	
	public boolean isHealthOnSide() {
		return healthOnSide;
	}
	
	public void setHealthOnSide(boolean healthOnSide) {
		this.healthOnSide = healthOnSide;
	}
	
	public boolean isHealthUnderName() {
		return healthUnderName;
	}
	
	public void setHealthUnderName(boolean healthUnderName) {
		this.healthUnderName = healthUnderName;
	}
	
	public boolean DoDaylightCycle() {
		return doDaylightCycle;
	}
	
	public void setDoDaylightCycle(boolean doDaylightCycle) {
		this.doDaylightCycle = doDaylightCycle;
	}
	
	public boolean isAutoCook() {
		return autoCook;
	}
	
	public void setAutoCook(boolean autoCook) {
		this.autoCook = autoCook;
	}
	
	public boolean isItemConversion() {
		return itemConversion;
	}
	
	public void setItemConversion(boolean itemConversion) {
		this.itemConversion = itemConversion;
	}
	
	public boolean isApplyCustomHealth() {
		return applyCustomHealth;
	}
	
	public void setApplyCustomHealth(boolean applyCustomHealth) {
		this.applyCustomHealth = applyCustomHealth;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getRandomTeamSize() {
		return randomTeamSize;
	}
	
	public void setRandomTeamSize(int randomTeamSize) {
		this.randomTeamSize = randomTeamSize;
	}
	
	public int getMaxTeamSize() {
		return maxTeamSize;
	}
	
	public void setMaxTeamSize(int maxTeamSize) {
		this.maxTeamSize = maxTeamSize;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getBorderSize() {
		return borderSize;
	}
	
	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}
	
	public double getBorderCenterX() {
		return borderCenterX;
	}
	
	public void setBorderCenterX(double borderCenterX) {
		this.borderCenterX = borderCenterX;
	}
	
	public double getBorderCenterZ() {
		return borderCenterZ;
	}
	
	public void setBorderCenterZ(double borderCenterZ) {
		this.borderCenterZ = borderCenterZ;
	}
	
	public double getOriginalBorderCenterX() {
		return originalBorderCenterX;
	}
	
	public void setOriginalBorderCenterX(double originalBorderCenterX) {
		this.originalBorderCenterX = originalBorderCenterX;
	}
	
	public double getOriginalBorderCenterZ() {
		return originalBorderCenterZ;
	}
	
	public void setOriginalBorderCenterZ(double originalBorderCenterZ) {
		this.originalBorderCenterZ = originalBorderCenterZ;
	}
	
	public boolean isShrinkEnabled() {
		return shrinkEnabled;
	}
	
	public void setShrinkEnabled(boolean shrinkEnabled) {
		this.shrinkEnabled = shrinkEnabled;
	}
	
	public int getShrinkTimer() {
		return shrinkTimer;
	}
	
	public void setShrinkTimer(int shrinkTimer) {
		this.shrinkTimer = shrinkTimer;
	}
	
	public int getShrinkSize() {
		return shrinkSize;
	}
	
	public void setShrinkSize(int shrinkSize) {
		this.shrinkSize = shrinkSize;
	}
	
	public int getShrinkOvertime() {
		return shrinkOvertime;
	}
	
	public void setShrinkOvertime(int shrinkOvertime) {
		this.shrinkOvertime = shrinkOvertime;
	}

	public String getShrinkMode() {
		return shrinkMode;
	}
	
	public void setShrinkMode(String shrinkMode) {
		this.shrinkMode = shrinkMode;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		uhcOnGoing = nbt.getBoolean("UHCStarted");
		autoCook = nbt.getBoolean("autoCook");
		itemConversion = nbt.getBoolean("itemConversion");
		applyCustomHealth = nbt.getBoolean("CustomHealthApplied");
		maxHealth = nbt.getInteger("maxHealth");
		
		friendlyFire = nbt.getBoolean("friendlyFire");
		teamCollision = nbt.getBoolean("teamCollision");
		healthInTab = nbt.getBoolean("healthInTab");
		healthOnSide = nbt.getBoolean("healthOnSide");
		healthUnderName = nbt.getBoolean("healthUnderName");
		
		randomTeamSize = nbt.getInteger("randomTeamSize");
		maxTeamSize = nbt.getInteger("maxTeamSize");
		difficulty = nbt.getInteger("difficulty");
		
		borderSize = nbt.getInteger("borderSize");
		borderCenterX = nbt.getDouble("borderCenterX");
		borderCenterZ = nbt.getDouble("borderCenterZ");
		originalBorderCenterX = nbt.getDouble("originalBorderCenterX");
		originalBorderCenterZ = nbt.getDouble("originalBorderCenterZ");
		
		shrinkEnabled = nbt.getBoolean("shrinkEnabled");
		shrinkTimer = nbt.getInteger("shrinkTimer");
		shrinkSize = nbt.getInteger("shrinkSize");
		shrinkOvertime = nbt.getInteger("shrinkOvertime");
		shrinkMode = nbt.getString("shrinkMode");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("UHCStarted", uhcOnGoing);
		compound.setBoolean("autoCook", autoCook);
		compound.setBoolean("itemConversion", itemConversion);
		compound.setBoolean("CustomHealthApplied", applyCustomHealth);
		compound.setInteger("maxHealth", maxHealth);
		
		compound.setBoolean("friendlyFire", friendlyFire);
		compound.setBoolean("teamCollision", teamCollision);
		compound.setBoolean("healthInTab", healthInTab);
		compound.setBoolean("healthOnSide", healthOnSide);
		compound.setBoolean("healthUnderName", healthUnderName);
		
		compound.setInteger("randomTeamSize", randomTeamSize);
		compound.setInteger("maxTeamSize", maxTeamSize);
		compound.setInteger("difficulty", difficulty);
		
		compound.setInteger("borderSize", borderSize);
		compound.setDouble("borderCenterX", borderCenterX);
		compound.setDouble("borderCenterZ", borderCenterZ);
		compound.setDouble("originalBorderCenterX", originalBorderCenterX);
		compound.setDouble("originalBorderCenterZ", originalBorderCenterZ);
		
		compound.setBoolean("shrinkEnabled", shrinkEnabled);
		compound.setInteger("shrinkTimer", shrinkTimer);
		compound.setInteger("shrinkSize", shrinkSize);
		compound.setInteger("shrinkOvertime", shrinkOvertime);
		compound.setString("shrinkMode", shrinkMode);

		return compound;
	}
	
	public static UHCSaveData getForWorld(World world) {
        MapStorage storage = world.getPerWorldStorage();
		UHCSaveData data = (UHCSaveData) storage.getOrLoadData(UHCSaveData.class, DATA_NAME);
		if (data == null) {
			data = new UHCSaveData();
			storage.setData(DATA_NAME, data);
		}
		return data;
	}
}
