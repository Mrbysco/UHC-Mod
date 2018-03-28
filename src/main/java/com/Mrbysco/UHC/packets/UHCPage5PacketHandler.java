package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage5PacketHandler implements IMessageHandler<UHCPage5Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage5Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		UHCSaveData saveData = UHCSaveData.getForWorld(serverPlayer.getServerWorld());
		NBTTagCompound playerData = serverPlayer.getEntityData();
		
		if(playerData.getBoolean("canEditUHC") == true)
		{
			saveData.setWeatherEnabled(message.weatherCycle);
			saveData.setMobGriefing(message.mobGriefing);
			saveData.setApplyCustomHealth(message.customHealth);
			saveData.setMaxHealth(message.maxHealth);
			
			saveData.setRandomSpawns(message.randomSpawns);
			saveData.setSpreadDistance(message.spreadDistance);
			saveData.setSpreadMaxRange(message.spreadMaxRange);
			saveData.setSpreadRespectTeam(message.spreadRespectTeam);
			saveData.markDirty();
			
			ModPackethandler.INSTANCE.sendToAll(new UHCPacketMessage(saveData));
		}
		else
		{
			serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have permissions to edit the UHC book."));
		}
		
		return null;
	}
}