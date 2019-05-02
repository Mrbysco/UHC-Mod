package com.mrbysco.uhc.utils;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class SpreadPosition {
    double x;
    double z;

    public SpreadPosition()
    {
    }

    public SpreadPosition(double xIn, double zIn)
    {
        this.x = xIn;
        this.z = zIn;
    }

    double dist(SpreadPosition pos)
    {
        double d0 = this.x - pos.x;
        double d1 = this.z - pos.z;
        return Math.sqrt(d0 * d0 + d1 * d1);
    }

    void normalize()
    {
        double d0 = (double)this.getLength();
        this.x /= d0;
        this.z /= d0;
    }

    float getLength()
    {
        return MathHelper.sqrt(this.x * this.x + this.z * this.z);
    }

    public void moveAway(SpreadPosition pos)
    {
        this.x -= pos.x;
        this.z -= pos.z;
    }

    public boolean clamp(double p_111093_1_, double p_111093_3_, double p_111093_5_, double p_111093_7_)
    {
        boolean flag = false;

        if (this.x < p_111093_1_)
        {
            this.x = p_111093_1_;
            flag = true;
        }
        else if (this.x > p_111093_5_)
        {
            this.x = p_111093_5_;
            flag = true;
        }

        if (this.z < p_111093_3_)
        {
            this.z = p_111093_3_;
            flag = true;
        }
        else if (this.z > p_111093_7_)
        {
            this.z = p_111093_7_;
            flag = true;
        }

        return flag;
    }

    public int getSpawnY(World worldIn)
    {
        BlockPos blockpos = new BlockPos(this.x, 256.0D, this.z);

        while (blockpos.getY() > 0)
        {
            blockpos = blockpos.down();

            if (worldIn.getBlockState(blockpos).getMaterial() != Material.AIR)
            {
                return blockpos.getY() + 1;
            }
        }

        return 257;
    }

    public boolean isSafe(World worldIn)
    {
        BlockPos blockpos = new BlockPos(this.x, 256.0D, this.z);

        while (blockpos.getY() > 0)
        {
            blockpos = blockpos.down();
            Material material = worldIn.getBlockState(blockpos).getMaterial();

            if (material != Material.AIR)
            {
                return !material.isLiquid() && material != Material.FIRE;
            }
        }

        return false;
    }

    public void randomize(Random rand, double p_111097_2_, double p_111097_4_, double p_111097_6_, double p_111097_8_)
    {
        this.x = MathHelper.nextDouble(rand, p_111097_2_, p_111097_6_);
        this.z = MathHelper.nextDouble(rand, p_111097_4_, p_111097_8_);
    }
}
