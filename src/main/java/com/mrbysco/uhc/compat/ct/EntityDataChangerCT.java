//package com.mrbysco.uhc.compat.ct;
//
//import com.blamejared.crafttweaker.api.CraftTweakerAPI;
//import com.blamejared.crafttweaker.api.annotations.ZenRegister;
//import com.blamejared.crafttweaker.impl.entity.MCEntityType;
//import com.mrbysco.uhc.compat.ct.actions.ActionEntityDataChanger;
//import net.minecraft.resources.ResourceLocation;
//import org.openzen.zencode.java.ZenCodeType.Method;
//import org.openzen.zencode.java.ZenCodeType.Name;
//
//@ZenRegister
//@Name("mods.spoiled.datachanger")
//public class EntityDataChangerCT {
//	@Method
//	public static void changeData(MCEntityType entity, ResourceLocation attribute, float value) {
//		CraftTweakerAPI.apply(new ActionEntityDataChanger(entity, attribute, value));
//	}
//}
