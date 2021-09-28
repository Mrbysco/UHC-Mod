package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team.CollisionRule;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor;

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

	public void encode(PacketBuffer buf) {
		buf.writeInt(randomTeams);
		buf.writeInt(maxTeams);
		buf.writeBoolean(teamCollision);
		buf.writeBoolean(teamDamage);
		buf.writeInt(difficulty);
		buf.writeBoolean(teamsLocked);
	}

	public static UHCPage1Packet decode(final PacketBuffer packetBuffer) {
		return new UHCPage1Packet(packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readBoolean(),
				packetBuffer.readBoolean(), packetBuffer.readInt(), packetBuffer.readBoolean());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayerEntity serverPlayer = ctx.getSender();
				ServerWorld serverWorld = serverPlayer.getServerWorld();
				ServerWorld overworld = serverPlayer.getServer().getWorld(World.OVERWORLD);
				if(overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundNBT playerData = serverPlayer.getPersistentData();
					MinecraftServer minecraftserver = serverWorld.getServer();
					IWorldInfo wInfo = serverWorld.getWorldInfo();
					Scoreboard scoreboard = serverWorld.getScoreboard();

					if(playerData.getBoolean("canEditUHC")) {
						for (ScorePlayerTeam team : scoreboard.getTeams()) {
							if(teamDamage) {
								if (team.getAllowFriendlyFire() != true) {
									team.setAllowFriendlyFire(true);
								}
							} else {
								if (team.getAllowFriendlyFire() != false)
								{
									team.setAllowFriendlyFire(false);
								}
							}

							if(teamCollision) {
								if (team.getCollisionRule() != CollisionRule.ALWAYS) {
									team.setCollisionRule(CollisionRule.ALWAYS);
								}
							} else {
								if (team.getCollisionRule() != CollisionRule.PUSH_OTHER_TEAMS) {
									team.setCollisionRule(CollisionRule.PUSH_OTHER_TEAMS);
								}
							}
						}

						if(wInfo.getDifficulty() != Difficulty.byId(difficulty))
							minecraftserver.setDifficultyForAllWorlds(Difficulty.byId(difficulty), true);

						saveData.setRandomTeamSize(randomTeams);
						saveData.setTeamsLocked(teamsLocked);
						saveData.setMaxTeamSize(maxTeams);
						saveData.setTeamCollision(teamCollision);
						saveData.setFriendlyFire(teamDamage);
						saveData.setDifficulty(difficulty);
						saveData.markDirty();

						UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(serverPlayer.getUniqueID(), saveData));
					} else {
						serverPlayer.sendMessage(new StringTextComponent("You don't have permissions to edit the UHC book").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
