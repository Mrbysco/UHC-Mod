package com.Mrbysco.UHC.handlers;

import java.util.List;

import com.Mrbysco.UHC.init.ModItems;
import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class UHCHandler {
	
	@SubscribeEvent
	public void UHCBookEvent(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			ItemStack bookStack = new ItemStack(ModItems.uhc_book);
			ItemStack editStack = new ItemStack(Items.LEAD);
			editStack.addEnchantment(Enchantments.BINDING_CURSE, 1);;
			editStack.addEnchantment(Enchantments.VANISHING_CURSE, 1);;
			editStack.setStackDisplayName("Editors Monacle");
			editStack.setTagInfo("lore", new NBTTagString("You have the power to edit the main UHC settings"));
			
			UHCSaveData saveData = UHCSaveData.getForWorld(world);

			List<Entity> entityList = world.getLoadedEntityList();
			
			for(Entity entity : entityList)
			{
				if(entity instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) entity;
					NBTTagCompound entityData = player.getEntityData();

					if (!world.isRemote && entityData.hasKey("canEditUHC") && saveData.isUhcOnGoing() == false)
					{
						if (player.inventory.getStackInSlot(39) == editStack)
						{
							return;
						}
						if (player.inventory.getStackInSlot(39).isEmpty())
						{
							player.inventory.setInventorySlotContents(39, editStack);

						}
					}
					
					if (!world.isRemote && !player.inventory.hasItemStack(bookStack) && (saveData.isUhcOnGoing() == false))
					{
						player.inventory.addItemStackToInventory(bookStack);
					}
					
					if (!world.isRemote && player.inventory.hasItemStack(bookStack) && (saveData.isUhcOnGoing() == true))
					{
						int slot = player.inventory.getSlotFor(bookStack);
						if (slot != -1)
						{
							player.inventory.removeStackFromSlot(slot);
						}
					}
				}
				
				if (entity instanceof EntityItem) {
					EntityItem itemEntity = (EntityItem) entity;
					ItemStack stack = itemEntity.getItem();
					if(stack.getItem() == ModItems.uhc_book)
					{
						world.removeEntity(itemEntity);
					}
				}
			}
		}
	}

	@SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			NBTTagCompound entityData = player.getEntityData();
				
			if (!entityData.hasKey("canEditUHC"))
			{
				if (!event.getWorld().isRemote) {
					entityData.setBoolean("canEditUHC", false);
				}
			}
			if (player.canUseCommand(2, "")) {
				if (!event.getWorld().isRemote) {
					entityData.setBoolean("canEditUHC", true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onNewPlayerJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event)
	{
		EntityPlayer player = event.player;
		UHCSaveData saveData = UHCSaveData.getForWorld(player.world);

		if (saveData.isUhcOnGoing())
		{
			player.setGameType(GameType.SPECTATOR);
		}
	}
	
	@SubscribeEvent
	public void onPlayerGivePermission(LivingHurtEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			Entity source = event.getSource().getTrueSource();
			EntityPlayer player = (EntityPlayer) event.getEntity();
			World world = player.world;
			NBTTagCompound entityData = player.getEntityData();
			
			if(source instanceof EntityPlayer)
			{
				EntityPlayer sourcePlayer = (EntityPlayer) source;
				if (sourcePlayer.canUseCommand(2, "")) {
					if (!world.isRemote) {
						if(!player.canUseCommand(2, ""))
						{
							if(entityData.hasKey("canEditUHC"))
								entityData.setBoolean("canEditUHC", false);
							else
								entityData.setBoolean("canEditUHC", true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerPermissionClone(PlayerEvent.Clone event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer originalPlayer = event.getOriginal();
			EntityPlayer newPlayer = event.getEntityPlayer();

			UHCSaveData saveData = UHCSaveData.getForWorld(newPlayer.world);

			NBTTagCompound originalData = originalPlayer.getEntityData();
			NBTTagCompound newData = newPlayer.getEntityData();
			
			if(saveData.isUhcOnGoing()) 
			{
				newPlayer.setGameType(GameType.SPECTATOR);
			}
			
			if(originalData.hasKey("canEditUHC"))
			{
				originalData.getBoolean("canEditUHC");
				newData.setBoolean("canEditUHC", true);
			}
			
			BlockPos deathPos = originalPlayer.getPosition();
			newData.setInteger("deathX", deathPos.getX());
			newData.setInteger("deathY", deathPos.getY());
			newData.setInteger("deathZ", deathPos.getZ());
			newData.setInteger("deathDim", originalPlayer.dimension);
			System.out.println("death positions saved");
			newPlayer.setSpawnPoint(deathPos, true);
		}
	}
	
	@SubscribeEvent
	public void TestItemEvent(PlayerInteractEvent event) {		
		EntityPlayer player = (EntityPlayer) event.getEntityPlayer();
		World world = player.world;
		ItemStack stack = player.getHeldItem(event.getHand());
		NBTTagCompound entityData = player.getEntityData();

		boolean flag = entityData.getBoolean("canEditUHC");
		
		UHCSaveData saveData = UHCSaveData.getForWorld(event.getWorld());
		
		if (stack.getItem() == Items.STICK && flag)
		{
			if(player.isSneaking())
			{
				stack.setStackDisplayName("uhcOnGoing = false");
				saveData.setUhcOnGoing(false);
				saveData.markDirty();
			}
			else
			{
				stack.setStackDisplayName("uhcOnGoing = true");
				saveData.setUhcOnGoing(true);
				saveData.markDirty();
			}	
		}
		
		if (stack.getItem() == Items.CARROT_ON_A_STICK && flag)
		{
			if(player.isSneaking())
			{
				stack.setStackDisplayName("itemConversion = false");
				saveData.setItemConversion(false);
				saveData.markDirty();
			}
			else
			{
				stack.setStackDisplayName("itemConversion = true");
				saveData.setItemConversion(true);
				saveData.markDirty();
			}	
		}
		
		if (stack.getItem() == Items.BOWL && flag)
		{
			if(player.isSneaking())
			{
				stack.setStackDisplayName("autoCook = false");
				saveData.setAutoCook(false);
				saveData.markDirty();
			}
			else
			{
				stack.setStackDisplayName("autoCook = true");
				saveData.setAutoCook(true);
				saveData.markDirty();
			}	
		}
		
		if (stack.getItem() == Items.ARROW && flag)
		{
			if(player.isSneaking())
			{
				stack.setStackDisplayName("customHealth = 20 [default]");
				saveData.setMaxHealth(20);
				saveData.markDirty();
			}
			else
			{
				stack.setStackDisplayName("customHealth = 40");
				saveData.setMaxHealth(40);
				saveData.markDirty();
			}	
		}
		
		if (stack.getItem() == Items.BUCKET && flag)
		{
			stack.setStackDisplayName(String.valueOf(saveData.getDifficulty()));
		}
	}
}
