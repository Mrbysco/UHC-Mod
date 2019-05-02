package com.Mrbysco.UHC.gui;

import com.Mrbysco.UHC.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LockButton extends GuiButton
{	
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");

	private final FontRenderer render;
	protected Boolean lockValue;
	
	public LockButton(int buttonId, int x, int y, FontRenderer renderIn, Boolean booleanIn)
	{
		super(buttonId, x, y, 15, 13, "");
		this.render = renderIn;
		this.lockValue = booleanIn;
	}
	
	public void setBoolean(Boolean value){
		this.lockValue = value;
	}
	
	public Boolean getBoolean() {
		return this.lockValue;
	}
	
	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (this.visible)
		{
			boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			boolean prev = this.hovered;
			this.hovered = flag;
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(BOOK_TEXTURE);
			int textureX = 79;
			int textureY = 192;
			if (flag)
				textureY += 16;
			
			if (!lockValue)
				textureX += 16;
	        
			drawTexturedModalRect(x, y,  textureX, textureY, 16, 13);
		}
	}
}