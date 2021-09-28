package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IWorldInfo;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class UHCPage5Packet {
	public boolean weatherCycle;
	public boolean mobGriefing;
	public boolean customHealth;
	public int maxHealth;
	
	public boolean randomSpawns;
	public int spreadDistance;
	public int spreadMaxRange;
	public boolean spreadRespectTeam;
	
	public UHCPage5Packet(boolean weatherCycle, boolean mobGriefing, boolean customHealth, int maxHealth, boolean randomSpawns, 
			int spreadDistance, int spreadMaxRange, boolean spreadRespectTeam) {
		this.weatherCycle = weatherCycle;
		this.mobGriefing = mobGriefing;
		this.customHealth = customHealth;
		this.maxHealth = maxHealth;
		
		this.randomSpawns = randomSpawns;
		this.spreadDistance = spreadDistance;
		this.spreadMaxRange = spreadMaxRange;
		this.spreadRespectTeam = spreadRespectTeam;
	}

	public void encode(PacketBuffer buf) {
		buf.writeBoolean(weatherCycle);
		buf.writeBoolean(mobGriefing);
		buf.writeBoolean(customHealth);
		buf.writeInt(maxHealth);
		
		buf.writeBoolean(randomSpawns);
		buf.writeInt(spreadDistance);
		buf.writeInt(spreadMaxRange);
		buf.writeBoolean(spreadRespectTeam);

	}

	public static UHCPage5Packet decode(final PacketBuffer packetBuffer) {
		return new UHCPage5Packet(packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readInt(),
				packetBuffer.readBoolean(), packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readBoolean());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayerEntity serverPlayer = ctx.getSender();
				ServerWorld serverWorld = serverPlayer.getServerWorld();
				ServerWorld overworld = serverPlayer.getServer().getWorld(World.OVERWORLD);
				if(overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundNBT playerData = serverPlayer.getPersistentData();
					MinecraftServer minecraftserver = serverWorld.getServer();
					IWorldInfo wInfo = serverWorld.getWorldInfo();
					GameRules rules = minecraftserver.getGameRules();

					if(playerData.getBoolean("canEditUHC")) {
						if(mobGriefing) {
							if(!rules.getBoolean(GameRules.MOB_GRIEFING))
								rules.get(GameRules.MOB_GRIEFING).set(true, minecraftserver);
						} else {
							if(rules.getBoolean(GameRules.MOB_GRIEFING))
								rules.get(GameRules.MOB_GRIEFING).set(false, minecraftserver);
						}

						if(weatherCycle) {
							if(!rules.getBoolean(GameRules.DO_WEATHER_CYCLE))
								rules.get(GameRules.DO_WEATHER_CYCLE).set(true, minecraftserver);
						} else {
							if(serverWorld.isRaining())
								wInfo.setRaining(false);
							if(rules.getBoolean(GameRules.DO_WEATHER_CYCLE))
								rules.get(GameRules.DO_WEATHER_CYCLE).set(false, minecraftserver);
						}

						saveData.setWeatherEnabled(weatherCycle);
						saveData.setMobGriefing(mobGriefing);
						saveData.setApplyCustomHealth(customHealth);
						saveData.setMaxHealth(maxHealth);

						saveData.setRandomSpawns(randomSpawns);
						saveData.setSpreadDistance(spreadDistance);
						saveData.setSpreadMaxRange(spreadMaxRange);
						saveData.setSpreadRespectTeam(spreadRespectTeam);
						saveData.markDirty();

						UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(serverPlayer.getUniqueID(), saveData));
					} else {
						serverPlayer.sendMessage(new StringTextComponent("You don't have permissions to edit the UHC book").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
