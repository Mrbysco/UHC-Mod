package com.mrbysco.uhc.item;

import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.packets.UHCPacketHandler;
import com.mrbysco.uhc.packets.UHCPacketMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class UHCBookItem extends Item {
	public UHCBookItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		if (worldIn.isClientSide) {
			com.mrbysco.uhc.client.screen.UHCBookScreen.openScreen(playerIn);
		} else {
			ServerLevel overworld = worldIn.getServer().getLevel(Level.OVERWORLD);
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) playerIn), new UHCPacketMessage(saveData));
			}
		}

		return super.use(worldIn, playerIn, handIn);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(Component.literal("Ultra Hard Coremod"));
	}
}
