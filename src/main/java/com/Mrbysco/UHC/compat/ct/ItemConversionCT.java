package com.Mrbysco.UHC.compat.ct;

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
        CraftTweakerAPI.apply(new ActionItemConversion(input, result, false));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, false));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, false));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, false));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4,
			IItemStack result5) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5, false));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4,
			IItemStack result5, IItemStack result6) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5, result6, false));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4,
			IItemStack result5, IItemStack result6, IItemStack result7) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5, result6, result7, false));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4,
			IItemStack result5, IItemStack result6, IItemStack result7, IItemStack result8) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5, result6, result7, result8, false));
	}
	
	@ZenMethod
	public static void addConversion(IItemStack input, IItemStack result, IItemStack result2, IItemStack result3, IItemStack result4,
			IItemStack result5, IItemStack result6, IItemStack result7, IItemStack result8, IItemStack result9) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, result, result2, result3, result4, result5, result6, result7, result8, result9, false));
	}
	
	@ZenMethod
	public static void removeConversion(IItemStack input) {
		CraftTweakerAPI.apply(new ActionItemConversion(input, true));
	}
}
