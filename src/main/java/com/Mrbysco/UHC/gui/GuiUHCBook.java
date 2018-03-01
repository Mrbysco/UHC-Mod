package com.Mrbysco.UHC.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.Mrbysco.UHC.Reference;
import com.Mrbysco.UHC.init.UHCSaveData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

@SideOnly(Side.CLIENT)
public class GuiUHCBook extends GuiScreen{
	/** Book texture */
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");
    /** The player editing the UHC Settings */
    private final EntityPlayer editingPlayer;
    /** The world */
    private final World world;
    /** The current scoreboard */
    private final Scoreboard scoreboard;
    /** The image size */
    private final int bookImageWidth = 192;
    private final int bookImageHeight = 192;
    /** The total amount of pages */
    private int bookTotalPages = 4;
    private int currPage;
    
    /** Buttons */
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;
    
    private GuiButtonColor buttonDarkRed, buttonGold, buttonDarkGreen, buttonDarkAqua, buttonDarkBlue, buttonDarkPurple, buttonDarkGray;
    
    private GuiButtonColor buttonRed, buttonYellow, buttonGreen, buttonAqua, buttonBlue, buttonLightPurple, buttonGray;
    
    private GuiButtonColor buttonBlack,  buttonSolo, buttonRandomize;
    
    private GuiTextField randSizeField;
    private GuiTextField maxTeamSizeField;
    
    private GuiTextField borderSizeField;
    private GuiTextField borderCenterXField;
    private GuiTextField borderCenterZField;
    private GuiTextField difficultyField;
    
    private GuiTextField shrinkTimerField;
    private GuiTextField shrinkSizeField;
    private GuiTextField shrinkOvertimeField;
    private GuiTextField timeLockTimerField;
    private GuiTextField minMarkTimerField;
    
    private ResetButton resetRandButton;
    private ResetButton resetTeamSizeButton;
    
    private booleanButton collisionButton;
    private booleanButton damageButton;
    
    private booleanButton healthTabButton;
    private booleanButton healthSideButton;
    private booleanButton healthNameButton;
    
    private ResetButton resetBorderSizeButton;
    private ResetButton resetBorderCenterXButton;
    private ResetButton resetBorderCenterZButton;
    private LocationButton centerCurrentXButton;
    private LocationButton centerCurrentZButton;
    private ResetButton resetShrinkTimerButton;
    private ResetButton resetShrinkSizeButton;
    private ResetButton resetShrinkOverTimeButton;
    private TextButton ShrinkModeButton;
    
    private booleanButton shrinkButton;
    private booleanButton timeLockButton;
    private TextButton timeModeButton;
    private ResetButton resetTimeLockTimerButton;
    private booleanButton minuteMarkButton;
    private ResetButton resetMinuteMarkTimerButton;
    
    private booleanButton netherButton;
    
    /** UHC save data */
    private UHCSaveData saveData;
    /** Player data */
	private NBTTagCompound playerData;

	public GuiUHCBook(EntityPlayer player, World world) {
        this.editingPlayer = player;
        this.world = world;
        this.scoreboard = world.getScoreboard();
        this.saveData = UHCSaveData.getForWorld(player.world);
        this.playerData = player.getEntityData();
	}
	
	@Override
	public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        this.buttonDone = this.addButton(new GuiButton(0, this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done")));

        int i = (this.width - 192) / 2;
        int j = 2;
        this.buttonNextPage = (GuiUHCBook.NextPageButton)this.addButton(new GuiUHCBook.NextPageButton(1, i + 120, 156, true));
        this.buttonPreviousPage = (GuiUHCBook.NextPageButton)this.addButton(new GuiUHCBook.NextPageButton(2, i + 38, 156, false));
        
    	this.buttonDarkRed = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(3, i + 43, j + 40, 10, 10, i + 92, j + 27, 0xFFAA0000, I18n.format("color.darkred.name")));
    	this.buttonGold = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(4, i + 43 + 15, j + 40, 10, 10, i + 92, j + 27,  0xFFFFAA00, I18n.format("color.gold.name")));
    	this.buttonDarkGreen = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(5, i + 43 + 30, j + 40, 10, 10, i + 92, j + 27,  0xFF00AA00, I18n.format("color.darkgreen.name")));
    	this.buttonDarkAqua = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(6, i + 43 + 45, j + 40, 10, 10, i + 92, j + 27,  0xFF00AAAA, I18n.format("color.darkaqua.name")));
    	this.buttonDarkBlue = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(7, i + 43 + 60, j + 40, 10, 10, i + 92, j + 27,  0xFF0000AA, I18n.format("color.darkblue.name")));
    	this.buttonDarkPurple = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(8, i + 43 + 75, j + 40, 10, 10, i + 92, j + 27,  0xFFAA00AA, I18n.format("color.darkpurple.name")));
    	this.buttonDarkGray = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(9, i + 43 + 90, j + 40, 10, 10, i + 92, j + 27,  0xFF555555, I18n.format("color.darkgray.name")));
    	
    	this.buttonRed = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(10, i + 43, j + 55, 10, 10, i + 92, j + 27,  0xFFFF5555, I18n.format("color.red.name")));
    	this.buttonYellow = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(11, i + 43 + 15, j + 55, 10, 10, i + 92, j + 27,  0xFFFFFF55, I18n.format("color.yellow.name")));
    	this.buttonGreen = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(12, i + 43 + 30, j + 55, 10, 10, i + 92, j + 27,  0xFF55FF55, I18n.format("color.green.name")));
    	this.buttonAqua = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(13, i + 43 + 45, j + 55, 10, 10, i + 92, j + 27,  0xFF55FFFF, I18n.format("color.aqua.name")));
    	this.buttonBlue = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(14, i + 43 + 60, j + 55, 10, 10, i + 92, j + 27,  0xFF5555FF, I18n.format("color.blue.name")));
    	this.buttonLightPurple = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(15, i + 43 + 75, j + 55, 10, 10, i + 92, j + 27, 0xFFFF55FF, I18n.format("color.lightpurple.name")));
    	this.buttonGray = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(16, i + 43 + 90, j + 55, 10, 10, i + 92, j + 27,  0xFFAAAAAA, I18n.format("color.gray.name")));
    	
    	this.buttonBlack = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(17, i + 43, j + 70, 10, 10, i + 92, j + 28,  0xFF000000, I18n.format("color.black.name")));
    	this.buttonSolo = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(18, i + 43 + 90, j + 70, 10, 10, i + 92, j + 28,  0xFFFFFFFF, I18n.format("color.white.name"), true));
    	this.buttonRandomize = (GuiUHCBook.GuiButtonColor)this.addButton(new GuiUHCBook.GuiButtonColor(19, i + 43 + 15, j + 70, 70, 10, i + 92, j + 71,  0xFFAAAAAA, I18n.format("color.random.name"), false, true));
    	
    	this.resetRandButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(20, i + 43 + 94, j + 85, fontRenderer));
    	this.resetTeamSizeButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(21, i + 43 + 94, j + 99, fontRenderer));
    	
    	this.collisionButton = (booleanButton)this.addButton(new booleanButton(22, i + 43 + 74, j + 113, fontRenderer, saveData.isTeamCollision(), world));
    	this.damageButton = (booleanButton)this.addButton(new booleanButton(23, i + 43 + 74, j + 127, fontRenderer, saveData.isFriendlyFire(), world));

    	this.healthTabButton = (booleanButton)this.addButton(new booleanButton(24, i + 43 + 80, j + 109, fontRenderer, saveData.isHealthInTab(), world));
    	this.healthSideButton = (booleanButton)this.addButton(new booleanButton(25, i + 43 + 80, j + 122, fontRenderer, saveData.isHealthOnSide(), world));
    	this.healthNameButton = (booleanButton)this.addButton(new booleanButton(26, i + 43 + 80, j + 135, fontRenderer, saveData.isHealthUnderName(), world));
    	
    	this.resetBorderSizeButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(27, i + 43 + 68, j + 36, fontRenderer));
    	this.resetBorderCenterXButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(28, i + 43 + 92, j + 60, fontRenderer));
    	this.resetBorderCenterZButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(29, i + 43 + 92, j + 74, fontRenderer));
    	this.centerCurrentXButton = (GuiUHCBook.LocationButton)this.addButton(new GuiUHCBook.LocationButton(30, i + 43 + 76, j + 60, fontRenderer));
    	this.centerCurrentZButton = (GuiUHCBook.LocationButton)this.addButton(new GuiUHCBook.LocationButton(31, i + 43 + 76, j + 74, fontRenderer));
    	
    	this.shrinkButton = (booleanButton)this.addButton(new booleanButton(32, i + 43 + 70, j + 90, fontRenderer, saveData.isShrinkEnabled(), world));  
    	this.resetShrinkTimerButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(33, i + 43 + 92, j + 104, fontRenderer));
    	this.resetShrinkSizeButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(34, i + 43 + 92, j + 116, fontRenderer));
    	this.resetShrinkOverTimeButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(35, i + 43 + 92, j + 128, fontRenderer));   	
    	this.ShrinkModeButton = (TextButton)this.addButton(new TextButton(36, i + 43 + 31, j + 140, saveData.getShrinkMode(), mc));
    	
    	this.timeLockButton = (booleanButton)this.addButton(new booleanButton(37, i + 43 + 56, j + 24, fontRenderer, saveData.isTimeLock(), world));  
    	this.timeModeButton = (TextButton)this.addButton(new TextButton(38, i + 43 + 32, j + 52, saveData.getTimeMode(), mc));  
    	this.resetTimeLockTimerButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(39, i + 43 + 80, j + 38, fontRenderer));
    	this.resetMinuteMarkTimerButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(40, i + 43 + 80, j + 77, fontRenderer));
    	this.minuteMarkButton = (booleanButton)this.addButton(new booleanButton(41, i + 43 + 56, j + 64, fontRenderer, saveData.isMinuteMark(), world));  
    	
    	this.netherButton = (booleanButton)this.addButton(new booleanButton(42, i + 43 + 80, j + 94, fontRenderer, saveData.isNetherEnabled(), world));  

    	randSizeField = new GuiTextField(0, fontRenderer, i + 43 + 80, j + 89, 20, 8);
    	setupField(randSizeField, 2, 0xFFFFAA00, String.valueOf(saveData.getRandomTeamSize()));

		maxTeamSizeField = new GuiTextField(1, fontRenderer, i + 43 + 80, j + 101, 20, 8);
		setupField(maxTeamSizeField, 2, 0xFFFFAA00, String.valueOf(saveData.getMaxTeamSize()));

		borderSizeField = new GuiTextField(2, fontRenderer, i + 43 + 40, j + 40, 32, 8);
		setupField(borderSizeField, 4, 0xFFFFAA00, String.valueOf(saveData.getBorderSize()));
		
		borderCenterXField = new GuiTextField(3, fontRenderer, i + 55, j + 64, 52, 8);
		setupField(borderCenterXField, 5, 0xFFFFAA00, String.valueOf(saveData.getBorderCenterX()));
		
		borderCenterZField = new GuiTextField(4, fontRenderer, i + 55, j + 76, 79, 8);
		setupField(borderCenterZField, 5, 0xFFFFAA00, String.valueOf(saveData.getBorderCenterZ()));
		
		difficultyField = new GuiTextField(5, fontRenderer, i + 43 + 52, j + 144, 14, 8);
		setupField(difficultyField, 1, 0xFFFFAA00, String.valueOf(saveData.getDifficulty()));
	    
		shrinkTimerField = new GuiTextField(6, fontRenderer, i + 43 + 52, j + 107, 32, 8);
		setupField(shrinkTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkTimer()));
		
		shrinkSizeField = new GuiTextField(7, fontRenderer, i + 43 + 28, j + 118, 32, 8);
		setupField(shrinkSizeField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkSize()));
		
		shrinkOvertimeField = new GuiTextField(8, fontRenderer, i + 43 + 32, j + 129, 32, 8);
		setupField(shrinkOvertimeField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkOvertime()));
		
		timeLockTimerField = new GuiTextField(9, fontRenderer, i + 43 + 52, j + 41, 32, 8);
		setupField(timeLockTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getTimeLockTimer()));
		
		minMarkTimerField = new GuiTextField(10, fontRenderer, i + 43 + 38, j + 80, 32, 8);
		setupField(minMarkTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getMinuteMarkTime()));
		
        this.updateButtons();
	}
	
	public void setupField(GuiTextField field, int maxLength, int color, String text)
	{
		field.setFocused(false);
		field.setCanLoseFocus(true);
		field.setMaxStringLength(maxLength);
		field.setText(text);
		field.setEnableBackgroundDrawing(false);
		field.setTextColor(color);
	}
	
	@Override
	public void updateScreen() {
		if(this.currPage == 0)
		{
			if(randSizeField != null)
				randSizeField.updateCursorCounter();
			if(maxTeamSizeField != null)
				maxTeamSizeField.updateCursorCounter();
			if(difficultyField != null)
				difficultyField.updateCursorCounter();
		}
		
		if(this.currPage == 1)
		{
			if(borderSizeField != null)
				borderSizeField.updateCursorCounter();
			if(borderCenterXField != null)
				borderCenterXField.updateCursorCounter();
			if(borderCenterZField != null)
				borderCenterZField.updateCursorCounter();
			if(shrinkTimerField != null)
				shrinkTimerField.updateCursorCounter();
			if(shrinkSizeField != null)
				shrinkSizeField.updateCursorCounter();
			if(shrinkOvertimeField != null)
				shrinkOvertimeField.updateCursorCounter();
		}
		
		if(this.currPage == 2)
		{
			if(timeLockTimerField != null)
				timeLockTimerField.updateCursorCounter();
			if(minMarkTimerField != null)
				minMarkTimerField.updateCursorCounter();
		}
		super.updateScreen();
	}
	
	/**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
	@Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    private void updateButtons()
    {
        this.buttonNextPage.visible = (this.currPage < this.bookTotalPages - 1);
        this.buttonPreviousPage.visible = this.currPage > 0;
        this.buttonDone.visible = true;
        
        this.buttonDarkRed.visible = this.currPage == 0;
    	this.buttonGold.visible = this.currPage == 0;
    	this.buttonDarkGreen.visible = this.currPage == 0;
    	this.buttonDarkAqua.visible = this.currPage == 0;
    	this.buttonDarkBlue.visible = this.currPage == 0;
    	this.buttonDarkPurple.visible = this.currPage == 0;
    	this.buttonDarkGray.visible = this.currPage == 0;
    	
    	this.buttonRed.visible = this.currPage == 0;
    	this.buttonYellow.visible = this.currPage == 0;
    	this.buttonGreen.visible = this.currPage == 0;
    	this.buttonAqua.visible = this.currPage == 0;
    	this.buttonBlue.visible = this.currPage == 0;
    	this.buttonLightPurple.visible = this.currPage == 0;
    	this.buttonGray.visible = this.currPage == 0;
    	
    	this.buttonBlack.visible = this.currPage == 0;
    	this.buttonSolo.visible = this.currPage == 0;
    	this.buttonRandomize.visible = this.currPage == 0;
    	
    	this.resetRandButton.visible = this.currPage == 0;
    	this.resetTeamSizeButton.visible = this.currPage == 0;
    	
    	this.collisionButton.visible = this.currPage == 0;
    	this.damageButton.visible = this.currPage == 0;
    	
    	this.healthTabButton.visible = this.currPage == 2;
    	this.healthSideButton.visible = this.currPage == 2;
    	this.healthNameButton.visible = this.currPage == 2;
    	    	
    	this.randSizeField.setVisible(this.currPage == 0);
    	this.maxTeamSizeField.setVisible(this.currPage == 0);
    	this.difficultyField.setVisible(this.currPage == 0);
    	
    	this.borderSizeField.setVisible(this.currPage == 1);
    	this.borderCenterXField.setVisible(this.currPage == 1);
    	this.borderCenterZField.setVisible(this.currPage == 1);
    	
    	this.borderSizeField.setEnabled(this.currPage == 1);
    	this.borderCenterXField.setEnabled(this.currPage == 1);
    	this.borderCenterZField.setEnabled(this.currPage == 1);
    	
    	this.shrinkTimerField.setVisible(this.currPage == 1);
    	this.shrinkTimerField.setEnabled(this.currPage == 1);
    	this.shrinkSizeField.setVisible(this.currPage == 1);
    	this.shrinkSizeField.setEnabled(this.currPage == 1);
    	this.shrinkOvertimeField.setVisible(this.currPage == 1);
    	this.shrinkOvertimeField.setEnabled(this.currPage == 1);
    	
    	this.resetBorderSizeButton.visible = this.currPage == 1;
    	this.resetBorderCenterXButton.visible = this.currPage == 1;
    	this.resetBorderCenterZButton.visible = this.currPage == 1;
    	this.centerCurrentXButton.visible = this.currPage == 1;
    	this.centerCurrentZButton.visible = this.currPage == 1;
    	
    	this.shrinkButton.visible = this.currPage == 1;
    	this.resetShrinkTimerButton.visible = this.currPage == 1;
    	this.resetShrinkSizeButton.visible = this.currPage == 1;
    	this.resetShrinkOverTimeButton.visible = this.currPage == 1;
    	this.ShrinkModeButton.visible = this.currPage == 1;
    	
    	this.timeLockButton.visible = this.currPage == 2;
    	this.timeModeButton.visible = this.currPage == 2;
    	this.resetTimeLockTimerButton.visible = this.currPage == 2;
    	this.timeLockTimerField.setVisible(this.currPage == 2);
    	this.timeLockTimerField.setEnabled(this.currPage == 2);
    	this.minMarkTimerField.setVisible(this.currPage == 2);
    	this.minMarkTimerField.setEnabled(this.currPage == 2);
    	this.minuteMarkButton.visible = this.currPage == 2;
    	this.resetMinuteMarkTimerButton.visible = this.currPage == 2;
    	this.netherButton.visible = this.currPage == 2;
    }
    
    public void drawField(GuiTextField field)
    {
    	if(field != null)
    	{
    		field.drawTextBox();
    	}
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BOOK_TEXTURE);
        int i = (this.width - 192) / 2;
        int j = 2;
        this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);

        String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);

        int j1 = this.fontRenderer.getStringWidth(s4);
        this.fontRenderer.drawString(s4, i - j1 + 192 - 44, 18, 0);
        
		String minuteMessageString = I18n.format("book.uhc.option.minutes");
		String locationString = I18n.format("book.uhc.option.location");
		String resetString = I18n.format("book.uhc.option.reset");

		super.drawScreen(mouseX, mouseY, partialTicks);
	    if(this.currPage == 0)
	    {
	        String teamSelect = I18n.format("book.uhc.team.select");
	        
	        String randSizeString = I18n.format("book.uhc.option.randsize");
	        this.fontRenderer.drawString(randSizeString, i + 43, j + 89, 0xFF555555);
	        drawField(randSizeField);

	        
	        String maxTeamSizeString = I18n.format("book.uhc.option.maxteams");
	        this.fontRenderer.drawString(maxTeamSizeString, i + 43, j + 101, 0xFF555555);
	        drawField(maxTeamSizeField);

			boolean flag = mouseX >= maxTeamSizeField.x && mouseY >= maxTeamSizeField.y && mouseX < maxTeamSizeField.x + maxTeamSizeField.width && mouseY < maxTeamSizeField.y + maxTeamSizeField.height;
			String infinityString = I18n.format("book.uhc.option.infinite");
			
			if(isColorNotHovered() && !flag)
	        {
    	        this.fontRenderer.drawString(teamSelect, i + 65, j + 28, 0xFF555555);
        	}
			
			if(flag && maxTeamSizeField.isFocused() == false)
			{
		        this.drawCenteredString(fontRenderer, infinityString, i + 91, j + 28, 0xFFFF5555);
			}
			
			String teamCollisionString = I18n.format("book.uhc.option.collision");
	        this.fontRenderer.drawString(teamCollisionString, i + 43, j + 118, 0xFF555555);

			String teamDamageString = I18n.format("book.uhc.option.damage");
	        this.fontRenderer.drawString(teamDamageString, i + 43, j + 131, 0xFF555555);

			String difficultyString = I18n.format("book.uhc.option.difficulty");
	        this.fontRenderer.drawString(difficultyString, i + 43, j + 144, 0xFF555555);
	        drawField(difficultyField);
	    }
	    
	    if(this.currPage == 1)
	    {
	    	String borderSizeString = I18n.format("book.uhc.option.bordersize");
	        this.fontRenderer.drawString(borderSizeString, i + 48, j + 28, 0xFF555555);
	        drawField(borderSizeField);
	        
	        String centerString = I18n.format("book.uhc.option.bordercenter");
	        this.fontRenderer.drawString(centerString, i + 42, j + 53, 0xFF555555);
	        
	        String centerxString = I18n.format("book.uhc.option.bordercenterx");
	        this.fontRenderer.drawString(centerxString, i + 42, j + 64, 0xFF555555);
	        drawField(borderCenterXField);
	        
	        String centerZString = I18n.format("book.uhc.option.bordercenterz");
	        this.fontRenderer.drawString(centerZString, i + 42, j + 76, 0xFF555555);
	        drawField(borderCenterZField);
	        
	        String ShrinkString = I18n.format("book.uhc.option.shrinkenabled");
	        this.fontRenderer.drawString(ShrinkString, i + 38, j + 94, 0xFF555555);
	        
	        String ShrinkTimerString = I18n.format("book.uhc.option.shrinktimer");
	        this.fontRenderer.drawString(ShrinkTimerString, i + 44, j + 107, 0xFF555555);
	        drawField(shrinkTimerField);
	        
	        String ShrinkSizeString = I18n.format("book.uhc.option.shrinksize");
	        this.fontRenderer.drawString(ShrinkSizeString, i + 44, j + 118, 0xFF555555);
	        drawField(shrinkSizeField);
	        
	        String ShrinkOvertimeString = I18n.format("book.uhc.option.shrinkovertime");
	        this.fontRenderer.drawString(ShrinkOvertimeString, i + 44, j + 129, 0xFF555555);
	        drawField(shrinkOvertimeField);
	        
	        String ShrinkModeString = I18n.format("book.uhc.option.shrinkmode");
	        this.fontRenderer.drawString(ShrinkModeString, i + 44, j + 140, 0xFF555555);
	        drawField(shrinkOvertimeField);
	        
			boolean flag = mouseX >= shrinkTimerField.x && mouseY >= shrinkTimerField.y && mouseX < shrinkTimerField.x + shrinkTimerField.width && mouseY < shrinkTimerField.y + shrinkTimerField.height;
			boolean flag1 = mouseX >= shrinkOvertimeField.x && mouseY >= shrinkOvertimeField.y && mouseX < shrinkOvertimeField.x + shrinkOvertimeField.width && mouseY < shrinkOvertimeField.y + shrinkOvertimeField.height;
			if((flag && shrinkTimerField.isFocused() == false) || (flag1 && shrinkOvertimeField.isFocused() == false))
			{
		        this.drawCenteredString(fontRenderer, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
			}
			
			String ShrinkModeShrink = I18n.format("book.uhc.option.shrinkmodeshrink");
			String ShrinkModeShrink2 = I18n.format("book.uhc.option.shrinkmodeshrink2");
			String ShrinkModeArena = I18n.format("book.uhc.option.shrinkmodearena");
			String ShrinkModeArena2 = I18n.format("book.uhc.option.shrinkmodearena2");
			String ShrinkModeControl = I18n.format("book.uhc.option.shrinkmodecontrol");
			String ShrinkModeControl2 = I18n.format("book.uhc.option.shrinkmodecontrol2");
			if(ShrinkModeButton.isMouseOver() == true)
			{
				String ShrinkMode = saveData.getShrinkMode();
				if(ShrinkMode.equals("Shrink"))
				{
					this.drawCenteredString(fontRenderer, ShrinkModeShrink, mouseX, mouseY + 5, 0xFFFFAA00);
					this.drawCenteredString(fontRenderer, ShrinkModeShrink2, mouseX, mouseY + 15, 0xFFFFAA00);
				}
				if(ShrinkMode.equals("Arena"))
				{
					this.drawCenteredString(fontRenderer, ShrinkModeArena, mouseX, mouseY + 5, 0xFFFFAA00);
					this.drawCenteredString(fontRenderer, ShrinkModeArena2, mouseX, mouseY + 15, 0xFFFFAA00);
				}
				if(ShrinkMode.equals("Control"))
				{
					this.drawCenteredString(fontRenderer, ShrinkModeControl, mouseX, mouseY + 5, 0xFFFFAA00);
					this.drawCenteredString(fontRenderer, ShrinkModeControl2, mouseX, mouseY + 15, 0xFFFFAA00);
				}
			}
	    }
	    
	    if(this.currPage == 2)
	    {
	    	String TimeLockString = I18n.format("book.uhc.option.timelock");
	    	this.fontRenderer.drawString(TimeLockString, i + 38, j + 28, 0xFF555555);
	    	drawField(timeLockTimerField);
	    	
	    	String TimeLockTimerString = I18n.format("book.uhc.option.timelocktimer");
	    	this.fontRenderer.drawString(TimeLockTimerString, i + 44, j + 41, 0xFF555555);
	    	
	    	String TimeLockModeString = I18n.format("book.uhc.option.timelockmode");
	    	this.fontRenderer.drawString(TimeLockModeString, i + 44, j + 53, 0xFF555555);
			
			String timeModeDayText = I18n.format("book.uhc.option.timemodeday"); 
			int dayInt = fontRenderer.getStringWidth(timeModeDayText);
			String timeModeNightText = I18n.format("book.uhc.option.timemodenight"); 
			int nightInt = fontRenderer.getStringWidth(timeModeNightText);

			if(timeModeButton.isMouseOver() == true)
			{
				String TimeMode = saveData.getTimeMode();
				if(TimeMode.equals("Day"))
			        this.drawCenteredString(fontRenderer, timeModeDayText, mouseX, mouseY + 5, 0xFFFFAA00);
				if(TimeMode.equals("Night"))
			        this.drawCenteredString(fontRenderer, timeModeNightText, mouseX, mouseY + 5, 0xFFFFAA00);
			}
			
			String minMarkString = I18n.format("book.uhc.option.minmark");
	    	this.fontRenderer.drawString(minMarkString, i + 38, j + 68, 0xFF555555);

			String minMarkTimerString = I18n.format("book.uhc.option.minmarktime");
	    	this.fontRenderer.drawString(minMarkTimerString, i + 44, j + 80, 0xFF555555);
			drawField(minMarkTimerField);
			
			boolean flag = mouseX >= timeLockTimerField.x && mouseY >= timeLockTimerField.y && mouseX < timeLockTimerField.x + timeLockTimerField.width && mouseY < timeLockTimerField.y + timeLockTimerField.height;
			boolean flag1 = mouseX >= minMarkTimerField.x && mouseY >= minMarkTimerField.y && mouseX < minMarkTimerField.x + minMarkTimerField.width && mouseY < minMarkTimerField.y + minMarkTimerField.height;
			if((flag && timeLockTimerField.isFocused() == false) || (flag1 && minMarkTimerField.isFocused() == false))
			{
		        this.drawCenteredString(fontRenderer, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
			}
			
			
			String netherTravelString = I18n.format("book.uhc.option.nether");
	        this.fontRenderer.drawString(netherTravelString, i + 38, j + 98, 0xFF555555);

			String healthInTabString = I18n.format("book.uhc.option.healthtab");
	        this.fontRenderer.drawString(healthInTabString, i + 38, j + 114, 0xFF555555);
	        
			String healthOnSideString = I18n.format("book.uhc.option.healthside");
	        this.fontRenderer.drawString(healthOnSideString, i + 38, j + 126, 0xFF555555);

			String healthUnderNameString = I18n.format("book.uhc.option.healthname");
	        this.fontRenderer.drawString(healthUnderNameString, i + 38, j + 138, 0xFF555555);
	    }
	    
	    for(GuiButton button : buttonList)
		{
			if(button instanceof LocationButton && button.visible)
			{
				if(button.isMouseOver())
				{
					this.drawCenteredString(fontRenderer, locationString, mouseX, mouseY - 14, 0xFFFF5555);
				}
			}
			if(button instanceof ResetButton && button.visible)
			{
				if(button.isMouseOver())
				{
					this.drawCenteredString(fontRenderer, resetString, mouseX, mouseY - 14, 0xFFFF5555);
				}
			}
		}
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            else if (button.id == 1)
            {
                if (this.currPage < this.bookTotalPages - 1)
                {
                    ++this.currPage;
                }
            }
            else if (button.id == 2)
            {
                if (this.currPage > 0)
                {
                    --this.currPage;
                }
            }
            else if (button.id == 3)
            {
				AddToTeam(TextFormatting.DARK_RED.getFriendlyName(), TextFormatting.DARK_RED, I18n.format("color.darkred.name"));
            }
            else if (button.id == 4)
            {
				AddToTeam(TextFormatting.GOLD.getFriendlyName(), TextFormatting.GOLD, I18n.format("color.gold.name"));
            }
            else if (button.id == 5)
            {
				AddToTeam(TextFormatting.DARK_GREEN.getFriendlyName(), TextFormatting.DARK_GREEN, I18n.format("color.darkgreen.name"));
            }
            else if (button.id == 6)
            {
				AddToTeam(TextFormatting.DARK_AQUA.getFriendlyName(), TextFormatting.DARK_AQUA, I18n.format("color.darkaqua.name"));
            }
            else if (button.id == 7)
            {
				AddToTeam(TextFormatting.DARK_BLUE.getFriendlyName(), TextFormatting.DARK_BLUE, I18n.format("color.darkblue.name"));
            }
            else if (button.id == 8)
            {
				AddToTeam(TextFormatting.DARK_PURPLE.getFriendlyName(), TextFormatting.DARK_PURPLE, I18n.format("color.darkpurple.name"));
            }
            else if (button.id == 9)
            {
				AddToTeam(TextFormatting.DARK_GRAY.getFriendlyName(), TextFormatting.DARK_GRAY, I18n.format("color.darkgray.name"));
            }
            else if (button.id == 10)
            {
				AddToTeam(TextFormatting.RED.getFriendlyName(), TextFormatting.RED, I18n.format("color.red.name"));
            }
            else if (button.id == 11)
            {
				AddToTeam(TextFormatting.YELLOW.getFriendlyName(), TextFormatting.YELLOW, I18n.format("color.yellow.name"));
            }
            else if (button.id == 12)
            {
				AddToTeam(TextFormatting.GREEN.getFriendlyName(), TextFormatting.GREEN, I18n.format("color.green.name"));
            }
            else if (button.id == 13)
            {
				AddToTeam(TextFormatting.AQUA.getFriendlyName(), TextFormatting.AQUA, I18n.format("color.aqua.name"));
            }
            else if (button.id == 14)
            {
				AddToTeam(TextFormatting.BLUE.getFriendlyName(), TextFormatting.BLUE, I18n.format("color.blue.name"));
            }
            else if (button.id == 15)
            {
				AddToTeam(TextFormatting.LIGHT_PURPLE.getFriendlyName(), TextFormatting.LIGHT_PURPLE, I18n.format("color.lightpurple.name"));
            }
            else if (button.id == 16)
            {
				AddToTeam(TextFormatting.GRAY.getFriendlyName(), TextFormatting.GRAY, I18n.format("color.gray.name"));
            }
            else if (button.id == 17)
            {
				AddToTeam("spectator", TextFormatting.BLACK, I18n.format("color.black.name"));
            }
            else if (button.id == 18)
            {
            	AddToTeam("solo", TextFormatting.WHITE, I18n.format("color.white.name"));
            }
            else if (button.id == 19 && playerData.getBoolean("canEditUHC") == true)
            {
            	randomTeam();
            }
            
            else if(button.id == 20 && playerData.getBoolean("canEditUHC") == true)
            {
            	saveData.setRandomTeamSize(6);
            	saveData.markDirty();
            	randSizeField.setText(String.valueOf(saveData.getRandomTeamSize()));
            }
            else if(button.id == 21 && playerData.getBoolean("canEditUHC") == true)
            {
            	saveData.setMaxTeamSize(-1);
            	saveData.markDirty();
            	maxTeamSizeField.setText(String.valueOf(saveData.getMaxTeamSize()));
            }
            else if(button.id == 22 && playerData.getBoolean("canEditUHC") == true)
            {
            	boolean flag = saveData.isTeamCollision();
            	collisionButton.setBoolean(!flag);
            	saveData.setTeamCollision(!flag);
            	saveData.markDirty();
            }
            else if(button.id == 23 && playerData.getBoolean("canEditUHC") == true)
            {
            	boolean flag = saveData.isFriendlyFire();
            	damageButton.setBoolean(!flag);
            	saveData.setFriendlyFire(!flag);
            	saveData.markDirty();
            }
            else if(button.id == 24 && playerData.getBoolean("canEditUHC") == true)
            {
            	if(saveData.isHealthInTab() == false)
            	{
            		healthTabButton.setBoolean(true);
            		healthSideButton.setBoolean(false);
            		healthNameButton.setBoolean(false);
            		saveData.setHealthInTab(true);
            		saveData.setHealthOnSide(false);
            		saveData.setHealthUnderName(false);
            		saveData.markDirty();
            	}
            }
            else if(button.id == 25 && playerData.getBoolean("canEditUHC") == true)
            {
            	if(saveData.isHealthOnSide() == false)
            	{
            		healthTabButton.setBoolean(false);
            		healthSideButton.setBoolean(true);
            		healthNameButton.setBoolean(false);
	        		saveData.setHealthInTab(false);
	        		saveData.setHealthOnSide(true);
	        		saveData.setHealthUnderName(false);
	        		saveData.markDirty();
            	}
            }
            else if(button.id == 26 && playerData.getBoolean("canEditUHC") == true)
            {
            	if(saveData.isHealthUnderName() == false)
            	{
            		healthTabButton.setBoolean(false);
            		healthSideButton.setBoolean(false);
            		healthNameButton.setBoolean(true);
	        		saveData.setHealthInTab(false);
	        		saveData.setHealthOnSide(false);
	        		saveData.setHealthUnderName(true);
	        		saveData.markDirty();
            	}
        		
            }
            
            else if(button.id == 27 && playerData.getBoolean("canEditUHC") == true)
            {
            	saveData.setBorderSize(2048);
            	saveData.markDirty();
            	borderSizeField.setText(String.valueOf(saveData.getBorderSize()));
            }
            else if(button.id == 28 && playerData.getBoolean("canEditUHC") == true)
            {
            	double originalX = saveData.getOriginalBorderCenterX();
            	saveData.setBorderCenterX(originalX);
            	saveData.markDirty();
            	borderCenterXField.setText(String.valueOf(saveData.getBorderCenterX()));
            }
            else if(button.id == 29 && playerData.getBoolean("canEditUHC") == true)
            {
            	double originalZ = saveData.getOriginalBorderCenterZ();
            	saveData.setBorderCenterZ(originalZ);
            	saveData.markDirty();
            	borderCenterZField.setText(String.valueOf(saveData.getBorderCenterZ()));
            }
            
            else if(button.id == 30 && playerData.getBoolean("canEditUHC") == true)
            {
            	double playerX = editingPlayer.posX;
            	saveData.setBorderCenterX(playerX);
            	saveData.markDirty();
            	borderCenterXField.setText(String.valueOf(saveData.getBorderCenterX()));
            }
            else if(button.id == 31 && playerData.getBoolean("canEditUHC") == true)
            {
            	double playerZ = editingPlayer.posZ;
            	saveData.setBorderCenterZ(playerZ);
            	saveData.markDirty();
            	borderCenterZField.setText(String.valueOf(saveData.getBorderCenterZ()));
            }
            else if(button.id == 32 && playerData.getBoolean("canEditUHC") == true)
            {
            	boolean flag = saveData.isShrinkEnabled();
            	shrinkButton.setBoolean(!flag);
            	saveData.setShrinkEnabled(!flag);
            	saveData.markDirty();
            }
            
            else if(button.id == 33 && playerData.getBoolean("canEditUHC") == true)
            {
            	saveData.setShrinkTimer(60);
            	saveData.markDirty();
            	shrinkTimerField.setText(String.valueOf(saveData.getShrinkTimer()));
            }
            else if(button.id == 34 && playerData.getBoolean("canEditUHC") == true)
            {
            	saveData.setShrinkSize(256);
            	saveData.markDirty();
            	shrinkSizeField.setText(String.valueOf(saveData.getShrinkSize()));
            }
            else if(button.id == 35 && playerData.getBoolean("canEditUHC") == true)
            {
            	saveData.setShrinkOvertime(60);
            	saveData.markDirty();
            	shrinkOvertimeField.setText(String.valueOf(saveData.getShrinkOvertime()));
            }
            else if(button.id == 36 && playerData.getBoolean("canEditUHC") == true)
            {
            	String mode = saveData.getShrinkMode();
            	if(mode.equals("Shrink"))
            	{
            		saveData.setShrinkMode("Arena");
            		saveData.markDirty();
            		ShrinkModeButton.setText(saveData.getShrinkMode());
            	}
            	if(mode.equals("Arena"))
            	{
            		saveData.setShrinkMode("Control");
            		saveData.markDirty();
            		ShrinkModeButton.setText(saveData.getShrinkMode());
            	}
            	if(mode.equals("Control"))
            	{
            		saveData.setShrinkMode("Shrink");
            		saveData.markDirty();
            		ShrinkModeButton.setText(saveData.getShrinkMode());
            	}
            }
            else if(button.id == 37 && playerData.getBoolean("canEditUHC") == true)
            {
            	boolean flag = saveData.isTimeLock();
            	timeLockButton.setBoolean(!flag);
            	saveData.setTimeLock(!flag);
            	saveData.markDirty();
            }
            else if(button.id == 38 && playerData.getBoolean("canEditUHC") == true)
            {
            	String mode = saveData.getTimeMode();
            	if(mode.equals("Day"))
            	{
            		saveData.setTimeMode("Night");
            		saveData.markDirty();
            		timeModeButton.setText(saveData.getTimeMode());
            	}
            	if(mode.equals("Night"))
            	{
            		saveData.setTimeMode("Day");
            		saveData.markDirty();
            		timeModeButton.setText(saveData.getTimeMode());
            	}
            }
            else if(button.id == 39 && playerData.getBoolean("canEditUHC") == true)
            {
            	saveData.setTimeLockTimer(60);
            	saveData.markDirty();
            	timeLockTimerField.setText(String.valueOf(saveData.getTimeLockTimer()));
            }
            else if(button.id == 40 && playerData.getBoolean("canEditUHC") == true)
            {
            	saveData.setMinuteMarkTime(30);
            	saveData.markDirty();
            	minMarkTimerField.setText(String.valueOf(saveData.getMinuteMarkTime()));
            }
            else if(button.id == 41 && playerData.getBoolean("canEditUHC") == true)
            {
            	boolean flag = saveData.isMinuteMark();
            	minuteMarkButton.setBoolean(!flag);
            	saveData.setMinuteMark(!flag);
            	saveData.markDirty();
            }
            else if(button.id == 42 && playerData.getBoolean("canEditUHC") == true)
            {
            	boolean flag = saveData.isNetherEnabled();
            	netherButton.setBoolean(!flag);
            	saveData.setNetherEnabled(!flag);
            	saveData.markDirty();
            }
            
            this.updateButtons();
        }
    }

    @Override
    public boolean doesGuiPauseGame(){
        return false;
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	int middleWidth = (this.width - 192) / 2;
        int middleHeigth = 2;
        
        if (mouseButton == 0)
        {
        	if(this.currPage == 0)
        	{
        		if(randSizeField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
            		randSizeField.setText("");
            	if(maxTeamSizeField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
            		maxTeamSizeField.setText("");
            	if(difficultyField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
            		difficultyField.setText("");
            	
            	
            	if(randSizeField.isFocused() == false)
            		randSizeField.setText(String.valueOf(saveData.getRandomTeamSize()));
            	if(maxTeamSizeField.isFocused() == false)
            		maxTeamSizeField.setText(String.valueOf(saveData.getMaxTeamSize()));
            	if(difficultyField.isFocused() == false)
            		difficultyField.setText(String.valueOf(saveData.getDifficulty()));
        	}
        	
        	if(this.currPage == 1)
        	{
        		if(borderSizeField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
            		borderSizeField.setText("");
            	if(borderCenterXField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
            		borderCenterXField.setText("");
            	if(borderCenterZField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
            		borderCenterZField.setText("");
            	if(shrinkTimerField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
            		shrinkTimerField.setText("");
            	if(shrinkSizeField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
            		shrinkSizeField.setText("");
            	if(shrinkOvertimeField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
            		shrinkOvertimeField.setText("");
            	
            	if(borderSizeField.isFocused() == false)
            		borderSizeField.setText(String.valueOf(saveData.getBorderSize()));
            	if(borderCenterXField.isFocused() == false)
            		borderCenterXField.setText(String.valueOf(saveData.getBorderCenterX()));
            	if(borderCenterZField.isFocused() == false)
            		borderCenterZField.setText(String.valueOf(saveData.getBorderCenterZ()));
            	if(shrinkTimerField.isFocused() == false)
            		shrinkTimerField.setText(String.valueOf(saveData.getShrinkTimer()));
            	if(shrinkSizeField.isFocused() == false)
            		shrinkSizeField.setText(String.valueOf(saveData.getShrinkSize()));
            	if(shrinkOvertimeField.isFocused() == false)
            		shrinkOvertimeField.setText(String.valueOf(saveData.getShrinkOvertime()));
        	}
        	
        	if(this.currPage == 2)
        	{
        		if(timeLockTimerField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
        			timeLockTimerField.setText("");
        		if(minMarkTimerField.mouseClicked(mouseX, mouseY, mouseButton) && playerData.getBoolean("canEditUHC") == true)
        			minMarkTimerField.setText("");
        		
        		if(timeLockTimerField.isFocused() == false)
        			timeLockTimerField.setText(String.valueOf(saveData.getTimeLockTimer()));
        		if(minMarkTimerField.isFocused() == false)
        			minMarkTimerField.setText(String.valueOf(saveData.getMinuteMarkTime()));
        	}
        }
        
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	super.keyTyped(typedChar, keyCode);
    	
    	if(this.currPage == 0)
    	{
    		if(randSizeField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
        		randSizeField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(maxTeamSizeField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
        		maxTeamSizeField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(difficultyField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			difficultyField.textboxKeyTyped(typedChar, keyCode);
    		}
    		
    		if (keyCode == Keyboard.KEY_RETURN)
    		{
    			if(randSizeField.isFocused())
    			{
    				String randText = randSizeField.getText();
    				
    				if(randText.isEmpty())
    					randSizeField.setText(String.valueOf(saveData.getRandomTeamSize()));
    				else
    				{
    					saveData.setRandomTeamSize(Integer.parseInt(randText));
    					saveData.markDirty();
    				}
    				
    				randSizeField.setFocused(false);
    			}
    			
    			if(maxTeamSizeField.isFocused())
    			{
    				String teamSize = maxTeamSizeField.getText();
    				
    				if(teamSize.isEmpty())
    					maxTeamSizeField.setText(String.valueOf(saveData.getMaxTeamSize()));
    				else
    				{
    					saveData.setMaxTeamSize(Integer.parseInt(teamSize));
    					saveData.markDirty();
    				}
    				
    				maxTeamSizeField.setFocused(false);
    			}
    			
    			if(difficultyField.isFocused())
    			{
    				String difficultyText = difficultyField.getText();
    				
    				if(difficultyText.isEmpty() || Integer.parseInt(difficultyText) > 3)
    					difficultyField.setText(String.valueOf(saveData.getDifficulty()));
    				else
    				{
    					saveData.setDifficulty(Integer.parseInt(difficultyText));
    					saveData.markDirty();
    				}
    				
    				difficultyField.setFocused(false);
    			}
    		}
    	}
    	
    	if(this.currPage == 1)
    	{
    		if(borderSizeField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			borderSizeField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(borderCenterXField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK 
    				|| keyCode == Keyboard.KEY_PERIOD || keyCode == Keyboard.KEY_MINUS))
    		{
    			borderCenterXField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(borderCenterZField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK 
    				|| keyCode == Keyboard.KEY_PERIOD || keyCode == Keyboard.KEY_MINUS))
    		{
    			borderCenterZField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(shrinkTimerField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			shrinkTimerField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(shrinkSizeField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			shrinkSizeField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(shrinkOvertimeField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			shrinkOvertimeField.textboxKeyTyped(typedChar, keyCode);
    		}
    		
    		if (keyCode == Keyboard.KEY_RETURN)
    		{
    			/* Border Size Field */
    			if(borderSizeField.isFocused())
    			{
    				String borderSize = borderSizeField.getText();
    				
    				if(borderSize.isEmpty())
    					borderSizeField.setText(String.valueOf(saveData.getBorderSize()));
    				else
    				{
    					saveData.setBorderSize(Integer.parseInt(borderSize));
    					saveData.markDirty();
    				}
    				
    				borderSizeField.setFocused(false);
    			}
    			/* Border Center X Field */
    			if(borderCenterXField.isFocused())
    			{
    				String borderX = borderCenterXField.getText();
    				
    				if(borderX.isEmpty())
    					borderCenterXField.setText(String.valueOf(saveData.getBorderCenterX()));
    				else
    				{
    					saveData.setBorderCenterX(Integer.parseInt(borderX));
    					saveData.markDirty();
    				}
    			
    				borderCenterXField.setFocused(false);
    			}
    			/* Border Center Z Field */
    			if(borderCenterZField.isFocused())
    			{
    				String borderZ = borderCenterZField.getText();
    				
    				if(borderZ.isEmpty())
    					borderCenterZField.setText(String.valueOf(saveData.getBorderCenterZ()));
    				else
    				{
    					saveData.setBorderCenterZ(Integer.parseInt(borderZ));
    					saveData.markDirty();
    				}
    			
    				borderCenterZField.setFocused(false);
    			}
    			/* Shrink Timer Field */
    			if(shrinkTimerField.isFocused())
    			{
    				String shrinkTimer = shrinkTimerField.getText();
    				
    				if(shrinkTimer.isEmpty())
    					shrinkTimerField.setText(String.valueOf(saveData.getShrinkTimer()));
    				else
    				{
    					saveData.setShrinkTimer(Integer.parseInt(shrinkTimer));
    					saveData.markDirty();
    				}
    				
    				shrinkTimerField.setFocused(false);
    			}
    			/* Shrink Size Field */
    			if(shrinkSizeField.isFocused())
    			{
    				String shrinkSize = shrinkSizeField.getText();
    				
    				if(shrinkSize.isEmpty())
    					shrinkSizeField.setText(String.valueOf(saveData.getShrinkSize()));
    				else
    				{
    					saveData.setShrinkSize(Integer.parseInt(shrinkSize));
    					saveData.markDirty();
    				}
    				
    				shrinkSizeField.setFocused(false);
    			}
    			/* Shrink Over Time Field */
    			if(shrinkOvertimeField.isFocused())
    			{
    				String ShrinkOverTime = shrinkOvertimeField.getText();
    				
    				if(ShrinkOverTime.isEmpty())
    					shrinkOvertimeField.setText(String.valueOf(saveData.getShrinkOvertime()));
    				else
    				{
    					saveData.setShrinkOvertime(Integer.parseInt(ShrinkOverTime));
    					saveData.markDirty();
    				}
    				
    				shrinkOvertimeField.setFocused(false);
    			}
    		}
    	}
    	if (this.currPage == 2)
    	{
    		if(timeLockTimerField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			timeLockTimerField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(minMarkTimerField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			minMarkTimerField.textboxKeyTyped(typedChar, keyCode);
    		}
    		
    		if (keyCode == Keyboard.KEY_RETURN)
    		{
    			/* Shrink Timer Field */
    			if(timeLockTimerField.isFocused())
    			{
    				String timeLockTimer = timeLockTimerField.getText();
    				
    				if(timeLockTimer.isEmpty())
    					timeLockTimerField.setText(String.valueOf(saveData.getTimeLockTimer()));
    				else
    				{
    					saveData.setTimeLockTimer(Integer.parseInt(timeLockTimer));
    					saveData.markDirty();
    				}
    				
    				timeLockTimerField.setFocused(false);
    			}
    			/* Minute Mark Timer Field */
    			if(minMarkTimerField.isFocused())
    			{
    				String minuteMarkTimer = minMarkTimerField.getText();
    				
    				if(minuteMarkTimer.isEmpty())
    					minMarkTimerField.setText(String.valueOf(saveData.getMinuteMarkTime()));
    				else
    				{
    					saveData.setMinuteMarkTime(Integer.parseInt(minuteMarkTimer));
    					saveData.markDirty();
    				}
    				
    				minMarkTimerField.setFocused(false);
    			}
    		}
    	}
    }
    
    public boolean charNumeric(char typedChar)
    {
    	return (typedChar >= '0' && typedChar <= '9');
    }
    
    public void AddToTeam(String teamString, TextFormatting format, String name)
    {
    	ScorePlayerTeam team = scoreboard.getPlayersTeam(editingPlayer.getName());
    	if(team != scoreboard.getTeam(teamString))
    	{
    		for(String players : editingPlayer.world.getMinecraftServer().getOnlinePlayerNames())
    		{
    			EntityPlayer onlinePlayer = world.getPlayerEntityByName(editingPlayer.getName());
    			
    			if(format != format.WHITE)
    				onlinePlayer.sendMessage(new TextComponentTranslation("book.uhc.team.selected", new Object[] {editingPlayer.getName(), format + name}));
            	else
            		onlinePlayer.sendMessage(new TextComponentTranslation("book.uhc.team.solo", new Object[] {editingPlayer.getName(), format + name}));
    		}
        	scoreboard.addPlayerToTeam(editingPlayer.getName(), teamString);
    	}
    }
    
    public void randomTeam() {
    	String name = "Dark Red";
    	TextFormatting format = TextFormatting.DARK_RED;
    	
    	Random rand = new Random();
    	int randInt = rand.nextInt(15);
    	switch(randInt)
    	{
    		default:
    			break;
    		case 0:
    			name = I18n.format("color.darkred.name");
    			format = TextFormatting.DARK_RED;
    			break;
    		case 1:
    			name = I18n.format("color.gold.name");
    			format = TextFormatting.GOLD;
    			break;
    		case 2:
    			name = I18n.format("color.darkgreen.name");
    			format = TextFormatting.DARK_GREEN;
    			break;
    		case 3:
    			name = I18n.format("color.darkaqua.name");
    			format = TextFormatting.DARK_AQUA;
    			break;
    		case 4:
    			name = I18n.format("color.darkblue.name");
    			format = TextFormatting.DARK_BLUE;
    			break;
    		case 5:
    			name = I18n.format("color.darkpurple.name");
    			format = TextFormatting.DARK_PURPLE;
    			break;
    		case 6:
    			name = I18n.format("color.darkgray.name");
    			format = TextFormatting.DARK_GRAY;
    			break;
    		case 7:
    			name = I18n.format("color.red.name");
    			format = TextFormatting.RED;
    			break;
    		case 8:
    			name = I18n.format("color.yellow.name");
    			format = TextFormatting.YELLOW;
    			break;
    		case 9:
    			name = I18n.format("color.green.name");
    			format = TextFormatting.GREEN;
    			break;
    		case 10:
    			name = I18n.format("color.aqua.name");
    			format = TextFormatting.AQUA;
    			break;
    		case 12:
    			name = I18n.format("color.blue.name");
    			format = TextFormatting.BLUE;
    			break;
    		case 13:
    			name = I18n.format("color.lightpurple.name");
    			format = TextFormatting.LIGHT_PURPLE;
    			break;
    		case 14:
    			name = I18n.format("color.gray.name");
    			format = TextFormatting.GRAY;
    			break;
    	}

    	AddToTeam(format.getFriendlyName(), format, name);
    }
    
    public boolean isColorNotHovered()
    {
    	return !buttonDarkRed.isMouseOver() && !buttonGold.isMouseOver() && !buttonDarkGreen.isMouseOver() && !buttonDarkAqua.isMouseOver() && 
    			!buttonDarkBlue.isMouseOver() && !buttonDarkPurple.isMouseOver() && !buttonDarkGray.isMouseOver() && !buttonRed.isMouseOver() &&
    			 !buttonYellow.isMouseOver() && !buttonGreen.isMouseOver() && !buttonAqua.isMouseOver() && !buttonBlue.isMouseOver() &&
    			 !buttonLightPurple.isMouseOver() && !buttonGray.isMouseOver() && !buttonBlack.isMouseOver() && !buttonSolo.isMouseOver() &&
    			 !buttonRandomize.isMouseOver();
    }
    
	@SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean isForward;

        public NextPageButton(int buttonId, int x, int y, boolean isForwardIn)
        {
            super(buttonId, x, y, 23, 13, "");
            this.isForward = isForwardIn;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
        {
            if (this.visible)
            {
            	this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        		mc.getTextureManager().bindTexture(BOOK_TEXTURE);
        		int textureX = 0;
        		int textureY = 192;
        		if (this.hovered)
        			textureX += 23;
        		if (!isForward)
        			textureY += 13;
        		drawTexturedModalRect(x, y,  textureX, textureY, 23, 13);
            }
        }
    }
	
	@SideOnly(Side.CLIENT)
	static class ResetButton extends GuiButton
	{	
		private final FontRenderer render;
		
		public ResetButton(int buttonId, int x, int y, FontRenderer renderIn)
		{
			super(buttonId, x, y, 16, 13, "");
			this.render = renderIn;
		}
		
		/**
		 * Draws this button to the screen.
		 */
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
		{
			if (this.visible)
			{
				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(BOOK_TEXTURE);
				int textureX = 0;
				int textureY = 218;
				if (this.hovered)
					textureX += 16;
				drawTexturedModalRect(x, y,  textureX, textureY, 16, 13);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	static class LocationButton extends GuiButton
	{	
		private final FontRenderer render;
		
		public LocationButton(int buttonId, int x, int y, FontRenderer renderIn)
		{
			super(buttonId, x, y, 14, 13, "");
			this.render = renderIn;
		}
		
		/**
		 * Draws this button to the screen.
		 */
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
		{
			if (this.visible)
			{
				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(BOOK_TEXTURE);
				int textureX = 0;
				int textureY = 232;
				if (this.hovered)
					textureX += 15;
				
				drawTexturedModalRect(x, y,  textureX, textureY, 15, 13);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public class GuiButtonColor extends GuiButton {

		private final boolean solo;
		private final boolean randomize;
		public int color;
		public String name;
		
		public int textX;
		public int textY;
		
		public GuiButtonColor(int buttonId, int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, String nameIn) {
			super(buttonId, x, y, widthIn, heightIn, null);
			this.textX = textXIn;
			this.textY = textYIn;
			this.color = colorIn;
			this.name = nameIn;
			this.solo = false;
			this.randomize = false;
		}
		
		public GuiButtonColor(int buttonId, int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, String nameIn, boolean soloIn) {
			super(buttonId, x, y, widthIn, heightIn, null);
			this.textX = textXIn;
			this.textY = textYIn;
			this.color = colorIn;
			this.name = nameIn;
			this.solo = soloIn;
			this.randomize = false;
		}
		
		public GuiButtonColor(int buttonId, int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, String nameIn, boolean soloIn, boolean randomizeIn) {
			super(buttonId, x, y, widthIn, heightIn, null);
			this.textX = textXIn;
			this.textY = textYIn;
			this.color = colorIn;
			this.name = nameIn;
			this.solo = false;
			this.randomize = randomizeIn;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible)
	        {
	            FontRenderer fontrenderer = mc.fontRenderer;
				boolean prev = this.hovered;
				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				
				GlStateManager.pushMatrix();
				GlStateManager.color(1, 1, 1, 1);
				
				String randomizeMessage = I18n.format("book.uhc.team.randomizer", name);
		        int r1 = fontrenderer.getStringWidth(randomizeMessage);

		        
				if(!this.solo && !this.randomize)
				{
					drawRect(this.x, this.y, this.x + width, this.y + height , color);
				}
				else
				{
					drawRect(this.x, this.y, this.x + width, this.y + height , 0xFF555555);
					drawRect(this.x + 1, this.y + 1, this.x + width - 1, this.y + height - 1 , color);
					
					if(this.randomize)
						this.drawCenteredString(fontrenderer, randomizeMessage, textX, textY, 0xFFFFFF55);
				}
				
				String joinMessage = I18n.format("book.uhc.team.hover", name);
				if(this.randomize) {
					joinMessage = I18n.format("book.uhc.team.randomize");
				}
		        int j1 = fontrenderer.getStringWidth(joinMessage);

				if (this.hovered)
				{
					if(this.randomize)
						this.drawCenteredString(fontrenderer, joinMessage, textX, textY - 44, 0xFFFF5555);
					else
						this.drawCenteredString(fontrenderer, joinMessage, textX, textY, color);
				}
			        
				
				GlStateManager.popMatrix();
	        }
		}
	}
}