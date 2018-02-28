package com.Mrbysco.UHC.gui;

import com.Mrbysco.UHC.gui.enums.BooleanEnum;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class enumButton extends GuiButton
{	
	private final FontRenderer render;
	private final World world;
	private final BooleanEnum enumValue;
	
	public enumButton(int buttonId, int x, int y, FontRenderer renderIn, BooleanEnum enumIn, World worldIn)
	{
		super(buttonId, x, y, 40, 8, "");
		this.render = renderIn;
		this.world = worldIn;
		this.enumValue = enumIn;
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
			
	        
	        render.drawString("", this.x, this.y, 0xFFFFAA00);
		}
	}
}