package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.utils.SpreadPosition;
import com.mrbysco.uhc.utils.SpreadUtil;
import com.mrbysco.uhc.utils.TeamUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UHCStartPacket {
	public UHCStartPacket() {

	}

	public void encode(FriendlyByteBuf buf) {

	}

	public static UHCStartPacket decode(final FriendlyByteBuf packetBuffer) {
		return new UHCStartPacket();
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer serverPlayer = ctx.getSender();
				ServerLevel overworld = serverPlayer.getServer().overworld();
				if (overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundTag playerData = serverPlayer.getPersistentData();
					ServerLevel level = serverPlayer.getLevel();
					WorldBorder border = level.getWorldBorder(); //TODO: Check if this should be using the overworld like the command does
					MinecraftServer server = level.getServer();
					List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
					Scoreboard scoreboard = level.getScoreboard();
					LevelData info = level.getLevelData();

					if (playerData.getBoolean("canEditUHC")) {
						List<ServerPlayer> soloPlayers = new ArrayList<>(playerList);
						List<ServerPlayer> teamPlayers = new ArrayList<>(playerList);

						for (ServerPlayer player : playerList) {
							if (player.getTeam() == scoreboard.getPlayerTeam("spectator"))
								player.setGameMode(GameType.SPECTATOR);
							if (player.getTeam() == null)
								scoreboard.addPlayerToTeam(player.getName().getString(), scoreboard.getPlayerTeam("solo"));
							if (player.getTeam() != scoreboard.getPlayerTeam("solo"))
								soloPlayers.remove(player);
						}

						teamPlayers.removeAll(soloPlayers);

						double centerX = saveData.getBorderCenterX();
						double centerZ = saveData.getBorderCenterZ();
						if (border.getCenterX() != centerX && border.getCenterZ() != centerZ)
							border.setCenter(centerX, centerZ);

						int borderSize = saveData.getBorderSize();
						border.setSize(borderSize);

						double spreadDistance = saveData.getSpreadDistance();
						double spreadMaxRange = saveData.getSpreadMaxRange();

						if (spreadMaxRange >= (borderSize / 2F))
							spreadMaxRange = (borderSize / 2F);

						level.setDayTime(0);
						info.setRaining(false);

						if (saveData.isRandomSpawns()) {
							try {
								SpreadUtil.spread(soloPlayers, new SpreadPosition(centerX, centerZ), spreadDistance, spreadMaxRange, level, saveData.isSpreadRespectTeam());
							} catch (CommandRuntimeException e) {
								e.printStackTrace();
							}

							try {
								SpreadUtil.spread(teamPlayers, new SpreadPosition(centerX, centerZ), spreadDistance, spreadMaxRange, level, false);
							} catch (CommandRuntimeException e) {
								e.printStackTrace();
							}
						} else {
							for (ServerPlayer player : playerList) {
								if (player.getTeam() != scoreboard.getPlayerTeam("solo")) {
									BlockPos pos = TeamUtil.getPosForTeam(player.getTeam().getColor());

									player.connection.teleport(pos.getX(), pos.getY(), pos.getZ(), player.getYRot(), player.getXRot());
								} else {
									try {
										SpreadUtil.spread(soloPlayers, new SpreadPosition(centerX, centerZ), spreadDistance, spreadMaxRange, level, false);
									} catch (CommandRuntimeException e) {
										e.printStackTrace();
									}
								}
							}
						}

						for (ServerPlayer player : playerList) {
							Objective score = scoreboard.getOrCreateObjective("health");
							if (score != null)
								scoreboard.resetPlayerScore(player.getName().getString(), score);

							if (player.isCreative())
								player.setGameMode(GameType.SURVIVAL);

							if (player.getEffect(MobEffects.GLOWING) != null)
								player.removeEffect(MobEffects.GLOWING);
						}

						if (saveData.isSpawnRoom()) {
							double centerX1 = centerX - 7;
							double centerX2 = centerX + 7;
							double centerZ1 = centerZ - 7;
							double centerZ2 = centerZ + 7;

							for (double i = centerX1; i <= centerX2; i++) {
								for (double j = centerZ1; j <= centerZ2; j++) {
									for (double k = 250; k <= 253; k++) {
										level.setBlockAndUpdate(new BlockPos(i, k, j), Blocks.AIR.defaultBlockState());
									}
								}
							}
							saveData.setSpawnRoom(false);
							saveData.setSpawnRoomDimension(Level.OVERWORLD.location());
							saveData.setDirty();
						}

						saveData.setUHCDimension(serverPlayer.level.dimension().location());
						saveData.setUhcStarting(true);
						saveData.setDirty();
					} else {
						serverPlayer.sendSystemMessage(Component.literal(ChatFormatting.RED + "You don't have permissions to start the UHC"));
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
