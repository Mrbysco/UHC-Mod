package com.mrbysco.uhc.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class UHCTeleporter implements ITeleporter{
    private final BlockPos targetPos;

	public UHCTeleporter(BlockPos targetPos) {
        this.targetPos = targetPos;
    }

	public UHCTeleporter() {
		this.targetPos = null;
	}

	@Override
	public Entity placeEntity(Entity newEntity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		Entity entity = repositionEntity.apply(false); //Must be false or we fall on vanilla

		if(targetPos != null)
			entity.moveToBlockPosAndAngles(targetPos, yaw, entity.rotationPitch);

		return entity;
	}
}
