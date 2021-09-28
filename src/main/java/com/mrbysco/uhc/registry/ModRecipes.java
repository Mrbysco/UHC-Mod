package com.mrbysco.uhc.registry;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.recipes.AutoCookRecipe;
import com.mrbysco.uhc.recipes.AutoCookRecipe.SerializerAutoCookRecipe;
import com.mrbysco.uhc.recipes.ConversionRecipe;
import com.mrbysco.uhc.recipes.ConversionRecipe.SerializerConversionRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

	public static final IRecipeType<AutoCookRecipe> AUTO_COOK_RECIPE_TYPE = IRecipeType.register(new ResourceLocation(Reference.MOD_ID, "auto_cook_recipe").toString());
	public static final IRecipeType<ConversionRecipe> CONVERSION_RECIPE_TYPE = IRecipeType.register(new ResourceLocation(Reference.MOD_ID, "conversion_recipe").toString());

	public static final RegistryObject<SerializerAutoCookRecipe> AUTO_COOK_SERIALIZER = RECIPE_SERIALIZERS.register("auto_cook_recipe", SerializerAutoCookRecipe::new);
	public static final RegistryObject<SerializerConversionRecipe> CONVERSION_SERIALIZER = RECIPE_SERIALIZERS.register("conversion_recipe", SerializerConversionRecipe::new);
}
