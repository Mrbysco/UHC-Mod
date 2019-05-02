package com.mrbysco.uhc.commands;

import com.google.common.collect.Sets;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandForceTeam extends CommandUhcBase
{
	@Override
	public String getName() {
		return "forceteam";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.uhc.forceteam.usage";
	}
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
		if (args.length < 1)
        {
            throw new WrongUsageException("commands.uhc.forceteam.usage", new Object[0]);
        }
        else
        {
        	if (args.length == 1)
            {
                throw new WrongUsageException("commands.uhc.forceteam.usage", new Object[0]);
            }
        	else
        	{
                this.forceTeam(sender, args, 0, server);
        	}
        }
    }

	protected Scoreboard getScoreboard(MinecraftServer server)
    {
        return server.getWorld(0).getScoreboard();
    }
	
	protected void forceTeam(ICommandSender sender, String[] args, int startIndex, MinecraftServer server) throws CommandException
    {
        Scoreboard scoreboard = this.getScoreboard(server);
        String s = args[startIndex++];
        Set<String> set = Sets.<String>newHashSet();
        Set<String> set1 = Sets.<String>newHashSet();

        if (sender instanceof EntityPlayer && startIndex == args.length)
        {
            String s4 = getCommandSenderAsPlayer(sender).getName();

            if (scoreboard.addPlayerToTeam(s4, s))
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

                        if (scoreboard.addPlayerToTeam(s3, s))
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

                    if (scoreboard.addPlayerToTeam(s2, s))
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
            notifyCommandListener(sender, this, "commands.uhc.forceteam.success", new Object[] {set.size(), s, joinNiceString(set.toArray(new String[set.size()]))});
        }

        if (!set1.isEmpty())
        {
            throw new CommandException("commands.uhc.forceteam.failure", new Object[] {set1.size(), s, joinNiceString(set1.toArray(new String[set1.size()]))});
        }
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
