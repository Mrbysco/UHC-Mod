package com.Mrbysco.UHC.compat.ct.actions;

import com.Mrbysco.UHC.lists.ConversionList;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;

public class ActionItemConversion implements IAction {

	private final ItemStack input;
	
	private final ItemStack output1;
	private final ItemStack output2;
	private final ItemStack output3;
	private final ItemStack output4;
	private final ItemStack output5;
	private final ItemStack output6;
	private final ItemStack output7;
	private final ItemStack output8;
	private final ItemStack output9;
	
	public ActionItemConversion(IItemStack input, IItemStack output1) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = CraftTweakerMC.getItemStack(output1);
		this.output2 = ItemStack.EMPTY;
		this.output3 = ItemStack.EMPTY;
		this.output4 = ItemStack.EMPTY;
		this.output5 = ItemStack.EMPTY;
		this.output6 = ItemStack.EMPTY;
		this.output7 = ItemStack.EMPTY;
		this.output8 = ItemStack.EMPTY;
		this.output9 = ItemStack.EMPTY;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = CraftTweakerMC.getItemStack(output1);
		this.output2 = CraftTweakerMC.getItemStack(output2);
		this.output3 = ItemStack.EMPTY;
		this.output4 = ItemStack.EMPTY;
		this.output5 = ItemStack.EMPTY;
		this.output6 = ItemStack.EMPTY;
		this.output7 = ItemStack.EMPTY;
		this.output8 = ItemStack.EMPTY;
		this.output9 = ItemStack.EMPTY;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = CraftTweakerMC.getItemStack(output1);
		this.output2 = CraftTweakerMC.getItemStack(output2);
		this.output3 = CraftTweakerMC.getItemStack(output3);
		this.output4 = ItemStack.EMPTY;
		this.output5 = ItemStack.EMPTY;
		this.output6 = ItemStack.EMPTY;
		this.output7 = ItemStack.EMPTY;
		this.output8 = ItemStack.EMPTY;
		this.output9 = ItemStack.EMPTY;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = CraftTweakerMC.getItemStack(output1);
		this.output2 = CraftTweakerMC.getItemStack(output2);
		this.output3 = CraftTweakerMC.getItemStack(output3);
		this.output4 = CraftTweakerMC.getItemStack(output4);
		this.output5 = ItemStack.EMPTY;
		this.output6 = ItemStack.EMPTY;
		this.output7 = ItemStack.EMPTY;
		this.output8 = ItemStack.EMPTY;
		this.output9 = ItemStack.EMPTY;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = CraftTweakerMC.getItemStack(output1);
		this.output2 = CraftTweakerMC.getItemStack(output2);
		this.output3 = CraftTweakerMC.getItemStack(output3);
		this.output4 = CraftTweakerMC.getItemStack(output4);
		this.output5 = CraftTweakerMC.getItemStack(output5);
		this.output6 = ItemStack.EMPTY;
		this.output7 = ItemStack.EMPTY;
		this.output8 = ItemStack.EMPTY;
		this.output9 = ItemStack.EMPTY;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5, IItemStack output6) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = CraftTweakerMC.getItemStack(output1);
		this.output2 = CraftTweakerMC.getItemStack(output2);
		this.output3 = CraftTweakerMC.getItemStack(output3);
		this.output4 = CraftTweakerMC.getItemStack(output4);
		this.output5 = CraftTweakerMC.getItemStack(output5);
		this.output6 = CraftTweakerMC.getItemStack(output6);
		this.output7 = ItemStack.EMPTY;
		this.output8 = ItemStack.EMPTY;
		this.output9 = ItemStack.EMPTY;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5, IItemStack output6, IItemStack output7) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = CraftTweakerMC.getItemStack(output1);
		this.output2 = CraftTweakerMC.getItemStack(output2);
		this.output3 = CraftTweakerMC.getItemStack(output3);
		this.output4 = CraftTweakerMC.getItemStack(output4);
		this.output5 = CraftTweakerMC.getItemStack(output5);
		this.output6 = CraftTweakerMC.getItemStack(output6);
		this.output7 = CraftTweakerMC.getItemStack(output7);
		this.output8 = ItemStack.EMPTY;
		this.output9 = ItemStack.EMPTY;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5, IItemStack output6, IItemStack output7, IItemStack output8) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = CraftTweakerMC.getItemStack(output1);
		this.output2 = CraftTweakerMC.getItemStack(output2);
		this.output3 = CraftTweakerMC.getItemStack(output3);
		this.output4 = CraftTweakerMC.getItemStack(output4);
		this.output5 = CraftTweakerMC.getItemStack(output5);
		this.output6 = CraftTweakerMC.getItemStack(output6);
		this.output7 = CraftTweakerMC.getItemStack(output7);
		this.output8 = CraftTweakerMC.getItemStack(output8);
		this.output9 = ItemStack.EMPTY;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5, IItemStack output6, IItemStack output7, IItemStack output8, IItemStack output9) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = CraftTweakerMC.getItemStack(output1);
		this.output2 = CraftTweakerMC.getItemStack(output2);
		this.output3 = CraftTweakerMC.getItemStack(output3);
		this.output4 = CraftTweakerMC.getItemStack(output4);
		this.output5 = CraftTweakerMC.getItemStack(output5);
		this.output6 = CraftTweakerMC.getItemStack(output6);
		this.output7 = CraftTweakerMC.getItemStack(output7);
		this.output8 = CraftTweakerMC.getItemStack(output8);
		this.output9 = CraftTweakerMC.getItemStack(output9);
	}
	
	public ActionItemConversion(IItemStack input) {
		this.input = CraftTweakerMC.getItemStack(input);
		this.output1 = ItemStack.EMPTY;
		this.output2 = ItemStack.EMPTY;
		this.output3 = ItemStack.EMPTY;
		this.output4 = ItemStack.EMPTY;
		this.output5 = ItemStack.EMPTY;
		this.output6 = ItemStack.EMPTY;
		this.output7 = ItemStack.EMPTY;
		this.output8 = ItemStack.EMPTY;
		this.output9 = ItemStack.EMPTY;
	}

	@Override
	public void apply() {
		ConversionList.addConversion(input, output1, output2, 
				output3, output4, output5, output6, output7, output8, output9);
	}
	
	@Override
	public String describe() {
		return String.format("Conversion for " + input.toString() + " has been removed");	
	}
}