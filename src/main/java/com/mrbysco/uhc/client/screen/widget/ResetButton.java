package com.mrbysco.uhc.client.screen.widget;

import com.mrbysco.uhc.client.screen.UHCBookScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ResetButton extends Button {
	public ResetButton(int x, int y, Button.OnPress onPressIn) {
		super(x, y, 16, 13, Component.empty(), onPressIn, DEFAULT_NARRATION);
	}

	/**
	 * Draws this button to the screen.
	 */
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		int textureX = 0;
		int textureY = 218;
		if (this.isMouseOver(mouseX, mouseY))
			textureX += 16;

		guiGraphics.blit(UHCBookScreen.BOOK_TEXTURE, getX(), getY(), textureX, textureY, 16, 13);
		if (this.isHoveredOrFocused()) {
//			this.renderToolTip(guiGraphics, mouseX, mouseY);
		}
	}
}