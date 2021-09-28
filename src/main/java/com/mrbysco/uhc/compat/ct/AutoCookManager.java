package com.mrbysco.uhc.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.mrbysco.uhc.recipes.AutoCookRecipe;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeGlobals.Global;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.spoiled.autocook")
public class AutoCookManager implements IRecipeManager {

	@Global("spoiling")
	public static final AutoCookManager INSTANCE = new AutoCookManager();

	private AutoCookManager() {
	}

	@Method
	public void addAutoCooking(String name, IIngredient ingredient, IItemStack output, float experience) {
		final ResourceLocation id = new ResourceLocation("crafttweaker", name);
		final Ingredient inputIngredient = ingredient.asVanillaIngredient();
		final ItemStack resultItemStack = output.getInternal();
		final AutoCookRecipe recipe = new AutoCookRecipe(id, "", inputIngredient, resultItemStack, experience);
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
	}

	@Override
	public IRecipeType getRecipeType() {
		return ModRecipes.AUTO_COOK_RECIPE_TYPE;
	}
}
