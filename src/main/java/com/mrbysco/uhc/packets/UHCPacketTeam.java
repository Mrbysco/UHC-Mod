package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class UHCPacketTeam {
	public String playerName;
	public String team;
	public String teamName;
	public int colorIndex;

	public UHCPacketTeam(String name, String team, String teamName, int colorIndex) {
		this.playerName = name;
		this.team = team;
		this.teamName = teamName;
		this.colorIndex = colorIndex;
	}

	public UHCPacketTeam(ITextComponent name, String team, String teamName, int colorIndex) {
		this.playerName = name.toString();
		this.team = team;
		this.teamName = teamName;
		this.colorIndex = colorIndex;
	}

	public UHCPacketTeam(PacketBuffer packetBuffer) {
		this.playerName = packetBuffer.readString();
		this.team = packetBuffer.readString();
		this.teamName = packetBuffer.readString();
		this.colorIndex = packetBuffer.readInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeString(playerName);
		buf.writeString(team);
		buf.writeString(teamName);
		buf.writeInt(colorIndex);
	}

	public static UHCPacketTeam decode(final PacketBuffer packetBuffer) {
		return new UHCPacketTeam(packetBuffer);
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
					if(playerData.getBoolean(teamAntiSpam)) {
						serverPlayer.sendMessage(new TranslationTextComponent("book.uhc.team.antispam"), Util.DUMMY_UUID);
					} else {
						if(saveData.areTeamsLocked()) {
							if(playerData.getBoolean("canEditUHC") == true) {
								switchTeams(serverPlayer, saveData.getMaxTeamSize());
							} else {
								serverPlayer.sendMessage(new TranslationTextComponent("book.uhc.team.locked"), Util.DUMMY_UUID);
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

	private void switchTeams(ServerPlayerEntity serverPlayer, int maxTeamSize) {
		Scoreboard scoreboard = serverPlayer.getServerWorld().getScoreboard();
		CompoundNBT playerData = serverPlayer.getPersistentData();
		ScorePlayerTeam scorePlayerTeam = scoreboard.getTeam(team);
		if(team.equals("solo")) {
			scoreboard.addPlayerToTeam(playerName, scorePlayerTeam);
			playerData.putBoolean(teamAntiSpam, true);
			sendTeamSwitchMessage(serverPlayer);
		} else {
			if(maxTeamSize == -1) {
				scoreboard.addPlayerToTeam(playerName, scorePlayerTeam);
				playerData.putBoolean(teamAntiSpam, true);
				sendTeamSwitchMessage(serverPlayer);
			} else {
				if(scorePlayerTeam.getMembershipCollection().size() < maxTeamSize) {
					scoreboard.addPlayerToTeam(playerName, scorePlayerTeam);
					playerData.putBoolean(teamAntiSpam, true);
					sendTeamSwitchMessage(serverPlayer);
				} else {
					serverPlayer.sendMessage(new TranslationTextComponent("book.uhc.team.maxed", team), Util.DUMMY_UUID);
				}
			}
		}
	}

	private void sendTeamSwitchMessage(ServerPlayerEntity serverPlayer) {
		for(ServerPlayerEntity players : serverPlayer.getServer().getPlayerList().getPlayers()) {
			if(team.equals("solo"))
				players.sendMessage(new TranslationTextComponent("book.uhc.team.solo", playerName, TextFormatting.fromColorIndex(colorIndex) + teamName), Util.DUMMY_UUID);
			else
				players.sendMessage(new TranslationTextComponent("book.uhc.team.selected", playerName, TextFormatting.fromColorIndex(colorIndex) + teamName), Util.DUMMY_UUID);
		}
	}
}
