package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;

import com.Mrbysco.UHC.config.UltraHardCoremodConfigGen;
import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.lists.RespawnList;
import com.Mrbysco.UHC.lists.info.RespawnInfo;
import com.Mrbysco.UHC.utils.TimerThing;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import twilightforest.block.BlockTFBossSpawner;

public class ModCompatHandler {
	public TimerThing milliTime;
	
	public ModCompatHandler() {
		milliTime = new TimerThing();
	}
	
	@Optional.Method(modid = "twilightforest")
	@SubscribeEvent
	public void TwilightHandler(TickEvent.WorldTickEvent event)
	{
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			MinecraftServer server = world.getMinecraftServer();
			ArrayList<EntityPlayerMP> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
			
			Scoreboard scoreboard = world.getScoreboard();
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
	    		WorldBorder border = world.getWorldBorder();
				GameRules rules = world.getGameRules();
				
				ArrayList<Entity> entityList = new ArrayList<>(world.loadedEntityList);
				
				if(saveData.isUhcOnGoing() && !saveData.isUhcIsFinished())
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
							int respawnTime = (UltraHardCoremodConfigGen.modCompat.twilightforest.twilightRespawnTime * 1200);

							if(world.getBlockState(info.getPos()).getBlock() instanceof BlockTFBossSpawner)
							{
								if(!info.isSpawnerExists())
									info.setSpawnerExists(true);
							}
							
							if(!collidingBossMobs.isEmpty() && !info.isBossExists())
							{
								for (EntityMob mob : collidingBossMobs)
								{
									if(!mob.isNonBoss())
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
											if(info.timer == 0 && !info.isBossExists() && !info.isSpawnerExists())
											{
												info.teamsReached.add(team);
												
												world.setBlockState(pos, state);
												info.setBossExists(true);
												info.timer = respawnTime;
											}
										}
									}
								}
							}
							if (System.currentTimeMillis() > milliTime.getMilliTime() + 1000L)
							{
								milliTime.setMilliTimeToCurrent();
								
								if(info.timer > 0)
								       info.timer--;
							}
						}
					}
				}
			}
		}
	}
}
