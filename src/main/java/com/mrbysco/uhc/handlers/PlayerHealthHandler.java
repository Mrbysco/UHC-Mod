package com.mrbysco.uhc.handlers;

import com.mrbysco.uhc.init.UHCSaveData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class PlayerHealthHandler {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START && event.side.isServer()) {
			EntityPlayer player = event.player;
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				final NBTTagCompound entityData = player.getEntityData();
				
				double playerHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
				double maxHealth = (double) saveData.getMaxHealth();
	
				if(saveData.isApplyCustomHealth())
				{
					if(playerHealth != maxHealth)
					{
						this.setHealth(player, saveData.getMaxHealth());
			            entityData.setBoolean("modifiedMaxHealth", true);
					}
				}
				else
				{
					if(playerHealth != 20.0)
					{
						this.setHealth(player, 20);
			            entityData.setBoolean("modifiedMaxHealth", false);
					}
				}
			}
		}
	}
	
	public void setHealth(EntityPlayer entity, int maxHealth) {
        entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)maxHealth);
        entity.setHealth(maxHealth);
    }
	
	@SubscribeEvent
	public void respawnReset(PlayerEvent.Clone event) {
		EntityPlayer newPlayer = event.getEntityPlayer();
		final NBTTagCompound entityData = newPlayer.getEntityData();
		
		this.setHealth(newPlayer, 20);
        entityData.setBoolean("modifiedMaxHealth", false);
	}
}
