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
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class UHCHandler {
	
	@SubscribeEvent
	public void UHCBookEvent(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer() && !event.world.isRemote)
		{
			World world = event.world;
			ItemStack bookStack = new ItemStack(ModItems.uhc_book);
			ItemStack editStack = new ItemStack(Items.LEAD);
			editStack.addEnchantment(Enchantments.BINDING_CURSE, 1);;
			editStack.addEnchantment(Enchantments.VANISHING_CURSE, 1);;
			editStack.setStackDisplayName("Editors Monacle");
			editStack.setTagInfo("lore", new NBTTagString("You have the power to edit the main UHC settings"));

			UHCSaveData saveData = UHCSaveData.getForWorld(world);

			List<Entity> entityList = world.loadedEntityList;
			
			for(Entity entity : entityList)
			{
				if(entity instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) entity;
					NBTTagCompound entityData = player.getEntityData();

					if (entityData.getBoolean("canEditUHC") == true && saveData.isUhcOnGoing() == false)
					{
						//System.out.println(entityData.toString());
						if (player.inventory.getStackInSlot(39) == editStack)
						{
							return;
						}
						if (player.inventory.getStackInSlot(39).isEmpty())
						{
							player.inventory.setInventorySlotContents(39, editStack);
						}
					}
					if (entityData.getBoolean("canEditUHC") == false)
					{
						player.inventory.removeStackFromSlot(39);
					}
					
					if (!player.inventory.hasItemStack(bookStack) && (saveData.isUhcOnGoing() == false))
					{
						player.inventory.addItemStackToInventory(bookStack);
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
    public void playerEditUHCEvent(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		NBTTagCompound entityData = player.getEntityData();
		World world = player.world;
		
		if(!world.isRemote)
		{
			if (!entityData.hasKey("canEditUHC"))
				entityData.setBoolean("canEditUHC", false);
			
			if(entityData.getBoolean("canEditUHC") == false)
			{
				if (player.canUseCommand(2, ""))
					entityData.setBoolean("canEditUHC", true);
			}
		}
	}
	
	@SubscribeEvent
	public void onNewPlayerJoin(PlayerLoggedInEvent event)
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
				if (!world.isRemote) {
					if (sourcePlayer.canUseCommand(2, "")) {
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
				if(originalData.getBoolean("canEditUHC"))
					newData.setBoolean("canEditUHC", true);
				else
					newData.setBoolean("canEditUHC", false);
			}
			
			BlockPos deathPos = originalPlayer.getPosition();
			newData.setInteger("deathX", deathPos.getX());
			newData.setInteger("deathY", deathPos.getY());
			newData.setInteger("deathZ", deathPos.getZ());
			newData.setInteger("deathDim", originalPlayer.dimension);
			newPlayer.setSpawnPoint(deathPos, true);
		}
	}
	
	@SubscribeEvent
	public void DimensionChangeEvent(EntityTravelToDimensionEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntity();
			World world = player.world;
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			if(saveData.isUhcOnGoing() && saveData.isNetherEnabled() == false)
			{
				if(event.getDimension() == -1)
				{
					event.setCanceled(true);
				}
			}
		}
	}
}
