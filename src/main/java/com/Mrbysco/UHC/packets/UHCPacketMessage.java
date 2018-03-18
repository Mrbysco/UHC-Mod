package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.gui.GuiUHCBook;
import com.Mrbysco.UHC.init.UHCSaveData;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UHCPacketMessage implements IMessage
{
    private UHCSaveData data;
	
	public UHCPacketMessage() {}
	
	public UHCPacketMessage(UHCSaveData uhcData)
	{
		data = uhcData;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (this.data == null)
            this.data = new UHCSaveData();
		
		this.data.readFromNBT(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound tag = new NBTTagCompound();
        ByteBufUtils.writeTag(buf, this.data.writeToNBT(tag));
	}
	
	public static class PacketHandler implements IMessageHandler<UHCPacketMessage, IMessage>
	{
		@Override
		public IMessage onMessage(UHCPacketMessage message, MessageContext ctx) {
			
	        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		    return null;
		}
		
		private void handle(UHCPacketMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT && message != null)
            {
				GuiUHCBook.saveData = message.data;
            }
		}
	}
}