package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.UltraHardCoremod;
import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.handler.TimerHandler;
import com.mrbysco.uhc.lists.EntityDataChangeList;
import com.mrbysco.uhc.lists.EntityDataChangeList.AttributeChange;
import com.mrbysco.uhc.lists.info.RespawnInfo;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.tileentity.spawner.BossSpawnerTileEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModCompatHandler {
	public int twilightBossGraceTimer;

	private final Map<BlockPos, RespawnInfo> respawnList = new HashMap<>();

	@SubscribeEvent
	public void TwilightHandler(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && ModList.get().isLoaded("twilightforest")) {
			ServerWorld world = (ServerWorld)event.world;
			
			Scoreboard scoreboard = world.getScoreboard();
			ServerWorld overworld = event.world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				MinecraftServer server = world.getServer();
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
				ArrayList<ServerPlayerEntity> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

				if(saveData.isUhcOnGoing()) {
					world.tickableTileEntities.forEach((tile) -> {
						if(!tile.isRemoved() && tile instanceof BossSpawnerTileEntity) {
							BlockPos pos = tile.getPos();
							BlockState state = tile.getBlockState();
							if(respawnList.containsKey(pos)) {
								RespawnInfo info = new RespawnInfo(state);
								info.setSpawnerExists(true);
								respawnList.put(pos, info);
							}
						}
					});

					if(!playerList.isEmpty() && !respawnList.isEmpty()) {
						if (world.getGameTime() % 20 == 0) {
							if(!saveData.getTwilightRespawn()) {
								if(timerData.getTwilightBossGraceTimer() != this.twilightBossGraceTimer) {
									this.twilightBossGraceTimer = timerData.getTwilightBossGraceTimer();
									if(saveData.getTwilightRespawn()) {
										saveData.setTwilightRespawn(false);
										saveData.markDirty();
									}
								}

								if(timerData.getTwilightBossGraceTimer() >= TimerHandler.tickTime(UHCConfig.COMMON.twilightRespawnTime.get())) {
									this.twilightBossGraceTimer = TimerHandler.tickTime(UHCConfig.COMMON.twilightRespawnTime.get());
									saveData.setTwilightRespawn(true);
									saveData.markDirty();
								} else {
									++this.twilightBossGraceTimer;
									timerData.setTwilightBossGraceTimer(this.twilightBossGraceTimer);
									timerData.markDirty();
								}
							}
						}
					}

					if(!saveData.isUhcIsFinished() && saveData.getTwilightRespawn()) {
						if(!respawnList.isEmpty()) {
							for (Map.Entry<BlockPos, RespawnInfo> infoEntry : respawnList.entrySet()) {
								BlockPos pos = infoEntry.getKey();
								RespawnInfo info = infoEntry.getValue();
								BlockState state = info.getState();
								AxisAlignedBB hitbox = new AxisAlignedBB(pos.getX() - 0.5f, 0 - 0.5f, pos.getZ() - 0.5f, pos.getX() + 0.5f, 256 + 0.5f, pos.getZ() + 0.5f)
										.expand(-50, -50, -50).expand(50, 50, 50);
								ArrayList<ServerPlayerEntity> collidingList = new ArrayList<>(world.getEntitiesWithinAABB(ServerPlayerEntity.class, hitbox));

								ArrayList<MonsterEntity> collidingBossMobs = new ArrayList<>(world.getEntitiesWithinAABB(MonsterEntity.class, hitbox));

								if(world.getBlockState(pos).getBlock() instanceof twilightforest.block.BossSpawnerBlock) {
									if(!info.isSpawnerExists())
										info.setSpawnerExists(true);
								}

								if(!collidingBossMobs.isEmpty() && !info.isBossExists()) {
									for (MonsterEntity mob : collidingBossMobs) {
										if(!mob.canChangeDimension() && mob.getType().getRegistryName().getNamespace().equals("twilightforest")) {
											info.setBossExists(true);
										}
									}
								}

								if(!collidingList.isEmpty()) {
									for (ServerPlayerEntity player : collidingList) {
										Team team = player.getTeam();

										if(team != null && team != scoreboard.getTeam("solo") && !player.isSpectator()) {
											if(!info.teamsReached.contains(team)) {
												if(!info.isBossExists() && !info.isSpawnerExists()) {
													info.teamsReached.add(team);

													world.setBlockState(pos, state);
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
		World world = event.getWorld();
		if (!world.isRemote) {
			if (event.getEntity() instanceof LivingEntity && EntityDataChangeList.dataMap != null && !EntityDataChangeList.dataMap.isEmpty()) {
				LivingEntity livingEntity = (LivingEntity) event.getEntity();
				if (EntityDataChangeList.dataMap.containsKey(livingEntity.getType())) {
					AttributeChange attributeChange = EntityDataChangeList.dataMap.get(livingEntity.getType());
					Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeChange.getAttributeLocation());
					if (attribute != null) {
						ModifiableAttributeInstance instance = livingEntity.getAttribute(attribute);
						if(instance != null) {
							instance.setBaseValue(attributeChange.getValue());
						} else {
							UltraHardCoremod.LOGGER.error(String.format("Can't find attribute %s on entity %s", attribute.getAttributeName(), livingEntity.getType().getRegistryName()));
						}
					} else {
						UltraHardCoremod.LOGGER.error("Can't find attribute by the resource name: " + attribute.getAttributeName());
					}
				}
			}
		}
	}
}
