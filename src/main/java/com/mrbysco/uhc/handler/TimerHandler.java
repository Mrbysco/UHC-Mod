package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TimerHandler {
	public int shrinkTimeUntil;
	public int timeLockTimer;
	public int minuteMarkTimer;
	public int nameTimer;
	public int glowTimer;

	@SubscribeEvent
	public void timerEvent(TickEvent.LevelTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer()) {
			Level level = event.level;
			ServerLevel overworld = level.getServer().overworld();
			if (overworld != null && level.getGameTime() % 20 == 0) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
				MinecraftServer server = level.getServer();
				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

				if (!playerList.isEmpty() && saveData.isUhcOnGoing()) {
					if (!saveData.isShrinkApplied()) {
						if (saveData.isShrinkEnabled()) {
							if (timerData.getShrinkTimeUntil() != this.shrinkTimeUntil) {
								this.shrinkTimeUntil = timerData.getShrinkTimeUntil();
							}

							if (timerData.getShrinkTimeUntil() >= tickTime(saveData.getShrinkTimer())) {
								this.shrinkTimeUntil = tickTime(saveData.getShrinkTimer());
							} else {
								++this.shrinkTimeUntil;
								timerData.setShrinkTimeUntil(this.shrinkTimeUntil);
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
							if (timerData.getTimeLockTimer() != this.timeLockTimer)
								this.timeLockTimer = timerData.getTimeLockTimer();

							if (timerData.getTimeLockTimer() >= tickTime(saveData.getTimeLockTimer())) {
								this.timeLockTimer = tickTime(saveData.getTimeLockTimer());
							} else {
								++this.timeLockTimer;
								timerData.setTimeLockTimer(this.timeLockTimer);
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
						if (timerData.getMinuteMarkTimer() != this.minuteMarkTimer) {
							this.minuteMarkTimer = timerData.getMinuteMarkTimer();
						}

						if (timerData.getMinuteMarkTimer() >= tickTime(saveData.getMinuteMarkTime())) {
							this.minuteMarkTimer = tickTime(saveData.getMinuteMarkTime());
						} else {
							++this.minuteMarkTimer;
							timerData.setMinuteMarkTimer(this.minuteMarkTimer);
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
							if (timerData.getNameTimer() != this.nameTimer)
								this.nameTimer = timerData.getNameTimer();

							if (timerData.getNameTimer() >= tickTime(saveData.getNameTimer())) {
								this.nameTimer = tickTime(saveData.getNameTimer());
							} else {
								++this.nameTimer;
								timerData.setNameTimer(this.nameTimer);
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
							if (timerData.getGlowTimer() != this.glowTimer)
								this.glowTimer = timerData.getGlowTimer();

							if (timerData.getGlowTimer() >= tickTime(saveData.getGlowTime())) {
								this.glowTimer = tickTime(saveData.getGlowTime());
							} else {
								++this.glowTimer;
								timerData.setGlowTimer(this.glowTimer);
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
	}

	public static int tickTime(int oldTime) {
		return oldTime * 60;
	}
}
