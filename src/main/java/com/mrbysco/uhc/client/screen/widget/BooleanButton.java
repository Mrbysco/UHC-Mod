package com.mrbysco.uhc.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;

public class BooleanButton extends Button {
	protected boolean booleanValue;

	public BooleanButton(int x, int y, boolean booleanIn, Button.OnPress onPressIn) {
		super(x, y, 15, 13, Component.empty(), onPressIn, DEFAULT_NARRATION);
		this.booleanValue = booleanIn;
	}

	public void setBoolean(boolean value) {
		this.booleanValue = value;
	}

	public boolean getBoolean() {
		return this.booleanValue;
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		int textureX = 47;
		int textureY = 192;
		if (isMouseOver(mouseX, mouseY))
			textureY += 14;

		if (booleanValue)
			textureX += 16;

		guiGraphics.blit(BookViewScreen.BOOK_LOCATION, getX(), getY(), textureX, textureY, 15, 13);
		if (this.isHoveredOrFocused()) {
//			this.renderToolTip(guiGraphics, mouseX, mouseY);
		}
	}
}