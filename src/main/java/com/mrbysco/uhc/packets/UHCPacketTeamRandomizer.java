package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class UHCPacketTeamRandomizer {
	public UHCPacketTeamRandomizer() {

	}

	public void encode(PacketBuffer buf) {

	}

	public static UHCPacketTeamRandomizer decode(final PacketBuffer packetBuffer) {
		return new UHCPacketTeamRandomizer();
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayerEntity serverPlayer = ctx.getSender();
				ServerWorld overworld = serverPlayer.getServer().getWorld(World.OVERWORLD);
				if(overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundNBT playerData = serverPlayer.getPersistentData();
					ServerWorld world = serverPlayer.getServerWorld();
					MinecraftServer server = world.getServer();
					ArrayList<ServerPlayerEntity> playerList = (ArrayList<ServerPlayerEntity>)server.getPlayerList().getPlayers();
					Scoreboard scoreboard = world.getScoreboard();

					if(playerData.getBoolean("canEditUHC") == true) {
						ArrayList<ServerPlayerEntity>teamPlayers = (ArrayList<ServerPlayerEntity>) playerList.clone();

						for (PlayerEntity player : playerList) {
							if(player.getTeam() == scoreboard.getTeam("spectator"))
								teamPlayers.remove(player);
							else
								scoreboard.removePlayerFromTeams(player.getName().getString());
						}

						List<ScorePlayerTeam> foundTeams = new ArrayList<ScorePlayerTeam>();
						for (ScorePlayerTeam team : scoreboard.getTeams()) {
							if(team != scoreboard.getTeam("spectator")) {
								foundTeams.add(team);
							}
						}

						for(ScorePlayerTeam team : foundTeams) {
							if(!team.getMembershipCollection().isEmpty()) {
								team.getMembershipCollection().clear();
								foundTeams.remove(team);
							}
						}

						int randomTeams = saveData.getRandomTeamSize();
						if(randomTeams > 14) {
							saveData.setRandomTeamSize(14);
							saveData.markDirty();
							randomTeams = 14;
						}

						Collections.shuffle(playerList);
						ArrayList<ServerPlayerEntity>tempList = (ArrayList<ServerPlayerEntity>) teamPlayers.clone();

						int playerAmount = playerList.size();
						int amountPerTeam = (int)Math.ceil((double)playerAmount / (double)randomTeams);

						ArrayList<String> possibleTeams = getTeams();

						for(int i = 0; i < randomTeams; i++) {
							String teamName = possibleTeams.get(possibleTeams.size() > 1 ? world.rand.nextInt(possibleTeams.size()) : 0);
							possibleTeams.remove(teamName);
							ScorePlayerTeam team = scoreboard.getTeam(teamName);

							for(int j = 0; j < amountPerTeam; j++) {
								if(!tempList.isEmpty()) {
									PlayerEntity player = tempList.get(0);

									ScorePlayerTeam scorePlayerTeam = scoreboard.getTeam(teamName);
									scoreboard.addPlayerToTeam(player.getName().getString(), scorePlayerTeam);
									for(ServerPlayerEntity players : playerList) {
										if(team != null)
											players.sendMessage(new TranslationTextComponent("book.uhc.team.randomized", player.getName(), team.getColor() + team.getName().replaceAll("_", " ")), Util.DUMMY_UUID);
									}
									tempList.remove(player);
								}
							}
						}
					} else {
						serverPlayer.sendMessage(new StringTextComponent("You don't have permissions to randomize the teams").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}

	private static ArrayList<String> getTeams() {
		ArrayList<String> teams = new ArrayList<>();

		teams.add(TextFormatting.DARK_RED.getFriendlyName());
		teams.add(TextFormatting.GOLD.getFriendlyName());
		teams.add(TextFormatting.DARK_GREEN.getFriendlyName());
		teams.add(TextFormatting.DARK_AQUA.getFriendlyName());
		teams.add(TextFormatting.DARK_BLUE.getFriendlyName());
		teams.add(TextFormatting.DARK_PURPLE.getFriendlyName());
		teams.add(TextFormatting.DARK_GRAY.getFriendlyName());
		teams.add(TextFormatting.RED.getFriendlyName());
		teams.add(TextFormatting.YELLOW.getFriendlyName());
		teams.add(TextFormatting.GREEN.getFriendlyName());
		teams.add(TextFormatting.AQUA.getFriendlyName());
		teams.add(TextFormatting.BLUE.getFriendlyName());
		teams.add(TextFormatting.LIGHT_PURPLE.getFriendlyName());
		teams.add(TextFormatting.GRAY.getFriendlyName());

		return teams;
	}
}