package com.Mrbysco.UHC.commands;

import net.minecraft.command.ICommandSender;
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
        return server.isSinglePlayer() || super.checkPermission(server, sender);
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "commands.uhc.usage";
    }
}