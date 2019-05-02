package com.Mrbysco.UHC.utils;

import com.Mrbysco.UHC.config.UltraHardCoremodConfigGen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class TeamUtil {
		
	public static BlockPos getPosForTeam(TextFormatting teamColor) {
		BlockPos pos = new BlockPos(0, 0, 0);
		
		switch (teamColor) {
		case DARK_RED:
			String team1 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam01;
			String[] position1 = team1.split(",");
			pos = new BlockPos(Integer.valueOf(position1[0]), Integer.valueOf(position1[1]), Integer.valueOf(position1[2]));
			break;
		case GOLD:
			String team2 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam02;
			String[] position2 = team2.split(",");
			pos = new BlockPos(Integer.valueOf(position2[0]), Integer.valueOf(position2[1]), Integer.valueOf(position2[2]));
			break;
		case DARK_GREEN:
			String team3 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam03;
			String[] position3 = team3.split(",");
			pos = new BlockPos(Integer.valueOf(position3[0]), Integer.valueOf(position3[1]), Integer.valueOf(position3[2]));
			break;
		case DARK_AQUA:
			String team4 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam04;
			String[] position4 = team4.split(",");
			pos = new BlockPos(Integer.valueOf(position4[0]), Integer.valueOf(position4[1]), Integer.valueOf(position4[2]));
			break;
		case DARK_BLUE:
			String team5 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam05;
			String[] position5 = team5.split(",");
			pos = new BlockPos(Integer.valueOf(position5[0]), Integer.valueOf(position5[1]), Integer.valueOf(position5[2]));
			break;
		case DARK_PURPLE:
			String team6 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam06;
			String[] position6 = team6.split(",");
			pos = new BlockPos(Integer.valueOf(position6[0]), Integer.valueOf(position6[1]), Integer.valueOf(position6[2]));
			break;
		case DARK_GRAY:
			String team7 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam07;
			String[] position7 = team7.split(",");
			pos = new BlockPos(Integer.valueOf(position7[0]), Integer.valueOf(position7[1]), Integer.valueOf(position7[2]));
			break;
		case RED:
			String team8 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam08;
			String[] position8 = team8.split(",");
			pos = new BlockPos(Integer.valueOf(position8[0]), Integer.valueOf(position8[1]), Integer.valueOf(position8[2]));
			break;
		case YELLOW:
			String team9 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam09;
			String[] position9 = team9.split(",");
			pos = new BlockPos(Integer.valueOf(position9[0]), Integer.valueOf(position9[1]), Integer.valueOf(position9[2]));
			break;
		case GREEN:
			String team10 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam10;
			String[] position10 = team10.split(",");
			pos = new BlockPos(Integer.valueOf(position10[0]), Integer.valueOf(position10[1]), Integer.valueOf(position10[2]));
			break;
		case AQUA:
			String team11 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam11;
			String[] position11 = team11.split(",");
			pos = new BlockPos(Integer.valueOf(position11[0]), Integer.valueOf(position11[1]), Integer.valueOf(position11[2]));
			break;
		case BLUE:
			String team12 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam12;
			String[] position12 = team12.split(",");
			pos = new BlockPos(Integer.valueOf(position12[0]), Integer.valueOf(position12[1]), Integer.valueOf(position12[2]));
			break;
		case LIGHT_PURPLE:
			String team13 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam13;
			String[] position13 = team13.split(",");
			pos = new BlockPos(Integer.valueOf(position13[0]), Integer.valueOf(position13[1]), Integer.valueOf(position13[2]));
			break;
		case GRAY:
			String team14 = UltraHardCoremodConfigGen.teamSpawns.spawnTeam14;
			String[] position14 = team14.split(",");
			pos = new BlockPos(Integer.valueOf(position14[0]), Integer.valueOf(position14[1]), Integer.valueOf(position14[2]));
			break;
		case BLACK:
			break;
		case BOLD:
			break;
		case ITALIC:
			break;
		case OBFUSCATED:
			break;
		case RESET:
			break;
		case STRIKETHROUGH:
			break;
		case UNDERLINE:
			break;
		case WHITE:
			break;
		default:
			break;
		}
		
		return pos;
	}
	
	public static String getTeamNameFromInt(int value)
	{		
		switch (value) {
		case 0:
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

		default:
			return TextFormatting.DARK_RED.getFriendlyName();
		}
	}
}
