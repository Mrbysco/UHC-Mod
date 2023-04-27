package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.recipes.ConversionRecipe;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class ItemConversionHandler {

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			Player player = event.player;
			Level level = player.level;
			MinecraftServer server = level.getServer();
			ServerLevel overworld = server.overworld();
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
							if (!findStack.isEmpty()) {
								ConversionRecipe conversionRecipe = level.getRecipeManager().getRecipeFor(ModRecipes.CONVERSION_RECIPE_TYPE.get(),
										new SimpleContainer(findStack), level).orElse(null);
								if (conversionRecipe != null) {
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
	public void onWorldTick(TickEvent.LevelTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			Level level = event.level;
			MinecraftServer server = level.getServer();
			ServerLevel overworld = server.overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				ArrayList<Entity> entityList = new ArrayList<>(((ServerLevel) level).getEntities(EntityType.ITEM, Entity::isAlive));
				for (Entity entity : entityList) {
					if (entity instanceof ItemEntity item) {
						BlockPos pos = item.blockPosition();
						ItemStack dropStack = item.getItem();
						int count = dropStack.getCount();
						if (!saveData.isNotchApples()) {
							if (!dropStack.isEmpty() && dropStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
								for (int j = 0; j < count; j++) {
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.GOLD_BLOCK, 8)));
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.APPLE)));
								}
								item.discard();
							}
						} else if (!saveData.isLevel2Potions()) {
							if (!dropStack.isEmpty() && dropStack.getItem() == Items.GLOWSTONE_DUST) {
								for (int j = 0; j < count; j++) {
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GLOWSTONE)));
								}
								item.discard();
							}
						} else if (!saveData.isRegenPotions()) {
							if (!dropStack.isEmpty() && dropStack.getItem() == Items.GHAST_TEAR) {
								for (int j = 0; j < count; j++) {
									level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GOLD_INGOT)));
								}
								item.discard();
							}
						} else {
							if (saveData.isItemConversion()) {
								ConversionRecipe conversionRecipe = level.getRecipeManager().getRecipeFor(ModRecipes.CONVERSION_RECIPE_TYPE.get(),
										new SimpleContainer(dropStack), level).orElse(null);
								if (conversionRecipe != null) {
									for (ItemStack result : conversionRecipe.getResults()) {
										for (int j = 0; j < count; j++) {
											level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), result.copy()));
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

	@SubscribeEvent
	public void onEntityJoin(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		if (!level.isClientSide && event.getEntity() instanceof ItemEntity) {
			ServerLevel overworld = level.getServer().overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if (saveData.isItemConversion() && saveData.isUhcOnGoing() && level.dimension().location().equals(saveData.getUHCDimension())) {
					ItemEntity item = (ItemEntity) event.getEntity();
					BlockPos pos = item.blockPosition();
					ItemStack dropStack = item.getItem();
					int count = dropStack.getCount();

					ConversionRecipe conversionRecipe = level.getRecipeManager().getRecipeFor(ModRecipes.CONVERSION_RECIPE_TYPE.get(),
							new SimpleContainer(dropStack), level).orElse(null);
					if (conversionRecipe != null) {
						for (ItemStack result : conversionRecipe.getResults()) {
							for (int j = 0; j < count; j++) {
								level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), result.copy()));
							}
						}
						item.discard();
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
