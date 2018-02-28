package com.Mrbysco.UHC.compat.ct;

import com.Mrbysco.UHC.lists.ConversionList;

import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ActionItemConversion implements IAction {

	private final IItemStack input;
	
	private final IItemStack output1;
	private final IItemStack output2;
	private final IItemStack output3;
	private final IItemStack output4;
	private final IItemStack output5;
	private final IItemStack output6;
	private final IItemStack output7;
	private final IItemStack output8;
	private final IItemStack output9;
	
	private final boolean removal;

	public ActionItemConversion(IItemStack input, IItemStack output1, boolean removal) {
		this.input = input;
		this.output1 = output1;
		this.output2 = null;
		this.output3 = null;
		this.output4 = null;
		this.output5 = null;
		this.output6 = null;
		this.output7 = null;
		this.output8 = null;
		this.output9 = null;
		this.removal = removal;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, boolean removal) {
		this.input = input;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = null;
		this.output4 = null;
		this.output5 = null;
		this.output6 = null;
		this.output7 = null;
		this.output8 = null;
		this.output9 = null;
		this.removal = removal;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, boolean removal) {
		this.input = input;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = output3;
		this.output4 = null;
		this.output5 = null;
		this.output6 = null;
		this.output7 = null;
		this.output8 = null;
		this.output9 = null;
		this.removal = removal;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			boolean removal) {
		this.input = input;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = output3;
		this.output4 = output4;
		this.output5 = null;
		this.output6 = null;
		this.output7 = null;
		this.output8 = null;
		this.output9 = null;
		this.removal = removal;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5, boolean removal) {
		this.input = input;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = output3;
		this.output4 = output4;
		this.output5 = output5;
		this.output6 = null;
		this.output7 = null;
		this.output8 = null;
		this.output9 = null;
		this.removal = removal;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5, IItemStack output6, boolean removal) {
		this.input = input;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = output3;
		this.output4 = output4;
		this.output5 = output5;
		this.output6 = output6;
		this.output7 = null;
		this.output8 = null;
		this.output9 = null;
		this.removal = removal;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5, IItemStack output6, IItemStack output7, boolean removal) {
		this.input = input;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = output3;
		this.output4 = output4;
		this.output5 = output5;
		this.output6 = output6;
		this.output7 = output7;
		this.output8 = null;
		this.output9 = null;
		this.removal = removal;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5, IItemStack output6, IItemStack output7, IItemStack output8, boolean removal) {
		this.input = input;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = output3;
		this.output4 = output4;
		this.output5 = output5;
		this.output6 = output6;
		this.output7 = output7;
		this.output8 = output8;
		this.output9 = null;
		this.removal = removal;
	}
	
	public ActionItemConversion(IItemStack input, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4, 
			IItemStack output5, IItemStack output6, IItemStack output7, IItemStack output8, IItemStack output9, boolean removal) {
		this.input = input;
		this.output1 = output1;
		this.output2 = output2;
		this.output3 = output3;
		this.output4 = output4;
		this.output5 = output5;
		this.output6 = output6;
		this.output7 = output7;
		this.output8 = output8;
		this.output9 = output9;
		this.removal = removal;
	}
	
	public ActionItemConversion(IItemStack input, boolean removal) {
		this.input = input;
		this.output1 = null;
		this.output2 = null;
		this.output3 = null;
		this.output4 = null;
		this.output5 = null;
		this.output6 = null;
		this.output7 = null;
		this.output8 = null;
		this.output9 = null;
		this.removal = removal;
	}

	@Override
	public void apply() {
		if (this.removal)
			ConversionList.removeConversion(Item.getByNameOrId(input.getName()), input.getMetadata());
		else
			ConversionList.addConversion(Item.getByNameOrId(input.getName()), input.getMetadata(), toStack(output1), toStack(output2), 
				toStack(output3), toStack(output4), toStack(output5), toStack(output6), toStack(output7), toStack(output8), toStack(output9));
	}

	public ItemStack toStack(IItemStack iitem) {
		ItemStack stack;
		
		if (iitem.isEmpty())
		{
			stack = ItemStack.EMPTY;
		}
		else
		{
			stack = new ItemStack(Item.getByNameOrId(iitem.getName()), iitem.getAmount(), iitem.getMetadata());
		}
		
		return stack;
	}
	
	@Override
	public String describe() {
		if (this.removal)
			return String.format("Conversion for " + input.toString() + " has been added");	
		else
			return String.format("Conversion for " + input.toString() + " has been removed");	
	}
}