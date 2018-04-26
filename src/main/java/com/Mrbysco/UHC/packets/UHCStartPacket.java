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
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
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
			WorldServer world = serverPlayer.getServerWorld();
			WorldBorder border = world.getWorldBorder();
			MinecraftServer server = world.getMinecraftServer();
			ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();
			Scoreboard scoreboard = world.getScoreboard();
			WorldInfo info = world.getWorldInfo();
			NBTTagCompound playerData = serverPlayer.getEntityData();
			
			if(playerData.getBoolean("canEditUHC") == true)
			{
				ArrayList<EntityPlayerMP>soloPlayers = (ArrayList<EntityPlayerMP>) playerList.clone();
				ArrayList<EntityPlayerMP>teamPlayers = (ArrayList<EntityPlayerMP>) playerList.clone();
				
				for (EntityPlayer player : playerList)
				{
					if(player.getTeam() != scoreboard.getTeam("solo"))
						soloPlayers.remove(player);
				}
				
				teamPlayers.removeAll(soloPlayers);
				
				double centerX = saveData.getBorderCenterX();
				double centerZ = saveData.getBorderCenterZ();
				double spreadDistance = saveData.getSpreadDistance();
				double spreadMaxRange = saveData.getSpreadMaxRange();
				int BorderSize = saveData.getBorderSize();
				
				world.setWorldTime(0);
				info.setRaining(false);
				
				if(saveData.isRandomSpawns())
				{
					try {
						SpreadUtil.spread(teamPlayers, soloPlayers, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, world, saveData.isSpreadRespectTeam());
					} catch (CommandException e) {
						e.printStackTrace();
					}
				}
				else
				{
					for(EntityPlayer player : playerList)
					{
						if(player.getTeam() != scoreboard.getTeam("solo"))
						{
							BlockPos pos = TeamUtil.getPosForTeam(player.getTeam().getColor());
							System.out.println(pos.toString());
							
				            ((EntityPlayerMP)player).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
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
				
				for(EntityPlayer player : playerList)
				{
					ScoreObjective score = scoreboard.getObjective("health");
					if(score != null)
						scoreboard.removeObjectiveFromEntity(player.getName(), score);
					
					if(player.isCreative())
						player.setGameType(GameType.SURVIVAL);
				}
				
				if(saveData.isSpawnRoom())
				{
					double centerX1 = centerX -7;
					double centerX2 = centerX +7;
					double centerZ1 = centerZ -7;
					double centerZ2 = centerZ +7;
					
					for(double i = centerX1; i <= centerX2; i++)
					{
						for(double j = centerZ1; j <= centerZ2; j++)
						{
							for(double k = 250; k <= 253; k++)
							{
								world.setBlockState(new BlockPos(i, k, j), Blocks.AIR.getDefaultState());
							}
						}
					}
					saveData.setSpawnRoom(false);
					saveData.setSpawnRoomDimension(0);
					saveData.markDirty();
				}
				
				if (border.getCenterX() != centerX && border.getCenterZ() != centerZ)
					border.setCenter(centerX, centerZ);
				
				border.setTransition(BorderSize);
				
				for(EntityPlayerMP player : playerList)
				{							
					player.inventory.clear();
					
					if(player.getActivePotionEffect(MobEffects.MINING_FATIGUE) == null)
						player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 32767 * 20, 10, true, false));
					
					if(player.isGlowing())
						player.setGlowing(false);
				}
				
				saveData.setUhcStarting(true);
				saveData.markDirty();
			}
			else
			{
				serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have permissions to start the UHC."));
			}
		}
	}
}
