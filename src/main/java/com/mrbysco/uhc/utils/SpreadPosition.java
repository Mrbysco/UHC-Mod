package com.mrbysco.uhc.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

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

	public int getSpawnY(BlockGetter getter, int y) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(this.x, (double) (y + 1), this.z);
		boolean flag = getter.getBlockState(blockpos$mutableblockpos).isAir();
		blockpos$mutableblockpos.move(Direction.DOWN);

		boolean flag2;
		for (boolean flag1 = getter.getBlockState(blockpos$mutableblockpos).isAir(); blockpos$mutableblockpos.getY() > getter.getMinBuildHeight(); flag1 = flag2) {
			blockpos$mutableblockpos.move(Direction.DOWN);
			flag2 = getter.getBlockState(blockpos$mutableblockpos).isAir();
			if (!flag2 && flag1 && flag) {
				return blockpos$mutableblockpos.getY() + 1;
			}

			flag = flag1;
		}

		return y + 1;
	}

	public boolean isSafe(BlockGetter getter, int maxY) {
		BlockPos blockpos = BlockPos.containing(this.x, (double) (this.getSpawnY(getter, maxY) - 1), this.z);
		BlockState blockstate = getter.getBlockState(blockpos);
		return blockpos.getY() < maxY && !blockstate.liquid() && !blockstate.is(BlockTags.FIRE);
	}

	public void randomize(RandomSource rand, double minX, double minZ, double maxX, double maxZ) {
		this.x = Mth.nextDouble(rand, minX, maxX);
		this.z = Mth.nextDouble(rand, minZ, maxZ);
	}
}
