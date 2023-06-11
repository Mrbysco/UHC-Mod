package com.mrbysco.uhc.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class ColorButton extends Button {
	private final boolean solo;
	private final boolean randomize;
	public final int color;
	public final Component name;

	public final int textX;
	public final int textY;

	public ColorButton(int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, Component nameIn, Button.OnPress onPressIn) {
		super(x, y, widthIn, heightIn, Component.empty(), onPressIn, DEFAULT_NARRATION);
		this.textX = textXIn;
		this.textY = textYIn;
		this.color = colorIn;
		this.name = nameIn;
		this.solo = false;
		this.randomize = false;
	}

	public ColorButton(int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, Component nameIn, boolean soloIn, Button.OnPress onPressIn) {
		super(x, y, widthIn, heightIn, Component.empty(), onPressIn, DEFAULT_NARRATION);
		this.textX = textXIn;
		this.textY = textYIn;
		this.color = colorIn;
		this.name = nameIn;
		this.solo = soloIn;
		this.randomize = false;
	}

	public ColorButton(int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, Component nameIn, boolean soloIn, boolean randomizeIn, Button.OnPress onPressIn) {
		super(x, y, widthIn, heightIn, Component.empty(), onPressIn, DEFAULT_NARRATION);
		this.textX = textXIn;
		this.textY = textYIn;
		this.color = colorIn;
		this.name = nameIn;
		this.solo = false;
		this.randomize = randomizeIn;
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			Font font = Minecraft.getInstance().font;
			this.isHovered = isMouseOver(mouseX, mouseY);

			PoseStack poseStack = guiGraphics.pose();
			poseStack.pushPose();
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

			String randomizeMessage = I18n.get("book.uhc.team.randomizer");

			if (!this.solo && !this.randomize) {
				guiGraphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + height, color);
			} else {
				guiGraphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + height, 0xFF555555);
				guiGraphics.fill(this.getX() + 1, this.getY() + 1, this.getX() + width - 1, this.getY() + height - 1, color);

				if (this.randomize)
					guiGraphics.drawCenteredString(font, randomizeMessage, textX - 6, textY, 0xFFFFFF55);
			}

			String joinMessage = I18n.get("book.uhc.team.hover", name.getString());
			if (this.randomize) {
				joinMessage = I18n.get("book.uhc.team.randomize");
			}

			if (this.isHovered) {
				if (this.randomize)
					guiGraphics.drawCenteredString(font, joinMessage, textX, textY - 44, 0xFFFF5555);
				else
					guiGraphics.drawCenteredString(font, joinMessage, textX, textY, color);
			}


			poseStack.popPose();
		}
	}
}