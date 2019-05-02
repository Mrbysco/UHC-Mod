package com.Mrbysco.UHC.handlers;

import com.Mrbysco.UHC.UltraHardCoremod;
import com.Mrbysco.UHC.config.UltraHardCoremodConfigGen;
import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.lists.EntityDataChangeList;
import com.Mrbysco.UHC.lists.RespawnList;
import com.Mrbysco.UHC.lists.info.RespawnInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.UUID;

public class ModCompatHandler {
	public int twilightBossGraceTimer;
	
	@Optional.Method(modid = "twilightforest")
	@SubscribeEvent
	public void TwilightHandler(TickEvent.WorldTickEvent event)
	{
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			
			Scoreboard scoreboard = world.getScoreboard();
			if(DimensionManager.getWorld(0) != null)
			{
				MinecraftServer server = world.getMinecraftServer();
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
				ArrayList<EntityPlayerMP> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
				
				ArrayList<Entity> entityList = new ArrayList<>(world.loadedEntityList);

				if(!playerList.isEmpty() && saveData.isUhcOnGoing() && !RespawnList.respawnList.isEmpty())
				{
					if (world.getWorldTime() % 20 == 0)
					{
						if(!saveData.getTwilightRespawn())
						{
							if(timerData.getTwilightBossGraceTimer() != this.twilightBossGraceTimer)
							{
								this.twilightBossGraceTimer = timerData.getTwilightBossGraceTimer();
								if(saveData.getTwilightRespawn())
								{
									saveData.setTwilightRespawn(false);
									saveData.markDirty();
								}
							}
							
							if(timerData.getTwilightBossGraceTimer() >= TimerHandler.tickTime(UltraHardCoremodConfigGen.modCompat.twilightforest.twilightRespawnTime))
							{
								this.twilightBossGraceTimer = TimerHandler.tickTime(UltraHardCoremodConfigGen.modCompat.twilightforest.twilightRespawnTime);
								saveData.setTwilightRespawn(true);
								saveData.markDirty();
							}
							else
							{
								++this.twilightBossGraceTimer;
								timerData.setTwilightBossGraceTimer(this.twilightBossGraceTimer);
								timerData.markDirty();
							}
						}
					}
				}
				
				if(saveData.isUhcOnGoing() && !saveData.isUhcIsFinished() && saveData.getTwilightRespawn())
				{
					if(!RespawnList.respawnList.isEmpty())
					{
						for (RespawnInfo info : RespawnList.respawnList)
						{
							BlockPos pos = info.getPos();
							IBlockState state = info.getState();
							AxisAlignedBB hitbox = new AxisAlignedBB(pos.getX() - 0.5f, 0 - 0.5f, pos.getZ() - 0.5f, pos.getX() + 0.5f, 256 + 0.5f, pos.getZ() + 0.5f)
									.expand(-50, -50, -50).expand(50, 50, 50);
							ArrayList<EntityPlayerMP> collidingList = new ArrayList<>(world.getEntitiesWithinAABB(EntityPlayerMP.class, hitbox));
							
							ArrayList<EntityMob> collidingBossMobs = new ArrayList<>(world.getEntitiesWithinAABB(EntityMob.class, hitbox));

							if(world.getBlockState(info.getPos()).getBlock() instanceof twilightforest.block.BlockTFBossSpawner)
							{
								if(!info.isSpawnerExists())
									info.setSpawnerExists(true);
							}
							
							if(!collidingBossMobs.isEmpty() && !info.isBossExists())
							{
								for (EntityMob mob : collidingBossMobs)
								{
									if(!mob.isNonBoss() && EntityRegistry.getEntry(mob.getClass()).getRegistryName().getNamespace().equals("twilightforest"))
									{
										info.setBossExists(true);
									}
								}
							}
							
							if(!collidingList.isEmpty())
							{
								for (EntityPlayerMP player : collidingList)
								{
									Team team = player.getTeam();
									
									if(team != null && team != scoreboard.getTeam("solo") && !player.isSpectator())
									{
										if(info.teamsReached.contains(team))
											return;
										else
										{
											if(!info.isBossExists() && !info.isSpawnerExists())
											{
												info.teamsReached.add(team);
												
												world.setBlockState(pos, state);
												info.setBossExists(true);
											}
										}
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
	public void mobDataChanger(EntityJoinWorldEvent event)
	{
		World world = event.getWorld();
		if(!world.isRemote)
		{
			Entity entity = event.getEntity();
			if(EntityDataChangeList.dataMap != null && !EntityDataChangeList.dataMap.isEmpty())
			{
				if(EntityDataChangeList.dataMap.containsKey(entity.getClass()))
				{
					NBTTagCompound nbttagcompound = entity.writeToNBT(new NBTTagCompound());
			        NBTTagCompound nbttagcompound1 = nbttagcompound.copy();
		            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
		            
		            try
		            {
		            	String data = EntityDataChangeList.dataMap.get(entity.getClass());
		            	if(data.startsWith("{") && data.endsWith("}"))
		            	{
			                nbttagcompound2 = JsonToNBT.getTagFromJson(data);
		            	}
		            	else
		            	{
			                nbttagcompound2 = JsonToNBT.getTagFromJson("{" + data + "}");
		            	}
		            }
		            catch (NBTException nbtexception)
		            {
		            	UltraHardCoremod.logger.error("nope... " +  nbtexception);
		            }

		            if(!nbttagcompound2.isEmpty())
		            {
		            	UUID uuid = entity.getUniqueID();
		                nbttagcompound.merge(nbttagcompound2);
		                entity.setUniqueId(uuid);
	                    entity.readFromNBT(nbttagcompound);
		            }
				}
			}
		}
	}
	
	public ResourceLocation getEntityLocation(String name)
	{
		String[] splitResource = name.split(":");
		if (splitResource.length != 2)
			return null;
		else
			return new ResourceLocation(splitResource[0], splitResource[1]);
	}
}
