package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.utils.SpreadPosition;
import com.Mrbysco.UHC.utils.SpreadUtil;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BorderHandler {
	public int controlTimer;
	
	@SubscribeEvent
	public void borderHandling(TickEvent.WorldTickEvent event)
	{
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			MinecraftServer server = world.getMinecraftServer();
			ArrayList<EntityPlayerMP> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
			
			Scoreboard scoreboard = world.getScoreboard();
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			UHCTimerData timerData = UHCTimerData.getForWorld(world);
    		WorldBorder border = world.getWorldBorder();
    		
    		if(saveData.getOriginalBorderCenterX() == -1)
			{
				double originalX = border.getCenterX();
				saveData.setOriginalBorderCenterX(originalX);
				saveData.markDirty();
			}
			if(saveData.getOriginalBorderCenterZ() == -1)
			{
				double originalZ = border.getCenterZ();
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
					double originalX = border.getCenterX();
					saveData.setBorderCenterX(originalX);
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
					double originalZ = border.getCenterZ();
					saveData.setBorderCenterZ(originalZ);
					saveData.markDirty();
				}
			}
    		
			if(!saveData.isUhcOnGoing())
			{
				if(saveData.isShrinkEnabled())
				{
					int shrinkTimer = timerData.getShrinkTimeUntil();
					boolean shrinkFlag = shrinkTimer == TimerHandler.tickTime(saveData.getShrinkTimer());
					boolean shrinkApplied = saveData.isShrinkApplied();
					String shrinkMode = saveData.getShrinkMode();
					
					double centerX = saveData.getBorderCenterX();
					double centerZ = saveData.getBorderCenterZ();
					double spreadDistance = saveData.getSpreadDistance();
					int BorderSize = saveData.getBorderSize();
					
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
					if(shrinkMode.equals("Arena") && shrinkFlag && !shrinkApplied)
					{
						try {
							SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance / ((double) oldSize / (double) newSize), 
								newSize, world, true);
						} catch (CommandException e) {
							e.printStackTrace();
						}
						
						border.setSize(newSize);
						
						saveData.setShrinkApplied(true);
						saveData.markDirty();
					}
					if(shrinkMode.equals("Control") && shrinkFlag)
					{
						boolean controlled = timerData.isControlled();
						
						AxisAlignedBB hitbox = new AxisAlignedBB(saveData.getBorderCenterX() - 0.5f, 0 - 0.5f, saveData.getBorderCenterZ() - 0.5f, saveData.getBorderCenterX() + 0.5f, 256 + 0.5f, saveData.getBorderCenterZ() + 0.5f)
								.expand(-20, -20, -20).expand(20, 20, 20);
						ArrayList<ScorePlayerTeam> teams = new ArrayList<>(scoreboard.getTeams());
						
						ArrayList<EntityPlayerMP> collidingList = new ArrayList<>(world.getEntitiesWithinAABB(EntityPlayerMP.class, hitbox));
						
						if(collidingList.isEmpty())
						{
							controlled = false;
							if(timerData.isControlled() != controlled)
							{
								timerData.setControlled(controlled);
								timerData.markDirty();
							}
						}
						else
						{
							if(!collidingList.get(0).isSpectator())
							{
								controlled = true;
								if(timerData.isControlled() != controlled)
								{
									timerData.setControlled(controlled);
									timerData.markDirty();
								}
							}
						}
						
						if(controlled)
						{
							if(controlTimer >= 20)
							{
								for(EntityPlayerMP players : playerList)
								{
									EntityPlayerMP player = collidingList.get(0);
									
									if(player.getTeam() != null)
									{
										String teamName = player.getTeam().getName();
										players.sendStatusMessage(new TextComponentTranslation("book.uhc.shrink.control", new Object[] {teamName.substring(0, 1).toUpperCase() + teamName.substring(1)}), true);
									}
									else
										players.sendStatusMessage(new TextComponentTranslation("book.uhc.shrink.control", new Object[] {player.getName()}), true);
								}
								
								int size = border.getSize();
								border.setSize(size - 1);
								
								controlTimer = 0;
							}
							else
							{
								this.controlTimer++;
							}
						}
					}
				}
			}
		}
	}
}
