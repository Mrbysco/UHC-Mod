package com.mrbysco.uhc.handler;

import com.mojang.brigadier.StringReader;
import com.mrbysco.uhc.UltraHardCoremod;
import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.network.payload.UHCSyncPayload;
import com.mrbysco.uhc.registry.UHCDataAttachments;
import com.mrbysco.uhc.registry.UHCRegistry;
import com.mrbysco.uhc.spawnitem.ItemObject;
import com.mrbysco.uhc.spawnitem.SpawnItemsHandler;
import com.mrbysco.uhc.util.PlayerHelper;
import com.mrbysco.uhc.util.UHCHelper;
import com.mrbysco.uhc.util.UHCTransition;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber
public class UHCHandler {

	public static int uhcStartTimer;

	@SubscribeEvent
	public static void UHCStartEventWorld(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.getGameTime() % 20 == 0 && level.dimension().equals(Level.OVERWORLD)) {
			UHCSaveData saveData = UHCSaveData.get(level);
			UHCTimerData timerData = UHCTimerData.get(level);
			MinecraftServer server = level.getServer();
			List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

			ResourceLocation configDimension = ResourceLocation.tryParse(UHCConfig.COMMON.spawnDimension.get());
			if (!saveData.getUHCDimension().equals(configDimension))
				saveData.setUHCDimension(configDimension);

			if (!playerList.isEmpty()) {
				if (saveData.isUhcStarting()) {
					if (timerData.getUhcStartTimer() != uhcStartTimer) {
						uhcStartTimer = timerData.getUhcStartTimer();
					}

					if (timerData.getUhcStartTimer() == 2 || timerData.getUhcStartTimer() == 3 || timerData.getUhcStartTimer() == 4 ||
							timerData.getUhcStartTimer() == 5 || timerData.getUhcStartTimer() == 6 || timerData.getUhcStartTimer() == 7) {
						if (timerData.getUhcStartTimer() == 2) {
							sendSystemMessage(playerList, Component.translatable("uhc.start.5"));
							++uhcStartTimer;
							timerData.setUhcStartTimer(uhcStartTimer);
							timerData.setDirty();
						} else if (timerData.getUhcStartTimer() == 3) {
							sendSystemMessage(playerList, Component.translatable("uhc.start.4"));
							++uhcStartTimer;
							timerData.setUhcStartTimer(uhcStartTimer);
							timerData.setDirty();
						} else if (timerData.getUhcStartTimer() == 4) {
							sendSystemMessage(playerList, Component.translatable("uhc.start.3"));
							++uhcStartTimer;
							timerData.setUhcStartTimer(uhcStartTimer);
							timerData.setDirty();
						} else if (timerData.getUhcStartTimer() == 5) {
							sendSystemMessage(playerList, Component.translatable("uhc.start.2"));
							++uhcStartTimer;
							timerData.setUhcStartTimer(uhcStartTimer);
							timerData.setDirty();
						} else if (timerData.getUhcStartTimer() == 6) {
							sendSystemMessage(playerList, Component.translatable("uhc.start.1"));
							++uhcStartTimer;
							timerData.setUhcStartTimer(uhcStartTimer);
							timerData.setDirty();
						} else if (timerData.getUhcStartTimer() == 7) {
							sendSystemMessage(playerList, Component.translatable("uhc.start"));

							timerData.setUhcStartTimer(0);
							saveData.setDirty();
						}
					} else {
						++uhcStartTimer;
						timerData.setUhcStartTimer(uhcStartTimer);
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

	@SubscribeEvent
	public static void UHCStartEventPlayer(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			UHCSaveData saveData = UHCSaveData.get(level);

			if (saveData.isUhcStarting()) {
				if (!player.hasData(UHCDataAttachments.START_FATIGUE))
					player.setData(UHCDataAttachments.START_FATIGUE, true);

				if (uhcStartTimer == 7) {
					if (!SpawnItemsHandler.spawnItemList.isEmpty()) {
						for (ItemObject object : SpawnItemsHandler.spawnItemList) {
							ResourceLocation location = ResourceLocation.tryParse(object.itemLocation());
							if (location == null) continue;
							Item item = BuiltInRegistries.ITEM.get(location);
							if (item != null) {
								ItemStack stack = new ItemStack(item, object.count());
								if (!object.components().isEmpty()) {
									ItemParser parser = new ItemParser(player.level().registryAccess());
									try {
										ItemParser.ItemResult result = parser.parse(new StringReader(object.itemLocation() + object.components()));
										//Have to add the item location so that the parser doesn't throw an error
										stack.applyComponents(result.components());
									} catch (Exception e) {
										UltraHardCoremod.LOGGER.trace("Exception: ", e);
									}
								}

								if (!player.addItem(stack)) {
									ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), stack);
									level.addFreshEntity(itemEntity);
								}
							}
						}
					}

					player.removeAllEffects();
					player.setData(UHCDataAttachments.START_FATIGUE, false);

					if (!player.getActiveEffects().isEmpty())
						player.removeAllEffects();

					saveData.setUhcStarting(false);
					saveData.setUhcOnGoing(true);
				} else {
					if (player.getEffect(MobEffects.DIG_SLOWDOWN) == null)
						player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 32767 * 20, 10, true, false));

					if (player.getEffect(MobEffects.MOVEMENT_SLOWDOWN) == null)
						player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 32767 * 20, 10, true, false));

					if (player.getInventory().contains(UHCRegistry.UHC_BOOK.toStack())) {
						int bookSlot = player.getInventory().findSlotMatchingUnusedItem(UHCRegistry.UHC_BOOK.toStack());
						if (bookSlot != -1)
							player.getInventory().removeItemNoUpdate(bookSlot);
					}

					if (!player.getInventory().getItem(39).isEmpty())
						player.getInventory().removeItemNoUpdate(39);
				}
			}
			if (saveData.isUhcOnGoing()) {
				if (player.hasData(UHCDataAttachments.START_FATIGUE)) {
					player.removeAllEffects();

					if (!player.getActiveEffects().isEmpty())
						player.removeAllEffects();

					player.removeData(UHCDataAttachments.START_FATIGUE);
				}
			}
		}
	}

	public static void giveResult(Player player, ItemStack stack) {
		if (stack == ItemStack.EMPTY || stack == null)
			return;

		player.addItem(stack);
	}

	public static void sendSystemMessage(List<ServerPlayer> list, Component text) {
		for (ServerPlayer player : list) {
			player.sendSystemMessage(text);
		}
	}

	public static ItemStack editorLead(RegistryAccess registryAccess) {
		ItemStack editStack = new ItemStack(Items.LEAD);
		editStack.enchant(registryAccess.holderOrThrow(Enchantments.BINDING_CURSE), 1);
		editStack.enchant(registryAccess.holderOrThrow(Enchantments.VANISHING_CURSE), 1);
		editStack.set(DataComponents.ENCHANTMENTS, Objects.requireNonNull(editStack.get(DataComponents.ENCHANTMENTS)).withTooltip(false));
		editStack.set(DataComponents.CUSTOM_NAME, Component.literal("Editors Monocle"));
		editStack.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("You have the power to edit the main UHC settings"))));

		return editStack;
	}

	@SubscribeEvent
	public static void UhcEvents(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			ItemStack bookStack = UHCRegistry.UHC_BOOK.toStack();

			UHCSaveData saveData = UHCSaveData.get(level);

			if (!saveData.isUhcOnGoing() && !saveData.isUhcStarting()) {
				if (player.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
					if (player.getInventory().getItem(39) == editorLead(level.registryAccess()))
						return;

					if (player.getInventory().getItem(39).isEmpty())
						player.getInventory().setItem(39, editorLead(level.registryAccess()));
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
	public static void checkWinner(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			UHCSaveData saveData = UHCSaveData.get(level);

			if (saveData.isUhcOnGoing() && !saveData.isUhcIsFinished()) {
				Scoreboard scoreboard = level.getScoreboard();
				MinecraftServer server = level.getServer();

				List<PlayerTeam> teamsAlive = new ArrayList<>();
				for (PlayerTeam team : scoreboard.getPlayerTeams()) {
					if (!team.getPlayers().isEmpty() && team != scoreboard.getPlayerTeam("spectator")) {
						if (teamsAlive.contains(team))
							return;
						else
							teamsAlive.add(team);
					}
				}

				if (!teamsAlive.isEmpty() && teamsAlive != null) {
					teamsAlive.removeIf(team -> team.getPlayers().isEmpty());
				}

				List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

				if (teamsAlive.size() == 1) {
					PlayerTeam team = teamsAlive.getFirst();
					if (teamsAlive.getFirst() != null) {
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
							YouWonTheUHC(teamsAlive.getFirst(), playerList, level);
							for (int i = 0; i < 7; i++) {
								for (String players : teamsAlive.getFirst().getPlayers()) {
									Player teamPlayer = PlayerHelper.getPlayerEntityByName(level, players);
									if (teamPlayer != null) {
										FireworkRocketEntity rocket = new FireworkRocketEntity(level,
												teamPlayer.getX(), teamPlayer.getY() + 3, teamPlayer.getZ(), getFirework(level.random));
										level.addFreshEntity(rocket);
									}
								}
							}

							if (!teamsAlive.getFirst().getPlayers().isEmpty() && teamsAlive.getFirst().getPlayers().size() > 1) {
								List<String> teamPlayers = new ArrayList<>(teamsAlive.getFirst().getPlayers());
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

	/**
	 * Used to test the showdown platform
	 */
	@SubscribeEvent
	public static void testingEvent(PlayerInteractEvent.RightClickItem event) {
//		Level level = event.getLevel();
//		Player player = event.getEntity();
//
//		if (!level.isClientSide()) {
//			if (event.getItemStack().getItem() == Items.FEATHER) {
//				List<ServerPlayer> players = new ArrayList<>();
//				players.add((ServerPlayer) player);
//				setupShowdownAndTeleport(level, players);
//			}
//		}
	}

	public static void setupShowdownAndTeleport(Level level, List<ServerPlayer> players) {
		double centerX = 0;
		double centerZ = 0;

		double centerX1 = centerX - 21;
		double centerX2 = centerX + 21;
		double centerZ1 = centerZ - 21;
		double centerZ2 = centerZ + 21;

		Block showdownBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(UHCConfig.COMMON.showdownBlock.get()));

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
				case 2 -> player.teleportTo(centerX1 + 2.5, 251, centerZ1 + 2.5);
				case 3 -> player.teleportTo(centerX2 - 1.5, 251, centerZ1 + 2.5);
				case 4 -> player.teleportTo(centerX1 + 2.5, 251, centerZ2 - 1.5);
				case 5 -> player.teleportTo(centerX2 - 1.5, 251, centerZ);
				case 6 -> player.teleportTo(centerX1 + 2.5, 251, centerZ);
				case 7 -> player.teleportTo(centerX, 251, centerZ1 + 2.5);
				case 8 -> player.teleportTo(centerX, 251, centerZ2 - 1.5);
				default -> player.teleportTo(centerX2 - 1.5, 251, centerZ2 - 1.5);
			}
		}
	}

	/**
	 * Only really does anything if there's a showdown
	 */
	@SubscribeEvent
	public static void checkShowDownWinner(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			UHCSaveData saveData = UHCSaveData.get(level);

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
					Player showdownWinner = playersAlive.getFirst();
					WonTheShowdown(showdownWinner, playerList, level);
					saveData.setUhcShowdownFinished(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void throwEvent(ItemTossEvent event) {
		Entity entity = event.getEntity();
		Level level = entity.level();
		ItemStack stack = event.getEntity().getItem();
		if (!level.isClientSide() && !UHCHelper.isUHCOnGoing(level)) {
			if (ItemStack.isSameItem(stack, UHCRegistry.UHC_BOOK.toStack())) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void spawnRoomEvent(LevelTickEvent.Pre event) {
		Level level = event.getLevel();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			UHCSaveData saveData = UHCSaveData.get(level);

			if (saveData.isSpawnRoom() && !saveData.isUhcOnGoing()) {
				ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, saveData.getSpawnRoomDimension());
				ServerLevel dimensionWorld = level.getServer().getLevel(dimensionKey);
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

	@SubscribeEvent
	public static void spawnRoomEvent(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			UHCSaveData saveData = UHCSaveData.get(level);

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
							DimensionTransition transition = UHCTransition.makeTransition(spawnRoomWorld, player, player.position());
							player.changeDimension(transition);
						} else {
							player.sendSystemMessage(Component.literal("Dimension invalid, please contact the host, Dimension: " + saveData.getSpawnRoomDimension()));
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void playerEditUHCEvent(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide()) {
			if (!player.hasData(UHCDataAttachments.CAN_EDIT_UHC))
				player.setData(UHCDataAttachments.CAN_EDIT_UHC, false);

			if (!player.getData(UHCDataAttachments.CAN_EDIT_UHC) && player.hasPermissions(2))
				player.setData(UHCDataAttachments.CAN_EDIT_UHC, true);

			if (player.getData(UHCDataAttachments.CAN_EDIT_UHC) && !player.hasPermissions(2))
				player.setData(UHCDataAttachments.CAN_EDIT_UHC, false);
		}
	}

	@SubscribeEvent
	public static void onNewPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();
		Level level = player.level();

		if (!level.isClientSide()) {
			ServerPlayer playerMP = (ServerPlayer) player;

			if (UHCHelper.isUHCOnGoing(level) && player.getTeam() == null) {
				playerMP.setGameMode(GameType.SPECTATOR);
			}
		}
	}

	@SubscribeEvent
	public static void DimensionChangeEvent(EntityTravelToDimensionEvent event) {
		if (event.getEntity() instanceof Player player) {
			Level level = player.level();
			if (!level.isClientSide()) {
				if (UHCHelper.isUHCOnGoing(level)) {
					UHCSaveData saveData = UHCSaveData.get(level);
					if (!saveData.isNetherEnabled()) {
						if (event.getDimension() == Level.NETHER)
							event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerPermissionClone(PlayerEvent.Clone event) {
		Player originalPlayer = event.getOriginal();
		Player newPlayer = event.getEntity();

		if (!newPlayer.level().isClientSide) {
			newPlayer.setData(UHCDataAttachments.CAN_EDIT_UHC, originalPlayer.getData(UHCDataAttachments.CAN_EDIT_UHC));

			BlockPos deathPos = originalPlayer.blockPosition();
			newPlayer.setData(UHCDataAttachments.DEATH_POS, GlobalPos.of(originalPlayer.level().dimension(), deathPos));
			((ServerPlayer) newPlayer).setRespawnPosition(originalPlayer.level().dimension(), deathPos, originalPlayer.getYRot(), true, false);
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide()) {
			Scoreboard scoreboard = level.getScoreboard();
			UHCSaveData saveData = UHCSaveData.get(level);
			if (saveData.isUhcOnGoing()) {
				PlayerTeam spectatorTeam = scoreboard.getPlayerTeam("spectator");
				scoreboard.addPlayerToTeam(player.getName().getString(), spectatorTeam);

				scoreboard.getObjective("health");
				scoreboard.resetSinglePlayerScore(ScoreHolder.forNameOnly(player.getScoreboardName()), scoreboard.getObjective("health"));
			}
		}
	}

	public static void YouWonTheUHC(PlayerTeam team, List<ServerPlayer> playerList, Level level) {
		if (!level.isClientSide()) {
			String teamName = team.getName();
			for (ServerPlayer player : playerList) {
				if (player.getTeam() == team) {
					for (int i = 0; i < 10; i++) {
						if (level.getRandom().nextInt(10) < 3) {
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

	public static void SoloWonTheUHC(Player winningPlayer, List<ServerPlayer> playerList, Level level) {
		if (!level.isClientSide()) {
			for (ServerPlayer player : playerList) {
				if (player.getName() == winningPlayer.getName()) {
					for (int i = 0; i < 10; i++) {
						if (level.getRandom().nextInt(10) < 3) {
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

	public static void WonTheShowdown(Player winningPlayer, List<ServerPlayer> playerList, Level level) {
		if (!level.isClientSide()) {
			for (ServerPlayer player : playerList) {
				if (player.getName() == winningPlayer.getName()) {
					for (int i = 0; i < 10; i++) {
						if (level.getRandom().nextInt(10) < 3) {
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

	public static ItemStack getFirework(RandomSource rand) {
		ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);

		int[] colors = new int[rand.nextInt(8) + 1];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = DyeColor.byId(rand.nextInt(16)).getId();
		}

		byte type = (byte) (rand.nextInt(3) + 1);
		type = type == 3 ? 4 : type;
		FireworkExplosion.Shape shape = FireworkExplosion.Shape.byId(type);

		List<FireworkExplosion> explosions = new ArrayList<>();
		explosions.add(
				new FireworkExplosion(shape, IntList.of(colors), IntList.of(), true, true)
		);

		Fireworks fireworks = new Fireworks(1, explosions);

		firework.set(DataComponents.FIREWORKS, fireworks);

		return firework;
	}

	@SubscribeEvent
	public static void SyncPlayerWithData(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		if (event.getEntity() instanceof Player player && !level.isClientSide()) {
			Scoreboard scoreboard = level.getScoreboard();
			UHCSaveData saveData = UHCSaveData.get(level);

			if (player.getTeam() == null) {
				PlayerTeam soloTeam = scoreboard.getPlayerTeam("solo");
				scoreboard.addPlayerToTeam(player.getName().getString(), soloTeam);
			}

			PacketDistributor.sendToPlayer((ServerPlayer) player, new UHCSyncPayload(saveData.save(new CompoundTag(), level.registryAccess())));
		}
	}
}
