package com.mrbysco.uhc.lists.info;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Team;

import java.util.ArrayList;

public class RespawnInfo {
	private final BlockState state;
	public ArrayList<Team> teamsReached;
	public boolean spawnerExists;
	public boolean bossExists;

	public RespawnInfo(BlockState state) {
		this.state = state;
		this.teamsReached = new ArrayList<>();
		this.spawnerExists = false;
		this.bossExists = false;
	}

	public BlockState getState() {
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
