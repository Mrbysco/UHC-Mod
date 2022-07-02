//package com.mrbysco.uhc.compat.ct.actions;
//
//import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
//import com.blamejared.crafttweaker.impl.entity.MCEntityType;
//import com.mrbysco.uhc.lists.EntityDataChangeList;
//import com.mrbysco.uhc.lists.EntityDataChangeList.AttributeChange;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//
//public class ActionEntityDataChanger implements IRuntimeAction {
//	private final EntityType<?> entityType;
//	private final AttributeChange attributeChange;
//
//	public ActionEntityDataChanger(MCEntityType entityType, ResourceLocation attributeLocation, float value) {
//		this.entityType = entityType.getInternal();
//		this.attributeChange = new AttributeChange(attributeLocation, value);
//	}
//
//	@Override
//	public void apply() {
//		EntityDataChangeList.addDataChange((EntityType<? extends LivingEntity>) entityType, attributeChange);
//	}
//
//	@Override
//	public String describe() {
//		return this.entityType.getRegistryName() + "'s entitydata will be changed upon spawn";
//	}
//}