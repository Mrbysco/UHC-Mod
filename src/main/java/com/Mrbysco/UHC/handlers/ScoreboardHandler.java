package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team.CollisionRule;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ScoreboardHandler {
	@SubscribeEvent
	public void ScoreboardStuff(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			Scoreboard scoreboard = world.getScoreboard();
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			MinecraftServer server = world.getMinecraftServer();
			ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();
			
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
						ScorePlayerTeam team = scoreboard.createTeam(color.getFriendlyName());
						team.setPrefix(color.toString());
						team.setColor(color);
					}
				}
			}

			if(scoreboard.getTeam("spectator") == null)
			{
				ScorePlayerTeam team = scoreboard.createTeam("spectator");
				team.setPrefix(TextFormatting.BLACK.toString());
				team.setColor(TextFormatting.BLACK);
			}
			
			if(scoreboard.getObjective("health") == null)
			{
				scoreboard.addScoreObjective("health", IScoreCriteria.HEALTH);
			}
			
			boolean healthExists = scoreboard.getObjective("health") != null;
			if(saveData.isHealthInTab())
			{
				if(healthExists)
				{
					ScoreObjective score = scoreboard.getObjective("health");
					if(scoreboard.getObjectiveInDisplaySlot(0) != score)
					{
						scoreboard.setObjectiveInDisplaySlot(0, score);
						scoreboard.setObjectiveInDisplaySlot(1, null);
						scoreboard.setObjectiveInDisplaySlot(2, null);
					}
				}
			}
			if(saveData.isHealthOnSide())
			{
				if(healthExists)
				{
					ScoreObjective score = scoreboard.getObjective("health");
					if(scoreboard.getObjectiveInDisplaySlot(1) != score)
					{
						scoreboard.setObjectiveInDisplaySlot(0, null);
						scoreboard.setObjectiveInDisplaySlot(1, score);
						scoreboard.setObjectiveInDisplaySlot(2, null);
					}
				}
			}
			if(saveData.isHealthUnderName())
			{
				if(healthExists)
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
			
			for (ScorePlayerTeam team : scoreboard.getTeams())
			{
				if(saveData.isFriendlyFire())
				{
					if (team.getAllowFriendlyFire() != true)
					{
						team.setAllowFriendlyFire(true);
					}
				}
				else
				{
					if (team.getAllowFriendlyFire() != false)
					{
						team.setAllowFriendlyFire(false);
					}
				}
				
				if(saveData.isTeamCollision())
				{
					if (team.getCollisionRule() != CollisionRule.ALWAYS)
					{
						team.setCollisionRule(CollisionRule.ALWAYS);
					}
				}
				else
				{
					if (team.getCollisionRule() != CollisionRule.HIDE_FOR_OTHER_TEAMS)
					{
						team.setCollisionRule(CollisionRule.HIDE_FOR_OTHER_TEAMS);
					}
				}
			}
			
			for(EntityPlayer player : playerList)
			{
				if (saveData.isUhcOnGoing() == false)
				{
					if(player.isGlowing() == false)
						player.setGlowing(true);
					else
						return;
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
	}
}
