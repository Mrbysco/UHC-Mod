package com.mrbysco.uhc.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.core.NonNullList;
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

public class ConversionRecipe implements Recipe<Container> {
	protected final ResourceLocation id;
	protected final String group;
	protected final Ingredient ingredient;
	protected final NonNullList<ItemStack> results;

	public ConversionRecipe(ResourceLocation id, String group, Ingredient ingredient, NonNullList<ItemStack> stack) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.results = stack;
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipes.CONVERSION_RECIPE_TYPE.get();
	}

	@Override
	public boolean matches(Container inv, Level level) {
		return this.ingredient.test(inv.getItem(0));
	}

	public NonNullList<ItemStack> getResults() {
		return results;
	}

	public ItemStack assemble(Container inventory) {
		return getResultItem().copy();
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
		return this.results.get(0);
	}

	public String getGroup() {
		return this.group;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.CONVERSION_SERIALIZER.get();
	}

	public static class SerializerConversionRecipe implements RecipeSerializer<ConversionRecipe> {
		@Override
		public ConversionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			String s = GsonHelper.getAsString(json, "group", "");
			JsonElement jsonelement = (JsonElement) (GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient"));
			Ingredient ingredient = Ingredient.fromJson(jsonelement);
			//Forge: Check if primitive string to keep vanilla or an object which can contain a count field.
			if (!json.has("results"))
				throw new com.google.gson.JsonSyntaxException("Missing results, expected to find a string or object");
			NonNullList<ItemStack> nonnulllist = readItemStacks(GsonHelper.getAsJsonArray(json, "results"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No results for conversion recipe");
			} else if (nonnulllist.size() > 9) {
				throw new JsonParseException("Too many results for conversion recipe the max is " + 9);
			}
			return new ConversionRecipe(recipeId, s, ingredient, nonnulllist);
		}

		private static NonNullList<ItemStack> readItemStacks(JsonArray resultArray) {
			NonNullList<ItemStack> nonnulllist = NonNullList.create();

			for (int i = 0; i < resultArray.size(); ++i) {
				if (resultArray.get(i).isJsonObject()) {
					ItemStack stack = ShapedRecipe.itemStackFromJson(resultArray.get(i).getAsJsonObject());
					nonnulllist.add(stack);
				}
			}

			return nonnulllist;
		}

		@Nullable
		@Override
		public ConversionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);

			int size = buffer.readVarInt();
			NonNullList<ItemStack> resultList = NonNullList.withSize(size, ItemStack.EMPTY);
			for (int j = 0; j < resultList.size(); ++j) {
				resultList.set(j, buffer.readItem());
			}
			return new ConversionRecipe(recipeId, s, ingredient, resultList);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ConversionRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);
			buffer.writeVarInt(recipe.results.size());
			for (ItemStack stack : recipe.results) {
				buffer.writeItem(stack);
			}
		}
	}
}