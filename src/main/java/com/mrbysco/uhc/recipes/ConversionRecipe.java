package com.mrbysco.uhc.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class ConversionRecipe implements IRecipe<IInventory> {
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
	public IRecipeType<?> getType() {
		return ModRecipes.CONVERSION_RECIPE_TYPE;
	}

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		return this.ingredient.test(inv.getStackInSlot(0));
	}

	public NonNullList<ItemStack> getResults() {
		return results;
	}

	public ItemStack getCraftingResult(IInventory inventory) {
		return getRecipeOutput().copy();
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
		return this.results.get(0);
	}

	public String getGroup() {
		return this.group;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.CONVERSION_SERIALIZER.get();
	}

	public static class SerializerConversionRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ConversionRecipe> {
		@Override
		public ConversionRecipe read(ResourceLocation recipeId, JsonObject json) {
			String s = JSONUtils.getString(json, "group", "");
			JsonElement jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
			Ingredient ingredient = Ingredient.deserialize(jsonelement);
			//Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
			if (!json.has("results")) throw new com.google.gson.JsonSyntaxException("Missing results, expected to find a string or object");
			NonNullList<ItemStack> nonnulllist = readItemStacks(JSONUtils.getJsonArray(json, "results"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No results for conversion recipe");
			} else if (nonnulllist.size() > 9) {
				throw new JsonParseException("Too many results for conversion recipe the max is " + 9);
			}
			return new ConversionRecipe(recipeId, s, ingredient, nonnulllist);
		}

		private static NonNullList<ItemStack> readItemStacks(JsonArray resultArray) {
			NonNullList<ItemStack> nonnulllist = NonNullList.create();

			for(int i = 0; i < resultArray.size(); ++i) {
				if(resultArray.get(i).isJsonObject()) {
					ItemStack stack = ShapedRecipe.deserializeItem(resultArray.get(i).getAsJsonObject());
					nonnulllist.add(stack);
				}
			}

			return nonnulllist;
		}

		@Nullable
		@Override
		public ConversionRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			String s = buffer.readString(32767);
			Ingredient ingredient = Ingredient.read(buffer);

			int size = buffer.readVarInt();
			NonNullList<ItemStack> resultList = NonNullList.withSize(size, ItemStack.EMPTY);
			for(int j = 0; j < resultList.size(); ++j) {
				resultList.set(j, buffer.readItemStack());
			}
			return new ConversionRecipe(recipeId, s, ingredient, resultList);
		}

		@Override
		public void write(PacketBuffer buffer, ConversionRecipe recipe) {
			buffer.writeString(recipe.group);
			recipe.ingredient.write(buffer);
			buffer.writeVarInt(recipe.results.size());
			for(ItemStack stack : recipe.results) {
				buffer.writeItemStack(stack);
			}
		}
	}
}