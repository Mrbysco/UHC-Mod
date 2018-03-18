package com.Mrbysco.UHC.config;

import com.Mrbysco.UHC.Reference;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("ultrahardcoremod.config.title")
public class UltraHardCoremodConfigGen {

	@Config.Comment({"Team Spawns"})
	public static teamSpawns teamSpawns = new teamSpawns();
	
	public static class teamSpawns{
		@Config.Comment("Team 1 Spawn")
		public String spawnTeam01 = "0,0,0";
		
		@Config.Comment("Team 2 Spawn")
		public String spawnTeam02 = "0,0,0";
		
		@Config.Comment("Team 3 Spawn")
		public String spawnTeam03 = "0,0,0";
		
		@Config.Comment("Team 4 Spawn")
		public String spawnTeam04 = "0,0,0";
		
		@Config.Comment("Team 5 Spawn")
		public String spawnTeam05 = "0,0,0";
		
		@Config.Comment("Team 6 Spawn")
		public String spawnTeam06 = "0,0,0";
		
		@Config.Comment("Team 7 Spawn")
		public String spawnTeam07 = "0,0,0";
		
		@Config.Comment("Team 8 Spawn")
		public String spawnTeam08 = "0,0,0";
		
		@Config.Comment("Team 9 Spawn")
		public String spawnTeam09 = "0,0,0";
		
		@Config.Comment("Team 10 Spawn")
		public String spawnTeam10 = "0,0,0";
		
		@Config.Comment("Team 11 Spawn")
		public String spawnTeam11 = "0,0,0";
		
		@Config.Comment("Team 12 Spawn")
		public String spawnTeam12 = "0,0,0";
		
		@Config.Comment("Team 13 Spawn")
		public String spawnTeam13 = "0,0,0";
		
		@Config.Comment("Team 14 Spawn")
		public String spawnTeam14 = "0,0,0";
	}

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}