package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPacketMessage implements IMessage
{
	protected boolean uhcStarting;
	
	protected boolean friendlyFire;
	protected boolean teamCollision;
	protected boolean healthInTab;
	protected boolean healthOnSide;
	protected boolean healthUnderName;
	
	protected boolean doDaylightCycle;
	protected boolean autoCook;
	protected boolean itemConversion;
	protected boolean applyCustomHealth;
	protected int maxHealth;
	
	protected int randomTeamSize;
	protected int maxTeamSize;
	
	protected int difficulty;
	
	protected int borderSize;
	protected double borderCenterX;
	protected double borderCenterZ;
	protected double originalBorderCenterX;
	protected double originalBorderCenterZ;
	
	protected boolean shrinkEnabled;
	protected int shrinkTimer;
	protected int shrinkSize;
	protected int shrinkOvertime;
	protected String shrinkMode;

	protected boolean timeLock;
	protected int timeLockTimer;
	protected String timeMode;
	
	protected boolean minuteMark;
	protected int minuteMarkTime;
	protected boolean timedNames;
	protected int nameTimer;
	protected boolean timedGlow;
	protected int glowTime;
	
	protected boolean netherEnabled;
	protected boolean regenPotions;
	protected boolean level2Potions;
	protected boolean notchApples;
	
	protected boolean weatherEnabled;
	protected boolean mobGriefing;
	
	public UHCPacketMessage() {}
	
	public UHCPacketMessage(boolean uhcStartingIn,boolean friendlyFireIn,boolean teamCollisionIn,boolean healthInTabIn,boolean healthOnSideIn,
			boolean healthUnderNameIn,boolean doDaylightCycleIn,boolean autoCookIn,boolean itemConversionIn,boolean applyCustomHealthIn,
			int maxHealthIn,int randomTeamSizeIn,int maxTeamSizeIn,int difficultyIn,int borderSizeIn,double borderCenterXIn,
			double borderCenterZIn,double originalBorderCenterXIn,double originalBorderCenterZIn,boolean shrinkEnabledIn,int shrinkTimerIn,
			int shrinkSizeIn,int shrinkOvertimeIn,String shrinkModeIn,boolean timeLockIn,int timeLockTimerIn,String timeModeIn,
			boolean minuteMarkIn,int minuteMarkTimeIn,boolean timedNamesIn,int nameTimerIn,boolean timedGlowIn,int glowTimeIn,
			boolean netherEnabledIn,boolean regenPotionsIn,boolean level2PotionsIn,boolean notchApplesIn,boolean weatherEnabledIn,
			boolean mobGriefingIn)
	{
		uhcStarting = uhcStartingIn;
		
		friendlyFire = friendlyFireIn;
		teamCollision = teamCollisionIn;
		healthInTab = healthInTabIn;
		healthOnSide = healthOnSideIn;
		healthUnderName = healthUnderNameIn;
		
		autoCook = doDaylightCycleIn;
		itemConversion = itemConversionIn;
		applyCustomHealth = applyCustomHealthIn;
		maxHealth = maxHealthIn;
		
		randomTeamSize = randomTeamSizeIn;
		maxTeamSize = maxTeamSizeIn;
		difficulty = difficultyIn;
		
		borderSize = borderSizeIn;
		borderCenterX = borderCenterXIn;
		borderCenterZ = borderCenterZIn;
		originalBorderCenterX = originalBorderCenterXIn;
		originalBorderCenterZ = originalBorderCenterZIn;
		
		shrinkEnabled = shrinkEnabledIn;
		shrinkTimer = shrinkTimerIn;
		shrinkSize = shrinkSizeIn;
		shrinkOvertime = shrinkOvertimeIn;
		shrinkMode = shrinkModeIn;
		
		timeLock = timeLockIn;
		timeLockTimer = timeLockTimerIn;
		timeMode = timeModeIn;
		
		minuteMark = minuteMarkIn;
		minuteMarkTime = minuteMarkTimeIn;
		timedNames = timedNamesIn;
		nameTimer = nameTimerIn;
		timedGlow = timedGlowIn;
		glowTime = glowTimeIn;
		
		netherEnabled = netherEnabledIn;
		regenPotions = regenPotionsIn;
		level2Potions = level2PotionsIn;
		notchApples = notchApplesIn;
		
		weatherEnabled = weatherEnabledIn;
		mobGriefing = mobGriefingIn;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		uhcStarting = buf.readBoolean();
		
		friendlyFire = buf.readBoolean();
		teamCollision = buf.readBoolean();
		healthInTab = buf.readBoolean();
		healthOnSide = buf.readBoolean();
		healthUnderName = buf.readBoolean();
		
		autoCook = buf.readBoolean();
		itemConversion = buf.readBoolean();
		applyCustomHealth = buf.readBoolean();
		maxHealth = buf.readInt();
		
		randomTeamSize = buf.readInt();
		maxTeamSize = buf.readInt();
		difficulty = buf.readInt();
		
		borderSize = buf.readInt();
		borderCenterX = buf.readDouble();
		borderCenterZ = buf.readDouble();
		originalBorderCenterX = buf.readDouble();
		originalBorderCenterZ = buf.readDouble();
		
		shrinkEnabled = buf.readBoolean();
		shrinkTimer = buf.readInt();
		shrinkSize = buf.readInt();
		shrinkOvertime = buf.readInt();
		shrinkMode = ByteBufUtils.readUTF8String(buf);
		
		timeLock = buf.readBoolean();
		timeLockTimer = buf.readInt();
		timeMode = ByteBufUtils.readUTF8String(buf);
		
		minuteMark = buf.readBoolean();
		minuteMarkTime = buf.readInt();
		timedNames = buf.readBoolean();
		nameTimer = buf.readInt();
		timedGlow = buf.readBoolean();
		glowTime = buf.readInt();
		
		netherEnabled = buf.readBoolean();
		regenPotions = buf.readBoolean();
		level2Potions = buf.readBoolean();
		notchApples = buf.readBoolean();
		
		weatherEnabled = buf.readBoolean();
		mobGriefing = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(uhcStarting);
		buf.writeBoolean(autoCook);
		buf.writeBoolean(itemConversion);
		buf.writeBoolean(applyCustomHealth);
		buf.writeInt(maxHealth);
		buf.writeBoolean(friendlyFire);
		buf.writeBoolean(teamCollision);
		buf.writeBoolean(healthInTab);
		buf.writeBoolean(healthOnSide);
		buf.writeBoolean(healthUnderName);
		buf.writeInt(randomTeamSize);
		buf.writeInt(maxTeamSize);
		buf.writeInt(difficulty);
		buf.writeInt(borderSize);
		buf.writeDouble(borderCenterX);
		buf.writeDouble(borderCenterZ);
		buf.writeDouble(originalBorderCenterX);
		buf.writeDouble(originalBorderCenterZ);
		buf.writeBoolean(shrinkEnabled);
		buf.writeInt(shrinkTimer);
		buf.writeInt(shrinkSize);
		buf.writeInt(shrinkOvertime);
		ByteBufUtils.writeUTF8String(buf, shrinkMode);
		buf.writeBoolean(timeLock);
		buf.writeInt(timeLockTimer);
		ByteBufUtils.writeUTF8String(buf, timeMode);
		buf.writeBoolean(minuteMark);
		buf.writeInt(minuteMarkTime);
		buf.writeBoolean(timedNames);
		buf.writeInt(nameTimer);
		buf.writeBoolean(timedGlow);
		buf.writeInt(glowTime);
		buf.writeBoolean(netherEnabled);
		buf.writeBoolean(weatherEnabled);
		buf.writeBoolean(mobGriefing);
	}
	
	public static class PacketHandler implements IMessageHandler<UHCPacketMessage, IMessage>
	{
		@Override
		public IMessage onMessage(UHCPacketMessage message, MessageContext ctx) {
			
	        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		    return null;
		}
		
		private void handle(UHCPacketMessage message, MessageContext ctx) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			UHCSaveData saveData = UHCSaveData.getForWorld(serverPlayer.getServerWorld());

			saveData.setUhcStarting(message.uhcStarting);
			
			saveData.setFriendlyFire(message.friendlyFire);
			saveData.setTeamCollision(message.teamCollision);
			saveData.setHealthInTab(message.healthInTab);
			saveData.setHealthOnSide(message.healthOnSide);
			saveData.setHealthUnderName(message.healthUnderName);
				
			saveData.setDoDaylightCycle(message.doDaylightCycle);
			saveData.setAutoCook(message.autoCook);
			saveData.setItemConversion(message.itemConversion);
			saveData.setApplyCustomHealth(message.applyCustomHealth);
			saveData.setMaxHealth(message.maxHealth);
				
			saveData.setRandomTeamSize(message.randomTeamSize);
			saveData.setMaxTeamSize(message.maxTeamSize);
				
			saveData.setDifficulty(message.difficulty);
				
			saveData.setBorderSize(message.borderSize);
			saveData.setBorderCenterX(message.borderCenterX);
			saveData.setBorderCenterZ(message.borderCenterZ);
			saveData.setOriginalBorderCenterX(message.originalBorderCenterX);
			saveData.setOriginalBorderCenterZ(message.originalBorderCenterZ);
				
			saveData.setShrinkEnabled(message.shrinkEnabled);
			saveData.setShrinkTimer(message.shrinkTimer);
			saveData.setShrinkSize(message.shrinkSize);
			saveData.setShrinkOvertime(message.shrinkOvertime);
			saveData.setShrinkMode(message.shrinkMode);

			saveData.setTimeLock(message.timeLock);
			saveData.setTimeLockTimer(message.timeLockTimer);
			saveData.setTimeMode(message.timeMode);
				
			saveData.setMinuteMark(message.minuteMark);
			saveData.setMinuteMarkTime(message.minuteMarkTime);
			saveData.setTimedNames(message.timedNames);
			saveData.setNameTimer(message.nameTimer);
			saveData.setTimedGlow(message.timedGlow);
			saveData.setGlowTime(message.glowTime);
				
			saveData.setNetherEnabled(message.netherEnabled);
			saveData.setRegenPotions(message.regenPotions);
			saveData.setLevel2Potions(message.level2Potions);
			saveData.setNotchApples(message.notchApples);
				
			saveData.setWeatherEnabled(message.weatherEnabled);
			saveData.setMobGriefing(message.mobGriefing);
			saveData.markDirty();
		}
	}
}