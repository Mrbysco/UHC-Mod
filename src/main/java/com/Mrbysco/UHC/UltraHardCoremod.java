package com.Mrbysco.UHC;

import com.Mrbysco.UHC.commands.CommandTreeUHC;
import com.Mrbysco.UHC.config.UltraHardCoremodConfigGen;
import com.Mrbysco.UHC.handlers.AutoCookHandler;
import com.Mrbysco.UHC.handlers.BorderHandler;
import com.Mrbysco.UHC.handlers.GameRuleHandler;
import com.Mrbysco.UHC.handlers.GraceHandler;
import com.Mrbysco.UHC.handlers.ItemConversionHandler;
import com.Mrbysco.UHC.handlers.ModCompatHandler;
import com.Mrbysco.UHC.handlers.PlayerHealthHandler;
import com.Mrbysco.UHC.handlers.ScoreboardHandler;
import com.Mrbysco.UHC.handlers.TeamSpamHandler;
import com.Mrbysco.UHC.handlers.TimedActionHandler;
import com.Mrbysco.UHC.handlers.TimerHandler;
import com.Mrbysco.UHC.handlers.UHCHandler;
import com.Mrbysco.UHC.init.GuiHandler;
import com.Mrbysco.UHC.lists.ConversionList;
import com.Mrbysco.UHC.lists.CookList;
import com.Mrbysco.UHC.lists.EntityDataChangeList;
import com.Mrbysco.UHC.lists.RespawnList;
import com.Mrbysco.UHC.lists.SpawnItemList;
import com.Mrbysco.UHC.packets.ModPackethandler;
import com.Mrbysco.UHC.proxy.CommonProxy;
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