package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.UUID;
import java.util.function.Supplier;

public class UHCPacketMessage {
	private final CompoundTag dataTag;

	private UHCPacketMessage(FriendlyByteBuf buf) {
		dataTag = buf.readNbt();
	}

	public UHCPacketMessage(CompoundTag data) {
		this.dataTag = data;
	}

	public UHCPacketMessage(UUID playerUUID, CompoundTag tag) {
		this.dataTag = tag;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeNbt(dataTag);
	}

	public static UHCPacketMessage decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPacketMessage(packetBuffer);
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			UHCSaveData data = UHCSaveData.load(dataTag);
			if (ctx.getDirection().getReceptionSide().isClient()) {
				com.mrbysco.uhc.client.ClientHelper.updateBook(data);
			}
		});
		ctx.setPacketHandled(true);
	}
}