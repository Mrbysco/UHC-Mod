package com.Mrbysco.UHC.compat.ct;

import com.Mrbysco.UHC.compat.ct.actions.ActionSpawnItems;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ultrahardcoremod.starting")
public class SpawnItemsCT {

	@ZenMethod
    public static void addStartingGear(IItemStack stack) {
        CraftTweakerAPI.apply(new ActionSpawnItems(stack));
	}
	
	@ZenMethod
	public static void addStartingGear(IItemStack stack, IItemStack stack2) {
		CraftTweakerAPI.apply(new ActionSpawnItems(stack, stack2));
	}
	
	@ZenMethod
	public static void addStartingGear(IItemStack stack, IItemStack stack2, IItemStack stack3) {
		CraftTweakerAPI.apply(new ActionSpawnItems(stack, stack2, stack3));
	}
	
	@ZenMethod
	public static void addStartingGear(IItemStack stack, IItemStack stack2, IItemStack stack3, IItemStack stack4) {
		CraftTweakerAPI.apply(new ActionSpawnItems(stack, stack2, stack3, stack4));
	}
	
	@ZenMethod
	public static void addStartingGear(IItemStack stack, IItemStack stack2, IItemStack stack3, IItemStack stack4, 
			IItemStack stack5) {
		CraftTweakerAPI.apply(new ActionSpawnItems(stack, stack2, stack3, stack4, stack5));
	}
	
	@ZenMethod
	public static void addStartingGear(IItemStack stack, IItemStack stack2, IItemStack stack3, IItemStack stack4,
			IItemStack stack5, IItemStack stack6) {
		CraftTweakerAPI.apply(new ActionSpawnItems(stack, stack2, stack3, stack4, stack5, stack6));
	}
	
	@ZenMethod
	public static void addStartingGear(IItemStack stack, IItemStack stack2, IItemStack stack3, IItemStack stack4,
			IItemStack stack5, IItemStack stack6, IItemStack stack7) {
		CraftTweakerAPI.apply(new ActionSpawnItems(stack, stack2, stack3, stack4, stack5, stack6, stack7));
	}
	
	@ZenMethod
	public static void addStartingGear(IItemStack stack, IItemStack stack2, IItemStack stack3, IItemStack stack4,
			IItemStack stack5, IItemStack stack6, IItemStack stack7, IItemStack stack8) {
		CraftTweakerAPI.apply(new ActionSpawnItems(stack, stack2, stack3, stack4, stack5, stack6, stack7, stack8));
	}
	
	@ZenMethod
	public static void addStartingGear(IItemStack stack, IItemStack stack2, IItemStack stack3, IItemStack stack4,
			IItemStack stack5, IItemStack stack6, IItemStack stack7, IItemStack stack8, IItemStack stack9) {
		CraftTweakerAPI.apply(new ActionSpawnItems(stack, stack2, stack3, stack4, stack5, stack6, stack7, stack8, stack9));
	}
}
