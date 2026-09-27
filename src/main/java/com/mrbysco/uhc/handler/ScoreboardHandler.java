package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.world.scores.criteria.ObjectiveCriteria.RenderType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class ScoreboardHandler {
	@SubscribeEvent
	public static void ScoreboardStuff(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			Scoreboard scoreboard = level.getScoreboard();
			UHCSaveData saveData = UHCSaveData.get(level);

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

			if (scoreboard.getObjective("health") == null) {
				scoreboard.addObjective("health", ObjectiveCriteria.HEALTH, Component.literal("health"), RenderType.HEARTS, true, null);
			}

			boolean healthExists = scoreboard.getObjective("health") != null;

			if (saveData.isHealthInTab() && healthExists) {
				Objective score = scoreboard.getObjective("health");
				if (scoreboard.getDisplayObjective(DisplaySlot.LIST) != score) {
					scoreboard.setDisplayObjective(DisplaySlot.LIST, score);
					scoreboard.setDisplayObjective(DisplaySlot.SIDEBAR, null);
					scoreboard.setDisplayObjective(DisplaySlot.BELOW_NAME, null);
				}
			}

			if (saveData.isHealthOnSide() && healthExists) {
				Objective score = scoreboard.getObjective("health");
				if (scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR) != score) {
					scoreboard.setDisplayObjective(DisplaySlot.LIST, null);
					scoreboard.setDisplayObjective(DisplaySlot.SIDEBAR, score);
					scoreboard.setDisplayObjective(DisplaySlot.BELOW_NAME, null);
				}
			}
			if (saveData.isHealthUnderName() && healthExists) {
				Objective score = scoreboard.getObjective("health");
				if (scoreboard.getDisplayObjective(DisplaySlot.BELOW_NAME) != score) {
					scoreboard.setDisplayObjective(DisplaySlot.LIST, null);
					scoreboard.setDisplayObjective(DisplaySlot.SIDEBAR, null);
					scoreboard.setDisplayObjective(DisplaySlot.BELOW_NAME, score);
				}
			}
		}
	}

	@SubscribeEvent
	public static void scoreboardPlayer(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			UHCSaveData saveData = UHCSaveData.get(level);
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

	public static void makeTeam(Scoreboard scoreboard, String teamName, ChatFormatting color) {
		PlayerTeam team = scoreboard.addPlayerTeam(teamName);
		team.setPlayerPrefix(Component.literal(color.toString()));
		team.setColor(color);
	}
}
