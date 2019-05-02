package com.mrbysco.uhc.compat.ct.actions;

import com.mrbysco.uhc.lists.EntityDataChangeList;
import crafttweaker.IAction;

public class ActionEntityDataChanger implements IAction {

	private final String entityId;
	private final String dataChange;

	public ActionEntityDataChanger(String entityId, String dataChange) {
		this.entityId = entityId;
		this.dataChange = dataChange;
	}
	
	@Override
	public void apply() {
		EntityDataChangeList.addDataChange(entityId, dataChange);
	}

	@Override
	public String describe() {
		return String.format(this.entityId.toString() + "'s entitydata will be changed upon spawn");
	}
}