package com.mrbysco.uhc.command;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.network.payload.UHCSyncPayload;
import com.mrbysco.uhc.registry.UHCDataAttachments;
import com.mrbysco.uhc.util.PlayerHelper;
import com.mrbysco.uhc.util.SpreadPosition;
import com.mrbysco.uhc.util.SpreadUtil;
import com.mrbysco.uhc.util.TeamUtil;
import com.mrbysco.uhc.util.UHCTransition;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
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
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EventBusSubscriber
public class UHCCommands {
	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event) {
		UHCCommands.initializeCommands(event.getDispatcher(), event.getBuildContext());
	}

	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
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
					if (team != null && !team.getPlayers().isEmpty() && team != scoreboard.getPlayerTeam("spectator")) {
						List<String> foundPlayers = new ArrayList<>(team.getPlayers());

						for (String playerFound : foundPlayers) {
							scoreboard.removePlayerFromTeam(playerFound, team);
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
			ResourceKey<Level> spawnRoom = ResourceKey.create(Registries.DIMENSION, saveData.getSpawnRoomDimension());
			ServerLevel level = server.getLevel(spawnRoom);
			for (double i = centerX1; i <= centerX2; i++) {
				for (double j = centerZ1; j <= centerZ2; j++) {
					for (double k = 250; k <= 253; k++) {
						level.setBlockAndUpdate(BlockPos.containing(i, k, j), Blocks.AIR.defaultBlockState());
					}
				}
			}

			WorldBorder border = level.getWorldBorder(); //TODO: Check if this should be using the overworld like the command does
			border.setSize(server.getAbsoluteMaxWorldSize());

			timerData.resetAll();
			timerData.setDirty();
			saveData.resetAll(server.registryAccess());
			saveData.setDirty();

			PacketDistributor.sendToAllPlayers(new UHCSyncPayload(saveData.save(new CompoundTag(), level.registryAccess())));
			ctx.getSource().sendSuccess(() -> Component.translatable("commands.uhc.reset.success"), true);
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
			Block roomBlock = BuiltInRegistries.BLOCK.get(spawnroomBlock);
			if (roomBlock == null) {
				roomBlock = Blocks.BARRIER;
			}
			ResourceKey<Level> spawnRoom = ResourceKey.create(Registries.DIMENSION, saveData.getSpawnRoomDimension());
			ServerLevel level = server.getLevel(spawnRoom);
			if (level != null) {
				if (value.equals("place")) {
					for (double i = centerX1; i <= centerX2; i++) {
						for (double j = centerZ1; j <= centerZ2; j++) {
							senderWorld.setBlockAndUpdate(BlockPos.containing(i, 250, j), roomBlock.defaultBlockState());
							if (j == centerZ1 || j == centerZ2) {
								for (double k = 250; k <= 253; k++) {
									senderWorld.setBlockAndUpdate(BlockPos.containing(i, k, j), roomBlock.defaultBlockState());
								}
							}
						}

						if (i == centerX1 || i == centerX2) {
							for (double j = centerZ1; j <= centerZ2; j++) {
								for (double k = 250; k <= 253; k++) {
									senderWorld.setBlockAndUpdate(BlockPos.containing(i, k, j), roomBlock.defaultBlockState());
								}
							}
						}
					}
					senderWorld.setDefaultSpawnPos(BlockPos.containing(centerX, 252, centerZ), 90F);
					saveData.setSpawnRoom(true);
					saveData.setSpawnRoomDimension(senderWorld.dimension().location());
					saveData.setDirty();

					BlockPos centerPos = BlockPos.containing(centerX, 250, centerZ);
					MutableComponent position = ComponentUtils.wrapInSquareBrackets(
							Component.literal(centerPos.toShortString())).withStyle((style) ->
							style.withColor(ChatFormatting.GOLD)
									.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
											"/tp @s " + centerPos.getX() + " " + centerPos.getY() + " " + centerPos.getZ())));

					ctx.getSource().sendSuccess(() -> Component.translatable("commands.uhc.spawnroom.success", position), true);
				} else {
					for (double i = centerX1; i <= centerX2; i++) {
						for (double j = centerZ1; j <= centerZ2; j++) {
							for (double k = 250; k <= 253; k++) {
								BlockPos pos = BlockPos.containing(i, k, j);
								if (level.getBlockState(pos).is(roomBlock))
									level.removeBlock(pos, true);
							}
						}
					}
					saveData.setSpawnRoom(false);
					saveData.setSpawnRoomDimension(Level.OVERWORLD.location());
					saveData.setDirty();
					ctx.getSource().sendSuccess(() -> Component.translatable("commands.uhc.spawnroom.success1"), true);
				}
			}
		}
		return 0;
	}

	private static int forceTeam(CommandContext<CommandSourceStack> ctx, PlayerTeam teamIn, Collection<ScoreHolder> players) {
		CommandSourceStack source = ctx.getSource();
		Scoreboard scoreboard = source.getServer().getScoreboard();

		for (ScoreHolder s : players) {
			scoreboard.addPlayerToTeam(s.getScoreboardName(), teamIn);
		}

		source.sendSuccess(() -> Component.translatable("commands.uhc.forceteam.success", players.size(), teamIn.getFormattedDisplayName()), true);

		return players.size();
	}

	private static int respawn(CommandContext<CommandSourceStack> ctx, PlayerTeam teamIn, Collection<ScoreHolder> players) {
		MinecraftServer server = ctx.getSource().getServer();
		ServerLevel overworld = server.overworld();
		Scoreboard scoreboard = server.getScoreboard();
		if (overworld != null) {
			UHCSaveData saveData = UHCSaveData.get(overworld);
			for (ScoreHolder s : players) {
				if (scoreboard.addPlayerToTeam(s.getScoreboardName(), teamIn)) {
					respawnPlayers(ctx, s.getScoreboardName(), teamIn, scoreboard, overworld, saveData);

				}
			}

		}
		return players.size();
	}

	public static void respawnPlayers(CommandContext<CommandSourceStack> ctx, String playerName, PlayerTeam selectedTeam, Scoreboard scoreboard, ServerLevel world, UHCSaveData uhcData) {
		Player player = PlayerHelper.getPlayerEntityByName(world, playerName);
		if (player != null) {
			if (selectedTeam != null) {
				if (!selectedTeam.getPlayers().isEmpty()) {
					Collection<String> teamMembers = selectedTeam.getPlayers();
					if (!teamMembers.isEmpty()) {
						String memberName = Iterables.get(teamMembers, 0);
						Player teamMember = PlayerHelper.getPlayerEntityByName(world, memberName);

						if (teamMember != null) {
							BlockPos pos = teamMember.blockPosition();
							ResourceKey<Level> teamDimension = teamMember.level().dimension();
							if (player.level().dimension() != teamDimension) {
								ServerLevel dimensionWorld = ctx.getSource().getServer().getLevel(teamDimension);
								DimensionTransition transition = UHCTransition.makeTransition(dimensionWorld, player, Vec3.atCenterOf(pos));
								player.changeDimension(transition);
							}

							player.teleportTo(pos.getX(), pos.getY(), pos.getZ());
							if (!player.level().isClientSide) {
								((ServerPlayer) player).setGameMode(GameType.SURVIVAL);
							}

							if (scoreboard.getObjective("health") != null)
								scoreboard.resetSinglePlayerScore(ScoreHolder.fromGameProfile(player.getGameProfile()), scoreboard.getObjective("health"));

							setCustomHealth(player, uhcData);
						}
					} else {
						if (player.hasData(UHCDataAttachments.DEATH_POS)) {
							GlobalPos deathPos = player.getData(UHCDataAttachments.DEATH_POS);

							if (!player.level().dimension().equals(deathPos.dimension())) {
								DimensionTransition transition = UHCTransition.makeTransition(ctx.getSource().getServer().getLevel(deathPos.dimension()), player, Vec3.atCenterOf(deathPos.pos()));
								player.changeDimension(transition);
							}

							BlockPos pos = deathPos.pos();
							player.teleportTo(pos.getX(), pos.getY(), pos.getZ());
						} else {
							ResourceKey<Level> deathDimension = ResourceKey.create(Registries.DIMENSION, uhcData.getUHCDimension());
							ServerLevel dimensionWorld = ctx.getSource().getServer().getLevel(deathDimension);
							if (!player.level().dimension().location().equals(uhcData.getUHCDimension())) {
								DimensionTransition transition = UHCTransition.makeTransition(dimensionWorld, player, player.position());
								player.changeDimension(transition);
							}

							List<ServerPlayer> playerList = new ArrayList<>(Collections.singletonList((ServerPlayer) player));
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
									} catch (RuntimeException e) {
										e.printStackTrace();
									}
								}
								try {
									SpreadUtil.spread(playerList, new SpreadPosition(centerX, centerZ), spreadDistance, spreadMaxRange, dimensionWorld, false);
								} catch (RuntimeException e) {
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
										} catch (RuntimeException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
						if (!player.level().isClientSide) {
							((ServerPlayer) player).setGameMode(GameType.SURVIVAL);
						}
						if (scoreboard.getObjective("health") != null)
							scoreboard.resetSinglePlayerScore(ScoreHolder.fromGameProfile(player.getGameProfile()), scoreboard.getObjective("health"));

						setCustomHealth(player, uhcData);
					}
				}
			}
		}
	}

	public static void setCustomHealth(Player player, UHCSaveData uhcData) {
		double playerHealth = player.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
		boolean flag = uhcData.isApplyCustomHealth();
		double maxHealth = (double) uhcData.getMaxHealth();

		if (playerHealth != maxHealth && flag) {
			player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);

			int instantHealth = uhcData.getMaxHealth() / 4;
			player.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, instantHealth, true, false));
			player.setData(UHCDataAttachments.MODIFIED_MAX_HEALTH, true);
		}
	}
}
