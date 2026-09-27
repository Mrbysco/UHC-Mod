package com.mrbysco.uhc.datagen.assets;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.registry.UHCRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class UHCModelProvider extends ItemModelProvider {
	public UHCModelProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, Reference.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		generatedItem(UHCRegistry.UHC_BOOK.getId());
	}

	private void generatedItem(ResourceLocation location) {
		singleTexture(location.getPath(), ResourceLocation.withDefaultNamespace("item/generated"),
				"layer0", ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "item/" + location.getPath()));
	}
}
