package com.mrbysco.uhc.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class AutoCookRecipe implements Recipe<Container> {
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
	public RecipeType<?> getType() {
		return ModRecipes.AUTO_COOK_RECIPE_TYPE.get();
	}

	@Override
	public boolean matches(Container inv, Level worldIn) {
		return this.ingredient.test(inv.getItem(0));
	}

	public ItemStack assemble(Container inventory) {
		return this.result.copy();
	}

	public boolean canCraftInDimensions(int x, int y) {
		return false;
	}

	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}

	public ItemStack getResultItem() {
		return this.result;
	}

	public String getGroup() {
		return this.group;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.AUTO_COOK_SERIALIZER.get();
	}

	public static class SerializerAutoCookRecipe implements RecipeSerializer<AutoCookRecipe> {
		@Override
		public AutoCookRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			String s = GsonHelper.getAsString(json, "group", "");
			JsonElement jsonelement = (JsonElement) (GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient"));
			Ingredient ingredient = Ingredient.fromJson(jsonelement);
			//Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
			if (!json.has("result"))
				throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
			ItemStack itemstack;
			if (json.get("result").isJsonObject())
				itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			else {
				String s1 = GsonHelper.getAsString(json, "result");
				ResourceLocation resourcelocation = new ResourceLocation(s1);
				itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
					return new IllegalStateException("Item: " + s1 + " does not exist");
				}));
			}
			float f = GsonHelper.getAsFloat(json, "experience", 0.0F);
			return new AutoCookRecipe(recipeId, s, ingredient, itemstack, f);
		}

		@Nullable
		@Override
		public AutoCookRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			ItemStack itemstack = buffer.readItem();
			float experience = buffer.readFloat();
			return new AutoCookRecipe(recipeId, s, ingredient, itemstack, experience);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, AutoCookRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);
			buffer.writeItem(recipe.result);
			buffer.writeFloat(recipe.experience);
		}
	}
}