package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.ItemConversion;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.registry.UHCGamerules;
import com.mrbysco.uhc.registry.UHCRegistries;
import com.mrbysco.uhc.util.UHCHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class ItemConversionHandler {

	@SubscribeEvent
	public static void onItemEntityTick(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Level level = player.level();
		if (UHCHelper.isUHCOnGoing(level)) {
			UHCSaveData saveData = UHCSaveData.get(level);

			for (int i = 0; i < player.getInventory().getContainerSize() - 4; ++i) {
				ItemStack stack = player.getInventory().getItem(i);
				int count = stack.getCount();
				if (!saveData.isNotchApples()) {
					if (!stack.isEmpty() && stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
						player.getInventory().removeItemNoUpdate(i);
						for (int j = 0; j < count; j++) {
							giveResult(player, new ItemStack(Blocks.GOLD_BLOCK, 8));
							giveResult(player, new ItemStack(Items.APPLE));
						}
					}
				} else if (!saveData.isLevel2Potions()) {
					if (!stack.isEmpty() && stack.getItem() == Items.GLOWSTONE_DUST) {
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
						if (!stack.isEmpty()) {
							ItemConversion conversion = getMatchingConversion(level.registryAccess(), stack);
							if (conversion != null) {
								for (ItemStack result : conversion.results()) {
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

	@SubscribeEvent
	public static void onItemEntityTick(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();
		Level level = entity.level();
		if (entity instanceof ItemEntity itemEntity && UHCHelper.isUHCOnGoing(level)) {
			UHCSaveData saveData = UHCSaveData.get(level);
			BlockPos pos = itemEntity.blockPosition();
			ItemStack stack = itemEntity.getItem();
			int count = stack.getCount();
			if (!saveData.isNotchApples()) {
				if (!stack.isEmpty() && stack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
					for (int j = 0; j < count; j++) {
						level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.GOLD_BLOCK, 8)));
						level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.APPLE)));
					}
					itemEntity.discard();
				}
			} else if (!saveData.isLevel2Potions()) {
				if (!stack.isEmpty() && stack.is(Items.GLOWSTONE_DUST)) {
					for (int j = 0; j < count; j++) {
						level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GLOWSTONE)));
					}
					itemEntity.discard();
				}
			} else if (!saveData.isRegenPotions()) {
				if (!stack.isEmpty() && stack.is(Items.GHAST_TEAR)) {
					for (int j = 0; j < count; j++) {
						level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GOLD_INGOT)));
					}
					itemEntity.discard();
				}
			} else {
				ItemConversion conversion = getMatchingConversion(level.registryAccess(), stack);
				if (conversion != null) {
					for (ItemStack result : conversion.results()) {
						for (int j = 0; j < count; j++) {
							level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), result.copy()));
						}
					}
					itemEntity.discard();
				}
			}

		}
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		if (!level.isClientSide && event.getEntity() instanceof ItemEntity itemEntity &&
				level.getGameRules().getBoolean(UHCGamerules.ITEM_CONVERSION) && UHCHelper.isUHCOnGoing(level)
		) {
			ItemConversion conversion = getMatchingConversion(level.registryAccess(), itemEntity.getItem());
			if (conversion != null) {
				BlockPos pos = itemEntity.blockPosition();
				ItemStack stack = itemEntity.getItem();
				int count = stack.getCount();

				for (ItemStack result : conversion.results()) {
					for (int j = 0; j < count; j++) {
						level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), result.copy()));
					}
				}
				itemEntity.discard();
			}
		}
	}

	@Nullable
	private static ItemConversion getMatchingConversion(RegistryAccess registryAccess, ItemStack stack) {
		Registry<ItemConversion> conversionRegistry = registryAccess.registryOrThrow(UHCRegistries.ITEM_CONVERSION_REGISTRY_KEY);
		for (ItemConversion conversion : conversionRegistry) {
			if (conversion.ingredient().test(stack)) {
				return conversion;
			}
		}
		return null;
	}


	private static void giveResult(Player player, ItemStack stack) {
		if (!stack.isEmpty()) {
			if (!player.getInventory().add(stack))
				player.spawnAtLocation(stack, 0.5F);
		}
	}
}
