package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.lists.ConversionList;
import com.Mrbysco.UHC.lists.info.ItemConversionInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ItemConversionHandler {
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = (EntityPlayer) event.player;
			World world = player.world;
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				
				if (!saveData.isNotchApples())
				{
					for (int i = 0; i < player.inventory.getSizeInventory()-4; ++i)
			        {
						ItemStack findStack = player.inventory.getStackInSlot(i);
			            if(!findStack.isEmpty() && findStack.getItem() == Items.GOLDEN_APPLE && findStack.getMetadata() == 1)
			            {
			            	ItemStack itemstack = player.inventory.getStackInSlot(i);
			                ItemStack stack = itemstack.copy();
			                int count = stack.getCount();
			                
			                ItemStack gold_block = new ItemStack(Blocks.GOLD_BLOCK, 8);
			                ItemStack apple = new ItemStack(Items.APPLE, 1);
			                
							player.inventory.removeStackFromSlot(i);
	
			                giveResult(player, copyStack(gold_block), count);
							giveResult(player, copyStack(apple), count);
			            }   
					}
				}
				
				if (!saveData.isLevel2Potions())
				{
					for (int i = 0; i < player.inventory.getSizeInventory()-4; ++i)
			        {
						ItemStack findStack = player.inventory.getStackInSlot(i);
			            if(!findStack.isEmpty() && findStack.getItem() == Items.GLOWSTONE_DUST)
			            {
			            	ItemStack itemstack = player.inventory.getStackInSlot(i);
			                ItemStack stack = itemstack.copy();
			                int count = stack.getCount();
			                
			                ItemStack glowstoneBlock = new ItemStack(Blocks.GLOWSTONE);
			                
							player.inventory.removeStackFromSlot(i);
	
							giveResult(player, copyStack(glowstoneBlock), count);
			            }
					}
				}
				
				if (!saveData.isRegenPotions())
				{
					for (int i = 0; i < player.inventory.getSizeInventory()-4; ++i)
					{
						ItemStack findStack = player.inventory.getStackInSlot(i);
						if(!findStack.isEmpty() && findStack.getItem() == Items.GHAST_TEAR)
						{
							ItemStack itemstack = player.inventory.getStackInSlot(i);
							ItemStack stack = itemstack.copy();
							int count = stack.getCount();
							
							ItemStack goldIngot = new ItemStack(Items.GOLD_INGOT);
							
							player.inventory.removeStackFromSlot(i);
							
							giveResult(player, copyStack(goldIngot), count);
						}
					}
				}
				
				if (saveData.isItemConversion())
				{
					for (ItemConversionInfo info : ConversionList.conversionList)
					{
						for (int i = 0; i < player.inventory.getSizeInventory()-4; ++i)
				        {
				            ItemStack findStack = player.inventory.getStackInSlot(i);
				            
				            if(!findStack.isEmpty() && findStack.isItemEqualIgnoreDurability(info.getInput())) {
				                ItemStack itemstack = findStack.copy();
				                ItemStack stack = itemstack.copy();
				                int count = stack.getCount();
								giveResult(player, copyStack(info.getResult()), count);
								giveResult(player, copyStack(info.getResult2()), count);
								giveResult(player, copyStack(info.getResult3()), count);
								giveResult(player, copyStack(info.getResult4()), count);
								giveResult(player, copyStack(info.getResult5()), count);
								giveResult(player, copyStack(info.getResult6()), count);
								giveResult(player, copyStack(info.getResult7()), count);
								giveResult(player, copyStack(info.getResult8()), count);
								giveResult(player, copyStack(info.getResult9()), count);
								player.inventory.removeStackFromSlot(i);
				            }
				        }
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{			
			World world = event.world;
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				ArrayList<Entity> entityList = new ArrayList<>(world.loadedEntityList);	
				
				if (!saveData.isNotchApples())
				{
					for (Entity entity : entityList)
					{
						if(entity instanceof EntityItem)
						{
							EntityItem item = (EntityItem)entity;
							ItemStack dropStack = item.getItem();
							
							if(!dropStack.isEmpty() && dropStack.getItem() == Items.GOLDEN_APPLE && dropStack.getMetadata() == 1)
				            {
				                ItemStack gold_block = new ItemStack(Blocks.GOLD_BLOCK, (8 * dropStack.getCount()));
				                ItemStack apple = new ItemStack(Items.APPLE, (1 * dropStack.getCount()));
				                
				                EntityItem item1 = new EntityItem(world, item.posX, item.posY, item.posZ, gold_block);
				                EntityItem item2 = new EntityItem(world, item.posX, item.posY, item.posZ, apple);
				                world.removeEntity(item);
				                world.spawnEntity(item1);
								world.spawnEntity(item2);
				            }   
						}
					}
				}
				
				if (!saveData.isLevel2Potions())
				{
					for (Entity entity : entityList)
					{
						if(entity instanceof EntityItem)
						{
							EntityItem item = (EntityItem)entity;
							ItemStack dropStack = item.getItem();
							
							if(!dropStack.isEmpty() && dropStack.getItem() == Items.GLOWSTONE_DUST)
				            {
				                ItemStack glowstoneBlock = new ItemStack(Blocks.GLOWSTONE, (1 * dropStack.getCount()));
				                
				                EntityItem item1 = new EntityItem(world, item.posX, item.posY, item.posZ, glowstoneBlock);
				                world.removeEntity(item);
				                world.spawnEntity(item1);
				            }   
						}
					}
				}
				
				if (!saveData.isRegenPotions())
				{
					for (Entity entity : entityList)
					{
						if(entity instanceof EntityItem)
						{
							EntityItem item = (EntityItem)entity;
							ItemStack dropStack = item.getItem();
							
							if(!dropStack.isEmpty() && dropStack.getItem() == Items.GHAST_TEAR)
				            {
				                ItemStack goldIngot = new ItemStack(Items.GOLD_INGOT, (1 * dropStack.getCount()));
				                
				                EntityItem item1 = new EntityItem(world, item.posX, item.posY, item.posZ, goldIngot);
				                world.removeEntity(item);
				                world.spawnEntity(item1);
				            }   
						}
					}
				}
				
				if (saveData.isItemConversion())
				{
					for (ItemConversionInfo info : ConversionList.conversionList)
					{
						for (Entity entity : entityList)
						{
							if(entity instanceof EntityItem)
							{
								EntityItem item = (EntityItem)entity;
								ItemStack dropStack = item.getItem();
								
								if(!dropStack.isEmpty() && dropStack.isItemEqual(info.getInput()))
					            {
									int count = dropStack.copy().getCount();
					                ItemStack stack1 = info.getResult().copy();
					                stack1.setCount(info.getResult().getCount() * count);
					                ItemStack stack2 = info.getResult2().copy();
					                stack2.setCount(info.getResult2().getCount() * count);
					                ItemStack stack3 = info.getResult3().copy();
					                stack3.setCount(info.getResult3().getCount() * count);
					                ItemStack stack4 = info.getResult4().copy();
					                stack4.setCount(info.getResult4().getCount() * count);
					                ItemStack stack5 = info.getResult5().copy();
					                stack5.setCount(info.getResult5().getCount() * count);
					                ItemStack stack6 = info.getResult6().copy();
					                stack6.setCount(info.getResult6().getCount() * count);
					                ItemStack stack7 = info.getResult7().copy();
					                stack7.setCount(info.getResult7().getCount() * count);
					                ItemStack stack8 = info.getResult8().copy();
					                stack8.setCount(info.getResult8().getCount() * count);
					                ItemStack stack9 = info.getResult9().copy();
					                stack9.setCount(info.getResult9().getCount() * count);
					                
					                world.removeEntity(item);
					                spawnResult(world, item, stack1);
					                spawnResult(world, item, stack2);
					                spawnResult(world, item, stack3);
					                spawnResult(world, item, stack4);
					                spawnResult(world, item, stack5);
					                spawnResult(world, item, stack6);
					                spawnResult(world, item, stack7);
					                spawnResult(world, item, stack8);
					                spawnResult(world, item, stack9);
					            }   
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
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

			if(saveData.isAutoCook() && !world.isRemote)
			{
				for(EntityItem drop : drops)
				{
					for(ItemConversionInfo info : ConversionList.conversionList)
					{
						if(drop.getItem().isItemEqual(info.getInput()))
						{
							int count = drop.getItem().copy().getCount();
							if(!info.getResult().isEmpty())
							{
								ItemStack stack1 = info.getResult().copy();
								int newCount = info.getResult().getCount() * count;
								stack1.setCount(newCount);
								drop.setItem(stack1);
							}
							if(!info.getResult2().isEmpty())
							{
								ItemStack stack2 = info.getResult2().copy();
								stack2.setCount(info.getResult2().getCount() * count);
								drops.add(new EntityItem(world, drop.posX, drop.posY, drop.posZ, stack2));
							}
							if(!info.getResult3().isEmpty())
							{
								ItemStack stack3 = info.getResult3().copy();
								stack3.setCount(info.getResult3().getCount() * count);
								drops.add(new EntityItem(world, drop.posX, drop.posY, drop.posZ, stack3));
							}
							if(!info.getResult4().isEmpty())
							{
								ItemStack stack4 = info.getResult4().copy();
								stack4.setCount(info.getResult4().getCount() * count);
								drops.add(new EntityItem(world, drop.posX, drop.posY, drop.posZ, stack4));
							}
							if(!info.getResult5().isEmpty())
							{
								ItemStack stack5 = info.getResult5().copy();
								stack5.setCount(info.getResult5().getCount() * count);
								drops.add(new EntityItem(world, drop.posX, drop.posY, drop.posZ, stack5));
							}
							if(!info.getResult6().isEmpty())
							{
								ItemStack stack6 = info.getResult6().copy();
								stack6.setCount(info.getResult6().getCount() * count);
								drops.add(new EntityItem(world, drop.posX, drop.posY, drop.posZ, stack6));
							}
							if(!info.getResult7().isEmpty())
							{
								ItemStack stack7 = info.getResult7().copy();
								stack7.setCount(info.getResult7().getCount() * count);
								drops.add(new EntityItem(world, drop.posX, drop.posY, drop.posZ, stack7));
							}
							if(!info.getResult8().isEmpty())
							{
								ItemStack stack8 = info.getResult8().copy();
								stack8.setCount(info.getResult8().getCount() * count);
								drops.add(new EntityItem(world, drop.posX, drop.posY, drop.posZ, stack8));
							}
							if(!info.getResult9().isEmpty())
							{
								ItemStack stack9 = info.getResult9().copy();
								stack9.setCount(info.getResult9().getCount() * count);
								drops.add(new EntityItem(world, drop.posX, drop.posY, drop.posZ, stack9));
							}
						}
					}
				}	
			}
		}
	}
	
	public ItemStack copyStack(ItemStack stack)
	{
		if(stack == null)
			return ItemStack.EMPTY;
		else
			return stack.copy();
		
	}
	
	public void spawnResult(World world, EntityItem item, ItemStack stack)
	{
		if(!stack.isEmpty() && stack != null)
		{
			EntityItem resultItem = new EntityItem(world, item.posX, item.posY, item.posZ, stack);
	        world.spawnEntity(resultItem);
		}
	}
	
	public void giveResult(EntityPlayer player, ItemStack stack, int count)
	{
		if(!stack.isEmpty() && stack != null)
		{
			int originalCount = stack.getCount();
			int newCount = originalCount * count;
			
			stack.setCount(newCount);
			if(player.inventory.getFirstEmptyStack() != -1)
				player.inventory.addItemStackToInventory(stack);
			else
				player.entityDropItem(stack, 0.5F);
		}
	}
}
