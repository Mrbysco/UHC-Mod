package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.lists.SpawnItemList;
import com.mrbysco.uhc.lists.info.SpawnItemInfo;
import com.mrbysco.uhc.packets.UHCPacketHandler;
import com.mrbysco.uhc.packets.UHCPacketMessage;
import com.mrbysco.uhc.registry.ModRegistry;
import com.mrbysco.uhc.utils.UHCTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Random;

public class UHCHandler {
	
	public int uhcStartTimer;

	@SubscribeEvent
	public void UHCStartEventWorld(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer()) {
			ServerWorld overworld = event.world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				World world = event.world;
				if (world.getGameTime() % 20 == 0) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					UHCTimerData timerData = UHCTimerData.get(overworld);
					MinecraftServer server = overworld.getServer();
					ArrayList<ServerPlayerEntity> playerList = (ArrayList<ServerPlayerEntity>)server.getPlayerList().getPlayers();

					ResourceLocation configDimension = ResourceLocation.tryCreate(UHCConfig.COMMON.spawnDimension.get());
					if(!saveData.getUHCDimension().equals(configDimension))
						saveData.setUHCDimension(configDimension);

					if(!playerList.isEmpty()) {
						if(saveData.isUhcStarting()) {
							if(timerData.getUhcStartTimer() != this.uhcStartTimer) {
								this.uhcStartTimer = timerData.getUhcStartTimer();
							}

							if(timerData.getUhcStartTimer() == 2 || timerData.getUhcStartTimer() == 3 || timerData.getUhcStartTimer() == 4 ||
									timerData.getUhcStartTimer() == 5 || timerData.getUhcStartTimer() == 6 || timerData.getUhcStartTimer() == 7) {
								if(timerData.getUhcStartTimer() == 2) {
									sendMessage(playerList, new TranslationTextComponent("uhc.start.5"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								} else if(timerData.getUhcStartTimer() == 3) {
									sendMessage(playerList, new TranslationTextComponent("uhc.start.4"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								} else if(timerData.getUhcStartTimer() == 4) {
									sendMessage(playerList, new TranslationTextComponent("uhc.start.3"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								} else if(timerData.getUhcStartTimer() == 5) {
									sendMessage(playerList, new TranslationTextComponent("uhc.start.2"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								} else if(timerData.getUhcStartTimer() == 6) {
									sendMessage(playerList, new TranslationTextComponent("uhc.start.1"));
									++this.uhcStartTimer;
									timerData.setUhcStartTimer(this.uhcStartTimer);
									timerData.markDirty();
								} else if(timerData.getUhcStartTimer() == 7) {
									sendMessage(playerList, new TranslationTextComponent("uhc.start"));

									timerData.setUhcStartTimer(0);
									saveData.markDirty();
								}
							} else {
								++this.uhcStartTimer;
								timerData.setUhcStartTimer(this.uhcStartTimer);
								timerData.markDirty();
							}
						} else {
							if(timerData.getUhcStartTimer() != 0) {
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
		if (event.phase.equals(TickEvent.Phase.END) && event.side.isServer()) {
			PlayerEntity player = event.player;
			ServerWorld overworld = player.world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				CompoundNBT entityData = player.getPersistentData();

				if(saveData.isUhcStarting()) {
					if (!entityData.contains("startFatigue"))
						entityData.putBoolean("startFatigue", true);

					if(this.uhcStartTimer == 7) {
						if(!SpawnItemList.spawnItemList.isEmpty() && SpawnItemList.spawnItemList != null) {
							for(SpawnItemInfo info : SpawnItemList.spawnItemList) {
								for(int i = 0; i < info.getStackCount(); i++) {
									giveResult(player, info.getStack(i));
								}
							}
						}
						
						player.clearActivePotions();
						entityData.putBoolean("startFatigue", false);

						if(player.getActivePotionEffects().size() > 0)
							player.clearActivePotions();

						saveData.setUhcStarting(false);
						saveData.setUhcOnGoing(true);
					} else {
						if(player.getActivePotionEffect(Effects.MINING_FATIGUE) == null)
							player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 32767 * 20, 10, true, false));
						
						if(player.getActivePotionEffect(Effects.SLOWNESS) == null)
							player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 32767 * 20, 10, true, false));
						
						if(player.inventory.hasItemStack(new ItemStack(ModRegistry.UHC_BOOK.get()))) {
							int bookSlot = player.inventory.findSlotMatchingUnusedItem(new ItemStack(ModRegistry.UHC_BOOK.get()));
							if(bookSlot != -1)
								player.inventory.removeStackFromSlot(bookSlot);
						}
						
						if (!player.inventory.getStackInSlot(39).isEmpty())
							player.inventory.removeStackFromSlot(39);
					}
				}
				if(saveData.isUhcOnGoing()) {
					if(entityData.getBoolean("startFatigue")) {
						player.clearActivePotions();

						if(player.getActivePotionEffects().size() > 0)
							player.clearActivePotions();

						entityData.putBoolean("startFatigue", false);
					}
				}
			}
		}
	}

	public void giveResult(PlayerEntity player, ItemStack stack) {
		if(stack == ItemStack.EMPTY || stack == null)
			return;
		else
			player.addItemStackToInventory(stack);
	}
	
	public static void sendMessage(ArrayList<ServerPlayerEntity> list, ITextComponent text) {
		for (ServerPlayerEntity player : list) {
			player.sendMessage(text, Util.DUMMY_UUID);
		}
	}
	
	public ItemStack editorLead() {
		ItemStack editStack = new ItemStack(Items.LEAD);
		editStack.addEnchantment(Enchantments.BINDING_CURSE, 1);
		editStack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
		editStack.setDisplayName(new StringTextComponent("Editors Monocle"));
		CompoundNBT nbt = editStack.getOrCreateTag();
		nbt.putInt("HideFlags", 1);
		editStack.setTag(nbt);
		editStack.setTagInfo("lore", StringNBT.valueOf("You have the power to edit the main UHC settings"));
		
		return editStack;
	}
	
	@SubscribeEvent
	public void UhcEvents(TickEvent.PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			PlayerEntity player = event.player;
			ServerWorld overworld = player.world.getServer().getWorld(World.OVERWORLD);
			ItemStack bookStack = new ItemStack(ModRegistry.UHC_BOOK.get());

			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);

				if(!saveData.isUhcOnGoing() && !saveData.isUhcStarting()) {
					CompoundNBT entityData = player.getPersistentData();
					
					if (entityData.getBoolean("canEditUHC")) {
						if (player.inventory.getStackInSlot(39) == editorLead())
							return;
						
						if (player.inventory.getStackInSlot(39).isEmpty())
							player.inventory.setInventorySlotContents(39, editorLead());
					}
					
					if(!player.inventory.getItemStack().isItemEqual(bookStack)) {
						if (!player.inventory.hasItemStack(bookStack))
							player.inventory.addItemStackToInventory(bookStack);
					}
					
					if(player.getActivePotionEffect(Effects.SATURATION) == null)
						player.addPotionEffect(new EffectInstance(Effects.SATURATION, 32767 * 20, 10, true, false));
	
					if(player.getActivePotionEffect(Effects.RESISTANCE) == null)
						player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 32767 * 20, 10, true, false));
					
					if(player.getActivePotionEffect(Effects.MINING_FATIGUE) == null)
						player.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 32767 * 20, 10, true, false));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void checkWinner(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			World world = event.world;
			ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				
				if(saveData.isUhcOnGoing() && !saveData.isUhcIsFinished()) {
					Scoreboard scoreboard = world.getScoreboard();
					MinecraftServer server = world.getServer();
					
					ArrayList<ScorePlayerTeam> teamsAlive = new ArrayList<>();
					for(ScorePlayerTeam team : scoreboard.getTeams()) {
						if(team.getMembershipCollection().size() > 0 && team != scoreboard.getTeam("spectator")) {
							if(teamsAlive.contains(team))
								return;
							else
								teamsAlive.add(team);
						}
					}

					if(!teamsAlive.isEmpty() && teamsAlive != null) {
						teamsAlive.removeIf(team -> team.getMembershipCollection().size() == 0);
					}
					
					ArrayList<ServerPlayerEntity> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
					
					if(teamsAlive.size() == 1) {
						ScorePlayerTeam team = teamsAlive.get(0);
						if(teamsAlive.get(0) != null) {
							if(team == scoreboard.getTeam("solo")) {
								if(team.getMembershipCollection().size() == 1) {
									for (String s : team.getMembershipCollection()) {
										PlayerEntity winningPlayer = getPlayerEntityByName(world, s);
										SoloWonTheUHC(winningPlayer, playerList, world);
										saveData.setUhcIsFinished(true);
						            }
								}
							} else {
								YouWonTheUHC(teamsAlive.get(0), playerList, world);
								for(int i = 0; i < 7; i++) {
									for(String players : teamsAlive.get(0).getMembershipCollection()) {
										PlayerEntity teamPlayer = getPlayerEntityByName(world, players);
										FireworkRocketEntity rocket = new FireworkRocketEntity(world,
												teamPlayer.getPosX(), teamPlayer.getPosY() + 3, teamPlayer.getPosZ(), getFirework(world.rand));
										world.addEntity(rocket);
									}
								}
								
								if(!teamsAlive.get(0).getMembershipCollection().isEmpty() && teamsAlive.get(0).getMembershipCollection().size() > 1) {
									ArrayList<String> teamPlayers = new ArrayList<>(teamsAlive.get(0).getMembershipCollection());
									ArrayList<ServerPlayerEntity> playersAlive = new ArrayList<>();
									
									for(String playerName : teamPlayers) {
										scoreboard.removePlayerFromTeams(playerName);
										PlayerEntity player = getPlayerEntityByName(world, playerName);
										playersAlive.add((ServerPlayerEntity)player);
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

	public PlayerEntity getPlayerEntityByName(World world, String name) {
		for (int j2 = 0; j2 < world.getPlayers().size(); ++j2) {
			PlayerEntity player = world.getPlayers().get(j2);

			if (name.equals(player.getName().toString())) {
				return player;
			}
		}

		return null;
	}
	
	@SubscribeEvent
	public void testingEvent(PlayerInteractEvent.RightClickItem event) {
//		World world = event.getWorld();
//		PlayerEntity player = event.getPlayer();
//
//		if(!world.isRemote) {
//			if(event.getItemStack().getItem() == Items.FEATHER) {
//				ArrayList<ServerPlayerEntity> players = new ArrayList<>();
//				players.add((ServerPlayerEntity)player);
//				setupShowdownAndTeleport(world, players);
//			}
//		}
	}
	
	public void setupShowdownAndTeleport(World world, ArrayList<ServerPlayerEntity> players) {
		double centerX = 0;
		double centerZ = 0;
		
		double centerX1 = centerX -21;
		double centerX2 = centerX +21;
		double centerZ1 = centerZ -21;
		double centerZ2 = centerZ +21;
		
		Block showdownBlock = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(UHCConfig.COMMON.showdownBlock.get()));
		
		if(showdownBlock == null) {
			showdownBlock = Blocks.STONE_BRICKS;
		}
		
		for(double i = centerX1; i <= centerX2; i++) {
			for(double j = centerZ1; j <= centerZ2; j++) {
				world.setBlockState(new BlockPos(i, 250, j), showdownBlock.getDefaultState());
				if(j == centerZ1 || j == centerZ2) {
					for(double k = 250; k <= 253; k++) {
						world.setBlockState(new BlockPos(i, k, j), showdownBlock.getDefaultState());
					}
				}
			}
			
			if(i == centerX1 || i == centerX2) {
				for(double j = centerZ1; j <= centerZ2; j++) {
					for(double k = 250; k <= 253; k++) {
						world.setBlockState(new BlockPos(i, k, j), showdownBlock.getDefaultState());
					}
				}
			}
		}
		
		int TeleportChoosing = 0;
		for(ServerPlayerEntity player : players) {
			TeleportChoosing++;
			if(TeleportChoosing > 8) {
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

	/**
	 * Only really does anything if there's a showdown
	 * @param event
	 */
	@SubscribeEvent
	public void checkShowDownWinner(TickEvent.WorldTickEvent event) {
		World world = event.world;
		ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);

		if(overworld != null) {
			UHCSaveData saveData = UHCSaveData.get(overworld);
			
			if(saveData.isUhcOnGoing() && saveData.isUhcIsFinished() && saveData.isUhcShowdown() && !saveData.isUhcShowdownFinished())
			{
				Scoreboard scoreboard = world.getScoreboard();
				MinecraftServer server = world.getServer();
				
				ArrayList<ServerPlayerEntity> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
				ArrayList<ServerPlayerEntity> playersAlive = new ArrayList<>();

				for(ServerPlayerEntity player : playerList) {
					if(player.interactionManager.getGameType() == GameType.SURVIVAL && player.getTeam() != scoreboard.getTeam("spectator")) {
						playersAlive.add(player);
					}
				}
				
				if(!playersAlive.isEmpty() && playersAlive != null) {
					playersAlive.removeIf(player -> player.interactionManager.getGameType() != GameType.SURVIVAL);
				}
				
				if(playersAlive.size() == 1) {
					PlayerEntity showdownWinner = playersAlive.get(0);
					WonTheShowdown(showdownWinner, playerList, world);
					saveData.setUhcShowdownFinished(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void throwEvent(ItemTossEvent event) {
		Entity entity = event.getEntity();
		ItemStack stack = event.getEntityItem().getItem();
		if(!entity.world.isRemote) {
			ServerWorld overworld = entity.world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);

				if(!saveData.isUhcOnGoing()) {
					if(stack.isItemEqual(new ItemStack(ModRegistry.UHC_BOOK.get()))) {
						event.setCanceled(true);
						event.setResult(Result.DENY);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void spawnRoomEvent(TickEvent.WorldTickEvent event){
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			ServerWorld overworld = event.world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);

				if(saveData.isSpawnRoom() && !saveData.isUhcOnGoing()) {
					RegistryKey<World> dimensionKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, saveData.getSpawnRoomDimension());
					World dimensionWorld = event.world.getServer().getWorld(dimensionKey);
					if(dimensionWorld != null) {
						double centerX1 = saveData.getBorderCenterX() -7;
						double centerX2 = saveData.getBorderCenterX() +7;
						double centerZ1 = saveData.getBorderCenterZ() -7;
						double centerZ2 = saveData.getBorderCenterZ() +7;
										
						for(double i = centerX1; i <= centerX2; i++)
						{
							double d0 = dimensionWorld.rand.nextGaussian() * 0.02D;
				            double d1 = dimensionWorld.rand.nextGaussian() * 0.02D;
				            double d2 = dimensionWorld.rand.nextGaussian() * 0.02D;
							for(double j = centerZ1; j <= centerZ2; j++)
							{
								if (dimensionWorld.rand.nextInt(10000) <= 4 && dimensionWorld.getBlockState(new BlockPos(i, 250, j)).isTransparent())
									((ServerWorld) dimensionWorld).spawnParticle(ParticleTypes.CRIT, i, 250 - 0.5, j, 3, d0, d1, d2, 0.0D);
								
								if(j == centerZ1 || j == centerZ2)
								{
									for(double k = 250; k <= 253; k++)
									{
										if (dimensionWorld.rand.nextInt(1000) <= 3 && dimensionWorld.getBlockState(new BlockPos(i, k, j)).isTransparent())
											((ServerWorld) dimensionWorld).spawnParticle(ParticleTypes.TOTEM_OF_UNDYING, i, k + 1.0D, j, 3, d0, d1, d2, 0.0D);
									}
								}
							}
							
							if(i == centerX1 || i == centerX2) {
								for(double j = centerZ1; j <= centerZ2; j++) {
									for(double k = 250; k <= 253; k++) {
										if (dimensionWorld.rand.nextInt(1000) <= 3 && dimensionWorld.getBlockState(new BlockPos(i, k, j)).isTransparent())
											((ServerWorld) dimensionWorld).spawnParticle(ParticleTypes.TOTEM_OF_UNDYING, i, k + 1.0D, j, 3, d0, d1, d2, 0.0D);
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
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			PlayerEntity player = event.player;
			World world = player.world;
			ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				
				if(saveData.isSpawnRoom() && !saveData.isUhcOnGoing()) {
					double centerX1 = saveData.getBorderCenterX() -7;
					double centerX2 = saveData.getBorderCenterX() +7;
					double centerZ1 = saveData.getBorderCenterZ() -7;
					double centerZ2 = saveData.getBorderCenterZ() +7;
					
					AxisAlignedBB hitbox = new AxisAlignedBB(centerX1 - 0.5f, 248 - 0.5f, centerZ1 - 0.5f, centerX2 + 0.5f, 260 + 0.5f, centerZ2 + 0.5f);
					ArrayList<PlayerEntity> collidingList = new ArrayList<>(world.getEntitiesWithinAABB(PlayerEntity.class, hitbox));
					
					if(!collidingList.contains(player) && !player.isCreative() && !player.isSpectator()) {
						if(player.world.getDimensionKey().getLocation().equals(saveData.getSpawnRoomDimension())) {
				            ((ServerPlayerEntity)player).connection.setPlayerLocation(saveData.getBorderCenterX(), 252, saveData.getBorderCenterZ(), player.rotationYaw, player.rotationPitch);
						} else if(!player.world.getDimensionKey().getLocation().equals(saveData.getSpawnRoomDimension())) {
							RegistryKey<World> dimensionKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, saveData.getSpawnRoomDimension());
							ServerWorld spawnRoomWorld = world.getServer().getWorld(dimensionKey);
							if(spawnRoomWorld != null) {
								player.changeDimension(spawnRoomWorld, new UHCTeleporter(player.getPosition()));
							} else {
								player.sendMessage(new StringTextComponent("Dimension invalid, please contact the host, Dimension: " + saveData.getSpawnRoomDimension()), Util.DUMMY_UUID);
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
    public void playerEditUHCEvent(PlayerTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			PlayerEntity player = event.player;
			CompoundNBT entityData = player.getPersistentData();
			World world = player.world;

			if(!world.isRemote) {
				if (!entityData.contains("canEditUHC"))
					entityData.putBoolean("canEditUHC", false);
				
				if(!entityData.getBoolean("canEditUHC") && player.hasPermissionLevel(2))
					entityData.putBoolean("canEditUHC", true);
				
				if(entityData.getBoolean("canEditUHC") && !player.hasPermissionLevel(2))
					entityData.putBoolean("canEditUHC", false);
			}
		}
	}	
	
	@SubscribeEvent
	public void onNewPlayerJoin(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();

		if(!player.world.isRemote) {
			ServerWorld overworld = player.world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				ServerPlayerEntity playerMP = (ServerPlayerEntity) player;

				if (saveData.isUhcOnGoing() && player.getTeam() == null) {
					playerMP.setGameType(GameType.SPECTATOR);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void DimensionChangeEvent(EntityTravelToDimensionEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			World world = player.world;
			if(!world.isRemote) {
				ServerWorld overworld = player.world.getServer().getWorld(World.OVERWORLD);
				if(overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					if(saveData.isUhcOnGoing()) {
						if(!saveData.isNetherEnabled())
						{
							if(event.getDimension() == World.THE_NETHER)
								event.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerPermissionClone(PlayerEvent.Clone event) {
		PlayerEntity originalPlayer = event.getOriginal();
		PlayerEntity newPlayer = event.getPlayer();

		CompoundNBT originalData = originalPlayer.getPersistentData();
		CompoundNBT newData = newPlayer.getPersistentData();
		
		if(!newPlayer.world.isRemote) {
			originalData.putBoolean("canEditUHC", newData.getBoolean("canEditUHC"));
			
			BlockPos deathPos = originalPlayer.getPosition();
			newData.putLong("deathPos", deathPos.toLong());
			newData.putString("deathDim", originalPlayer.world.getDimensionKey().getLocation().toString());
			((ServerPlayerEntity)newPlayer).func_242111_a(originalPlayer.world.getDimensionKey(), deathPos, originalPlayer.rotationYaw, true, false);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		PlayerEntity player = event.getPlayer();
		World world = player.world;
		if(!world.isRemote) {
			Scoreboard scoreboard = world.getScoreboard();
			ServerWorld overworld = player.world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				if(saveData.isUhcOnGoing()) {
					ScorePlayerTeam spectatorTeam = scoreboard.getTeam("spectator");
					scoreboard.addPlayerToTeam(player.getName().toString(), spectatorTeam);

					scoreboard.getObjective("health");
					scoreboard.removeObjectiveFromEntity(player.getName().toString(), scoreboard.getObjective("health"));
				}
			}
		}
	}
	
	public void YouWonTheUHC(ScorePlayerTeam team, ArrayList<ServerPlayerEntity> playerList, World world) {
		if(!world.isRemote) {
			ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				String teamName = team.getName();
				for(ServerPlayerEntity player : playerList) {
					if(player.getTeam() == team) {
						for(int i = 0; i < 10; i++) {
							if(world.rand.nextInt(10) < 3)
							{
								FireworkRocketEntity rocket = new FireworkRocketEntity(world, player.getPosX(), player.getPosY() + 3, player.getPosZ(), getFirework(world.rand));
								player.world.addEntity(rocket);
							}
						}
					}
					STitlePacket spackettitle1 = new STitlePacket(STitlePacket.Type.TITLE, new TranslationTextComponent("uhc.team.won", team.getColor() + teamName));
					player.connection.sendPacket(spackettitle1);
				}
			}
		}
	}
	
	public void SoloWonTheUHC(PlayerEntity winningPlayer, ArrayList<ServerPlayerEntity> playerList, World world) {
		if(!world.isRemote) {
			ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				for(ServerPlayerEntity player : playerList) {
					if(player.getName() == winningPlayer.getName()) {
						for(int i = 0; i < 10; i++) {
							if(world.rand.nextInt(10) < 3) {
								FireworkRocketEntity rocket = new FireworkRocketEntity(world, winningPlayer.getPosX(), winningPlayer.getPosY() + 3, winningPlayer.getPosZ(), getFirework(world.rand));
								player.world.addEntity(rocket);
							}
						}
					}
					STitlePacket spackettitle1 = new STitlePacket(STitlePacket.Type.TITLE, new TranslationTextComponent("uhc.player.won", TextFormatting.DARK_RED + winningPlayer.getName().toString()));
					player.connection.sendPacket(spackettitle1);
				}
			}
		}
	}
	
	public void WonTheShowdown(PlayerEntity winningPlayer, ArrayList<ServerPlayerEntity> playerList, World world) {
		if(!world.isRemote) {
			ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				for(ServerPlayerEntity player : playerList) {
					if(player.getName() == winningPlayer.getName()) {
						for(int i = 0; i < 10; i++) {
							if(world.rand.nextInt(10) < 3) {
								FireworkRocketEntity rocket = new FireworkRocketEntity(world, winningPlayer.getPosX(), winningPlayer.getPosY() + 3, winningPlayer.getPosZ(), getFirework(world.rand));
								player.world.addEntity(rocket);
							}
						}
					}
					STitlePacket spackettitle1 = new STitlePacket(STitlePacket.Type.TITLE, new TranslationTextComponent("uhc.player.showdown.won", TextFormatting.DARK_RED + winningPlayer.getName().toString()));
					player.connection.sendPacket(spackettitle1);
				}
			}
		}
	}
	
	public ItemStack getFirework(Random rand) {
		ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean("Flicker", true);
		nbt.putBoolean("Trail", true);

		int[] colors = new int[rand.nextInt(8) + 1];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = DyeColor.byId(rand.nextInt(16)).getId();
		}
		nbt.putIntArray("Colors", colors);
		byte type = (byte) (rand.nextInt(3) + 1);
		type = type == 3 ? 4 : type;
		nbt.putByte("Type", type);

		ListNBT explosions = new ListNBT();
		explosions.add(nbt);

		CompoundNBT fireworkTag = new CompoundNBT();
		fireworkTag.put("Explosions", explosions);
		fireworkTag.putByte("Flight", (byte) 1);
		nbt.put("Fireworks", fireworkTag);
		firework.setTag(new CompoundNBT());

        return firework;
	}
	
	@SubscribeEvent
	public void SyncPlayerWithData(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof PlayerEntity && !event.getWorld().isRemote) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			World world = event.getWorld();
			Scoreboard scoreboard = world.getScoreboard();
			ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
			if(overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);

				if(player.getTeam() == null) {
					ScorePlayerTeam soloTeam = scoreboard.getTeam("solo");
					scoreboard.addPlayerToTeam(player.getName().toString(), soloTeam);
				}

				UHCPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new UHCPacketMessage(saveData));
			}
		}
	}
}
