package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.recipes.AutoCookRecipe;
import com.mrbysco.uhc.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AutoCookHandler {

	@SubscribeEvent
	public void onEntityJoin(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		if (!level.isClientSide && event.getEntity() instanceof ItemEntity itemEntity) {
			ServerLevel overworld = event.getLevel().getServer().overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if (saveData.isAutoCookEnabled() && saveData.isUhcOnGoing() && event.getLevel().dimension().location().equals(saveData.getUHCDimension())) {
					BlockPos pos = itemEntity.blockPosition();
					ItemStack stack = itemEntity.getItem();
					AutoCookRecipe foundRecipe = level.getRecipeManager().getRecipeFor(ModRecipes.AUTO_COOK_RECIPE_TYPE.get(),
							new SimpleContainer(stack), level).orElse(null);
					if (foundRecipe != null) {
						for (int i = 0; i < stack.getCount(); i++) {
							ItemEntity resultEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), foundRecipe.assemble(null));
							level.addFreshEntity(resultEntity);
						}

						float xpAmount = foundRecipe.getExperience() * stack.getCount();
						while (xpAmount > 0) {
							int i = ExperienceOrb.getExperienceValue((int) xpAmount);
							xpAmount -= i;
							level.addFreshEntity(new ExperienceOrb(level, pos.getX(), pos.getY(), pos.getZ(), i));
						}

						itemEntity.discard();
					}
				}
			}
		}
	}
}
