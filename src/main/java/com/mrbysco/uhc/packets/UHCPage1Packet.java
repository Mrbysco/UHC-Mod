package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team.CollisionRule;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class UHCPage1Packet {
	public int randomTeams;
	public int maxTeams;
	public boolean teamCollision;
	public boolean teamDamage;
	public int difficulty;
	public boolean teamsLocked;

	public UHCPage1Packet(int randomTeam, int maxTeam, boolean collision, boolean teamDamage, int difficulty, boolean teamsLocked) {
		this.randomTeams = randomTeam;
		this.maxTeams = maxTeam;
		this.teamCollision = collision;
		this.teamDamage = teamDamage;
		this.difficulty = difficulty;
		this.teamsLocked = teamsLocked;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(randomTeams);
		buf.writeInt(maxTeams);
		buf.writeBoolean(teamCollision);
		buf.writeBoolean(teamDamage);
		buf.writeInt(difficulty);
		buf.writeBoolean(teamsLocked);
	}

	public static UHCPage1Packet decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPage1Packet(packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readBoolean(),
				packetBuffer.readBoolean(), packetBuffer.readInt(), packetBuffer.readBoolean());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer serverPlayer = ctx.getSender();
				ServerLevel serverWorld = serverPlayer.getLevel();
				ServerLevel overworld = serverPlayer.getServer().getLevel(Level.OVERWORLD);
				if (overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundTag playerData = serverPlayer.getPersistentData();
					MinecraftServer minecraftserver = serverWorld.getServer();
					LevelData wInfo = serverWorld.getLevelData();
					Scoreboard scoreboard = serverWorld.getScoreboard();

					if (playerData.getBoolean("canEditUHC")) {
						for (PlayerTeam team : scoreboard.getPlayerTeams()) {
							if (teamDamage) {
								if (team.isAllowFriendlyFire() != true) {
									team.setAllowFriendlyFire(true);
								}
							} else {
								if (team.isAllowFriendlyFire() != false) {
									team.setAllowFriendlyFire(false);
								}
							}

							if (teamCollision) {
								if (team.getCollisionRule() != CollisionRule.ALWAYS) {
									team.setCollisionRule(CollisionRule.ALWAYS);
								}
							} else {
								if (team.getCollisionRule() != CollisionRule.PUSH_OTHER_TEAMS) {
									team.setCollisionRule(CollisionRule.PUSH_OTHER_TEAMS);
								}
							}
						}

						if (wInfo.getDifficulty() != Difficulty.byId(difficulty))
							minecraftserver.setDifficulty(Difficulty.byId(difficulty), true);

						saveData.setRandomTeamSize(randomTeams);
						saveData.setTeamsLocked(teamsLocked);
						saveData.setMaxTeamSize(maxTeams);
						saveData.setTeamCollision(teamCollision);
						saveData.setFriendlyFire(teamDamage);
						saveData.setDifficulty(difficulty);
						saveData.setDirty();

						UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(serverPlayer.getUUID(), saveData));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
