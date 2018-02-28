package com.Mrbysco.UHC.compat.ct;

import com.Mrbysco.UHC.lists.CookList;

import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.Item;

public class ActionAutoCook implements IAction {

	private final IItemStack rawProduct;
	private final IItemStack cookedProduct;
	private final boolean removal;
	private final float experience;

	public ActionAutoCook(IItemStack stack1, IItemStack stack2, boolean removal, float experience) {
		this.rawProduct = stack1;
		this.cookedProduct = stack1;
		this.removal = removal;
		this.experience = experience;
	}
	
	@Override
	public void apply() {
		if (this.removal)
			CookList.removeAutoCookInfo(Item.getByNameOrId(rawProduct.getName()), rawProduct.getMetadata(), Item.getByNameOrId(cookedProduct.getName()), cookedProduct.getMetadata() ,this.experience);
		else
			CookList.addAutoCookInfo(Item.getByNameOrId(rawProduct.getName()), rawProduct.getMetadata(), Item.getByNameOrId(cookedProduct.getName()), cookedProduct.getMetadata() ,this.experience);
	}

	@Override
	public String describe() {
		if (this.removal)
			return String.format(this.rawProduct.toString() + " to " + this.cookedProduct.toString() + " has been removed from the Auto Cook list");	
		else
			return String.format(this.rawProduct.toString() + " to " + this.cookedProduct.toString() + " has been added to the Auto Cook list");	
	}
}