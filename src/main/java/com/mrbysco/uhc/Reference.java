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

	public static final Component shrinkTooltip = Component.translatable("book.uhc.explain.shrinkmodeshrink").append(" ")
			.append(Component.translatable("book.uhc.explain.shrinkmodeshrink2"));
	public static final Component arenaTooltip = Component.translatable("book.uhc.explain.shrinkmodearena").append(" ")
			.append(Component.translatable("book.uhc.explain.shrinkmodearena2"));
	public static final Component controlTooltip = Component.translatable("book.uhc.explain.shrinkmodecontrol").append(" ")
			.append(Component.translatable("book.uhc.explain.shrinkmodecontrol2"));
	public static final Component healthExplain = Component.translatable("book.uhc.explain.healthExplain");
	public static final Component timeLockTooltip = Component.translatable("book.uhc.explain.timelock");
	public static final Component minuteMarkExplain = Component.translatable("book.uhc.explain.minmark").append(" ")
			.append(Component.translatable("book.uhc.explain.minmark2"));
	public static final Component timedNameExplain = Component.translatable("book.uhc.explain.timename").append(" ")
			.append(Component.translatable("book.uhc.explain.timename2"));
	public static final Component timedGlowExplain = Component.translatable("book.uhc.explain.timeglow").append(" ")
			.append(Component.translatable("book.uhc.explain.timeglow2"));
	public static final Component regenPotionExplain = Component.translatable("book.uhc.explain.regenpotion").append(" ")
			.append(Component.translatable("book.uhc.explain.regenpotion2"));
	public static final Component level2PotionExplain = Component.translatable("book.uhc.explain.level2potion").append(" ")
			.append(Component.translatable("book.uhc.explain.level2potion2"));
	public static final Component notchApplesExplain = Component.translatable("book.uhc.explain.notchapple").append(" ")
			.append(Component.translatable("book.uhc.explain.notchapple2"));
	public static final Component autoSmeltExplain = Component.translatable("book.uhc.explain.autocook").append(" ")
			.append(Component.translatable("book.uhc.explain.autocook2").append(" ")
			.append(Component.translatable("book.uhc.explain.autocook3").append(" ")
			.append(Component.translatable("book.uhc.explain.autocook4"))));
	public static final Component itemConvertExplain = Component.translatable("book.uhc.explain.itemconvert").append(" ")
			.append(Component.translatable("book.uhc.explain.itemconvert2").append(" ")
			.append(Component.translatable("book.uhc.explain.itemconvert3").append(" ")
			.append(Component.translatable("book.uhc.explain.itemconvert4").append(" ")
			.append(Component.translatable("book.uhc.explain.itemconvert5")))));
	public static final Component netherTravelExplain = Component.translatable("book.uhc.explain.nether");
	public static final Component weatherExplain = Component.translatable("book.uhc.explain.weather");
	public static final Component mobGriefingExplain = Component.translatable("book.uhc.explain.mobgriefing");
	public static final Component customHealthExplain = Component.translatable("book.uhc.explain.customhealth");
	public static final Component randomSpawnsExplain = Component.translatable("book.uhc.explain.randomspawns").append(" ")
			.append(Component.translatable("book.uhc.explain.randomspawns2"));
	public static final Component spreadDistanceExplain = Component.translatable("book.uhc.explain.spreaddistance");
	public static final Component spreadMaxRangeExplain = Component.translatable("book.uhc.explain.spreadrange");
	public static final Component spreadRespectTeamExplain = Component.translatable("book.uhc.explain.spreadteams");
	public static final Component gracePeriodExplain = Component.translatable("book.uhc.explain.graceperiod").append(" ")
			.append(Component.translatable("book.uhc.explain.graceperiod2"));
}
