package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.UltraHardCoremod;
import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BorderHandler {
	@SubscribeEvent
	public void borderHandling(TickEvent.WorldTickEvent event)
	{
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			Scoreboard scoreboard = world.getScoreboard();
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			UHCTimerData timerData = UHCTimerData.getForWorld(world);
    		WorldBorder border = world.getWorldBorder();
    		
			if(saveData.isUhcOnGoing())
			{
				if(saveData.isShrinkEnabled())
				{
					int shrinkTimer = timerData.getShrinkTimeUntil();
					boolean shrinkFlag = shrinkTimer == UltraHardCoremod.tickTime(saveData.getShrinkTimer());
					boolean shrinkApplied = saveData.isShrinkApplied();
					String shrinkMode = saveData.getShrinkMode();
					
					int oldSize = saveData.getBorderSize();
					int newSize = saveData.getShrinkSize();
					int shrinkTimeSec = saveData.getShrinkTimer()*60;
					//shrink time * 60 as worldborder checks in seconds
		    		
					if(shrinkMode.equals("Shrink") && shrinkFlag && !shrinkApplied)
					{
						border.setTransition(oldSize, newSize, shrinkTimeSec);
						saveData.setShrinkApplied(true);
						saveData.markDirty();
					}
					if(shrinkMode.equals("Arena") && shrinkFlag )
					{
						
					}
					if(shrinkMode.equals("Control") && shrinkFlag)
					{

					}
				}
			}
			else
			{
				if(saveData.getOriginalBorderCenterX() == -1)
				{
					double originalX = border.getCenterX();
					saveData.setOriginalBorderCenterX(originalX);
					saveData.markDirty();
				}
				if(saveData.getOriginalBorderCenterZ() == -1)
				{
					double originalZ = border.getCenterX();
					saveData.setOriginalBorderCenterZ(originalZ);
					saveData.markDirty();
				}
				
				if(saveData.getBorderCenterX() == -1)
				{
					if(saveData.getOriginalBorderCenterX() != -1)
					{
						double originalX = saveData.getOriginalBorderCenterX();
						saveData.setBorderCenterX(originalX);
						saveData.markDirty();
					}
					else
					{
						double originalZ = border.getCenterX();
						saveData.setBorderCenterZ(originalZ);
						saveData.markDirty();
					}
				}
				if(saveData.getBorderCenterZ() == -1)
				{
					if(saveData.getOriginalBorderCenterX() != -1)
					{
						double originalZ = saveData.getOriginalBorderCenterZ();
						saveData.setBorderCenterZ(originalZ);
						saveData.markDirty();
					}
					else
					{
						double originalZ = border.getCenterX();
						saveData.setBorderCenterZ(originalZ);
						saveData.markDirty();
					}
				}
			}
		}
	}
}
