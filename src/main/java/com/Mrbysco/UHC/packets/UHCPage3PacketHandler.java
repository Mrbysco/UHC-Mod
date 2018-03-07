package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage3PacketHandler implements IMessageHandler<UHCPage3Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage3Packet message, MessageContext ctx) {
		
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		return null;
	}
	
	private void handle(UHCPage3Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		World world = serverPlayer.getServerWorld();
		UHCSaveData saveData = UHCSaveData.getForWorld(world);
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
		}
	}
}