package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage1PacketHandler implements IMessageHandler<UHCPage1Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage1Packet message, MessageContext ctx) {
		
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
		return null;
	}
	
	private void handle(UHCPage1Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		UHCSaveData saveData = UHCSaveData.getForWorld(serverPlayer.getServerWorld());
		NBTTagCompound playerData = serverPlayer.getEntityData();
		boolean flag = playerData.getBoolean("canEditUHC");
		if(flag)
		{
			saveData.setRandomTeamSize(message.randomTeams);
			saveData.setMaxTeamSize(message.maxTeams);
			saveData.setTeamCollision(message.teamCollision);
			saveData.setFriendlyFire(message.teamDamage);
			saveData.setDifficulty(message.difficulty);
			saveData.markDirty();
		}
	}
}