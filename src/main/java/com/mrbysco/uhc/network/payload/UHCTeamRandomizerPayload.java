package com.mrbysco.uhc.network.payload;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.registry.UHCDataAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record UHCTeamRandomizerPayload() implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, UHCTeamRandomizerPayload> STREAM_CODEC = CustomPacketPayload.codec(
			UHCTeamRandomizerPayload::write,
			UHCTeamRandomizerPayload::new);
	public static final Type<UHCTeamRandomizerPayload> ID = new Type<>(Reference.modLoc("uhc_team_randomizer"));

	public UHCTeamRandomizerPayload(final FriendlyByteBuf packetBuffer) {
		this();
	}

	public void write(FriendlyByteBuf buf) {
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}


	public static class Handler {
		public static void handle(final UHCTeamRandomizerPayload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					UHCSaveData saveData = UHCSaveData.get(serverPlayer.level());
					ServerLevel level = serverPlayer.serverLevel();
					List<ServerPlayer> playerList = level.players();
					Scoreboard scoreboard = level.getScoreboard();

					if (serverPlayer.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
						List<ServerPlayer> teamPlayers = new ArrayList<>(playerList);

						for (ServerPlayer player : playerList) {
							if (player.getTeam() == scoreboard.getPlayerTeam("spectator"))
								teamPlayers.remove(player);
							else
								scoreboard.removePlayerFromTeam(player.getName().getString());
						}

						List<PlayerTeam> foundTeams = new ArrayList<>();
						for (PlayerTeam team : scoreboard.getPlayerTeams()) {
							if (team != scoreboard.getPlayerTeam("spectator")) {
								foundTeams.add(team);
							}
						}

						for (PlayerTeam team : foundTeams) {
							if (!team.getPlayers().isEmpty()) {
								team.getPlayers().clear();
								foundTeams.remove(team);
							}
						}

						int randomTeams = saveData.getRandomTeamSize();
						if (randomTeams > 14) {
							saveData.setRandomTeamSize(14);
							saveData.setDirty();
							randomTeams = 14;
						}

						Collections.shuffle(playerList);
						List<ServerPlayer> tempList = new ArrayList<>(teamPlayers);

						int playerAmount = playerList.size();
						int amountPerTeam = (int) Math.ceil((double) playerAmount / (double) randomTeams);

						List<String> possibleTeams = getTeams();

						for (int i = 0; i < randomTeams; i++) {
							String teamName = possibleTeams.get(possibleTeams.size() > 1 ? level.random.nextInt(possibleTeams.size()) : 0);
							possibleTeams.remove(teamName);
							PlayerTeam team = scoreboard.getPlayerTeam(teamName);

							for (int j = 0; j < amountPerTeam; j++) {
								if (!tempList.isEmpty()) {
									Player player = tempList.getFirst();

									PlayerTeam scorePlayerTeam = scoreboard.getPlayerTeam(teamName);
									scoreboard.addPlayerToTeam(player.getName().getString(), scorePlayerTeam);
									for (ServerPlayer players : playerList) {
										if (team != null)
											players.sendSystemMessage(Component.translatable("book.uhc.team.randomized", player.getName(), team.getColor() + team.getName().replace("_", " ")));
									}
									tempList.remove(player);
								}
							}
						}
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to randomize the teams").withStyle(ChatFormatting.RED));
					}
				}
			}).exceptionally(e -> {
				// Handle exception
				context.disconnect(Component.translatable("ultrahardcoremod.networking.uhc_team_randomizer.failed", e.getMessage()));
				return null;
			});
		}
	}

	private static List<String> getTeams() {
		List<String> teams = new ArrayList<>();

		teams.add(ChatFormatting.DARK_RED.getName());
		teams.add(ChatFormatting.GOLD.getName());
		teams.add(ChatFormatting.DARK_GREEN.getName());
		teams.add(ChatFormatting.DARK_AQUA.getName());
		teams.add(ChatFormatting.DARK_BLUE.getName());
		teams.add(ChatFormatting.DARK_PURPLE.getName());
		teams.add(ChatFormatting.DARK_GRAY.getName());
		teams.add(ChatFormatting.RED.getName());
		teams.add(ChatFormatting.YELLOW.getName());
		teams.add(ChatFormatting.GREEN.getName());
		teams.add(ChatFormatting.AQUA.getName());
		teams.add(ChatFormatting.BLUE.getName());
		teams.add(ChatFormatting.LIGHT_PURPLE.getName());
		teams.add(ChatFormatting.GRAY.getName());

		return teams;
	}
}