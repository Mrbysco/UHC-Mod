package com.mrbysco.uhc.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class TextButton extends Button {
	private final Minecraft mc;
	private int color = 0xFFFFAA00;
	private int hoverColor = 0xFFFF5555;
	private boolean shadow = true;

	public TextButton(int x, int y, Component text, Minecraft mc, OnPress onPressIn) {
		super(x, y, mc.font.width(text), mc.font.lineHeight, text, onPressIn, DEFAULT_NARRATION);
		this.mc = mc;
	}

	@Override
	public void setMessage(Component message) {
		super.setMessage(message);
		this.width = mc.font.width(message);
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		Font font = mc.font;

		int colorInt = this.color;
		if (!this.active) {
			colorInt = 0xAAAAAA;
		} else if (isMouseOver(mouseX, mouseY)) {
			colorInt = this.hoverColor;
		}

		Component component = getMessage();
		guiGraphics.drawString(font, component, getX(), getY(), colorInt, shadow);
	}
}