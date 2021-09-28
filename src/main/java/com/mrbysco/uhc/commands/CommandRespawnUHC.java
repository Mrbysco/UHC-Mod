package com.mrbysco.uhc.commands;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.mrbysco.uhc.utils.SpreadPosition;
import com.mrbysco.uhc.utils.SpreadUtil;
import com.mrbysco.uhc.utils.TeamUtil;
import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandRespawnUHC extends CommandUhcBase
{
	@Override
	public String getName() {
		return "respawn";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.uhc.respawn.usage";
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
        	else
        	{
                this.respawnTeamMember(sender, args, 0, server);
        	}
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
        
        if (sender instanceof PlayerEntity && startIndex == args.length)
        {
            String s4 = getCommandSenderAsPlayer(sender).getName();
            
            if (scoreboard.addPlayerToTeam(s4, s) && players.contains(s4))
            {
            	respawnPlayers(s4, s, sender, server);
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
                        	respawnPlayers(s3, s, sender, server);
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
                    	respawnPlayers(s2, s, sender, server);
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
        }

        if (!set1.isEmpty())
        {
            throw new CommandException("commands.uhc.respawn.failure", new Object[] {set1.size(), s, joinNiceString(set1.toArray(new String[set1.size()]))});
        }
    }
	
	public void respawnPlayers(String playerName, String teamName, ICommandSender sender, MinecraftServer server)
	{
        Scoreboard scoreboard = this.getScoreboard(server);
        UHCSaveData uhcData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));

		PlayerEntity player = server.getWorld(0).getPlayerEntityByName(playerName);
		if(player != null)
		{
        	CompoundNBT entityData = player.getEntityData();
            
            ScorePlayerTeam selectedTeam = scoreboard.getTeam(teamName);
            if(selectedTeam != null)
            {
            	if(!selectedTeam.getMembershipCollection().isEmpty())
            	{
                    Collection<String> teamMembers = selectedTeam.getMembershipCollection();
                    if(!teamMembers.isEmpty())
                    {
                    	String memberName = Iterables.get(teamMembers, 0);
                        PlayerEntity teamMember = server.getWorld(0).getPlayerEntityByName(memberName);

                        if(teamMember != null)
                        {
                        	BlockPos pos = teamMember.getPosition();
                        	int teamDimension = teamMember.dimension;
                        	if(player.dimension != teamDimension)
                				player.changeDimension(teamDimension);
                        	
                            player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
                            player.setGameType(GameType.SURVIVAL);
                            
                            if(scoreboard.getObjective("health") != null)
            			        scoreboard.removeObjectiveFromEntity(player.getName(), scoreboard.getObjective("health"));
                            
                            setCustomHealth(player);
                        }
                    }
                    else
                    {
                    	BlockPos pos1 = player.getBedLocation();
                    	
                		String dimInt = entityData.getString("deathDim");
                		Long deathPosLong = entityData.getLong("deathPos");
                		
                		if(dimInt.isEmpty() && deathPosLong.equals(0L))
                		{
                        	BlockPos deathPos;

                			if(deathPosLong != 0L)
            				{
                        		deathPos = BlockPos.fromLong(deathPosLong);
            				}
                    		else
                    		{
                        		deathPos = pos1;
                    		}

                        	if(!dimInt.isEmpty())
                        	{
                        		if(player.dimension != Integer.valueOf(dimInt))
                    			{
                    				player.changeDimension(Integer.valueOf(dimInt));
                    			}
                        	}
                        	
                            player.setPositionAndUpdate(deathPos.getX(), deathPos.getY(), deathPos.getZ());
                		}
                		else
                		{
                			if(player.dimension != uhcData.getUHCDimension())
                			{
                				player.changeDimension(uhcData.getUHCDimension());
                			}
                			
            				ArrayList<ServerPlayerEntity> playerList = new ArrayList<>(Arrays.asList((ServerPlayerEntity) player));
            				WorldBorder border = server.getWorld(0).getWorldBorder();

            				double centerX = uhcData.getBorderCenterX();
        					double centerZ = uhcData.getBorderCenterZ();
        					if (border.getCenterX() != centerX && border.getCenterZ() != centerZ)
        						border.setCenter(centerX, centerZ);
        					
        					int BorderSize = uhcData.getBorderSize();
        					
        					double spreadDistance = uhcData.getSpreadDistance();
        					double spreadMaxRange = uhcData.getSpreadMaxRange();
        					
            				if(spreadMaxRange >= (BorderSize / 2))
        						spreadMaxRange = (BorderSize / 2);
            				
                			if(uhcData.isRandomSpawns())
        					{
                				if(selectedTeam == scoreboard.getTeam("solo"))
                				{
                					try {
            							SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, server.getWorld(0), uhcData.isSpreadRespectTeam());
            						} catch (CommandException e) {
            							e.printStackTrace();
            						}
                				}
        						try {
        							SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, server.getWorld(0), false);
        						} catch (CommandException e) {
        							e.printStackTrace();
        						}
        					}
        					else
        					{
        						for(PlayerEntity players : playerList)
        						{
        							if(selectedTeam != scoreboard.getTeam("solo"))
        							{
        								BlockPos pos = TeamUtil.getPosForTeam(player.getTeam().getColor());
        								
        					            ((ServerPlayerEntity)player).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
        							}
        							else
        							{
        								try {
        									SpreadUtil.spread(playerList, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, server.getWorld(0), false);
        								} catch (CommandException e) {
        									e.printStackTrace();
        								}
        							}
        						}
        					}
                		}
            			
                    	player.setGameType(GameType.SURVIVAL);
                    	if(scoreboard.getObjective("health") != null)
        			        scoreboard.removeObjectiveFromEntity(player.getName(), scoreboard.getObjective("health"));
                    	
                    	setCustomHealth(player);
                    }
            	}
            }
		}
	}
	
	public void setCustomHealth(PlayerEntity player)
	{
        UHCSaveData uhcData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
    	CompoundNBT entityData = player.getEntityData();

		double playerHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
		boolean flag = uhcData.isApplyCustomHealth();
		double maxHealth = (double) uhcData.getMaxHealth();
		
		if(playerHealth != maxHealth && flag)
		{
			player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
			
			int instantHealth = uhcData.getMaxHealth() / 4;
			player.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 1, instantHealth, true, false));
			
            entityData.setBoolean("modifiedMaxHealth", true);
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
