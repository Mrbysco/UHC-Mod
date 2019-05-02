package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
			if(DimensionManager.getWorld(0) != null)
			{
				UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
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
					
					List<ScorePlayerTeam> foundTeams = new ArrayList<ScorePlayerTeam>();
					for (ScorePlayerTeam team : scoreboard.getTeams())
					{
						if(team != scoreboard.getTeam("spectator"))
						{
							foundTeams.add(team);
						}
					}
					
					for(ScorePlayerTeam team : foundTeams)
					{
						if(!team.getMembershipCollection().isEmpty())
						{
							team.getMembershipCollection().clear();
							foundTeams.remove(team);
						}
					}
				
					int randomTeams = saveData.getRandomTeamSize();
					if(randomTeams > 14)
					{
						saveData.setRandomTeamSize(14);
						saveData.markDirty();
						randomTeams = 14;
					}
					
					Collections.shuffle(playerList);
					ArrayList<EntityPlayerMP>tempList = (ArrayList<EntityPlayerMP>) teamPlayers.clone();

					int playerAmount = playerList.size();
					int amountPerTeam = (int)Math.ceil((double)playerAmount / (double)randomTeams);
					
					ArrayList<String> possibleTeams = getTeams();
					
					for(int i = 0; i < randomTeams; i++)
					{
						String teamName = possibleTeams.get(possibleTeams.size() > 1 ? world.rand.nextInt(possibleTeams.size()) : 0);
						possibleTeams.remove(teamName);
						ScorePlayerTeam team = scoreboard.getTeam(teamName);

						for(int j = 0; j < amountPerTeam; j++)
						{
							if(!tempList.isEmpty())
							{
								EntityPlayer player = tempList.get(0);

								scoreboard.addPlayerToTeam(player.getName(), teamName);
								for(EntityPlayerMP players : playerList)
								{
									if(team != null)
										players.sendMessage(new TextComponentTranslation("book.uhc.team.randomized", new Object[] {player.getName(), team.getColor() + team.getName().replaceAll("_", " ")}));
								}
								tempList.remove(player);
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
	
	private static ArrayList<String> getTeams()
	{
		ArrayList<String> teams = new ArrayList<>();
		
		teams.add(TextFormatting.DARK_RED.getFriendlyName());
		teams.add(TextFormatting.GOLD.getFriendlyName());
		teams.add(TextFormatting.DARK_GREEN.getFriendlyName());
		teams.add(TextFormatting.DARK_AQUA.getFriendlyName());
		teams.add(TextFormatting.DARK_BLUE.getFriendlyName());
		teams.add(TextFormatting.DARK_PURPLE.getFriendlyName());
		teams.add(TextFormatting.DARK_GRAY.getFriendlyName());
		teams.add(TextFormatting.RED.getFriendlyName());
		teams.add(TextFormatting.YELLOW.getFriendlyName());
		teams.add(TextFormatting.GREEN.getFriendlyName());
		teams.add(TextFormatting.AQUA.getFriendlyName());
		teams.add(TextFormatting.BLUE.getFriendlyName());
		teams.add(TextFormatting.LIGHT_PURPLE.getFriendlyName());
		teams.add(TextFormatting.GRAY.getFriendlyName());
		
		return teams;
	}
}