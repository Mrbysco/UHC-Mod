package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage4PacketHandler implements IMessageHandler<UHCPage4Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage4Packet message, MessageContext ctx) {
		
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		return null;
	}
	
	private void handle(UHCPage4Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		World world = serverPlayer.getServerWorld();
		UHCSaveData saveData = UHCSaveData.getForWorld(world);
		NBTTagCompound playerData = serverPlayer.getEntityData();
		
		if(playerData.getBoolean("canEditUHC") == true)
		{
			saveData.setRegenPotions(message.regenPotions);
			saveData.setLevel2Potions(message.level2Potions);
			saveData.setNotchApples(message.notchApples);
			saveData.setAutoCook(message.autoCook);
			saveData.setItemConversion(message.itemConversion);
			saveData.setNetherEnabled(message.netherTravel);
			saveData.setHealthInTab(message.healthTab);
			saveData.setHealthOnSide(message.healthSide);
			saveData.setHealthUnderName(message.healthName);
			saveData.markDirty();
		}
	}
}