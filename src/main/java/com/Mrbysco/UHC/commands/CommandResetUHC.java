package com.Mrbysco.UHC.commands;

import java.util.ArrayList;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.packets.ModPackethandler;
import com.Mrbysco.UHC.packets.UHCPacketMessage;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CommandResetUHC extends CommandBase
{
	@Override
	public String getName() {
		return "reset";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.uhc.reset.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
		Entity senderEntity = sender.getCommandSenderEntity();
		EntityPlayer player = null;
		if(senderEntity instanceof EntityPlayer)
		{
			player = (EntityPlayer) senderEntity;
		}
		final NBTTagCompound entityData = player.getEntityData();

        return server.isSinglePlayer() || super.checkPermission(server, sender) || (player != null && entityData.getBoolean("canEditUHC") == true);
    }
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        World world = (World)(sender instanceof EntityPlayer ? ((EntityPlayer)sender).world : server.getWorld(0));
		ArrayList<EntityPlayerMP> playerList = (ArrayList<EntityPlayerMP>)server.getPlayerList().getPlayers();

        if(!world.isRemote)
        {
        	UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
    		UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
    		
			Scoreboard scoreboard = world.getScoreboard();

			for(EntityPlayerMP player : playerList)
			{
				player.inventory.clear();
				
				if(player.getTeam() != null)
					scoreboard.removePlayerFromTeams(player.getName());		
			}
			
			double centerX = saveData.getBorderCenterX();
			double centerZ = saveData.getBorderCenterZ();
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
			
    		timerData.resetAll();
    		timerData.markDirty();
    		saveData.resetAll();
    		saveData.markDirty();
    		
    		ModPackethandler.INSTANCE.sendToAll(new UHCPacketMessage(saveData));
            sender.sendMessage(new TextComponentTranslation("commands.uhc.reset.success"));
        }
    }
}
