package com.mrbysco.uhc;

import com.mrbysco.uhc.commands.CommandTreeUHC;
import com.mrbysco.uhc.config.UltraHardCoremodConfigGen;
import com.mrbysco.uhc.handlers.AutoCookHandler;
import com.mrbysco.uhc.handlers.BorderHandler;
import com.mrbysco.uhc.handlers.GameRuleHandler;
import com.mrbysco.uhc.handlers.GraceHandler;
import com.mrbysco.uhc.handlers.ItemConversionHandler;
import com.mrbysco.uhc.handlers.ModCompatHandler;
import com.mrbysco.uhc.handlers.PlayerHealthHandler;
import com.mrbysco.uhc.handlers.ScoreboardHandler;
import com.mrbysco.uhc.handlers.TeamSpamHandler;
import com.mrbysco.uhc.handlers.TimedActionHandler;
import com.mrbysco.uhc.handlers.TimerHandler;
import com.mrbysco.uhc.handlers.UHCHandler;
import com.mrbysco.uhc.init.GuiHandler;
import com.mrbysco.uhc.lists.ConversionList;
import com.mrbysco.uhc.lists.CookList;
import com.mrbysco.uhc.lists.EntityDataChangeList;
import com.mrbysco.uhc.lists.RespawnList;
import com.mrbysco.uhc.lists.SpawnItemList;
import com.mrbysco.uhc.packets.ModPackethandler;
import com.mrbysco.uhc.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, 
name = Reference.MOD_NAME, 
version = Reference.VERSION, 
acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS,
dependencies = Reference.DEPENDENCIES)

public class UltraHardCoremod {
	@Instance(Reference.MOD_ID)
	public static UltraHardCoremod instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		logger.debug("Registering config / checking config");
		MinecraftForge.EVENT_BUS.register(new UltraHardCoremodConfigGen());
		
		logger.debug("Registering Packet");
		ModPackethandler.registerMessages();
		
		proxy.Preinit();
	}
	
	
	@EventHandler
    public void init(FMLInitializationEvent event)
	{
		logger.debug("Registering event handlers");
		MinecraftForge.EVENT_BUS.register(new UHCHandler());
		MinecraftForge.EVENT_BUS.register(new GameRuleHandler());
		MinecraftForge.EVENT_BUS.register(new TimerHandler());
		MinecraftForge.EVENT_BUS.register(new TimedActionHandler());
		MinecraftForge.EVENT_BUS.register(new ScoreboardHandler());
		MinecraftForge.EVENT_BUS.register(new BorderHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerHealthHandler());
		MinecraftForge.EVENT_BUS.register(new AutoCookHandler());
		MinecraftForge.EVENT_BUS.register(new ItemConversionHandler());
		MinecraftForge.EVENT_BUS.register(new ModCompatHandler());
		MinecraftForge.EVENT_BUS.register(new GraceHandler());
		MinecraftForge.EVENT_BUS.register(new TeamSpamHandler());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		logger.debug("Initialize default cook list");
		CookList.initializeAutoCook();
		
		logger.debug("Initialize default conversion list");
		ConversionList.initializeConversion();
		
		logger.debug("Initialize default spawn item list (which is empty)");
		SpawnItemList.initializeSpawnItems();
		
		RespawnList.initializeRespawnList();
		EntityDataChangeList.initializeDataChanges();
		
		proxy.Init();
    }
	
	@EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
		proxy.PostInit();
    }
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
    {
		event.registerServerCommand(new CommandTreeUHC());
    }
}