package com.Mrbysco.UHC.lists.info;

import net.minecraft.item.Item;

public class AutoCookInfo {
	private Item input;
	private int inputMeta;
	private Item result;
	private int resultMeta;
	private float experience;
	
	public AutoCookInfo(Item itemIn, int meta, Item itemIn2, int meta2, float experienceAmount) {
		this.input = itemIn;
		this.inputMeta = meta;
		this.result = itemIn2;
		this.resultMeta = meta2;
		this.experience = experienceAmount;
	}
	
	public AutoCookInfo(Item itemIn, Item itemIn2, float experienceAmount) {
		this.input = itemIn;
		this.result = itemIn2;
		this.experience = experienceAmount;
	}
	
	public Item getInput() {
		return input;
	}
	
	public int getInputMeta() {
		return inputMeta;
	}
	
	public Item getResult() {
		return result;
	}
	
	public int getResultMeta() {
		return resultMeta;
	}
	
	public float getExperience() {
		return experience;
	}
}
