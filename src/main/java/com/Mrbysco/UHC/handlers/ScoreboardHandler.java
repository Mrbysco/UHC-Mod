package com.Mrbysco.UHC.handlers;

import java.util.Collection;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team.CollisionRule;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ScoreboardHandler {
	@SubscribeEvent
	public void ScoreboardStuff(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			World world = player.world;
			NBTTagCompound entityData = player.getEntityData();
			Scoreboard scoreboard = world.getScoreboard();
			UHCSaveData saveData = UHCSaveData.getForWorld(world);

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
			
			if(saveData.isUhcOnGoing() && player.getTeam() == null)
			{
				scoreboard.addPlayerToTeam(player.getName(), "solo");
			}
			
			if(scoreboard.getTeam("solo") == null)
			{
				ScorePlayerTeam team = scoreboard.createTeam("solo");
				team.setPrefix(TextFormatting.WHITE.toString());
				team.setColor(TextFormatting.WHITE);
				team.setAllowFriendlyFire(true);
				team.setCollisionRule(CollisionRule.ALWAYS);
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
				boolean flag = team != scoreboard.getTeam("solo");
				if(saveData.isFriendlyFire() && flag)
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
				
				if(saveData.isTeamCollision() && flag)
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

			ScorePlayerTeam team = scoreboard.getPlayersTeam(player.getName());
			if (team != null)
			{
				Collection<PotionEffect> effects = player.getActivePotionEffects();				
				if (saveData.isUhcOnGoing() == false)
				{
					if(player.isGlowing() == false)
						player.setGlowing(true);
					else
						return;
				}
				else
				{
					player.setGlowing(false);
				}
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
