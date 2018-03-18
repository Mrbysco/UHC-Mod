package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
			boolean flag = saveData.isApplyCustomHealth();
			double maxHealth = (double) saveData.getMaxHealth();
			
			if(saveData.isUhcOnGoing() == false)
			{
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
						this.setHealth(player, saveData.getMaxHealth());
					}
				}
			}
		} 
	}
	
	public void setHealth(EntityPlayer entity, int maxHealth) {
        entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)maxHealth);
        entity.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, 1, maxHealth/4, true, false));
    }
	
	@SubscribeEvent
	public void respawnReset(PlayerEvent.Clone event) {
		EntityPlayer newPlayer = event.getEntityPlayer();
		final NBTTagCompound entityData = newPlayer.getEntityData();
		
		this.setHealth(newPlayer, 20);
        entityData.setBoolean("modifiedMaxHealth", false);
	}
}
