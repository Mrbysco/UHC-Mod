package com.mrbysco.uhc.network.payload;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.registry.UHCDataAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UHCTeamPayload(String playerName, String team, String teamName,
                             int colorIndex) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, UHCTeamPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			UHCTeamPayload::playerName,
			ByteBufCodecs.STRING_UTF8,
			UHCTeamPayload::team,
			ByteBufCodecs.STRING_UTF8,
			UHCTeamPayload::teamName,
			ByteBufCodecs.INT,
			UHCTeamPayload::colorIndex,
			UHCTeamPayload::new);
	public static final CustomPacketPayload.Type<UHCTeamPayload> ID = new CustomPacketPayload.Type<>(Reference.modLoc("sync_emote"));

	public UHCTeamPayload(Component name, String team, String teamName, int colorIndex) {
		this(name.getString(), team, teamName, colorIndex);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(final UHCTeamPayload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					UHCSaveData saveData = UHCSaveData.get(serverPlayer.level());
					if (serverPlayer.getData(UHCDataAttachments.TEAM_ANTI_SPAM)) {
						serverPlayer.sendSystemMessage(Component.translatable("book.uhc.team.antispam"));
					} else {
						if (saveData.areTeamsLocked()) {
							if (serverPlayer.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
								switchTeams(payload, serverPlayer, saveData.getMaxTeamSize());
							} else {
								serverPlayer.sendSystemMessage(Component.translatable("book.uhc.team.locked"));
							}
						} else {
							switchTeams(payload, serverPlayer, saveData.getMaxTeamSize());
						}
					}
				}
			}).exceptionally(e -> {
				// Handle exception
				context.disconnect(Component.translatable("ultrahardcoremod.networking.team.failed", e.getMessage()));
				return null;
			});
		}

		private static void switchTeams(final UHCTeamPayload payload, ServerPlayer serverPlayer, int maxTeamSize) {
			Scoreboard scoreboard = serverPlayer.serverLevel().getScoreboard();
			PlayerTeam scorePlayerTeam = scoreboard.getPlayerTeam(payload.team());
			if (payload.team().equals("solo")) {
				scoreboard.addPlayerToTeam(payload.playerName(), scorePlayerTeam);
				serverPlayer.setData(UHCDataAttachments.TEAM_ANTI_SPAM, true);
				sendTeamSwitchMessage(payload, serverPlayer);
			} else {
				if (maxTeamSize == -1) {
					scoreboard.addPlayerToTeam(payload.playerName(), scorePlayerTeam);
					serverPlayer.setData(UHCDataAttachments.TEAM_ANTI_SPAM, true);
					sendTeamSwitchMessage(payload, serverPlayer);
				} else {
					if (scorePlayerTeam.getPlayers().size() < maxTeamSize) {
						scoreboard.addPlayerToTeam(payload.playerName(), scorePlayerTeam);
						serverPlayer.setData(UHCDataAttachments.TEAM_ANTI_SPAM, true);
						sendTeamSwitchMessage(payload, serverPlayer);
					} else {
						serverPlayer.sendSystemMessage(Component.translatable("book.uhc.team.maxed", payload.team()));
					}
				}
			}
		}

		private static void sendTeamSwitchMessage(final UHCTeamPayload payload, ServerPlayer serverPlayer) {
			for (ServerPlayer players : serverPlayer.getServer().getPlayerList().getPlayers()) {
				if (payload.team().equals("solo"))
					players.sendSystemMessage(Component.translatable("book.uhc.team.solo", payload.playerName(), ChatFormatting.getById(payload.colorIndex()) + payload.teamName()));
				else
					players.sendSystemMessage(Component.translatable("book.uhc.team.selected", payload.playerName(), ChatFormatting.getById(payload.colorIndex()) + payload.teamName()));
			}
		}
	}
}
