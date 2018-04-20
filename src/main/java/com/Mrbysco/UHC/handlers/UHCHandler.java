package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;
import java.util.List;

import com.Mrbysco.UHC.init.ModItems;
import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.packets.ModPackethandler;
import com.Mrbysco.UHC.packets.UHCPacketMessage;
import com.Mrbysco.UHC.utils.UHCTeleporter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class UHCHandler {
	
	public int uhcStartTimer;

	@SubscribeEvent
	public void UHCStartEvent(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			UHCTimerData timerData = UHCTimerData.getForWorld(world);
			MinecraftServer server = world.getMinecraftServer();
			ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();
			
			if (saveData.isUhcOnGoing() == false)
			{
				for(EntityPlayer player : playerList)
				{
					if(player.isGlowing() == false)
						player.setGlowing(true);
					else
						return;
				}
			}
			
			if(!playerList.isEmpty())
			{	
				if(saveData.isUhcStarting())
				{
					if(timerData.getUhcStartTimer() != this.uhcStartTimer)
					{
						this.uhcStartTimer = timerData.getUhcStartTimer();
					}
					
					if(timerData.getUhcStartTimer() == 40 || timerData.getUhcStartTimer() == 60 || timerData.getUhcStartTimer() == 80 || 
						timerData.getUhcStartTimer() == 100 || timerData.getUhcStartTimer() == 120 || timerData.getUhcStartTimer() == 140)
					{
						if(timerData.getUhcStartTimer() == 20)
						{
							sendMessage(playerList, new TextComponentTranslation(TextFormatting.YELLOW + "uhc.start.5"));
							++this.uhcStartTimer;
							timerData.setUhcStartTimer(this.uhcStartTimer);
							timerData.markDirty();
						}
						else if(timerData.getUhcStartTimer() == 40)
						{
							sendMessage(playerList, new TextComponentTranslation(TextFormatting.YELLOW + "uhc.start.4"));
							++this.uhcStartTimer;
							timerData.setUhcStartTimer(this.uhcStartTimer);
							timerData.markDirty();
						}
						else if(timerData.getUhcStartTimer() == 60)
						{
							sendMessage(playerList, new TextComponentTranslation(TextFormatting.YELLOW + "uhc.start.3"));
							++this.uhcStartTimer;
							timerData.setUhcStartTimer(this.uhcStartTimer);
							timerData.markDirty();
						}
						else if(timerData.getUhcStartTimer() == 80)
						{
							sendMessage(playerList, new TextComponentTranslation(TextFormatting.YELLOW + "uhc.start.2"));
							++this.uhcStartTimer;
							timerData.setUhcStartTimer(this.uhcStartTimer);
							timerData.markDirty();
						}
						else if(timerData.getUhcStartTimer() == 100)
						{
							sendMessage(playerList, new TextComponentTranslation(TextFormatting.YELLOW + "uhc.start.1"));
							++this.uhcStartTimer;
							timerData.setUhcStartTimer(this.uhcStartTimer);
							timerData.markDirty();
						}
						else if(timerData.getUhcStartTimer() == 140)
						{
							for(EntityPlayerMP player : playerList)
							{
								player.clearActivePotions();
							}
							
							sendMessage(playerList, new TextComponentString(TextFormatting.YELLOW + "uhc.start.0"));
							this.uhcStartTimer = 0;

							saveData.setUhcStarting(false);
							timerData.setUhcStartTimer(this.uhcStartTimer);
							saveData.setUhcOnGoing(true);
							saveData.markDirty();
						}
					}
					else
					{
						for(EntityPlayerMP player : playerList)
						{
							if(player.getActivePotionEffect(MobEffects.MINING_FATIGUE) == null)
								player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 32767 * 20, 10, true, false));
						}
						
						++this.uhcStartTimer;
						timerData.setUhcStartTimer(this.uhcStartTimer);
						timerData.markDirty();
					}

				}
				else
				{
					if(timerData.getUhcStartTimer() != 0)
					{
						timerData.setUhcStartTimer(0);
						timerData.markDirty();
					}
				}
			}
		}
	}
	
	public static void sendMessage(ArrayList<EntityPlayerMP> list, ITextComponent text)
	{
		for (EntityPlayerMP player : list)
		{
			player.sendMessage(text);
		}
	}
	
	@SubscribeEvent
	public void UhcEvents(TickEvent.WorldTickEvent event) {
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
			List<Entity> entityList = world.loadedEntityList;
			
			for(Entity entity : entityList)
			{
				if(entity instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) entity;
					NBTTagCompound entityData = player.getEntityData();
					boolean UHCFlag = saveData.isUhcOnGoing();
					
					if (entityData.getBoolean("canEditUHC") == true && !UHCFlag)
					{
						if (player.inventory.getStackInSlot(39) == editStack)
							return;
						
						if (player.inventory.getStackInSlot(39).isEmpty())
							player.inventory.setInventorySlotContents(39, editStack);
					}
					if (entityData.getBoolean("canEditUHC") == false)
					{
						if(player.inventory.getStackInSlot(39) == editStack)
							player.inventory.removeStackFromSlot(39);
					}
					
					if (!player.inventory.hasItemStack(bookStack) && !UHCFlag)
					{
						player.inventory.addItemStackToInventory(bookStack);
					}
					
					if(!UHCFlag)
					{
						if(player.getActivePotionEffect(MobEffects.SATURATION) == null)
							player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 32767 * 20, 10, true, false));

						if(player.getActivePotionEffect(MobEffects.RESISTANCE) == null)
							player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 32767 * 20, 10, true, false));
					}
				}
				
				if (entity instanceof EntityItem) {
					EntityItem itemEntity = (EntityItem) entity;
					
					if(itemEntity.getItem().getItem() == ModItems.uhc_book)
						world.removeEntity(itemEntity);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void spawnRoomEvent(TickEvent.WorldTickEvent event){
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			UHCSaveData saveData = UHCSaveData.getForWorld(world);

			if(saveData.isSpawnRoom() && !saveData.isUhcOnGoing()) {
				double centerX1 = saveData.getBorderCenterX() -7;
				double centerX2 = saveData.getBorderCenterX() +7;
				double centerZ1 = saveData.getBorderCenterZ() -7;
				double centerZ2 = saveData.getBorderCenterZ() +7;
				
				AxisAlignedBB hitbox = new AxisAlignedBB(centerX1 - 0.5f, 248 - 0.5f, centerZ1 - 0.5f, centerX2 + 0.5f, 260 + 0.5f, centerZ2 + 0.5f);
				ArrayList<EntityPlayer> collidingList = new ArrayList<>(world.getEntitiesWithinAABB(EntityPlayer.class, hitbox));

				MinecraftServer server = world.getMinecraftServer();
				ArrayList<EntityPlayerMP> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
				
				for(EntityPlayerMP player : playerList)
				{
					if(!collidingList.contains(player) && !player.isCreative() && !player.isSpectator())
					{
						if(player.dimension == saveData.getSpawnRoomDimension())
						{
				            ((EntityPlayerMP)player).connection.setPlayerLocation(saveData.getBorderCenterX(), 252, saveData.getBorderCenterZ(), player.rotationYaw, player.rotationPitch);
						}
						else if(player.dimension != saveData.getSpawnRoomDimension())
						{
							player.changeDimension(saveData.getSpawnRoomDimension(), new UHCTeleporter(player.getPosition()));
						}
					}
				}
				
				for(double i = centerX1; i <= centerX2; i++)
				{
					double d0 = world.rand.nextGaussian() * 0.02D;
		            double d1 = world.rand.nextGaussian() * 0.02D;
		            double d2 = world.rand.nextGaussian() * 0.02D;
					for(double j = centerZ1; j <= centerZ2; j++)
					{
						if (world.rand.nextInt(10000) <= 4)
							((WorldServer) world).spawnParticle(EnumParticleTypes.CRIT, i, 250 - 0.5, j, 3, d0, d1, d2, 0.0D);
						
						if(j == centerZ1 || j == centerZ2)
						{
							for(double k = 250; k <= 253; k++)
							{
								if (world.rand.nextInt(1000) <= 3)
									((WorldServer) world).spawnParticle(EnumParticleTypes.TOTEM, i, k + 1.0D, j, 3, d0, d1, d2, 0.0D);
							}
						}
					}
					
					if(i == centerX1 || i == centerX2)
					{
						for(double j = centerZ1; j <= centerZ2; j++)
						{
							for(double k = 250; k <= 253; k++)
							{
								if (world.rand.nextInt(1000) <= 3)
									((WorldServer) world).spawnParticle(EnumParticleTypes.TOTEM, i, k + 1.0D, j, 3, d0, d1, d2, 0.0D);
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
    public void playerEditUHCEvent(PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			NBTTagCompound entityData = player.getEntityData();
			World world = player.world;
			UHCSaveData saveData = UHCSaveData.getForWorld(world);

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
	}	
	
	@SubscribeEvent
	public void onNewPlayerJoin(PlayerLoggedInEvent event)
	{
		EntityPlayer player = event.player;
		WorldServer worldServer = (WorldServer)player.world;
		UHCSaveData saveData = UHCSaveData.getForWorld(player.world);

		if(!player.world.isRemote)
		{
			EntityPlayerMP playerMP = (EntityPlayerMP) player;

			if (saveData.isUhcOnGoing() && player.getTeam() == null)
			{
				playerMP.setGameType(GameType.SPECTATOR);
			}
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
			if(!world.isRemote)
			{
				if(saveData.isUhcOnGoing())
				{
					if(!saveData.isNetherEnabled())
					{
						if(event.getDimension() == -1)
							event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerGivePermission(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			Entity source = event.getSource().getTrueSource();
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			World world = player.world;
			NBTTagCompound entityData = player.getEntityData();
			
			if(source instanceof EntityPlayer)
			{
				EntityPlayer sourcePlayer = (EntityPlayer) source;
				if (!world.isRemote) {
					if (sourcePlayer.canUseCommand(2, "")) {
						if(!player.canUseCommand(2, ""))
						{
							if(entityData.getBoolean("canEditUHC"))
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
		EntityPlayer originalPlayer = event.getOriginal();
		EntityPlayer newPlayer = event.getEntityPlayer();

		UHCSaveData saveData = UHCSaveData.getForWorld(newPlayer.world);

		NBTTagCompound originalData = originalPlayer.getEntityData();
		NBTTagCompound newData = newPlayer.getEntityData();
		
		if(!newPlayer.world.isRemote)
		{
			if(saveData.isUhcOnGoing())
				newPlayer.setGameType(GameType.SPECTATOR);
			
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
	public void SyncPlayerWithData(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer && !event.getWorld().isRemote)
		{
			EntityPlayer player = (EntityPlayer)event.getEntity();
			World world = event.getWorld();
			UHCSaveData saveData = UHCSaveData.getForWorld(world);

			ModPackethandler.INSTANCE.sendTo(new UHCPacketMessage(saveData), (EntityPlayerMP) player);
		}
	}
}
