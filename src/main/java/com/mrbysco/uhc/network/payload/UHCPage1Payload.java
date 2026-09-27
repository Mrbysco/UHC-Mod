package com.mrbysco.uhc.network.payload;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.registry.UHCDataAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team.CollisionRule;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UHCPage1Payload(int randomTeam, int maxTeam, boolean collision, boolean teamDamage, int difficulty,
                              boolean teamsLocked) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, UHCPage1Payload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			UHCPage1Payload::randomTeam,
			ByteBufCodecs.INT,
			UHCPage1Payload::maxTeam,
			ByteBufCodecs.BOOL,
			UHCPage1Payload::collision,
			ByteBufCodecs.BOOL,
			UHCPage1Payload::teamDamage,
			ByteBufCodecs.INT,
			UHCPage1Payload::difficulty,
			ByteBufCodecs.BOOL,
			UHCPage1Payload::teamsLocked,
			UHCPage1Payload::new
	);
	public static final Type<UHCPage1Payload> ID = new Type<>(Reference.modLoc("sync_page_1"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(final UHCPage1Payload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					UHCSaveData saveData = UHCSaveData.get(serverPlayer.level());
					MinecraftServer minecraftserver = serverPlayer.getServer();
					LevelData wInfo = serverPlayer.level().getLevelData();
					Scoreboard scoreboard = serverPlayer.level().getScoreboard();

					if (serverPlayer.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
						for (PlayerTeam team : scoreboard.getPlayerTeams()) {
							if (payload.teamDamage()) {
								if (!team.isAllowFriendlyFire()) {
									team.setAllowFriendlyFire(true);
								}
							} else {
								if (team.isAllowFriendlyFire()) {
									team.setAllowFriendlyFire(false);
								}
							}

							if (payload.collision()) {
								if (team.getCollisionRule() != CollisionRule.ALWAYS) {
									team.setCollisionRule(CollisionRule.ALWAYS);
								}
							} else {
								if (team.getCollisionRule() != CollisionRule.PUSH_OTHER_TEAMS) {
									team.setCollisionRule(CollisionRule.PUSH_OTHER_TEAMS);
								}
							}
						}

						if (wInfo.getDifficulty() != Difficulty.byId(payload.difficulty()))
							minecraftserver.setDifficulty(Difficulty.byId(payload.difficulty()), true);

						saveData.setRandomTeamSize(payload.randomTeam());
						saveData.setTeamsLocked(payload.teamsLocked());
						saveData.setMaxTeamSize(payload.maxTeam());
						saveData.setTeamCollision(payload.collision());
						saveData.setFriendlyFire(payload.teamDamage());
						saveData.setDifficulty(payload.difficulty());
						saveData.setDirty();

						PacketDistributor.sendToAllPlayers(new UHCSyncPayload(saveData.save(new CompoundTag(), serverPlayer.registryAccess())));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}
			}).exceptionally(e -> {
				// Handle exception
				context.disconnect(Component.translatable("ultrahardcoremod.networking.page_1.failed", e.getMessage()));
				return null;
			});
		}
	}
}
