package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class GraceHandler {
	public int graceTimer;

	@SubscribeEvent
	public void graceTimerEvent(TickEvent.LevelTickEvent event) {
		Level level = event.level;
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			MinecraftServer server = level.getServer();
			ServerLevel overworld = (ServerLevel) level;
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

				if (!playerList.isEmpty() && saveData.isUhcOnGoing()) {
					if (level.getGameTime() % 20 == 0) {
						if (!saveData.isGraceFinished()) {
							if (saveData.isGraceEnabled()) {
								if (timerData.getGlowTimer() != this.graceTimer) {
									this.graceTimer = timerData.getGlowTimer();
									if (saveData.isGraceFinished()) {
										saveData.setGraceFinished(false);
										saveData.setDirty();
									}
								}

								if (timerData.getGlowTimer() >= TimerHandler.tickTime(saveData.getGraceTime())) {
									this.graceTimer = TimerHandler.tickTime(saveData.getGraceTime());
									saveData.setGraceFinished(true);
									saveData.setDirty();
								} else {
									++this.graceTimer;
									timerData.setGraceTimer(this.graceTimer);
									timerData.setDirty();
								}
							} else {
								if (timerData.getGraceTimer() != 0) {
									timerData.setGraceTimer(0);
									timerData.setDirty();
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void graceTimerEvent(LivingAttackEvent event) {
		Level level = event.getEntity().level();
		if (!level.isClientSide) {
			MinecraftServer server = level.getServer();
			ServerLevel overworld = server.overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if (saveData.isGraceEnabled() && !saveData.isGraceFinished()) {
					if (event.getEntity() instanceof Player) {
						Entity trueSource = event.getSource().getEntity();
						if (trueSource instanceof Player) {
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void graceTimerEvent(LivingHurtEvent event) {
		Level level = event.getEntity().level();
		if (!level.isClientSide) {
			MinecraftServer server = level.getServer();
			ServerLevel overworld = server.overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if (saveData.isGraceEnabled() && !saveData.isGraceFinished()) {
					if (event.getEntity() instanceof Player) {
						Entity trueSource = event.getSource().getEntity();
						if (trueSource instanceof Player) {
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
}
