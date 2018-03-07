package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class PlayerHealthHandler {

	@SubscribeEvent
	public void onWorldTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START) {
			EntityPlayer player = event.player;
			World world = player.world;
			UHCSaveData saveData = UHCSaveData.getForWorld(event.player.world);
			final NBTTagCompound entityData = player.getEntityData();
			
			double playerHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
			
			if(saveData.isUhcStarting())
			{
				boolean flag = saveData.isApplyCustomHealth();
				if(playerHealth != saveData.getMaxHealth() && flag)
				{
					this.setHealth(player, saveData.getMaxHealth(), saveData.getMaxHealth());
		            entityData.setBoolean("modifiedMaxHealth", true);
				}
				
				if(playerHealth != saveData.getMaxHealth() && flag && entityData.getBoolean("revival"));
				{
					this.setHealth(player, saveData.getMaxHealth(), saveData.getMaxHealth());
		            entityData.setBoolean("modifiedMaxHealth", true);
		            entityData.setBoolean("revival", false);
				}
			}
			
		} 
	}
	
	public void setHealth(EntityPlayer entity, int maxHealth, int startHealth) {
        entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)maxHealth);
        entity.setHealth((float)startHealth);
    }
	
	@SubscribeEvent
	public void respawnReset(PlayerEvent.PlayerRespawnEvent event) {
		final NBTTagCompound entityData = event.player.getEntityData();
		
		this.setHealth(event.player, 20, 20);
        entityData.setBoolean("modifiedMaxHealth", false);
	}
}
