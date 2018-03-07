package com.Mrbysco.UHC.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UHCPage1Packet implements IMessage
{
	public int randomTeams;
	public int maxTeams;
	public boolean teamCollision;
	public boolean teamDamage;
	public int difficulty;
	
	public UHCPage1Packet() {}
	
	public UHCPage1Packet(int randomTeam, int maxTeam, boolean collision, boolean teamDamage, int difficulty) {
		this.randomTeams = randomTeam;
		this.maxTeams = maxTeam;
		this.teamCollision = collision;
		this.teamDamage = teamDamage;
		this.difficulty = difficulty;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		randomTeams = buf.readInt();
		maxTeams = buf.readInt();
		teamCollision = buf.readBoolean();
		teamDamage = buf.readBoolean();
		difficulty = buf.readInt();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(randomTeams);
		buf.writeInt(maxTeams);
		buf.writeBoolean(teamCollision);
		buf.writeBoolean(teamDamage);
		buf.writeInt(difficulty);
	}
}
