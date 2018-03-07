package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BorderHandler {
	@SubscribeEvent
	public void borderHandling(TickEvent.PlayerTickEvent event)
	{
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			World world = player.world;
			NBTTagCompound entityData = player.getEntityData();
			Scoreboard scoreboard = world.getScoreboard();
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			UHCTimerData timerData = UHCTimerData.getForWorld(world);
    		WorldBorder border = world.getWorldBorder();
    		
			if(saveData.isUhcOnGoing())
			{
				if(saveData.isShrinkEnabled())
				{
					String shrinkMode = saveData.getShrinkMode();
					//shrink time * 60 as worldborder checks in seconds
		    		
					if(shrinkMode.equals("Shrink"))
					{
						
					}
					if(shrinkMode.equals("Arena"))
					{

					}
					if(shrinkMode.equals("Control"))
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
				}
				if(saveData.getOriginalBorderCenterZ() == -1)
				{
					double originalZ = border.getCenterX();
					saveData.setOriginalBorderCenterZ(originalZ);
				}
				
				if(saveData.getBorderCenterX() == -1)
				{
					if(saveData.getOriginalBorderCenterX() != -1)
					{
						double originalX = saveData.getOriginalBorderCenterX();
						saveData.setBorderCenterX(originalX);
					}
					else
					{
						double originalZ = border.getCenterX();
						saveData.setBorderCenterZ(originalZ);
					}
				}
				if(saveData.getBorderCenterZ() == -1)
				{
					if(saveData.getOriginalBorderCenterX() != -1)
					{
						double originalZ = saveData.getOriginalBorderCenterZ();
						saveData.setBorderCenterZ(originalZ);
					}
					else
					{
						double originalZ = border.getCenterX();
						saveData.setBorderCenterZ(originalZ);
					}
				}
			}
		}
	}
}
