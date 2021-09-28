package com.mrbysco.uhc.data;

import com.mrbysco.uhc.Reference;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class UHCSaveData extends WorldSavedData{

	private static final String DATA_NAME = Reference.MOD_ID + "_world_data";
	
	private boolean uhcStarting;
	private boolean uhcOnGoing;
	private boolean uhcIsFinished;
	private boolean uhcShowdownOnGoing;
	private boolean uhcShowdownFinished;
	private boolean teamsLocked;
	private ResourceLocation UHCDimension;

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
	private boolean shrinkApplied;

	private boolean timeLock;
	private int timeLockTimer;
	private boolean timeLockApplied;
	private String timeMode;
	
	private boolean minuteMark;
	private int minuteMarkTime;
	private boolean timedNames;
	private int nameTimer;
	private boolean timedNamesApplied;
	private boolean timedGlow;
	private int glowTime;
	private boolean glowTimeApplied;
	
	private boolean netherEnabled;
	private boolean regenPotions;
	private boolean level2Potions;
	private boolean notchApples;
	
	private boolean weatherEnabled;
	private boolean mobGriefing;
	
	private boolean randomSpawns;
	private int spreadDistance;
	private int spreadMaxRange;
	private boolean spreadRespectTeam;

	private boolean spawnRoom;
	private ResourceLocation spawnRoomDimension;
	
	private boolean graceEnabled;
	private int graceTime;
	private boolean graceFinished;
	private boolean twilightRespawn;

	public UHCSaveData(String name) {
		super(name);
		this.UHCDimension = new ResourceLocation("overworld");
		this.friendlyFire = true;
		this.teamCollision = true;
		this.healthInTab = true;
		this.healthOnSide = false;
		this.healthUnderName = false;
		
		this.uhcStarting = false;
		this.uhcOnGoing = false;
		this.uhcIsFinished = false;
		this.uhcShowdownOnGoing = false;
		this.uhcShowdownFinished = false;
		this.autoCook = false;
		this.itemConversion = false;
		this.applyCustomHealth = false;
		this.maxHealth = 20;
		
		this.randomTeamSize = 6;
		this.maxTeamSize = -1;
		this.difficulty = 3;
		
		this.borderSize = 2048;
		this.borderCenterX = Integer.MAX_VALUE;
		this.borderCenterZ = Integer.MAX_VALUE;
		this.originalBorderCenterX = Integer.MAX_VALUE;
		this.originalBorderCenterZ = Integer.MAX_VALUE;
		
		this.shrinkEnabled = false;
		this.shrinkTimer = 60;
		this.shrinkSize = 256;
		this.shrinkOvertime = 60;
		this.shrinkMode = "Shrink";
		this.shrinkApplied = false;
		
		this.timeLock = false;
		this.timeLockTimer = 60;
		this.timeLockApplied = false;
		this.timeMode = "Day";
		
		this.minuteMark = false;
		this.minuteMarkTime = 30;
		this.timedNames = false;
		this.nameTimer = 30;
		this.timedNamesApplied = false;
		this.timedGlow = false;
		this.glowTime = 30;
		this.glowTimeApplied = false;
		
		this.netherEnabled = true;
		this.regenPotions = true;
		this.level2Potions = true;
		this.notchApples = true;
		
		this.weatherEnabled = true;
		this.mobGriefing = true;
		
		this.randomSpawns = true;
		this.spreadDistance = 100;
		this.spreadMaxRange = 2048;
		this.spreadRespectTeam = true;
		
		this.spawnRoom = false;
		this.spawnRoomDimension = new ResourceLocation("overworld");
		
		this.graceEnabled = false;
		this.graceTime = 20;
		this.graceFinished = false;
		this.twilightRespawn = false;
		this.teamsLocked = false;
	}

	public UHCSaveData() {
		super(DATA_NAME);

		this.UHCDimension = new ResourceLocation("overworld");
		this.friendlyFire = true;
		this.teamCollision = true;
		this.healthInTab = true;
		this.healthOnSide = false;
		this.healthUnderName = false;

		this.uhcStarting = false;
		this.uhcOnGoing = false;
		this.uhcIsFinished = false;
		this.uhcShowdownOnGoing = false;
		this.uhcShowdownFinished = false;
		this.autoCook = false;
		this.itemConversion = false;
		this.applyCustomHealth = false;
		this.maxHealth = 20;
		
		this.randomTeamSize = 6;
		this.maxTeamSize = -1;
		this.difficulty = 3;
		
		this.borderSize = 2048;
		this.borderCenterX = Integer.MAX_VALUE;
		this.borderCenterZ = Integer.MAX_VALUE;
		this.originalBorderCenterX = Integer.MAX_VALUE;
		this.originalBorderCenterZ = Integer.MAX_VALUE;
		
		this.shrinkEnabled = false;
		this.shrinkTimer = 60;
		this.shrinkSize = 256;
		this.shrinkOvertime = 60;
		this.shrinkMode = "Shrink";
		this.shrinkApplied = false;
		
		this.timeLock = false;
		this.timeLockTimer = 60;
		this.timeLockApplied = false;
		this.timeMode = "Day";
		
		this.minuteMark = false;
		this.minuteMarkTime = 30;
		this.timedNames = false;
		this.nameTimer = 30;
		this.timedNamesApplied = false;
		this.timedGlow = false;
		this.glowTime = 30;
		this.glowTimeApplied = false;
		
		this.netherEnabled = true;
		this.regenPotions = true;
		this.level2Potions = true;
		this.notchApples = true;
		
		this.weatherEnabled = true;
		this.mobGriefing = true;
		
		this.randomSpawns = true;
		this.spreadDistance = 100;
		this.spreadMaxRange = 2048;
		this.spreadRespectTeam = true;
		
		this.spawnRoom = false;
		this.spawnRoomDimension = new ResourceLocation("overworld");
		
		this.graceEnabled = false;
		this.graceTime = 20;
		this.graceFinished = false;
		this.twilightRespawn = false;
		this.teamsLocked = false;
	}
	
	public void resetAll() {
		this.UHCDimension = new ResourceLocation("overworld");
		this.friendlyFire = true;
		this.teamCollision = true;
		this.healthInTab = true;
		this.healthOnSide = false;
		this.healthUnderName = false;

		this.uhcStarting = false;
		this.uhcOnGoing = false;
		this.uhcIsFinished = false;
		this.uhcShowdownOnGoing = false;
		this.uhcShowdownFinished = false;
		this.autoCook = false;
		this.itemConversion = false;
		this.applyCustomHealth = false;
		this.maxHealth = 20;
		
		this.randomTeamSize = 6;
		this.maxTeamSize = -1;
		this.difficulty = 3;
		
		this.borderSize = 2048;
		this.borderCenterX = Integer.MAX_VALUE;
		this.borderCenterZ = Integer.MAX_VALUE;
		
		this.shrinkEnabled = false;
		this.shrinkTimer = 60;
		this.shrinkSize = 256;
		this.shrinkOvertime = 60;
		this.shrinkMode = "Shrink";
		this.shrinkApplied = false;
		
		this.timeLock = false;
		this.timeLockTimer = 60;
		this.timeLockApplied = false;
		this.timeMode = "Day";
		
		this.minuteMark = false;
		this.minuteMarkTime = 30;
		this.timedNames = false;
		this.nameTimer = 30;
		this.timedNamesApplied = false;
		this.timedGlow = false;
		this.glowTime = 30;
		this.glowTimeApplied = false;
		
		this.netherEnabled = true;
		this.regenPotions = true;
		this.level2Potions = true;
		this.notchApples = true;
		
		this.weatherEnabled = true;
		this.mobGriefing = true;
		
		this.randomSpawns = true;
		this.spreadDistance = 100;
		this.spreadMaxRange = 2048;
		this.spreadRespectTeam = true;
		
		this.graceEnabled = false;
		this.graceTime = 20;
		this.graceFinished = false;
		this.twilightRespawn = false;
		this.teamsLocked = false;
	}
	
	public boolean isUhcStarting() {
		return uhcStarting;
	}
	
	public void setUhcStarting(boolean uhcStarting) {
		this.uhcStarting = uhcStarting;
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
	
	public boolean isShrinkApplied() {
		return shrinkApplied;
	}
	
	public void setShrinkApplied(boolean shrinkApplied) {
		this.shrinkApplied = shrinkApplied;
	}
	
	public boolean isTimeLock() {
		return timeLock;
	}
	
	public void setTimeLock(boolean timeLock) {
		this.timeLock = timeLock;
	}
	
	public int getTimeLockTimer() {
		return timeLockTimer;
	}
	
	public void setTimeLockTimer(int timeLockTimer) {
		this.timeLockTimer = timeLockTimer;
	}
	
	public String getTimeMode() {
		return timeMode;
	}
	
	public void setTimeMode(String timeMode) {
		this.timeMode = timeMode;
	}
	
	public boolean isMinuteMark() {
		return minuteMark;
	}
	
	public void setMinuteMark(boolean minuteMark) {
		this.minuteMark = minuteMark;
	}
	
	public int getMinuteMarkTime() {
		return minuteMarkTime;
	}
	
	public void setMinuteMarkTime(int minuteMarkTime) {
		this.minuteMarkTime = minuteMarkTime;
	}
	
	public boolean isNetherEnabled() {
		return netherEnabled;
	}
	
	public void setNetherEnabled(boolean netherEnabled) {
		this.netherEnabled = netherEnabled;
	}
	
	public boolean isRegenPotions() {
		return regenPotions;
	}
	
	public void setRegenPotions(boolean regenPotions) {
		this.regenPotions = regenPotions;
	}
	
	public boolean isLevel2Potions() {
		return level2Potions;
	}
	
	public void setLevel2Potions(boolean level2Potions) {
		this.level2Potions = level2Potions;
	}
	
	public boolean isNotchApples() {
		return notchApples;
	}
	
	public void setNotchApples(boolean notchApples) {
		this.notchApples = notchApples;
	}
	
	public boolean isTimedNames() {
		return timedNames;
	}
	
	public void setTimedNames(boolean timedNames) {
		this.timedNames = timedNames;
	}
	
	public int getNameTimer() {
		return nameTimer;
	}
	
	public void setNameTimer(int nameTimer) {
		this.nameTimer = nameTimer;
	}
	
	public boolean isTimedGlow() {
		return timedGlow;
	}
	
	public void setTimedGlow(boolean timedGlow) {
		this.timedGlow = timedGlow;
	}
	
	public int getGlowTime() {
		return glowTime;
	}
	
	public void setGlowTime(int glowTime) {
		this.glowTime = glowTime;
	}
	
	public boolean isWeatherEnabled() {
		return weatherEnabled;
	}
	
	public void setWeatherEnabled(boolean weatherEnabled) {
		this.weatherEnabled = weatherEnabled;
	}
	
	public boolean isMobGriefing() {
		return mobGriefing;
	}
	
	public void setMobGriefing(boolean mobGriefing) {
		this.mobGriefing = mobGriefing;
	}
	
	public boolean isRandomSpawns() {
		return randomSpawns;
	}
	
	public void setRandomSpawns(boolean randomSpawns) {
		this.randomSpawns = randomSpawns;
	}
	
	public int getSpreadDistance() {
		return spreadDistance;
	}
	
	public void setSpreadDistance(int spreadDistance) {
		this.spreadDistance = spreadDistance;
	}
	
	public int getSpreadMaxRange() {
		return spreadMaxRange;
	}
	
	public void setSpreadMaxRange(int spreadMaxRange) {
		this.spreadMaxRange = spreadMaxRange;
	}
	
	public boolean isSpreadRespectTeam() {
		return spreadRespectTeam;
	}
	
	public void setSpreadRespectTeam(boolean spreadRespectTeam) {
		this.spreadRespectTeam = spreadRespectTeam;
	}
	
	public boolean isSpawnRoom() {
		return spawnRoom;
	}
	
	public void setSpawnRoom(boolean spawnRoom) {
		this.spawnRoom = spawnRoom;
	}
	
	public ResourceLocation getSpawnRoomDimension() {
		return spawnRoomDimension;
	}
	
	public void setSpawnRoomDimension(ResourceLocation spawnRoomDimension) {
		this.spawnRoomDimension = spawnRoomDimension;
	}
	
	public boolean isUhcIsFinished() {
		return uhcIsFinished;
	}
	
	public void setUhcIsFinished(boolean uhcIsFinished) {
		this.uhcIsFinished = uhcIsFinished;
	}
	
	public boolean isUhcShowdown() {
		return uhcShowdownOnGoing;
	}
	
	public void setUhcShowdown(boolean uhcShowdownOnGoing) {
		this.uhcShowdownOnGoing = uhcShowdownOnGoing;
	}
	
	public boolean isUhcShowdownFinished() {
		return uhcShowdownFinished;
	}
	
	public void setUhcShowdownFinished(boolean uhcShowdownFinished) {
		this.uhcShowdownFinished = uhcShowdownFinished;
	}
	
	public boolean isTimedNamesApplied() {
		return timedNamesApplied;
	}
	
	public void setTimedNamesApplied(boolean timedNamesApplied) {
		this.timedNamesApplied = timedNamesApplied;
	}
	
	public boolean isGlowTimeApplied() {
		return glowTimeApplied;
	}
	
	public void setGlowTimeApplied(boolean glowTimeApplied) {
		this.glowTimeApplied = glowTimeApplied;
	}
	
	public boolean isTimeLockApplied() {
		return timeLockApplied;
	}
	
	public void setTimeLockApplied(boolean timeLockApplied) {
		this.timeLockApplied = timeLockApplied;
	}
	
	public ResourceLocation getUHCDimension() {
		return UHCDimension;
	}
	
	public void setUHCDimension(ResourceLocation uHCDimension) {
		UHCDimension = uHCDimension;
	}
	
	public boolean getTwilightRespawn() {
		return twilightRespawn;
	}
	
	public void setTwilightRespawn(boolean twilightRespawn) {
		this.twilightRespawn = twilightRespawn;
	}
	
	public boolean isGraceEnabled() {
		return graceEnabled;
	}
	
	public void setGraceEnabled(boolean graceEnabled) {
		this.graceEnabled = graceEnabled;
	}
	
	public int getGraceTime() {
		return graceTime;
	}
	
	public void setGraceTime(int graceTime) {
		this.graceTime = graceTime;
	}
	
	public boolean isGraceFinished() {
		return graceFinished;
	}
	
	public void setGraceFinished(boolean graceFinished) {
		this.graceFinished = graceFinished;
	}
	
	public boolean areTeamsLocked() {
		return teamsLocked;
	}
	
	public void setTeamsLocked(boolean teamsLocked) {
		this.teamsLocked = teamsLocked;
	}
	
	@Override
	public void read(CompoundNBT nbt) {
		uhcStarting = nbt.getBoolean("uhcStarting");
		uhcOnGoing = nbt.getBoolean("uhcOnGoing");
		uhcIsFinished = nbt.getBoolean("uhcIsFinished");
		uhcShowdownOnGoing = nbt.getBoolean("uhcShowdownOnGoing");
		uhcShowdownFinished = nbt.getBoolean("uhcShowdownFinished");
		autoCook = nbt.getBoolean("autoCook");
		itemConversion = nbt.getBoolean("itemConversion");
		applyCustomHealth = nbt.getBoolean("CustomHealthApplied");
		maxHealth = nbt.getInt("maxHealth");
		
		friendlyFire = nbt.getBoolean("friendlyFire");
		teamCollision = nbt.getBoolean("teamCollision");
		healthInTab = nbt.getBoolean("healthInTab");
		healthOnSide = nbt.getBoolean("healthOnSide");
		healthUnderName = nbt.getBoolean("healthUnderName");
		
		randomTeamSize = nbt.getInt("randomTeamSize");
		maxTeamSize = nbt.getInt("maxTeamSize");
		difficulty = nbt.getInt("difficulty");
		
		borderSize = nbt.getInt("borderSize");
		borderCenterX = nbt.getDouble("borderCenterX");
		borderCenterZ = nbt.getDouble("borderCenterZ");
		originalBorderCenterX = nbt.getDouble("originalBorderCenterX");
		originalBorderCenterZ = nbt.getDouble("originalBorderCenterZ");
		
		shrinkEnabled = nbt.getBoolean("shrinkEnabled");
		shrinkTimer = nbt.getInt("shrinkTimer");
		shrinkSize = nbt.getInt("shrinkSize");
		shrinkOvertime = nbt.getInt("shrinkOvertime");
		shrinkMode = nbt.getString("shrinkMode");
		shrinkApplied = nbt.getBoolean("shrinkApplied");
		
		timeLock = nbt.getBoolean("timeLock");
		timeLockTimer = nbt.getInt("timeLockTimer");
		timeLockApplied = nbt.getBoolean("timeLockApplied");
		timeMode = nbt.getString("timeMode");
		
		minuteMark = nbt.getBoolean("minuteMark");
		minuteMarkTime = nbt.getInt("minuteMarkTime");
		timedNames = nbt.getBoolean("timedNames");
		nameTimer = nbt.getInt("nameTimer");
		timedNamesApplied = nbt.getBoolean("timedNamesApplied");
		timedGlow = nbt.getBoolean("timedGlow");
		glowTime = nbt.getInt("glowTime");
		glowTimeApplied = nbt.getBoolean("glowTimeApplied");

		netherEnabled = nbt.getBoolean("netherEnabled");
		regenPotions = nbt.getBoolean("regenPotions");
		level2Potions = nbt.getBoolean("level2Potions");
		notchApples = nbt.getBoolean("notchApples");
		
		weatherEnabled = nbt.getBoolean("weatherEnabled");
		mobGriefing = nbt.getBoolean("mobGriefing");
		
		randomSpawns = nbt.getBoolean("randomSpawns");
		spreadDistance = nbt.getInt("spreadDistance");
		spreadMaxRange = nbt.getInt("spreadMaxRange");
		spreadRespectTeam = nbt.getBoolean("spreadRespectTeam");

		spawnRoom = nbt.getBoolean("spawnRoom");
		spawnRoomDimension = ResourceLocation.tryCreate(nbt.getString("spawnRoomDimension"));
		
		UHCDimension = ResourceLocation.tryCreate(nbt.getString("UHCDimension"));
		
		graceEnabled = nbt.getBoolean("graceEnabled");
		graceTime = nbt.getInt("graceTime");
		graceFinished = nbt.getBoolean("graceFinished");
		twilightRespawn = nbt.getBoolean("twilightRespawn");
		teamsLocked = nbt.getBoolean("teamsLocked");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putBoolean("uhcStarting", uhcStarting);
		compound.putBoolean("uhcOnGoing", uhcOnGoing);
		compound.putBoolean("uhcIsFinished", uhcIsFinished);
		compound.putBoolean("uhcShowdownOnGoing", uhcShowdownOnGoing);
		compound.putBoolean("uhcShowdownFinished", uhcShowdownFinished);
		compound.putBoolean("autoCook", autoCook);
		compound.putBoolean("itemConversion", itemConversion);
		compound.putBoolean("CustomHealthApplied", applyCustomHealth);
		compound.putInt("maxHealth", maxHealth);
		
		compound.putBoolean("friendlyFire", friendlyFire);
		compound.putBoolean("teamCollision", teamCollision);
		compound.putBoolean("healthInTab", healthInTab);
		compound.putBoolean("healthOnSide", healthOnSide);
		compound.putBoolean("healthUnderName", healthUnderName);
		
		compound.putInt("randomTeamSize", randomTeamSize);
		compound.putInt("maxTeamSize", maxTeamSize);
		compound.putInt("difficulty", difficulty);
		
		compound.putInt("borderSize", borderSize);
		compound.putDouble("borderCenterX", borderCenterX);
		compound.putDouble("borderCenterZ", borderCenterZ);
		compound.putDouble("originalBorderCenterX", originalBorderCenterX);
		compound.putDouble("originalBorderCenterZ", originalBorderCenterZ);
		
		compound.putBoolean("shrinkEnabled", shrinkEnabled);
		compound.putInt("shrinkTimer", shrinkTimer);
		compound.putInt("shrinkSize", shrinkSize);
		compound.putInt("shrinkOvertime", shrinkOvertime);
		compound.putString("shrinkMode", shrinkMode);
		compound.putBoolean("shrinkApplied", shrinkApplied);

		compound.putBoolean("timeLock", timeLock);
		compound.putInt("timeLockTimer", timeLockTimer);
		compound.putBoolean("timeLockApplied", timeLockApplied);
		compound.putString("timeMode", timeMode);
		
		compound.putBoolean("minuteMark", minuteMark);
		compound.putInt("minuteMarkTime", minuteMarkTime);
		compound.putBoolean("timedNames", timedNames);
		compound.putInt("nameTimer", nameTimer);
		compound.putBoolean("timedNamesApplied", timedNamesApplied);
		compound.putBoolean("timedGlow", timedGlow);
		compound.putInt("glowTime", glowTime);
		compound.putBoolean("glowTimeApplied", glowTimeApplied);
		
		compound.putBoolean("netherEnabled", netherEnabled);
		compound.putBoolean("regenPotions", regenPotions);
		compound.putBoolean("level2Potions", level2Potions);
		compound.putBoolean("notchApples", notchApples);

		compound.putBoolean("weatherEnabled", weatherEnabled);
		compound.putBoolean("mobGriefing", mobGriefing);
		
		compound.putBoolean("randomSpawns", randomSpawns);
		compound.putInt("spreadDistance", spreadDistance);
		compound.putInt("spreadMaxRange", spreadMaxRange);
		compound.putBoolean("spreadRespectTeam", spreadRespectTeam);
		
		compound.putBoolean("spawnRoom", spawnRoom);
		compound.putString("spawnRoomDimension", spawnRoomDimension.toString());

		compound.putString("UHCDimension", UHCDimension.toString());
		
		compound.putBoolean("graceEnabled", graceEnabled);
		compound.putInt("graceTime", graceTime);
		compound.putBoolean("graceFinished", graceFinished);
		compound.putBoolean("twilightRespawn", twilightRespawn);
		compound.putBoolean("teamsLocked", teamsLocked);
		
		return compound;
	}

	public static UHCSaveData get(World world) {
		if (!(world instanceof ServerWorld)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);

		DimensionSavedDataManager storage = overworld.getSavedData();
		return storage.getOrCreate(UHCSaveData::new, DATA_NAME);
	}
}
