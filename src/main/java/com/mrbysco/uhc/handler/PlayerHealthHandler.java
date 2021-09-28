package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerHealthHandler {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START && event.side.isServer()) {
			PlayerEntity player = event.player;
			ServerWorld overworld = player.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				final CompoundNBT entityData = player.getPersistentData();
				
				double baseHealth = player.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
				double maxHealth = (double) saveData.getMaxHealth();
	
				if(saveData.isApplyCustomHealth()) {
					if(baseHealth != maxHealth) {
						this.setHealth(player, saveData.getMaxHealth());
			            entityData.putBoolean("modifiedMaxHealth", true);
					}
				} else {
					if(baseHealth != 20.0) {
						this.setHealth(player, 20.0f);
			            entityData.remove("modifiedMaxHealth");
					}
				}
			}
		}
	}
	
	public void setHealth(PlayerEntity entity, float maxHealth) {
        entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
        entity.setHealth(maxHealth);
    }
	
	@SubscribeEvent
	public void respawnReset(PlayerEvent.Clone event) {
		PlayerEntity newPlayer = event.getPlayer();
		final CompoundNBT entityData = newPlayer.getPersistentData();
		
		this.setHealth(newPlayer, 20);
        entityData.putBoolean("modifiedMaxHealth", false);
	}
}
