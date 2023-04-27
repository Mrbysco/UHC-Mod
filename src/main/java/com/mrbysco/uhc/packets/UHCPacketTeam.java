package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class UHCPacketTeam {
	public final String playerName;
	public final String team;
	public final String teamName;
	public final int colorIndex;

	public UHCPacketTeam(String name, String team, String teamName, int colorIndex) {
		this.playerName = name;
		this.team = team;
		this.teamName = teamName;
		this.colorIndex = colorIndex;
	}

	public UHCPacketTeam(Component name, String team, String teamName, int colorIndex) {
		this.playerName = name.getString();
		this.team = team;
		this.teamName = teamName;
		this.colorIndex = colorIndex;
	}

	public UHCPacketTeam(FriendlyByteBuf packetBuffer) {
		this.playerName = packetBuffer.readUtf();
		this.team = packetBuffer.readUtf();
		this.teamName = packetBuffer.readUtf();
		this.colorIndex = packetBuffer.readInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(playerName);
		buf.writeUtf(team);
		buf.writeUtf(teamName);
		buf.writeInt(colorIndex);
	}

	public static UHCPacketTeam decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPacketTeam(packetBuffer);
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
					if (playerData.getBoolean(teamAntiSpam)) {
						serverPlayer.sendSystemMessage(Component.translatable("book.uhc.team.antispam"));
					} else {
						if (saveData.areTeamsLocked()) {
							if (playerData.getBoolean("canEditUHC")) {
								switchTeams(serverPlayer, saveData.getMaxTeamSize());
							} else {
								serverPlayer.sendSystemMessage(Component.translatable("book.uhc.team.locked"));
							}
						} else {
							switchTeams(serverPlayer, saveData.getMaxTeamSize());
						}
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}

	private final String teamAntiSpam = Reference.MOD_PREFIX + "team_anti_spam";

	private void switchTeams(ServerPlayer serverPlayer, int maxTeamSize) {
		Scoreboard scoreboard = serverPlayer.getLevel().getScoreboard();
		CompoundTag playerData = serverPlayer.getPersistentData();
		PlayerTeam scorePlayerTeam = scoreboard.getPlayerTeam(team);
		if (team.equals("solo")) {
			scoreboard.addPlayerToTeam(playerName, scorePlayerTeam);
			playerData.putBoolean(teamAntiSpam, true);
			sendTeamSwitchMessage(serverPlayer);
		} else {
			if (maxTeamSize == -1) {
				scoreboard.addPlayerToTeam(playerName, scorePlayerTeam);
				playerData.putBoolean(teamAntiSpam, true);
				sendTeamSwitchMessage(serverPlayer);
			} else {
				if (scorePlayerTeam.getPlayers().size() < maxTeamSize) {
					scoreboard.addPlayerToTeam(playerName, scorePlayerTeam);
					playerData.putBoolean(teamAntiSpam, true);
					sendTeamSwitchMessage(serverPlayer);
				} else {
					serverPlayer.sendSystemMessage(Component.translatable("book.uhc.team.maxed", team));
				}
			}
		}
	}

	private void sendTeamSwitchMessage(ServerPlayer serverPlayer) {
		for (ServerPlayer players : serverPlayer.getServer().getPlayerList().getPlayers()) {
			if (team.equals("solo"))
				players.sendSystemMessage(Component.translatable("book.uhc.team.solo", playerName, ChatFormatting.getById(colorIndex) + teamName));
			else
				players.sendSystemMessage(Component.translatable("book.uhc.team.selected", playerName, ChatFormatting.getById(colorIndex) + teamName));
		}
	}
}
