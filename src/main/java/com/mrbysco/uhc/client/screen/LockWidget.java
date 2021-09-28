package com.mrbysco.uhc.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrbysco.uhc.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class LockWidget extends Button {
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");
	protected Boolean lockValue;
	
	public LockWidget(int x, int y, Boolean booleanIn, Button.IPressable onPressIn) {
		super(x, y, 15, 13, StringTextComponent.EMPTY, onPressIn);
		this.lockValue = booleanIn;
	}
	
	public void setBoolean(Boolean value){
		this.lockValue = value;
	}
	
	public Boolean getBoolean() {
		return this.lockValue;
	}

	@Override
	public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		int textureX = 79;
		int textureY = 192;
		if (isMouseOver(mouseX, mouseY))
			textureY += 16;

		if (!lockValue)
			textureX += 16;

		this.blit(matrixStack, x, y,  textureX, textureY, 15, 13);
		if (this.isHovered()) {
			this.renderToolTip(matrixStack, mouseX, mouseY);
		}
	}
}