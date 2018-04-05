package com.Mrbysco.UHC.compat.ct;

import com.Mrbysco.UHC.compat.ct.actions.ActionTwilightBossRespawn;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.ultrahardcoremod.twilight.boss")
public class TwilightBossRespawnCT {

	@ZenMethod
    public static void addBoss(String position, String boss) {
        CraftTweakerAPI.apply(new ActionTwilightBossRespawn(position, boss));
	}
}
