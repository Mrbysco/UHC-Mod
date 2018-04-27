package com.Mrbysco.UHC.packets;

import java.util.ArrayList;
import java.util.Collections;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.utils.TeamUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPacketTeamRandomizer implements IMessage
{
	public UHCPacketTeamRandomizer() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
	}
	
	public static class PacketHandler implements IMessageHandler<UHCPacketTeamRandomizer, IMessage>
	{
		@Override
		public IMessage onMessage(UHCPacketTeamRandomizer message, MessageContext ctx) {
			
	        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		    return null;
		}
		
		private void handle(UHCPacketTeamRandomizer message, MessageContext ctx) {
			EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
			UHCSaveData saveData = UHCSaveData.getForWorld(serverPlayer.getServerWorld());
			World world = serverPlayer.getServerWorld();
			WorldBorder border = world.getWorldBorder();
			MinecraftServer server = world.getMinecraftServer();
			ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();
			Scoreboard scoreboard = world.getScoreboard();
			WorldInfo info = world.getWorldInfo();
			NBTTagCompound playerData = serverPlayer.getEntityData();
			
			if(playerData.getBoolean("canEditUHC") == true)
			{
				ArrayList<EntityPlayerMP>teamPlayers = (ArrayList<EntityPlayerMP>) playerList.clone();

				for (EntityPlayer player : playerList)
				{
					if(player.getTeam() == scoreboard.getTeam("spectator"))
						teamPlayers.remove(player);
					else
						scoreboard.removePlayerFromTeams(player.getName());
				}
			
				int randomTeams = saveData.getRandomTeamSize();
				Collections.shuffle(playerList);
				ArrayList<EntityPlayerMP>tempList = (ArrayList<EntityPlayerMP>) teamPlayers.clone();

				int playerAmount = playerList.size();
				int amountPerTeam = (int)Math.ceil((double)playerAmount / (double)randomTeams);
				for(int i = 0; i < randomTeams; i++)
				{
					for(int j = 0; j < amountPerTeam; j++)
					{
						if(tempList.size() != 0)
						{
							EntityPlayer player = tempList.get(0);
							scoreboard.addPlayerToTeam(player.getName(), TeamUtil.getTeamNameFromInt(i+1));
							
							for(EntityPlayerMP players : playerList)
							{
								players.sendMessage(new TextComponentTranslation("book.uhc.team.randomized", new Object[] {player.getName(), TeamUtil.getTeamNameFromInt(i+1).replaceAll("_", " ")}));
							}
							tempList.remove(0);
						}
					}
				}
			}
			else
			{
				serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have permissions to randomize the teams."));
			}
		}
	}
}