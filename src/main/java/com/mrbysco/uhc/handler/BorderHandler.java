package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.utils.SpreadPosition;
import com.mrbysco.uhc.utils.SpreadUtil;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class BorderHandler {
	public int controlTimer;

	@SubscribeEvent
	public void borderHandling(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			Level world = event.world;
			MinecraftServer server = world.getServer();
			List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

			ServerLevel overworld = server.getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
				WorldBorder border = world.getWorldBorder();

				if (saveData.getOriginalBorderCenterX() == Integer.MAX_VALUE) {
					double originalX = border.getCenterX();
					saveData.setOriginalBorderCenterX(originalX);
					saveData.setDirty();
				}
				if (saveData.getOriginalBorderCenterZ() == Integer.MAX_VALUE) {
					double originalZ = border.getCenterZ();
					saveData.setOriginalBorderCenterZ(originalZ);
					saveData.setDirty();
				}

				if (saveData.getBorderCenterX() == Integer.MAX_VALUE) {
					double originalX;
					if (saveData.getOriginalBorderCenterX() != Integer.MAX_VALUE) {
						originalX = saveData.getOriginalBorderCenterX();
					} else {
						originalX = border.getCenterX();
					}
					saveData.setBorderCenterX(originalX);
					saveData.setDirty();
				}
				if (saveData.getBorderCenterZ() == Integer.MAX_VALUE) {
					double originalZ;
					if (saveData.getOriginalBorderCenterX() != Integer.MAX_VALUE) {
						originalZ = saveData.getOriginalBorderCenterZ();
					} else {
						originalZ = border.getCenterZ();
					}
					saveData.setBorderCenterZ(originalZ);
					saveData.setDirty();
				}

				if (saveData.isUhcOnGoing()) {
					if (saveData.isShrinkEnabled()) {
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

						if (shrinkMode.equals("Shrink") && shrinkFlag && !shrinkApplied) {
							server.getCommands().performCommand(server.createCommandSourceStack(), "/worldborder set " + newSize + " " + shrinkTimeSec);

							//border.setTransition(oldSize, newSize, shrinkTimeSec);
							for (ServerPlayer player : playerList) {
								ClientboundSetTitleTextPacket setTitlePacket = new ClientboundSetTitleTextPacket(Component.translatable("message.border.moving"));
								player.connection.send(setTitlePacket);
							}

							saveData.setShrinkApplied(true);
							saveData.setDirty();
						}
						if (shrinkMode.equals("Arena") && shrinkFlag && !shrinkApplied) {
							try {
								SpreadUtil.spread(playerList, new SpreadPosition(centerX, centerZ), spreadDistance / ((double) oldSize / (double) newSize),
										(newSize / 2.0), world, true);
							} catch (CommandRuntimeException e) {
								e.printStackTrace();
							}

							//border.setSize(newSize);
							server.getCommands().performCommand(server.createCommandSourceStack(), "/worldborder set " + newSize);

							saveData.setShrinkApplied(true);
							saveData.setDirty();
						}
						if (shrinkMode.equals("Control") && shrinkFlag) {
							boolean controlled = timerData.isControlled();

							AABB hitbox = new AABB(saveData.getBorderCenterX() - 0.5f, 0 - 0.5f, saveData.getBorderCenterZ() - 0.5f, saveData.getBorderCenterX() + 0.5f, 256 + 0.5f, saveData.getBorderCenterZ() + 0.5f)
									.expandTowards(-20, -20, -20).expandTowards(20, 20, 20);
							ArrayList<ServerPlayer> collidingList = new ArrayList<>(world.getEntitiesOfClass(ServerPlayer.class, hitbox));

							if (collidingList.isEmpty()) {
								controlled = false;
								if (timerData.isControlled() != controlled) {
									timerData.setControlled(controlled);
									timerData.setDirty();
								}
							} else {
								if (!collidingList.get(0).isSpectator()) {
									controlled = true;
									if (timerData.isControlled() != controlled) {
										timerData.setControlled(controlled);
										timerData.setDirty();
									}
								}
							}

							if (controlled) {
								if (controlTimer >= 20) {
									for (ServerPlayer players : playerList) {
										ServerPlayer player = collidingList.get(0);

										if (player.getTeam() != null) {
											String teamName = player.getTeam().getName();
											players.displayClientMessage(Component.translatable("book.uhc.shrink.control", teamName.substring(0, 1).toUpperCase() + teamName.substring(1)), true);
										} else
											players.displayClientMessage(Component.translatable("book.uhc.shrink.control", player.getName()), true);
									}

									int controlSize = border.getAbsoluteMaxSize() - 1;
									//border.setSize(controlSize);
									server.getCommands().performCommand(server.createCommandSourceStack(), "/worldborder set " + controlSize);

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