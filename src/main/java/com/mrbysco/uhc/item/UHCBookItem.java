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
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide) {
			com.mrbysco.uhc.client.screen.UHCBookScreen.openScreen(player);
		} else {
			ServerLevel overworld = level.getServer().overworld();
			if (overworld != null) {
				UHCSaveData saveData = UHCSaveData.get(overworld);
				UHCPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new UHCPacketMessage(saveData));
			}
		}

		return super.use(level, player, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> componentList, TooltipFlag flag) {
		super.appendHoverText(stack, level, componentList, flag);
		componentList.add(Component.literal("Ultra Hard Coremod"));
	}
}
