package com.mrbysco.uhc.client.screen.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class NumberEditbox extends EditBox {
	public final int decimalPoints;
	public final boolean allowNegative;

	public NumberEditbox(Font font, int x, int y, int width, int height, Component defaultValue, int decimalPoints, boolean allowNegative) {
		super(font, x, y, width, height, defaultValue);
		this.decimalPoints = decimalPoints;
		this.allowNegative = allowNegative;
	}

	public NumberEditbox(Font font, int x, int y, int width, int height, Component defaultValue, int decimalPoints) {
		this(font, x, y, width, height, defaultValue, decimalPoints, false);
	}

	public NumberEditbox(Font font, int x, int y, int width, int height, Component defaultValue, boolean allowNegative) {
		this(font, x, y, width, height, defaultValue, 2, allowNegative);
	}

	public NumberEditbox(Font font, int x, int y, int width, int height, Component defaultValue) {
		this(font, x, y, width, height, defaultValue, 2, false);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

//	@Override
//	public boolean charTyped(char typedChar, int keyCode) {
//		return super.charTyped(typedChar, keyCode);
//	}

	@Override
	public void insertText(String textToWrite) {
		String newValue = textToWrite.replaceAll("[^0-9.-]", "");
		if (allowNegative) {
			newValue = textToWrite.replace("-", "");
		}
		if (decimalPoints == 0) {
			newValue = newValue.replace(".", "");
		}
		super.insertText(newValue);
	}

	@Override
	public void setValue(String value) {
		if (value.isEmpty()) {
			super.setValue("0");
		} else {
			//Minus checking
			boolean containsMinus = value.contains("-");
			if (value.chars().filter(ch -> ch == '-').count() > 1 || !value.startsWith("-") && containsMinus) {
				value = value.replace("-", "");
			}
			if (!allowNegative) {
				if (value.contains("-")) {
					value = value.replace("-", "");
				}
			}
			super.setValue(String.format("%." + decimalPoints + "f", Float.parseFloat(value)));
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.setFocused(true);
		super.onClick(mouseX, mouseY);
	}
}
