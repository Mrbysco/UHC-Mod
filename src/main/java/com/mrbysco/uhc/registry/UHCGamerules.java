package com.mrbysco.uhc.registry;

import net.minecraft.world.level.GameRules;

public class UHCGamerules {

	public static final GameRules.Key<GameRules.BooleanValue> AUTO_COOK =
			GameRules.register("uhcAutoCook", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.BooleanValue> ITEM_CONVERSION =
			GameRules.register("uhcItemConversion", GameRules.Category.MISC, GameRules.BooleanValue.create(false));

	public static void init() {
		// NOOP
	}
}
