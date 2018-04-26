package com.Mrbysco.UHC.handlers;

import java.util.ArrayList;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TimedActionHandler {
	@SubscribeEvent
	public void ScoreboardStuff(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			MinecraftServer server = world.getMinecraftServer();
			ArrayList<EntityPlayerMP> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
			
			Scoreboard scoreboard = world.getScoreboard();
			UHCSaveData saveData = UHCSaveData.getForWorld(world);
			UHCTimerData timerData = UHCTimerData.getForWorld(world);
    		WorldBorder border = world.getWorldBorder();
			GameRules rules = world.getGameRules();

			if(saveData.isUhcOnGoing())
			{
				if(saveData.isTimeLock())
	    		{
					int timeLockTimer = timerData.getTimeLockTimer();
					boolean timeFlag = timeLockTimer == TimerHandler.tickTime(saveData.getTimeLockTimer());
					if(timeFlag && !saveData.isTimeLockApplied())
					{
						if(rules.getBoolean("doDaylightCycle"))
						{
							if(saveData.getTimeMode().equals("Day"))
							{
								if(world.isDaytime())
								{
									if(rules.getBoolean("doDaylightCycle"))
										rules.setOrCreateGameRule("doDaylightCycle", String.valueOf(false));
								}
							}
							else if(saveData.getTimeMode().equals("Night"))
							{
								if(!world.isDaytime())
								{
									if(rules.getBoolean("doDaylightCycle"))
										rules.setOrCreateGameRule("doDaylightCycle", String.valueOf(false));
								}
							}
							else
							{
								if(rules.getBoolean("doDaylightCycle"))
									rules.setOrCreateGameRule("doDaylightCycle", String.valueOf(true));
							}
						}
						
						for(EntityPlayerMP player : playerList)
						{
							player.sendMessage(new TextComponentTranslation("message.timelock", new Object[] {TextFormatting.YELLOW + saveData.getTimeMode()}));
						}
						
						saveData.setTimeLockApplied(true);
						saveData.markDirty();
					}
	    		}
	    		
	    		if(saveData.isMinuteMark())
	    		{
	    			int minuteMarkTimer = timerData.getMinuteMarkTimer();
	    			int minutes = saveData.getMinuteMarkTime();
	    			int minuteAmount = timerData.getMinuteMarkAmount();
					boolean minuteMarkFlag = minuteMarkTimer >= TimerHandler.tickTime(minutes);
					if(minuteMarkFlag)
					{
						if(minuteAmount != timerData.getMinuteMarkAmount())
							minuteAmount = timerData.getMinuteMarkAmount();
						
						++minuteAmount;
						timerData.setMinuteMarkAmount(minuteAmount);
						for(EntityPlayerMP player : playerList)
						{
							if(minuteAmount == 1)
								player.sendMessage(new TextComponentTranslation("message.minutemark.single.time", new Object[] {TextFormatting.YELLOW + String.valueOf(minutes * minuteAmount)}));
							else
								player.sendMessage(new TextComponentTranslation("message.minutemark.time", new Object[] {TextFormatting.YELLOW + String.valueOf(minutes * minuteAmount)}));
						}
						timerData.setMinuteMarkTimer(0);
						timerData.markDirty();
					}
	    		}
	    		
	    		if(saveData.isTimedNames() && !saveData.isTimedNamesApplied())
	    		{
	    			int timedNameTimer = timerData.getNameTimer();
	    			boolean timedNameFlag = timedNameTimer == TimerHandler.tickTime(saveData.getNameTimer());
	    			
	    			if(timedNameFlag)
	    			{
	    				for (ScorePlayerTeam team : scoreboard.getTeams())
	    				{
	    					if (team.getNameTagVisibility() != Team.EnumVisible.ALWAYS)
	    						team.setNameTagVisibility(Team.EnumVisible.ALWAYS);
	    				}
	    			}
	    			else
	    			{
	    				for (ScorePlayerTeam team : scoreboard.getTeams())
	    				{
	    					if (team.getNameTagVisibility() != Team.EnumVisible.HIDE_FOR_OTHER_TEAMS)
	    						team.setNameTagVisibility(Team.EnumVisible.HIDE_FOR_OTHER_TEAMS);
	    				}
	    			}

	    			for(EntityPlayerMP player : playerList)
					{
						player.sendMessage(new TextComponentTranslation("message.timedname", new Object[] {TextFormatting.YELLOW + saveData.getTimeMode()}));
					}
	    			
					saveData.setTimedNamesApplied(true);
					saveData.markDirty();
	    		}
	    		
	    		if(saveData.isTimedGlow() && !saveData.isGlowTimeApplied())
	    		{
	    			int timedGlowTimer = timerData.getGlowTimer();
	    			boolean timedGlowFlag = timedGlowTimer == TimerHandler.tickTime(saveData.getGlowTime());
	    			
	    			if(timedGlowFlag)
	    			{
	    				for (EntityPlayerMP player : playerList)
	    				{
	    					if(!player.isGlowing())
	    						player.setGlowing(true);
	    				}
	    			}
	    			else
	    			{
	    				for (EntityPlayerMP player : playerList)
	    				{
	    					if(player.isGlowing())
	    						player.setGlowing(false);
	    				}
	    			}
	    			
	    			for(EntityPlayerMP player : playerList)
					{
						player.sendMessage(new TextComponentTranslation("message.timedglow", new Object[] {TextFormatting.YELLOW + saveData.getTimeMode()}));
					}

					saveData.setGlowTimeApplied(true);
					saveData.markDirty();
	    		}
			}
		}
	}
}
