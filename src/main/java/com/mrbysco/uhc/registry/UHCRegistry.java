package com.mrbysco.uhc.registry;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.item.UHCBook;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class UHCRegistry {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

	public static final DeferredItem<UHCBook> UHC_BOOK = ITEMS.registerItem("uhc_book", UHCBook::new);
}
