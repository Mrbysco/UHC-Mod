package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GameRuleHandler {
	@SubscribeEvent
	public void GameRules(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				MinecraftServer server = event.world.getMinecraftServer();
				WorldServer wServer = server.getWorld(0);
				GameRules rules = wServer.getGameRules();
				WorldInfo wInfo = event.world.getWorldInfo();
	
				if(!saveData.isWeatherEnabled())
				{
					if(event.world.isRaining())
						wInfo.setRaining(false);
					if(rules.getBoolean("doWeatherCycle"))
						rules.setOrCreateGameRule("doWeatherCycle", String.valueOf(false));
				}
				
				if(rules.getBoolean("naturalRegeneration") == true)
					rules.setOrCreateGameRule("naturalRegeneration", String.valueOf(false));
			}
		}
	}
}
