package com.Mrbysco.UHC.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UHCPage3Packet implements IMessage
{
	public boolean timeLock;
	public int timeLockUntil;
	public String timeLockMode;
	public boolean minuteMark;
	public int minuteEvery;
	public boolean timedNames;
	public int timedNamesAfter;
	public boolean timedGlow;
	public int timedGlowAfter;
	
	public UHCPage3Packet() {}
	
	public UHCPage3Packet(boolean timeLock , int timeLockUntil, String timeLockMode, boolean minuteMark, int minuteEvery, 
			boolean timedNames, int timedNamesAfter, boolean timedGlow, int timedGlowAfter) {
		this.timeLock = timeLock;
		this.timeLockUntil = timeLockUntil;
		this.timeLockMode = timeLockMode;
		this.minuteMark = minuteMark;
		this.minuteEvery = minuteEvery;
		this.timedNames = timedNames;
		this.timedNamesAfter = timedNamesAfter;
		this.timedGlow = timedGlow;
		this.timedGlowAfter = timedGlowAfter;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		timeLock = buf.readBoolean();
		timeLockUntil = buf.readInt();
		timeLockMode = ByteBufUtils.readUTF8String(buf);
		minuteMark = buf.readBoolean();
		minuteEvery = buf.readInt();
		timedNames = buf.readBoolean();
		timedNamesAfter = buf.readInt();
		timedGlow = buf.readBoolean();
		timedGlowAfter = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(timeLock);
		buf.writeInt(timeLockUntil);
		ByteBufUtils.writeUTF8String(buf, timeLockMode);
		buf.writeBoolean(minuteMark);
		buf.writeInt(minuteEvery);
		buf.writeBoolean(timedNames);
		buf.writeInt(timedNamesAfter);
		buf.writeBoolean(timedGlow);
		buf.writeInt(timedGlowAfter);
	}
}
