package com.mrbysco.uhc.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public class UHCTransition {

	public static DimensionTransition makeTransition(ServerLevel destination, Entity entity, Vec3 pos) {
		return new DimensionTransition(destination, pos, Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.DO_NOTHING);
	}

}
