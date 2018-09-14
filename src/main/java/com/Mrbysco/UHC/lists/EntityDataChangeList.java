package com.Mrbysco.UHC.lists;

import java.util.Collections;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

public class EntityDataChangeList {
	public static HashMap<Class<? extends Entity>, String> dataMap = new HashMap<>();
	
	public static void initializeDataChanges() {
		
	}
	
	public static void addDataChange(String entityId, String dataChange) 
	{
		if (dataMap.containsKey(EntityList.getClass(getEntityLocation(entityId))))
			return;
		else
			dataMap.put(EntityList.getClass(getEntityLocation(entityId)), dataChange);
	}
	
	public static ResourceLocation getEntityLocation(String name)
	{
		String[] splitResource = name.split(":");
		if (splitResource.length != 2)
			return null;
		else
			return new ResourceLocation(splitResource[0], splitResource[1]);
	}
}
