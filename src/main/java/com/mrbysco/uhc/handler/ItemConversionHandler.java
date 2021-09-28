package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.recipes.ConversionRecipe;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemConversionHandler {
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			PlayerEntity player = event.player;
			World world = player.world;
			MinecraftServer server = world.getServer();
			ServerWorld overworld = server.getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);

				for (int i = 0; i < player.inventory.getSizeInventory() - 4; ++i) {
					ItemStack findStack = player.inventory.getStackInSlot(i);
					int count = findStack.getCount();
					if (!saveData.isNotchApples()) {
						if (!findStack.isEmpty() && findStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
							player.inventory.removeStackFromSlot(i);
							for(int j = 0; j < count; j++) {
								giveResult(player, new ItemStack(Blocks.GOLD_BLOCK, 8));
								giveResult(player, new ItemStack(Items.APPLE));
							}
						}
					} else if (!saveData.isLevel2Potions()) {
						if (!findStack.isEmpty() && findStack.getItem() == Items.GLOWSTONE_DUST) {
							player.inventory.removeStackFromSlot(i);
							for(int j = 0; j < count; j++) {
								giveResult(player, new ItemStack(Blocks.GLOWSTONE));
							}
						}
					} else if (!saveData.isRegenPotions()) {
						player.inventory.removeStackFromSlot(i);
						for(int j = 0; j < count; j++) {
							giveResult(player, new ItemStack(Items.GOLD_INGOT));
						}
					} else {
						if (saveData.isItemConversion()) {
							List<ConversionRecipe> conversionRecipes = world.getRecipeManager().getRecipesForType(ModRecipes.CONVERSION_RECIPE_TYPE);
							for (ConversionRecipe conversionRecipe : conversionRecipes) {
								if (!findStack.isEmpty() && conversionRecipe.getIngredients().get(0).test(findStack)) {
									for(ItemStack result : conversionRecipe.getResults()) {
										for(int j = 0; j < count; j++) {
											giveResult(player, result.copy());
										}
									}
									player.inventory.removeStackFromSlot(i);
								}
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			World world = event.world;
			MinecraftServer server = world.getServer();
			ServerWorld overworld = server.getWorld(World.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				ArrayList<Entity> entityList = new ArrayList<>(((ServerWorld) world).getEntities(EntityType.ITEM, Entity::isAlive));
				for (Entity entity : entityList) {
					if (entity instanceof ItemEntity) {
						ItemEntity item = (ItemEntity) entity;
						BlockPos pos = item.getPosition();
						ItemStack dropStack = item.getItem();
						int count = dropStack.getCount();
						if (!saveData.isNotchApples()) {
							if (!dropStack.isEmpty() && dropStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
								for (int j = 0; j < count; j++) {
									world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.GOLD_BLOCK, 8)));
									world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.APPLE)));
								}
								item.remove();
							}
						} else if (!saveData.isLevel2Potions()) {
							if (!dropStack.isEmpty() && dropStack.getItem() == Items.GLOWSTONE_DUST) {
								for (int j = 0; j < count; j++) {
									world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GLOWSTONE)));
								}
								item.remove();
							}
						} else if (!saveData.isRegenPotions()) {
							if (!dropStack.isEmpty() && dropStack.getItem() == Items.GHAST_TEAR) {
								for (int j = 0; j < count; j++) {
									world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GOLD_INGOT)));
								}
								item.remove();
							}
						} else {
							if (saveData.isItemConversion()) {
								List<ConversionRecipe> conversionRecipes = world.getRecipeManager().getRecipesForType(ModRecipes.CONVERSION_RECIPE_TYPE);
								for (ConversionRecipe conversionRecipe : conversionRecipes) {
									if (!dropStack.isEmpty() && conversionRecipe.getIngredients().get(0).test(dropStack)) {
										for (ItemStack result : conversionRecipe.getResults()) {
											for (int j = 0; j < count; j++) {
												world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), result.copy()));
											}
										}
										item.remove();
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent event) {
		if(!event.getWorld().isRemote && event.getEntity() instanceof ItemEntity) {
			ServerWorld overworld = event.getWorld().getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if(saveData.isItemConversion() && saveData.isUhcOnGoing() && event.getWorld().getDimensionKey().getLocation().equals(saveData.getUHCDimension())) {
					World world = event.getWorld();
					ItemEntity item = (ItemEntity) event.getEntity();
					BlockPos pos = item.getPosition();
					ItemStack dropStack = item.getItem();
					int count = dropStack.getCount();

					List<ConversionRecipe> conversionRecipes = world.getRecipeManager().getRecipesForType(ModRecipes.CONVERSION_RECIPE_TYPE);
					for (ConversionRecipe conversionRecipe : conversionRecipes) {
						if (!dropStack.isEmpty() && conversionRecipe.getIngredients().get(0).test(dropStack)) {
							for (ItemStack result : conversionRecipe.getResults()) {
								for (int j = 0; j < count; j++) {
									world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), result.copy()));
								}
							}
							item.remove();
						}
					}
				}
			}
		}
	}
	
	public void giveResult(PlayerEntity player, ItemStack stack) {
		if(!stack.isEmpty()) {
			if(player.inventory.getFirstEmptyStack() != -1)
				player.inventory.addItemStackToInventory(stack);
			else
				player.entityDropItem(stack, 0.5F);
		}
	}
}
