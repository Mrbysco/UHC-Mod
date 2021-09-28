package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.UUID;
import java.util.function.Supplier;

public class UHCPacketMessage {
    private UHCSaveData data;

	private UHCPacketMessage(PacketBuffer buf) {
		this.data = new UHCSaveData();
		this.data.read(buf.readCompoundTag());
	}

	public UHCPacketMessage(UHCSaveData data) {
		this.data = data;
	}

	public UHCPacketMessage(UUID playerUUID, UHCSaveData tag) {
		this.data = tag;
	}

	public void encode(PacketBuffer buf) {
		buf.writeCompoundTag(data.write(new CompoundNBT()));
	}

	public static UHCPacketMessage decode(final PacketBuffer packetBuffer) {
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