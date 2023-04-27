package com.mrbysco.uhc.registry;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.recipes.AutoCookRecipe;
import com.mrbysco.uhc.recipes.ConversionRecipe;
import com.mrbysco.uhc.recipes.NBTChangeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Reference.MOD_ID);

	public static final RegistryObject<RecipeType<AutoCookRecipe>> AUTO_COOK_RECIPE_TYPE = RECIPE_TYPES.register("auto_cook_recipe", () -> new RecipeType<>() {
	});
	public static final RegistryObject<RecipeType<ConversionRecipe>> CONVERSION_RECIPE_TYPE = RECIPE_TYPES.register("conversion_recipe", () -> new RecipeType<>() {
	});
	public static final RegistryObject<RecipeType<NBTChangeRecipe>> NBT_CHANGE_TYPE = RECIPE_TYPES.register("nbt_change", () -> new RecipeType<>() {
	});

	public static final RegistryObject<AutoCookRecipe.SerializerAutoCookRecipe> AUTO_COOK_SERIALIZER = RECIPE_SERIALIZERS.register("auto_cook_recipe", AutoCookRecipe.SerializerAutoCookRecipe::new);
	public static final RegistryObject<ConversionRecipe.SerializerConversionRecipe> CONVERSION_SERIALIZER = RECIPE_SERIALIZERS.register("conversion_recipe", ConversionRecipe.SerializerConversionRecipe::new);
	public static final RegistryObject<NBTChangeRecipe.SerializerNBTChangeRecipe> NBT_CHANGE = RECIPE_SERIALIZERS.register("nbt_change", NBTChangeRecipe.SerializerNBTChangeRecipe::new);
}
