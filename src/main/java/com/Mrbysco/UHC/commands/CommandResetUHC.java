package com.Mrbysco.UHC.commands;

import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.init.UHCTimerData;
import com.Mrbysco.UHC.packets.ModPackethandler;
import com.Mrbysco.UHC.packets.UHCPacketMessage;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

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
        if(!world.isRemote)
        {
        	UHCSaveData saveData = UHCSaveData.getForWorld(world);
    		UHCTimerData timerData = UHCTimerData.getForWorld(world);
    		timerData.resetAll();
    		timerData.markDirty();
    		saveData.resetAll();
    		saveData.markDirty();
    		ModPackethandler.INSTANCE.sendToAll(new UHCPacketMessage(saveData));
            sender.sendMessage(new TextComponentTranslation("commands.uhc.reset.success"));
        }
    }
}
