package com.mrbysco.uhc.spawnitem;

import java.util.Objects;

public final class ItemObject {
	private final String itemLocation;
	private final String components;
	private final int count;

	public ItemObject(String resourceLocation, String tag, int count) {
		this.itemLocation = resourceLocation;
		this.components = tag;
		this.count = count;
	}

	public String slotName() {
		return itemLocation;
	}

	public String itemLocation() {
		return itemLocation;
	}

	public String components() {
		return components;
	}

	public int count() {
		return count;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ItemObject) obj;
		return Objects.equals(this.itemLocation, that.itemLocation) &&
				Objects.equals(this.components, that.components) &&
				this.count == that.count;
	}

	@Override
	public int hashCode() {
		return Objects.hash(itemLocation, components, count);
	}

	@Override
	public String toString() {
		return "ItemObject[" +
				"resourceLocation=" + itemLocation + ", " +
				"components=" + components + ", " +
				"count=" + count + ']';
	}
}
