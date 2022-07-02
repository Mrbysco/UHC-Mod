package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerHealthHandler {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START && event.side.isServer()) {
			Player player = event.player;
			ServerLevel overworld = player.getServer().getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				final CompoundTag entityData = player.getPersistentData();

				double baseHealth = player.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
				double maxHealth = (double) saveData.getMaxHealth();

				if (saveData.isApplyCustomHealth()) {
					if (baseHealth != maxHealth) {
						this.setHealth(player, saveData.getMaxHealth());
						entityData.putBoolean("modifiedMaxHealth", true);
					}
				} else {
					if (baseHealth != 20.0) {
						this.setHealth(player, 20.0f);
						entityData.remove("modifiedMaxHealth");
					}
				}
			}
		}
	}

	public void setHealth(Player entity, float maxHealth) {
		entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
		entity.setHealth(maxHealth);
	}

	@SubscribeEvent
	public void respawnReset(PlayerEvent.Clone event) {
		Player newPlayer = event.getPlayer();
		final CompoundTag entityData = newPlayer.getPersistentData();

		this.setHealth(newPlayer, 20);
		entityData.putBoolean("modifiedMaxHealth", false);
	}
}
