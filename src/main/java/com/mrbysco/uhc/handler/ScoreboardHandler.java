package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreCriteria.RenderType;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ScoreboardHandler {
	@SubscribeEvent
	public void ScoreboardStuff(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			World world = event.world;
			ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				Scoreboard scoreboard = world.getScoreboard();
				UHCSaveData saveData = UHCSaveData.get(overworld);

				for (TextFormatting color : TextFormatting.values()) {
					boolean flag = !color.equals(TextFormatting.OBFUSCATED) && !color.equals(TextFormatting.BOLD) && 
							!color.equals(TextFormatting.STRIKETHROUGH) && !color.equals(TextFormatting.UNDERLINE) && 
							!color.equals(TextFormatting.ITALIC) && !color.equals(TextFormatting.RESET);
					
					if(color.getColorIndex() != 0 && color.getColorIndex() <= 14 && flag) {
						String colorString = color.getFriendlyName();
						
						if(scoreboard.getTeam(colorString) == null) {
							makeTeam(scoreboard, color.getFriendlyName(), color);
						}
					}
				}
				
				if(scoreboard.getTeam("solo") == null) {
					makeTeam(scoreboard, "solo", TextFormatting.WHITE);
				}
				
				if(scoreboard.getTeam("spectator") == null) {
					makeTeam(scoreboard, "spectator", TextFormatting.BLACK);
				}
				
				if(scoreboard.getObjective("health") == null) {
					scoreboard.addObjective("health", ScoreCriteria.HEALTH, new StringTextComponent("health"), RenderType.HEARTS);
				}
				
				boolean healthExists = scoreboard.getObjective("health") != null;
				
				if(saveData.isHealthInTab() && healthExists) {
					ScoreObjective score = scoreboard.getObjective("health");
					if(scoreboard.getObjectiveInDisplaySlot(0) != score) {
						scoreboard.setObjectiveInDisplaySlot(0, score);
						scoreboard.setObjectiveInDisplaySlot(1, null);
						scoreboard.setObjectiveInDisplaySlot(2, null);
					}
				}
				
				if(saveData.isHealthOnSide() && healthExists) {
					ScoreObjective score = scoreboard.getObjective("health");
					if(scoreboard.getObjectiveInDisplaySlot(1) != score) {
						scoreboard.setObjectiveInDisplaySlot(0, null);
						scoreboard.setObjectiveInDisplaySlot(1, score);
						scoreboard.setObjectiveInDisplaySlot(2, null);
					}
				}
				if(saveData.isHealthUnderName() && healthExists) {
					ScoreObjective score = scoreboard.getObjective("health");
					if(scoreboard.getObjectiveInDisplaySlot(2) != score) {
						scoreboard.setObjectiveInDisplaySlot(0, null);
						scoreboard.setObjectiveInDisplaySlot(1, null);
						scoreboard.setObjectiveInDisplaySlot(2, score);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void scoreboardPlayer(TickEvent.PlayerTickEvent event){
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			PlayerEntity player = event.player;
			World world = player.world;
			ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				Scoreboard scoreboard = world.getScoreboard();

				if (!saveData.isUhcOnGoing() && !saveData.isUhcStarting()) {
					if(player.getActivePotionEffect(Effects.GLOWING) == null) {
						player.addPotionEffect(new EffectInstance(Effects.GLOWING, 32767 * 20, 10, true, false));
					}
				}

				if(scoreboard.getPlayersTeam(player.getName().toString()) == scoreboard.getTeam("spectator") && saveData.isUhcOnGoing()) {
					if(!player.isCreative())
						player.setGameType(GameType.SPECTATOR);
				}
			}
		}
	}
	
	public void makeTeam(Scoreboard scoreboard, String teamName, TextFormatting color) {
		ScorePlayerTeam team = scoreboard.createTeam(teamName);
		team.setPrefix(new StringTextComponent(color.toString()));
		team.setColor(color);
	}
}
