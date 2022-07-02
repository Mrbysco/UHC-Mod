package com.mrbysco.uhc.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class UHCTeleporter implements ITeleporter {
	private final BlockPos targetPos;

	public UHCTeleporter(BlockPos targetPos) {
		this.targetPos = targetPos;
	}

	public UHCTeleporter() {
		this.targetPos = null;
	}

	@Override
	public Entity placeEntity(Entity newEntity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		Entity entity = repositionEntity.apply(false); //Must be false or we fall on vanilla

		if (targetPos != null)
			entity.moveTo(targetPos, yaw, entity.getXRot());

		return entity;
	}
}
