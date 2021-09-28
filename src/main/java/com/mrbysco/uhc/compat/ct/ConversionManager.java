package com.mrbysco.uhc.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.mrbysco.uhc.recipes.ConversionRecipe;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeGlobals.Global;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.spoiled.conversion")
public class ConversionManager implements IRecipeManager {

	@Global("conversion")
	public static final ConversionManager INSTANCE = new ConversionManager();

	private ConversionManager() {
	}

	@Method
	public void addConversion(String name, IIngredient ingredient, IItemStack[] outputs) {
		final ResourceLocation id = new ResourceLocation("crafttweaker", name);
		final Ingredient inputIngredient = ingredient.asVanillaIngredient();
		NonNullList<ItemStack> nonnulllist = NonNullList.create();
		for(IItemStack stack : outputs) {
			nonnulllist.add(stack.getInternal());
		}
		final ConversionRecipe recipe = new ConversionRecipe(id, "", inputIngredient, nonnulllist);
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
	}

	@Override
	public IRecipeType getRecipeType() {
		return ModRecipes.AUTO_COOK_RECIPE_TYPE;
	}
}
