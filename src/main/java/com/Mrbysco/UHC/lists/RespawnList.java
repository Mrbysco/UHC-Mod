package com.Mrbysco.UHC.lists;

import java.util.ArrayList;

import com.Mrbysco.UHC.lists.info.AutoCookInfo;
import com.Mrbysco.UHC.lists.info.RespawnInfo;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class RespawnList {
	public static ArrayList<RespawnInfo> respawnList = new ArrayList<>();
	public static RespawnInfo respawn_info;
	
	public static void initializeAutoCook() {

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
