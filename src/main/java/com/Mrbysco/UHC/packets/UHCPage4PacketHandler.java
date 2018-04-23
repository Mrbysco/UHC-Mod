package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage4PacketHandler implements IMessageHandler<UHCPage4Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage4Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		World world = serverPlayer.getServerWorld();
		UHCSaveData saveData = UHCSaveData.getForWorld(world);
		NBTTagCompound playerData = serverPlayer.getEntityData();
		Scoreboard scoreboard = world.getScoreboard();
		
		if(playerData.getBoolean("canEditUHC") == true)
		{
			boolean healthExists = scoreboard.getObjective("health") != null;
			if(message.healthTab && healthExists)
			{
				ScoreObjective score = scoreboard.getObjective("health");
				if(scoreboard.getObjectiveInDisplaySlot(0) != score)
				{
					scoreboard.setObjectiveInDisplaySlot(0, score);
					scoreboard.setObjectiveInDisplaySlot(1, null);
					scoreboard.setObjectiveInDisplaySlot(2, null);
				}
			}
			if(message.healthSide && healthExists)
			{
				ScoreObjective score = scoreboard.getObjective("health");
				if(scoreboard.getObjectiveInDisplaySlot(1) != score)
				{
					scoreboard.setObjectiveInDisplaySlot(0, null);
					scoreboard.setObjectiveInDisplaySlot(1, score);
					scoreboard.setObjectiveInDisplaySlot(2, null);
				}
			}
			if(message.healthName && healthExists)
			{
				ScoreObjective score = scoreboard.getObjective("health");
				if(scoreboard.getObjectiveInDisplaySlot(2) != score)
				{
					scoreboard.setObjectiveInDisplaySlot(0, null);
					scoreboard.setObjectiveInDisplaySlot(1, null);
					scoreboard.setObjectiveInDisplaySlot(2, score);
				}
			}
			
			saveData.setRegenPotions(message.regenPotions);
			saveData.setLevel2Potions(message.level2Potions);
			saveData.setNotchApples(message.notchApples);
			saveData.setAutoCook(message.autoCook);
			saveData.setItemConversion(message.itemConversion);
			saveData.setNetherEnabled(message.netherTravel);
			saveData.setHealthInTab(message.healthTab);
			saveData.setHealthOnSide(message.healthSide);
			saveData.setHealthUnderName(message.healthName);
			saveData.markDirty();
			
			ModPackethandler.INSTANCE.sendToAll(new UHCPacketMessage(saveData));
		}
		else
		{
			serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have permissions to edit the UHC book."));
		}
		
		return null;
	}
}