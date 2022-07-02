package com.mrbysco.uhc.lists;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;

public class EntityDataChangeList {
	public static HashMap<EntityType<? extends LivingEntity>, AttributeChange> dataMap = new HashMap<>();

	public static void initializeDataChanges() {

	}

	public static void addDataChange(EntityType<? extends LivingEntity> entityType, AttributeChange dataChange) {
		if (!dataMap.containsKey(entityType))
			dataMap.put(entityType, dataChange);
	}

	public static class AttributeChange {
		private final ResourceLocation attributeLocation;
		private final float value;

		public AttributeChange(ResourceLocation attributeLocation, float value) {
			this.attributeLocation = attributeLocation;
			this.value = value;
		}

		public ResourceLocation getAttributeLocation() {
			return attributeLocation;
		}

		public float getValue() {
			return value;
		}
	}
}
