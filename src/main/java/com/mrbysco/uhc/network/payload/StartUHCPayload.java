package com.mrbysco.uhc.network.payload;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.registry.UHCDataAttachments;
import com.mrbysco.uhc.util.SpreadPosition;
import com.mrbysco.uhc.util.SpreadUtil;
import com.mrbysco.uhc.util.TeamUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
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
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record StartUHCPayload() implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, StartUHCPayload> STREAM_CODEC = CustomPacketPayload.codec(
			StartUHCPayload::write,
			StartUHCPayload::new);
	public static final Type<StartUHCPayload> ID = new Type<>(Reference.modLoc("start_uhc"));

	public StartUHCPayload(final FriendlyByteBuf packetBuffer) {
		this();
	}

	public void write(FriendlyByteBuf buf) {
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(final StartUHCPayload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					if (serverPlayer.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
						UHCSaveData saveData = UHCSaveData.get(serverPlayer.level());
						ServerLevel level = serverPlayer.serverLevel();
						WorldBorder border = level.getWorldBorder();
						MinecraftServer server = level.getServer();
						List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
						Scoreboard scoreboard = level.getScoreboard();
						LevelData info = level.getLevelData();

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
						if (border.getCenterX() != centerX || border.getCenterZ() != centerZ)
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
							} catch (RuntimeException e) {
								e.printStackTrace();
							}

							try {
								SpreadUtil.spread(teamPlayers, new SpreadPosition(centerX, centerZ), spreadDistance, spreadMaxRange, level, false);
							} catch (RuntimeException e) {
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
									} catch (RuntimeException e) {
										e.printStackTrace();
									}
								}
							}
						}

						for (ServerPlayer player : playerList) {
							Objective score = scoreboard.getObjective("health");
							if (score != null)
								scoreboard.resetSinglePlayerScore(ScoreHolder.fromGameProfile(player.getGameProfile()), score);

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
										level.setBlockAndUpdate(BlockPos.containing(i, k, j), Blocks.AIR.defaultBlockState());
									}
								}
							}
							saveData.setSpawnRoom(false);
							saveData.setSpawnRoomDimension(Level.OVERWORLD.location());
							saveData.setDirty();
						}

						saveData.setUHCDimension(serverPlayer.level().dimension().location());
						saveData.setUhcStarting(true);
						saveData.setDirty();
					} else {
						serverPlayer.sendSystemMessage(Component.literal(ChatFormatting.RED + "You don't have permissions to start the UHC"));
					}

				}
			}).exceptionally(e -> {
				// Handle exception
				context.disconnect(Component.translatable("ultrahardcoremod.networking.start_uhc.failed", e.getMessage()));
				return null;
			});
		}
	}
}
