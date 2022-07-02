//package com.mrbysco.uhc.compat.ct.actions;
//
//import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
//import com.blamejared.crafttweaker.api.item.IItemStack;
//import com.mrbysco.uhc.lists.SpawnItemList;
//import net.minecraft.world.item.ItemStack;
//
//public class ActionSpawnItems implements IRuntimeAction {
//
//	private final ItemStack[] outputs;
//
//	public ActionSpawnItems(IItemStack[] stacks) {
//		this.outputs = new ItemStack[stacks.length];
//		for (int i = 0; i < stacks.length; i++) {
//			this.outputs[i] = stacks[i].getInternal();
//		}
//	}
//
//	@Override
//	public void apply() {
//		SpawnItemList.addSpawnItems(outputs);
//	}
//
//	@Override
//	public String describe() {
//		return "Spawn items have been added";
//	}
//}