package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.utils.SpreadPosition;
import com.mrbysco.uhc.utils.SpreadUtil;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class BorderHandler {
	public int controlTimer;
	
	@SubscribeEvent
	public void borderHandling(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			World world = event.world;
			MinecraftServer server = world.getServer();
			List<ServerPlayerEntity> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
			
			ServerWorld overworld = server.getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
	    		WorldBorder border = world.getWorldBorder();
	    		
	    		if(saveData.getOriginalBorderCenterX() == Integer.MAX_VALUE) {
					double originalX = border.getCenterX();
					saveData.setOriginalBorderCenterX(originalX);
					saveData.markDirty();
				}
				if(saveData.getOriginalBorderCenterZ() == Integer.MAX_VALUE) {
					double originalZ = border.getCenterZ();
					saveData.setOriginalBorderCenterZ(originalZ);
					saveData.markDirty();
				}
				
				if(saveData.getBorderCenterX() == Integer.MAX_VALUE) {
					double originalX;
					if(saveData.getOriginalBorderCenterX() != Integer.MAX_VALUE) {
						originalX = saveData.getOriginalBorderCenterX();
					} else {
						originalX = border.getCenterX();
					}
					saveData.setBorderCenterX(originalX);
					saveData.markDirty();
				}
				if(saveData.getBorderCenterZ() == Integer.MAX_VALUE) {
					double originalZ;
					if(saveData.getOriginalBorderCenterX() != Integer.MAX_VALUE) {
						originalZ = saveData.getOriginalBorderCenterZ();
					} else {
						originalZ = border.getCenterZ();
					}
					saveData.setBorderCenterZ(originalZ);
					saveData.markDirty();
				}
	    		
				if(saveData.isUhcOnGoing()) {
					if(saveData.isShrinkEnabled()) {
						int shrinkTimer = timerData.getShrinkTimeUntil();
						boolean shrinkFlag = shrinkTimer == TimerHandler.tickTime(saveData.getShrinkTimer());
						boolean shrinkApplied = saveData.isShrinkApplied();
						String shrinkMode = saveData.getShrinkMode();
						
						double centerX = saveData.getBorderCenterX();
						double centerZ = saveData.getBorderCenterZ();
						double spreadDistance = saveData.getSpreadDistance();

						int oldSize = saveData.getBorderSize();
						int newSize = saveData.getShrinkSize();
		                long shrinkTimeSec = saveData.getShrinkOvertime() > 0 ? saveData.getShrinkOvertime() * 60L : 0L;
						
						if(shrinkMode.equals("Shrink") && shrinkFlag && !shrinkApplied) {
							server.getCommandManager().handleCommand(server.getCommandSource(), "/worldborder set " + newSize + " " + shrinkTimeSec);

							//border.setTransition(oldSize, newSize, shrinkTimeSec);
							for(ServerPlayerEntity player : playerList) {
								STitlePacket spackettitle1 = new STitlePacket(STitlePacket.Type.TITLE, new TranslationTextComponent("message.border.moving"));
								player.connection.sendPacket(spackettitle1);
							}
							
							saveData.setShrinkApplied(true);
							saveData.markDirty();
						}
						if(shrinkMode.equals("Arena") && shrinkFlag && !shrinkApplied) {
							try {
								SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance / ((double) oldSize / (double) newSize), 
									(newSize / 2.0), world, true);
							} catch (CommandException e) {
								e.printStackTrace();
							}
							
							//border.setSize(newSize);
							server.getCommandManager().handleCommand(server.getCommandSource(), "/worldborder set " + newSize);

							saveData.setShrinkApplied(true);
							saveData.markDirty();
						}
						if(shrinkMode.equals("Control") && shrinkFlag) {
							boolean controlled = timerData.isControlled();
							
							AxisAlignedBB hitbox = new AxisAlignedBB(saveData.getBorderCenterX() - 0.5f, 0 - 0.5f, saveData.getBorderCenterZ() - 0.5f, saveData.getBorderCenterX() + 0.5f, 256 + 0.5f, saveData.getBorderCenterZ() + 0.5f)
									.expand(-20, -20, -20).expand(20, 20, 20);
							ArrayList<ServerPlayerEntity> collidingList = new ArrayList<>(world.getEntitiesWithinAABB(ServerPlayerEntity.class, hitbox));
							
							if(collidingList.isEmpty()) {
								controlled = false;
								if(timerData.isControlled() != controlled) {
									timerData.setControlled(controlled);
									timerData.markDirty();
								}
							} else {
								if(!collidingList.get(0).isSpectator()) {
									controlled = true;
									if(timerData.isControlled() != controlled) {
										timerData.setControlled(controlled);
										timerData.markDirty();
									}
								}
							}

							if(controlled) {
								if(controlTimer >= 20) {
									for(ServerPlayerEntity players : playerList) {
										ServerPlayerEntity player = collidingList.get(0);
										
										if(player.getTeam() != null) {
											String teamName = player.getTeam().getName();
											players.sendStatusMessage(new TranslationTextComponent("book.uhc.shrink.control", teamName.substring(0, 1).toUpperCase() + teamName.substring(1)), true);
										}
										else
											players.sendStatusMessage(new TranslationTextComponent("book.uhc.shrink.control", player.getName()), true);
									}
									
									int controlSize = border.getSize() - 1;
									//border.setSize(controlSize);
									server.getCommandManager().handleCommand(server.getCommandSource(), "/worldborder set " + controlSize);
									
									controlTimer = 0;
								} else {
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
