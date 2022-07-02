package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.recipes.AutoCookRecipe;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class AutoCookHandler {

	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent event) {
		if (!event.getWorld().isClientSide && event.getEntity() instanceof ItemEntity) {
			ServerLevel overworld = event.getWorld().getServer().getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if (saveData.isAutoCook() && saveData.isUhcOnGoing() && event.getWorld().dimension().location().equals(saveData.getUHCDimension())) {
					handleSmelting(event.getWorld(), (ItemEntity) event.getEntity());
				}
			}
		}
	}

	public void handleSmelting(Level world, ItemEntity itemEntity) {
		ItemStack stack = itemEntity.getItem();
		BlockPos pos = itemEntity.blockPosition();
		List<AutoCookRecipe> recipes = world.getRecipeManager().getAllRecipesFor(ModRecipes.AUTO_COOK_RECIPE_TYPE.get());
		for (AutoCookRecipe recipe : recipes) {
			if (recipe.getIngredients().get(0).test(stack)) {
				for (int i = 0; i < stack.getCount(); i++) {
					ItemEntity resultEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), recipe.assemble(null));
					world.addFreshEntity(resultEntity);
				}

				float xpAmount = recipe.getExperience() * stack.getCount();
				while (xpAmount > 0) {
					int i = ExperienceOrb.getExperienceValue((int) xpAmount);
					xpAmount -= i;
					world.addFreshEntity(new ExperienceOrb(world, pos.getX(), pos.getY(), pos.getZ(), i));
				}

				itemEntity.discard();
				break;
			}
		}
	}
}
