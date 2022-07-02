package com.mrbysco.uhc.packets;

import com.mrbysco.uhc.data.UHCSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

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

	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(weatherCycle);
		buf.writeBoolean(mobGriefing);
		buf.writeBoolean(customHealth);
		buf.writeInt(maxHealth);

		buf.writeBoolean(randomSpawns);
		buf.writeInt(spreadDistance);
		buf.writeInt(spreadMaxRange);
		buf.writeBoolean(spreadRespectTeam);

	}

	public static UHCPage5Packet decode(final FriendlyByteBuf packetBuffer) {
		return new UHCPage5Packet(packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readBoolean(), packetBuffer.readInt(),
				packetBuffer.readBoolean(), packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readBoolean());
	}

	public void handle(Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer() && ctx.getSender() != null) {
				ServerPlayer serverPlayer = ctx.getSender();
				ServerLevel serverWorld = serverPlayer.getLevel();
				ServerLevel overworld = serverPlayer.getServer().getLevel(Level.OVERWORLD);
				if (overworld != null) {
					UHCSaveData saveData = UHCSaveData.get(overworld);
					CompoundTag playerData = serverPlayer.getPersistentData();
					MinecraftServer minecraftserver = serverWorld.getServer();
					LevelData wInfo = serverWorld.getLevelData();
					GameRules rules = minecraftserver.getGameRules();

					if (playerData.getBoolean("canEditUHC")) {
						if (mobGriefing) {
							if (!rules.getBoolean(GameRules.RULE_MOBGRIEFING))
								rules.getRule(GameRules.RULE_MOBGRIEFING).set(true, minecraftserver);
						} else {
							if (rules.getBoolean(GameRules.RULE_MOBGRIEFING))
								rules.getRule(GameRules.RULE_MOBGRIEFING).set(false, minecraftserver);
						}

						if (weatherCycle) {
							if (!rules.getBoolean(GameRules.RULE_WEATHER_CYCLE))
								rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(true, minecraftserver);
						} else {
							if (serverWorld.isRaining())
								wInfo.setRaining(false);
							if (rules.getBoolean(GameRules.RULE_WEATHER_CYCLE))
								rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, minecraftserver);
						}

						saveData.setWeatherEnabled(weatherCycle);
						saveData.setMobGriefing(mobGriefing);
						saveData.setApplyCustomHealth(customHealth);
						saveData.setMaxHealth(maxHealth);

						saveData.setRandomSpawns(randomSpawns);
						saveData.setSpreadDistance(spreadDistance);
						saveData.setSpreadMaxRange(spreadMaxRange);
						saveData.setSpreadRespectTeam(spreadRespectTeam);
						saveData.setDirty();

						UHCPacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UHCPacketMessage(serverPlayer.getUUID(), saveData));
					} else {
						serverPlayer.sendSystemMessage(Component.literal("You don't have permissions to edit the UHC book").withStyle(ChatFormatting.RED));
					}
				}

			}
		});
		ctx.setPacketHandled(true);
	}
}
