package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameRuleHandler {
	@SubscribeEvent
	public void onLevelLoad(LevelEvent.Load event) {
		LevelAccessor levelAccessor = event.getLevel();
		if (!levelAccessor.isClientSide()) {
			MinecraftServer server = levelAccessor.getServer();
			GameRules rules = server.getGameRules();

			if (rules.getBoolean(GameRules.RULE_NATURAL_REGENERATION))
				rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, server);
		}
	}

	@SubscribeEvent
	public void GameRules(TickEvent.LevelTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			Level level = event.level;
			MinecraftServer server = level.getServer();
			GameRules rules = server.getGameRules();
			ServerLevel overworld = server.overworld();
			if (overworld != null) {
				if (overworld.getGameTime() % 20 == 0) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					LevelData wInfo = level.getLevelData();

					if (!saveData.isWeatherEnabled()) {
						if (level.isRaining())
							wInfo.setRaining(false);
						if (rules.getBoolean(GameRules.RULE_WEATHER_CYCLE))
							rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
					}

//					if (rules.getBoolean(GameRules.RULE_NATURAL_REGENERATION))
//						rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, server);
				}
			}
		}
	}
}
