package com.mrbysco.uhc.client.screen.widget;

import com.mrbysco.uhc.Reference;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class LockButton extends Button {
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");
	protected boolean lockValue;

	public LockButton(int x, int y, boolean booleanIn, Button.OnPress onPressIn) {
		super(x, y, 15, 13, Component.empty(), onPressIn, DEFAULT_NARRATION);
		this.lockValue = booleanIn;
	}

	public void setBoolean(Boolean value) {
		this.lockValue = value;
	}

	public boolean getBoolean() {
		return this.lockValue;
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		int textureX = 79;
		int textureY = 192;
		if (isMouseOver(mouseX, mouseY))
			textureY += 16;

		if (!lockValue)
			textureX += 16;

		guiGraphics.blit(BookViewScreen.BOOK_LOCATION, getX(), getY(), textureX, textureY, 15, 13);
		if (this.isHoveredOrFocused()) {
//			this.renderToolTip(guiGraphics, mouseX, mouseY);
		}
	}
}