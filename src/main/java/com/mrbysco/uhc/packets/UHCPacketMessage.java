package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.UUID;
import java.util.function.Supplier;

public class UHCPacketMessage {
	private UHCSaveData data;

	private UHCPacketMessage(FriendlyByteBuf buf) {
		this.data = new UHCSaveData();
		this.data.load(buf.readNbt());
	}

	public UHCPacketMessage(UHCSaveData data) {
		this.data = data;
	}

	public UHCPacketMessage(UUID playerUUID, UHCSaveData tag) {
		this.data = tag;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeNbt(data.save(new CompoundTag()));
	}

	public static UHCPacketMessage decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPacketMessage(packetBuffer);
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient() && ctx.getSender() != null) {
				com.mrbysco.uhc.client.ClientHelper.updateBook(data);
			}
		});
		ctx.setPacketHandled(true);
	}
}