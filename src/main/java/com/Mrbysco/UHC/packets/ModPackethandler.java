package com.Mrbysco.UHC.packets;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModPackethandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("uhc");
	
	public static void registerMessages() {
		INSTANCE.registerMessage(UHCPacketMessage.PacketHandler.class, UHCPacketMessage.class, 0, Side.CLIENT);
		INSTANCE.registerMessage(UHCStartPacket.PacketHandler.class, UHCStartPacket.class, 1, Side.SERVER);
		INSTANCE.registerMessage(UHCPacketTeamHandler.class, UHCPacketTeam.class, 2, Side.SERVER);
		INSTANCE.registerMessage(UHCPage1PacketHandler.class, UHCPage1Packet.class, 3, Side.SERVER);
		INSTANCE.registerMessage(UHCPage2PacketHandler.class, UHCPage2Packet.class, 4, Side.SERVER);
		INSTANCE.registerMessage(UHCPage3PacketHandler.class, UHCPage3Packet.class, 5, Side.SERVER);
		INSTANCE.registerMessage(UHCPage4PacketHandler.class, UHCPage4Packet.class, 6, Side.SERVER);
		INSTANCE.registerMessage(UHCPage5PacketHandler.class, UHCPage5Packet.class, 7, Side.SERVER);
		INSTANCE.registerMessage(UHCPage6PacketHandler.class, UHCPage6Packet.class, 8, Side.SERVER);
		INSTANCE.registerMessage(UHCPacketTeamRandomizer.PacketHandler.class, UHCPacketTeamRandomizer.class, 9, Side.SERVER);
	}
}
