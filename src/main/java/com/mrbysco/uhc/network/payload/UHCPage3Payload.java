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
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UHCPage3Payload(boolean timeLock, int timeLockUntil, String timeLockMode, boolean minuteMark, int minuteEvery,
                             boolean timedNames, int timedNamesAfter, boolean timedGlow, int timedGlowAfter) implements CustomPacketPayload {

	public static final StreamCodec<FriendlyByteBuf, UHCPage3Payload> STREAM_CODEC = ExtraStreamCodecs.composite(
			ByteBufCodecs.BOOL,
			UHCPage3Payload::timeLock,
			ByteBufCodecs.INT,
			UHCPage3Payload::timeLockUntil,
			ByteBufCodecs.STRING_UTF8,
			UHCPage3Payload::timeLockMode,
			ByteBufCodecs.BOOL,
			UHCPage3Payload::minuteMark,
			ByteBufCodecs.INT,
			UHCPage3Payload::minuteEvery,
			ByteBufCodecs.BOOL,
			UHCPage3Payload::timedNames,
			ByteBufCodecs.INT,
			UHCPage3Payload::timedNamesAfter,
			ByteBufCodecs.BOOL,
			UHCPage3Payload::timedGlow,
			ByteBufCodecs.INT,
			UHCPage3Payload::timedGlowAfter,
			UHCPage3Payload::new
	);

	public static final Type<UHCPage3Payload> ID = new Type<>(Reference.modLoc("sync_page_3"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(final UHCPage3Payload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					UHCSaveData saveData = UHCSaveData.get(serverPlayer.level());

					if (serverPlayer.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
						saveData.setTimeLock(payload.timeLock());
						saveData.setTimeLockTimer(payload.timeLockUntil());
						saveData.setTimeMode(payload.timeLockMode());
						saveData.setMinuteMark(payload.minuteMark());
						saveData.setMinuteMarkTime(payload.minuteEvery());
						saveData.setTimedNames(payload.timedNames());
						saveData.setNameTimer(payload.timedNamesAfter());
						saveData.setTimedGlow(payload.timedGlow());
						saveData.setGlowTime(payload.timedGlowAfter());
						saveData.setDirty();

						PacketDistributor.sendToAllPlayers(new UHCSyncPayload(saveData.save(new CompoundTag(), serverPlayer.registryAccess())));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}
			}).exceptionally(e -> {
				// Handle exception
				context.disconnect(Component.translatable("ultrahardcoremod.networking.page_3.failed", e.getMessage()));
				return null;
			});
		}
	}
}
