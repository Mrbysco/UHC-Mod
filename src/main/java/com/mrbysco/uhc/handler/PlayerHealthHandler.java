package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.registry.UHCDataAttachments;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Objects;

@EventBusSubscriber
public class PlayerHealthHandler {

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD)) {
			UHCSaveData saveData = UHCSaveData.get(level);

			double baseHealth = Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).getBaseValue();
			double maxHealth = saveData.getMaxHealth();

			if (saveData.isApplyCustomHealth()) {
				if (baseHealth != maxHealth) {
					setHealth(player, saveData.getMaxHealth());
					player.setData(UHCDataAttachments.MODIFIED_MAX_HEALTH, true);
				}
			} else {
				if (baseHealth != 20.0) {
					setHealth(player, 20.0f);
					player.removeData(UHCDataAttachments.MODIFIED_MAX_HEALTH);
				}
			}
		}
	}

	private static void setHealth(Player entity, float maxHealth) {
		var instance = entity.getAttribute(Attributes.MAX_HEALTH);
		if (instance != null) {
			instance.setBaseValue(maxHealth);
			entity.setHealth(maxHealth);
		}
	}

	@SubscribeEvent
	public static void respawnReset(PlayerEvent.Clone event) {
		Player newPlayer = event.getEntity();

		setHealth(newPlayer, 20);
		newPlayer.setData(UHCDataAttachments.MODIFIED_MAX_HEALTH, false);
	}
}
