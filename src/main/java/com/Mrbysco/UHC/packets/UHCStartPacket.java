package com.Mrbysco.UHC.packets;

import java.util.List;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.utils.SpreadPosition;
import com.Mrbysco.UHC.utils.SpreadUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
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
			List<EntityPlayerMP> playerList = server.getPlayerList().getPlayers();
			Scoreboard scoreboard = world.getScoreboard();
			WorldInfo info = world.getWorldInfo();
			NBTTagCompound playerData = serverPlayer.getEntityData();
			
			if(playerData.getBoolean("canEditUHC") == true)
			{
				
				for (EntityPlayer player : playerList)
				{
					if(player.getTeam() == scoreboard.getTeam("spectator"))
					{
						playerList.remove(player);
					}
				}
				
				double centerX = saveData.getBorderCenterX();
				double centerZ = saveData.getBorderCenterZ();
				int BorderSize = saveData.getBorderSize();
				
				//if (border.getCenterX() != centerX && border.getCenterZ() != centerZ)
				//	border.setCenter(centerX, centerZ);
				
				//border.setSize(BorderSize);
				world.setWorldTime(0);
				info.setRaining(false);
				
				try {
					SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), 20, 100, world, true);
				} catch (CommandException e) {
					e.printStackTrace();
				}
				
				for(String players : server.getOnlinePlayerNames())
				{
					EntityPlayer onlinePlayer = world.getPlayerEntityByName(players);
					
				}
				saveData.setUhcStarting(true);
				saveData.markDirty();
			}
		}
	}
}
