package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.UltraHardCoremod;
import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.lists.EntityDataChangeList;
import com.mrbysco.uhc.lists.EntityDataChangeList.AttributeChange;
import com.mrbysco.uhc.lists.info.RespawnInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModCompatHandler {
	public int twilightBossGraceTimer;

	private final Map<BlockPos, RespawnInfo> respawnList = new HashMap<>();

	@SubscribeEvent
	public void TwilightHandler(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && ModList.get().isLoaded("twilightforest")) {
			ServerLevel world = (ServerLevel) event.world;

			Scoreboard scoreboard = world.getScoreboard();
			ServerLevel overworld = event.world.getServer().getLevel(Level.OVERWORLD);
			if (overworld != null) {
				MinecraftServer server = world.getServer();
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

				if (saveData.isUhcOnGoing()) {
//					world.tickableBlockEntities.forEach((tile) -> { TODO: Get the boss spawners working
//						if (!tile.isRemoved() && tile instanceof BossSpawnerTileEntity) {
//							BlockPos pos = tile.getBlockPos();
//							BlockState state = tile.getBlockState();
//							if (respawnList.containsKey(pos)) {
//								RespawnInfo info = new RespawnInfo(state);
//								info.setSpawnerExists(true);
//								respawnList.put(pos, info);
//							}
//						}
//					});

					if (!playerList.isEmpty() && !respawnList.isEmpty()) {
						if (world.getGameTime() % 20 == 0) {
							if (!saveData.getTwilightRespawn()) {
								if (timerData.getTwilightBossGraceTimer() != this.twilightBossGraceTimer) {
									this.twilightBossGraceTimer = timerData.getTwilightBossGraceTimer();
									if (saveData.getTwilightRespawn()) {
										saveData.setTwilightRespawn(false);
										saveData.setDirty();
									}
								}

								if (timerData.getTwilightBossGraceTimer() >= TimerHandler.tickTime(UHCConfig.COMMON.twilightRespawnTime.get())) {
									this.twilightBossGraceTimer = TimerHandler.tickTime(UHCConfig.COMMON.twilightRespawnTime.get());
									saveData.setTwilightRespawn(true);
									saveData.setDirty();
								} else {
									++this.twilightBossGraceTimer;
									timerData.setTwilightBossGraceTimer(this.twilightBossGraceTimer);
									timerData.setDirty();
								}
							}
						}
					}

					if (!saveData.isUhcIsFinished() && saveData.getTwilightRespawn()) {
						if (!respawnList.isEmpty()) {
							for (Map.Entry<BlockPos, RespawnInfo> infoEntry : respawnList.entrySet()) {
								BlockPos pos = infoEntry.getKey();
								RespawnInfo info = infoEntry.getValue();
								BlockState state = info.getState();
								AABB hitbox = new AABB(pos.getX() - 0.5f, 0 - 0.5f, pos.getZ() - 0.5f, pos.getX() + 0.5f, 256 + 0.5f, pos.getZ() + 0.5f)
										.expandTowards(-50, -50, -50).expandTowards(50, 50, 50);
								ArrayList<ServerPlayer> collidingList = new ArrayList<>(world.getEntitiesOfClass(ServerPlayer.class, hitbox));

								ArrayList<Monster> collidingBossMobs = new ArrayList<>(world.getEntitiesOfClass(Monster.class, hitbox));

								if (world.getBlockState(pos).getBlock() instanceof twilightforest.block.BossSpawnerBlock) {
									if (!info.isSpawnerExists())
										info.setSpawnerExists(true);
								}

								if (!collidingBossMobs.isEmpty() && !info.isBossExists()) {
									for (Monster mob : collidingBossMobs) {
										if (!mob.canChangeDimensions() && ForgeRegistries.ENTITIES.getKey(mob.getType()).getNamespace().equals("twilightforest")) {
											info.setBossExists(true);
										}
									}
								}

								if (!collidingList.isEmpty()) {
									for (ServerPlayer player : collidingList) {
										Team team = player.getTeam();

										if (team != null && team != scoreboard.getPlayerTeam("solo") && !player.isSpectator()) {
											if (!info.teamsReached.contains(team)) {
												if (!info.isBossExists() && !info.isSpawnerExists()) {
													info.teamsReached.add(team);

													world.setBlockAndUpdate(pos, state);
													info.setBossExists(true);
												}
											}
										}
									}
								}
								infoEntry.setValue(info);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void mobDataChanger(EntityJoinWorldEvent event) {
		Level world = event.getWorld();
		if (!world.isClientSide) {
			if (event.getEntity() instanceof LivingEntity && EntityDataChangeList.dataMap != null && !EntityDataChangeList.dataMap.isEmpty()) {
				LivingEntity livingEntity = (LivingEntity) event.getEntity();
				if (EntityDataChangeList.dataMap.containsKey(livingEntity.getType())) {
					AttributeChange attributeChange = EntityDataChangeList.dataMap.get(livingEntity.getType());
					Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeChange.getAttributeLocation());
					if (attribute != null) {
						AttributeInstance instance = livingEntity.getAttribute(attribute);
						if (instance != null) {
							instance.setBaseValue(attributeChange.getValue());
						} else {
							UltraHardCoremod.LOGGER.error(String.format("Can't find attribute %s on entity %s", attribute.getDescriptionId(), ForgeRegistries.ENTITIES.getKey(livingEntity.getType())));
						}
					} else {
						UltraHardCoremod.LOGGER.error("Can't find attribute by the resource name: " + attribute.getDescriptionId());
					}
				}
			}
		}
	}
}
