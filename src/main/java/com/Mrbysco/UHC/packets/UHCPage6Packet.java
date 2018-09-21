package com.Mrbysco.UHC.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UHCPage6Packet implements IMessage
{
	public boolean graceEnabled;
	public int graceTime;
	
	public UHCPage6Packet() {}
	
	public UHCPage6Packet(boolean graceEnabled, int graceTime) {
		this.graceEnabled = graceEnabled;
		this.graceTime = graceTime;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		graceEnabled = buf.readBoolean();
		graceTime = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(graceEnabled);
		buf.writeInt(graceTime);
	}
}
