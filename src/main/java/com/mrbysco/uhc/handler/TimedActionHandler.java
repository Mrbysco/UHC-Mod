package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class TimedActionHandler {
	@SubscribeEvent
	public static void ScoreboardStuff(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			MinecraftServer server = level.getServer();
			List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
			Scoreboard scoreboard = level.getScoreboard();

			UHCSaveData saveData = UHCSaveData.get(level);
			UHCTimerData timerData = UHCTimerData.get(level);
			GameRules rules = level.getGameRules();

			if (saveData.isUhcOnGoing()) {
				if (saveData.isTimeLock()) {
					int timeLockTimer = timerData.getTimeLockTimer();
					boolean timeFlag = timeLockTimer == TimerHandler.tickTime(saveData.getTimeLockTimer());
					if (timeFlag) {
						if (rules.getBoolean(GameRules.RULE_DAYLIGHT)) {
							if (saveData.getTimeMode().equals("Day")) {
								if (level.isDay()) {
									if (rules.getBoolean(GameRules.RULE_DAYLIGHT))
										rules.getRule(GameRules.RULE_DAYLIGHT).set(false, server);
								}
							} else if (saveData.getTimeMode().equals("Night")) {
								if (!level.isDay()) {
									if (rules.getBoolean(GameRules.RULE_DAYLIGHT))
										rules.getRule(GameRules.RULE_DAYLIGHT).set(false, server);
								}
							} else {
								if (rules.getBoolean(GameRules.RULE_DAYLIGHT))
									rules.getRule(GameRules.RULE_DAYLIGHT).set(true, server);
							}
						}

						if (!saveData.isTimeLockApplied()) {
							for (ServerPlayer player : playerList) {
								player.sendSystemMessage(Component.translatable("uhc.message.timelock", ChatFormatting.GOLD + saveData.getTimeMode()));
							}

							saveData.setTimeLockApplied(true);
							saveData.setDirty();
						}
					}
				}

				if (saveData.isMinuteMark()) {
					int minuteMarkTimer = timerData.getMinuteMarkTimer();
					int minutes = saveData.getMinuteMarkTime();
					int minuteAmount = timerData.getMinuteMarkAmount();
					boolean minuteMarkFlag = minuteMarkTimer >= TimerHandler.tickTime(minutes);
					if (minuteMarkFlag) {
						if (minuteAmount != timerData.getMinuteMarkAmount())
							minuteAmount = timerData.getMinuteMarkAmount();

						++minuteAmount;
						timerData.setMinuteMarkAmount(minuteAmount);
						for (ServerPlayer player : playerList) {
							FireworkRocketEntity rocket = new FireworkRocketEntity(level, player.getX(), player.getY() + 2, player.getZ(), ItemStack.EMPTY);
							player.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1F, 1F);
							player.level().addFreshEntity(rocket);

							int minutesDone = minutes * minuteAmount;
							if (minuteAmount == 1) {
								player.displayClientMessage(Component.translatable("uhc.message.minutemark.single.time", ChatFormatting.YELLOW + String.valueOf(minutesDone)), true);
								player.sendSystemMessage(Component.translatable("uhc.message.minutemark.single.time", ChatFormatting.YELLOW + String.valueOf(minutesDone)));
							} else {
								player.displayClientMessage(Component.translatable("uhc.message.minutemark.time", ChatFormatting.YELLOW + String.valueOf(minutesDone)), true);
								player.sendSystemMessage(Component.translatable("uhc.message.minutemark.time", ChatFormatting.YELLOW + String.valueOf(minutesDone)));
							}
						}
						timerData.setMinuteMarkTimer(0);
						timerData.setDirty();
					}
				}

				if (saveData.isTimedNames()) {
					int timedNameTimer = timerData.getNameTimer();
					boolean timedNameFlag = timedNameTimer == TimerHandler.tickTime(saveData.getNameTimer());

					if (timedNameFlag && !saveData.isTimedNamesApplied()) {
						for (PlayerTeam team : scoreboard.getPlayerTeams()) {
							if (team.getNameTagVisibility() != Team.Visibility.ALWAYS)
								team.setNameTagVisibility(Team.Visibility.ALWAYS);
						}

						for (ServerPlayer player : playerList) {
							player.sendSystemMessage(Component.translatable("uhc.message.timedname"));
						}

						saveData.setTimedNamesApplied(true);
						saveData.setDirty();
					} else {
						for (PlayerTeam team : scoreboard.getPlayerTeams()) {
							if (team.getNameTagVisibility() != Team.Visibility.HIDE_FOR_OTHER_TEAMS)
								team.setNameTagVisibility(Team.Visibility.HIDE_FOR_OTHER_TEAMS);
						}
					}
				}

				if (saveData.isTimedGlow()) {
					int timedGlowTimer = timerData.getGlowTimer();
					boolean timedGlowFlag = timedGlowTimer == TimerHandler.tickTime(saveData.getGlowTime());

					if (timedGlowFlag) {
						for (ServerPlayer player : playerList) {
							if (player.getEffect(MobEffects.GLOWING) == null)
								player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 32767 * 20, 10, true, false));
						}

						if (!saveData.isGlowTimeApplied()) {
							for (ServerPlayer player : playerList) {
								player.sendSystemMessage(Component.translatable("uhc.message.timedglow"));
							}
							saveData.setGlowTimeApplied(true);
							saveData.setDirty();
						}
					} else {
						for (ServerPlayer player : playerList) {
							if (player.hasGlowingTag())
								player.setGlowingTag(false);
						}
					}
				}
			}
		}
	}
}
