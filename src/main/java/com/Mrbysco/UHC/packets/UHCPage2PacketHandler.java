package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage2PacketHandler implements IMessageHandler<UHCPage2Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage2Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		UHCSaveData saveData = UHCSaveData.getForWorld(serverPlayer.getServerWorld());
		NBTTagCompound playerData = serverPlayer.getEntityData();
		
		if(playerData.getBoolean("canEditUHC") == true)
		{
			saveData.setBorderSize(message.borderSize);
			saveData.setBorderCenterX(message.centerX);
			saveData.setBorderCenterZ(message.centerZ);
			saveData.setShrinkEnabled(message.borderShrink);
			saveData.setShrinkTimer(message.timeUntil);
			saveData.setShrinkSize(message.size);
			saveData.setShrinkOvertime(message.over);
			saveData.setShrinkMode(message.Shrinkmode);
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
