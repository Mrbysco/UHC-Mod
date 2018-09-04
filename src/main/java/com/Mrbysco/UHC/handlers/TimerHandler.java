package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.utils.TimerThing;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TimerHandler {
	public int shrinkTimeUntil;
	public int timeLockTimer;
	public int minuteMarkTimer;
	public int nameTimer;
	public int glowTimer;
	public TimerThing milliTime;
	
	public TimerHandler() {
		milliTime = new TimerThing();
	}
	
	@SubscribeEvent
	public void UHCBookEvent(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
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
					if (System.currentTimeMillis() > milliTime.getMilliTime() + 1000L)
					{
						milliTime.setMilliTimeToCurrent();
						
						if(!saveData.isShrinkApplied())
						{
							if(saveData.isShrinkEnabled())
							{
								if(timerData.getShrinkTimeUntil() != this.shrinkTimeUntil)
								{
									this.shrinkTimeUntil = timerData.getShrinkTimeUntil();
								}
								
								if(timerData.getShrinkTimeUntil() >= tickTime(saveData.getShrinkTimer()))
								{
									this.shrinkTimeUntil = tickTime(saveData.getShrinkTimer());
								}
								else
								{
									++this.shrinkTimeUntil;
									timerData.setShrinkTimeUntil(this.shrinkTimeUntil);
									timerData.markDirty();
								}
							}
							else
							{
								timerData.setShrinkTimeUntil(0);
								timerData.markDirty();
							}
						}
						
						if(!saveData.isTimeLockApplied())
						{
							if(saveData.isTimeLock())
							{
								if(timerData.getTimeLockTimer() != this.timeLockTimer)
									this.timeLockTimer = timerData.getTimeLockTimer();
								
								if(timerData.getTimeLockTimer() >= tickTime(saveData.getTimeLockTimer()))
								{
									this.timeLockTimer = tickTime(saveData.getTimeLockTimer());
								}
								else
								{
									++this.timeLockTimer;
									timerData.setTimeLockTimer(this.timeLockTimer);
									timerData.markDirty();
								}			
							}
							else
							{
								timerData.setTimeLockTimer(0);
								timerData.markDirty();
							}
						}
						
						if(saveData.isMinuteMark())
						{
							if(timerData.getMinuteMarkTimer() != this.minuteMarkTimer)
							{
								this.minuteMarkTimer = timerData.getMinuteMarkTimer();
							}
							
							if(timerData.getMinuteMarkTimer() >= tickTime(saveData.getMinuteMarkTime()))
							{
								this.minuteMarkTimer = tickTime(saveData.getMinuteMarkTime());
							}
							else
							{
								++this.minuteMarkTimer;
								timerData.setMinuteMarkTimer(this.minuteMarkTimer);
								timerData.markDirty();
							}
						}
						else
						{
							timerData.setMinuteMarkTimer(0);
							timerData.markDirty();
						}
						
						if(!saveData.isTimedNamesApplied())
						{
							if(saveData.isTimedNames())
							{
								if(timerData.getNameTimer() != this.nameTimer)
									this.nameTimer = timerData.getNameTimer();
								
								if(timerData.getNameTimer() >= tickTime(saveData.getNameTimer()))
								{
									this.nameTimer = tickTime(saveData.getNameTimer());
								}
								else
								{
									++this.nameTimer;
									timerData.setNameTimer(this.nameTimer);
									timerData.markDirty();
								}			
							}
							else
							{
								timerData.setNameTimer(0);
								timerData.markDirty();
							}
						}
						
						if(!saveData.isGlowTimeApplied())
						{
							if(saveData.isTimedGlow())
							{
								if(timerData.getGlowTimer() != this.glowTimer)
									this.glowTimer = timerData.getGlowTimer();
								
								if(timerData.getGlowTimer() >= tickTime(saveData.getGlowTime()))
								{
									this.glowTimer = tickTime(saveData.getGlowTime());
								}
								else
								{
									++this.glowTimer;
									timerData.setGlowTimer(this.glowTimer);
									timerData.markDirty();
								}			
							}
							else
							{
								timerData.setGlowTimer(0);
								timerData.markDirty();
							}
						}
					}
				}
			}
			else
			{
				milliTime.setMilliTimeToCurrent();
			}
		}
	}
	
	public static int tickTime(int oldTime)
	{
		return oldTime * 60;
	}
}
