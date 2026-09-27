package com.mrbysco.uhc.bootstrap;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.AutoSmelt;
import com.mrbysco.uhc.registry.UHCRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class SmeltingBootstrap {
	public static void bootstrap(BootstrapContext<AutoSmelt> context) {
		registerSmelting(context, Items.POTATO, Items.BAKED_POTATO, 0.35F);
		registerSmelting(context, Items.CHORUS_FRUIT, Items.POPPED_CHORUS_FRUIT, 0.1F);
		registerSmelting(context, Items.BEEF, Items.COOKED_BEEF, 0.35F);
		registerSmelting(context, Items.CHICKEN, Items.COOKED_CHICKEN, 0.35F);
		registerSmelting(context, Items.COD, Items.COOKED_COD, 0.35F);
		registerSmelting(context, Blocks.KELP, Items.DRIED_KELP, 0.1F);
		registerSmelting(context, Items.SALMON, Items.COOKED_SALMON, 0.35F);
		registerSmelting(context, Items.MUTTON, Items.COOKED_MUTTON, 0.35F);
		registerSmelting(context, Items.PORKCHOP, Items.COOKED_PORKCHOP, 0.35F);
		registerSmelting(context, Items.RABBIT, Items.COOKED_RABBIT, 0.35F);

		registerSmelting(context, VanillaRecipeProvider.COAL_SMELTABLES, Items.COAL, 0.1F);
		registerSmelting(context, VanillaRecipeProvider.IRON_SMELTABLES, Items.IRON_INGOT, 0.7F);
		registerSmelting(context, VanillaRecipeProvider.COPPER_SMELTABLES, Items.COPPER_INGOT, 0.7F);
		registerSmelting(context, VanillaRecipeProvider.GOLD_SMELTABLES, Items.GOLD_INGOT, 1.0F);
		registerSmelting(context, VanillaRecipeProvider.DIAMOND_SMELTABLES, Items.DIAMOND, 1.0F);
		registerSmelting(context, VanillaRecipeProvider.LAPIS_SMELTABLES, Items.LAPIS_LAZULI, 0.2F);
		registerSmelting(context, VanillaRecipeProvider.REDSTONE_SMELTABLES, Items.REDSTONE, 0.7F);
		registerSmelting(context, VanillaRecipeProvider.EMERALD_SMELTABLES, Items.EMERALD, 1.0F);
		registerSmelting(context, Blocks.NETHER_QUARTZ_ORE, Items.QUARTZ, 0.2F);
	}

	private static void registerSmelting(BootstrapContext<AutoSmelt> context, List<ItemLike> inputs, Item output, float experience) {
		for (ItemLike input : inputs) {
			registerSmelting(context, input, output, experience);
		}
	}

	private static void registerSmelting(BootstrapContext<AutoSmelt> context, ItemLike input, Item output, float experience) {
		ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output);
		ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(input.asItem());
		ResourceKey<AutoSmelt> recipeKey = key(outputId.getPath() + "_from_" + inputId.getPath());
		context.register(recipeKey, new AutoSmelt(Ingredient.of(input), output.getDefaultInstance(), experience));
	}

	private static ResourceKey<AutoSmelt> key(String name) {
		return ResourceKey.create(UHCRegistries.AUTO_SMELT_REGISTRY_KEY, Reference.modLoc(name));
	}
}
