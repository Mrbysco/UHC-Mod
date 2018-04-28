package com.Mrbysco.UHC.handlers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.Mrbysco.UHC.init.ModItems;
import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.lists.SpawnItemList;
import com.Mrbysco.UHC.lists.info.SpawnItemInfo;
import com.Mrbysco.UHC.packets.ModPackethandler;
import com.Mrbysco.UHC.packets.UHCPacketMessage;
import com.Mrbysco.UHC.utils.UHCTeleporter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class UHCHandler {
	
	public int uhcStartTimer;

	@SubscribeEvent
	public void UHCStartEventWorld(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
				MinecraftServer server = DimensionManager.getWorld(0).getMinecraftServer();
				ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();
				
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
							if(timerData.getUhcStartTimer() == 40)
							{
								sendMessage(playerList, new TextComponentTranslation("uhc.start.5"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.markDirty();
							}
							else if(timerData.getUhcStartTimer() == 60)
							{
								sendMessage(playerList, new TextComponentTranslation("uhc.start.4"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.markDirty();
							}
							else if(timerData.getUhcStartTimer() == 80)
							{
								sendMessage(playerList, new TextComponentTranslation("uhc.start.3"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.markDirty();
							}
							else if(timerData.getUhcStartTimer() == 100)
							{
								sendMessage(playerList, new TextComponentTranslation("uhc.start.2"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.markDirty();
							}
							else if(timerData.getUhcStartTimer() == 120)
							{
								sendMessage(playerList, new TextComponentTranslation("uhc.start.1"));
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.markDirty();
							}
							else if(timerData.getUhcStartTimer() == 140)
							{
								sendMessage(playerList, new TextComponentTranslation("uhc.start"));

								saveData.setUhcStarting(false);
								timerData.setUhcStartTimer(0);
								saveData.setUhcOnGoing(true);
								saveData.markDirty();
							}
						}
						else
						{
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
	}
	
	@SubscribeEvent
	public void UHCStartEventPlayer(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
				
				if(saveData.isUhcStarting())
				{
					if(this.uhcStartTimer == 140)
					{
						if(!SpawnItemList.spawnItemList.isEmpty() && SpawnItemList.spawnItemList != null)
						{
							for(SpawnItemInfo info : SpawnItemList.spawnItemList)
							{
								giveResult(player, info.getstack1().copy());
								giveResult(player, info.getstack2().copy());
								giveResult(player, info.getstack3().copy());
								giveResult(player, info.getstack4().copy());
								giveResult(player, info.getstack5().copy());
								giveResult(player, info.getstack6().copy());
								giveResult(player, info.getstack7().copy());
								giveResult(player, info.getstack8().copy());
								giveResult(player, info.getstack9().copy());
							}
						}
						
						player.clearActivePotions();
					}
					else
					{
						if(player.getActivePotionEffect(MobEffects.MINING_FATIGUE) == null)
							player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 32767 * 20, 10, true, false));
						
						if(player.getActivePotionEffect(MobEffects.SLOWNESS) == null)
							player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 32767 * 20, 10, true, false));
						
						if(player.inventory.hasItemStack(new ItemStack(ModItems.uhc_book)))
						{
							int bookSlot = player.inventory.findSlotMatchingUnusedItem(new ItemStack(ModItems.uhc_book));
							if(bookSlot != -1)
								player.inventory.removeStackFromSlot(bookSlot);
						}
						
						if (!player.inventory.getStackInSlot(39).isEmpty())
							player.inventory.removeStackFromSlot(39);
					}
				}
			}
		}
	}
	
	public void giveResult(EntityPlayer player, ItemStack stack)
	{
		if(stack == ItemStack.EMPTY || stack == null)
			return;
		else
			player.addItemStackToInventory(stack);
	}
	
	public static void sendMessage(ArrayList<EntityPlayerMP> list, ITextComponent text)
	{
		for (EntityPlayerMP player : list)
		{
			player.sendMessage(text);
		}
	}
	
	public ItemStack editorLead()
	{
		ItemStack editStack = new ItemStack(Items.LEAD);
		editStack.addEnchantment(Enchantments.BINDING_CURSE, 1);;
		editStack.addEnchantment(Enchantments.VANISHING_CURSE, 1);;
		editStack.setStackDisplayName("Editors Monocle");
		NBTTagCompound nbt = editStack.getTagCompound();
		nbt.setInteger("HideFlags", 1);
		editStack.setTagCompound(nbt);
		editStack.setTagInfo("lore", new NBTTagString("You have the power to edit the main UHC settings"));
		
		return editStack;
	}
	
	@SubscribeEvent
	public void UhcEvents(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			World world = player.world;
			ItemStack bookStack = new ItemStack(ModItems.uhc_book);

			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				List<Entity> entityList = world.loadedEntityList;
				
				if(!saveData.isUhcOnGoing() && !saveData.isUhcStarting())
				{
					MinecraftServer server = world.getMinecraftServer();
	
					NBTTagCompound entityData = player.getEntityData();
					
					if (entityData.getBoolean("canEditUHC") == true)
					{
						if (player.inventory.getStackInSlot(39) == editorLead())
							return;
						
						if (player.inventory.getStackInSlot(39).isEmpty())
							player.inventory.setInventorySlotContents(39, editorLead());
					}
					
					if(!player.inventory.getItemStack().isItemEqual(bookStack))
					{
						if (!player.inventory.hasItemStack(bookStack))
							player.inventory.addItemStackToInventory(bookStack);
					}
					
					if(player.getActivePotionEffect(MobEffects.SATURATION) == null)
						player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 32767 * 20, 10, true, false));
	
					if(player.getActivePotionEffect(MobEffects.RESISTANCE) == null)
						player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 32767 * 20, 10, true, false));
				}
				
				if(saveData.isUhcOnGoing() && !saveData.isUhcIsFinished())
				{
					Scoreboard scoreboard = world.getScoreboard();
					MinecraftServer server = world.getMinecraftServer();
					
					ArrayList<ScorePlayerTeam> teamsAlive = new ArrayList<>();
					for(ScorePlayerTeam team : scoreboard.getTeams())
					{
						if(team.getMembershipCollection().size() > 0 && team.getName() != "spectator")
						{
							if(teamsAlive.contains(team))
								return;
							else
								teamsAlive.add(team);
						}
					}
					
					if(!teamsAlive.isEmpty() && teamsAlive != null)
					{
						for(ScorePlayerTeam team : teamsAlive)
						{
							if(team.getMembershipCollection().size() < 1 && team != null)
							{
								teamsAlive.remove(team);
							}
						}
					}
					ArrayList<EntityPlayerMP> playerList = new ArrayList<>(server.getPlayerList().getPlayers());

					if(teamsAlive.size() == 1)
					{
						ScorePlayerTeam team = teamsAlive.get(0);
						if(teamsAlive.get(0) != null && team == scoreboard.getTeam("solo"))
						{
							if(team.getMembershipCollection().size() == 1)
							{
								for (String s : team.getMembershipCollection())
					            {
									EntityPlayer winningPlayer = world.getPlayerEntityByName(s);
									SoloWonTheUHC(winningPlayer, playerList, world);
									saveData.setUhcIsFinished(true);
					            }
							}
						}
						else
						{
							YouWonTheUHC(teamsAlive.get(0), playerList, world);
							saveData.setUhcIsFinished(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void throwEvent(ItemTossEvent event)
	{
		Entity entity = event.getEntity();
		World world = entity.world;
		ItemStack stack = event.getEntityItem().getItem();
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

			if(!saveData.isUhcOnGoing())
			{
				if(stack.isItemEqual(new ItemStack(ModItems.uhc_book)))
				{
					event.setCanceled(true);
					event.setResult(Result.DENY);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void spawnRoomEvent(TickEvent.WorldTickEvent event){
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

				if(saveData.isSpawnRoom() && !saveData.isUhcOnGoing()) {
					if(DimensionManager.getWorld(saveData.getSpawnRoomDimension()) != null)
					{
						World world = DimensionManager.getWorld(saveData.getSpawnRoomDimension());
						double centerX1 = saveData.getBorderCenterX() -7;
						double centerX2 = saveData.getBorderCenterX() +7;
						double centerZ1 = saveData.getBorderCenterZ() -7;
						double centerZ2 = saveData.getBorderCenterZ() +7;
										
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
		}
	}
	
	@SubscribeEvent
	public void SpawnRoomPlayerEvent(TickEvent.PlayerTickEvent event){
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			World world = player.world;
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				
				if(saveData.isSpawnRoom() && !saveData.isUhcOnGoing()) {
					double centerX1 = saveData.getBorderCenterX() -7;
					double centerX2 = saveData.getBorderCenterX() +7;
					double centerZ1 = saveData.getBorderCenterZ() -7;
					double centerZ2 = saveData.getBorderCenterZ() +7;
					
					AxisAlignedBB hitbox = new AxisAlignedBB(centerX1 - 0.5f, 248 - 0.5f, centerZ1 - 0.5f, centerX2 + 0.5f, 260 + 0.5f, centerZ2 + 0.5f);
					ArrayList<EntityPlayer> collidingList = new ArrayList<>(world.getEntitiesWithinAABB(EntityPlayer.class, hitbox));
					
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
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

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
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
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
			newData.setBoolean("dead", true);
			newPlayer.setSpawnPoint(deathPos, true);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;
		if(!world.isRemote)
		{
			MinecraftServer server = world.getMinecraftServer();
			Scoreboard scoreboard = world.getScoreboard();
			ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				if(saveData.isUhcOnGoing())
				{
					scoreboard.addPlayerToTeam(player.getName(), "spectator");
					
					if(scoreboard.getObjective("health") != null)
				        scoreboard.removeObjectiveFromEntity(player.getName(), scoreboard.getObjective("health"));
				}
			}
		}
	}
	
	public void YouWonTheUHC(ScorePlayerTeam team, ArrayList<EntityPlayerMP> playerList, World world)
	{
		if(!world.isRemote)
		{
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				String teamName = team.getDisplayName();
				for(EntityPlayer player : playerList)
				{
					player.sendMessage(new TextComponentTranslation("uhc.team.won", new Object[] {team.getColor() + teamName}));
				}
				
				for(String member : team.getMembershipCollection())
				{
					for(int i = 0; i > 7; i++)
					{
						EntityPlayer player = world.getPlayerEntityByName(member);
						if(player != null)
						{
							EntityFireworkRocket rocket = new EntityFireworkRocket(world, player.posX, player.posY, player.posZ, getFirework(world.rand));
							player.world.spawnEntity(rocket);
						}
					}
				}
			}
		}
	}
	
	public void SoloWonTheUHC(EntityPlayer winningPlayer, ArrayList<EntityPlayerMP> playerList, World world)
	{
		if(!world.isRemote)
		{
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				for(EntityPlayer player : playerList)
				{
					player.sendMessage(new TextComponentTranslation("uhc.team.won", new Object[] {TextFormatting.YELLOW + winningPlayer.getName()}));
				}
				
				for(int i = 0; i > 7; i++)
				{
					if(winningPlayer != null)
					{
						EntityFireworkRocket rocket = new EntityFireworkRocket(world, winningPlayer.posX, winningPlayer.posY, winningPlayer.posZ, getFirework(world.rand));
						winningPlayer.world.spawnEntity(rocket);	
					}
				}
			}
		}
	}
	
	public ItemStack getFirework(Random rand) {
        ItemStack firework = new ItemStack(Items.FIREWORKS);
        firework.setTagCompound(new NBTTagCompound());

        NBTTagCompound data = new NBTTagCompound();
        data.setByte("Flight", (byte) 1);

        NBTTagList list = new NBTTagList();
        NBTTagCompound fireworkData = new NBTTagCompound();

        fireworkData.setByte("Trail", (byte) 1);
        fireworkData.setByte("Type", (byte) 3);
        fireworkData.setIntArray("Colors", new int[]{new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)).getRGB()});

        list.appendTag(fireworkData);
        data.setTag("Explosions", list);
        firework.getTagCompound().setTag("Fireworks", data);

        return firework;
	}
	
	@SubscribeEvent
	public void SyncPlayerWithData(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer && !event.getWorld().isRemote)
		{
			EntityPlayer player = (EntityPlayer)event.getEntity();
			World world = event.getWorld();
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

			ModPackethandler.INSTANCE.sendTo(new UHCPacketMessage(saveData), (EntityPlayerMP) player);
		}
	}
}
