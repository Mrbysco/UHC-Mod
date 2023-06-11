package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class UHCPage4Packet {
	public final boolean regenPotions;
	public final boolean level2Potions;
	public final boolean notchApples;
	public final boolean autoCook;
	public final boolean itemConversion;
	public final boolean netherTravel;
	public final boolean healthTab;
	public final boolean healthSide;
	public final boolean healthName;

	public UHCPage4Packet(boolean regenPotions, boolean level2Potions, boolean notchApples, boolean autoCook, boolean itemConversion,
						  boolean netherTravel, boolean healthTab, boolean healthSide, boolean healthName) {
		this.regenPotions = regenPotions;
		this.level2Potions = level2Potions;
		this.notchApples = notchApples;
		this.autoCook = autoCook;
		this.itemConversion = itemConversion;
		this.netherTravel = netherTravel;
		this.healthTab = healthTab;
		this.healthSide = healthSide;
		this.healthName = healthName;
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(regenPotions);
		buf.writeBoolean(level2Potions);
		buf.writeBoolean(notchApples);
		buf.writeBoolean(autoCook);
		buf.writeBoolean(itemConversion);
		buf.writeBoolean(netherTravel);
		buf.writeBoolean(healthTab);
		buf.writeBoolean(healthSide);
		buf.writeBoolean(healthName);
	}

	public static UHCPage4Packet decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPage4Packet(packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readBoolean(),
				packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readBoolean(),
				packetBuffer.readBoolean(), packetBuffer.readBoolean());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer serverPlayer = ctx.getSender();
				ServerLevel overworld = serverPlayer.getServer().overworld();
				if (overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundTag playerData = serverPlayer.getPersistentData();

					if (playerData.getBoolean("canEditUHC")) {
						saveData.setRegenPotions(regenPotions);
						saveData.setLevel2Potions(level2Potions);
						saveData.setNotchApples(notchApples);
						saveData.setAutoCook(autoCook);
						saveData.setItemConversion(itemConversion);
						saveData.setNetherEnabled(netherTravel);
						saveData.setHealthInTab(healthTab);
						saveData.setHealthOnSide(healthSide);
						saveData.setHealthUnderName(healthName);
						saveData.setDirty();

						UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(saveData.save(new CompoundTag())));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
