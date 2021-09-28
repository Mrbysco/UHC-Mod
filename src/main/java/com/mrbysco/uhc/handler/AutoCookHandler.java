package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.recipes.AutoCookRecipe;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class AutoCookHandler {

	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent event) {
		if(!event.getWorld().isRemote && event.getEntity() instanceof ItemEntity) {
			ServerWorld overworld = event.getWorld().getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if(saveData.isAutoCook() && saveData.isUhcOnGoing() && event.getWorld().getDimensionKey().getLocation().equals(saveData.getUHCDimension())) {
					handleSmelting(event.getWorld(), (ItemEntity) event.getEntity());
				}
			}
		}
	}

	public void handleSmelting(World world, ItemEntity itemEntity) {
		ItemStack stack = itemEntity.getItem();
		BlockPos pos = itemEntity.getPosition();
		List<AutoCookRecipe> recipes = world.getRecipeManager().getRecipesForType(ModRecipes.AUTO_COOK_RECIPE_TYPE);
		for(AutoCookRecipe recipe : recipes) {
			if(recipe.getIngredients().get(0).test(stack)) {
				for(int i = 0; i < stack.getCount(); i++) {
					ItemEntity resultEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), recipe.getCraftingResult(null));
					world.addEntity(resultEntity);
				}

				float xpAmount = recipe.getExperience() * stack.getCount();
				while (xpAmount > 0) {
					int i = ExperienceOrbEntity.getXPSplit((int) xpAmount);
					xpAmount -= i;
					world.addEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), i));
				}

				itemEntity.remove();
				break;
			}
		}
	}
}
