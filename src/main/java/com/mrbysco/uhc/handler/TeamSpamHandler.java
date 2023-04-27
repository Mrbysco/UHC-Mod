package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.Reference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamSpamHandler {

	private final String teamAntiSpam = Reference.MOD_PREFIX + "team_anti_spam";
	private static final HashMap<Player, Integer> spammerList = new HashMap<>();

	@SubscribeEvent
	public void teamSpamProtectionEvent(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			Player player = event.player;
			Level level = player.level;
			CompoundTag playerData = player.getPersistentData();

			if (playerData.getBoolean(teamAntiSpam)) {
				if (!spammerList.containsKey(player))
					spammerList.put(player, 0);
			}

			if (!spammerList.isEmpty()) {
				if (level.getGameTime() % 20 == 0) {
					ArrayList<Player> removalList = new ArrayList<>();

					for (HashMap.Entry<Player, Integer> entry : spammerList.entrySet()) {
						Player listPlayer = entry.getKey();
						Integer listInt = entry.getValue();

						if (listInt == 5) {
							removalList.add(listPlayer);
						} else {
							spammerList.put(listPlayer, spammerList.get(listPlayer) + 1);
						}
					}

					for (Player remove : removalList) {
						CompoundTag removePlayerData = player.getPersistentData();
						removePlayerData.putBoolean(teamAntiSpam, false);
						spammerList.remove(remove);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getEntity();
		player.getPersistentData().remove(teamAntiSpam);
	}
}
