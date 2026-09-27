package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.util.UHCHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class TimerHandler {
	public static int shrinkTimeUntil;
	public static int timeLockTimer;
	public static int minuteMarkTimer;
	public static int nameTimer;
	public static int glowTimer;

	@SubscribeEvent
	public static void timerEvent(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD) && level.getGameTime() % 20 == 0 && UHCHelper.isUHCOnGoing(level)) {
				UHCSaveData saveData = UHCSaveData.get(level);
				UHCTimerData timerData = UHCTimerData.get(level);
				MinecraftServer server = level.getServer();
				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

				if (!playerList.isEmpty()) {
					if (!saveData.isShrinkApplied()) {
						if (saveData.isShrinkEnabled()) {
							if (timerData.getShrinkTimeUntil() != shrinkTimeUntil) {
								shrinkTimeUntil = timerData.getShrinkTimeUntil();
							}

							if (timerData.getShrinkTimeUntil() >= tickTime(saveData.getShrinkTimer())) {
								shrinkTimeUntil = tickTime(saveData.getShrinkTimer());
							} else {
								++shrinkTimeUntil;
								timerData.setShrinkTimeUntil(shrinkTimeUntil);
								timerData.setDirty();
							}
						} else {
							if (timerData.getShrinkTimeUntil() != 0) {
								timerData.setShrinkTimeUntil(0);
								timerData.setDirty();
							}
						}
					}

					if (!saveData.isTimeLockApplied()) {
						if (saveData.isTimeLock()) {
							if (timerData.getTimeLockTimer() != timeLockTimer)
								timeLockTimer = timerData.getTimeLockTimer();

							if (timerData.getTimeLockTimer() >= tickTime(saveData.getTimeLockTimer())) {
								timeLockTimer = tickTime(saveData.getTimeLockTimer());
							} else {
								++timeLockTimer;
								timerData.setTimeLockTimer(timeLockTimer);
								timerData.setDirty();
							}
						} else {
							if (timerData.getTimeLockTimer() != 0) {
								timerData.setTimeLockTimer(0);
								timerData.setDirty();
							}
						}
					}

					if (saveData.isMinuteMark()) {
						if (timerData.getMinuteMarkTimer() != minuteMarkTimer) {
							minuteMarkTimer = timerData.getMinuteMarkTimer();
						}

						if (timerData.getMinuteMarkTimer() >= tickTime(saveData.getMinuteMarkTime())) {
							minuteMarkTimer = tickTime(saveData.getMinuteMarkTime());
						} else {
							++minuteMarkTimer;
							timerData.setMinuteMarkTimer(minuteMarkTimer);
							timerData.setDirty();
						}
					} else {
						if (timerData.getMinuteMarkTimer() != 0) {
							timerData.setMinuteMarkTimer(0);
							timerData.setDirty();
						}
					}

					if (!saveData.isTimedNamesApplied()) {
						if (saveData.isTimedNames()) {
							if (timerData.getNameTimer() != nameTimer)
								nameTimer = timerData.getNameTimer();

							if (timerData.getNameTimer() >= tickTime(saveData.getNameTimer())) {
								nameTimer = tickTime(saveData.getNameTimer());
							} else {
								++nameTimer;
								timerData.setNameTimer(nameTimer);
								timerData.setDirty();
							}
						} else {
							if (timerData.getNameTimer() != 0) {
								timerData.setNameTimer(0);
								timerData.setDirty();
							}
						}
					}

					if (!saveData.isGlowTimeApplied()) {
						if (saveData.isTimedGlow()) {
							if (timerData.getGlowTimer() != glowTimer)
								glowTimer = timerData.getGlowTimer();

							if (timerData.getGlowTimer() >= tickTime(saveData.getGlowTime())) {
								glowTimer = tickTime(saveData.getGlowTime());
							} else {
								++glowTimer;
								timerData.setGlowTimer(glowTimer);
								timerData.setDirty();
							}
						} else {
							if (timerData.getGlowTimer() != 0) {
								timerData.setGlowTimer(0);
								timerData.setDirty();
							}
						}
					}
				}

		}
	}

	public static int tickTime(int oldTime) {
		return oldTime * 60;
	}
}
