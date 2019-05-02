package com.mrbysco.uhc.compat.ct;

import com.mrbysco.uhc.compat.ct.actions.ActionAutoCook;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ultrahardcoremod.autocook")
public class AutoCookCT {

	@ZenMethod
    public static void addRecipe(IItemStack stack, IItemStack stack2, float experience) {
        CraftTweakerAPI.apply(new ActionAutoCook(stack, stack2, experience));
	}
	
	@ZenMethod
	public static void removeRecipe(IItemStack stack) {
		CraftTweakerAPI.apply(new ActionAutoCook(stack));
	}
}
