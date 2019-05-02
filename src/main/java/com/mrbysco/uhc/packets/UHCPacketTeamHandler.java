package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.init.UHCSaveData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPacketTeamHandler implements IMessageHandler<UHCPacketTeam, IMessage>
{
	@Override
	public IMessage onMessage(UHCPacketTeam message, MessageContext ctx) {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		return null;
	}
	
	private void handle(UHCPacketTeam message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

		if(DimensionManager.getWorld(0) != null)
		{
			UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
			NBTTagCompound playerData = serverPlayer.getEntityData();
			if(playerData.getBoolean(teamAntiSpam))
			{
				serverPlayer.sendMessage(new TextComponentTranslation("book.uhc.team.antispam"));
			}
			else
			{
				if(saveData.areTeamsLocked())
				{
					if(playerData.getBoolean("canEditUHC") == true)
					{
						switchTeams(serverPlayer, message, saveData.getMaxTeamSize());
					}
					else
					{
						serverPlayer.sendMessage(new TextComponentTranslation("book.uhc.team.locked"));
					}
				}
				else
				{
					switchTeams(serverPlayer, message, saveData.getMaxTeamSize());
				}
			}
		}
	}
	
	private final String teamAntiSpam = Reference.MOD_PREFIX + "team_anti_spam";
	
	private void switchTeams(EntityPlayerMP serverPlayer, UHCPacketTeam message, int maxTeamSize)
	{
		Scoreboard scoreboard = serverPlayer.getServerWorld().getScoreboard();
		NBTTagCompound playerData = serverPlayer.getEntityData();

		if(message.team.equals("solo"))
		{
			scoreboard.addPlayerToTeam(message.playerName, message.team);
			playerData.setBoolean(teamAntiSpam, true);
			sendTeamSwitchMessage(serverPlayer, message);
		}
		else
		{			
			if(maxTeamSize == -1)
			{
				scoreboard.addPlayerToTeam(message.playerName, message.team);
				playerData.setBoolean(teamAntiSpam, true);
				sendTeamSwitchMessage(serverPlayer, message);
			}
			else
			{
				if(scoreboard.getTeam(message.team).getMembershipCollection().size() < maxTeamSize) 
				{
					scoreboard.addPlayerToTeam(message.playerName, message.team);
					playerData.setBoolean(teamAntiSpam, true);
					sendTeamSwitchMessage(serverPlayer, message);
				}
				else
				{
					serverPlayer.sendMessage(new TextComponentTranslation("book.uhc.team.maxed", new Object[] {message.team}));
				}
			}
		}
	}
	
	private void sendTeamSwitchMessage(EntityPlayerMP serverPlayer, UHCPacketTeam message)
	{
		for(EntityPlayerMP players : serverPlayer.getServer().getPlayerList().getPlayers())
		{
			if(message.team.equals("solo"))
				players.sendMessage(new TextComponentTranslation("book.uhc.team.solo", new Object[] {message.playerName, TextFormatting.fromColorIndex(message.colorIndex) + message.teamName}));
			else
				players.sendMessage(new TextComponentTranslation("book.uhc.team.selected", new Object[] {message.playerName, TextFormatting.fromColorIndex(message.colorIndex) + message.teamName}));
		}
	}
}