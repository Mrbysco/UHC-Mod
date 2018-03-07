package com.Mrbysco.UHC.handlers;

import java.util.List;
import java.util.Random;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.lists.CookList;
import com.Mrbysco.UHC.lists.info.AutoCookInfo;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoCookHandler {
	
	@SubscribeEvent
	public void onHarvestDrop(BlockEvent.HarvestDropsEvent event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		Random rand = world.rand;
		List<ItemStack> drops = event.getDrops();
		UHCSaveData saveData = UHCSaveData.getForWorld(world);

		if(saveData.isAutoCook() && !world.isRemote)
		{
			for(ItemStack drop : drops)
			{
				for(AutoCookInfo info : CookList.autoCookList)
				{
					 if(drop.getItem() == info.getInput())
					 {
						 ItemStack stack = new ItemStack(info.getResult(), drop.getCount(), info.getResultMeta());
						 drops.remove(drop);
						 drops.add(stack);

						 	float xpAmount = info.getExperience();
							if(xpAmount != 0)
							{
								int xp = 0;
								for (int i = 0; i < stack.getCount(); i++) {
									float random = rand.nextFloat();
									if(random <= xpAmount)
									{
										xp = xp + (int)Math.ceil(xpAmount);
									}
								}
								
								if(xp != 0)
								{
									EntityXPOrb exp = new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), xp);
									world.spawnEntity(exp);
								}
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
		UHCSaveData saveData = UHCSaveData.getForWorld(world);

		if(saveData.isAutoCook() && !world.isRemote)
		{
			for(EntityItem drop : drops)
			{
				for(AutoCookInfo info : CookList.autoCookList)
				{
					if(drop.getItem().getItem() == info.getInput())
					{
						ItemStack stack = drop.getItem();
						drop.setItem(new ItemStack(info.getResult(), stack.getCount(), info.getResultMeta()));
						 
						float xpAmount = info.getExperience();
						if(xpAmount != 0)
						{
							int xp = 0;
							for (int i = 0; i < stack.getCount(); i++) {
								float random = rand.nextFloat();
								if(random <= xpAmount)
								{
									xp = xp + (int)Math.ceil(xpAmount);
								}
							}
							
							if(xp != 0)
							{
								EntityXPOrb exp = new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), xp);
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
		UHCSaveData saveData = UHCSaveData.getForWorld(world);

		if(saveData.isAutoCook() && !world.isRemote)
		{
			for(AutoCookInfo info : CookList.autoCookList)
			{
				if(item.getItem().getItem() == info.getInput())
				{
					ItemStack stack = item.getItem();
					item.setItem(new ItemStack(info.getResult(), stack.getCount(), info.getResultMeta()));

					float xpAmount = info.getExperience();
					if(xpAmount != 0F)
					{					
						int xp = 0;
						for (int i = 0; i < stack.getCount(); i++) {
							float random = rand.nextFloat();
							if(random <= xpAmount)
							{
								xp = xp + (int)Math.ceil(xpAmount);
							}
						}
						
						if(xp != 0)
						{
							EntityXPOrb exp = new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), xp);
							world.spawnEntity(exp);
						}
					}
				}
			}
		}
	}
}
