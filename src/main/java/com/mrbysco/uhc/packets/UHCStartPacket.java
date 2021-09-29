package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.utils.SpreadPosition;
import com.mrbysco.uhc.utils.SpreadUtil;
import com.mrbysco.uhc.utils.TeamUtil;
import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.ArrayList;
import java.util.function.Supplier;

public class UHCStartPacket{
	public UHCStartPacket() {

	}

	public void encode(PacketBuffer buf) {

	}

	public static UHCStartPacket decode(final PacketBuffer packetBuffer) {
		return new UHCStartPacket();
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayerEntity serverPlayer = ctx.getSender();
				ServerWorld overworld = serverPlayer.getServer().getWorld(World.OVERWORLD);
				if(overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundNBT playerData = serverPlayer.getPersistentData();
					ServerWorld world = serverPlayer.getServerWorld();
					WorldBorder border = world.getWorldBorder();
					MinecraftServer server = world.getServer();
					ArrayList<ServerPlayerEntity> playerList = (ArrayList<ServerPlayerEntity>)server.getPlayerList().getPlayers();
					Scoreboard scoreboard = world.getScoreboard();
					IWorldInfo info = world.getWorldInfo();

					if(playerData.getBoolean("canEditUHC") == true) {
						ArrayList<ServerPlayerEntity>soloPlayers = (ArrayList<ServerPlayerEntity>) playerList.clone();
						ArrayList<ServerPlayerEntity>teamPlayers = (ArrayList<ServerPlayerEntity>) playerList.clone();

						for (PlayerEntity player : playerList) {
							if(player.getTeam() == scoreboard.getTeam("spectator"))
								player.setGameType(GameType.SPECTATOR);
							if(player.getTeam() == null)
								scoreboard.addPlayerToTeam(player.getName().getString(), scoreboard.getTeam("solo"));
							if(player.getTeam() != scoreboard.getTeam("solo"))
								soloPlayers.remove(player);
						}

						teamPlayers.removeAll(soloPlayers);

						double centerX = saveData.getBorderCenterX();
						double centerZ = saveData.getBorderCenterZ();
						if (border.getCenterX() != centerX && border.getCenterZ() != centerZ)
							border.setCenter(centerX, centerZ);

						int BorderSize = saveData.getBorderSize();
						server.getCommandManager().handleCommand(serverPlayer.getCommandSource(), "/worldborder set " + BorderSize);
						//border.setTransition(BorderSize);

						double spreadDistance = saveData.getSpreadDistance();
						double spreadMaxRange = saveData.getSpreadMaxRange();

						if(spreadMaxRange >= (BorderSize / 2))
							spreadMaxRange = (BorderSize / 2);

						world.setDayTime(0);
						info.setRaining(false);

						if(saveData.isRandomSpawns())
						{
							try {
								SpreadUtil.spread(soloPlayers, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, world, saveData.isSpreadRespectTeam());
							} catch (CommandException e) {
								e.printStackTrace();
							}

							try {
								SpreadUtil.spread(teamPlayers, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, world, false);
							} catch (CommandException e) {
								e.printStackTrace();
							}
						} else {
							for(PlayerEntity player : playerList) {
								if(player.getTeam() != scoreboard.getTeam("solo")) {
									BlockPos pos = TeamUtil.getPosForTeam(player.getTeam().getColor());

									((ServerPlayerEntity)player).connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
								} else {
									try {
										SpreadUtil.spread(soloPlayers, new SpreadPosition(centerX,centerZ), spreadDistance, spreadMaxRange, world, false);
									} catch (CommandException e) {
										e.printStackTrace();
									}
								}
							}
						}

						for(PlayerEntity player : playerList) {
							ScoreObjective score = scoreboard.getObjective("health");
							if(score != null)
								scoreboard.removeObjectiveFromEntity(player.getName().getString(), score);

							if(player.isCreative())
								player.setGameType(GameType.SURVIVAL);

							if (player.getActivePotionEffect(Effects.GLOWING) != null)
								player.removePotionEffect(Effects.GLOWING);
						}

						if(saveData.isSpawnRoom()) {
							double centerX1 = centerX -7;
							double centerX2 = centerX +7;
							double centerZ1 = centerZ -7;
							double centerZ2 = centerZ +7;

							for(double i = centerX1; i <= centerX2; i++) {
								for(double j = centerZ1; j <= centerZ2; j++) {
									for(double k = 250; k <= 253; k++) {
										world.setBlockState(new BlockPos(i, k, j), Blocks.AIR.getDefaultState());
									}
								}
							}
							saveData.setSpawnRoom(false);
							saveData.setSpawnRoomDimension(World.OVERWORLD.getLocation());
							saveData.markDirty();
						}

						saveData.setUHCDimension(serverPlayer.world.getDimensionKey().getLocation());
						saveData.setUhcStarting(true);
						saveData.markDirty();
					} else {
						serverPlayer.sendMessage(new StringTextComponent(TextFormatting.RED + "You don't have permissions to start the UHC"), Util.DUMMY_UUID);
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
