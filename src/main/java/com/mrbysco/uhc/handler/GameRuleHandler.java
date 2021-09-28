package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameRuleHandler {
	@SubscribeEvent
	public void GameRules(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			MinecraftServer server = event.world.getServer();
			ServerWorld overworld = server.getWorld(World.OVERWORLD);
			if(overworld != null) {
				if (overworld.getGameTime() % 20 == 0) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					GameRules rules = overworld.getGameRules();
					IWorldInfo wInfo = event.world.getWorldInfo();

					if(!saveData.isWeatherEnabled()) {
						if(event.world.isRaining())
							wInfo.setRaining(false);
						if(rules.getBoolean(GameRules.DO_WEATHER_CYCLE))
							rules.get(GameRules.DO_WEATHER_CYCLE).set(false, server);
					}

					if(rules.getBoolean(GameRules.NATURAL_REGENERATION))
						rules.get(GameRules.NATURAL_REGENERATION).set(false, server);
				}
			}
		}
	}
}
