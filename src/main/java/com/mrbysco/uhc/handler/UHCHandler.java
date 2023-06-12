package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.lists.SpawnItemList;
import com.mrbysco.uhc.lists.info.SpawnItemInfo;
import com.mrbysco.uhc.packets.UHCPacketHandler;
import com.mrbysco.uhc.packets.UHCPacketMessage;
import com.mrbysco.uhc.registry.ModRegistry;
import com.mrbysco.uhc.utils.PlayerHelper;
import com.mrbysco.uhc.utils.UHCTeleporter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class UHCHandler {

	public int uhcStartTimer;

	@SubscribeEvent
	public void UHCStartEventWorld(TickEvent.LevelTickEvent event) {
		Level level = event.level;
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			if (level.getGameTime() % 20 == 0) {
				final ServerLevel overworld = (ServerLevel) level;

				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
				MinecraftServer server = overworld.getServer();
				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

				ResourceLocation configDimension = ResourceLocation.tryParse(UHCConfig.COMMON.spawnDimension.get());
				if (!saveData.getUHCDimension().equals(configDimension))
					saveData.setUHCDimension(configDimension);

				if (!playerList.isEmpty()) {
					if (saveData.isUhcStarting()) {
						if (timerData.getUhcStartTimer() != this.uhcStartTimer) {
							this.uhcStartTimer = timerData.getUhcStartTimer();
						}

						if (timerData.getUhcStartTimer() == 2 || timerData.getUhcStartTimer() == 3 || timerData.getUhcStartTimer() == 4 ||
								timerData.getUhcStartTimer() == 5 || timerData.getUhcStartTimer() == 6 || timerData.getUhcStartTimer() == 7) {
							if (timerData.getUhcStartTimer() == 2) {
								sendSystemMessage(playerList, Component.translatable("uhc.start.5"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.setDirty();
							} else if (timerData.getUhcStartTimer() == 3) {
								sendSystemMessage(playerList, Component.translatable("uhc.start.4"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.setDirty();
							} else if (timerData.getUhcStartTimer() == 4) {
								sendSystemMessage(playerList, Component.translatable("uhc.start.3"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.setDirty();
							} else if (timerData.getUhcStartTimer() == 5) {
								sendSystemMessage(playerList, Component.translatable("uhc.start.2"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.setDirty();
							} else if (timerData.getUhcStartTimer() == 6) {
								sendSystemMessage(playerList, Component.translatable("uhc.start.1"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.setDirty();
							} else if (timerData.getUhcStartTimer() == 7) {
								sendSystemMessage(playerList, Component.translatable("uhc.start"));

								timerData.setUhcStartTimer(0);
								saveData.setDirty();
							}
						} else {
							++this.uhcStartTimer;
							timerData.setUhcStartTimer(this.uhcStartTimer);
							timerData.setDirty();
						}
					} else {
						if (timerData.getUhcStartTimer() != 0) {
							timerData.setUhcStartTimer(0);
							timerData.setDirty();
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void UHCStartEventPlayer(TickEvent.PlayerTickEvent event) {
		Player player = event.player;
		Level level = player.level();
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			final ServerLevel overworld = (ServerLevel) level;

			UHCSaveData saveData = UHCSaveData.get(overworld);
			CompoundTag entityData = player.getPersistentData();

			if (saveData.isUhcStarting()) {
				if (!entityData.contains("startFatigue"))
					entityData.putBoolean("startFatigue", true);

				if (this.uhcStartTimer == 7) {
					if (!SpawnItemList.spawnItemList.isEmpty() && SpawnItemList.spawnItemList != null) {
						for (SpawnItemInfo info : SpawnItemList.spawnItemList) {
							for (int i = 0; i < info.getStackCount(); i++) {
								giveResult(player, info.getStack(i));
							}
						}
					}

					player.removeAllEffects();
					entityData.putBoolean("startFatigue", false);

					if (player.getActiveEffects().size() > 0)
						player.removeAllEffects();

					saveData.setUhcStarting(false);
					saveData.setUhcOnGoing(true);
				} else {
					if (player.getEffect(MobEffects.DIG_SLOWDOWN) == null)
						player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 32767 * 20, 10, true, false));

					if (player.getEffect(MobEffects.MOVEMENT_SLOWDOWN) == null)
						player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 32767 * 20, 10, true, false));

					if (player.getInventory().contains(new ItemStack(ModRegistry.UHC_BOOK.get()))) {
						int bookSlot = player.getInventory().findSlotMatchingUnusedItem(new ItemStack(ModRegistry.UHC_BOOK.get()));
						if (bookSlot != -1)
							player.getInventory().removeItemNoUpdate(bookSlot);
					}

					if (!player.getInventory().getItem(39).isEmpty())
						player.getInventory().removeItemNoUpdate(39);
				}
			}
			if (saveData.isUhcOnGoing()) {
				if (entityData.getBoolean("startFatigue")) {
					player.removeAllEffects();

					if (player.getActiveEffects().size() > 0)
						player.removeAllEffects();

					entityData.putBoolean("startFatigue", false);
				}
			}
		}
	}

	public void giveResult(Player player, ItemStack stack) {
		if (stack == ItemStack.EMPTY || stack == null)
			return;

		player.addItem(stack);
	}

	public static void sendSystemMessage(List<ServerPlayer> list, Component text) {
		for (ServerPlayer player : list) {
			player.sendSystemMessage(text);
		}
	}

	public ItemStack editorLead() {
		ItemStack editStack = new ItemStack(Items.LEAD);
		editStack.enchant(Enchantments.BINDING_CURSE, 1);
		editStack.enchant(Enchantments.VANISHING_CURSE, 1);
		editStack.setHoverName(Component.literal("Editors Monocle"));
		CompoundTag nbt = editStack.getOrCreateTag();
		nbt.putInt("HideFlags", 1);
		editStack.setTag(nbt);
		editStack.addTagElement("lore", StringTag.valueOf("You have the power to edit the main UHC settings"));

		return editStack;
	}

	@SubscribeEvent
	public void UhcEvents(TickEvent.PlayerTickEvent event) {
		Player player = event.player;
		Level level = player.level();
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			final ServerLevel overworld = (ServerLevel) level;
			ItemStack bookStack = new ItemStack(ModRegistry.UHC_BOOK.get());

			UHCSaveData saveData = UHCSaveData.get(overworld);

			if (!saveData.isUhcOnGoing() && !saveData.isUhcStarting()) {
				CompoundTag entityData = player.getPersistentData();

				if (entityData.getBoolean("canEditUHC")) {
					if (player.getInventory().getItem(39) == editorLead())
						return;

					if (player.getInventory().getItem(39).isEmpty())
						player.getInventory().setItem(39, editorLead());
				}

				if (!ItemStack.isSameItem(player.getInventory().getSelected(), bookStack)) {
					if (!player.getInventory().contains(bookStack))
						player.getInventory().add(bookStack);
				}

				if (player.getEffect(MobEffects.SATURATION) == null)
					player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 32767 * 20, 10, true, false));

				if (player.getEffect(MobEffects.DAMAGE_RESISTANCE) == null)
					player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 32767 * 20, 10, true, false));

				if (player.getEffect(MobEffects.DIG_SLOWDOWN) == null)
					player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 32767 * 20, 10, true, false));
			}
		}
	}

	@SubscribeEvent
	public void checkWinner(TickEvent.LevelTickEvent event) {
		Level level = event.level;
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			ServerLevel overworld = (ServerLevel) level;
			UHCSaveData saveData = UHCSaveData.get(overworld);

			if (saveData.isUhcOnGoing() && !saveData.isUhcIsFinished()) {
				Scoreboard scoreboard = level.getScoreboard();
				MinecraftServer server = level.getServer();

				List<PlayerTeam> teamsAlive = new ArrayList<>();
				for (PlayerTeam team : scoreboard.getPlayerTeams()) {
					if (team.getPlayers().size() > 0 && team != scoreboard.getPlayerTeam("spectator")) {
						if (teamsAlive.contains(team))
							return;
						else
							teamsAlive.add(team);
					}
				}

				if (!teamsAlive.isEmpty() && teamsAlive != null) {
					teamsAlive.removeIf(team -> team.getPlayers().size() == 0);
				}

				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

				if (teamsAlive.size() == 1) {
					PlayerTeam team = teamsAlive.get(0);
					if (teamsAlive.get(0) != null) {
						if (team == scoreboard.getPlayerTeam("solo")) {
							if (team.getPlayers().size() == 1) {
								for (String s : team.getPlayers()) {
									Player winningPlayer = PlayerHelper.getPlayerEntityByName(level, s);
									if (winningPlayer != null) {
										SoloWonTheUHC(winningPlayer, playerList, level);
										saveData.setUhcIsFinished(true);
									}
								}
							}
						} else {
							YouWonTheUHC(teamsAlive.get(0), playerList, level);
							for (int i = 0; i < 7; i++) {
								for (String players : teamsAlive.get(0).getPlayers()) {
									Player teamPlayer = PlayerHelper.getPlayerEntityByName(level, players);
									if (teamPlayer != null) {
										FireworkRocketEntity rocket = new FireworkRocketEntity(level,
												teamPlayer.getX(), teamPlayer.getY() + 3, teamPlayer.getZ(), getFirework(level.random));
										level.addFreshEntity(rocket);
									}
								}
							}

							if (!teamsAlive.get(0).getPlayers().isEmpty() && teamsAlive.get(0).getPlayers().size() > 1) {
								List<String> teamPlayers = new ArrayList<>(teamsAlive.get(0).getPlayers());
								List<ServerPlayer> playersAlive = new ArrayList<>();

								for (String playerName : teamPlayers) {
									scoreboard.removePlayerFromTeam(playerName);
									Player player = PlayerHelper.getPlayerEntityByName(level, playerName);
									if (player != null)
										playersAlive.add((ServerPlayer) player);
								}

								setupShowdownAndTeleport(level, playersAlive);

								saveData.setUhcShowdown(true);
							}

							saveData.setUhcIsFinished(true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void testingEvent(PlayerInteractEvent.RightClickItem event) {
		/** Used to test the showdown platform
		 Level level = event.getLevel();
		 Player player = event.getEntity();

		 if(!level.isClientSide) {
		 if(event.getItemStack().getItem() == Items.FEATHER) {
		 List<ServerPlayer> players = new ArrayList<>();
		 players.add((ServerPlayer)player);
		 setupShowdownAndTeleport(level, players);
		 }
		 }
		 */
	}

	public void setupShowdownAndTeleport(Level level, List<ServerPlayer> players) {
		double centerX = 0;
		double centerZ = 0;

		double centerX1 = centerX - 21;
		double centerX2 = centerX + 21;
		double centerZ1 = centerZ - 21;
		double centerZ2 = centerZ + 21;

		Block showdownBlock = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(UHCConfig.COMMON.showdownBlock.get()));

		if (showdownBlock == null) {
			showdownBlock = Blocks.STONE_BRICKS;
		}

		for (double i = centerX1; i <= centerX2; i++) {
			for (double j = centerZ1; j <= centerZ2; j++) {
				level.setBlockAndUpdate(BlockPos.containing(i, 250, j), showdownBlock.defaultBlockState());
				if (j == centerZ1 || j == centerZ2) {
					for (double k = 250; k <= 253; k++) {
						level.setBlockAndUpdate(BlockPos.containing(i, k, j), showdownBlock.defaultBlockState());
					}
				}
			}

			if (i == centerX1 || i == centerX2) {
				for (double j = centerZ1; j <= centerZ2; j++) {
					for (double k = 250; k <= 253; k++) {
						level.setBlockAndUpdate(BlockPos.containing(i, k, j), showdownBlock.defaultBlockState());
					}
				}
			}
		}

		int TeleportChoosing = 0;
		for (ServerPlayer player : players) {
			TeleportChoosing++;
			if (TeleportChoosing > 8) {
				TeleportChoosing = 0;
				TeleportChoosing++;
			}
			switch (TeleportChoosing) {
				default -> player.teleportTo(centerX2 - 1.5, 251, centerZ2 - 1.5);
				case 2 -> player.teleportTo(centerX1 + 2.5, 251, centerZ1 + 2.5);
				case 3 -> player.teleportTo(centerX2 - 1.5, 251, centerZ1 + 2.5);
				case 4 -> player.teleportTo(centerX1 + 2.5, 251, centerZ2 - 1.5);
				case 5 -> player.teleportTo(centerX2 - 1.5, 251, centerZ);
				case 6 -> player.teleportTo(centerX1 + 2.5, 251, centerZ);
				case 7 -> player.teleportTo(centerX, 251, centerZ1 + 2.5);
				case 8 -> player.teleportTo(centerX, 251, centerZ2 - 1.5);
			}
		}
	}

	/**
	 * Only really does anything if there's a showdown
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void checkShowDownWinner(TickEvent.LevelTickEvent event) {
		Level level = event.level;
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			ServerLevel overworld = (ServerLevel) level;
			UHCSaveData saveData = UHCSaveData.get(overworld);

			if (saveData.isUhcOnGoing() && saveData.isUhcIsFinished() && saveData.isUhcShowdown() && !saveData.isUhcShowdownFinished()) {
				Scoreboard scoreboard = level.getScoreboard();
				MinecraftServer server = level.getServer();

				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
				List<ServerPlayer> playersAlive = new ArrayList<>();

				for (ServerPlayer player : playerList) {
					if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL && player.getTeam() != scoreboard.getPlayerTeam("spectator")) {
						playersAlive.add(player);
					}
				}

				if (!playersAlive.isEmpty() && playersAlive != null) {
					playersAlive.removeIf(player -> player.gameMode.getGameModeForPlayer() != GameType.SURVIVAL);
				}

				if (playersAlive.size() == 1) {
					Player showdownWinner = playersAlive.get(0);
					WonTheShowdown(showdownWinner, playerList, level);
					saveData.setUhcShowdownFinished(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void throwEvent(ItemTossEvent event) {
		Entity entity = event.getEntity();
		ItemStack stack = event.getEntity().getItem();
		if (!entity.level().isClientSide) {
			ServerLevel overworld = entity.level().getServer().overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);

				if (!saveData.isUhcOnGoing()) {
					if (ItemStack.isSameItem(stack, new ItemStack(ModRegistry.UHC_BOOK.get()))) {
						event.setCanceled(true);
						event.setResult(Result.DENY);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void spawnRoomEvent(TickEvent.LevelTickEvent event) {
		Level level = event.level;
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			ServerLevel overworld = (ServerLevel) level;
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);

				if (saveData.isSpawnRoom() && !saveData.isUhcOnGoing()) {
					ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, saveData.getSpawnRoomDimension());
					ServerLevel dimensionWorld = event.level.getServer().getLevel(dimensionKey);
					if (dimensionWorld != null) {
						double centerX1 = saveData.getBorderCenterX() - 7;
						double centerX2 = saveData.getBorderCenterX() + 7;
						double centerZ1 = saveData.getBorderCenterZ() - 7;
						double centerZ2 = saveData.getBorderCenterZ() + 7;

						for (double i = centerX1; i <= centerX2; i++) {
							double d0 = dimensionWorld.random.nextGaussian() * 0.02D;
							double d1 = dimensionWorld.random.nextGaussian() * 0.02D;
							double d2 = dimensionWorld.random.nextGaussian() * 0.02D;
							for (double j = centerZ1; j <= centerZ2; j++) {
								if (dimensionWorld.random.nextInt(10000) <= 4 && dimensionWorld.getBlockState(BlockPos.containing(i, 250, j)).useShapeForLightOcclusion())
									dimensionWorld.sendParticles(ParticleTypes.CRIT, i, 250 - 0.5, j, 3, d0, d1, d2, 0.0D);

								if (j == centerZ1 || j == centerZ2) {
									for (double k = 250; k <= 253; k++) {
										if (dimensionWorld.random.nextInt(1000) <= 3 && dimensionWorld.getBlockState(BlockPos.containing(i, k, j)).useShapeForLightOcclusion())
											dimensionWorld.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, i, k + 1.0D, j, 3, d0, d1, d2, 0.0D);
									}
								}
							}

							if (i == centerX1 || i == centerX2) {
								for (double j = centerZ1; j <= centerZ2; j++) {
									for (double k = 250; k <= 253; k++) {
										if (dimensionWorld.random.nextInt(1000) <= 3 && dimensionWorld.getBlockState(BlockPos.containing(i, k, j)).useShapeForLightOcclusion())
											dimensionWorld.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, i, k + 1.0D, j, 3, d0, d1, d2, 0.0D);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void SpawnRoomPlayerEvent(TickEvent.PlayerTickEvent event) {
		Player player = event.player;
		Level level = player.level();
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && level.dimension().equals(Level.OVERWORLD)) {
			ServerLevel overworld = (ServerLevel) level;
			UHCSaveData saveData = UHCSaveData.get(overworld);

			if (saveData.isSpawnRoom() && !saveData.isUhcOnGoing()) {
				double centerX1 = saveData.getBorderCenterX() - 7;
				double centerX2 = saveData.getBorderCenterX() + 7;
				double centerZ1 = saveData.getBorderCenterZ() - 7;
				double centerZ2 = saveData.getBorderCenterZ() + 7;

				AABB hitbox = new AABB(centerX1 - 0.5f, 248 - 0.5f, centerZ1 - 0.5f, centerX2 + 0.5f, 260 + 0.5f, centerZ2 + 0.5f);
				List<Player> collidingList = new ArrayList<>(level.getEntitiesOfClass(Player.class, hitbox));

				if (!collidingList.contains(player) && !player.isCreative() && !player.isSpectator()) {
					if (player.level().dimension().location().equals(saveData.getSpawnRoomDimension())) {
						((ServerPlayer) player).connection.teleport(saveData.getBorderCenterX(), 252, saveData.getBorderCenterZ(), player.getYRot(), player.getXRot());
					} else if (!player.level().dimension().location().equals(saveData.getSpawnRoomDimension())) {
						ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, saveData.getSpawnRoomDimension());
						ServerLevel spawnRoomWorld = level.getServer().getLevel(dimensionKey);
						if (spawnRoomWorld != null) {
							player.changeDimension(spawnRoomWorld, new UHCTeleporter(player.blockPosition()));
						} else {
							player.sendSystemMessage(Component.literal("Dimension invalid, please contact the host, Dimension: " + saveData.getSpawnRoomDimension()));
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerEditUHCEvent(PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			Player player = event.player;
			CompoundTag entityData = player.getPersistentData();
			Level level = player.level();

			if (!level.isClientSide) {
				if (!entityData.contains("canEditUHC"))
					entityData.putBoolean("canEditUHC", false);

				if (!entityData.getBoolean("canEditUHC") && player.hasPermissions(2))
					entityData.putBoolean("canEditUHC", true);

				if (entityData.getBoolean("canEditUHC") && !player.hasPermissions(2))
					entityData.putBoolean("canEditUHC", false);
			}
		}
	}

	@SubscribeEvent
	public void onNewPlayerJoin(PlayerLoggedInEvent event) {
		Player player = event.getEntity();

		if (!player.level().isClientSide) {
			ServerLevel overworld = player.level().getServer().overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				ServerPlayer playerMP = (ServerPlayer) player;

				if (saveData.isUhcOnGoing() && player.getTeam() == null) {
					playerMP.setGameMode(GameType.SPECTATOR);
				}
			}
		}
	}

	@SubscribeEvent
	public void DimensionChangeEvent(EntityTravelToDimensionEvent event) {
		if (event.getEntity() instanceof Player player) {
			Level level = player.level();
			if (!level.isClientSide) {
				ServerLevel overworld = player.level().getServer().overworld();
				if (overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					if (saveData.isUhcOnGoing()) {
						if (!saveData.isNetherEnabled()) {
							if (event.getDimension() == Level.NETHER)
								event.setCanceled(true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerPermissionClone(PlayerEvent.Clone event) {
		Player originalPlayer = event.getOriginal();
		Player newPlayer = event.getEntity();

		CompoundTag originalData = originalPlayer.getPersistentData();
		CompoundTag newData = newPlayer.getPersistentData();

		if (!newPlayer.level().isClientSide) {
			originalData.putBoolean("canEditUHC", newData.getBoolean("canEditUHC"));

			BlockPos deathPos = originalPlayer.blockPosition();
			newData.putLong("deathPos", deathPos.asLong());
			newData.putString("deathDim", originalPlayer.level().dimension().location().toString());
			((ServerPlayer) newPlayer).setRespawnPosition(originalPlayer.level().dimension(), deathPos, originalPlayer.getYRot(), true, false);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide) {
			Scoreboard scoreboard = level.getScoreboard();
			ServerLevel overworld = player.level().getServer().overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if (saveData.isUhcOnGoing()) {
					PlayerTeam spectatorTeam = scoreboard.getPlayerTeam("spectator");
					scoreboard.addPlayerToTeam(player.getName().getString(), spectatorTeam);

					scoreboard.getOrCreateObjective("health");
					scoreboard.resetPlayerScore(player.getName().getString(), scoreboard.getOrCreateObjective("health"));
				}
			}
		}
	}

	public void YouWonTheUHC(PlayerTeam team, List<ServerPlayer> playerList, Level level) {
		if (!level.isClientSide) {
			ServerLevel overworld = level.getServer().overworld();
			if (overworld != null) {
				String teamName = team.getName();
				for (ServerPlayer player : playerList) {
					if (player.getTeam() == team) {
						for (int i = 0; i < 10; i++) {
							if (level.random.nextInt(10) < 3) {
								FireworkRocketEntity rocket = new FireworkRocketEntity(level, player.getX(), player.getY() + 3, player.getZ(), getFirework(level.random));
								player.level().addFreshEntity(rocket);
							}
						}
					}
					ClientboundSetTitleTextPacket setTitleTextPacket = new ClientboundSetTitleTextPacket(Component.translatable("uhc.team.won", team.getColor() + teamName));
					player.connection.send(setTitleTextPacket);
				}
			}
		}
	}

	public void SoloWonTheUHC(Player winningPlayer, List<ServerPlayer> playerList, Level level) {
		if (!level.isClientSide) {
			ServerLevel overworld = level.getServer().overworld();
			if (overworld != null) {
				for (ServerPlayer player : playerList) {
					if (player.getName() == winningPlayer.getName()) {
						for (int i = 0; i < 10; i++) {
							if (level.random.nextInt(10) < 3) {
								FireworkRocketEntity rocket = new FireworkRocketEntity(level, winningPlayer.getX(), winningPlayer.getY() + 3, winningPlayer.getZ(), getFirework(level.random));
								player.level().addFreshEntity(rocket);
							}
						}
					}
					ClientboundSetTitleTextPacket setTitleTextPacket = new ClientboundSetTitleTextPacket(Component.translatable("uhc.player.won", ChatFormatting.DARK_RED + winningPlayer.getName().getString()));
					player.connection.send(setTitleTextPacket);
				}
			}
		}
	}

	public void WonTheShowdown(Player winningPlayer, List<ServerPlayer> playerList, Level level) {
		if (!level.isClientSide) {
			ServerLevel overworld = level.getServer().overworld();
			if (overworld != null) {
				for (ServerPlayer player : playerList) {
					if (player.getName() == winningPlayer.getName()) {
						for (int i = 0; i < 10; i++) {
							if (level.random.nextInt(10) < 3) {
								FireworkRocketEntity rocket = new FireworkRocketEntity(level, winningPlayer.getX(), winningPlayer.getY() + 3, winningPlayer.getZ(), getFirework(level.random));
								player.level().addFreshEntity(rocket);
							}
						}
					}
					ClientboundSetTitleTextPacket setTitleTextPacket = new ClientboundSetTitleTextPacket(Component.translatable("uhc.player.showdown.won", ChatFormatting.DARK_RED + winningPlayer.getName().getString()));
					player.connection.send(setTitleTextPacket);
				}
			}
		}
	}

	public ItemStack getFirework(RandomSource rand) {
		ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("Flicker", true);
		nbt.putBoolean("Trail", true);

		int[] colors = new int[rand.nextInt(8) + 1];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = DyeColor.byId(rand.nextInt(16)).getId();
		}
		nbt.putIntArray("Colors", colors);
		byte type = (byte) (rand.nextInt(3) + 1);
		type = type == 3 ? 4 : type;
		nbt.putByte("Type", type);

		ListTag explosions = new ListTag();
		explosions.add(nbt);

		CompoundTag fireworkTag = new CompoundTag();
		fireworkTag.put("Explosions", explosions);
		fireworkTag.putByte("Flight", (byte) 1);
		nbt.put("Fireworks", fireworkTag);
		firework.setTag(new CompoundTag());

		return firework;
	}

	@SubscribeEvent
	public void SyncPlayerWithData(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		if (event.getEntity() instanceof Player player && !level.isClientSide) {
			Scoreboard scoreboard = level.getScoreboard();
			ServerLevel overworld = level.getServer().overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);

				if (player.getTeam() == null) {
					PlayerTeam soloTeam = scoreboard.getPlayerTeam("solo");
					scoreboard.addPlayerToTeam(player.getName().getString(), soloTeam);
				}

				UHCPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new UHCPacketMessage(saveData.save(new CompoundTag())));
			}
		}
	}
}
