package com.Mrbysco.UHC.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UHCPage2Packet implements IMessage
{
	public int borderSize;
	public double centerX;
	public double centerZ;
	public boolean borderShrink;
	public int timeUntil;
	public int size;
	public int over;
	public String Shrinkmode;
	
	public UHCPage2Packet() {}
	
	public UHCPage2Packet(int borderSize, double centerX, double centerZ, boolean borderShrink, int timeUntil, int size, int over, String mode) {
		this.borderSize = borderSize;
		this.centerX = centerX;
		this.centerZ = centerZ;
		this.borderShrink = borderShrink;
		this.timeUntil = timeUntil;
		this.size = size;
		this.over = over;
		this.Shrinkmode = mode;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		borderSize = buf.readInt();
		centerX = buf.readDouble();
		centerZ = buf.readDouble();
		borderShrink = buf.readBoolean();
		timeUntil = buf.readInt();
		size = buf.readInt();
		over = buf.readInt();
		Shrinkmode = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(borderSize);
		buf.writeDouble(centerX);
		buf.writeDouble(centerZ);
		buf.writeBoolean(borderShrink);
		buf.writeInt(timeUntil);
		buf.writeInt(size);
		buf.writeInt(over);
		ByteBufUtils.writeUTF8String(buf, Shrinkmode);
	}
}
