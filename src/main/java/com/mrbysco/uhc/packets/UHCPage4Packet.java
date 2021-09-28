package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class UHCPage4Packet {
	public boolean regenPotions;
	public boolean level2Potions;
	public boolean notchApples;
	public boolean autoCook;
	public boolean itemConversion;
	public boolean netherTravel;
	public boolean healthTab;
	public boolean healthSide;
	public boolean healthName;
	
	public UHCPage4Packet(boolean regenPotions , boolean level2Potions, boolean notchApples, boolean autoCook, boolean itemConversion, 
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

	public void encode(PacketBuffer buf) {
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

	public static UHCPage4Packet decode(final PacketBuffer packetBuffer) {
		return new UHCPage4Packet(packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readBoolean(),
				packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readBoolean(),
				packetBuffer.readBoolean(), packetBuffer.readBoolean());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayerEntity serverPlayer = ctx.getSender();
				ServerWorld overworld = serverPlayer.getServer().getWorld(World.OVERWORLD);
				if(overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundNBT playerData = serverPlayer.getPersistentData();

					if(playerData.getBoolean("canEditUHC")) {
						saveData.setRegenPotions(regenPotions);
						saveData.setLevel2Potions(level2Potions);
						saveData.setNotchApples(notchApples);
						saveData.setAutoCook(autoCook);
						saveData.setItemConversion(itemConversion);
						saveData.setNetherEnabled(netherTravel);
						saveData.setHealthInTab(healthTab);
						saveData.setHealthOnSide(healthSide);
						saveData.setHealthUnderName(healthName);
						saveData.markDirty();

						UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(serverPlayer.getUniqueID(), saveData));
					} else {
						serverPlayer.sendMessage(new StringTextComponent("You don't have permissions to edit the UHC book").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
