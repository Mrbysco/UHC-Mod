package com.mrbysco.uhc.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;

public class SpreadPosition {
	double x;
	double z;

	public SpreadPosition() {
	}

	public SpreadPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}

	double dist(SpreadPosition pos) {
		double d0 = this.x - pos.x;
		double d1 = this.z - pos.z;
		return Math.sqrt(d0 * d0 + d1 * d1);
	}

	void normalize() {
		double d0 = (double) this.getLength();
		this.x /= d0;
		this.z /= d0;
	}

	float getLength() {
		return Mth.sqrt((float) (this.x * this.x + this.z * this.z));
	}

	public void moveAway(SpreadPosition pos) {
		this.x -= pos.x;
		this.z -= pos.z;
	}

	public boolean clamp(double minX, double minZ, double maxX, double maxZ) {
		boolean flag = false;

		if (this.x < minX) {
			this.x = minX;
			flag = true;
		} else if (this.x > maxX) {
			this.x = maxX;
			flag = true;
		}

		if (this.z < minZ) {
			this.z = minZ;
			flag = true;
		} else if (this.z > maxZ) {
			this.z = maxZ;
			flag = true;
		}

		return flag;
	}

	public int getSpawnY(Level level) {
		BlockPos blockpos = new BlockPos(this.x, 256.0D, this.z);

		while (blockpos.getY() > 0) {
			blockpos = blockpos.below();

			if (level.getBlockState(blockpos).getMaterial() != Material.AIR) {
				return blockpos.getY() + 1;
			}
		}

		return 257;
	}

	public boolean isSafe(Level level) {
		BlockPos blockpos = new BlockPos(this.x, 256.0D, this.z);

		while (blockpos.getY() > 0) {
			blockpos = blockpos.below();
			Material material = level.getBlockState(blockpos).getMaterial();

			if (material != Material.AIR) {
				return !material.isLiquid() && material != Material.FIRE;
			}
		}

		return false;
	}

	public void randomize(RandomSource rand, double minX, double minZ, double maxX, double maxZ) {
		this.x = Mth.nextDouble(rand, minX, maxX);
		this.z = Mth.nextDouble(rand, minZ, maxZ);
	}
}
