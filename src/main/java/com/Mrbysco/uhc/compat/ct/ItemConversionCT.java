package com.mrbysco.uhc.compat.ct;

import com.mrbysco.uhc.compat.ct.actions.ActionItemConversion;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ultrahardcoremod.conversion")
public class ItemConversionCT {

	@ZenMethod
    public static void addConversion(IItemStack input, IItemStack result) {
        CraftTweakerAPI.apply(new ActionItemConversion(input, result));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4, 
			IItemStack result5) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4,
			IItemStack result5, IItemStack result6) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5, result6));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4,
			IItemStack result5, IItemStack result6, IItemStack result7) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5, result6, result7));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4,
			IItemStack result5, IItemStack result6, IItemStack result7, IItemStack result8) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5, result6, result7, result8));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4,
			IItemStack result5, IItemStack result6, IItemStack result7, IItemStack result8, IItemStack result9) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5, result6, result7, result8, result9));
	}
}
