package com.Mrbysco.UHC.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandUhcBase extends CommandBase{

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
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
		if(player == null)
		{
			return super.checkPermission(server, sender);
		}
		else
		{
			final NBTTagCompound entityData = player.getEntityData();

	        return super.checkPermission(server, sender) || (player != null && entityData.getBoolean("canEditUHC") == true);
		}
    }
}
