package com.mrbysco.uhc.commands;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ScoreHolderArgument;
import net.minecraft.command.arguments.TeamArgument;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UHCCommands {


	public static void initializeCommands (CommandDispatcher<CommandSource> dispatcher) {
		final LiteralArgumentBuilder<CommandSource> root = Commands.literal("uhc");
		root.requires((commandSource) -> commandSource.hasPermissionLevel(2))
				.then(Commands.literal("forceteam")
						.then(Commands.argument("team", TeamArgument.team())
								.executes((source) -> forceTeam(source, TeamArgument.getTeam(source, "team"), ScoreHolderArgument.getScoreHolder(source, "members")))))
				.then(Commands.literal("reset")
						.executes((source) -> reset(source)))
				.then(Commands.literal("spawnroom")
						.then(Commands.literal("place")
							.executes((source) -> placeSpawnroom(source, "place")))
						.then(Commands.literal("remove")
								.executes((source) -> placeSpawnroom(source, "remove"))))
				.then(Commands.literal("respawn")
						.then(Commands.argument("team", TeamArgument.team())
								.executes((source) -> respawn(source, TeamArgument.getTeam(source, "team"), ScoreHolderArgument.getScoreHolder(source, "members")))));


		dispatcher.register(root);
	}

	private static int reset(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		MinecraftServer server = ctx.getSource().getServer();
		List<ServerPlayerEntity> playerList = server.getPlayerList().getPlayers();
		ServerWorld overworld = server.getWorld(World.OVERWORLD);
		if(overworld != null) {
			UHCSaveData saveData = UHCSaveData.get(overworld);
			UHCTimerData timerData = UHCTimerData.get(overworld);
			Scoreboard scoreboard = overworld.getScoreboard();

			if(scoreboard != null) {
				for(ScorePlayerTeam team : scoreboard.getTeams()) {
					if(team != null && team.getMembershipCollection().size() > 0 && team != scoreboard.getTeam("spectator")) {
						if(team.getMembershipCollection() != null && !team.getMembershipCollection().isEmpty()) {
							List<String> foundPlayers = new ArrayList<>();
							for(String players : team.getMembershipCollection()) {
								foundPlayers.add(players);
							}

							for(String playerFound : foundPlayers) {
								scoreboard.removePlayerFromTeam(playerFound, team);
							}
						}
					}
				}
			}

			for(ServerPlayerEntity player : playerList) {
				player.inventory.clear();
				player.heal(Integer.MAX_VALUE);

				if(player.getTeam() != null) {
					ScorePlayerTeam spectatorTeam = scoreboard.getTeam("spectator");
					scoreboard.addPlayerToTeam(player.getName().getString(), spectatorTeam);
				}
			}

			double centerX = saveData.getBorderCenterX();
			double centerZ = saveData.getBorderCenterZ();
			double centerX1 = centerX -7;
			double centerX2 = centerX +7;
			double centerZ1 = centerZ -7;
			double centerZ2 = centerZ +7;
			RegistryKey<World> spawnRoom = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, saveData.getSpawnRoomDimension());
			ServerWorld world = server.getWorld(spawnRoom);
			for(double i = centerX1; i <= centerX2; i++) {
				for(double j = centerZ1; j <= centerZ2; j++) {
					for(double k = 250; k <= 253; k++) {
						world.setBlockState(new BlockPos(i, k, j), Blocks.AIR.getDefaultState());
					}
				}
			}

			server.getCommandManager().handleCommand(ctx.getSource(), "/worldborder set " + server.getMaxWorldSize());

			timerData.resetAll();
			timerData.markDirty();
			saveData.resetAll();
			saveData.markDirty();

			UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(saveData));
			ctx.getSource().sendFeedback(new TranslationTextComponent("commands.uhc.reset.success"), true);
		}

		return 0;
	}

	private static int placeSpawnroom(CommandContext<CommandSource> ctx, String value) throws CommandSyntaxException {
		MinecraftServer server = ctx.getSource().getServer();
		ServerWorld overworld = server.getWorld(World.OVERWORLD);
		if(overworld != null) {
			ServerWorld senderWorld = ctx.getSource().getWorld();
			UHCSaveData saveData = UHCSaveData.get(overworld);
			double centerX = saveData.getBorderCenterX();
			double centerZ = saveData.getBorderCenterZ();

			double centerX1 = centerX -7;
			double centerX2 = centerX +7;
			double centerZ1 = centerZ -7;
			double centerZ2 = centerZ +7;
			ResourceLocation spawnroomBlock = ResourceLocation.tryCreate(UHCConfig.COMMON.spawnRoomBlock.get());
			Block roomBlock = ForgeRegistries.BLOCKS.getValue(spawnroomBlock);
			if(roomBlock == null) {
				roomBlock = Blocks.BARRIER;
			}
			RegistryKey<World> spawnRoom = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, saveData.getSpawnRoomDimension());
			ServerWorld world = server.getWorld(spawnRoom);
			if(world != null) {
				if(value.equals("place")) {
					for(double i = centerX1; i <= centerX2; i++)
					{
						for(double j = centerZ1; j <= centerZ2; j++)
						{
							for(double k = 250; k <= 253; k++)
							{
								world.setBlockState(new BlockPos(i, k, j), roomBlock.getDefaultState());
							}
						}
					}
					saveData.setSpawnRoom(false);
					saveData.setSpawnRoomDimension(World.OVERWORLD.getLocation());
					saveData.markDirty();
				} else {
					for(double i = centerX1; i <= centerX2; i++) {
						for(double j = centerZ1; j <= centerZ2; j++) {
							senderWorld.setBlockState(new BlockPos(i, 250, j), roomBlock.getDefaultState());
							if(j == centerZ1 || j == centerZ2) {
								for(double k = 250; k <= 253; k++) {
									senderWorld.setBlockState(new BlockPos(i, k, j), roomBlock.getDefaultState());
								}
							}
						}

						if(i == centerX1 || i == centerX2) {
							for(double j = centerZ1; j <= centerZ2; j++) {
								for(double k = 250; k <= 253; k++) {
									senderWorld.setBlockState(new BlockPos(i, k, j), roomBlock.getDefaultState());
								}
							}
						}
					}
					senderWorld.setSpawnLocation(new BlockPos(centerX, 252, centerZ), 90F);
					saveData.setSpawnRoom(true);
					saveData.setSpawnRoomDimension(senderWorld.getDimensionKey().getLocation());
					saveData.markDirty();
					ctx.getSource().sendFeedback(new TranslationTextComponent("commands.uhc.spawnroom.success"), true);
				}
			}
		}
		return 0;
	}

	private static int forceTeam(CommandContext<CommandSource> ctx, ScorePlayerTeam teamIn, Collection<String> players) throws CommandSyntaxException {
		CommandSource source = ctx.getSource();
		Scoreboard scoreboard = source.getServer().getScoreboard();

		for(String s : players) {
			scoreboard.addPlayerToTeam(s, teamIn);
		}

		source.sendFeedback(new TranslationTextComponent("commands.uhc.forceteam.success", players.size(), teamIn.func_237501_d_()), true);

		return players.size();
	}

	private static int respawn(CommandContext<CommandSource> ctx, ScorePlayerTeam teamIn, Collection<String> players) throws CommandSyntaxException {
		MinecraftServer server = ctx.getSource().getServer();
		ServerWorld overworld = server.getWorld(World.OVERWORLD);
		Scoreboard scoreboard = server.getScoreboard();
		if(overworld != null) {
			UHCSaveData saveData = UHCSaveData.get(overworld);
			for(String s : players) {
				if(scoreboard.addPlayerToTeam(s, teamIn)) {
					respawnPlayers(ctx, s, teamIn, scoreboard, overworld, saveData);

				}
			}

		}
		return players.size();
	}

	public static void respawnPlayers(CommandContext<CommandSource> ctx, String playerName, ScorePlayerTeam selectedTeam, Scoreboard scoreboard, ServerWorld world, UHCSaveData uhcData) {
		PlayerEntity player = PlayerHelper.getPlayerEntityByName(world, playerName);
		if(player != null) {
			CompoundNBT entityData = player.getPersistentData();

			if(selectedTeam != null) {
				if(!selectedTeam.getMembershipCollection().isEmpty()) {
					Collection<String> teamMembers = selectedTeam.getMembershipCollection();
					if(!teamMembers.isEmpty()) {
						String memberName = Iterables.get(teamMembers, 0);
						PlayerEntity teamMember = PlayerHelper.getPlayerEntityByName(world, memberName);

						if(teamMember != null) {
							BlockPos pos = teamMember.getPosition();
							RegistryKey<World> teamDimension = teamMember.world.getDimensionKey();
							if(player.world.getDimensionKey() != teamDimension)
								player.changeDimension((ServerWorld)teamMember.world, new UHCTeleporter(pos));

							player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
							player.setGameType(GameType.SURVIVAL);

							if(scoreboard.getObjective("health") != null)
								scoreboard.removeObjectiveFromEntity(player.getName().getString(), scoreboard.getObjective("health"));

							setCustomHealth(player, uhcData);
						}
					} else {
						Optional<BlockPos> bedPos = player.getBedPosition();

						String dimensionLocation = entityData.getString("deathDim");
						Long deathPosLong = entityData.getLong("deathPos");

						if(dimensionLocation.isEmpty() && deathPosLong.equals(0L)) {
							BlockPos deathPos = BlockPos.ZERO;

							if(deathPosLong != 0L) {
								deathPos = BlockPos.fromLong(deathPosLong);
							} else {
								if(bedPos.isPresent()) {
									deathPos = bedPos.get();
								}
							}

							if(!dimensionLocation.isEmpty()) {
								if(!player.world.getDimensionKey().getLocation().toString().equals(dimensionLocation)) {
									ResourceLocation location = ResourceLocation.tryCreate(dimensionLocation);
									if(location != null) {
										RegistryKey<World> deathDimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, location);
										ServerWorld dimensionWorld = ctx.getSource().getServer().getWorld(deathDimension);
										player.changeDimension(dimensionWorld, new UHCTeleporter(deathPos));
									}
								}
							}

							player.setPositionAndUpdate(deathPos.getX(), deathPos.getY(), deathPos.getZ());
						} else {
							RegistryKey<World> deathDimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, uhcData.getUHCDimension());
							ServerWorld dimensionWorld = ctx.getSource().getServer().getWorld(deathDimension);
							if(!player.world.getDimensionKey().getLocation().equals(uhcData.getUHCDimension())) {
								player.changeDimension(dimensionWorld, new UHCTeleporter());
							}

							ArrayList<ServerPlayerEntity> playerList = new ArrayList<>(Collections.singletonList((ServerPlayerEntity) player));
							WorldBorder border = dimensionWorld.getWorldBorder();

							double centerX = uhcData.getBorderCenterX();
							double centerZ = uhcData.getBorderCenterZ();
							if (border.getCenterX() != centerX && border.getCenterZ() != centerZ)
								border.setCenter(centerX, centerZ);

							int BorderSize = uhcData.getBorderSize();

							double spreadDistance = uhcData.getSpreadDistance();
							double spreadMaxRange = uhcData.getSpreadMaxRange();

							if(spreadMaxRange >= (BorderSize / 2.0))
								spreadMaxRange = (BorderSize / 2.0);

							if(uhcData.isRandomSpawns()) {
								if(selectedTeam == scoreboard.getTeam("solo")) {
									try {
										SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, dimensionWorld, uhcData.isSpreadRespectTeam());
									} catch (CommandException e) {
										e.printStackTrace();
									}
								}
								try {
									SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, dimensionWorld, false);
								} catch (CommandException e) {
									e.printStackTrace();
								}
							} else {
								for(PlayerEntity players : playerList) {
									if(selectedTeam != scoreboard.getTeam("solo")) {
										BlockPos pos = TeamUtil.getPosForTeam(player.getTeam().getColor());

										((ServerPlayerEntity)player).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
									} else {
										try {
											SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, dimensionWorld, false);
										} catch (CommandException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}

						player.setGameType(GameType.SURVIVAL);
						if(scoreboard.getObjective("health") != null)
							scoreboard.removeObjectiveFromEntity(player.getName().getString(), scoreboard.getObjective("health"));

						setCustomHealth(player, uhcData);
					}
				}
			}
		}
	}

	public static void setCustomHealth(PlayerEntity player, UHCSaveData uhcData) {
		CompoundNBT entityData = player.getPersistentData();

		double playerHealth = player.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
		boolean flag = uhcData.isApplyCustomHealth();
		double maxHealth = (double) uhcData.getMaxHealth();

		if(playerHealth != maxHealth && flag) {
			player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);

			int instantHealth = uhcData.getMaxHealth() / 4;
			player.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 1, instantHealth, true, false));

			entityData.putBoolean("modifiedMaxHealth", true);
		}
	}
}
