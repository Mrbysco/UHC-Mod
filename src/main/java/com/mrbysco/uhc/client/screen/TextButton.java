package com.mrbysco.uhc.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class TextButton extends Button {
	private final Minecraft mc;
	private int color = 0xFFFFAA00;
	private int hoverColor = 0xFFFF5555;
	private boolean shadow = true;

	public TextButton(int x, int y, ITextComponent text, Minecraft mc, Button.IPressable onPressIn) {
		super(x, y, mc.fontRenderer.getStringPropertyWidth(text), mc.fontRenderer.FONT_HEIGHT, text, onPressIn);
		this.mc = mc;
	}

	@Override
	public void setMessage(ITextComponent message) {
		super.setMessage(message);
		this.width = mc.fontRenderer.getStringPropertyWidth(message);
	}

	public void shadowEnabled(boolean shadow){
		this.shadow = shadow;
	}
	
	public void setColors(int normal, int hover){
		this.hoverColor = hover;
		this.color = normal;
	}

	@Override
	public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		FontRenderer renderer = mc.fontRenderer;

		int colorInt = this.color;
		if (color != 0) {
			colorInt = color;
		} else if (!this.active) {
			colorInt = 0xAAAAAA;
		} else if (isMouseOver(mouseX, mouseY)) {
			colorInt = this.hoverColor;
		}

		ITextComponent component = getMessage();
		if(shadow) {
			renderer.drawTextWithShadow(matrixStack, component, x, y, colorInt);
		} else {
			renderer.drawText(matrixStack, component, x, y, colorInt);
		}
		if (this.isHovered()) {
			this.renderToolTip(matrixStack, mouseX, mouseY);
		}
	}
}