package com.mrbysco.uhc.lists;

import com.mrbysco.uhc.lists.info.RespawnInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class RespawnList {
	public static ArrayList<RespawnInfo> respawnList = new ArrayList<>();
	public static RespawnInfo respawn_info;
	
	public static void initializeRespawnList() {

	}
	
	public static void addBossRespawn(BlockPos pos, IBlockState state)
	{
		// Check if the info doesn't already exist
		respawn_info = new RespawnInfo(pos, state);
		if(respawnList.contains(respawn_info))
			return;
		else
			respawnList.add(respawn_info);
	}
}
