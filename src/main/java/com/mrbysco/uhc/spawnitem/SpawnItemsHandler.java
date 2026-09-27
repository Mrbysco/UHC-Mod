package com.mrbysco.uhc.spawnitem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrbysco.uhc.UltraHardCoremod;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpawnItemsHandler {
	public static final List<ItemObject> spawnItemList = new ArrayList<>();

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final File SPAWN_ITEMS_FOLDER = new File(FMLPaths.CONFIGDIR.get().toFile() + "/SpawnItems");
	public static final File SPAWN_ITEMS_FILE = new File(SPAWN_ITEMS_FOLDER, "SpawnItems.json");

	public static void initializeSpawnItems() {
		if (!SPAWN_ITEMS_FOLDER.exists() || !SPAWN_ITEMS_FILE.exists()) {
			SPAWN_ITEMS_FOLDER.mkdirs();

			List<ItemObject> items = new ArrayList<>(9);
			for (int i = 0; i < 9; i++) {
				items.add(new ItemObject("", "", 1));
			}

			SpawnItems initialConfig = new SpawnItems(items);
			try (FileWriter writer = new FileWriter(SPAWN_ITEMS_FILE)) {
				GSON.toJson(initialConfig, writer);
				writer.flush();
			} catch (IOException e) {
				UltraHardCoremod.LOGGER.trace("Failed to save Spawn Items", e);
			}
		}
	}

	public static void loadInitialConfig() {
		spawnItemList.clear();
		String fileName = SPAWN_ITEMS_FILE.getName();
		try (FileReader json = new FileReader(SPAWN_ITEMS_FILE)) {
			final SpawnItems spawnItems = GSON.fromJson(json, SpawnItems.class);
			if (spawnItems != null) {
				SpawnItemsHandler.spawnItemList.addAll(spawnItems.spawnItems());
			} else {
				UltraHardCoremod.LOGGER.error("Could not load Spawn Items from {}.", fileName);
			}
		} catch (final Exception e) {
			UltraHardCoremod.LOGGER.error("Unable to load file {}. Please make sure it's a valid json.", fileName);
			UltraHardCoremod.LOGGER.trace("Exception: ", e);
		}
	}
}
