package com.mrbysco.uhc.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerHelper {

	/**
	 * Gets a player entity by their name.
	 *
	 * @param level The level to search in.
	 * @param name  The name of the player.
	 * @return The player entity with the given name, or null if not found.
	 */
	public static Player getPlayerEntityByName(Level level, String name) {
		for (Player player : level.players()) {
			if (name.equals(player.getName().getString())) {
				return player;
			}
		}

		return null;
	}
}
