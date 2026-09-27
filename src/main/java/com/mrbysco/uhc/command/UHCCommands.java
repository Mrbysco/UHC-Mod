package com.mrbysco.uhc.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class UHCCommands {
	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event) {
		UHCCommands.initializeCommands(event.getDispatcher(), event.getBuildContext());
	}

	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {

	}
}
