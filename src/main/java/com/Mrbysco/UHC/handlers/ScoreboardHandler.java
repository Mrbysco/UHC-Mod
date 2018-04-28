package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ScoreboardHandler {
	@SubscribeEvent
	public void ScoreboardStuff(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			Scoreboard scoreboard = world.getScoreboard();
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
			MinecraftServer server = world.getMinecraftServer();
			ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();
			WorldServer wServer = server.getWorld(0);
			GameRules rules = wServer.getGameRules();
			
			for (TextFormatting color : TextFormatting.values())
			{
				boolean flag = !color.equals(TextFormatting.OBFUSCATED) && !color.equals(TextFormatting.BOLD) && 
						!color.equals(TextFormatting.STRIKETHROUGH) && !color.equals(TextFormatting.UNDERLINE) && 
						!color.equals(TextFormatting.ITALIC) && !color.equals(TextFormatting.RESET);
				
				if(color.getColorIndex() != 0 && color.getColorIndex() <= 14 && flag)
				{
					String colorString = color.getFriendlyName();
					
					if(scoreboard.getTeam(colorString) == null)
					{
						makeTeam(scoreboard, color.getFriendlyName(), color);
					}
				}
			}
			
			if(scoreboard.getTeam("solo") == null)
			{
				makeTeam(scoreboard, "solo", TextFormatting.WHITE);
			}
			
			if(scoreboard.getTeam("spectator") == null)
			{
				makeTeam(scoreboard, "spectator", TextFormatting.BLACK);
			}
			
			if(scoreboard.getObjective("health") == null)
			{
				scoreboard.addScoreObjective("health", IScoreCriteria.HEALTH);
			}
			
			boolean healthExists = scoreboard.getObjective("health") != null;
			
			if(saveData.isHealthInTab() && healthExists)
			{
				ScoreObjective score = scoreboard.getObjective("health");
				if(scoreboard.getObjectiveInDisplaySlot(0) != score)
				{
					scoreboard.setObjectiveInDisplaySlot(0, score);
					scoreboard.setObjectiveInDisplaySlot(1, null);
					scoreboard.setObjectiveInDisplaySlot(2, null);
				}
			}
			
			if(saveData.isHealthOnSide() && healthExists)
			{
				ScoreObjective score = scoreboard.getObjective("health");
				if(scoreboard.getObjectiveInDisplaySlot(1) != score)
				{
					scoreboard.setObjectiveInDisplaySlot(0, null);
					scoreboard.setObjectiveInDisplaySlot(1, score);
					scoreboard.setObjectiveInDisplaySlot(2, null);
				}
			}
			if(saveData.isHealthUnderName() && healthExists)
			{
				ScoreObjective score = scoreboard.getObjective("health");
				if(scoreboard.getObjectiveInDisplaySlot(2) != score)
				{
					scoreboard.setObjectiveInDisplaySlot(0, null);
					scoreboard.setObjectiveInDisplaySlot(1, null);
					scoreboard.setObjectiveInDisplaySlot(2, score);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void scoreboardPlayer(TickEvent.PlayerTickEvent event){
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			World world = player.world;
			Scoreboard scoreboard = world.getScoreboard();
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

			if (!saveData.isUhcOnGoing() && !saveData.isUhcStarting())
			{
				System.out.println("hi");
				if(player.getActivePotionEffect(MobEffects.GLOWING) == null)
					player.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 32767 * 20, 10, true, false));
			}
			
			if(scoreboard.getPlayersTeam(player.getName()) == scoreboard.getTeam("spectator") && saveData.isUhcOnGoing())
			{
				if(player.isCreative())
					return;
				else
					player.setGameType(GameType.SPECTATOR);
			}
		}
	}
	
	public void makeTeam(Scoreboard scoreboard, String teamName, TextFormatting color)
	{
		ScorePlayerTeam team = scoreboard.createTeam(teamName);
		team.setPrefix(color.toString());
		team.setColor(color);
	}
}
