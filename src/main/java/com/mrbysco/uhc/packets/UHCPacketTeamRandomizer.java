package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class UHCPacketTeamRandomizer {
	public UHCPacketTeamRandomizer() {

	}

	public void encode(FriendlyByteBuf buf) {

	}

	public static UHCPacketTeamRandomizer decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPacketTeamRandomizer();
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer serverPlayer = ctx.getSender();
				ServerLevel overworld = serverPlayer.getServer().getLevel(Level.OVERWORLD);
				if (overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundTag playerData = serverPlayer.getPersistentData();
					ServerLevel world = serverPlayer.getLevel();
					MinecraftServer server = world.getServer();
					List<ServerPlayer> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
					Scoreboard scoreboard = world.getScoreboard();

					if (playerData.getBoolean("canEditUHC") == true) {
						List<ServerPlayer> teamPlayers = new ArrayList<>(playerList);

						for (Player player : playerList) {
							if (player.getTeam() == scoreboard.getPlayerTeam("spectator"))
								teamPlayers.remove(player);
							else
								scoreboard.removePlayerFromTeam(player.getName().getString());
						}

						List<PlayerTeam> foundTeams = new ArrayList<PlayerTeam>();
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

						ArrayList<String> possibleTeams = getTeams();

						for (int i = 0; i < randomTeams; i++) {
							String teamName = possibleTeams.get(possibleTeams.size() > 1 ? world.random.nextInt(possibleTeams.size()) : 0);
							possibleTeams.remove(teamName);
							PlayerTeam team = scoreboard.getPlayerTeam(teamName);

							for (int j = 0; j < amountPerTeam; j++) {
								if (!tempList.isEmpty()) {
									Player player = tempList.get(0);

									PlayerTeam scorePlayerTeam = scoreboard.getPlayerTeam(teamName);
									scoreboard.addPlayerToTeam(player.getName().getString(), scorePlayerTeam);
									for (ServerPlayer players : playerList) {
										if (team != null)
											players.sendSystemMessage(Component.translatable("book.uhc.team.randomized", player.getName(), team.getColor() + team.getName().replaceAll("_", " ")));
									}
									tempList.remove(player);
								}
							}
						}
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to randomize the teams").withStyle(ChatFormatting.RED));
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}

	private static ArrayList<String> getTeams() {
		ArrayList<String> teams = new ArrayList<>();

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