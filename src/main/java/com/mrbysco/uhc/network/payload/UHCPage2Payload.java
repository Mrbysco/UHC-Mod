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

public record UHCPage2Payload(int borderSize, double centerX, double centerZ, boolean borderShrink, int timeUntil,
                              int size, int over, String shrinkMode) implements CustomPacketPayload {

	public static final StreamCodec<FriendlyByteBuf, UHCPage2Payload> STREAM_CODEC = ExtraStreamCodecs.composite(
			ByteBufCodecs.INT,
			UHCPage2Payload::borderSize,
			ByteBufCodecs.DOUBLE,
			UHCPage2Payload::centerX,
			ByteBufCodecs.DOUBLE,
			UHCPage2Payload::centerZ,
			ByteBufCodecs.BOOL,
			UHCPage2Payload::borderShrink,
			ByteBufCodecs.INT,
			UHCPage2Payload::timeUntil,
			ByteBufCodecs.INT,
			UHCPage2Payload::size,
			ByteBufCodecs.INT,
			UHCPage2Payload::over,
			ByteBufCodecs.STRING_UTF8,
			UHCPage2Payload::shrinkMode,
			UHCPage2Payload::new
	);

	public static final Type<UHCPage2Payload> ID = new Type<>(Reference.modLoc("sync_page_2"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(final UHCPage2Payload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					UHCSaveData saveData = UHCSaveData.get(serverPlayer.level());

					if (serverPlayer.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
						saveData.setBorderSize(payload.borderSize());
						saveData.setBorderCenterX(payload.centerX());
						saveData.setBorderCenterZ(payload.centerZ());
						saveData.setShrinkEnabled(payload.borderShrink());
						saveData.setShrinkTimer(payload.timeUntil());
						saveData.setShrinkSize(payload.size());
						saveData.setShrinkOvertime(payload.over());
						saveData.setShrinkMode(payload.shrinkMode());
						saveData.setDirty();

						PacketDistributor.sendToAllPlayers(new UHCSyncPayload(saveData.save(new CompoundTag(), serverPlayer.registryAccess())));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}
			}).exceptionally(e -> {
				// Handle exception
				context.disconnect(Component.translatable("ultrahardcoremod.networking.page_2.failed", e.getMessage()));
				return null;
			});
		}
	}
}
