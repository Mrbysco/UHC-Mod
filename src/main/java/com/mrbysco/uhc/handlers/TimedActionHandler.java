package com.mrbysco.uhc.handlers;

import com.mrbysco.uhc.init.UHCSaveData;
import com.mrbysco.uhc.init.UHCTimerData;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class TimedActionHandler {
	@SubscribeEvent
	public void ScoreboardStuff(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer())
		{
			World world = event.world;
			MinecraftServer server = world.getMinecraftServer();
			ArrayList<EntityPlayerMP> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
			
			Scoreboard scoreboard = world.getScoreboard();
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
				UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
//	    		WorldBorder border = world.getWorldBorder();
				GameRules rules = world.getGameRules();
	
				if(saveData.isUhcOnGoing())
				{
					if(saveData.isTimeLock())
		    		{
						int timeLockTimer = timerData.getTimeLockTimer();
						boolean timeFlag = timeLockTimer == TimerHandler.tickTime(saveData.getTimeLockTimer());
						if(timeFlag)
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
							
							if(!saveData.isTimeLockApplied())
							{
								for(EntityPlayerMP player : playerList)
								{
			    					player.sendMessage(new TextComponentTranslation("message.timelock", new Object[] {TextFormatting.GOLD + saveData.getTimeMode()}));
								}
								
								saveData.setTimeLockApplied(true);
								saveData.markDirty();
							}
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
								EntityFireworkRocket rocket = new EntityFireworkRocket(world, player.posX, player.posY + 2, player.posZ, ItemStack.EMPTY);
								player.playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, 1F, 1F);
	    						player.world.spawnEntity(rocket);

								int minutesDone = minutes * minuteAmount;
								if(minuteAmount == 1)
								{
		    						player.sendStatusMessage(new TextComponentTranslation("message.minutemark.single.time", new Object[] {TextFormatting.YELLOW + String.valueOf(minutesDone)}), true);
		    						player.sendMessage(new TextComponentTranslation("message.minutemark.single.time", new Object[] {TextFormatting.YELLOW + String.valueOf(minutesDone)}));
								}
								else
								{
			    					player.sendStatusMessage(new TextComponentTranslation("message.minutemark.time", new Object[] {TextFormatting.YELLOW + String.valueOf(minutesDone)}), true);
			    					player.sendMessage(new TextComponentTranslation("message.minutemark.time", new Object[] {TextFormatting.YELLOW + String.valueOf(minutesDone)}));
								}
							}
							timerData.setMinuteMarkTimer(0);
							timerData.markDirty();
						}
		    		}
		    		
		    		if(saveData.isTimedNames())
		    		{
		    			int timedNameTimer = timerData.getNameTimer();
		    			boolean timedNameFlag = timedNameTimer == TimerHandler.tickTime(saveData.getNameTimer());
		    			
		    			if(timedNameFlag && !saveData.isTimedNamesApplied())
		    			{
		    				for (ScorePlayerTeam team : scoreboard.getTeams())
		    				{
		    					if (team.getNameTagVisibility() != Team.EnumVisible.ALWAYS)
		    						team.setNameTagVisibility(Team.EnumVisible.ALWAYS);
		    				}
		    				
		    				for(EntityPlayerMP player : playerList)
							{
		    					player.sendMessage(new TextComponentTranslation("message.timedname"));
							}
	
							saveData.setTimedNamesApplied(true);
							saveData.markDirty();
		    			}
		    			else
		    			{
		    				for (ScorePlayerTeam team : scoreboard.getTeams())
		    				{
		    					if (team.getNameTagVisibility() != Team.EnumVisible.HIDE_FOR_OTHER_TEAMS)
		    						team.setNameTagVisibility(Team.EnumVisible.HIDE_FOR_OTHER_TEAMS);
		    				}
		    			}
		    		}
		    		
		    		if(saveData.isTimedGlow())
		    		{
		    			int timedGlowTimer = timerData.getGlowTimer();
		    			boolean timedGlowFlag = timedGlowTimer == TimerHandler.tickTime(saveData.getGlowTime());
	
		    			if(timedGlowFlag)
		    			{
		    				for (EntityPlayerMP player : playerList)
		    				{
		    					if(player.getActivePotionEffect(MobEffects.GLOWING) == null)
		    						player.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 32767 * 20, 10, true, false));
		    				}
		    				
		    				if(!saveData.isGlowTimeApplied())
		    				{
		    					for (EntityPlayerMP player : playerList)
		    					{
			    					player.sendMessage(new TextComponentTranslation("message.timedglow"));
		    					}
								saveData.setGlowTimeApplied(true);
								saveData.markDirty();
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
		    		}
				}
			}
		}
	}
}
