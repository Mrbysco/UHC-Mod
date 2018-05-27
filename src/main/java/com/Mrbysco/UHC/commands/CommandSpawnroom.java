package com.Mrbysco.UHC.commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;

public class CommandSpawnroom extends CommandBase
{
	@Override
	public String getName() {
		return "spawnroom";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.uhc.spawnroom.usage";
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
        WorldInfo info = world.getWorldInfo();
        
		UHCSaveData saveData = UHCSaveData.getForWorld(server.getWorld(0));
		double centerX = saveData.getBorderCenterX();
		double centerZ = saveData.getBorderCenterZ();
		
		double centerX1 = centerX -7;
		double centerX2 = centerX +7;
		double centerZ1 = centerZ -7;
		double centerZ2 = centerZ +7;
		
		if(args.length >= 2)
		{
            throw new CommandException("commands.uhc.spawnroom.failure.input");
		}
		else
		{
			if ("remove".equalsIgnoreCase(args[0]))
	        {
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
		        sender.sendMessage(new TextComponentTranslation("commands.uhc.spawnroom.success1"));
	        }
			else if ("place".equalsIgnoreCase(args[0]))
			{
				for(double i = centerX1; i <= centerX2; i++)
				{
					for(double j = centerZ1; j <= centerZ2; j++)
					{
						world.setBlockState(new BlockPos(i, 250, j), Blocks.BARRIER.getDefaultState());
						if(j == centerZ1 || j == centerZ2)
						{
							for(double k = 250; k <= 253; k++)
							{
								world.setBlockState(new BlockPos(i, k, j), Blocks.BARRIER.getDefaultState());
							}
						}
					}
					
					if(i == centerX1 || i == centerX2)
					{
						for(double j = centerZ1; j <= centerZ2; j++)
						{
							for(double k = 250; k <= 253; k++)
							{
								world.setBlockState(new BlockPos(i, k, j), Blocks.BARRIER.getDefaultState());
							}
						}
					}
				}
				world.setSpawnPoint(new BlockPos(centerX, 252, centerZ));
				saveData.setSpawnRoom(true);
				saveData.setSpawnRoomDimension(world.provider.getDimension());
				saveData.markDirty();
		        sender.sendMessage(new TextComponentTranslation("commands.uhc.spawnroom.success"));
			}
			else
			{
	            throw new CommandException("commands.uhc.spawnroom.failure", new Object[] {args[0]});
			}
		}
    }
	
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"place", "remove"}) : Collections.emptyList();
    }
}
