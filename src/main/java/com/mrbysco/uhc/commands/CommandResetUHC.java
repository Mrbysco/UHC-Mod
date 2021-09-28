package com.mrbysco.uhc.commands;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.data.UHCTimerData;
import com.mrbysco.uhc.packets.UHCPacketHandler;
import com.mrbysco.uhc.packets.UHCPacketMessage;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

public class CommandResetUHC extends CommandUhcBase
{
	@Override
	public String getName() {
		return "reset";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.uhc.reset.usage";
	}
	
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        World world = (World)(sender instanceof PlayerEntity ? ((PlayerEntity)sender).world : server.getWorld(0));
		ArrayList<ServerPlayerEntity> playerList = (ArrayList<ServerPlayerEntity>)server.getPlayerList().getPlayers();

        if(!world.isRemote)
        {
        	UHCSaveData saveData = UHCSaveData.getForWorld(DimensionManager.getWorld(0));
    		UHCTimerData timerData = UHCTimerData.getForWorld(DimensionManager.getWorld(0));
			WorldBorder border = server.worlds[0].getWorldBorder();

			Scoreboard scoreboard = server.worlds[0].getScoreboard();
			
			if(scoreboard != null)
			{
				for(ScorePlayerTeam team : scoreboard.getTeams())
				{
					if(team != null && team.getMembershipCollection().size() > 0 && team != scoreboard.getTeam("spectator"))
					{
						if(team.getMembershipCollection() != null && !team.getMembershipCollection().isEmpty())
						{
							List<String> foundPlayers = new ArrayList<String>();
							for(String players : team.getMembershipCollection())
							{
								foundPlayers.add(players);
							}
							
							for(String playerFound : foundPlayers)
							{
								scoreboard.removePlayerFromTeam(playerFound, team);
							}
						}
					}
				}
			}
			
			for(ServerPlayerEntity player : playerList)
			{
				player.inventory.clear();
				player.heal(100);
				
				if(player.getTeam() != null)
					scoreboard.addPlayerToTeam(player.getName(), "spectator");
			}
			
			double centerX = saveData.getBorderCenterX();
			double centerZ = saveData.getBorderCenterZ();
			double centerX1 = centerX -7;
			double centerX2 = centerX +7;
			double centerZ1 = centerZ -7;
			double centerZ2 = centerZ +7;
			for(double i = centerX1; i <= centerX2; i++)
			{
				for(double j = centerZ1; j <= centerZ2; j++)
				{
					for(double k = 250; k <= 253; k++)
					{
						world.setBlockState(new BlockPos(i, k, j), Blocks.AIR.getDefaultState());
					}
				}
			}
			
			server.commandManager.executeCommand(server, "/worldborder set " + server.getMaxWorldSize());

    		timerData.resetAll();
    		timerData.markDirty();
    		saveData.resetAll();
    		saveData.markDirty();
    		
    		UHCPacketHandler.INSTANCE.sendToAll(new UHCPacketMessage(saveData));
            sender.sendMessage(new TranslationTextComponent("commands.uhc.reset.success"));
        }
    }
}
