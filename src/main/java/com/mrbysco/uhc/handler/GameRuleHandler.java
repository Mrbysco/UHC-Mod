package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber
public class GameRuleHandler {
	@SubscribeEvent
	public static void onLevelLoad(LevelEvent.Load event) {
		LevelAccessor level = event.getLevel();
		if (!level.isClientSide()) {
			MinecraftServer server = level.getServer();
			GameRules rules = server.getGameRules();

			if (rules.getBoolean(GameRules.RULE_NATURAL_REGENERATION))
				rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, server);
		}
	}

	@SubscribeEvent
	public static void onLevelTick(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.getGameTime() % 20 == 0 && level.dimension().equals(Level.OVERWORLD)) {
			MinecraftServer server = level.getServer();
			GameRules rules = server.getGameRules();
			UHCSaveData saveData = UHCSaveData.get(level);
			LevelData wInfo = level.getLevelData();

			if (!saveData.isWeatherEnabled()) {
				if (level.isRaining())
					wInfo.setRaining(false);
				if (rules.getBoolean(GameRules.RULE_WEATHER_CYCLE))
					rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
			}
		}
	}
}
