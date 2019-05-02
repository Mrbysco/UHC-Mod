package com.mrbysco.uhc.lists.info;

import net.minecraft.item.ItemStack;

public class AutoCookInfo {
	private ItemStack input;
	private ItemStack result;
	private float experience;
	
	public AutoCookInfo(ItemStack stack, ItemStack stack2, float experienceAmount) {
		this.input = stack;
		this.result = stack2;
		this.experience = experienceAmount;
	}
	
	public ItemStack getInput() {
		return input;
	}
	
	public ItemStack getResult() {
		return result;
	}
	
	public float getExperience() {
		return experience;
	}
}
