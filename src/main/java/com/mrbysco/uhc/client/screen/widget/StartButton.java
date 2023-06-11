package com.mrbysco.uhc.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;

public class StartButton extends Button {

	public StartButton(int x, int y, Button.OnPress onPressIn) {
		super(x, y, 85, 22, Component.empty(), onPressIn, DEFAULT_NARRATION);
	}

	/**
	 * Draws this button to the screen.
	 */
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		int textureX = 30;
		int textureY = 232;
		if (this.isMouseOver(mouseX, mouseY))
			textureX += 85;

		guiGraphics.blit(BookViewScreen.BOOK_LOCATION, getX(), getY(), textureX, textureY, 85, 22);
		if (this.isHoveredOrFocused()) {
//				this.renderToolTip(guiGraphics, mouseX, mouseY);
		}
	}
}