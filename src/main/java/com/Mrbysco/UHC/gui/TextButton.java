package com.Mrbysco.UHC.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextButton extends GuiButton
{	
	private final Minecraft mc;
	private int color = 0xFFFFAA00;
	private int hoverColor = 0xFFFF5555;
	private boolean shadow = true;

	public TextButton(int buttonId, int x, int y, String text, Minecraft mc)
	{
		super(buttonId, x, y, mc.fontRenderer.getStringWidth(text), mc.fontRenderer.FONT_HEIGHT, text);
		this.mc = mc;
	}
	
	public void setText(String text){
		this.displayString = text;
		this.width = mc.fontRenderer.getStringWidth(text);
	}
	
	public void shadowEnabled(boolean shadow){
		this.shadow = shadow;
	}
	
	public void setColors(int normal, int hover){
		this.hoverColor = hover;
		this.color = normal;
	}
	
	/**
	 * Draws this button to the screen.
	 */
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (this.visible)
		{
			FontRenderer renderer = mc.fontRenderer;
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int colorInt = this.color;
			if (packedFGColour != 0)
            {
				colorInt = packedFGColour;
            }
            else if (!this.enabled)
            {
            	colorInt = 0xAAAAAA;
            }
            else if (hovered)
            {
            	colorInt = this.hoverColor;
            }
			
			renderer.drawString(this.displayString, this.x, this.y, colorInt, this.shadow);
		}
	}
}