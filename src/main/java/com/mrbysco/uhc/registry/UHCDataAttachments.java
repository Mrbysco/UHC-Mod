package com.mrbysco.uhc.registry;

import com.mojang.serialization.Codec;
import com.mrbysco.uhc.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class UHCDataAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Reference.MOD_ID);

	public static final Supplier<AttachmentType<Boolean>> MODIFIED_MAX_HEALTH = ATTACHMENT_TYPES.register(
			"modified_max_health", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

	public static final Supplier<AttachmentType<Boolean>> CAN_EDIT_UHC = ATTACHMENT_TYPES.register(
			"can_edit_uhc", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

	public static final Supplier<AttachmentType<Boolean>> TEAM_ANTI_SPAM = ATTACHMENT_TYPES.register(
			"team_anti_spam", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

	public static final Supplier<AttachmentType<Boolean>> START_FATIGUE = ATTACHMENT_TYPES.register(
			"start_fatigue", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

	public static final Supplier<AttachmentType<GlobalPos>> DEATH_POS = ATTACHMENT_TYPES.register(
			"death_pos", () -> AttachmentType.builder(() -> GlobalPos.of(Level.OVERWORLD, BlockPos.ZERO)).serialize(GlobalPos.CODEC).build());
}
