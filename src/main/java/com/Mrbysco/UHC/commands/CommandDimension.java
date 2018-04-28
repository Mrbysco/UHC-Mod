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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CommandDimension extends CommandBase
{
	@Override
	public String getName() {
		return "dimension";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.uhc.dimension.usage";
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
		UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
		
		if ("reset".equalsIgnoreCase(args[0]))
        {
			if(args.length >= 2)
	        {
				throw new CommandException("commands.uhc.dimension.failure.input");
	        }
			else
			{
				//saveData.setUHCDimension(0);
				//saveData.markDirty();
		        sender.sendMessage(new TextComponentTranslation("commands.uhc.dimension.reset"));
			}
        }
		else if ("set".equalsIgnoreCase(args[0]))
		{
			if(args.length >= 3)
			{
				throw new CommandException("commands.uhc.dimension.set.failure2", new Object[] {args[2]});
			}
			else
			{
				if(args[1].isEmpty())
				{
					throw new CommandException("commands.uhc.dimension.set.failure", new Object[0]);
				}
				else
				{
					if(args[1].matches("^-?[0-9]\\d*(\\.\\d+)?$"))
					{
						int dimension = Integer.parseInt(args[1]);
					    //saveData.setUHCDimension(dimension);
						//saveData.markDirty();
				        sender.sendMessage(new TextComponentTranslation("commands.uhc.dimension.set.success", new Object[] {args[1]}));
					}
					else
					{
						throw new CommandException("commands.uhc.dimension.set.failure.number", new Object[] {args[1]});
					}
				}	
			}
		}
		else
		{
			throw new CommandException("commands.uhc.dimension.set.failure3", new Object[] {args[0]});
		}
    }
	
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"set", "reset"}) : Collections.emptyList();
    }
}
