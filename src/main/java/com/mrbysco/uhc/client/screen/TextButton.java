package com.mrbysco.uhc.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class TextButton extends Button {
	private final Minecraft mc;
	private int color = 0xFFFFAA00;
	private int hoverColor = 0xFFFF5555;
	private boolean shadow = true;

	public TextButton(int x, int y, Component text, Minecraft mc, Button.OnPress onPressIn) {
		super(x, y, mc.font.width(text), mc.font.lineHeight, text, onPressIn);
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
	public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		Font renderer = mc.font;

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
			renderer.drawShadow(matrixStack, component, x, y, colorInt);
		} else {
			renderer.draw(matrixStack, component, x, y, colorInt);
		}
		if (this.isHoveredOrFocused()) {
			this.renderToolTip(matrixStack, mouseX, mouseY);
		}
	}
}