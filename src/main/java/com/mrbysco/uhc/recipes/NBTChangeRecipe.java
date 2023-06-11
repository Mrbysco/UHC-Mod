package com.mrbysco.uhc.recipes;

import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.uhc.UltraHardCoremod;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class NBTChangeRecipe implements Recipe<Container> {
	protected final ResourceLocation id;
	protected final ResourceLocation entityType;
	protected final String nbtChange;

	public NBTChangeRecipe(ResourceLocation id, ResourceLocation entity, String nbtChange) {
		this.id = id;
		this.entityType = entity;
		this.nbtChange = nbtChange;
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipes.NBT_CHANGE_TYPE.get();
	}

	@Override
	public boolean matches(Container inv, Level level) {
		return false;
	}

	public ItemStack assemble(Container inventory, RegistryAccess access) {
		return getResultItem(access).copy();
	}

	public boolean canCraftInDimensions(int x, int y) {
		return false;
	}

	public ItemStack getResultItem(RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	public ResourceLocation getEntityType() {
		return entityType;
	}

	public String getNbtChange() {
		return nbtChange;
	}

	public CompoundTag getNBTTag() {
		CompoundTag tag = new CompoundTag();
		try {
			if (nbtChange.startsWith("{") && nbtChange.endsWith("}")) {
				tag = TagParser.parseTag(nbtChange);
			} else {
				tag = TagParser.parseTag("{" + nbtChange + "}");
			}
		} catch (CommandSyntaxException nbtexception) {
			UltraHardCoremod.LOGGER.error("nope... " + nbtexception.getMessage());
		}

		return tag;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.NBT_CHANGE.get();
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public static class SerializerNBTChangeRecipe implements RecipeSerializer<NBTChangeRecipe> {
		@Override
		public NBTChangeRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
			String entity = GsonHelper.getAsString(jsonObject, "entityType");
			ResourceLocation entityType = ResourceLocation.tryParse(entity);
			if (!ForgeRegistries.ENTITY_TYPES.containsKey(entityType)) {
				throw new IllegalStateException("EntityType: " + entity + " does not exist");
			}
			String nbt = GsonHelper.getAsString(jsonObject, "nbt");
			return new NBTChangeRecipe(recipeId, entityType, nbt);
		}

		@Nullable
		@Override
		public NBTChangeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			ResourceLocation entityType = buffer.readResourceLocation();
			String nbtChange = buffer.readUtf(32767);
			return new NBTChangeRecipe(recipeId, entityType, nbtChange);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, NBTChangeRecipe recipe) {
			buffer.writeResourceLocation(recipe.entityType);
			buffer.writeUtf(recipe.nbtChange);
		}
	}
}