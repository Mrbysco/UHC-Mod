package com.mrbysco.uhc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record AutoSmelt(Ingredient ingredient, ItemStack result, float experience) {
	public static final Codec<AutoSmelt> DIRECT_CODEC = ExtraCodecs.catchDecoderException(RecordCodecBuilder.create(inst -> inst.group(
			Ingredient.CODEC.fieldOf("ingredient").forGetter(AutoSmelt::ingredient),
			ItemStack.CODEC.fieldOf("result").forGetter(AutoSmelt::result),
			Codec.FLOAT.fieldOf("experience").forGetter(AutoSmelt::experience)
	).apply(inst, AutoSmelt::new)));
}
