package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.Reference;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamSpamHandler {

	private final String teamAntiSpam = Reference.MOD_PREFIX + "team_anti_spam";
	private static final HashMap<PlayerEntity, Integer> spammerList = new HashMap<>();

	@SubscribeEvent
	public void teamSpamProtectionEvent(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			PlayerEntity player = event.player;
			World world = player.world;
			CompoundNBT playerData = player.getPersistentData();
		
			if(playerData.getBoolean(teamAntiSpam)) {
				if(!spammerList.containsKey(player))
					spammerList.put(player, 0);
			}
			
			if(!spammerList.isEmpty()) {
				if (world.getGameTime() % 20 == 0) {
					ArrayList<PlayerEntity> removalList = new ArrayList<>();
					
					for (HashMap.Entry<PlayerEntity, Integer> entry : spammerList.entrySet()) {
						PlayerEntity listPlayer = entry.getKey();
						Integer listInt = entry.getValue();
						
						if(listInt == 5)
						{
							removalList.add(listPlayer);
						}
						else
						{
							spammerList.put(listPlayer, spammerList.get(listPlayer) + 1);
						}
					}
					
					for(PlayerEntity remove : removalList) {
						CompoundNBT removePlayerData = player.getPersistentData();
						removePlayerData.putBoolean(teamAntiSpam, false);
						spammerList.remove(remove);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		PlayerEntity player = event.getPlayer();
		player.getPersistentData().remove(teamAntiSpam);
	}
}
