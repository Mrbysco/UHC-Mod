package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.lists.ConversionList;
import com.Mrbysco.UHC.lists.info.ItemConversionInfo;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

			if (saveData.isNotchApples())
			{
				for (int i = 0; i < player.inventory.getSizeInventory()-4; ++i)
		        {
					ItemStack findStack = player.inventory.getStackInSlot(i);
		            Item item = findStack.getItem();
		            int meta = findStack.getMetadata();
		            if(!findStack.isEmpty() && item == Items.GOLDEN_APPLE && meta == 1)
		            {
		            	ItemStack itemstack = player.inventory.getStackInSlot(i);
		                ItemStack stack = itemstack.copy();
		                int count = stack.getCount();
		                
		                ItemStack gold_block = new ItemStack(Blocks.GOLD_BLOCK, 8);
		                ItemStack apple = new ItemStack(Items.APPLE, 1);
		                
		                giveResult(player, copyStack(gold_block), count);
						giveResult(player, copyStack(apple), count);
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
			            Item item = findStack.getItem();
			            int meta = findStack.getMetadata();
			            
			            if(!findStack.isEmpty() && item == info.getInput() && meta == info.getInputMeta()) {
			                ItemStack itemstack = player.inventory.getStackInSlot(i);
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
	
	public void giveResult(EntityPlayer player, ItemStack stack, int count)
	{
		if(!stack.isEmpty())
		{
			int originalCount = stack.getCount();
			int newCount = originalCount * count;
			
			stack.setCount(newCount);
			player.entityDropItem(stack, 0.5F);
		}
	}
}
