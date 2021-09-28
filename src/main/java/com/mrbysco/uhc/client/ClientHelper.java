package com.mrbysco.uhc.client;

import com.mrbysco.uhc.client.screen.UHCBookScreen;
import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.client.Minecraft;

public class ClientHelper {
	public static void updateBook(UHCSaveData data) {
		Minecraft mc = Minecraft.getInstance();
		UHCBookScreen.saveData = data;
	}
}
