package com.mrbysco.uhc.client.screen.widget;

import com.mrbysco.uhc.client.screen.UHCBookScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class LocationButton extends Button {
	public LocationButton(int x, int y, Button.OnPress onPressIn) {
		super(x, y, 14, 13, Component.empty(), onPressIn, DEFAULT_NARRATION);
	}

	/**
	 * Draws this button to the screen.
	 */
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		int textureX = 0;
		int textureY = 232;
		if (this.isMouseOver(mouseX, mouseY))
			textureX += 15;

		guiGraphics.blit(UHCBookScreen.BOOK_TEXTURE, getX(), getY(), textureX, textureY, 15, 13);
		if (this.isHoveredOrFocused()) {
//				this.renderToolTip(guiGraphics, mouseX, mouseY);
		}
	}
}