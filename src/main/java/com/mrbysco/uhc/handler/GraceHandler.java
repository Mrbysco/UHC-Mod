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
	public void graceTimerEvent(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer()) {
			Level world = event.world;
			MinecraftServer server = event.world.getServer();
			ServerLevel overworld = server.getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

				if (!playerList.isEmpty() && saveData.isUhcOnGoing()) {
					if (world.getGameTime() % 20 == 0) {
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
		Level world = event.getEntityLiving().level;
		if (!world.isClientSide) {
			MinecraftServer server = world.getServer();
			ServerLevel overworld = server.getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if (saveData.isGraceEnabled() && !saveData.isGraceFinished()) {
					if (event.getEntityLiving() instanceof Player) {
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
		Level world = event.getEntityLiving().level;
		if (!world.isClientSide) {
			MinecraftServer server = world.getServer();
			ServerLevel overworld = server.getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if (saveData.isGraceEnabled() && !saveData.isGraceFinished()) {
					if (event.getEntityLiving() instanceof Player) {
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
