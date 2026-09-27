package com.mrbysco.uhc.network.payload;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.registry.UHCDataAttachments;
import com.mrbysco.uhc.util.ExtraStreamCodecs;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.LevelData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UHCPage5Payload(boolean weatherCycle, boolean mobGriefing, boolean customHealth, int maxHealth,
                              boolean randomSpawns,
                              int spreadDistance, int spreadMaxRange,
                              boolean spreadRespectTeam) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, UHCPage5Payload> STREAM_CODEC = ExtraStreamCodecs.composite(
			ByteBufCodecs.BOOL,
			UHCPage5Payload::weatherCycle,
			ByteBufCodecs.BOOL,
			UHCPage5Payload::mobGriefing,
			ByteBufCodecs.BOOL,
			UHCPage5Payload::customHealth,
			ByteBufCodecs.INT,
			UHCPage5Payload::maxHealth,
			ByteBufCodecs.BOOL,
			UHCPage5Payload::randomSpawns,
			ByteBufCodecs.INT,
			UHCPage5Payload::spreadDistance,
			ByteBufCodecs.INT,
			UHCPage5Payload::spreadMaxRange,
			ByteBufCodecs.BOOL,
			UHCPage5Payload::spreadRespectTeam,
			UHCPage5Payload::new
	);

	public static final CustomPacketPayload.Type<UHCPage5Payload> ID = new CustomPacketPayload.Type<>(Reference.modLoc("sync_page_5"));

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(final UHCPage5Payload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					UHCSaveData saveData = UHCSaveData.get(serverPlayer.level());

					if (serverPlayer.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
						MinecraftServer minecraftserver = serverPlayer.getServer();
						LevelData wInfo = serverPlayer.level().getLevelData();
						GameRules rules = minecraftserver.getGameRules();

						if (payload.mobGriefing()) {
							if (!rules.getBoolean(GameRules.RULE_MOBGRIEFING))
								rules.getRule(GameRules.RULE_MOBGRIEFING).set(true, minecraftserver);
						} else {
							if (rules.getBoolean(GameRules.RULE_MOBGRIEFING))
								rules.getRule(GameRules.RULE_MOBGRIEFING).set(false, minecraftserver);
						}

						if (payload.weatherCycle()) {
							if (!rules.getBoolean(GameRules.RULE_WEATHER_CYCLE))
								rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(true, minecraftserver);
						} else {
							if (serverPlayer.level().isRaining())
								wInfo.setRaining(false);
							if (rules.getBoolean(GameRules.RULE_WEATHER_CYCLE))
								rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, minecraftserver);
						}

						saveData.setWeatherEnabled(payload.weatherCycle());
						saveData.setMobGriefing(payload.mobGriefing());
						saveData.setApplyCustomHealth(payload.customHealth());
						saveData.setMaxHealth(payload.maxHealth());

						saveData.setRandomSpawns(payload.randomSpawns());
						saveData.setSpreadDistance(payload.spreadDistance());
						saveData.setSpreadMaxRange(payload.spreadMaxRange());
						saveData.setSpreadRespectTeam(payload.spreadRespectTeam());
						saveData.setDirty();

						net.neoforged.neoforge.network.PacketDistributor.sendToAllPlayers(new UHCSyncPayload(saveData.save(new CompoundTag(), serverPlayer.registryAccess())));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}
			}).exceptionally(e -> {
				// Handle exception
				context.disconnect(Component.translatable("ultrahardcoremod.networking.page_5.failed", e.getMessage()));
				return null;
			});
		}
	}
}
