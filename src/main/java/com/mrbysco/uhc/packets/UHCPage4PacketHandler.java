package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.init.UHCSaveData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage4PacketHandler implements IMessageHandler<UHCPage4Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage4Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
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
				
				ModPackethandler.INSTANCE.sendToAll(new UHCPacketMessage(saveData));
			}
			else
			{
				serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have permissions to edit the UHC book."));
			}
		}
		return null;
	}
}