package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.lists.info.RespawnInfo;
import com.mrbysco.uhc.recipes.NBTChangeRecipe;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ModCompatHandler {
	public int twilightBossGraceTimer;

	private final Map<BlockPos, RespawnInfo> respawnList = new HashMap<>();

	@SubscribeEvent
	public void TwilightHandler(TickEvent.LevelTickEvent event) {
		Level level = event.level;
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() &&
				level.dimension().equals(Level.OVERWORLD) && ModList.get().isLoaded("twilightforest")) {
			ServerLevel serverLevel = (ServerLevel) event.level;

			Scoreboard scoreboard = serverLevel.getScoreboard();
			ServerLevel overworld = (ServerLevel) level;
			MinecraftServer server = serverLevel.getServer();
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
					if (serverLevel.getGameTime() % 20 == 0) {
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
							List<ServerPlayer> collidingList = new ArrayList<>(serverLevel.getEntitiesOfClass(ServerPlayer.class, hitbox));

							List<Monster> collidingBossMobs = new ArrayList<>(serverLevel.getEntitiesOfClass(Monster.class, hitbox));

							if (serverLevel.getBlockState(pos).getBlock() instanceof twilightforest.block.BossSpawnerBlock) {
								if (!info.isSpawnerExists())
									info.setSpawnerExists(true);
							}

							if (!collidingBossMobs.isEmpty() && !info.isBossExists()) {
								for (Monster mob : collidingBossMobs) {
									if (!mob.canChangeDimensions() && ForgeRegistries.ENTITY_TYPES.getKey(mob.getType()).getNamespace().equals("twilightforest")) {
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

												serverLevel.setBlockAndUpdate(pos, state);
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

	@SubscribeEvent
	public void mobNBTChanger(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		if (!level.isClientSide) {
			ResourceLocation location = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType());
			if (event.getEntity() instanceof LivingEntity livingEntity) {
				NBTChangeRecipe changeRecipe = level.getRecipeManager().getAllRecipesFor(ModRecipes.NBT_CHANGE_TYPE.get()).stream()
						.filter(recipe -> recipe.getEntityType().equals(location)).findFirst().orElse(null);
				if (changeRecipe != null) {
					CompoundTag entityTag = entityToNBT(livingEntity);
					CompoundTag entityTagCopy = entityTag.copy();
					CompoundTag entityTag2 = changeRecipe.getNBTTag();

					if (!entityTag2.isEmpty()) {
						entityTagCopy.merge(entityTag2);
						UUID uuid = livingEntity.getUUID();
						livingEntity.load(entityTagCopy);
						livingEntity.setUUID(uuid);
					}
				}
			}
		}
	}

	public static CompoundTag entityToNBT(Entity theEntity) {
		CompoundTag compoundTag = theEntity.saveWithoutId(new CompoundTag());
		if (theEntity instanceof Player) {
			ItemStack itemstack = ((Player) theEntity).getInventory().getSelected();
			if (!itemstack.isEmpty()) {
				compoundTag.put("SelectedItem", itemstack.save(new CompoundTag()));
			}
		}
		return compoundTag;
	}
}
