package com.mrbysco.uhc.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class BooleanButton extends Button {
	protected Boolean booleanValue;

	public BooleanButton(int x, int y, Boolean booleanIn, Button.OnPress onPressIn) {
		super(x, y, 15, 13, Component.empty(), onPressIn);
		this.booleanValue = booleanIn;
	}

	public void setBoolean(Boolean value) {
		this.booleanValue = value;
	}

	public Boolean getBoolean() {
		return this.booleanValue;
	}

	@Override
	public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BookViewScreen.BOOK_LOCATION);
		int textureX = 47;
		int textureY = 192;
		if (isMouseOver(mouseX, mouseY))
			textureY += 14;

		if (booleanValue)
			textureX += 16;

		this.blit(matrixStack, x, y, textureX, textureY, 15, 13);
		if (this.isHoveredOrFocused()) {
			this.renderToolTip(matrixStack, mouseX, mouseY);
		}
	}
}