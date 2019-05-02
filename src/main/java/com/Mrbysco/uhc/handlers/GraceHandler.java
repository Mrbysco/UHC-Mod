package com.mrbysco.uhc.handlers;

import com.mrbysco.uhc.init.UHCSaveData;
import com.mrbysco.uhc.init.UHCTimerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class GraceHandler {
	public int graceTimer;
	
	@SubscribeEvent
	public void graceTimerEvent(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer())
		{
			World world = event.world;
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
				MinecraftServer server = world.getMinecraftServer();
				ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();
				
				if(!playerList.isEmpty() && saveData.isUhcOnGoing())
				{
					if (world.getWorldTime() % 20 == 0)
					{
						if(!saveData.isGraceFinished())
						{
							if(saveData.isGraceEnabled())
							{
								if(timerData.getGlowTimer() != this.graceTimer) 
								{
									this.graceTimer = timerData.getGlowTimer();
									if(saveData.isGraceFinished())
									{
										saveData.setGraceFinished(false);
										saveData.markDirty();
									}
								}
								
								if(timerData.getGlowTimer() >= TimerHandler.tickTime(saveData.getGraceTime()))
								{
									this.graceTimer = TimerHandler.tickTime(saveData.getGraceTime());
									saveData.setGraceFinished(true);
									saveData.markDirty();
								}
								else
								{
									++this.graceTimer;
									timerData.setGraceTimer(this.graceTimer);
									timerData.markDirty();
								}			
							}
							else
							{
								if(timerData.getGraceTimer() != 0)
								{
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
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
			if(saveData.isGraceEnabled() && !saveData.isGraceFinished())
			{
				if(event.getEntityLiving() instanceof EntityPlayer)
				{
					Entity trueSource = event.getSource().getTrueSource();
					if(trueSource != null && trueSource instanceof EntityPlayer)
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void graceTimerEvent(LivingHurtEvent event) {
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
			if(saveData.isGraceEnabled() && !saveData.isGraceFinished())
			{
				if(event.getEntityLiving() instanceof EntityPlayer)
				{
					Entity trueSource = event.getSource().getTrueSource();
					if(trueSource != null && trueSource instanceof EntityPlayer)
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}
}
