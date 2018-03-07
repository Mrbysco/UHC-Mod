package com.Mrbysco.UHC.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UHCPage4Packet implements IMessage
{
	public boolean regenPotions;
	public boolean level2Potions;
	public boolean notchApples;
	public boolean autoCook;
	public boolean itemConversion;
	public boolean netherTravel;
	public boolean healthTab;
	public boolean healthSide;
	public boolean healthName;
	
	public UHCPage4Packet() {}
	
	public UHCPage4Packet(boolean regenPotions , boolean level2Potions, boolean notchApples, boolean autoCook, boolean itemConversion, 
			boolean netherTravel, boolean healthTab, boolean healthSide, boolean healthName) {
		this.regenPotions = regenPotions;
		this.level2Potions = level2Potions;
		this.notchApples = notchApples;
		this.autoCook = autoCook;
		this.itemConversion = itemConversion;
		this.netherTravel = netherTravel;
		this.healthTab = healthTab;
		this.healthSide = healthSide;
		this.healthName = healthName;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		regenPotions = buf.readBoolean();
		level2Potions = buf.readBoolean();
		notchApples = buf.readBoolean();
		autoCook = buf.readBoolean();
		itemConversion = buf.readBoolean();
		netherTravel = buf.readBoolean();
		healthTab = buf.readBoolean();
		healthSide = buf.readBoolean();
		healthName = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(regenPotions);
		buf.writeBoolean(level2Potions);
		buf.writeBoolean(notchApples);
		buf.writeBoolean(autoCook);
		buf.writeBoolean(itemConversion);
		buf.writeBoolean(netherTravel);
		buf.writeBoolean(healthTab);
		buf.writeBoolean(healthSide);
		buf.writeBoolean(healthName);
	}
}
