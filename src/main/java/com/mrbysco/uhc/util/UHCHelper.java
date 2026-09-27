package com.mrbysco.uhc.util;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.world.level.Level;

public class UHCHelper {
	public static boolean isUHCOnGoing(Level level) {
		if (level.isClientSide()) return false;
		return UHCSaveData.get(level).isUhcOnGoing();
	}
}
