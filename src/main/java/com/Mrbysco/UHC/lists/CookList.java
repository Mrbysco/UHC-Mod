package com.Mrbysco.UHC.lists;

import java.util.ArrayList;

import com.Mrbysco.UHC.lists.info.AutoCookInfo;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CookList {
	public static ArrayList<AutoCookInfo> autoCookList = new ArrayList<>();

	public static AutoCookInfo autoCook_Info;
	
	public static void initializeAutoCook() {
		//Ores
		addAutoCookInfo(Item.getItemFromBlock(Blocks.GOLD_ORE), Items.GOLD_INGOT, 1F);
		addAutoCookInfo(Item.getItemFromBlock(Blocks.IRON_ORE), Items.IRON_INGOT, 0.7F);
		
		//Food
		addAutoCookInfo(Items.BEEF, Items.COOKED_BEEF, 0.35F);
		addAutoCookInfo(Items.CHICKEN, Items.COOKED_CHICKEN, 0.35F);
		addAutoCookInfo(Items.FISH, Items.COOKED_FISH, 0.35F);
		addAutoCookInfo(Items.FISH, 1, Items.COOKED_FISH, 1, 0.35F);
		addAutoCookInfo(Items.MUTTON, Items.COOKED_MUTTON, 0.35F);
		addAutoCookInfo(Items.PORKCHOP, Items.COOKED_PORKCHOP, 0.35F);
		addAutoCookInfo(Items.RABBIT, Items.COOKED_RABBIT, 0.35F);
	}
	
	public static void addAutoCookInfo(ItemStack stack, ItemStack stack2, float experienceAmount)
	{
		// Check if the info doesn't already exist
		autoCook_Info = new AutoCookInfo(stack, stack2, experienceAmount);
		if(autoCookList.contains(autoCook_Info))
			return;
		else
			autoCookList.add(autoCook_Info);
	}
	
	public static void addAutoCookInfo(Item stack, Item stack2, float experienceAmount)
	{
		// Check if the info doesn't already exist
		autoCook_Info = new AutoCookInfo(new ItemStack(stack), new ItemStack(stack2), experienceAmount);
		if(autoCookList.contains(autoCook_Info))
			return;
		else
			autoCookList.add(autoCook_Info);
	}
	
	public static void addAutoCookInfo(Item stack, int meta, Item stack2, int meta2, float experienceAmount)
	{
		// Check if the info doesn't already exist
		autoCook_Info = new AutoCookInfo(new ItemStack(stack, meta), new ItemStack(stack2, meta2), experienceAmount);
		if(autoCookList.contains(autoCook_Info))
			return;
		else
			autoCookList.add(autoCook_Info);
	}
	
	public static void removeAutoCookInfo(ItemStack output)
	{
		if(autoCookList != null)
		{
			for(AutoCookInfo info : autoCookList)
			{
				if(info.getResult().equals(output))
				{
					autoCookList.remove(info);
				}
			}
		}
	}
}
