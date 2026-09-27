package com.mrbysco.uhc.item;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.network.payload.UHCSyncPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class UHCBook extends Item {
	public UHCBook(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		if (level.isClientSide()) {
			com.mrbysco.uhc.client.screen.UHCBookScreen.openScreen(player);
		} else {
			UHCSaveData saveData = UHCSaveData.get(level);
			PacketDistributor.sendToPlayer((ServerPlayer) player, new UHCSyncPayload(saveData.save(new CompoundTag(), level.registryAccess())));
		}

		return super.use(level, player, usedHand);
	}
}
