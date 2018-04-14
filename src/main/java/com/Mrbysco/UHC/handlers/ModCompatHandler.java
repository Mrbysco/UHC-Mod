package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;

import com.Mrbysco.UHC.config.UltraHardCoremodConfigGen;
import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.lists.RespawnList;
import com.Mrbysco.UHC.lists.info.RespawnInfo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModCompatHandler {
	
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
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			UHCTimerData timerData = UHCTimerData.getForWorld(world);
    		WorldBorder border = world.getWorldBorder();
			GameRules rules = world.getGameRules();
			
			if(!RespawnList.respawnList.isEmpty())
			{
				for (RespawnInfo info : RespawnList.respawnList)
				{
					BlockPos pos = info.getPos();
					IBlockState state = info.getState();
					AxisAlignedBB hitbox = new AxisAlignedBB(pos.getX() - 0.5f, 0 - 0.5f, pos.getZ() - 0.5f, pos.getX() + 0.5f, 256 + 0.5f, pos.getZ() + 0.5f)
							.expand(-50, -50, -50).expand(50, 50, 50);
					ArrayList<EntityPlayerMP> collidingList = new ArrayList<>(world.getEntitiesWithinAABB(EntityPlayerMP.class, hitbox));

					if(!collidingList.isEmpty())
					{
						for (EntityPlayerMP player : collidingList)
						{
							Team team = player.getTeam();
							
							if(team != null)
							{
								if(info.teamsReached.contains(team))
									return;
								else
								{
									if(info.timer != 0)
									{
										info.teamsReached.add(team);

										world.setBlockState(pos, state);
										info.timer = (UltraHardCoremodConfigGen.modCompat.twilightforest.twilightRespawnTime * 20);
									}
								}
							}
						}
					}
					if(info.timer > 0)
					       info.timer--;
				}
			}

		}
	}
	
}
