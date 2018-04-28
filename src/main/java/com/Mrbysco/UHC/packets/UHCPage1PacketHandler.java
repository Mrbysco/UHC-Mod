package com.Mrbysco.UHC.packets;

import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team.CollisionRule;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UHCPage1PacketHandler implements IMessageHandler<UHCPage1Packet, IMessage>
{
	@Override
	public IMessage onMessage(UHCPage1Packet message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		World world = serverPlayer.getServerWorld();
		UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
		NBTTagCompound playerData = serverPlayer.getEntityData();
		WorldInfo wInfo = world.getWorldInfo();
		Scoreboard scoreboard = world.getScoreboard();
		
		if(playerData.getBoolean("canEditUHC"))
		{
			for (ScorePlayerTeam team : scoreboard.getTeams())
			{
				if(message.teamDamage)
				{
					if (team.getAllowFriendlyFire() != true)
					{
						team.setAllowFriendlyFire(true);
					}
				}
				else
				{
					if (team.getAllowFriendlyFire() != false)
					{
						team.setAllowFriendlyFire(false);
					}
				}
				
				if(message.teamCollision)
				{
					if (team.getCollisionRule() != CollisionRule.ALWAYS)
					{
						team.setCollisionRule(CollisionRule.ALWAYS);
					}
				}
				else
				{
					if (team.getCollisionRule() != CollisionRule.HIDE_FOR_OTHER_TEAMS)
					{
						team.setCollisionRule(CollisionRule.HIDE_FOR_OTHER_TEAMS);
					}
				}
			}
			
			if(wInfo.getDifficulty() != EnumDifficulty.getDifficultyEnum(message.difficulty))
				wInfo.setDifficulty(EnumDifficulty.getDifficultyEnum(message.difficulty));
			
			saveData.setRandomTeamSize(message.randomTeams);
			saveData.setMaxTeamSize(message.maxTeams);
			saveData.setTeamCollision(message.teamCollision);
			saveData.setFriendlyFire(message.teamDamage);
			saveData.setDifficulty(message.difficulty);
			saveData.markDirty();
			
			ModPackethandler.INSTANCE.sendToAll(new UHCPacketMessage(saveData));
		}
		else
		{
			serverPlayer.sendMessage(new TextComponentString(TextFormatting.RED + "You don't have permissions to edit the UHC book."));
		}
		
		return null;
	}
}