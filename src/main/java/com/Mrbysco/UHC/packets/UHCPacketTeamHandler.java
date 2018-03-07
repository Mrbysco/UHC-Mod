package com.Mrbysco.UHC.packets;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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
		System.out.println("hey");

		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		Scoreboard scoreboard = serverPlayer.getServerWorld().getScoreboard();
		if(message.team.equals("solo"))
			scoreboard.removePlayerFromTeams(message.playerName);
		else
			scoreboard.addPlayerToTeam(message.playerName, message.team);
		
		
		for(EntityPlayerMP players : serverPlayer.getServer().getPlayerList().getPlayers())
		{
			if(message.team.equals("solo"))
				players.sendMessage(new TextComponentTranslation("book.uhc.team.solo", new Object[] {message.playerName, TextFormatting.fromColorIndex(message.colorIndex) + message.teamName}));
			else
				players.sendMessage(new TextComponentTranslation("book.uhc.team.selected", new Object[] {message.playerName, TextFormatting.fromColorIndex(message.colorIndex) + message.teamName}));
		}
	}
}