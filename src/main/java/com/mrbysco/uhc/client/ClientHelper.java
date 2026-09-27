package com.mrbysco.uhc.client;

import com.mrbysco.uhc.client.screen.UHCBookScreen;
import com.mrbysco.uhc.data.UHCSaveData;

public class ClientHelper {
	public static void updateBook(UHCSaveData data) {
		UHCBookScreen.saveData = data;
	}
}
