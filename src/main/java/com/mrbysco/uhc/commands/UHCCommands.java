package com.mrbysco.uhc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mrbysco.uhc.Reference;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
public class UHCCommands {
	public static void initializeCommands (CommandDispatcher<CommandSource> dispatcher) {
		final LiteralArgumentBuilder<CommandSource> root = Commands.literal(Reference.MOD_ID);
//		root.requires((commandSource) -> commandSource.hasPermissionLevel(2))
//				.then(Commands.literal("tileentity").then(Commands.literal("list").executes(UHCCommands::listTiles)));
		//TODO: Add the commands!
		dispatcher.register(root);
	}
}
