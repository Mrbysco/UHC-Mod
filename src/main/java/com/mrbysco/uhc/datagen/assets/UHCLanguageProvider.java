package com.mrbysco.uhc.datagen.assets;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.registry.UHCRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class UHCLanguageProvider extends LanguageProvider {
	public UHCLanguageProvider(PackOutput output) {
		super(output, Reference.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addItem(UHCRegistry.UHC_BOOK, "│ UHC Book│");
		// Book
		add("book.uhc.team.select", "Select team");
		add("book.uhc.team.hover", "Join Team %s.");
		add("book.uhc.team.selected", "%s has joined team %s");
		add("book.uhc.team.randomized", "%s has been put in team %s randomly.");
		add("book.uhc.team.solo", "%s is now playing %s");
		add("book.uhc.team.randomize", "Randomize Players");
		add("book.uhc.team.randomizer", "Randomize");
		add("book.uhc.team.maxed", "Can not join team %s, team has max amount of players.");
		add("book.uhc.team.antispam", "Please wait a few seconds before changing team.");
		add("book.uhc.team.locked", "You can not change teams, teams have been locked.");

		add("book.uhc.shrink.control", "%s is at the control point. Shrinking the border by 1 block every second.");

		add("book.uhc.option.reset", "Reset Value");
		add("book.uhc.option.location", "Current Location");
		add("book.uhc.option.randsize", "Random Teams:");
		add("book.uhc.option.maxteams", "Max Team Size:");
		add("book.uhc.option.infinite", "-1 = Infinite");
		add("book.uhc.option.locked", "Lock Teams");
		add("book.uhc.option.minutes", "In Minutes");

		add("book.uhc.option.collision", "Team Collision:");
		add("book.uhc.option.damage", "Team Damage:");
		add("book.uhc.option.difficulty", "Difficulty:");

		add("book.uhc.option.healthtab", "Health In Tab:");
		add("book.uhc.option.healthside", "Health On Side:");
		add("book.uhc.option.healthname", "Health Under Name:");

		add("book.uhc.option.bordersize", "Worldborder Size:");
		add("book.uhc.option.bordercenter", "Worldborder Center");
		add("book.uhc.option.bordercenterx", "X:");
		add("book.uhc.option.bordercenterz", "Z:");

		add("book.uhc.option.shrinkenabled", "Border Shrink:");
		add("book.uhc.option.shrinktimer", "Time until:");
		add("book.uhc.option.shrinksize", "Size:");
		add("book.uhc.option.shrinkovertime", "Over:");
		add("book.uhc.option.shrinkmode", "Mode:");

		add("book.uhc.option.timelock", "Time Lock:");
		add("book.uhc.option.timelocktimer", "Time until:");
		add("book.uhc.option.timelockmode", "Mode:");
		add("book.uhc.option.timemodeday", "Time will stop at daytime");
		add("book.uhc.option.timemodenight", "Time will stop at night");

		add("book.uhc.option.minmark", "Minute Mark:");
		add("book.uhc.option.minmarktime", "Every:");
		add("book.uhc.option.timedname", "Timed Names:");
		add("book.uhc.option.timednametime", "After:");
		add("book.uhc.option.timedglow", "Timed Glow:");
		add("book.uhc.option.timedglowtime", "After:");

		add("book.uhc.option.nether", "Nether Travel:");
		add("book.uhc.option.regenpotion", "Regen Potions:");
		add("book.uhc.option.level2potion", "Level 2 Potions:");
		add("book.uhc.option.notchapples", "Notch Apples:");
		add("book.uhc.option.autocook", "Auto Cook:");
		add("book.uhc.option.convertion", "Item Conversion:");

		add("book.uhc.option.weather", "Weather Cycle:");
		add("book.uhc.option.mobgriefing", "Mob Griefing:");
		add("book.uhc.option.customhealth", "Custom Health:");
		add("book.uhc.option.maxhealth", "Max Health:");
		add("book.uhc.option.randomspawns", "Random Spawns:");
		add("book.uhc.option.spreaddistance", "Distance:");
		add("book.uhc.option.spreadrange", "Max Range:");
		add("book.uhc.option.spreadteams", "Respect Teams:");

		add("book.uhc.option.grace", "Grace Period:");
		add("book.uhc.option.gracetimer", "Grace for:");

		add("book.uhc.explain.shrinkmodeshrink", "Shrink the border");
		add("book.uhc.explain.shrinkmodeshrink2", "over the amount of minutes.");
		add("book.uhc.explain.shrinkmodearena", "Instantly shrink");
		add("book.uhc.explain.shrinkmodearena2", "and re-spread players.");
		add("book.uhc.explain.shrinkmodecontrol", "Control point shrinks");
		add("book.uhc.explain.shrinkmodecontrol2", "the border when occupied.");

		add("book.uhc.explain.timelock", "When enabled time will lock to the specified.");
		add("book.uhc.explain.minmark", "When enabled shrinkMode Every specified amount of");
		add("book.uhc.explain.minmark2", "minutes a message will display in chat.");
		add("book.uhc.explain.timename", "When enabled Enemy names will be");
		add("book.uhc.explain.timename2", "hidden for x amount of minutes.");
		add("book.uhc.explain.timeglow", "When enabled Players will glow after");
		add("book.uhc.explain.timeglow2", "specified Amount of minutes.");

		add("book.uhc.explain.nether", "When disabled players can't travel to the nether.");
		add("book.uhc.explain.regenpotion", "When disabled Ghast tears will convert to gold ingots");
		add("book.uhc.explain.regenpotion2", "preventing the brewing of regeneration potions.");
		add("book.uhc.explain.level2potion", "When disabled Glowstone dust will convert to");
		add("book.uhc.explain.level2potion2", "Glowstone preventing the brewing of level 2 potions.");
		add("book.uhc.explain.notchapple", "When disabled Notch apples will convert");
		add("book.uhc.explain.notchapple2", "to their old crafting materials.");

		add("book.uhc.explain.autocook", "When enabled vanilla ores and");
		add("book.uhc.explain.autocook2", "food will be smelted/cooked");
		add("book.uhc.explain.autocook3", "");
		add("book.uhc.explain.autocook4", "New smelting can be added with CraftTweaker.");

		add("book.uhc.explain.itemconvert", "When enabled will do nothing unless");
		add("book.uhc.explain.itemconvert2", "item conversions are added");
		add("book.uhc.explain.itemconvert3", "through CraftTweaker");
		add("book.uhc.explain.itemconvert4", "");
		add("book.uhc.explain.itemconvert5", "Given items will convert to given materials.");

		add("book.uhc.explain.weather", "When disabled doWeatherCycle will be disabled.");
		add("book.uhc.explain.mobgriefing", "When disabled mobGriefing will be disabled.");

		add("book.uhc.explain.customhealth", "When enabled custom max health can be set here.");
		add("book.uhc.explain.healthExplain", "1 per half heart.");
		add("book.uhc.explain.randomspawns", "When enabled uses spreadplayer command.");
		add("book.uhc.explain.randomspawns2", "When disabled it uses the spawns from config.");
		add("book.uhc.explain.spreaddistance", "Amount of blocks between teams.");
		add("book.uhc.explain.spreadrange", "The range in which it teleports players.");
		add("book.uhc.explain.spreadteams", "When enabled it teleports teams together.");

		add("book.uhc.explain.graceperiod", "When enabled the specified amount of minutes players");
		add("book.uhc.explain.graceperiod2", "won't be able to fight eachother.");

		// Team names
		add("uhc.color.darkred", "Dark Red");
		add("uhc.color.gold", "Gold");
		add("uhc.color.darkgreen", "Dark Green");
		add("uhc.color.darkaqua", "Dark Aqua");
		add("uhc.color.darkblue", "Dark Blue");
		add("uhc.color.darkpurple", "Dark Purple");
		add("uhc.color.darkgray", "Dark Gray");

		add("uhc.color.red", "Red");
		add("uhc.color.yellow", "Yellow");
		add("uhc.color.green", "Green");
		add("uhc.color.aqua", "Aqua");
		add("uhc.color.blue", "Blue");
		add("uhc.color.lightpurple", "Light Purple");
		add("uhc.color.gray", "Gray");

		add("uhc.color.black", "Spectator");
		add("uhc.color.white", "Solo");
		add("uhc.color.random", "Randomize");

		// Messages
		add("uhc.message.minutemark.single.time", "%d§r §eMinute has passed | Minute Mark§r");
		add("uhc.message.minutemark.time", "%d§r §eMinutes have passed | Minute Mark§r");
		add("uhc.message.border.moving", "§cThe world border has started moving!§r");
		add("uhc.message.timelock", "§6The time will be locked to §r%d");
		add("uhc.message.timedname", "§6Name tags are now visible!§r");
		add("uhc.message.timedglow", "§6Players will now glow!§r");

		add("uhc.start.5", "§eUHC Starting in 5§r");
		add("uhc.start.4", "§e4§r");
		add("uhc.start.3", "§e3§r");
		add("uhc.start.2", "§e2§r");
		add("uhc.start.1", "§e1§r");
		add("uhc.start", "§eGO!§r");

		add("uhc.team.won", "§6Team §r%s§6 has won the UHC!§r");
		add("uhc.player.won", "%s§6 has won the UHC on their own!§r");
		add("uhc.player.showdown.won", "%s§6 has won the final showdown!§r");

		add("ultrahardcoremod.networking.start_uhc.failed", "Failed to start UHC: %s");
		add("ultrahardcoremod.networking.page_1.failed", "Failed to sync page 1: %s");
		add("ultrahardcoremod.networking.page_2.failed", "Failed to sync page 2: %s");
		add("ultrahardcoremod.networking.page_3.failed", "Failed to sync page 3: %s");
		add("ultrahardcoremod.networking.page_4.failed", "Failed to sync page 4: %s");
		add("ultrahardcoremod.networking.page_5.failed", "Failed to sync page 5: %s");
		add("ultrahardcoremod.networking.page_6.failed", "Failed to sync page 6: %s");
		add("ultrahardcoremod.networking.sync_data.failed", "Failed to sync data: %s");
		add("ultrahardcoremod.networking.uhc_team_randomizer.failed", "Failed to randomize team data: %s");
		add("ultrahardcoremod.networking.team.failed", "Failed to switch teams: %s");



	}
}
