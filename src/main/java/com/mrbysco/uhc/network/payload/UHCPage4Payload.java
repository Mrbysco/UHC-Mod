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
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UHCPage4Payload(boolean regenPotions, boolean level2Potions, boolean notchApples, boolean autoCook,
                              boolean itemConversion,
                              boolean netherTravel, boolean healthTab, boolean healthSide,
                              boolean healthName) implements CustomPacketPayload {


	public static final StreamCodec<FriendlyByteBuf, UHCPage4Payload> STREAM_CODEC = ExtraStreamCodecs.composite(
			ByteBufCodecs.BOOL,
			UHCPage4Payload::regenPotions,
			ByteBufCodecs.BOOL,
			UHCPage4Payload::level2Potions,
			ByteBufCodecs.BOOL,
			UHCPage4Payload::notchApples,
			ByteBufCodecs.BOOL,
			UHCPage4Payload::autoCook,
			ByteBufCodecs.BOOL,
			UHCPage4Payload::itemConversion,
			ByteBufCodecs.BOOL,
			UHCPage4Payload::netherTravel,
			ByteBufCodecs.BOOL,
			UHCPage4Payload::healthTab,
			ByteBufCodecs.BOOL,
			UHCPage4Payload::healthSide,
			ByteBufCodecs.BOOL,
			UHCPage4Payload::healthName,
			UHCPage4Payload::new
	);

	public static final CustomPacketPayload.Type<UHCPage4Payload> ID = new CustomPacketPayload.Type<>(Reference.modLoc("sync_page_4"));

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public static class Handler {
		public static void handle(final UHCPage4Payload payload, final IPayloadContext context) {
			context.enqueueWork(() -> {
				if (context.player() instanceof ServerPlayer serverPlayer) {
					UHCSaveData saveData = UHCSaveData.get(serverPlayer.level());

					if (serverPlayer.hasData(UHCDataAttachments.CAN_EDIT_UHC)) {
						saveData.setRegenPotions(payload.regenPotions());
						saveData.setLevel2Potions(payload.level2Potions());
						saveData.setNotchApples(payload.notchApples());
						saveData.setAutoCook(payload.autoCook());
						saveData.setItemConversion(payload.itemConversion());
						saveData.setNetherEnabled(payload.netherTravel());
						saveData.setHealthInTab(payload.healthTab());
						saveData.setHealthOnSide(payload.healthSide());
						saveData.setHealthUnderName(payload.healthName());
						saveData.setDirty();

						net.neoforged.neoforge.network.PacketDistributor.sendToAllPlayers(new UHCSyncPayload(saveData.save(new CompoundTag(), serverPlayer.registryAccess())));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}
			}).exceptionally(e -> {
				// Handle exception
				context.disconnect(Component.translatable("ultrahardcoremod.networking.page_4.failed", e.getMessage()));
				return null;
			});
		}
	}
}
