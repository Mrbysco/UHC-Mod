package com.mrbysco.uhc.spawnitem;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class SpawnItemReloadManager implements ResourceManagerReloadListener {
	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		SpawnItemsHandler.loadInitialConfig();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onAddReloadListeners(AddReloadListenerEvent event) {
		event.addListener(this);
	}
}
