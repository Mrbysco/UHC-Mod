package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.util.UHCHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class GraceHandler {
	public static int graceTimer;

	@SubscribeEvent
	public static void graceTimerEvent(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD) && level.getGameTime() % 20 == 0 && UHCHelper.isUHCOnGoing(level)) {
			MinecraftServer server = level.getServer();
			UHCSaveData saveData = UHCSaveData.get(level);
			UHCTimerData timerData = UHCTimerData.get(level);
			List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

			if (!playerList.isEmpty()) {
				if (!saveData.isGraceFinished()) {
					if (saveData.isGraceEnabled()) {
						if (timerData.getGlowTimer() != graceTimer) {
							graceTimer = timerData.getGlowTimer();
							if (saveData.isGraceFinished()) {
								saveData.setGraceFinished(false);
								saveData.setDirty();
							}
						}

						if (timerData.getGlowTimer() >= TimerHandler.tickTime(saveData.getGraceTime())) {
							graceTimer = TimerHandler.tickTime(saveData.getGraceTime());
							saveData.setGraceFinished(true);
							saveData.setDirty();
						} else {
							++graceTimer;
							timerData.setGraceTimer(graceTimer);
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

	@SubscribeEvent
	public static void graceTimerEvent(LivingIncomingDamageEvent event) {
		Level level = event.getEntity().level();
		if (!level.isClientSide()) {
			UHCSaveData saveData = UHCSaveData.get(level);
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

	@SubscribeEvent
	public static void graceTimerEvent(LivingDamageEvent.Pre event) {
		Level level = event.getEntity().level();
		if (!level.isClientSide()) {
			UHCSaveData saveData = UHCSaveData.get(level);
			if (saveData.isGraceEnabled() && !saveData.isGraceFinished()) {
				if (event.getEntity() instanceof Player) {
					Entity trueSource = event.getSource().getEntity();
					if (trueSource instanceof Player) {
						event.setNewDamage(0);
					}
				}
			}
		}
	}
}
