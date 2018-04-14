package com.Mrbysco.UHC.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class UHCTeleporter implements ITeleporter{

    private final BlockPos targetPos;

	public UHCTeleporter(BlockPos targetPos)
    {
        this.targetPos = targetPos;
    }

	@Override
    public void placeEntity(World world, Entity entity, float yaw)
    {
        entity.moveToBlockPosAndAngles(targetPos, yaw, entity.rotationPitch);
    }
}
