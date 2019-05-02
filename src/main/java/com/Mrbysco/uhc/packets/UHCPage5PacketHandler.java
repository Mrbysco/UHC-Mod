package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.init.UHCSaveData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage5PacketHandler implements IMessageHandler<UHCPage5Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage5Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		World world = serverPlayer.getServerWorld();
		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
			NBTTagCompound playerData = serverPlayer.getEntityData();
			MinecraftServer server = world.getMinecraftServer();
			WorldServer wServer = server.getWorld(0);
			GameRules rules = wServer.getGameRules();
			WorldInfo wInfo = world.getWorldInfo();
			
			if(playerData.getBoolean("canEditUHC") == true)
			{
				if(message.mobGriefing)
				{
					if(rules.getBoolean("mobGriefing") == false)
						rules.setOrCreateGameRule("mobGriefing", String.valueOf(true));
				}
				else
				{
					if(rules.getBoolean("mobGriefing"))
						rules.setOrCreateGameRule("mobGriefing", String.valueOf(false));
				}
				
				if(message.weatherCycle)
				{
					if(rules.getBoolean("doWeatherCycle") == false)
						rules.setOrCreateGameRule("doWeatherCycle", String.valueOf(true));
				}
				else
				{
					if(world.isRaining())
						wInfo.setRaining(false);
					if(rules.getBoolean("doWeatherCycle"))
						rules.setOrCreateGameRule("doWeatherCycle", String.valueOf(false));
				}
				
				saveData.setWeatherEnabled(message.weatherCycle);
				saveData.setMobGriefing(message.mobGriefing);
				saveData.setApplyCustomHealth(message.customHealth);
				saveData.setMaxHealth(message.maxHealth);
				
				saveData.setRandomSpawns(message.randomSpawns);
				saveData.setSpreadDistance(message.spreadDistance);
				saveData.setSpreadMaxRange(message.spreadMaxRange);
				saveData.setSpreadRespectTeam(message.spreadRespectTeam);
				saveData.markDirty();
				
				ModPackethandler.INSTANCE.sendToAll(new UHCPacketMessage(saveData));
			}
			else
			{
				serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have permissions to edit the UHC book."));
			}
		}
		return null;
	}
}