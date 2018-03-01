package com.Mrbysco.UHC.gui;

import com.Mrbysco.UHC.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class booleanButton extends GuiButton
{	
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");

	private final FontRenderer render;
	private final World world;
	private Boolean booleanValue;
	
	public booleanButton(int buttonId, int x, int y, FontRenderer renderIn, Boolean booleanIn, World worldIn)
	{
		super(buttonId, x, y, 15, 13, "");
		this.render = renderIn;
		this.world = worldIn;
		this.booleanValue = booleanIn;
	}
	
	public void setBoolean(Boolean value){
		this.booleanValue = value;
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
				textureX += 15;
	        
			drawTexturedModalRect(x, y,  textureX, textureY, 15, 13);
		}
	}
}