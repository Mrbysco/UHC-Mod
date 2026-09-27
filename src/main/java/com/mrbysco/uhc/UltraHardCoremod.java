package com.mrbysco.uhc;

import com.mojang.logging.LogUtils;
import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.registry.UHCDataAttachments;
import com.mrbysco.uhc.registry.UHCGamerules;
import com.mrbysco.uhc.registry.UHCRegistry;
import com.mrbysco.uhc.spawnitem.SpawnItemReloadManager;
import com.mrbysco.uhc.spawnitem.SpawnItemsHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(Reference.MOD_ID)
public class UltraHardCoremod {
	public static final Logger LOGGER = LogUtils.getLogger();

	public UltraHardCoremod(IEventBus eventBus, Dist dist, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, UHCConfig.commonSpec);

		UHCRegistry.ITEMS.register(eventBus);
		UHCDataAttachments.ATTACHMENT_TYPES.register(eventBus);
		UHCGamerules.init();

		eventBus.addListener(this::loadComplete);

		NeoForge.EVENT_BUS.register(new SpawnItemReloadManager());

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		SpawnItemsHandler.initializeSpawnItems();
	}
}
