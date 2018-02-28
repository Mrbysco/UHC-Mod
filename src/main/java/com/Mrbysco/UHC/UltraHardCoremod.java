package com.Mrbysco.UHC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Mrbysco.UHC.commands.CommandTreeUHC;
import com.Mrbysco.UHC.config.UltraHardCoremodConfigGen;
import com.Mrbysco.UHC.handlers.AutoCookHandler;
import com.Mrbysco.UHC.handlers.BorderHandler;
import com.Mrbysco.UHC.handlers.ItemConversionHandler;
import com.Mrbysco.UHC.handlers.PlayerHealthHandler;
import com.Mrbysco.UHC.handlers.ScoreboardHandler;
import com.Mrbysco.UHC.handlers.UHCHandler;
import com.Mrbysco.UHC.lists.ConversionList;
import com.Mrbysco.UHC.lists.CookList;
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

@Mod(modid = Reference.MOD_ID, 
name = Reference.MOD_NAME, 
version = Reference.VERSION, 
acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS)

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
		
		logger.debug("Initialize default cook list");
		CookList.initializeAutoCook();
		
		logger.debug("Initialize default conversion list");
		ConversionList.initializeConversion();
		
		proxy.Preinit();
	}
	
	
	@EventHandler
    public void init(FMLInitializationEvent event)
	{
		logger.debug("Registering event handlers");
		MinecraftForge.EVENT_BUS.register(new UHCHandler());
		MinecraftForge.EVENT_BUS.register(new ScoreboardHandler());
		MinecraftForge.EVENT_BUS.register(new BorderHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerHealthHandler());
		MinecraftForge.EVENT_BUS.register(new AutoCookHandler());
		MinecraftForge.EVENT_BUS.register(new ItemConversionHandler());
		
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