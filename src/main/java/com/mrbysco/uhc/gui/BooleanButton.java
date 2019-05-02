package com.mrbysco.uhc.gui;

import com.mrbysco.uhc.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BooleanButton extends GuiButton
{	
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");

	private final FontRenderer render;
	protected Boolean booleanValue;
	
	public BooleanButton(int buttonId, int x, int y, FontRenderer renderIn, Boolean booleanIn)
	{
		super(buttonId, x, y, 15, 13, "");
		this.render = renderIn;
		this.booleanValue = booleanIn;
	}
	
	public void setBoolean(Boolean value){
		this.booleanValue = value;
	}
	
	public Boolean getBoolean() {
		return this.booleanValue;
	}
	
	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (this.visible)
		{
			boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(BOOK_TEXTURE);
			int textureX = 47;
			int textureY = 192;
			if (flag)
				textureY += 14;
			
			if (booleanValue)
				textureX += 16;
	        
			drawTexturedModalRect(x, y,  textureX, textureY, 15, 13);
		}
	}
}