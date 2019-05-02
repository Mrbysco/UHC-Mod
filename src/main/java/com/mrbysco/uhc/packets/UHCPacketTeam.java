package com.mrbysco.uhc.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UHCPacketTeam implements IMessage
{
	public String playerName;
	public String team;
	public String teamName;
	public int colorIndex;
	
	public UHCPacketTeam() {}
	
	public UHCPacketTeam(String name, String team, String teamName, int colorIndex) {
		this.playerName = name;
		this.team = team;
		this.teamName = teamName;
		this.colorIndex = colorIndex;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		playerName = ByteBufUtils.readUTF8String(buf);
		team = ByteBufUtils.readUTF8String(buf);
		teamName = ByteBufUtils.readUTF8String(buf);
		colorIndex = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, playerName);
		ByteBufUtils.writeUTF8String(buf, team);
		ByteBufUtils.writeUTF8String(buf, teamName);
		buf.writeInt(colorIndex);
		
	}
}
