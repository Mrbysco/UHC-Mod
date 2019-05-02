package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.config.UltraHardCoremodConfigGen;
import com.Mrbysco.UHC.init.ModItems;
import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.lists.SpawnItemList;
import com.Mrbysco.UHC.lists.info.SpawnItemInfo;
import com.Mrbysco.UHC.packets.ModPackethandler;
import com.Mrbysco.UHC.packets.UHCPacketMessage;
import com.Mrbysco.UHC.utils.UHCTeleporter;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketTitle;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.util.ArrayList;
import java.util.Random;

public class UHCHandler {
	
	public int uhcStartTimer;

	@SubscribeEvent
	public void UHCStartEventWorld(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer())
		{
			if(DimensionManager.getWorld(0) != null)
			{
				World world = event.world;
				if (world.getWorldTime() % 20 == 0) {
					UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
					UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
					MinecraftServer server = DimensionManager.getWorld(0).getMinecraftServer();
					ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();

					if(saveData.getUHCDimension() != UltraHardCoremodConfigGen.general.dimension)
						saveData.setUHCDimension(UltraHardCoremodConfigGen.general.dimension);

					if(!playerList.isEmpty())
					{
						if(saveData.isUhcStarting())
						{
							if(timerData.getUhcStartTimer() != this.uhcStartTimer)
							{
								this.uhcStartTimer = timerData.getUhcStartTimer();
							}

							if(timerData.getUhcStartTimer() == 2 || timerData.getUhcStartTimer() == 3 || timerData.getUhcStartTimer() == 4 ||
									timerData.getUhcStartTimer() == 5 || timerData.getUhcStartTimer() == 6 || timerData.getUhcStartTimer() == 7) {
								if(timerData.getUhcStartTimer() == 2)
								{
									sendMessage(playerList, new TextComponentTranslation("uhc.start.5"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								}
								else if(timerData.getUhcStartTimer() == 3)
								{
									sendMessage(playerList, new TextComponentTranslation("uhc.start.4"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								}
								else if(timerData.getUhcStartTimer() == 4)
								{
									sendMessage(playerList, new TextComponentTranslation("uhc.start.3"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								}
								else if(timerData.getUhcStartTimer() == 5)
								{
									sendMessage(playerList, new TextComponentTranslation("uhc.start.2"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								}
								else if(timerData.getUhcStartTimer() == 6)
								{
									sendMessage(playerList, new TextComponentTranslation("uhc.start.1"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								}
								else if(timerData.getUhcStartTimer() == 7)
								{
									sendMessage(playerList, new TextComponentTranslation("uhc.start"));

									timerData.setUhcStartTimer(0);
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
	}
	
	@SubscribeEvent
	public void UHCStartEventPlayer(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer())
		{
			EntityPlayer player = event.player;
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				NBTTagCompound entityData = player.getEntityData();

				if(saveData.isUhcStarting())
				{					
					if (!entityData.hasKey("startFatigue"))
						entityData.setBoolean("startFatigue", true);

					if(this.uhcStartTimer == 7)
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
						entityData.setBoolean("startFatigue", false);

						if(player.getActivePotionEffects().size() > 0)
							player.clearActivePotions();

						saveData.setUhcStarting(false);
						saveData.setUhcOnGoing(true);
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
				if(saveData.isUhcOnGoing())
				{
					if(entityData.getBoolean("startFatigue") == true)
					{
						player.clearActivePotions();

						if(player.getActivePotionEffects().size() > 0)
							player.clearActivePotions();

						entityData.setBoolean("startFatigue", false);
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
			ItemStack bookStack = new ItemStack(ModItems.uhc_book);
			
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

				if(!saveData.isUhcOnGoing() && !saveData.isUhcStarting())
				{
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
					
					if(player.getActivePotionEffect(MobEffects.MINING_FATIGUE) == null)
						player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 32767 * 20, 10, true, false));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void checkWinner(TickEvent.WorldTickEvent event) 
	{
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
		
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				
				if(saveData.isUhcOnGoing() && !saveData.isUhcIsFinished())
				{
					Scoreboard scoreboard = world.getScoreboard();
					MinecraftServer server = world.getMinecraftServer();
					
					ArrayList<ScorePlayerTeam> teamsAlive = new ArrayList<>();
					for(ScorePlayerTeam team : scoreboard.getTeams())
					{
						if(team.getMembershipCollection().size() > 0 && team != scoreboard.getTeam("spectator"))
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
							if(team.getMembershipCollection().size() == 0 && team != null)
							{
								teamsAlive.remove(team);
							}
						}
					}
					
					ArrayList<EntityPlayerMP> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
					
					if(teamsAlive.size() == 1)
					{
						ScorePlayerTeam team = teamsAlive.get(0);
						if(teamsAlive.get(0) != null)
						{
							if(team == scoreboard.getTeam("solo"))
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
								for(int i = 0; i > 7; i++)
								{
									for(String players : teamsAlive.get(0).getMembershipCollection())
									{
										EntityPlayer teamPlayer = world.getPlayerEntityByName(players);
										EntityFireworkRocket rocket = new EntityFireworkRocket(world, teamPlayer.posX, teamPlayer.posY + 3, teamPlayer.posZ, getFirework(world.rand));
										world.spawnEntity(rocket);
									}
								}
								
								if(!teamsAlive.get(0).getMembershipCollection().isEmpty() && teamsAlive.get(0).getMembershipCollection().size() > 1)
								{
									ArrayList<String> teamPlayers = new ArrayList<>(teamsAlive.get(0).getMembershipCollection());
									ArrayList<EntityPlayerMP> playersAlive = new ArrayList<>();
									
									for(String playerName : teamPlayers)
									{
										scoreboard.removePlayerFromTeams(playerName);
										EntityPlayer player = world.getPlayerEntityByName(playerName);
										playersAlive.add((EntityPlayerMP)player);
									}
									
									setupShowdownAndTeleport(world, playersAlive);

									saveData.setUhcShowdown(true);
								}
								
								saveData.setUhcIsFinished(true);
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void testingEvent(PlayerInteractEvent.RightClickItem event)
	{
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();
		
		if(!world.isRemote)
		{
			if(event.getItemStack().getItem() == Items.FEATHER)
			{
				ArrayList<EntityPlayerMP> players = new ArrayList<>();
				players.add((EntityPlayerMP)player);
				setupShowdownAndTeleport(world, players);
			}
		}
	}
	
	public void setupShowdownAndTeleport(World world, ArrayList<EntityPlayerMP> players)
	{
		double centerX = 0;
		double centerZ = 0;
		
		double centerX1 = centerX -21;
		double centerX2 = centerX +21;
		double centerZ1 = centerZ -21;
		double centerZ2 = centerZ +21;
		
		Block showdownBlock = Block.getBlockFromName(UltraHardCoremodConfigGen.general.showdownBlock);
		
		if(showdownBlock == null)
		{
			showdownBlock = Blocks.STONEBRICK;
		}
		
		for(double i = centerX1; i <= centerX2; i++)
		{
			for(double j = centerZ1; j <= centerZ2; j++)
			{
				world.setBlockState(new BlockPos(i, 250, j), showdownBlock.getDefaultState());
				if(j == centerZ1 || j == centerZ2)
				{
					for(double k = 250; k <= 253; k++)
					{
						world.setBlockState(new BlockPos(i, k, j), showdownBlock.getDefaultState());
					}
				}
			}
			
			if(i == centerX1 || i == centerX2)
			{
				for(double j = centerZ1; j <= centerZ2; j++)
				{
					for(double k = 250; k <= 253; k++)
					{
						world.setBlockState(new BlockPos(i, k, j), showdownBlock.getDefaultState());
					}
				}
			}
		}
		
		int TeleportChoosing = 0;
		for(EntityPlayerMP player : players)
		{
			TeleportChoosing++;
			if(TeleportChoosing > 8)
			{
				TeleportChoosing = 0;
				TeleportChoosing++;
			}
			switch (TeleportChoosing) {
			case 1:
				player.setPositionAndUpdate(centerX2 -1.5, 251, centerZ2 -1.5);
				break;
			case 2:
				player.setPositionAndUpdate(centerX1 +2.5, 251, centerZ1 +2.5);
				break;
			case 3:
				player.setPositionAndUpdate(centerX2 -1.5, 251, centerZ1 +2.5);
				break;
			case 4:
				player.setPositionAndUpdate(centerX1 +2.5, 251, centerZ2 -1.5);
				break;
			case 5:
				player.setPositionAndUpdate(centerX2 -1.5, 251, centerZ);
				break;
			case 6:
				player.setPositionAndUpdate(centerX1 +2.5, 251, centerZ);
				break;
			case 7:
				player.setPositionAndUpdate(centerX, 251, centerZ1 +2.5);
				break;
			case 8:
				player.setPositionAndUpdate(centerX, 251, centerZ2 -1.5);
				break;

			default:
				player.setPositionAndUpdate(centerX2-1.5, 251, centerZ2 -1.5);
				break;
			}
		}
	}
	
	@SubscribeEvent
	public void checkShowDownWinner(TickEvent.WorldTickEvent event) //Only really does anything if there's a showdown
	{
		World world = event.world;
		
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
			
			if(saveData.isUhcOnGoing() && saveData.isUhcIsFinished() && saveData.isUhcShowdown() && !saveData.isUhcShowdownFinished())
			{
				Scoreboard scoreboard = world.getScoreboard();
				MinecraftServer server = world.getMinecraftServer();
				
				ArrayList<EntityPlayerMP> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
				ArrayList<EntityPlayerMP> playersAlive = new ArrayList<>();

				for(EntityPlayerMP player : playerList)
				{
					if(player.interactionManager.getGameType() == GameType.SURVIVAL && player.getTeam() != scoreboard.getTeam("spectator"))
					{
						playersAlive.add(player);
					}
				}
				
				if(!playersAlive.isEmpty() && playersAlive != null)
				{
					for(EntityPlayerMP player : playersAlive)
					{
						if(player.interactionManager.getGameType() != GameType.SURVIVAL)
						{
							playersAlive.remove(player);
						}
					}
				}
				
				if(!playersAlive.isEmpty() && playersAlive.size() == 1)
				{
					EntityPlayer showdownWinner = playersAlive.get(0);
					WonTheShowdown(showdownWinner, playerList, world);
					saveData.setUhcShowdownFinished(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void throwEvent(ItemTossEvent event)
	{
		Entity entity = event.getEntity();
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
								if (world.rand.nextInt(10000) <= 4 && world.getBlockState(new BlockPos(i, 250, j)).isTranslucent())
									((WorldServer) world).spawnParticle(EnumParticleTypes.CRIT, i, 250 - 0.5, j, 3, d0, d1, d2, 0.0D);
								
								if(j == centerZ1 || j == centerZ2)
								{
									for(double k = 250; k <= 253; k++)
									{
										if (world.rand.nextInt(1000) <= 3 && world.getBlockState(new BlockPos(i, k, j)).isTranslucent())
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
										if (world.rand.nextInt(1000) <= 3 && world.getBlockState(new BlockPos(i, k, j)).isTranslucent())
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

			if(!world.isRemote)
			{
				if (!entityData.hasKey("canEditUHC"))
					entityData.setBoolean("canEditUHC", false);
				
				if(entityData.getBoolean("canEditUHC") == false && player.canUseCommand(2, ""))
					entityData.setBoolean("canEditUHC", true);
				
				if(entityData.getBoolean("canEditUHC") == true && !player.canUseCommand(2, ""))
					entityData.setBoolean("canEditUHC", false);
			}
		}
	}	
	
	@SubscribeEvent
	public void onNewPlayerJoin(PlayerLoggedInEvent event)
	{
		EntityPlayer player = event.player;
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
	public void onPlayerPermissionClone(PlayerEvent.Clone event) {
		EntityPlayer originalPlayer = event.getOriginal();
		EntityPlayer newPlayer = event.getEntityPlayer();

		NBTTagCompound originalData = originalPlayer.getEntityData();
		NBTTagCompound newData = newPlayer.getEntityData();
		
		if(!newPlayer.world.isRemote)
		{
			originalData.setBoolean("canEditUHC", newData.getBoolean("canEditUHC"));
			
			BlockPos deathPos = originalPlayer.getPosition();
			newData.setLong("deathPos", deathPos.toLong());
			newData.setString("deathDim", String.valueOf(originalPlayer.dimension));
			newPlayer.setSpawnPoint(deathPos, true);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;
		if(!world.isRemote)
		{
			Scoreboard scoreboard = world.getScoreboard();
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
				String teamName = team.getDisplayName();
				for(EntityPlayerMP player : playerList)
				{
					if(player.getTeam() == team)
					{
						for(int i = 0; i < 10; i++)
						{
							if(world.rand.nextInt(10) < 3)
							{
								EntityFireworkRocket rocket = new EntityFireworkRocket(world, player.posX, player.posY + 3, player.posZ, getFirework(world.rand));
								player.world.spawnEntity(rocket);
							}
						}
					}
					SPacketTitle spackettitle1 = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentTranslation("uhc.team.won", new Object[] {team.getColor() + teamName}));
					player.connection.sendPacket(spackettitle1);
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
				for(EntityPlayerMP player : playerList)
				{
					if(player.getName() == winningPlayer.getName())
					{
						for(int i = 0; i < 10; i++)
						{
							if(world.rand.nextInt(10) < 3)
							{
								EntityFireworkRocket rocket = new EntityFireworkRocket(world, winningPlayer.posX, winningPlayer.posY + 3, winningPlayer.posZ, getFirework(world.rand));
								player.world.spawnEntity(rocket);
							}
						}
					}
					SPacketTitle spackettitle1 = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentTranslation("uhc.player.won", new Object[] {TextFormatting.DARK_RED + winningPlayer.getName()}));
					player.connection.sendPacket(spackettitle1);
				}
			}
		}
	}
	
	public void WonTheShowdown(EntityPlayer winningPlayer, ArrayList<EntityPlayerMP> playerList, World world)
	{
		if(!world.isRemote)
		{
			if(DimensionManager.getWorld(0) != null)
			{
				for(EntityPlayerMP player : playerList)
				{
					if(player.getName() == winningPlayer.getName())
					{
						for(int i = 0; i < 10; i++)
						{
							if(world.rand.nextInt(10) < 3)
							{
								EntityFireworkRocket rocket = new EntityFireworkRocket(world, winningPlayer.posX, winningPlayer.posY + 3, winningPlayer.posZ, getFirework(world.rand));
								player.world.spawnEntity(rocket);
							}
						}
					}
					SPacketTitle spackettitle1 = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentTranslation("uhc.player.showdown.won", new Object[] {TextFormatting.DARK_RED + winningPlayer.getName()}));
					player.connection.sendPacket(spackettitle1);
				}
			}
		}
	}
	
	public ItemStack getFirework(Random rand) {
		ItemStack firework = new ItemStack(Items.FIREWORKS);
		firework.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("Flicker", true);
		nbt.setBoolean("Trail", true);

		int[] colors = new int[rand.nextInt(8) + 1];
		for (int i = 0; i < colors.length; i++) 
		{
			colors[i] = ItemDye.DYE_COLORS[rand.nextInt(16)];
		}
		nbt.setIntArray("Colors", colors);
		byte type = (byte) (rand.nextInt(3) + 1);
		type = type == 3 ? 4 : type;
		nbt.setByte("Type", type);

		NBTTagList explosions = new NBTTagList();
		explosions.appendTag(nbt);

		NBTTagCompound fireworkTag = new NBTTagCompound();
		fireworkTag.setTag("Explosions", explosions);
		fireworkTag.setByte("Flight", (byte) 1);
		firework.getTagCompound().setTag("Fireworks", fireworkTag); 

        return firework;
	}
	
	@SubscribeEvent
	public void SyncPlayerWithData(EntityJoinWorldEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer && !event.getWorld().isRemote)
		{
			EntityPlayer player = (EntityPlayer)event.getEntity();
			World world = event.getWorld();
			Scoreboard scoreboard = world.getScoreboard();
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
			
			if(player.getTeam() == null)
				scoreboard.addPlayerToTeam(player.getName(), "solo");
			
			ModPackethandler.INSTANCE.sendTo(new UHCPacketMessage(saveData), (EntityPlayerMP) player);
		}
	}
}
