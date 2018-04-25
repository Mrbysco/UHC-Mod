package com.Mrbysco.UHC.lists.info;

import net.minecraft.item.ItemStack;

public class ItemConversionInfo {
	private ItemStack input;
	
	private ItemStack result;
	private ItemStack result2;
	private ItemStack result3;
	private ItemStack result4;
	private ItemStack result5;
	private ItemStack result6;
	private ItemStack result7;
	private ItemStack result8;
	private ItemStack result9;
	
	public ItemConversionInfo(ItemStack itemIn, ItemStack result, ItemStack result2, ItemStack result3, ItemStack result4, 
			ItemStack result5, ItemStack result6, ItemStack result7, ItemStack result8, ItemStack result9) {
		this.input = itemIn;
		
		this.result = result;
		this.result2 = result2;
		this.result3 = result3;
		this.result4 = result4;
		this.result5 = result5;
		this.result5 = result6;
		this.result7 = result7;
		this.result8 = result8;
		this.result9 = result9;
	}
	
	public ItemStack getInput() {
		return input;
	}
	
	public ItemStack getResult() {
		return result;
	}
	
	public ItemStack getResult2() {
		return result2;
	}
	
	public ItemStack getResult3() {
		return result3;
	}
	
	public ItemStack getResult4() {
		return result4;
	}
	
	public ItemStack getResult5() {
		return result5;
	}
	
	public ItemStack getResult6() {
		return result6;
	}
	
	public ItemStack getResult7() {
		return result7;
	}
	
	public ItemStack getResult8() {
		return result8;
	}
	
	public ItemStack getResult9() {
		return result9;
	}
}
