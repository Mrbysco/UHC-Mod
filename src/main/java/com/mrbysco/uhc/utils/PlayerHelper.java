package com.mrbysco.uhc.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class PlayerHelper {

	public static PlayerEntity getPlayerEntityByName(World world, String name) {
		for (int j2 = 0; j2 < world.getPlayers().size(); ++j2) {
			PlayerEntity player = world.getPlayers().get(j2);

			if (name.equals(player.getName().getString())) {
				return player;
			}
		}

		return null;
	}
}
