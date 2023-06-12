package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.world.scores.criteria.ObjectiveCriteria.RenderType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ScoreboardHandler {
	@SubscribeEvent
	public void ScoreboardStuff(TickEvent.LevelTickEvent event) {
		Level level = event.level;
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			ServerLevel overworld = (ServerLevel) level;
			if (overworld != null) {
				Scoreboard scoreboard = level.getScoreboard();
				UHCSaveData saveData = UHCSaveData.get(overworld);

				for (ChatFormatting color : ChatFormatting.values()) {
					boolean flag = !color.equals(ChatFormatting.OBFUSCATED) && !color.equals(ChatFormatting.BOLD) &&
							!color.equals(ChatFormatting.STRIKETHROUGH) && !color.equals(ChatFormatting.UNDERLINE) &&
							!color.equals(ChatFormatting.ITALIC) && !color.equals(ChatFormatting.RESET);

					if (color.getId() != 0 && color.getId() <= 14 && flag) {
						String colorString = color.getName();

						if (scoreboard.getPlayerTeam(colorString) == null) {
							makeTeam(scoreboard, color.getName(), color);
						}
					}
				}

				if (scoreboard.getPlayerTeam("solo") == null) {
					makeTeam(scoreboard, "solo", ChatFormatting.WHITE);
				}

				if (scoreboard.getPlayerTeam("spectator") == null) {
					makeTeam(scoreboard, "spectator", ChatFormatting.BLACK);
				}

				if (scoreboard.getOrCreateObjective("health") == null) {
					scoreboard.addObjective("health", ObjectiveCriteria.HEALTH, Component.literal("health"), RenderType.HEARTS);
				}

				boolean healthExists = scoreboard.getOrCreateObjective("health") != null;

				if (saveData.isHealthInTab() && healthExists) {
					Objective score = scoreboard.getOrCreateObjective("health");
					if (scoreboard.getDisplayObjective(0) != score) {
						scoreboard.setDisplayObjective(0, score);
						scoreboard.setDisplayObjective(1, null);
						scoreboard.setDisplayObjective(2, null);
					}
				}

				if (saveData.isHealthOnSide() && healthExists) {
					Objective score = scoreboard.getOrCreateObjective("health");
					if (scoreboard.getDisplayObjective(1) != score) {
						scoreboard.setDisplayObjective(0, null);
						scoreboard.setDisplayObjective(1, score);
						scoreboard.setDisplayObjective(2, null);
					}
				}
				if (saveData.isHealthUnderName() && healthExists) {
					Objective score = scoreboard.getOrCreateObjective("health");
					if (scoreboard.getDisplayObjective(2) != score) {
						scoreboard.setDisplayObjective(0, null);
						scoreboard.setDisplayObjective(1, null);
						scoreboard.setDisplayObjective(2, score);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void scoreboardPlayer(TickEvent.PlayerTickEvent event) {
		Player player = event.player;
		Level level = player.level();
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			ServerLevel overworld = (ServerLevel) level;
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				Scoreboard scoreboard = level.getScoreboard();

				if (!saveData.isUhcOnGoing() && !saveData.isUhcStarting()) {
					if (player.getEffect(MobEffects.GLOWING) == null) {
						player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 32767 * 20, 10, true, false));
					}
				}

				if (scoreboard.getPlayersTeam(player.getName().getString()) == scoreboard.getPlayerTeam("spectator") && saveData.isUhcOnGoing()) {
					if (!player.isCreative())
						((ServerPlayer) player).setGameMode(GameType.SPECTATOR);
				}
			}
		}
	}

	public void makeTeam(Scoreboard scoreboard, String teamName, ChatFormatting color) {
		PlayerTeam team = scoreboard.addPlayerTeam(teamName);
		team.setPlayerPrefix(Component.literal(color.toString()));
		team.setColor(color);
	}
}
