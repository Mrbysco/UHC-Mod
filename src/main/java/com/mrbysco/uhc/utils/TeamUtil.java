package com.mrbysco.uhc.utils;

import com.mrbysco.uhc.config.UHCConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;

import java.util.List;

public class TeamUtil {

	public static BlockPos getPosForTeam(ChatFormatting teamColor) {
		BlockPos pos = new BlockPos(0, -1, 0);
		List<? extends String> teamList = UHCConfig.COMMON.teamSpawns.get();
		if (teamList.size() == 14) {
			switch (teamColor) {
				case DARK_RED -> {
					String team1 = teamList.get(0);
					String[] position1 = team1.split(",");
					pos = new BlockPos(Integer.parseInt(position1[0]), Integer.parseInt(position1[1]), Integer.parseInt(position1[2]));
				}
				case GOLD -> {
					String team2 = teamList.get(1);
					String[] position2 = team2.split(",");
					pos = new BlockPos(Integer.parseInt(position2[0]), Integer.parseInt(position2[1]), Integer.parseInt(position2[2]));
				}
				case DARK_GREEN -> {
					String team3 = teamList.get(2);
					String[] position3 = team3.split(",");
					pos = new BlockPos(Integer.parseInt(position3[0]), Integer.parseInt(position3[1]), Integer.parseInt(position3[2]));
				}
				case DARK_AQUA -> {
					String team4 = teamList.get(3);
					String[] position4 = team4.split(",");
					pos = new BlockPos(Integer.parseInt(position4[0]), Integer.parseInt(position4[1]), Integer.parseInt(position4[2]));
				}
				case DARK_BLUE -> {
					String team5 = teamList.get(4);
					String[] position5 = team5.split(",");
					pos = new BlockPos(Integer.parseInt(position5[0]), Integer.parseInt(position5[1]), Integer.parseInt(position5[2]));
				}
				case DARK_PURPLE -> {
					String team6 = teamList.get(5);
					String[] position6 = team6.split(",");
					pos = new BlockPos(Integer.parseInt(position6[0]), Integer.parseInt(position6[1]), Integer.parseInt(position6[2]));
				}
				case DARK_GRAY -> {
					String team7 = teamList.get(6);
					String[] position7 = team7.split(",");
					pos = new BlockPos(Integer.parseInt(position7[0]), Integer.parseInt(position7[1]), Integer.parseInt(position7[2]));
				}
				case RED -> {
					String team8 = teamList.get(7);
					String[] position8 = team8.split(",");
					pos = new BlockPos(Integer.parseInt(position8[0]), Integer.parseInt(position8[1]), Integer.parseInt(position8[2]));
				}
				case YELLOW -> {
					String team9 = teamList.get(8);
					String[] position9 = team9.split(",");
					pos = new BlockPos(Integer.parseInt(position9[0]), Integer.parseInt(position9[1]), Integer.parseInt(position9[2]));
				}
				case GREEN -> {
					String team10 = teamList.get(9);
					String[] position10 = team10.split(",");
					pos = new BlockPos(Integer.parseInt(position10[0]), Integer.parseInt(position10[1]), Integer.parseInt(position10[2]));
				}
				case AQUA -> {
					String team11 = teamList.get(10);
					String[] position11 = team11.split(",");
					pos = new BlockPos(Integer.parseInt(position11[0]), Integer.parseInt(position11[1]), Integer.parseInt(position11[2]));
				}
				case BLUE -> {
					String team12 = teamList.get(11);
					String[] position12 = team12.split(",");
					pos = new BlockPos(Integer.parseInt(position12[0]), Integer.parseInt(position12[1]), Integer.parseInt(position12[2]));
				}
				case LIGHT_PURPLE -> {
					String team13 = teamList.get(12);
					String[] position13 = team13.split(",");
					pos = new BlockPos(Integer.parseInt(position13[0]), Integer.parseInt(position13[1]), Integer.parseInt(position13[2]));
				}
				case GRAY -> {
					String team14 = teamList.get(13);
					String[] position14 = team14.split(",");
					pos = new BlockPos(Integer.parseInt(position14[0]), Integer.parseInt(position14[1]), Integer.parseInt(position14[2]));
				}
				default -> {
				}
			}
		}

		return pos;
	}

	public static String getTeamNameFromInt(int value) {
		return switch (value) {
			default -> ChatFormatting.DARK_RED.getName();
			case 1 -> ChatFormatting.GOLD.getName();
			case 2 -> ChatFormatting.DARK_GREEN.getName();
			case 3 -> ChatFormatting.DARK_AQUA.getName();
			case 4 -> ChatFormatting.DARK_BLUE.getName();
			case 5 -> ChatFormatting.DARK_PURPLE.getName();
			case 6 -> ChatFormatting.DARK_GRAY.getName();
			case 7 -> ChatFormatting.RED.getName();
			case 8 -> ChatFormatting.YELLOW.getName();
			case 9 -> ChatFormatting.GREEN.getName();
			case 10 -> ChatFormatting.AQUA.getName();
			case 11 -> ChatFormatting.BLUE.getName();
			case 12 -> ChatFormatting.LIGHT_PURPLE.getName();
			case 13 -> ChatFormatting.GRAY.getName();
		};
	}
}
