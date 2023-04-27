package com.mrbysco.uhc.lists;

import com.mrbysco.uhc.lists.info.SpawnItemInfo;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class SpawnItemList {
	public static final ArrayList<SpawnItemInfo> spawnItemList = new ArrayList<>();

	public static SpawnItemInfo spawnItem_info;

	public static void initializeSpawnItems() {

	}

	public static void addSpawnItems(ItemStack[] stacks) {
		// Check if the info doesn't already exist
		spawnItem_info = new SpawnItemInfo(stacks);
		if (!spawnItemList.contains(spawnItem_info))
			spawnItemList.add(spawnItem_info);
	}
}
