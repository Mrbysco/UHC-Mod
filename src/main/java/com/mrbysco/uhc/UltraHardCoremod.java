package com.mrbysco.uhc;

import com.mrbysco.uhc.commands.UHCCommands;
import com.mrbysco.uhc.config.UHCConfig;
import com.mrbysco.uhc.handler.AutoCookHandler;
import com.mrbysco.uhc.handler.BorderHandler;
import com.mrbysco.uhc.handler.GameRuleHandler;
import com.mrbysco.uhc.handler.GraceHandler;
import com.mrbysco.uhc.handler.ItemConversionHandler;
import com.mrbysco.uhc.handler.ModCompatHandler;
import com.mrbysco.uhc.handler.PlayerHealthHandler;
import com.mrbysco.uhc.handler.ScoreboardHandler;
import com.mrbysco.uhc.handler.TeamSpamHandler;
import com.mrbysco.uhc.handler.TimedActionHandler;
import com.mrbysco.uhc.handler.TimerHandler;
import com.mrbysco.uhc.handler.UHCHandler;
import com.mrbysco.uhc.lists.SpawnItemList;
import com.mrbysco.uhc.packets.UHCPacketHandler;
import com.mrbysco.uhc.registry.ModRecipes;
import com.mrbysco.uhc.registry.ModRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Reference.MOD_ID)

public class UltraHardCoremod {
	public static final Logger LOGGER = LogManager.getLogger();

	public UltraHardCoremod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UHCConfig.commonSpec);
		eventBus.register(UHCConfig.class);

		eventBus.addListener(this::setup);

		ModRecipes.RECIPE_SERIALIZERS.register(eventBus);
		ModRecipes.RECIPE_TYPES.register(eventBus);
		ModRegistry.ITEMS.register(eventBus);

		MinecraftForge.EVENT_BUS.register(new UHCHandler());
		MinecraftForge.EVENT_BUS.register(new AutoCookHandler());
		MinecraftForge.EVENT_BUS.register(new GameRuleHandler());
		MinecraftForge.EVENT_BUS.register(new TimerHandler());
		MinecraftForge.EVENT_BUS.register(new TimedActionHandler());
		MinecraftForge.EVENT_BUS.register(new ScoreboardHandler());
		MinecraftForge.EVENT_BUS.register(new BorderHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerHealthHandler());
		MinecraftForge.EVENT_BUS.register(new AutoCookHandler());
		MinecraftForge.EVENT_BUS.register(new ItemConversionHandler());
		MinecraftForge.EVENT_BUS.register(new GraceHandler());
		MinecraftForge.EVENT_BUS.register(new TeamSpamHandler());
		MinecraftForge.EVENT_BUS.register(new ModCompatHandler());

		MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			//TODO: insert client stuff here
		});
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.debug("Registering Packets");
		UHCPacketHandler.registerMessages();

		SpawnItemList.initializeSpawnItems();
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		UHCCommands.initializeCommands(event.getDispatcher());
	}
}