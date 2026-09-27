package com.mrbysco.uhc.datagen;

import com.mrbysco.uhc.bootstrap.SmeltingBootstrap;
import com.mrbysco.uhc.datagen.assets.UHCLanguageProvider;
import com.mrbysco.uhc.datagen.assets.UHCModelProvider;
import com.mrbysco.uhc.registry.UHCRegistries;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class UHCDatagen {
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(UHCRegistries.AUTO_SMELT_REGISTRY_KEY, SmeltingBootstrap::bootstrap)
			.add(UHCRegistries.ITEM_CONVERSION_REGISTRY_KEY, (context -> {
			}));

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		event.createDatapackRegistryObjects(BUILDER);

		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		generator.addProvider(true, new UHCLanguageProvider(packOutput));
		generator.addProvider(true, new UHCModelProvider(packOutput, helper));
	}
}
