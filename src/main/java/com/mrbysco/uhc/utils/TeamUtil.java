package com.mrbysco.uhc.utils;

import com.mrbysco.uhc.config.UHCConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class TeamUtil {
		
	public static BlockPos getPosForTeam(TextFormatting teamColor) {
		BlockPos pos = new BlockPos(0, -1, 0);
		List<? extends String> teamList = UHCConfig.COMMON.teamSpawns.get();
		if(teamList.size() == 14) {
			switch (teamColor) {
				case DARK_RED:
					String team1 = teamList.get(0);
					String[] position1 = team1.split(",");
					pos = new BlockPos(Integer.parseInt(position1[0]), Integer.parseInt(position1[1]), Integer.parseInt(position1[2]));
					break;
				case GOLD:
					String team2 = teamList.get(1);
					String[] position2 = team2.split(",");
					pos = new BlockPos(Integer.parseInt(position2[0]), Integer.parseInt(position2[1]), Integer.parseInt(position2[2]));
					break;
				case DARK_GREEN:
					String team3 = teamList.get(2);
					String[] position3 = team3.split(",");
					pos = new BlockPos(Integer.parseInt(position3[0]), Integer.parseInt(position3[1]), Integer.parseInt(position3[2]));
					break;
				case DARK_AQUA:
					String team4 = teamList.get(3);
					String[] position4 = team4.split(",");
					pos = new BlockPos(Integer.parseInt(position4[0]), Integer.parseInt(position4[1]), Integer.parseInt(position4[2]));
					break;
				case DARK_BLUE:
					String team5 = teamList.get(4);
					String[] position5 = team5.split(",");
					pos = new BlockPos(Integer.parseInt(position5[0]), Integer.parseInt(position5[1]), Integer.parseInt(position5[2]));
					break;
				case DARK_PURPLE:
					String team6 = teamList.get(5);
					String[] position6 = team6.split(",");
					pos = new BlockPos(Integer.parseInt(position6[0]), Integer.parseInt(position6[1]), Integer.parseInt(position6[2]));
					break;
				case DARK_GRAY:
					String team7 = teamList.get(6);
					String[] position7 = team7.split(",");
					pos = new BlockPos(Integer.parseInt(position7[0]), Integer.parseInt(position7[1]), Integer.parseInt(position7[2]));
					break;
				case RED:
					String team8 = teamList.get(7);
					String[] position8 = team8.split(",");
					pos = new BlockPos(Integer.parseInt(position8[0]), Integer.parseInt(position8[1]), Integer.parseInt(position8[2]));
					break;
				case YELLOW:
					String team9 = teamList.get(8);
					String[] position9 = team9.split(",");
					pos = new BlockPos(Integer.parseInt(position9[0]), Integer.parseInt(position9[1]), Integer.parseInt(position9[2]));
					break;
				case GREEN:
					String team10 = teamList.get(9);
					String[] position10 = team10.split(",");
					pos = new BlockPos(Integer.parseInt(position10[0]), Integer.parseInt(position10[1]), Integer.parseInt(position10[2]));
					break;
				case AQUA:
					String team11 = teamList.get(10);
					String[] position11 = team11.split(",");
					pos = new BlockPos(Integer.parseInt(position11[0]), Integer.parseInt(position11[1]), Integer.parseInt(position11[2]));
					break;
				case BLUE:
					String team12 = teamList.get(11);
					String[] position12 = team12.split(",");
					pos = new BlockPos(Integer.parseInt(position12[0]), Integer.parseInt(position12[1]), Integer.parseInt(position12[2]));
					break;
				case LIGHT_PURPLE:
					String team13 = teamList.get(12);
					String[] position13 = team13.split(",");
					pos = new BlockPos(Integer.parseInt(position13[0]), Integer.parseInt(position13[1]), Integer.parseInt(position13[2]));
					break;
				case GRAY:
					String team14 = teamList.get(13);
					String[] position14 = team14.split(",");
					pos = new BlockPos(Integer.parseInt(position14[0]), Integer.parseInt(position14[1]), Integer.parseInt(position14[2]));
					break;
				default:
					break;
			}
		}
		
		return pos;
	}
	
	public static String getTeamNameFromInt(int value) {
		switch (value) {
			default:
				return TextFormatting.DARK_RED.getFriendlyName();
			case 1:
				return TextFormatting.GOLD.getFriendlyName();
			case 2:
				return TextFormatting.DARK_GREEN.getFriendlyName();
			case 3:
				return TextFormatting.DARK_AQUA.getFriendlyName();
			case 4:
				return TextFormatting.DARK_BLUE.getFriendlyName();
			case 5:
				return TextFormatting.DARK_PURPLE.getFriendlyName();
			case 6:
				return TextFormatting.DARK_GRAY.getFriendlyName();
			case 7:
				return TextFormatting.RED.getFriendlyName();
			case 8:
				return TextFormatting.YELLOW.getFriendlyName();
			case 9:
				return TextFormatting.GREEN.getFriendlyName();
			case 10:
				return TextFormatting.AQUA.getFriendlyName();
			case 11:
				return TextFormatting.BLUE.getFriendlyName();
			case 12:
				return TextFormatting.LIGHT_PURPLE.getFriendlyName();
			case 13:
				return TextFormatting.GRAY.getFriendlyName();
		}
	}
}
