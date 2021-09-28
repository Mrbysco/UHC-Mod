package com.mrbysco.uhc.config;

import com.mrbysco.uhc.UltraHardCoremod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UHCConfig {
	public static class Common {
		public final ConfigValue<? extends String> spawnDimension;
		public final ConfigValue<? extends String> spawnRoomBlock;
		public final ConfigValue<? extends String> showdownBlock;

		public final ConfigValue<List<? extends String>> teamSpawns;

		public final IntValue twilightRespawnTime;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("general");

			spawnDimension = builder
					.comment("Configures the spawn room block placed for the showdown [Default: \"minecraft:overworld\"]")
					.define("spawnDimension", "minecraft:overworld", o -> isValidResourceLocation(o));

			spawnRoomBlock = builder
					.comment("Configures the spawn room block placed by the /uhc spawnroom command [Default: \"minecraft:barrier\"]")
					.define("spawnRoomBlock", "minecraft:barrier", o -> isValidResourceLocation(o));

			showdownBlock = builder
					.comment("Configures the spawn room block placed for the showdown [Default: \"minecraft:stone_bricks\"]")
					.define("showdownBlock", "minecraft:stone_bricks", o -> isValidResourceLocation(o));

			builder.pop();
			builder.comment("Team Spawns")
					.push("team_spawns");


			String[] spawnPositions = new String[]
					{
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0",
							"0,-1,0"
					};

			teamSpawns = builder
					.comment("Team Spawns (Setting the Y value to -1 will make it find the surface for the X and Y)")
					.defineList("teamSpawns", Arrays.asList(spawnPositions), o -> isValidLocation(o));

			builder.pop();
			builder.comment("Mod Support")
					.push("mod_support");

				builder.comment("Twilight Forest")
						.push("twilightforest");

				twilightRespawnTime = builder
					.comment("The amount of seconds it takes before bosses can be spawned after the start of the UHC [default: 1200 aka 20 minutes]")
					.defineInRange("twilightRespawnTime", 1200, 0, Integer.MAX_VALUE);

				builder.pop();

			builder.pop();
		}
	}

	public static boolean isValidResourceLocation(Object object) {
		boolean flag = object instanceof String;
		if(flag) {
			String value = (String) object;
			return ResourceLocation.tryCreate(value) != null;
		}
		return false;
	}

	public static boolean isValidLocation(Object object) {
		boolean flag = object instanceof String;
		if(flag) {
			String value = (String) object;
			if(value.contains(",")) {
				String[] splitValues = value.split(",");
				if(splitValues.length == 3) {
					for(String splitValue : splitValues) {
						if(!NumberUtils.isParsable(splitValue)) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		UltraHardCoremod.LOGGER.debug("Loaded UltraHardCoremod's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading configEvent) {
		UltraHardCoremod.LOGGER.debug("UltraHardCoremod's config just got changed on the file system!");
	}
}
