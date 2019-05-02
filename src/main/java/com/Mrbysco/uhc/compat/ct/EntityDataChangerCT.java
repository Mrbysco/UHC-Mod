package com.mrbysco.uhc.compat.ct;

import com.mrbysco.uhc.compat.ct.actions.ActionEntityDataChanger;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ultrahardcoremod.datachanger")
public class EntityDataChangerCT {
	@ZenMethod
    public static void changeData(String entityId, String dataName) {
		CraftTweakerAPI.apply(new ActionEntityDataChanger(entityId, dataName));
    }

    @ZenMethod
    public static void changeData(IEntityDefinition entity, String dataName) {
    	changeData(entity.getId(), dataName);
    }
}
