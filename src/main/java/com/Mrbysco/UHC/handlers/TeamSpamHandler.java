package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamSpamHandler {

	private final String teamAntiSpam = Reference.MOD_PREFIX + "team_anti_spam";
	private static HashMap<EntityPlayer, Integer> spammerList = new HashMap<>();

	@SubscribeEvent
	public void teamSpamProtectionEvent(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			World world = player.world;
			NBTTagCompound playerData = player.getEntityData();
		
			if(playerData.getBoolean(teamAntiSpam))
			{
				if(!spammerList.containsKey(player))
					spammerList.put(player, 0);
			}
			
			if(!spammerList.isEmpty())
			{
				if (world.getWorldTime() % 20 == 0)
				{
					ArrayList<EntityPlayer> removalList = new ArrayList<>();
					
					for (HashMap.Entry<EntityPlayer, Integer> entry : spammerList.entrySet()) {
						EntityPlayer listPlayer = entry.getKey();
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
					
					for(EntityPlayer remove : removalList)
					{
						NBTTagCompound removePlayerData = player.getEntityData();
						removePlayerData.setBoolean(teamAntiSpam, false);
						spammerList.remove(remove);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		player.getEntityData().setBoolean(teamAntiSpam, false);
	}
}
