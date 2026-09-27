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
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UHCPage6Payload(boolean graceEnabled, int graceTime) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, UHCPage6Payload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			UHCPage6Payload::graceEnabled,
			ByteBufCodecs.INT,
			UHCPage6Payload::graceTime,
			UHCPage6Payload::new
	);

	public static final CustomPacketPayload.Type<UHCPage6Payload> ID = new CustomPacketPayload.Type<>(Reference.modLoc("sync_page_6"));

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(final UHCPage6Payload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					UHCSaveData saveData = UHCSaveData.get(serverPlayer.level());

					if (serverPlayer.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
						saveData.setGraceEnabled(payload.graceEnabled());
						saveData.setGraceTime(payload.graceTime());
						saveData.setDirty();

						net.neoforged.neoforge.network.PacketDistributor.sendToAllPlayers(new UHCSyncPayload(saveData.save(new CompoundTag(), serverPlayer.registryAccess())));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}
			}).exceptionally(e -> {
				// Handle exception
				context.disconnect(Component.translatable("ultrahardcoremod.networking.page_6.failed", e.getMessage()));
				return null;
			});
		}
	}
}
