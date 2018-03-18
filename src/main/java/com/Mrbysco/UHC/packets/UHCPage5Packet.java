package com.Mrbysco.UHC.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UHCPage5Packet implements IMessage
{
	public boolean weatherCycle;
	public boolean mobGriefing;
	public boolean customHealth;
	public int maxHealth;
	
	public boolean randomSpawns;
	public int spreadDistance;
	public int spreadMaxRange;
	public boolean spreadRespectTeam;
	
	public UHCPage5Packet() {}
	
	public UHCPage5Packet(boolean weatherCycle, boolean mobGriefing, boolean customHealth, int maxHealth, boolean randomSpawns, 
			int spreadDistance, int spreadMaxRange, boolean spreadRespectTeam) {
		this.weatherCycle = weatherCycle;
		this.mobGriefing = mobGriefing;
		this.customHealth = customHealth;
		this.maxHealth = maxHealth;
		
		this.randomSpawns = randomSpawns;
		this.spreadDistance = spreadDistance;
		this.spreadMaxRange = spreadMaxRange;
		this.spreadRespectTeam = spreadRespectTeam;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		weatherCycle = buf.readBoolean();
		mobGriefing = buf.readBoolean();
		customHealth = buf.readBoolean();
		maxHealth = buf.readInt();
		
		randomSpawns = buf.readBoolean();
		spreadDistance = buf.readInt();
		spreadMaxRange = buf.readInt();
		spreadRespectTeam = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(weatherCycle);
		buf.writeBoolean(mobGriefing);
		buf.writeBoolean(customHealth);
		buf.writeInt(maxHealth);
		
		buf.writeBoolean(randomSpawns);
		buf.writeInt(spreadDistance);
		buf.writeInt(spreadMaxRange);
		buf.writeBoolean(spreadRespectTeam);

	}
}
