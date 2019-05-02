package com.mrbysco.uhc.lists.info;

import net.minecraft.block.state.IBlockState;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class RespawnInfo {
	private BlockPos pos;
	private final IBlockState state;
	public ArrayList<Team> teamsReached;
	public boolean spawnerExists;
	public boolean bossExists;
	
	public RespawnInfo(BlockPos pos, IBlockState state) {
		this.pos = pos;
		this.state = state;
		this.teamsReached = new ArrayList<>();
		this.spawnerExists = false;
		this.bossExists = false;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public IBlockState getState() {
		return state;
	}
	
	public boolean isBossExists() {
		return bossExists;
	}
	
	public void setBossExists(boolean bossExists) {
		this.bossExists = bossExists;
	}
	
	public boolean isSpawnerExists() {
		return spawnerExists;
	}
	
	public void setSpawnerExists(boolean spawnerExists) {
		this.spawnerExists = spawnerExists;
	}
}
