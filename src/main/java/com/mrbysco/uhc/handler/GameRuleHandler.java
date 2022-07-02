package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameRuleHandler {
	@SubscribeEvent
	public void GameRules(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			MinecraftServer server = event.world.getServer();
			ServerLevel overworld = server.getLevel(Level.OVERWORLD);
			if (overworld != null) {
				if (overworld.getGameTime() % 20 == 0) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					GameRules rules = overworld.getGameRules();
					LevelData wInfo = event.world.getLevelData();

					if (!saveData.isWeatherEnabled()) {
						if (event.world.isRaining())
							wInfo.setRaining(false);
						if (rules.getBoolean(GameRules.RULE_WEATHER_CYCLE))
							rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
					}

					if (rules.getBoolean(GameRules.RULE_NATURAL_REGENERATION))
						rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, server);
				}
			}
		}
	}
}
