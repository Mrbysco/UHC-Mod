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

	public TextButton(int x, int y, Component text, Minecraft mc, Button.OnPress onPressIn) {
		super(x, y, mc.font.width(text), mc.font.lineHeight, text, onPressIn, DEFAULT_NARRATION);
		this.mc = mc;
	}

	@Override
	public void setMessage(Component message) {
		super.setMessage(message);
		this.width = mc.font.width(message);
	}

	public void shadowEnabled(boolean shadow) {
		this.shadow = shadow;
	}

	public void setColors(int normal, int hover) {
		this.hoverColor = hover;
		this.color = normal;
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		Font font = mc.font;

		int colorInt = this.color;
		if (color != 0) {
			colorInt = color;
		} else if (!this.active) {
			colorInt = 0xAAAAAA;
		} else if (isMouseOver(mouseX, mouseY)) {
			colorInt = this.hoverColor;
		}

		Component component = getMessage();
		if (shadow) {
			guiGraphics.drawString(font, component, getX(), getY(), colorInt);
		} else {
			guiGraphics.drawString(font, component, getX(), getY(), colorInt, false);
		}
		if (this.isHoveredOrFocused()) {
//			this.renderToolTip(matrixStack, mouseX, mouseY);
		}
	}
}