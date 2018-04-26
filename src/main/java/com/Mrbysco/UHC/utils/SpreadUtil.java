package com.Mrbysco.UHC.utils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SpreadUtil {
	public static void spread(List<EntityPlayerMP> players, SpreadPosition pos, double spreadDistance, double maxRange, World worldIn, boolean respectTeams) throws CommandException
    {
        Random random = new Random();
        double d0 = pos.x - maxRange;
        double d1 = pos.z - maxRange;
        double d2 = pos.x + maxRange;
        double d3 = pos.z + maxRange;
        SpreadPosition[] aSpreadUtil$position = createInitialPositions(random, respectTeams ? getNumberOfTeams(players) : players.size(), d0, d1, d2, d3);
        int i = spreadPositions(pos, spreadDistance, worldIn, random, d0, d1, d2, d3, aSpreadUtil$position, respectTeams);
        double d4 = setPlayerPositions(players, worldIn, aSpreadUtil$position, respectTeams);
    }
	
	public static SpreadPosition[] createInitialPositions(Random p_110670_1_, int p_110670_2_, double p_110670_3_, double p_110670_5_, double p_110670_7_, double p_110670_9_)
    {
        SpreadPosition[] aSpreadUtil$position = new SpreadPosition[p_110670_2_];

        for (int i = 0; i < aSpreadUtil$position.length; ++i)
        {
            SpreadPosition SpreadUtil$position = new SpreadPosition();
            SpreadUtil$position.randomize(p_110670_1_, p_110670_3_, p_110670_5_, p_110670_7_, p_110670_9_);
            aSpreadUtil$position[i] = SpreadUtil$position;
        }

        return aSpreadUtil$position;
    }
	
	public static int getNumberOfTeams(List<EntityPlayerMP> players)
    {
        Set<Team> set = Sets.<Team>newHashSet();

        for (Entity entity : players)
        {
            if (entity instanceof EntityPlayer)
            {
                set.add(entity.getTeam());
            }
            else
            {
                set.add(null);
            }
        }

        return set.size();
    }

    public static int spreadPositions(SpreadPosition p_110668_1_, double p_110668_2_, World worldIn, Random random, double minX, double minZ, double maxX, double maxZ, SpreadPosition[] p_110668_14_, boolean respectTeams) throws CommandException
    {
        boolean flag = true;
        double d0 = 3.4028234663852886E38D;
        int i;

        for (i = 0; i < 10000 && flag; ++i)
        {
            flag = false;
            d0 = 3.4028234663852886E38D;

            for (int j = 0; j < p_110668_14_.length; ++j)
            {
                SpreadPosition SpreadUtil$position = p_110668_14_[j];
                int k = 0;
                SpreadPosition SpreadUtil$position1 = new SpreadPosition();

                for (int l = 0; l < p_110668_14_.length; ++l)
                {
                    if (j != l)
                    {
                        SpreadPosition SpreadUtil$position2 = p_110668_14_[l];
                        double d1 = SpreadUtil$position.dist(SpreadUtil$position2);
                        d0 = Math.min(d1, d0);

                        if (d1 < p_110668_2_)
                        {
                            ++k;
                            SpreadUtil$position1.x += SpreadUtil$position2.x - SpreadUtil$position.x;
                            SpreadUtil$position1.z += SpreadUtil$position2.z - SpreadUtil$position.z;
                        }
                    }
                }

                if (k > 0)
                {
                    SpreadUtil$position1.x /= (double)k;
                    SpreadUtil$position1.z /= (double)k;
                    double d2 = (double)SpreadUtil$position1.getLength();

                    if (d2 > 0.0D)
                    {
                        SpreadUtil$position1.normalize();
                        SpreadUtil$position.moveAway(SpreadUtil$position1);
                    }
                    else
                    {
                        SpreadUtil$position.randomize(random, minX, minZ, maxX, maxZ);
                    }

                    flag = true;
                }

                if (SpreadUtil$position.clamp(minX, minZ, maxX, maxZ))
                {
                    flag = true;
                }
            }

            if (!flag)
            {
                for (SpreadPosition SpreadUtil$position3 : p_110668_14_)
                {
                    if (!SpreadUtil$position3.isSafe(worldIn))
                    {
                        SpreadUtil$position3.randomize(random, minX, minZ, maxX, maxZ);
                        flag = true;
                    }
                }
            }
        }

        if (i >= 10000)
        {
            throw new CommandException("commands.spreadplayers.failure." + (respectTeams ? "teams" : "players"), new Object[] {p_110668_14_.length, p_110668_1_.x, p_110668_1_.z, String.format("%.2f", d0)});
        }
        else
        {
            return i;
        }
    }

    public static double setPlayerPositions(List<EntityPlayerMP> players, World worldIn, SpreadPosition[] p_110671_3_, boolean p_110671_4_)
    {
        double d0 = 0.0D;
        int i = 0;
        Map<Team, SpreadPosition> map = Maps.<Team, SpreadPosition>newHashMap();

        for (int j = 0; j < players.size(); ++j)
        {
            Entity entity = players.get(j);
            SpreadPosition SpreadUtil$position;

            if (p_110671_4_)
            {
                Team team = entity instanceof EntityPlayer ? entity.getTeam() : null;

                if (!map.containsKey(team))
                {
                    map.put(team, p_110671_3_[i++]);
                }

                SpreadUtil$position = map.get(team);
            }
            else
            {
                SpreadUtil$position = p_110671_3_[i++];
            }

            entity.setPositionAndUpdate((double)((float)MathHelper.floor(SpreadUtil$position.x) + 0.5F), (double)SpreadUtil$position.getSpawnY(worldIn), (double)MathHelper.floor(SpreadUtil$position.z) + 0.5D);

            double d2 = Double.MAX_VALUE;

            for (SpreadPosition SpreadUtil$position1 : p_110671_3_)
            {
                if (SpreadUtil$position != SpreadUtil$position1)
                {
                    double d1 = SpreadUtil$position.dist(SpreadUtil$position1);
                    d2 = Math.min(d1, d2);
                }
            }

            d0 += d2;
        }

        d0 = d0 / (double)players.size();
        return d0;
    }
	
    
}
