package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class UHCPacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Reference.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private static int id = 0;

	public static void registerMessages() {
		INSTANCE.registerMessage(id++, UHCPacketMessage.class, UHCPacketMessage::encode, UHCPacketMessage::decode, UHCPacketMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

		INSTANCE.registerMessage(id++, UHCStartPacket.class, UHCStartPacket::encode, UHCStartPacket::decode, UHCStartPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, UHCPacketTeam.class, UHCPacketTeam::encode, UHCPacketTeam::decode, UHCPacketTeam::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, UHCPage1Packet.class, UHCPage1Packet::encode, UHCPage1Packet::decode, UHCPage1Packet::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, UHCPage2Packet.class, UHCPage2Packet::encode, UHCPage2Packet::decode, UHCPage2Packet::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, UHCPage3Packet.class, UHCPage3Packet::encode, UHCPage3Packet::decode, UHCPage3Packet::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, UHCPage4Packet.class, UHCPage4Packet::encode, UHCPage4Packet::decode, UHCPage4Packet::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, UHCPage5Packet.class, UHCPage5Packet::encode, UHCPage5Packet::decode, UHCPage5Packet::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, UHCPage6Packet.class, UHCPage6Packet::encode, UHCPage6Packet::decode, UHCPage6Packet::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
		INSTANCE.registerMessage(id++, UHCPacketTeamRandomizer.class, UHCPacketTeamRandomizer::encode, UHCPacketTeamRandomizer::decode, UHCPacketTeamRandomizer::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
	}
}
