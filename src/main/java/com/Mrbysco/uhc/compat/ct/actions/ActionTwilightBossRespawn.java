package com.mrbysco.uhc.compat.ct.actions;

import com.mrbysco.uhc.lists.RespawnList;
import crafttweaker.IAction;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;

public class ActionTwilightBossRespawn implements IAction {

	private final int ID;
	private final String boss;
	
	private final BlockPos pos;
	private final IBlockState state;

	public ActionTwilightBossRespawn(String pos, String boss) {
		String[] position = pos.split(",");
		this.boss = boss;
		this.pos = new BlockPos(Integer.valueOf(position[0]), Integer.valueOf(position[1]), Integer.valueOf(position[2]));
		
		String bossL = boss.toLowerCase();
		if(bossL == "naga")
			this.ID = 0;
		else if(bossL == "lich")
			this.ID = 1;
		else if(bossL == "hydra")
			this.ID = 2;
		else if(bossL.contains("ur") && bossL.contains("ghast"))
			this.ID = 3;
		else if(bossL.contains("phantom") || bossL.contains("knight"))
			this.ID = 4;
		else if(bossL.contains("queen") || bossL.contains("snow"))
			this.ID = 5;
		else if(bossL.contains("mino"))
			this.ID = 6;
		else if(bossL.contains("yeti"))
			this.ID = 7;
		else if(bossL.contains("quest"))
			this.ID = 8;
		else
			this.ID = 0;
		
		Block block = Block.getBlockFromName("twilightforest:boss_spawner");
		IBlockState state2 = block.getDefaultState();
		
		this.state = this.getStateForBoss(state2, this.ID);
	}
	
	@Optional.Method(modid = "twilightforest")
	private IBlockState getStateForBoss(IBlockState state1, int ID)
	{		
		return state1.withProperty(twilightforest.block.BlockTFBossSpawner.VARIANT, twilightforest.enums.BossVariant.getVariant(ID));
	}
	
	@Override
	public void apply() {
		if(state != null)
			RespawnList.addBossRespawn(this.pos, this.state);
	}

	@Override
	public String describe() {
		if(state != null)
			return String.format(this.state + " at " + this.pos + " has been added to the Respawn List");	
		else
			return String.format("Twilight Forest is not installed");
	}
}