package com.mrbysco.uhc.registry;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.item.UHCBookItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

	public static final RegistryObject<Item> UHC_BOOK = ITEMS.register("uhc_book", () ->
			new UHCBookItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
}
