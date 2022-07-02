package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.recipes.ConversionRecipe;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemConversionHandler {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			Player player = event.player;
			Level world = player.level;
			MinecraftServer server = world.getServer();
			ServerLevel overworld = server.getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);

				for (int i = 0; i < player.getInventory().getContainerSize() - 4; ++i) {
					ItemStack findStack = player.getInventory().getItem(i);
					int count = findStack.getCount();
					if (!saveData.isNotchApples()) {
						if (!findStack.isEmpty() && findStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
							player.getInventory().removeItemNoUpdate(i);
							for (int j = 0; j < count; j++) {
								giveResult(player, new ItemStack(Blocks.GOLD_BLOCK, 8));
								giveResult(player, new ItemStack(Items.APPLE));
							}
						}
					} else if (!saveData.isLevel2Potions()) {
						if (!findStack.isEmpty() && findStack.getItem() == Items.GLOWSTONE_DUST) {
							player.getInventory().removeItemNoUpdate(i);
							for (int j = 0; j < count; j++) {
								giveResult(player, new ItemStack(Blocks.GLOWSTONE));
							}
						}
					} else if (!saveData.isRegenPotions()) {
						player.getInventory().removeItemNoUpdate(i);
						for (int j = 0; j < count; j++) {
							giveResult(player, new ItemStack(Items.GOLD_INGOT));
						}
					} else {
						if (saveData.isItemConversion()) {
							List<ConversionRecipe> conversionRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipes.CONVERSION_RECIPE_TYPE.get());
							for (ConversionRecipe conversionRecipe : conversionRecipes) {
								if (!findStack.isEmpty() && conversionRecipe.getIngredients().get(0).test(findStack)) {
									for (ItemStack result : conversionRecipe.getResults()) {
										for (int j = 0; j < count; j++) {
											giveResult(player, result.copy());
										}
									}
									player.getInventory().removeItemNoUpdate(i);
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
			Level world = event.world;
			MinecraftServer server = world.getServer();
			ServerLevel overworld = server.getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				ArrayList<Entity> entityList = new ArrayList<>(((ServerLevel) world).getEntities(EntityType.ITEM, Entity::isAlive));
				for (Entity entity : entityList) {
					if (entity instanceof ItemEntity) {
						ItemEntity item = (ItemEntity) entity;
						BlockPos pos = item.blockPosition();
						ItemStack dropStack = item.getItem();
						int count = dropStack.getCount();
						if (!saveData.isNotchApples()) {
							if (!dropStack.isEmpty() && dropStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
								for (int j = 0; j < count; j++) {
									world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.GOLD_BLOCK, 8)));
									world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.APPLE)));
								}
								item.discard();
							}
						} else if (!saveData.isLevel2Potions()) {
							if (!dropStack.isEmpty() && dropStack.getItem() == Items.GLOWSTONE_DUST) {
								for (int j = 0; j < count; j++) {
									world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GLOWSTONE)));
								}
								item.discard();
							}
						} else if (!saveData.isRegenPotions()) {
							if (!dropStack.isEmpty() && dropStack.getItem() == Items.GHAST_TEAR) {
								for (int j = 0; j < count; j++) {
									world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GOLD_INGOT)));
								}
								item.discard();
							}
						} else {
							if (saveData.isItemConversion()) {
								List<ConversionRecipe> conversionRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipes.CONVERSION_RECIPE_TYPE.get());
								for (ConversionRecipe conversionRecipe : conversionRecipes) {
									if (!dropStack.isEmpty() && conversionRecipe.getIngredients().get(0).test(dropStack)) {
										for (ItemStack result : conversionRecipe.getResults()) {
											for (int j = 0; j < count; j++) {
												world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), result.copy()));
											}
										}
										item.discard();
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
		if (!event.getWorld().isClientSide && event.getEntity() instanceof ItemEntity) {
			ServerLevel overworld = event.getWorld().getServer().getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if (saveData.isItemConversion() && saveData.isUhcOnGoing() && event.getWorld().dimension().location().equals(saveData.getUHCDimension())) {
					Level world = event.getWorld();
					ItemEntity item = (ItemEntity) event.getEntity();
					BlockPos pos = item.blockPosition();
					ItemStack dropStack = item.getItem();
					int count = dropStack.getCount();

					List<ConversionRecipe> conversionRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipes.CONVERSION_RECIPE_TYPE.get());
					for (ConversionRecipe conversionRecipe : conversionRecipes) {
						if (!dropStack.isEmpty() && conversionRecipe.getIngredients().get(0).test(dropStack)) {
							for (ItemStack result : conversionRecipe.getResults()) {
								for (int j = 0; j < count; j++) {
									world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), result.copy()));
								}
							}
							item.discard();
						}
					}
				}
			}
		}
	}

	public void giveResult(Player player, ItemStack stack) {
		if (!stack.isEmpty()) {
			if (player.getInventory().getFreeSlot() != -1)
				player.getInventory().add(stack);
			else
				player.spawnAtLocation(stack, 0.5F);
		}
	}
}
