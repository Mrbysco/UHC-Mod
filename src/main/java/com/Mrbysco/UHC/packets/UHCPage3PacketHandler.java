package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage3PacketHandler implements IMessageHandler<UHCPage3Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage3Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
			NBTTagCompound playerData = serverPlayer.getEntityData();
			
			if(playerData.getBoolean("canEditUHC") == true)
			{
				saveData.setTimeLock(message.timeLock);
				saveData.setTimeLockTimer(message.timeLockUntil);
				saveData.setTimeMode(message.timeLockMode);
				saveData.setMinuteMark(message.minuteMark);
				saveData.setMinuteMarkTime(message.minuteEvery);
				saveData.setTimedNames(message.timedNames);
				saveData.setNameTimer(message.timedNamesAfter);
				saveData.setTimedGlow(message.timedGlow);
				saveData.setGlowTime(message.timedGlowAfter);
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