package com.Mrbysco.UHC;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Mrbysco.UHC.commands.CommandTreeUHC;
import com.Mrbysco.UHC.config.UltraHardCoremodConfigGen;
import com.Mrbysco.UHC.handlers.AutoCookHandler;
import com.Mrbysco.UHC.handlers.BorderHandler;
import com.Mrbysco.UHC.handlers.GameRuleHandler;
import com.Mrbysco.UHC.handlers.ItemConversionHandler;
import com.Mrbysco.UHC.handlers.PlayerHealthHandler;
import com.Mrbysco.UHC.handlers.ScoreboardHandler;
import com.Mrbysco.UHC.handlers.UHCHandler;
import com.Mrbysco.UHC.init.GuiHandler;
import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.lists.ConversionList;
import com.Mrbysco.UHC.lists.CookList;
import com.Mrbysco.UHC.packets.ModPackethandler;
import com.Mrbysco.UHC.proxy.CommonProxy;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

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
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new ScoreboardHandler());
		MinecraftForge.EVENT_BUS.register(new BorderHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerHealthHandler());
		MinecraftForge.EVENT_BUS.register(new AutoCookHandler());
		MinecraftForge.EVENT_BUS.register(new ItemConversionHandler());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
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
	
	public int shrinkTimeUntil;
	public int timeLockTimer;
	public int minuteMarkTimer;
	public int nameTimer;
	public int glowTimer;
	
	@SubscribeEvent
	public void UHCBookEvent(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			UHCTimerData timerData = UHCTimerData.getForWorld(world);

			if(saveData.isShrinkEnabled())
			{
				if(timerData.getShrinkTimeUntil() != this.shrinkTimeUntil)
					this.shrinkTimeUntil = timerData.getShrinkTimeUntil();
				
				if(timerData.getShrinkTimeUntil() >= tickTime(saveData.getShrinkTimer()))
				{
					this.shrinkTimeUntil = tickTime(saveData.getShrinkTimer());
					if(timerData.getShrinkTimeUntil() != tickTime(saveData.getShrinkTimer()))
					{
						timerData.setShrinkTimeUntil(tickTime(saveData.getShrinkTimer()));
						timerData.markDirty();
					}
				}
				else
				{
					int theTimer = this.shrinkTimeUntil++;
					timerData.setShrinkTimeUntil(theTimer);
					timerData.markDirty();
				}
			}
			if(saveData.isTimeLock())
			{
				if(timerData.getTimeLockTimer() != this.timeLockTimer)
					this.timeLockTimer = timerData.getTimeLockTimer();
				
				if(timerData.getTimeLockTimer() >= tickTime(saveData.getTimeLockTimer()))
				{
					this.timeLockTimer = tickTime(saveData.getTimeLockTimer());
					if(timerData.getTimeLockTimer() != tickTime(saveData.getTimeLockTimer()))
					{
						timerData.setTimeLockTimer(tickTime(saveData.getTimeLockTimer()));
						timerData.markDirty();
					}
				}
				else
				{
					int theTimer = this.timeLockTimer++;
					timerData.setTimeLockTimer(theTimer);
					timerData.markDirty();
				}			
			}
			if(saveData.isMinuteMark())
			{
				if(timerData.getMinuteMarkTimer() != this.minuteMarkTimer)
					this.minuteMarkTimer = timerData.getMinuteMarkTimer();
				
				if(timerData.getMinuteMarkTimer() >= tickTime(saveData.getMinuteMarkTime()))
				{
					this.minuteMarkTimer = tickTime(saveData.getMinuteMarkTime());
					if(timerData.getMinuteMarkTimer() != tickTime(saveData.getMinuteMarkTime()))
					{
						timerData.setMinuteMarkTimer(tickTime(saveData.getMinuteMarkTime()));
						timerData.markDirty();
					}
				}
				else
				{
					int theTimer = this.minuteMarkTimer++;
					timerData.setMinuteMarkTimer(theTimer);
					timerData.markDirty();
				}
			}
			if(saveData.isTimedNames())
			{
				if(timerData.getNameTimer() != this.nameTimer)
					this.nameTimer = timerData.getNameTimer();
				
				if(timerData.getNameTimer() >= tickTime(saveData.getNameTimer()))
				{
					this.nameTimer = tickTime(saveData.getNameTimer());
					if(timerData.getNameTimer() != tickTime(saveData.getNameTimer()))
					{
						timerData.setNameTimer(tickTime(saveData.getNameTimer()));
						timerData.markDirty();
					}
				}
				else
				{
					int theTimer = this.nameTimer++;
					timerData.setNameTimer(theTimer);
					timerData.markDirty();
				}			
			}
			if(saveData.isTimedGlow())
			{
				if(timerData.getGlowTimer() != this.glowTimer)
					this.glowTimer = timerData.getGlowTimer();
				
				if(timerData.getGlowTimer() >= tickTime(saveData.getGlowTime()))
				{
					this.glowTimer = tickTime(saveData.getGlowTime());
					if(timerData.getGlowTimer() != tickTime(saveData.getGlowTime()))
					{
						timerData.setGlowTimer(tickTime(saveData.getGlowTime()));
						timerData.markDirty();
					}
				}
				else
				{
					int theTimer = this.glowTimer++;
					timerData.setGlowTimer(theTimer);
					timerData.markDirty();
				}			
			}
		}
	}
	
	public int tickTime(int oldTime)
	{
		return oldTime * 1200;
	}
	
	public void resetTimer()
	{
		this.shrinkTimeUntil = 0;
		this.timeLockTimer = 0;
		this.minuteMarkTimer = 0;
		this.nameTimer = 0;
		this.glowTimer = 0;	
	}
}