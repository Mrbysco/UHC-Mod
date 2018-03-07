package com.Mrbysco.UHC.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandTreeUHC extends CommandTreeBase
{
    public CommandTreeUHC()
    {
        super.addSubcommand(new CommandResetUHC());
        super.addSubcommand(new CommandRespawnUHC());
    }


    @Override
    public String getName()
    {
        return "uhc";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
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

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "commands.uhc.usage";
    }
}