package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.util.SpreadPosition;
import com.mrbysco.uhc.util.SpreadUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class BorderHandler {
	public static int controlTimer;

	@SubscribeEvent
	public static void borderHandling(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.getGameTime() % 20 == 0 && level.dimension().equals(Level.OVERWORLD)) {
			UHCSaveData saveData = UHCSaveData.get(level);
			WorldBorder border = level.getWorldBorder();

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
		}
	}

	@SubscribeEvent
	public static void shrinkHandler(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			MinecraftServer server = level.getServer();

			UHCSaveData saveData = UHCSaveData.get(level);

			if (saveData.isUhcOnGoing() && saveData.isShrinkEnabled()) {
				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
				UHCTimerData timerData = UHCTimerData.get(level);
				WorldBorder border = level.getWorldBorder();

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
					border.lerpSizeBetween(oldSize, newSize, shrinkTimeSec);
					for (ServerPlayer player : playerList) {
						ClientboundSetTitleTextPacket setTitlePacket = new ClientboundSetTitleTextPacket(Component.translatable("uhc.message.border.moving"));
						player.connection.send(setTitlePacket);
					}

					saveData.setShrinkApplied(true);
					saveData.setDirty();
				}
				if (shrinkMode.equals("Arena") && shrinkFlag && !shrinkApplied) {
					try {
						SpreadUtil.spread(playerList, new SpreadPosition(centerX, centerZ), spreadDistance / ((double) oldSize / (double) newSize),
								(newSize / 2.0), level, true);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}

					border.setSize(newSize);

					saveData.setShrinkApplied(true);
					saveData.setDirty();
				}
				if (shrinkMode.equals("Control") && shrinkFlag) {
					boolean controlled = timerData.isControlled();

					AABB hitbox = new AABB(
							saveData.getBorderCenterX() - 0.5f, 0 - 0.5f, saveData.getBorderCenterZ() - 0.5f,
							saveData.getBorderCenterX() + 0.5f, 256 + 0.5f, saveData.getBorderCenterZ() + 0.5f)
							.inflate(20);
					List<ServerPlayer> collidingList = new ArrayList<>(level.getEntitiesOfClass(ServerPlayer.class, hitbox));

					if (collidingList.isEmpty()) {
						controlled = false;
						if (timerData.isControlled() != controlled) {
							timerData.setControlled(controlled);
							timerData.setDirty();
						}
					} else {
						if (!collidingList.getFirst().isSpectator()) {
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
								ServerPlayer player = collidingList.getFirst();

								if (player.getTeam() != null) {
									String teamName = player.getTeam().getName();
									players.displayClientMessage(Component.translatable("book.uhc.shrink.control", teamName.substring(0, 1).toUpperCase() + teamName.substring(1)), true);
								} else
									players.displayClientMessage(Component.translatable("book.uhc.shrink.control", player.getName()), true);
							}

							int controlSize = border.getAbsoluteMaxSize() - 1;
							border.setSize(controlSize);

							controlTimer = 0;
						} else {
							controlTimer++;
						}
					}
				}
			}
		}
	}
}
