package com.mrbysco.uhc.network;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.network.payload.StartUHCPayload;
import com.mrbysco.uhc.network.payload.UHCPage1Payload;
import com.mrbysco.uhc.network.payload.UHCPage2Payload;
import com.mrbysco.uhc.network.payload.UHCPage3Payload;
import com.mrbysco.uhc.network.payload.UHCPage4Payload;
import com.mrbysco.uhc.network.payload.UHCPage5Payload;
import com.mrbysco.uhc.network.payload.UHCPage6Payload;
import com.mrbysco.uhc.network.payload.UHCSyncPayload;
import com.mrbysco.uhc.network.payload.UHCTeamPayload;
import com.mrbysco.uhc.network.payload.UHCTeamRandomizerPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber()
public class UHCNetworkHandler {
	@SubscribeEvent
	public static void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(Reference.MOD_ID);

		registrar.playToClient(UHCSyncPayload.ID, UHCSyncPayload.STREAM_CODEC, UHCSyncPayload.Handler::handle);

		registrar.playToServer(StartUHCPayload.ID, StartUHCPayload.STREAM_CODEC, StartUHCPayload.Handler::handle);
		registrar.playToServer(UHCTeamPayload.ID, UHCTeamPayload.STREAM_CODEC, UHCTeamPayload.Handler::handle);
		registrar.playToServer(UHCTeamRandomizerPayload.ID, UHCTeamRandomizerPayload.STREAM_CODEC, UHCTeamRandomizerPayload.Handler::handle);

		registrar.playToServer(UHCPage1Payload.ID, UHCPage1Payload.STREAM_CODEC, UHCPage1Payload.Handler::handle);
		registrar.playToServer(UHCPage2Payload.ID, UHCPage2Payload.STREAM_CODEC, UHCPage2Payload.Handler::handle);
		registrar.playToServer(UHCPage3Payload.ID, UHCPage3Payload.STREAM_CODEC, UHCPage3Payload.Handler::handle);
		registrar.playToServer(UHCPage4Payload.ID, UHCPage4Payload.STREAM_CODEC, UHCPage4Payload.Handler::handle);
		registrar.playToServer(UHCPage5Payload.ID, UHCPage5Payload.STREAM_CODEC, UHCPage5Payload.Handler::handle);
		registrar.playToServer(UHCPage6Payload.ID, UHCPage6Payload.STREAM_CODEC, UHCPage6Payload.Handler::handle);
	}
}
