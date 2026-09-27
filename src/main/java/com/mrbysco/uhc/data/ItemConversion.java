package com.mrbysco.uhc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record ItemConversion(Ingredient ingredient, List<ItemStack> results) {
	public static final Codec<ItemConversion> DIRECT_CODEC = ExtraCodecs.catchDecoderException(RecordCodecBuilder.create(inst -> inst.group(
			Ingredient.CODEC.fieldOf("ingredient").forGetter(ItemConversion::ingredient),
			ItemStack.CODEC.listOf().fieldOf("result").forGetter(ItemConversion::results)
	).apply(inst, ItemConversion::new)));
}
