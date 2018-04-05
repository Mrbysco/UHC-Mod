package com.Mrbysco.UHC.lists.info;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.BlockPos;

public class RespawnInfo {
	private BlockPos pos;
	private IBlockState state;
	public int timer;
	public ArrayList<Team> teamsReached;
	
	public RespawnInfo(BlockPos pos, IBlockState state) {
		this.pos = pos;
		this.state = state;
		this.timer = 0;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public IBlockState getState() {
		return state;
	}
}
