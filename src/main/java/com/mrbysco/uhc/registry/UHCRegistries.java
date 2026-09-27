package com.mrbysco.uhc.registry;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.AutoSmelt;
import com.mrbysco.uhc.data.ItemConversion;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class UHCRegistries {
	public static final ResourceKey<Registry<AutoSmelt>> AUTO_SMELT_REGISTRY_KEY = ResourceKey.createRegistryKey(
			Reference.modLoc("auto_smelt"));
	public static final ResourceKey<Registry<ItemConversion>> ITEM_CONVERSION_REGISTRY_KEY = ResourceKey.createRegistryKey(
			Reference.modLoc("item_conversion"));

	@SubscribeEvent
	public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(AUTO_SMELT_REGISTRY_KEY,
				AutoSmelt.DIRECT_CODEC, AutoSmelt.DIRECT_CODEC);
		event.dataPackRegistry(ITEM_CONVERSION_REGISTRY_KEY,
				ItemConversion.DIRECT_CODEC, ItemConversion.DIRECT_CODEC);
	}
}
