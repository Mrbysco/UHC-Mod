package com.mrbysco.uhc;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class Reference {
	public static final String MOD_ID = "ultrahardcoremod";
	public static final String MOD_PREFIX = MOD_ID + ":";

	public static final Component minuteMessageString = Component.translatable("book.uhc.option.minutes").withStyle(ChatFormatting.RED);
	public static final Component locationString = Component.translatable("book.uhc.option.location");
	public static final Component resetString = Component.translatable("book.uhc.option.reset");
	public static final Component TimeLockString = Component.translatable("book.uhc.option.timelock");
	public static final Component minMarkString = Component.translatable("book.uhc.option.minmark");
	public static final Component timedNameString = Component.translatable("book.uhc.option.timedname");
	public static final Component timedGlowString = Component.translatable("book.uhc.option.timedglow");
	public static final Component timeModeDayText = Component.translatable("book.uhc.option.timemodeday");
	public static final Component timeModeNightText = Component.translatable("book.uhc.option.timemodenight");
	public static final Component netherTravelString = Component.translatable("book.uhc.option.nether");
	public static final Component regenPotionsString = Component.translatable("book.uhc.option.regenpotion");
	public static final Component level2PotionsString = Component.translatable("book.uhc.option.level2potion");
	public static final Component notchApplesString = Component.translatable("book.uhc.option.notchapples");
	public static final Component autoCookString = Component.translatable("book.uhc.option.autocook");
	public static final Component itemConvertString = Component.translatable("book.uhc.option.convertion");

	public static final Component weatherString = Component.translatable("book.uhc.option.weather");
	public static final Component mobGriefingString = Component.translatable("book.uhc.option.mobgriefing");
	public static final Component customHealthString = Component.translatable("book.uhc.option.customhealth");
	public static final Component randomSpawnString = Component.translatable("book.uhc.option.randomspawns");
	public static final Component spreadDistanceString = Component.translatable("book.uhc.option.spreaddistance");
	public static final Component spreadMaxRangeString = Component.translatable("book.uhc.option.spreadrange");
	public static final Component spreadRespectTeamString = Component.translatable("book.uhc.option.spreadteams");
}
