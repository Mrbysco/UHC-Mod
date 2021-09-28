package com.mrbysco.uhc.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class AutoCookRecipe implements IRecipe<IInventory> {
	protected final ResourceLocation id;
	protected final String group;
	protected final Ingredient ingredient;
	protected final ItemStack result;
	protected final float experience;

	public AutoCookRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack stack, float experience) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.result = stack;
		this.experience = experience;
	}

	public float getExperience() {
		return this.experience;
	}

	@Override
	public IRecipeType<?> getType() {
		return ModRecipes.AUTO_COOK_RECIPE_TYPE;
	}

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		return this.ingredient.test(inv.getStackInSlot(0));
	}

	public ItemStack getCraftingResult(IInventory inventory) {
		return this.result.copy();
	}

	public boolean canFit(int x, int y) {
		return false;
	}

	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}

	public ItemStack getRecipeOutput() {
		return this.result;
	}

	public String getGroup() {
		return this.group;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.AUTO_COOK_SERIALIZER.get();
	}

	public static class SerializerAutoCookRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AutoCookRecipe> {
		@Override
		public AutoCookRecipe read(ResourceLocation recipeId, JsonObject json) {
			String s = JSONUtils.getString(json, "group", "");
			JsonElement jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
			Ingredient ingredient = Ingredient.deserialize(jsonelement);
			//Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
			if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
			ItemStack itemstack;
			if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
			else {
				String s1 = JSONUtils.getString(json, "result");
				ResourceLocation resourcelocation = new ResourceLocation(s1);
				itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
					return new IllegalStateException("Item: " + s1 + " does not exist");
				}));
			}
			float f = JSONUtils.getFloat(json, "experience", 0.0F);
			return new AutoCookRecipe(recipeId, s, ingredient, itemstack, f);
		}

		@Nullable
		@Override
		public AutoCookRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			String s = buffer.readString(32767);
			Ingredient ingredient = Ingredient.read(buffer);
			ItemStack itemstack = buffer.readItemStack();
			float experience = buffer.readFloat();
			return new AutoCookRecipe(recipeId, s, ingredient, itemstack, experience);
		}

		@Override
		public void write(PacketBuffer buffer, AutoCookRecipe recipe) {
			buffer.writeString(recipe.group);
			recipe.ingredient.write(buffer);
			buffer.writeItemStack(recipe.result);
			buffer.writeFloat(recipe.experience);
		}
	}
}