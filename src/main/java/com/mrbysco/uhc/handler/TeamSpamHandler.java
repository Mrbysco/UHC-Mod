package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.registry.UHCDataAttachments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber
public class TeamSpamHandler {

	private static final Map<Player, Integer> spammerList = new HashMap<>();

	@SubscribeEvent
	public static void teamSpamProtectionEvent(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {

			if (player.getData(UHCDataAttachments.TEAM_ANTI_SPAM)) {
				if (!spammerList.containsKey(player))
					spammerList.put(player, 0);
			}

			if (!spammerList.isEmpty()) {
				if (level.getGameTime() % 20 == 0) {
					List<Player> removalList = new ArrayList<>();

					for (Map.Entry<Player, Integer> entry : spammerList.entrySet()) {
						Player listPlayer = entry.getKey();
						Integer listInt = entry.getValue();

						if (listInt == 5) {
							removalList.add(listPlayer);
						} else {
							spammerList.put(listPlayer, spammerList.get(listPlayer) + 1);
						}
					}

					for (Player remove : removalList) {
						remove.setData(UHCDataAttachments.TEAM_ANTI_SPAM, false);
						spammerList.remove(remove);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		Player player = event.getEntity();
		player.removeData(UHCDataAttachments.TEAM_ANTI_SPAM);
	}
}
