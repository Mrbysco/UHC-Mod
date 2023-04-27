package com.mrbysco.uhc.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpreadUtil {
	public static void spread(List<ServerPlayer> serverPlayers, SpreadPosition pos, double spreadDistance, double maxRange, Level level, boolean respectTeams) throws CommandRuntimeException {
		RandomSource random = RandomSource.create();
		double d0 = pos.x - maxRange;
		double d1 = pos.z - maxRange;
		double d2 = pos.x + maxRange;
		double d3 = pos.z + maxRange;
		SpreadPosition[] aSpreadUtil$position = createInitialPositions(random, respectTeams ? getNumberOfTeams(serverPlayers) : serverPlayers.size(), d0, d1, d2, d3);
		int i = spreadPositions(pos, spreadDistance, level, random, d0, d1, d2, d3, aSpreadUtil$position, respectTeams);
		double d4 = setPlayerPositions(serverPlayers, level, aSpreadUtil$position, respectTeams);
	}

	public static SpreadPosition[] createInitialPositions(RandomSource randomSource, int count, double minX, double maxX, double minZ, double maxZ) {
		SpreadPosition[] aSpreadUtil$position = new SpreadPosition[count];

		for (int i = 0; i < aSpreadUtil$position.length; ++i) {
			SpreadPosition SpreadUtil$position = new SpreadPosition();
			SpreadUtil$position.randomize(randomSource, minX, maxX, minZ, maxZ);
			aSpreadUtil$position[i] = SpreadUtil$position;
		}

		return aSpreadUtil$position;
	}

	public static int getNumberOfTeams(List<ServerPlayer> players) {
		Set<Team> set = Sets.<Team>newHashSet();

		for (Entity entity : players) {
			if (entity instanceof Player) {
				set.add(entity.getTeam());
			} else {
				set.add(null);
			}
		}

		return set.size();
	}

	public static int spreadPositions(SpreadPosition spreadPosition, double p_110668_2_, Level level, RandomSource random, double minX, double minZ, double maxX, double maxZ, SpreadPosition[] spreadPositions, boolean respectTeams) throws CommandRuntimeException {
		boolean flag = true;
		double d0 = 3.4028234663852886E38D;
		int i;

		for (i = 0; i < 10000 && flag; ++i) {
			flag = false;
			d0 = 3.4028234663852886E38D;

			for (int j = 0; j < spreadPositions.length; ++j) {
				SpreadPosition SpreadUtil$position = spreadPositions[j];
				int k = 0;
				SpreadPosition SpreadUtil$position1 = new SpreadPosition();

				for (int l = 0; l < spreadPositions.length; ++l) {
					if (j != l) {
						SpreadPosition SpreadUtil$position2 = spreadPositions[l];
						double d1 = SpreadUtil$position.dist(SpreadUtil$position2);
						d0 = Math.min(d1, d0);

						if (d1 < p_110668_2_) {
							++k;
							SpreadUtil$position1.x += SpreadUtil$position2.x - SpreadUtil$position.x;
							SpreadUtil$position1.z += SpreadUtil$position2.z - SpreadUtil$position.z;
						}
					}
				}

				if (k > 0) {
					SpreadUtil$position1.x /= (double) k;
					SpreadUtil$position1.z /= (double) k;
					double d2 = (double) SpreadUtil$position1.getLength();

					if (d2 > 0.0D) {
						SpreadUtil$position1.normalize();
						SpreadUtil$position.moveAway(SpreadUtil$position1);
					} else {
						SpreadUtil$position.randomize(random, minX, minZ, maxX, maxZ);
					}

					flag = true;
				}

				if (SpreadUtil$position.clamp(minX, minZ, maxX, maxZ)) {
					flag = true;
				}
			}

			if (!flag) {
				for (SpreadPosition SpreadUtil$position3 : spreadPositions) {
					if (!SpreadUtil$position3.isSafe(level)) {
						SpreadUtil$position3.randomize(random, minX, minZ, maxX, maxZ);
						flag = true;
					}
				}
			}
		}

		if (i >= 10000) {
			throw new CommandRuntimeException(Component.translatable("commands.spreadplayers.failure." + (respectTeams ? "teams" : "players"), spreadPositions.length, spreadPosition.x, spreadPosition.z, String.format("%.2f", d0)));
		} else {
			return i;
		}
	}

	public static double setPlayerPositions(List<ServerPlayer> players, Level level, SpreadPosition[] spreadPositions, boolean respectTeams) {
		double d0 = 0.0D;
		int i = 0;
		Map<Team, SpreadPosition> map = Maps.<Team, SpreadPosition>newHashMap();

		for (Entity entity : players) {
			SpreadPosition SpreadUtil$position;

			if (respectTeams) {
				Team team = entity instanceof Player ? entity.getTeam() : null;

				if (!map.containsKey(team)) {
					map.put(team, spreadPositions[i++]);
				}

				SpreadUtil$position = map.get(team);
			} else {
				SpreadUtil$position = spreadPositions[i++];
			}

			entity.teleportTo((double) ((float) Mth.floor(SpreadUtil$position.x) + 0.5F), (double) SpreadUtil$position.getSpawnY(level), (double) Mth.floor(SpreadUtil$position.z) + 0.5D);

			double d2 = Double.MAX_VALUE;

			for (SpreadPosition SpreadUtil$position1 : spreadPositions) {
				if (SpreadUtil$position != SpreadUtil$position1) {
					double d1 = SpreadUtil$position.dist(SpreadUtil$position1);
					d2 = Math.min(d1, d2);
				}
			}

			d0 += d2;
		}

		d0 = d0 / (double) players.size();
		return d0;
	}


}
