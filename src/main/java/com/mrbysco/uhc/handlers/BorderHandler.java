package com.mrbysco.uhc.handlers;

import com.mrbysco.uhc.init.UHCSaveData;
import com.mrbysco.uhc.init.UHCTimerData;
import com.mrbysco.uhc.utils.SpreadPosition;
import com.mrbysco.uhc.utils.SpreadUtil;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

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
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
	    		WorldBorder border = world.getWorldBorder();
	    		
	    		if(saveData.getOriginalBorderCenterX() == Integer.MAX_VALUE)
				{
					double originalX = border.getCenterX();
					saveData.setOriginalBorderCenterX(originalX);
					saveData.markDirty();
				}
				if(saveData.getOriginalBorderCenterZ() == Integer.MAX_VALUE)
				{
					double originalZ = border.getCenterZ();
					saveData.setOriginalBorderCenterZ(originalZ);
					saveData.markDirty();
				}
				
				if(saveData.getBorderCenterX() == Integer.MAX_VALUE)
				{
					if(saveData.getOriginalBorderCenterX() != Integer.MAX_VALUE)
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
				if(saveData.getBorderCenterZ() == Integer.MAX_VALUE)
				{
					if(saveData.getOriginalBorderCenterX() != Integer.MAX_VALUE)
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
	    		
				if(saveData.isUhcOnGoing())
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

						int oldSize = saveData.getBorderSize();
						int newSize = saveData.getShrinkSize();
		                long shrinkTimeSec = 0L;
		                
						shrinkTimeSec = saveData.getShrinkOvertime() > 0 ? saveData.getShrinkOvertime() * 60 : 0L;
						
						if(shrinkMode.equals("Shrink") && shrinkFlag && !shrinkApplied)
						{
							server.commandManager.executeCommand(server, "/worldborder set " + newSize + " " + shrinkTimeSec);

							//border.setTransition(oldSize, newSize, shrinkTimeSec);
							for(EntityPlayerMP player : playerList)
							{
								SPacketTitle spackettitle1 = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentTranslation("message.border.moving"));
								player.connection.sendPacket(spackettitle1);
							}
							
							saveData.setShrinkApplied(true);
							saveData.markDirty();
						}
						if(shrinkMode.equals("Arena") && shrinkFlag && !shrinkApplied)
						{
							try {
								SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance / ((double) oldSize / (double) newSize), 
									(newSize / 2), world, true);
							} catch (CommandException e) {
								e.printStackTrace();
							}
							
							//border.setSize(newSize);
							server.commandManager.executeCommand(server, "/worldborder set " + newSize);

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
									
									int controlSize = border.getSize() - 1;
									//border.setSize(controlSize);
									server.commandManager.executeCommand(server, "/worldborder set " + controlSize);
									
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
}
