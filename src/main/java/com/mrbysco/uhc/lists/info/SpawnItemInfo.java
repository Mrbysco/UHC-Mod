package com.mrbysco.uhc.lists.info;

import net.minecraft.world.item.ItemStack;

public class SpawnItemInfo {
	private final ItemStack[] stacks;

	public SpawnItemInfo(ItemStack[] stacks) {
		this.stacks = stacks;
	}

	public int getStackCount() {
		return stacks.length;
	}

	public ItemStack getStack(int value) {
		return stacks[value];
	}
}
