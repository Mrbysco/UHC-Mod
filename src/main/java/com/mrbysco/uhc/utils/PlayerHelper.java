package com.mrbysco.uhc.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerHelper {

	public static Player getPlayerEntityByName(Level world, String name) {
		for (int j2 = 0; j2 < world.players().size(); ++j2) {
			Player player = world.players().get(j2);

			if (name.equals(player.getName().getString())) {
				return player;
			}
		}

		return null;
	}
}
