package com.mrbysco.uhc.network.payload;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UHCSyncPayload(CompoundTag data) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, UHCSyncPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG,
			UHCSyncPayload::data,
			UHCSyncPayload::new);
	public static final Type<UHCSyncPayload> ID = new Type<>(Reference.modLoc("sync_data"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(final UHCSyncPayload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
						UHCSaveData data = UHCSaveData.load(payload.data, context.player().registryAccess());
						com.mrbysco.uhc.client.ClientHelper.updateBook(data);
					})
					.exceptionally(e -> {
						// Handle exception
						context.disconnect(Component.translatable("statues.networking.sync_data.failed", e.getMessage()));
						return null;
					});
		}
	}
}