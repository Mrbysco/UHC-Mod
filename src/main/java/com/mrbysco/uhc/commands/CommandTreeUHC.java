package com.mrbysco.uhc.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandTreeUHC extends CommandTreeBase
{
    public CommandTreeUHC()
    {
        super.addSubcommand(new CommandResetUHC());
        super.addSubcommand(new CommandRespawnUHC());
        super.addSubcommand(new CommandSpawnroom());
        super.addSubcommand(new CommandForceTeam());
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
		PlayerEntity player = null;
		if(senderEntity instanceof PlayerEntity)
		{
			player = (PlayerEntity) senderEntity;
		}
		if(player == null)
		{
			return super.checkPermission(server, sender);
		}
		else
		{
			final CompoundNBT entityData = player.getEntityData();

	        return super.checkPermission(server, sender) || (player != null && entityData.getBoolean("canEditUHC") == true);
		}
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "commands.uhc.usage";
    }
}