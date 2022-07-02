package com.mrbysco.uhc.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.uhc.Reference;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class LockButton extends Button {
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");
	protected Boolean lockValue;

	public LockButton(int x, int y, Boolean booleanIn, Button.OnPress onPressIn) {
		super(x, y, 15, 13, Component.empty(), onPressIn);
		this.lockValue = booleanIn;
	}

	public void setBoolean(Boolean value) {
		this.lockValue = value;
	}

	public Boolean getBoolean() {
		return this.lockValue;
	}

	@Override
	public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BookViewScreen.BOOK_LOCATION);
		int textureX = 79;
		int textureY = 192;
		if (isMouseOver(mouseX, mouseY))
			textureY += 16;

		if (!lockValue)
			textureX += 16;

		this.blit(matrixStack, x, y, textureX, textureY, 15, 13);
		if (this.isHoveredOrFocused()) {
			this.renderToolTip(matrixStack, mouseX, mouseY);
		}
	}
}