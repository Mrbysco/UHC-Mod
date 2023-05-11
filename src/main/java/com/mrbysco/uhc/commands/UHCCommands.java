package com.mrbysco.uhc.commands;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.packets.UHCPacketHandler;
import com.mrbysco.uhc.packets.UHCPacketMessage;
import com.mrbysco.uhc.utils.PlayerHelper;
import com.mrbysco.uhc.utils.SpreadPosition;
import com.mrbysco.uhc.utils.SpreadUtil;
import com.mrbysco.uhc.utils.TeamUtil;
import com.mrbysco.uhc.utils.UHCTeleporter;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UHCCommands {


	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("uhc");
		root.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.literal("forceteam")
						.then(Commands.argument("team", TeamArgument.team())
								.executes((source) -> forceTeam(source, TeamArgument.getTeam(source, "team"),
										ScoreHolderArgument.getNamesWithDefaultWildcard(source, "members")))))
				.then(Commands.literal("reset")
						.executes(UHCCommands::reset))
				.then(Commands.literal("spawnroom")
						.then(Commands.literal("place")
								.executes((source) -> placeSpawnroom(source, "place")))
						.then(Commands.literal("remove")
								.executes((source) -> placeSpawnroom(source, "remove"))))
				.then(Commands.literal("respawn")
						.then(Commands.argument("team", TeamArgument.team())
								.executes((source) -> respawn(source, TeamArgument.getTeam(source, "team"),
										ScoreHolderArgument.getNamesWithDefaultWildcard(source, "members")))));


		dispatcher.register(root);
	}

	private static int reset(CommandContext<CommandSourceStack> ctx) {
		MinecraftServer server = ctx.getSource().getServer();
		List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
		ServerLevel overworld = server.overworld();
		if (overworld != null) {
			UHCSaveData saveData = UHCSaveData.get(overworld);
			UHCTimerData timerData = UHCTimerData.get(overworld);
			Scoreboard scoreboard = overworld.getScoreboard();

			if (scoreboard != null) {
				for (PlayerTeam team : scoreboard.getPlayerTeams()) {
					if (team != null && team.getPlayers().size() > 0 && team != scoreboard.getPlayerTeam("spectator")) {
						if (team.getPlayers() != null && !team.getPlayers().isEmpty()) {
							List<String> foundPlayers = new ArrayList<>(team.getPlayers());

							for (String playerFound : foundPlayers) {
								scoreboard.removePlayerFromTeam(playerFound, team);
							}
						}
					}
				}
			}

			for (ServerPlayer player : playerList) {
				player.getInventory().clearContent();
				player.heal(Integer.MAX_VALUE);

				if (player.getTeam() != null) {
					PlayerTeam spectatorTeam = scoreboard.getPlayerTeam("spectator");
					scoreboard.addPlayerToTeam(player.getName().getString(), spectatorTeam);
				}
			}

			double centerX = saveData.getBorderCenterX();
			double centerZ = saveData.getBorderCenterZ();
			double centerX1 = centerX - 7;
			double centerX2 = centerX + 7;
			double centerZ1 = centerZ - 7;
			double centerZ2 = centerZ + 7;
			ResourceKey<Level> spawnRoom = ResourceKey.create(Registry.DIMENSION_REGISTRY, saveData.getSpawnRoomDimension());
			ServerLevel level = server.getLevel(spawnRoom);
			for (double i = centerX1; i <= centerX2; i++) {
				for (double j = centerZ1; j <= centerZ2; j++) {
					for (double k = 250; k <= 253; k++) {
						level.setBlockAndUpdate(new BlockPos(i, k, j), Blocks.AIR.defaultBlockState());
					}
				}
			}

			WorldBorder border = level.getWorldBorder(); //TODO: Check if this should be using the overworld like the command does
			border.setSize(server.getAbsoluteMaxWorldSize());

			timerData.resetAll();
			timerData.setDirty();
			saveData.resetAll();
			saveData.setDirty();

			UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(saveData));
			ctx.getSource().sendSuccess(Component.translatable("commands.uhc.reset.success"), true);
		}

		return 0;
	}

	private static int placeSpawnroom(CommandContext<CommandSourceStack> ctx, String value) {
		MinecraftServer server = ctx.getSource().getServer();
		ServerLevel overworld = server.overworld();
		if (overworld != null) {
			ServerLevel senderWorld = ctx.getSource().getLevel();
			UHCSaveData saveData = UHCSaveData.get(overworld);
			double centerX = saveData.getBorderCenterX();
			double centerZ = saveData.getBorderCenterZ();

			double centerX1 = centerX - 7;
			double centerX2 = centerX + 7;
			double centerZ1 = centerZ - 7;
			double centerZ2 = centerZ + 7;
			ResourceLocation spawnroomBlock = ResourceLocation.tryParse(UHCConfig.COMMON.spawnRoomBlock.get());
			Block roomBlock = ForgeRegistries.BLOCKS.getValue(spawnroomBlock);
			if (roomBlock == null) {
				roomBlock = Blocks.BARRIER;
			}
			ResourceKey<Level> spawnRoom = ResourceKey.create(Registry.DIMENSION_REGISTRY, saveData.getSpawnRoomDimension());
			ServerLevel level = server.getLevel(spawnRoom);
			if (level != null) {
				if (value.equals("place")) {
					for (double i = centerX1; i <= centerX2; i++) {
						for (double j = centerZ1; j <= centerZ2; j++) {
							for (double k = 250; k <= 253; k++) {
								level.setBlockAndUpdate(new BlockPos(i, k, j), roomBlock.defaultBlockState());
							}
						}
					}
					saveData.setSpawnRoom(false);
					saveData.setSpawnRoomDimension(Level.OVERWORLD.location());
					saveData.setDirty();
				} else {
					for (double i = centerX1; i <= centerX2; i++) {
						for (double j = centerZ1; j <= centerZ2; j++) {
							senderWorld.setBlockAndUpdate(new BlockPos(i, 250, j), roomBlock.defaultBlockState());
							if (j == centerZ1 || j == centerZ2) {
								for (double k = 250; k <= 253; k++) {
									senderWorld.setBlockAndUpdate(new BlockPos(i, k, j), roomBlock.defaultBlockState());
								}
							}
						}

						if (i == centerX1 || i == centerX2) {
							for (double j = centerZ1; j <= centerZ2; j++) {
								for (double k = 250; k <= 253; k++) {
									senderWorld.setBlockAndUpdate(new BlockPos(i, k, j), roomBlock.defaultBlockState());
								}
							}
						}
					}
					senderWorld.setDefaultSpawnPos(new BlockPos(centerX, 252, centerZ), 90F);
					saveData.setSpawnRoom(true);
					saveData.setSpawnRoomDimension(senderWorld.dimension().location());
					saveData.setDirty();
					ctx.getSource().sendSuccess(Component.translatable("commands.uhc.spawnroom.success"), true);
				}
			}
		}
		return 0;
	}

	private static int forceTeam(CommandContext<CommandSourceStack> ctx, PlayerTeam teamIn, Collection<String> players) {
		CommandSourceStack source = ctx.getSource();
		Scoreboard scoreboard = source.getServer().getScoreboard();

		for (String s : players) {
			scoreboard.addPlayerToTeam(s, teamIn);
		}

		source.sendSuccess(Component.translatable("commands.uhc.forceteam.success", players.size(), teamIn.getFormattedDisplayName()), true);

		return players.size();
	}

	private static int respawn(CommandContext<CommandSourceStack> ctx, PlayerTeam teamIn, Collection<String> players) {
		MinecraftServer server = ctx.getSource().getServer();
		ServerLevel overworld = server.overworld();
		Scoreboard scoreboard = server.getScoreboard();
		if (overworld != null) {
			UHCSaveData saveData = UHCSaveData.get(overworld);
			for (String s : players) {
				if (scoreboard.addPlayerToTeam(s, teamIn)) {
					respawnPlayers(ctx, s, teamIn, scoreboard, overworld, saveData);

				}
			}

		}
		return players.size();
	}

	public static void respawnPlayers(CommandContext<CommandSourceStack> ctx, String playerName, PlayerTeam selectedTeam, Scoreboard scoreboard, ServerLevel world, UHCSaveData uhcData) {
		Player player = PlayerHelper.getPlayerEntityByName(world, playerName);
		if (player != null) {
			CompoundTag entityData = player.getPersistentData();

			if (selectedTeam != null) {
				if (!selectedTeam.getPlayers().isEmpty()) {
					Collection<String> teamMembers = selectedTeam.getPlayers();
					if (!teamMembers.isEmpty()) {
						String memberName = Iterables.get(teamMembers, 0);
						Player teamMember = PlayerHelper.getPlayerEntityByName(world, memberName);

						if (teamMember != null) {
							BlockPos pos = teamMember.blockPosition();
							ResourceKey<Level> teamDimension = teamMember.level.dimension();
							if (player.level.dimension() != teamDimension)
								player.changeDimension((ServerLevel) teamMember.level, new UHCTeleporter(pos));

							player.teleportTo(pos.getX(), pos.getY(), pos.getZ());
							if (!player.level.isClientSide) {
								((ServerPlayer) player).setGameMode(GameType.SURVIVAL);
							}

							if (scoreboard.getOrCreateObjective("health") != null)
								scoreboard.resetPlayerScore(player.getName().getString(), scoreboard.getOrCreateObjective("health"));

							setCustomHealth(player, uhcData);
						}
					} else {
						Optional<BlockPos> bedPos = player.getSleepingPos();

						String dimensionLocation = entityData.getString("deathDim");
						Long deathPosLong = entityData.getLong("deathPos");

						if (dimensionLocation.isEmpty() && deathPosLong.equals(0L)) {
							BlockPos deathPos = BlockPos.ZERO;

							if (deathPosLong != 0L) {
								deathPos = BlockPos.of(deathPosLong);
							} else {
								if (bedPos.isPresent()) {
									deathPos = bedPos.get();
								}
							}

							if (!dimensionLocation.isEmpty()) {
								if (!player.level.dimension().location().toString().equals(dimensionLocation)) {
									ResourceLocation location = ResourceLocation.tryParse(dimensionLocation);
									if (location != null) {
										ResourceKey<Level> deathDimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, location);
										ServerLevel dimensionWorld = ctx.getSource().getServer().getLevel(deathDimension);
										player.changeDimension(dimensionWorld, new UHCTeleporter(deathPos));
									}
								}
							}

							player.teleportTo(deathPos.getX(), deathPos.getY(), deathPos.getZ());
						} else {
							ResourceKey<Level> deathDimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, uhcData.getUHCDimension());
							ServerLevel dimensionWorld = ctx.getSource().getServer().getLevel(deathDimension);
							if (!player.level.dimension().location().equals(uhcData.getUHCDimension())) {
								player.changeDimension(dimensionWorld, new UHCTeleporter());
							}

							ArrayList<ServerPlayer> playerList = new ArrayList<>(Collections.singletonList((ServerPlayer) player));
							WorldBorder border = dimensionWorld.getWorldBorder();

							double centerX = uhcData.getBorderCenterX();
							double centerZ = uhcData.getBorderCenterZ();
							if (border.getCenterX() != centerX && border.getCenterZ() != centerZ)
								border.setCenter(centerX, centerZ);

							int BorderSize = uhcData.getBorderSize();

							double spreadDistance = uhcData.getSpreadDistance();
							double spreadMaxRange = uhcData.getSpreadMaxRange();

							if (spreadMaxRange >= (BorderSize / 2.0))
								spreadMaxRange = (BorderSize / 2.0);

							if (uhcData.isRandomSpawns()) {
								if (selectedTeam == scoreboard.getPlayerTeam("solo")) {
									try {
										SpreadUtil.spread(playerList, new SpreadPosition(centerX, centerZ), spreadDistance, spreadMaxRange, dimensionWorld, uhcData.isSpreadRespectTeam());
									} catch (CommandRuntimeException e) {
										e.printStackTrace();
									}
								}
								try {
									SpreadUtil.spread(playerList, new SpreadPosition(centerX, centerZ), spreadDistance, spreadMaxRange, dimensionWorld, false);
								} catch (CommandRuntimeException e) {
									e.printStackTrace();
								}
							} else {
								for (Player players : playerList) {
									if (selectedTeam != scoreboard.getPlayerTeam("solo")) {
										BlockPos pos = TeamUtil.getPosForTeam(player.getTeam().getColor());

										((ServerPlayer) player).connection.teleport(pos.getX(), pos.getY(), pos.getZ(), player.getYRot(), player.getXRot());
									} else {
										try {
											SpreadUtil.spread(playerList, new SpreadPosition(centerX, centerZ), spreadDistance, spreadMaxRange, dimensionWorld, false);
										} catch (CommandRuntimeException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
						if (!player.level.isClientSide) {
							((ServerPlayer) player).setGameMode(GameType.SURVIVAL);
						}
						if (scoreboard.getOrCreateObjective("health") != null)
							scoreboard.resetPlayerScore(player.getName().getString(), scoreboard.getOrCreateObjective("health"));

						setCustomHealth(player, uhcData);
					}
				}
			}
		}
	}

	public static void setCustomHealth(Player player, UHCSaveData uhcData) {
		CompoundTag entityData = player.getPersistentData();

		double playerHealth = player.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
		boolean flag = uhcData.isApplyCustomHealth();
		double maxHealth = (double) uhcData.getMaxHealth();

		if (playerHealth != maxHealth && flag) {
			player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);

			int instantHealth = uhcData.getMaxHealth() / 4;
			player.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, instantHealth, true, false));

			entityData.putBoolean("modifiedMaxHealth", true);
		}
	}
}
