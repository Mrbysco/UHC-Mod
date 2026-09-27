package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.AutoSmelt;
import com.mrbysco.uhc.registry.UHCGamerules;
import com.mrbysco.uhc.registry.UHCRegistries;
import com.mrbysco.uhc.util.UHCHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class AutoCookHandler {
	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent event) {
		Level level = event.getLevel();
		if (!level.isClientSide && event.getEntity() instanceof ItemEntity itemEntity &&
				level.getGameRules().getBoolean(UHCGamerules.AUTO_COOK) && UHCHelper.isUHCOnGoing(level)
		) {
			AutoSmelt autoSmelt = getMatchingSmelt(level.registryAccess(), itemEntity.getItem());
			if (autoSmelt != null) {
				BlockPos pos = itemEntity.blockPosition();
				ItemStack stack = itemEntity.getItem();
				for (int i = 0; i < stack.getCount(); i++) {
					ItemEntity resultEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), autoSmelt.result().copy());
					level.addFreshEntity(resultEntity);
				}

				float xpAmount = autoSmelt.experience() * stack.getCount();
				while (xpAmount > 0) {
					int i = ExperienceOrb.getExperienceValue((int) xpAmount);
					xpAmount -= i;
					level.addFreshEntity(new ExperienceOrb(level, pos.getX(), pos.getY(), pos.getZ(), i));
				}

				itemEntity.discard();
			}
		}
	}

	@Nullable
	private static AutoSmelt getMatchingSmelt(RegistryAccess registryAccess, ItemStack stack) {
		Registry<AutoSmelt> smeltRegistry = registryAccess.registryOrThrow(UHCRegistries.AUTO_SMELT_REGISTRY_KEY);
		for (AutoSmelt autoSmelt : smeltRegistry) {
			if (autoSmelt.ingredient().test(stack)) {
				return autoSmelt;
			}
		}
		return null;
	}
}
