package com.mrbysco.uhc.handler;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class TimedActionHandler {
	@SubscribeEvent
	public void ScoreboardStuff(TickEvent.WorldTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.START) && event.side.isServer()) {
			World world = event.world;
			MinecraftServer server = world.getServer();
			ServerWorld overworld = world.getServer().getWorld(World.OVERWORLD);
			if(server != null && overworld != null) {
				ArrayList<ServerPlayerEntity> playerList = new ArrayList<>(server.getPlayerList().getPlayers());
				Scoreboard scoreboard = world.getScoreboard();

				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCTimerData timerData = UHCTimerData.get(overworld);
				GameRules rules = world.getGameRules();
	
				if(saveData.isUhcOnGoing()) {
					if(saveData.isTimeLock()) {
						int timeLockTimer = timerData.getTimeLockTimer();
						boolean timeFlag = timeLockTimer == TimerHandler.tickTime(saveData.getTimeLockTimer());
						if(timeFlag) {
							if(rules.getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
								if(saveData.getTimeMode().equals("Day")) {
									if(world.isDaytime()) {
										if(rules.getBoolean(GameRules.DO_DAYLIGHT_CYCLE))
											rules.get(GameRules.DO_DAYLIGHT_CYCLE).set(false, server);
									}
								} else if(saveData.getTimeMode().equals("Night")) {
									if(!world.isDaytime()) {
										if(rules.getBoolean(GameRules.DO_DAYLIGHT_CYCLE))
											rules.get(GameRules.DO_DAYLIGHT_CYCLE).set(false, server);
									}
								} else {
									if(rules.getBoolean(GameRules.DO_DAYLIGHT_CYCLE))
										rules.get(GameRules.DO_DAYLIGHT_CYCLE).set(true, server);
								}
							}
							
							if(!saveData.isTimeLockApplied()) {
								for(ServerPlayerEntity player : playerList) {
			    					player.sendMessage(new TranslationTextComponent("message.timelock", TextFormatting.GOLD + saveData.getTimeMode()), Util.DUMMY_UUID);
								}
								
								saveData.setTimeLockApplied(true);
								saveData.markDirty();
							}
						}
		    		}
		    		
		    		if(saveData.isMinuteMark()) {
		    			int minuteMarkTimer = timerData.getMinuteMarkTimer();
		    			int minutes = saveData.getMinuteMarkTime();
		    			int minuteAmount = timerData.getMinuteMarkAmount();
						boolean minuteMarkFlag = minuteMarkTimer >= TimerHandler.tickTime(minutes);
						if(minuteMarkFlag) {
							if(minuteAmount != timerData.getMinuteMarkAmount())
								minuteAmount = timerData.getMinuteMarkAmount();
							
							++minuteAmount;
							timerData.setMinuteMarkAmount(minuteAmount);
							for(ServerPlayerEntity player : playerList) {
								FireworkRocketEntity rocket = new FireworkRocketEntity(world, player.getPosX(), player.getPosY() + 2, player.getPosZ(), ItemStack.EMPTY);
								player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 1F, 1F);
	    						player.world.addEntity(rocket);

								int minutesDone = minutes * minuteAmount;
								if(minuteAmount == 1) {
		    						player.sendStatusMessage(new TranslationTextComponent("message.minutemark.single.time", TextFormatting.YELLOW + String.valueOf(minutesDone)), true);
		    						player.sendMessage(new TranslationTextComponent("message.minutemark.single.time", TextFormatting.YELLOW + String.valueOf(minutesDone)), Util.DUMMY_UUID);
								} else {
			    					player.sendStatusMessage(new TranslationTextComponent("message.minutemark.time", TextFormatting.YELLOW + String.valueOf(minutesDone)), true);
			    					player.sendMessage(new TranslationTextComponent("message.minutemark.time", TextFormatting.YELLOW + String.valueOf(minutesDone)), Util.DUMMY_UUID);
								}
							}
							timerData.setMinuteMarkTimer(0);
							timerData.markDirty();
						}
		    		}
		    		
		    		if(saveData.isTimedNames()) {
		    			int timedNameTimer = timerData.getNameTimer();
		    			boolean timedNameFlag = timedNameTimer == TimerHandler.tickTime(saveData.getNameTimer());
		    			
		    			if(timedNameFlag && !saveData.isTimedNamesApplied()) {
		    				for (ScorePlayerTeam team : scoreboard.getTeams()) {
		    					if (team.getNameTagVisibility() != Team.Visible.ALWAYS)
		    						team.setNameTagVisibility(Team.Visible.ALWAYS);
		    				}
		    				
		    				for(ServerPlayerEntity player : playerList) {
		    					player.sendMessage(new TranslationTextComponent("message.timedname"), Util.DUMMY_UUID);
							}
	
							saveData.setTimedNamesApplied(true);
							saveData.markDirty();
		    			} else {
		    				for (ScorePlayerTeam team : scoreboard.getTeams()) {
		    					if (team.getNameTagVisibility() != Team.Visible.HIDE_FOR_OTHER_TEAMS)
		    						team.setNameTagVisibility(Team.Visible.HIDE_FOR_OTHER_TEAMS);
		    				}
		    			}
		    		}
		    		
		    		if(saveData.isTimedGlow()) {
		    			int timedGlowTimer = timerData.getGlowTimer();
		    			boolean timedGlowFlag = timedGlowTimer == TimerHandler.tickTime(saveData.getGlowTime());
	
		    			if(timedGlowFlag) {
		    				for (ServerPlayerEntity player : playerList) {
		    					if(player.getActivePotionEffect(Effects.GLOWING) == null)
		    						player.addPotionEffect(new EffectInstance(Effects.GLOWING, 32767 * 20, 10, true, false));
		    				}
		    				
		    				if(!saveData.isGlowTimeApplied()) {
		    					for (ServerPlayerEntity player : playerList) {
			    					player.sendMessage(new TranslationTextComponent("message.timedglow"), Util.DUMMY_UUID);
		    					}
								saveData.setGlowTimeApplied(true);
								saveData.markDirty();
		    				}
		    			} else {
		    				for (ServerPlayerEntity player : playerList) {
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
