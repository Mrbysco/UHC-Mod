package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.lists.ConversionList;
import com.Mrbysco.UHC.lists.info.ItemConversionInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ItemConversionHandler {
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{			
			EntityPlayer player = (EntityPlayer) event.player;
			World world = player.world;
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			ArrayList<Entity> entityList = new ArrayList<>(world.getLoadedEntityList());
			
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
					for (Entity entity : entityList)
					{
						if(entity instanceof EntityItem)
						{
							EntityItem item = (EntityItem)entity;
							ItemStack dropStack = item.getItem();
							
							if(!dropStack.isEmpty() && dropStack.isItemEqual(info.getInput()))
				            {
				                ItemStack stack1 = info.getResult();
				                stack1.setCount(stack1.getCount() * dropStack.getCount());
				                ItemStack stack2 = info.getResult2();
				                stack2.setCount(stack2.getCount() * dropStack.getCount());
				                ItemStack stack3 = info.getResult3();
				                stack3.setCount(stack3.getCount() * dropStack.getCount());
				                ItemStack stack4 = info.getResult4();
				                stack4.setCount(stack4.getCount() * dropStack.getCount());
				                ItemStack stack5 = info.getResult5();
				                stack5.setCount(stack5.getCount() * dropStack.getCount());
				                ItemStack stack6 = info.getResult6();
				                stack6.setCount(stack6.getCount() * dropStack.getCount());
				                ItemStack stack7 = info.getResult7();
				                stack7.setCount(stack7.getCount() * dropStack.getCount());
				                ItemStack stack8 = info.getResult8();
				                stack8.setCount(stack8.getCount() * dropStack.getCount());
				                ItemStack stack9 = info.getResult9();
				                stack9.setCount(stack9.getCount() * dropStack.getCount());
				                
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
	
	public ItemStack copyStack(ItemStack stack)
	{
		if(stack == null)
			return ItemStack.EMPTY;
		else
			return stack.copy();
		
	}
	
	public void spawnResult(World world, EntityItem item, ItemStack stack)
	{
		if(stack != ItemStack.EMPTY && stack != null)
		{
			EntityItem resultItem = new EntityItem(world, item.posX, item.posY, item.posZ, stack);
	        world.spawnEntity(resultItem);
		}
	}
	
	public void giveResult(EntityPlayer player, ItemStack stack, int count)
	{
		if(!stack.isEmpty() && stack != ItemStack.EMPTY)
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
