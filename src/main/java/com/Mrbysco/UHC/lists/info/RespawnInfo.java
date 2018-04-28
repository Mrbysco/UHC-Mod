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
	public boolean spawnerExists;
	public boolean bossExists;
	
	public RespawnInfo(BlockPos pos, IBlockState state) {
		this.pos = pos;
		this.state = state;
		this.timer = 0;
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
