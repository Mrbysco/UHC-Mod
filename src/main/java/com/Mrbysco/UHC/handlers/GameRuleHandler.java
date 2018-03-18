package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GameRuleHandler {
	@SubscribeEvent
	public void GameRules(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(event.world);

			MinecraftServer server = event.world.getMinecraftServer();
			WorldServer wServer = server.getWorld(0);
			GameRules rules = wServer.getGameRules();
			WorldInfo wInfo = event.world.getWorldInfo();
			
			if(saveData.isMobGriefing())
			{
				if(rules.getBoolean("mobGriefing") == false)
					rules.setOrCreateGameRule("mobGriefing", String.valueOf(true));
			}
			else
			{
				if(rules.getBoolean("mobGriefing"))
					rules.setOrCreateGameRule("mobGriefing", String.valueOf(false));
			}
			
			if(saveData.isWeatherEnabled())
			{
				if(rules.getBoolean("doWeatherCycle") == false)
					rules.setOrCreateGameRule("doWeatherCycle", String.valueOf(true));
			}
			else
			{
				if(event.world.isRaining())
					wInfo.setRaining(false);
				if(rules.getBoolean("doWeatherCycle"))
					rules.setOrCreateGameRule("doWeatherCycle", String.valueOf(false));
			}
			
			if(wInfo.getDifficulty() != EnumDifficulty.getDifficultyEnum(saveData.getDifficulty()))
				wInfo.setDifficulty(EnumDifficulty.getDifficultyEnum(saveData.getDifficulty()));
		}
	}
}
