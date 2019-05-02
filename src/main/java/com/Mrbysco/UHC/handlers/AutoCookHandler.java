package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.lists.CookList;
import com.Mrbysco.UHC.lists.info.AutoCookInfo;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Random;

public class AutoCookHandler {
	
	@SubscribeEvent
	public void onHarvestDrop(BlockEvent.HarvestDropsEvent event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		List<ItemStack> drops = event.getDrops();
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

			if(saveData.isAutoCook() && !world.isRemote)
			{
				for(ItemStack drop : drops)
				{
					for(AutoCookInfo info : CookList.autoCookList)
					{
						 if(drop.isItemEqual(info.getInput()))
						 {
							 ItemStack stack = info.getResult().copy();
							 stack.setCount(stack.getCount() * info.getResult().copy().getCount());
							 
							 float xpAmount = info.getExperience() * drop.getCount();
							 while (xpAmount > 0)
							 {
								 int i = EntityXPOrb.getXPSplit((int) xpAmount);
								 xpAmount -= i;
								 EntityXPOrb exp = new EntityXPOrb(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, i);
								 world.spawnEntity(exp);
							 }
							 	
							 drops.remove(drop);
							 drops.add(stack);
						 }
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingDrop(LivingDropsEvent event) {
		World world = event.getEntity().getEntityWorld();
		BlockPos pos = event.getEntity().getPosition();
		Random rand = world.rand;
		List<EntityItem> drops = event.getDrops();
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

			if(saveData.isAutoCook() && !world.isRemote)
			{
				for(EntityItem drop : drops)
				{
					for(AutoCookInfo info : CookList.autoCookList)
					{
						if(drop.getItem().isItemEqual(info.getInput()))
						{
							ItemStack stack = drop.getItem();
							ItemStack newStack = info.getResult().copy();
							newStack.setCount(stack.getCount() * info.getResult().copy().getCount());
							drop.setItem(newStack);
							 
							float xpAmount = info.getExperience() * stack.getCount();
							while (xpAmount > 0)
							{
								int i = EntityXPOrb.getXPSplit((int) xpAmount);
								xpAmount -= i;
								EntityXPOrb exp = new EntityXPOrb(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, i);
								world.spawnEntity(exp);
							}
						}
					}
				}	
			}
		}
	}
	
	@SubscribeEvent
	public void onToss(ItemTossEvent event) {
		World world = event.getEntity().getEntityWorld();
		BlockPos pos = event.getEntity().getPosition();
		Random rand = world.rand;
		EntityItem item = event.getEntityItem();
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

			if(saveData.isAutoCook() && !world.isRemote)
			{
				for(AutoCookInfo info : CookList.autoCookList)
				{
					if(item.getItem().isItemEqual(info.getInput()))
					{
						ItemStack stack = item.getItem();
						ItemStack newStack = info.getResult().copy();
						newStack.setCount(stack.getCount() * info.getResult().copy().getCount());
						item.setItem(newStack);
	
						float xpAmount = info.getExperience() * stack.getCount();
						while (xpAmount > 0)
						{
							int i = EntityXPOrb.getXPSplit((int) xpAmount);
							xpAmount -= i;
							EntityXPOrb exp = new EntityXPOrb(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, i);
							world.spawnEntity(exp);
						}
					}
				}
			}
		}
	}
}
