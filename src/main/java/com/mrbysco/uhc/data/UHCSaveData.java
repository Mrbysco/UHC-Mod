package com.mrbysco.uhc.data;

import com.mrbysco.uhc.Reference;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class UHCSaveData extends SavedData {
	private static final String DATA_NAME = Reference.MOD_ID + "_world_data";

	private final CompoundTag data;

	public UHCSaveData(CompoundTag data) {
		this.data = data;
	}

	public UHCSaveData() {
		this(getDefaultTag());
	}

	private static CompoundTag getDefaultTag() {
		CompoundTag tag = new CompoundTag();

		tag.putBoolean("friendlyFire", true);
		tag.putBoolean("teamCollision", true);
		tag.putBoolean("healthInTab", true);
		tag.putBoolean("healthOnSide", false);
		tag.putBoolean("healthUnderName", false);

		tag.putBoolean("uhcStarting", false);
		tag.putBoolean("uhcOnGoing", false);
		tag.putBoolean("uhcIsFinished", false);
		tag.putBoolean("uhcShowdownOnGoing", false);
		tag.putBoolean("uhcShowdownFinished", false);
		tag.putBoolean("autoCook", false);
		tag.putBoolean("itemConversion", false);
		tag.putBoolean("applyCustomHealth", false);
		tag.putInt("maxHealth", 20);

		tag.putInt("randomTeamSize", 6);
		tag.putInt("maxTeamSize", -1);
		tag.putInt("difficulty", 3);

		tag.putInt("borderSize", 2048);
		tag.putDouble("borderCenterX", Integer.MAX_VALUE);
		tag.putDouble("borderCenterZ", Integer.MAX_VALUE);
		tag.putDouble("originalBorderCenterX", Integer.MAX_VALUE);
		tag.putDouble("originalBorderCenterZ", Integer.MAX_VALUE);

		tag.putBoolean("shrinkEnabled", false);
		tag.putInt("shrinkTimer", 60);
		tag.putInt("shrinkSize", 256);
		tag.putInt("shrinkOvertime", 60);
		tag.putString("shrinkMode", "Shrink");
		tag.putBoolean("shrinkApplied", false);

		tag.putBoolean("timeLock", false);
		tag.putInt("timeLockTimer", 60);
		tag.putBoolean("timeLockApplied", false);
		tag.putString("timeMode", "Day");

		tag.putBoolean("minuteMark", false);
		tag.putInt("minuteMarkTime", 30);
		tag.putBoolean("timedNames", false);
		tag.putInt("nameTimer", 30);
		tag.putBoolean("timedNamesApplied", false);
		tag.putBoolean("timedGlow", false);
		tag.putInt("glowTime", 30);
		tag.putBoolean("glowTimeApplied", false);

		tag.putBoolean("netherEnabled", true);
		tag.putBoolean("regenPotions", true);
		tag.putBoolean("level2Potions", true);
		tag.putBoolean("notchApples", true);

		tag.putBoolean("weatherEnabled", true);
		tag.putBoolean("mobGriefing", true);

		tag.putBoolean("randomSpawns", true);
		tag.putInt("spreadDistance", 100);
		tag.putInt("spreadMaxRange", 2048);
		tag.putBoolean("spreadRespectTeam", true);

		tag.putBoolean("spawnRoom", false);
		tag.putString("spawnRoomDimension", "minecraft:overworld");

		tag.putBoolean("graceEnabled", false);
		tag.putInt("graceTime", 20);
		tag.putBoolean("graceFinished", false);
		tag.putBoolean("twilightRespawn", false);
		tag.putBoolean("teamsLocked", false);

		return tag;
	}

	public void resetAll(RegistryAccess registryAccess) {
		this.data.putBoolean("friendlyFire", true);
		this.data.putBoolean("teamCollision", true);
		this.data.putBoolean("healthInTab", true);
		this.data.putBoolean("healthOnSide", false);
		this.data.putBoolean("healthUnderName", false);

		this.data.putBoolean("uhcStarting", false);
		this.data.putBoolean("uhcOnGoing", false);
		this.data.putBoolean("uhcIsFinished", false);
		this.data.putBoolean("uhcShowdownOnGoing", false);
		this.data.putBoolean("uhcShowdownFinished", false);
		this.data.putBoolean("autoCook", false);
		this.data.putBoolean("itemConversion", false);
		this.data.putBoolean("applyCustomHealth", false);
		this.data.putInt("maxHealth", 20);

		this.data.putInt("randomTeamSize", 6);
		this.data.putInt("maxTeamSize", -1);
		this.data.putInt("difficulty", 3);

		this.data.putInt("borderSize", 2048);
		this.data.putDouble("borderCenterX", Integer.MAX_VALUE);
		this.data.putDouble("borderCenterZ", Integer.MAX_VALUE);
		this.data.putDouble("originalBorderCenterX", Integer.MAX_VALUE);
		this.data.putDouble("originalBorderCenterZ", Integer.MAX_VALUE);

		this.data.putBoolean("shrinkEnabled", false);
		this.data.putInt("shrinkTimer", 60);
		this.data.putInt("shrinkSize", 256);
		this.data.putInt("shrinkOvertime", 60);
		this.data.putString("shrinkMode", "Shrink");
		this.data.putBoolean("shrinkApplied", false);

		this.data.putBoolean("timeLock", false);
		this.data.putInt("timeLockTimer", 60);
		this.data.putBoolean("timeLockApplied", false);
		this.data.putString("timeMode", "Day");

		this.data.putBoolean("minuteMark", false);
		this.data.putInt("minuteMarkTime", 30);
		this.data.putBoolean("timedNames", false);
		this.data.putInt("nameTimer", 30);
		this.data.putBoolean("timedNamesApplied", false);
		this.data.putBoolean("timedGlow", false);
		this.data.putInt("glowTime", 30);
		this.data.putBoolean("glowTimeApplied", false);

		this.data.putBoolean("netherEnabled", true);
		this.data.putBoolean("regenPotions", true);
		this.data.putBoolean("level2Potions", true);
		this.data.putBoolean("notchApples", true);

		this.data.putBoolean("weatherEnabled", true);
		this.data.putBoolean("mobGriefing", true);

		this.data.putBoolean("randomSpawns", true);
		this.data.putInt("spreadDistance", 100);
		this.data.putInt("spreadMaxRange", 2048);
		this.data.putBoolean("spreadRespectTeam", true);

		this.data.putBoolean("spawnRoom", false);
		this.data.putString("spawnRoomDimension", "minecraft:overworld");

		this.data.putBoolean("graceEnabled", false);
		this.data.putInt("graceTime", 20);
		this.data.putBoolean("graceFinished", false);
		this.data.putBoolean("twilightRespawn", false);
		this.data.putBoolean("teamsLocked", false);
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		tag.put("data", data);
		return tag;
	}

	public static UHCSaveData load(CompoundTag tag, HolderLookup.Provider provider) {
		return new UHCSaveData(tag.getCompound("data"));
	}

	public boolean isUhcStarting() {
		return this.data.getBoolean("uhcStarting");
	}

	public void setUhcStarting(boolean uhcStarting) {
		this.data.putBoolean("uhcStarting", uhcStarting);
	}

	public boolean isUhcOnGoing() {
		return this.data.getBoolean("uhcOnGoing");
	}

	public void setUhcOnGoing(boolean uhcOnGoing) {
		this.data.putBoolean("uhcOnGoing", uhcOnGoing);
	}

	public boolean isFriendlyFire() {
		return this.data.getBoolean("friendlyFire");
	}

	public void setFriendlyFire(boolean friendlyFire) {
		this.data.putBoolean("friendlyFire", friendlyFire);
	}

	public boolean isTeamCollision() {
		return this.data.getBoolean("teamCollision");
	}

	public void setTeamCollision(boolean teamCollision) {
		this.data.putBoolean("teamCollision", teamCollision);
	}

	public boolean isHealthInTab() {
		return this.data.getBoolean("healthInTab");
	}

	public void setHealthInTab(boolean healthInTab) {
		this.data.putBoolean("healthInTab", healthInTab);
	}

	public boolean isHealthOnSide() {
		return this.data.getBoolean("healthOnSide");
	}

	public void setHealthOnSide(boolean healthOnSide) {
		this.data.putBoolean("healthOnSide", healthOnSide);
	}

	public boolean isHealthUnderName() {
		return this.data.getBoolean("healthUnderName");
	}

	public void setHealthUnderName(boolean healthUnderName) {
		this.data.putBoolean("healthUnderName", healthUnderName);
	}

	public boolean doDaylightCycle() {
		return this.data.getBoolean("doDaylightCycle");
	}

	public void setDoDaylightCycle(boolean doDaylightCycle) {
		this.data.putBoolean("doDaylightCycle", doDaylightCycle);
	}

	public boolean isAutoCookEnabled() {
		return this.data.getBoolean("autoCook");
	}

	public void setAutoCook(boolean autoCook) {
		this.data.putBoolean("autoCook", autoCook);
	}

	public boolean isItemConversion() {
		return this.data.getBoolean("itemConversion");
	}

	public void setItemConversion(boolean itemConversion) {
		this.data.putBoolean("itemConversion", itemConversion);
	}

	public boolean isApplyCustomHealth() {
		return this.data.getBoolean("applyCustomHealth");
	}

	public void setApplyCustomHealth(boolean applyCustomHealth) {
		this.data.putBoolean("applyCustomHealth", applyCustomHealth);
	}

	public int getMaxHealth() {
		return this.data.getInt("maxHealth");
	}

	public void setMaxHealth(int maxHealth) {
		this.data.putInt("maxHealth", maxHealth);
	}

	public int getRandomTeamSize() {
		return this.data.getInt("randomTeamSize");
	}

	public void setRandomTeamSize(int randomTeamSize) {
		this.data.putInt("randomTeamSize", randomTeamSize);
	}

	public int getMaxTeamSize() {
		return this.data.getInt("maxTeamSize");
	}

	public void setMaxTeamSize(int maxTeamSize) {
		this.data.putInt("maxTeamSize", maxTeamSize);
	}

	public int getDifficulty() {
		return this.data.getInt("difficulty");
	}

	public void setDifficulty(int difficulty) {
		this.data.putInt("difficulty", difficulty);
	}

	public int getBorderSize() {
		return this.data.getInt("borderSize");
	}

	public void setBorderSize(int borderSize) {
		this.data.putInt("borderSize", borderSize);
	}

	public double getBorderCenterX() {
		return this.data.getDouble("borderCenterX");
	}

	public void setBorderCenterX(double borderCenterX) {
		this.data.putDouble("borderCenterX", borderCenterX);
	}

	public double getBorderCenterZ() {
		return this.data.getDouble("borderCenterZ");
	}

	public void setBorderCenterZ(double borderCenterZ) {
		this.data.putDouble("borderCenterZ", borderCenterZ);
	}

	public double getOriginalBorderCenterX() {
		return this.data.getDouble("originalBorderCenterX");
	}

	public void setOriginalBorderCenterX(double originalBorderCenterX) {
		this.data.putDouble("originalBorderCenterX", originalBorderCenterX);
	}

	public double getOriginalBorderCenterZ() {
		return this.data.getDouble("originalBorderCenterZ");
	}

	public void setOriginalBorderCenterZ(double originalBorderCenterZ) {
		this.data.putDouble("originalBorderCenterZ", originalBorderCenterZ);
	}

	public boolean isShrinkEnabled() {
		return this.data.getBoolean("shrinkEnabled");
	}

	public void setShrinkEnabled(boolean shrinkEnabled) {
		this.data.putBoolean("shrinkEnabled", shrinkEnabled);
	}

	public int getShrinkTimer() {
		return this.data.getInt("shrinkTimer");
	}

	public void setShrinkTimer(int shrinkTimer) {
		this.data.putInt("shrinkTimer", shrinkTimer);
	}

	public int getShrinkSize() {
		return this.data.getInt("shrinkSize");
	}

	public void setShrinkSize(int shrinkSize) {
		this.data.putInt("shrinkSize", shrinkSize);
	}

	public int getShrinkOvertime() {
		return this.data.getInt("shrinkOvertime");
	}

	public void setShrinkOvertime(int shrinkOvertime) {
		this.data.putInt("shrinkOvertime", shrinkOvertime);
	}

	public String getShrinkMode() {
		return this.data.getString("shrinkMode");
	}

	public void setShrinkMode(String shrinkMode) {
		this.data.putString("shrinkMode", shrinkMode);
	}

	public boolean isShrinkApplied() {
		return this.data.getBoolean("shrinkApplied");
	}

	public void setShrinkApplied(boolean shrinkApplied) {
		this.data.putBoolean("shrinkApplied", shrinkApplied);
	}

	public boolean isTimeLock() {
		return this.data.getBoolean("timeLock");
	}

	public void setTimeLock(boolean timeLock) {
		this.data.putBoolean("timeLock", timeLock);
	}

	public int getTimeLockTimer() {
		return this.data.getInt("timeLockTimer");
	}

	public void setTimeLockTimer(int timeLockTimer) {
		this.data.putInt("timeLockTimer", timeLockTimer);
	}

	public String getTimeMode() {
		return this.data.getString("timeMode");
	}

	public void setTimeMode(String timeMode) {
		this.data.putString("timeMode", timeMode);
	}

	public boolean isMinuteMark() {
		return this.data.getBoolean("minuteMark");
	}

	public void setMinuteMark(boolean minuteMark) {
		this.data.putBoolean("minuteMark", minuteMark);
	}

	public int getMinuteMarkTime() {
		return this.data.getInt("minuteMarkTime");
	}

	public void setMinuteMarkTime(int minuteMarkTime) {
		this.data.putInt("minuteMarkTime", minuteMarkTime);
	}

	public boolean isNetherEnabled() {
		return this.data.getBoolean("netherEnabled");
	}

	public void setNetherEnabled(boolean netherEnabled) {
		this.data.putBoolean("netherEnabled", netherEnabled);
	}

	public boolean isRegenPotions() {
		return this.data.getBoolean("regenPotions");
	}

	public void setRegenPotions(boolean regenPotions) {
		this.data.putBoolean("regenPotions", regenPotions);
	}

	public boolean isLevel2Potions() {
		return this.data.getBoolean("level2Potions");
	}

	public void setLevel2Potions(boolean level2Potions) {
		this.data.putBoolean("level2Potions", level2Potions);
	}

	public boolean isNotchApples() {
		return this.data.getBoolean("notchApples");
	}

	public void setNotchApples(boolean notchApples) {
		this.data.putBoolean("notchApples", notchApples);
	}

	public boolean isTimedNames() {
		return this.data.getBoolean("timedNames");
	}

	public void setTimedNames(boolean timedNames) {
		this.data.putBoolean("timedNames", timedNames);
	}

	public int getNameTimer() {
		return this.data.getInt("nameTimer");
	}

	public void setNameTimer(int nameTimer) {
		this.data.putInt("nameTimer", nameTimer);
	}

	public boolean isTimedGlow() {
		return this.data.getBoolean("timedGlow");
	}

	public void setTimedGlow(boolean timedGlow) {
		this.data.putBoolean("timedGlow", timedGlow);
	}

	public int getGlowTime() {
		return this.data.getInt("glowTime");
	}

	public void setGlowTime(int glowTime) {
		this.data.putInt("glowTime", glowTime);
	}

	public boolean isWeatherEnabled() {
		return this.data.getBoolean("weatherEnabled");
	}

	public void setWeatherEnabled(boolean weatherEnabled) {
		this.data.putBoolean("weatherEnabled", weatherEnabled);
	}

	public boolean isMobGriefing() {
		return this.data.getBoolean("mobGriefing");
	}

	public void setMobGriefing(boolean mobGriefing) {
		this.data.putBoolean("mobGriefing", mobGriefing);
	}

	public boolean isRandomSpawns() {
		return this.data.getBoolean("randomSpawns");
	}

	public void setRandomSpawns(boolean randomSpawns) {
		this.data.putBoolean("randomSpawns", randomSpawns);
	}

	public int getSpreadDistance() {
		return this.data.getInt("spreadDistance");
	}

	public void setSpreadDistance(int spreadDistance) {
		this.data.putInt("spreadDistance", spreadDistance);
	}

	public int getSpreadMaxRange() {
		return this.data.getInt("spreadMaxRange");
	}

	public void setSpreadMaxRange(int spreadMaxRange) {
		this.data.putInt("spreadMaxRange", spreadMaxRange);
	}

	public boolean isSpreadRespectTeam() {
		return this.data.getBoolean("spreadRespectTeam");
	}

	public void setSpreadRespectTeam(boolean spreadRespectTeam) {
		this.data.putBoolean("spreadRespectTeam", spreadRespectTeam);
	}

	public boolean isSpawnRoom() {
		return this.data.getBoolean("spawnRoom");
	}

	public void setSpawnRoom(boolean spawnRoom) {
		this.data.putBoolean("spawnRoom", spawnRoom);
	}

	public ResourceLocation getSpawnRoomDimension() {
		String dimensionId = this.data.getString("spawnRoomDimension");
		if (dimensionId.isEmpty()) {
			return Level.OVERWORLD.location();
		}
		return ResourceLocation.tryParse(dimensionId);
	}

	public void setSpawnRoomDimension(ResourceLocation spawnRoomDimension) {
		this.data.putString("spawnRoomDimension", spawnRoomDimension.toString());
	}

	public boolean isUhcIsFinished() {
		return this.data.getBoolean("uhcIsFinished");
	}

	public void setUhcIsFinished(boolean uhcIsFinished) {
		this.data.putBoolean("uhcIsFinished", uhcIsFinished);
	}

	public boolean isUhcShowdown() {
		return this.data.getBoolean("uhcShowdownOnGoing");
	}

	public void setUhcShowdown(boolean uhcShowdownOnGoing) {
		this.data.putBoolean("uhcShowdownOnGoing", uhcShowdownOnGoing);
	}

	public boolean isUhcShowdownFinished() {
		return this.data.getBoolean("uhcShowdownFinished");
	}

	public void setUhcShowdownFinished(boolean uhcShowdownFinished) {
		this.data.putBoolean("uhcShowdownFinished", uhcShowdownFinished);
	}

	public boolean isTimedNamesApplied() {
		return this.data.getBoolean("timedNamesApplied");
	}

	public void setTimedNamesApplied(boolean timedNamesApplied) {
		this.data.putBoolean("timedNamesApplied", timedNamesApplied);
	}

	public boolean isGlowTimeApplied() {
		return this.data.getBoolean("glowTimeApplied");
	}

	public void setGlowTimeApplied(boolean glowTimeApplied) {
		this.data.putBoolean("glowTimeApplied", glowTimeApplied);
	}

	public boolean isTimeLockApplied() {
		return this.data.getBoolean("timeLockApplied");
	}

	public void setTimeLockApplied(boolean timeLockApplied) {
		this.data.putBoolean("timeLockApplied", timeLockApplied);
	}

	public ResourceLocation getUHCDimension() {
		return ResourceLocation.tryParse(this.data.getString("UHCDimension"));
	}

	public void setUHCDimension(ResourceLocation uHCDimension) {
		this.data.putString("UHCDimension", uHCDimension.toString());
	}

	public boolean isGraceEnabled() {
		return this.data.getBoolean("graceEnabled");
	}

	public void setGraceEnabled(boolean graceEnabled) {
		this.data.putBoolean("graceEnabled", graceEnabled);
	}

	public int getGraceTime() {
		return this.data.getInt("graceTime");
	}

	public void setGraceTime(int graceTime) {
		this.data.putInt("graceTime", graceTime);
	}

	public boolean isGraceFinished() {
		return this.data.getBoolean("graceFinished");
	}

	public void setGraceFinished(boolean graceFinished) {
		this.data.putBoolean("graceFinished", graceFinished);
	}

	public boolean areTeamsLocked() {
		return this.data.getBoolean("teamsLocked");
	}

	public void setTeamsLocked(boolean teamsLocked) {
		this.data.putBoolean("teamsLocked", teamsLocked);
	}

	public static UHCSaveData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

		DimensionDataStorage storage = overworld.getDataStorage();
		return storage.computeIfAbsent(new Factory<>(UHCSaveData::new, UHCSaveData::load), DATA_NAME);
	}
}
