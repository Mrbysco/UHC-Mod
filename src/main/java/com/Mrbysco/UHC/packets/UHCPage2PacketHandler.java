package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage2PacketHandler implements IMessageHandler<UHCPage2Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage2Packet message, MessageContext ctx) {
		
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		return null;
	}
	
	private void handle(UHCPage2Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		World world = serverPlayer.getServerWorld();
		UHCSaveData saveData = UHCSaveData.getForWorld(world);
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
			saveData.markDirty();
		}
	}
}
