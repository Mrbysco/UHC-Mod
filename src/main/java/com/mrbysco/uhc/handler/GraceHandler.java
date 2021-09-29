package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
			World world = event.world;
			MinecraftServer server = event.world.getServer();
			ServerWorld overworld = server.getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
				List<ServerPlayerEntity> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
				
				if(!playerList.isEmpty() && saveData.isUhcOnGoing()) {
					if (world.getGameTime() % 20 == 0) {
						if(!saveData.isGraceFinished()) {
							if(saveData.isGraceEnabled()) {
								if(timerData.getGlowTimer() != this.graceTimer) {
									this.graceTimer = timerData.getGlowTimer();
									if(saveData.isGraceFinished()) {
										saveData.setGraceFinished(false);
										saveData.markDirty();
									}
								}
								
								if(timerData.getGlowTimer() >= TimerHandler.tickTime(saveData.getGraceTime())) {
									this.graceTimer = TimerHandler.tickTime(saveData.getGraceTime());
									saveData.setGraceFinished(true);
									saveData.markDirty();
								} else {
									++this.graceTimer;
									timerData.setGraceTimer(this.graceTimer);
									timerData.markDirty();
								}			
							} else {
								if(timerData.getGraceTimer() != 0) {
									timerData.setGraceTimer(0);
									timerData.markDirty();
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
		World world = event.getEntityLiving().world;
		if(!world.isRemote) {
			MinecraftServer server = world.getServer();
			ServerWorld overworld = server.getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if(saveData.isGraceEnabled() && !saveData.isGraceFinished()) {
					if(event.getEntityLiving() instanceof PlayerEntity) {
						Entity trueSource = event.getSource().getTrueSource();
						if(trueSource instanceof PlayerEntity) {
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void graceTimerEvent(LivingHurtEvent event) {
		World world = event.getEntityLiving().world;
		if(!world.isRemote) {
			MinecraftServer server = world.getServer();
			ServerWorld overworld = server.getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if(saveData.isGraceEnabled() && !saveData.isGraceFinished()) {
					if(event.getEntityLiving() instanceof PlayerEntity) {
						Entity trueSource = event.getSource().getTrueSource();
						if(trueSource instanceof PlayerEntity) {
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
}
