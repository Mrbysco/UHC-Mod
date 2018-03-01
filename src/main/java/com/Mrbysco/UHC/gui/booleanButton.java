package com.Mrbysco.UHC.gui;

import com.Mrbysco.UHC.Reference;
import com.Mrbysco.UHC.gui.enums.BooleanEnum;
import com.Mrbysco.UHC.init.UHCSaveData;

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
	private final BooleanEnum enumValue;
	
	public booleanButton(int buttonId, int x, int y, FontRenderer renderIn, BooleanEnum enumIn, World worldIn)
	{
		super(buttonId, x, y, 15, 13, "");
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
			mc.getTextureManager().bindTexture(BOOK_TEXTURE);
			int textureX = 47;
			int textureY = 192;
			if (flag)
				textureY += 14;
			
	        boolean value = getBooleanFromEnum(enumValue);
			if (value)
				textureX += 15;
	        
			drawTexturedModalRect(x, y,  textureX, textureY, 15, 13);
		}
	}
	
	public boolean getBooleanFromEnum(BooleanEnum value)
	{
		final UHCSaveData data = UHCSaveData.getForWorld(world);
		boolean correct = false;
		
		if(value == value.COLLISION)
		{
			correct = data.isTeamCollision();
		}
		if(value == value.DAMAGE)
		{
			correct = data.isFriendlyFire();
		}
		if(value == value.HEALTHTAB)
		{
			correct = data.isHealthInTab();
		}
		if(value == value.HEALTHSIDE)
		{
			correct = data.isHealthOnSide();
		}
		if(value == value.HEALTHNAME)
		{
			correct = data.isHealthUnderName();
		}
		if(value == value.SHRINK)
		{
			correct = data.isShrinkEnabled();
		}
		
		return correct;
	}
}