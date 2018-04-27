package com.Mrbysco.UHC.lists;

import java.util.ArrayList;

import com.Mrbysco.UHC.lists.info.SpawnItemInfo;

import net.minecraft.item.ItemStack;

public class SpawnItemList {
	public static ArrayList<SpawnItemInfo> spawnItemList = new ArrayList<>();

	public static SpawnItemInfo spawnItem_info;
	
	public static void initializeSpawnItems() {
		
	}
	
	public static void addSpawnItems(ItemStack stack1, ItemStack stack2, ItemStack stack3, ItemStack stack4, 
			ItemStack stack5, ItemStack stack6, ItemStack stack7, ItemStack stack8, ItemStack stack9) {
		// Check if the info doesn't already exist
		spawnItem_info = new SpawnItemInfo(stack1, stack2, stack3, stack4, stack5, stack6, stack7, stack8, stack9);
		if(spawnItemList.contains(spawnItem_info))
			return;
		else
			spawnItemList.add(spawnItem_info);
	}
}
