package com.mrbysco.uhc.spawnitem;

import java.util.List;

public class SpawnItems {
	private final List<ItemObject> spawnItems;

	public SpawnItems(List<ItemObject> spawnObjectList) {
		this.spawnItems = spawnObjectList;
	}

	public List<ItemObject> spawnItems() {
		return spawnItems;
	}
}
