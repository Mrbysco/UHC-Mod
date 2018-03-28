package com.Mrbysco.UHC.packets;

import java.util.ArrayList;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.utils.SpreadPosition;
import com.Mrbysco.UHC.utils.SpreadUtil;
import com.Mrbysco.UHC.utils.TeamUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCStartPacket implements IMessage{	
	public UHCStartPacket() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}
	
	public static class PacketHandler implements IMessageHandler<UHCStartPacket, IMessage>
	{
		@Override
		public IMessage onMessage(UHCStartPacket message, MessageContext ctx) {
			
	        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		    return null;
		}
		
		private void handle(UHCStartPacket message, MessageContext ctx) {
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
				ArrayList<EntityPlayerMP>soloPlayers = (ArrayList<EntityPlayerMP>) playerList.clone();
				for (EntityPlayer player : playerList)
				{
					if(player.getTeam() == null)
						soloPlayers.remove(player);
					
					if(player.getTeam() == scoreboard.getTeam("spectator"))
						playerList.remove(player);
				}
				
				double centerX = saveData.getBorderCenterX();
				double centerZ = saveData.getBorderCenterZ();
				double spreadDistance = saveData.getSpreadDistance();
				double spreadMaxRange = saveData.getSpreadMaxRange();
				int BorderSize = saveData.getBorderSize();
				
				//if (border.getCenterX() != centerX && border.getCenterZ() != centerZ)
				//	border.setCenter(centerX, centerZ);
				
				//border.setSize(BorderSize);
				world.setWorldTime(0);
				info.setRaining(false);
				
				if(saveData.isRandomSpawns())
				{
					try {
						SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, world, saveData.isSpreadRespectTeam());
					} catch (CommandException e) {
						e.printStackTrace();
					}
				}
				else
				{
					for(EntityPlayer player : playerList)
					{
						if(player.getTeam() != null)
						{
							BlockPos pos = TeamUtil.getPosForTeam(player.getTeam().getColor());
							System.out.println(pos.toString());
							
							server.getCommandManager().executeCommand(server , "/tp " + player.getName() + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ() );
							//player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
						}
						else
						{
							try {
								SpreadUtil.spread(soloPlayers, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, world, false);
							} catch (CommandException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				//saveData.setUhcStarting(true);
				//saveData.markDirty();
			}
			else
			{
				serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have permissions to start the UHC."));
			}
		}
	}
}
