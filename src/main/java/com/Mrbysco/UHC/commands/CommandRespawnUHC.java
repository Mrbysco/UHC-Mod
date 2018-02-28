package com.Mrbysco.UHC.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class CommandRespawnUHC extends CommandBase
{
	@Override
	public String getName() {
		return "respawn";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.uhc.respawn.usage";
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

        return server.isSinglePlayer() || super.checkPermission(server, sender) || (player != null && entityData.hasKey("canModify"));
    }
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
		if (args.length < 1)
        {
            throw new WrongUsageException("commands.uhc.respawn.usage", new Object[0]);
        }
        else
        {
        	if (args.length == 1)
            {
                throw new WrongUsageException("commands.uhc.respawn.usage", new Object[0]);
            }
            
            this.respawnTeamMember(sender, args, 0, server);
        }
    }

	protected Scoreboard getScoreboard(MinecraftServer server)
    {
        return server.getWorld(0).getScoreboard();
    }
	
	protected void respawnTeamMember(ICommandSender sender, String[] args, int startIndex, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = this.getScoreboard(server);
        String s = args[startIndex++];
        Set<String> set = Sets.<String>newHashSet();
        Set<String> set1 = Sets.<String>newHashSet();
        List<String> players = Arrays.asList(server.getOnlinePlayerNames());
        
        if (sender instanceof EntityPlayer && startIndex == args.length)
        {
            String s4 = getCommandSenderAsPlayer(sender).getName();
            
            if (scoreboard.addPlayerToTeam(s4, s) && players.contains(s4))
            {
                set.add(s4);
            }
            else
            {
                set1.add(s4);
            }
        }
        else
        {
            while (startIndex < args.length)
            {
                String s1 = args[startIndex++];

                if (EntitySelector.isSelector(s1))
                {
                    for (Entity entity : getEntityList(server, sender, s1))
                    {
                        String s3 = getEntityName(server, sender, entity.getCachedUniqueIdString());

                    	if (scoreboard.addPlayerToTeam(s3, s) && players.contains(s3))
                        {
                            set.add(s3);
                        }
                        else
                        {
                            set1.add(s3);
                        }
                    }
                }
                else
                {
                    String s2 = getEntityName(server, sender, s1);

                    if (scoreboard.addPlayerToTeam(s2, s) && players.contains(s2))
                    {
                        set.add(s2);
                    }
                    else
                    {
                        set1.add(s2);
                    }
                }
            }
        }

        if (!set.isEmpty())
        {
        		sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
                notifyCommandListener(sender, this, "commands.uhc.respawn.success", new Object[] {set.size(), s, joinNiceString(set.toArray(new String[set.size()]))});

                String playerName = removeBrackets(set.toString());
                System.out.println(set);
                EntityPlayer player = server.getWorld(0).getPlayerEntityByName(getEntityName(server, sender, playerName));
    			final NBTTagCompound entityData = player.getEntityData();

                World world = server.getWorld(0);
                ScorePlayerTeam team = scoreboard.getTeam(s);
                if(team == null)
                {
                	return;
                }
                Collection<String> collection = team.getMembershipCollection();
                sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
                if (collection.isEmpty())
                {
                    throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] {team.getName()});
                }
                
                String memberName = Iterables.get(collection, 0);
                EntityPlayer teamMember = server.getWorld(0).getPlayerEntityByName(getEntityName(server, sender, memberName));
                if(teamMember != null)
                {
                    BlockPos pos = teamMember.getPosition();
                	int teamDimension = teamMember.dimension;
                	if(player.dimension != teamDimension)
        			{
        				player.changeDimension(teamDimension);
        			}
                    player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
                    player.setGameType(GameType.SURVIVAL);
                }
                else
                {
                	int posX,posY,posZ;
                	BlockPos pos1 = player.getBedLocation();
            		int dimInt = entityData.getInteger("deathDim");

                	if(pos1 != null)
                	{
                		posX = pos1.getX();
                		posY = pos1.getY();
                		posZ = pos1.getZ();
                	}
                	else
                	{
                		posX = entityData.getInteger("deathX");
                		posY = entityData.getInteger("deathY");
                		posZ = entityData.getInteger("deathZ");
                		posZ = entityData.getInteger("deathZ");
                	}
        			entityData.setBoolean("revival", true);
        			if(player.dimension != dimInt)
        			{
        				player.changeDimension(dimInt);
        			}
                    player.setPositionAndUpdate(posX, posY, posZ);
                	player.setGameType(GameType.SURVIVAL);
                }

        }

        if (!set1.isEmpty())
        {
        	String invalidUser = removeBrackets(set1.toString());
            throw new CommandException("commands.uhc.respawn.failure", new Object[] {invalidUser, invalidUser});
        }
    }
	
	public String removeBrackets(String string)
	{
		return string.replace("[", "").replace("]", "");
	}
	
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, this.getScoreboard(server).getTeamNames());
        }
        else
        {
        	if (args.length >= 2)
            {
                return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            }
        	
            return Collections.<String>emptyList();
        }
	}
}
