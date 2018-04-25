package com.Mrbysco.UHC.compat.ct.actions;

import com.Mrbysco.UHC.lists.CookList;

import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;

public class ActionAutoCook implements IAction {

	private final ItemStack rawProduct;
	private final ItemStack cookedProduct;
	private final boolean removal;
	private final float experience;

	public ActionAutoCook(IItemStack stack1, IItemStack stack2, float experience) {
		this.rawProduct = CraftTweakerMC.getItemStack(stack1);
		this.cookedProduct = CraftTweakerMC.getItemStack(stack2);
		this.removal = false;
		this.experience = experience;
	}
	
	public ActionAutoCook(IItemStack output) {
		this.rawProduct = CraftTweakerMC.getItemStack(output);
		this.cookedProduct = ItemStack.EMPTY;
		this.removal = true;
		this.experience = 0;
	}
	
	@Override
	public void apply() {
		if (this.removal)
			CookList.removeAutoCookInfo(rawProduct);
		else
			CookList.addAutoCookInfo(rawProduct, cookedProduct, this.experience);
	}

	@Override
	public String describe() {
		if (this.removal)
			return String.format("Auto cooking " + this.rawProduct.getDisplayName() + " has been removed from the Auto Cook list");	
		else
			return String.format(this.rawProduct.getDisplayName() + " to " + this.cookedProduct.getDisplayName() + " has been added to the Auto Cook list");	
	}
}