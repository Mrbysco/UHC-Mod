package com.Mrbysco.UHC.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.Mrbysco.UHC.Reference;
import com.Mrbysco.UHC.init.UHCSaveData;
import com.Mrbysco.UHC.packets.ModPackethandler;
import com.Mrbysco.UHC.packets.UHCPacketTeam;
import com.Mrbysco.UHC.packets.UHCPacketTeamRandomizer;
import com.Mrbysco.UHC.packets.UHCPage1Packet;
import com.Mrbysco.UHC.packets.UHCPage2Packet;
import com.Mrbysco.UHC.packets.UHCPage3Packet;
import com.Mrbysco.UHC.packets.UHCPage4Packet;
import com.Mrbysco.UHC.packets.UHCPage5Packet;
import com.Mrbysco.UHC.packets.UHCStartPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiUHCBook extends GuiScreen{
	/** Book texture */
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");
    /** The player editing the UHC Settings */
    private final EntityPlayer editingPlayer;
    /** The image size */
    private final int bookImageWidth = 192;
    private final int bookImageHeight = 192;
    /** The total amount of pages */
    private int bookTotalPages = 6;
    private int currPage;
    
    protected ArrayList<GuiTextField> textBoxList = new ArrayList<GuiTextField>();
    
    /** Buttons */
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;
    
    private GuiButtonColor buttonDarkRed, buttonGold, buttonDarkGreen, buttonDarkAqua, buttonDarkBlue, buttonDarkPurple, buttonDarkGray;
    
    private GuiButtonColor buttonRed, buttonYellow, buttonGreen, buttonAqua, buttonBlue, buttonLightPurple, buttonGray;
    
    private GuiButtonColor buttonBlack, buttonSolo, buttonRandomize;
    
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
    private GuiTextField nameTimerField;
    private GuiTextField glowTimerField;
    
    private GuiTextField maxHealthField;
    private GuiTextField spreadDistanceField;
    private GuiTextField spreadMaxRangeField;
    
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
    private booleanButton nameButton;
    private ResetButton resetNameTimerButton;
    private booleanButton glowButton;
    private ResetButton resetGlowTimerButton;
    
    private booleanButton autoCookButton;
    private booleanButton itemConvertButton;
    
    private booleanButton netherButton;
    private booleanButton regenPotionsButton;
    private booleanButton level2PotionsButton;
    private booleanButton notchApplesButton;
    
    private booleanButton weatherButton;
    private booleanButton mobGriefingButton;
    
    private booleanButton customHealthButton;
    private booleanButton randomSpawnButton;
    private booleanButton spreadRespectTeamButton;
    
    private StartButton UHCStartButton;

    /** UHC save data */
    public static UHCSaveData saveData;
    
	private boolean uhcStarting;
	private boolean uhcOnGoing;
	
	private boolean friendlyFire;
	private boolean teamCollision;
	private boolean healthInTab;
	private boolean healthOnSide;
	private boolean healthUnderName;
	
	private boolean doDaylightCycle;
	private boolean autoCook;
	private boolean itemConversion;
	private boolean applyCustomHealth;
	private int maxHealth;
	
	private int randomTeamSize;
	private int maxTeamSize;
	
	private int difficulty;
	
	private int borderSize;
	private double borderCenterX;
	private double borderCenterZ;
	private double originalBorderCenterX;
	private double originalBorderCenterZ;
	
	private boolean shrinkEnabled;
	private int shrinkTimer;
	private int shrinkSize;
	private int shrinkOvertime;
	private String shrinkMode;

	private boolean timeLock;
	private int timeLockTimer;
	private String timeMode;
	
	private boolean minuteMark;
	private int minuteMarkTime;
	private boolean timedNames;
	private int nameTimer;
	private boolean timedGlow;
	private int glowTime;
	
	private boolean netherEnabled;
	private boolean regenPotions;
	private boolean level2Potions;
	private boolean notchApples;
	
	private boolean weatherEnabled;
	private boolean mobGriefing;
	
	private boolean randomSpawns;
	private int spreadDistance;
	private int spreadMaxRange;
	private boolean spreadRespectTeam;
	
	private NBTTagCompound playerData;

	
	public GuiUHCBook(EntityPlayer player) {
		UHCSaveData data = UHCSaveData.getForWorld(player.world);
        this.editingPlayer = player;
        this.playerData = player.getEntityData();
	}
	
	@Override
	public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        
        initValues();
        
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
    	
    	this.collisionButton = (booleanButton)this.addButton(new booleanButton(22, i + 43 + 74, j + 113, fontRenderer, saveData.isTeamCollision()));
    	this.damageButton = (booleanButton)this.addButton(new booleanButton(23, i + 43 + 74, j + 127, fontRenderer, saveData.isFriendlyFire()));

    	this.healthTabButton = (booleanButton)this.addButton(new booleanButton(24, i + 43 + 90, j + 109, fontRenderer, saveData.isHealthInTab()));
    	this.healthSideButton = (booleanButton)this.addButton(new booleanButton(25, i + 43 + 90, j + 122, fontRenderer, saveData.isHealthOnSide()));
    	this.healthNameButton = (booleanButton)this.addButton(new booleanButton(26, i + 43 + 90, j + 135, fontRenderer, saveData.isHealthUnderName()));
    	
    	this.resetBorderSizeButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(27, i + 43 + 68, j + 36, fontRenderer));
    	this.resetBorderCenterXButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(28, i + 43 + 92, j + 60, fontRenderer));
    	this.resetBorderCenterZButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(29, i + 43 + 92, j + 74, fontRenderer));
    	this.centerCurrentXButton = (GuiUHCBook.LocationButton)this.addButton(new GuiUHCBook.LocationButton(30, i + 43 + 76, j + 60, fontRenderer));
    	this.centerCurrentZButton = (GuiUHCBook.LocationButton)this.addButton(new GuiUHCBook.LocationButton(31, i + 43 + 76, j + 74, fontRenderer));
    	
    	this.shrinkButton = (booleanButton)this.addButton(new booleanButton(32, i + 43 + 70, j + 90, fontRenderer, saveData.isShrinkEnabled()));  
    	this.resetShrinkTimerButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(33, i + 43 + 92, j + 104, fontRenderer));
    	this.resetShrinkSizeButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(34, i + 43 + 92, j + 116, fontRenderer));
    	this.resetShrinkOverTimeButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(35, i + 43 + 92, j + 128, fontRenderer));   	
    	this.ShrinkModeButton = (TextButton)this.addButton(new TextButton(36, i + 43 + 31, j + 140, saveData.getShrinkMode(), mc));
    	
    	this.timeLockButton = (booleanButton)this.addButton(new booleanButton(37, i + 43 + 60, j + 25, fontRenderer, saveData.isTimeLock()));  
    	this.timeModeButton = (TextButton)this.addButton(new TextButton(38, i + 43 + 32, j + 52, saveData.getTimeMode(), mc));  
    	this.resetTimeLockTimerButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(39, i + 43 + 80, j + 38, fontRenderer));
    	this.resetMinuteMarkTimerButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(40, i + 43 + 80, j + 77, fontRenderer));
    	this.minuteMarkButton = (booleanButton)this.addButton(new booleanButton(41, i + 43 + 60, j + 64, fontRenderer, saveData.isMinuteMark()));  
    	
    	this.netherButton = (booleanButton)this.addButton(new booleanButton(42, i + 43 + 90, j + 94, fontRenderer, saveData.isNetherEnabled()));  
    	this.regenPotionsButton = (booleanButton)this.addButton(new booleanButton(43, i + 43 + 90, j + 23, fontRenderer, saveData.isRegenPotions()));  
    	this.level2PotionsButton = (booleanButton)this.addButton(new booleanButton(44, i + 43 + 90, j + 35, fontRenderer, saveData.isLevel2Potions()));  
    	this.notchApplesButton = (booleanButton)this.addButton(new booleanButton(45, i + 43 + 90, j + 48, fontRenderer, saveData.isNotchApples())); 
    	
    	this.nameButton = (booleanButton)this.addButton(new booleanButton(46, i + 43 + 60, j + 91, fontRenderer, saveData.isTimedNames()));  
    	this.resetNameTimerButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(47, i + 43 + 80, j + 103, fontRenderer));
    	this.glowButton = (booleanButton)this.addButton(new booleanButton(48, i + 43 + 60, j + 119, fontRenderer, saveData.isTimedGlow()));  
    	this.resetGlowTimerButton = (GuiUHCBook.ResetButton)this.addButton(new GuiUHCBook.ResetButton(49, i + 43 + 80, j + 131, fontRenderer));
    	this.autoCookButton = (booleanButton)this.addButton(new booleanButton(50, i + 43 + 90, j + 63, fontRenderer, saveData.isAutoCook()));  
    	this.itemConvertButton = (booleanButton)this.addButton(new booleanButton(51, i + 43 + 90, j + 77, fontRenderer, saveData.isItemConversion()));  
    	
    	this.weatherButton = (booleanButton)this.addButton(new booleanButton(52, i + 43 + 80, j + 23, fontRenderer, saveData.isWeatherEnabled()));  
    	this.mobGriefingButton = (booleanButton)this.addButton(new booleanButton(53, i + 43 + 80, j + 35, fontRenderer, saveData.isMobGriefing()));  
    	
    	this.customHealthButton = (booleanButton)this.addButton(new booleanButton(55, i + 43 + 80, j + 54, fontRenderer, saveData.isMobGriefing()));  
    	this.randomSpawnButton = (booleanButton)this.addButton(new booleanButton(56, i + 43 + 80, j + 81, fontRenderer, saveData.isRandomSpawns()));  
    	this.spreadRespectTeamButton = (booleanButton)this.addButton(new booleanButton(57, i + 43 + 80, j + 118, fontRenderer, saveData.isSpreadRespectTeam()));  
    	
    	this.UHCStartButton = (GuiUHCBook.StartButton)this.addButton(new GuiUHCBook.StartButton(54, i + 43 + 7, j + 132, fontRenderer));  

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
		
		nameTimerField = new GuiTextField(11, fontRenderer, i + 43 + 38, j + 106, 32, 8);
		setupField(nameTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getNameTimer()));
		
		glowTimerField = new GuiTextField(12, fontRenderer, i + 43 + 38, j + 133, 32, 8);
		setupField(glowTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getGlowTime()));
		
		maxHealthField = new GuiTextField(13, fontRenderer, i + 43 + 60, j + 69, 32, 8);
		setupField(maxHealthField, 4, 0xFFFFAA00, String.valueOf(saveData.getMaxHealth()));
		
		spreadDistanceField = new GuiTextField(13, fontRenderer, i + 43 + 60, j + 98, 32, 8);
		setupField(spreadDistanceField, 4, 0xFFFFAA00, String.valueOf(saveData.getSpreadDistance()));
		
		spreadMaxRangeField = new GuiTextField(13, fontRenderer, i + 43 + 60, j + 110, 32, 8);
		setupField(spreadMaxRangeField, 4, 0xFFFFAA00, String.valueOf(saveData.getSpreadMaxRange()));
		
        this.updateButtons();
	}
	
	public void initValues() {
		if (this.saveData == null)
            this.saveData = new UHCSaveData();
		
		this.uhcStarting = saveData.isUhcStarting();
        this.uhcOnGoing = saveData.isUhcOnGoing();
        	
        this.friendlyFire = saveData.isFriendlyFire();
        this.teamCollision = saveData.isTeamCollision();
        this.healthInTab = saveData.isHealthInTab();
        this.healthOnSide = saveData.isHealthOnSide();
        this.healthUnderName = saveData.isHealthUnderName();
        	
        this.doDaylightCycle = saveData.DoDaylightCycle();
        this.autoCook = saveData.isAutoCook();
        this.itemConversion = saveData.isItemConversion();
        this.applyCustomHealth = saveData.isApplyCustomHealth();
        this.maxHealth = saveData.getMaxHealth();
        	
        this.randomTeamSize = saveData.getRandomTeamSize();
        this.maxTeamSize = saveData.getMaxTeamSize();
        	
        this.difficulty = saveData.getDifficulty();
        	
        this.borderSize = saveData.getBorderSize();
        this.borderCenterX = saveData.getBorderCenterX();
        this.borderCenterZ = saveData.getBorderCenterZ();
        this.originalBorderCenterX = saveData.getOriginalBorderCenterX();
        this.originalBorderCenterZ = saveData.getOriginalBorderCenterZ();
        	
        this.shrinkEnabled = saveData.isShrinkEnabled();
        this.shrinkTimer = saveData.getShrinkTimer();
        this.shrinkSize = saveData.getShrinkSize();
        this.shrinkOvertime = saveData.getShrinkOvertime();
        this.shrinkMode = saveData.getShrinkMode();

        this.timeLock = saveData.isTimeLock();
        this.timeLockTimer = saveData.getTimeLockTimer();
        this.timeMode = saveData.getTimeMode();
        	
        this.minuteMark = saveData.isMinuteMark();
        this.minuteMarkTime = saveData.getMinuteMarkTime();
        this.timedNames = saveData.isTimedNames();
        this.nameTimer = saveData.getNameTimer();
        this.timedGlow = saveData.isTimedGlow();
        this.glowTime = saveData.getGlowTime();
        	
        this.netherEnabled = saveData.isNetherEnabled();
        this.regenPotions = saveData.isRegenPotions();
        this.level2Potions = saveData.isLevel2Potions();
        this.notchApples = saveData.isNotchApples();
        
        this.weatherEnabled = saveData.isWeatherEnabled();
        this.mobGriefing = saveData.isMobGriefing();
        
        this.randomSpawns = saveData.isRandomSpawns();
		this.spreadDistance = saveData.getSpreadDistance();
		this.spreadMaxRange = saveData.getSpreadMaxRange();
		this.spreadRespectTeam = saveData.isSpreadRespectTeam();
	}
	
	public void setupField(GuiTextField field, int maxLength, int color, String text)
	{
		field.setFocused(false);
		field.setCanLoseFocus(true);
		field.setMaxStringLength(maxLength);
		field.setText(text);
		field.setEnableBackgroundDrawing(false);
		field.setTextColor(color);
		textBoxList.add(field);
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
			if(nameTimerField != null)
				nameTimerField.updateCursorCounter();
			if(glowTimerField != null)
				glowTimerField.updateCursorCounter();
		}
		
		if(this.currPage == 4)
		{
			if(maxHealthField != null)
				maxHealthField.updateCursorCounter();
			if(spreadDistanceField != null)
				spreadDistanceField.updateCursorCounter();
			if(spreadMaxRangeField != null)
				spreadMaxRangeField.updateCursorCounter();
		}

		syncData();

		super.updateScreen();
	}
	
	public void syncData() {
		if(this.currPage == 0)
		{
			if(randSizeField.getText() != String.valueOf(saveData.getRandomTeamSize()) && randSizeField.isFocused() == false)
				randSizeField.setText(String.valueOf(saveData.getRandomTeamSize()));
			
			if(maxTeamSizeField.getText() != String.valueOf(saveData.getMaxTeamSize()) && maxTeamSizeField.isFocused() == false)
				maxTeamSizeField.setText(String.valueOf(saveData.getMaxTeamSize()));
			
			if(difficultyField.getText() != String.valueOf(saveData.getDifficulty()) && difficultyField.isFocused() == false)
				difficultyField.setText(String.valueOf(saveData.getDifficulty()));
			
			if(collisionButton.getBoolean() != saveData.isTeamCollision())
				collisionButton.setBoolean(saveData.isTeamCollision());
			
			if(damageButton.getBoolean() != saveData.isFriendlyFire())
				damageButton.setBoolean(saveData.isFriendlyFire());
		}
		
		if(this.currPage == 1)
		{
			if(borderSizeField.getText() != String.valueOf(saveData.getBorderSize()) && borderSizeField.isFocused() == false)
				borderSizeField.setText(String.valueOf(saveData.getBorderSize()));
			
			if(borderCenterXField.getText() != String.valueOf(saveData.getBorderCenterX()) && borderCenterXField.isFocused() == false)
				borderCenterXField.setText(String.valueOf(saveData.getBorderCenterX()));
			
			if(borderCenterZField.getText() != String.valueOf(saveData.getBorderCenterZ()) && borderCenterZField.isFocused() == false)
				borderCenterZField.setText(String.valueOf(saveData.getBorderCenterZ()));
			
			if(shrinkButton.getBoolean() != saveData.isShrinkEnabled())
				shrinkButton.setBoolean(saveData.isShrinkEnabled());
			
			if(shrinkTimerField.getText() != String.valueOf(saveData.getShrinkTimer()) && shrinkTimerField.isFocused() == false)
				shrinkTimerField.setText(String.valueOf(saveData.getShrinkTimer()));
			
			if(shrinkSizeField.getText() != String.valueOf(saveData.getShrinkSize()) && shrinkSizeField.isFocused() == false)
				shrinkSizeField.setText(String.valueOf(saveData.getShrinkSize()));
			
			if(shrinkOvertimeField.getText() != String.valueOf(saveData.getShrinkOvertime()) && shrinkOvertimeField.isFocused() == false)
				shrinkOvertimeField.setText(String.valueOf(saveData.getShrinkOvertime()));
			
			if(ShrinkModeButton.getText() != saveData.getShrinkMode())
				ShrinkModeButton.setText(saveData.getShrinkMode());
		}
		
		if(this.currPage == 2)
		{
			if(timeLockButton.getBoolean() != saveData.isTimeLock())
				timeLockButton.setBoolean(saveData.isTimeLock());
				
			if(timeLockTimerField.getText() != String.valueOf(saveData.getTimeLockTimer()) && timeLockTimerField.isFocused() == false)
				timeLockTimerField.setText(String.valueOf(saveData.getTimeLockTimer()));
			
			if(minuteMarkButton.getBoolean() != saveData.isMinuteMark())
				minuteMarkButton.setBoolean(saveData.isMinuteMark());
			
			if(minMarkTimerField.getText() != String.valueOf(saveData.getMinuteMarkTime()) && minMarkTimerField.isFocused() == false)
				minMarkTimerField.setText(String.valueOf(saveData.getMinuteMarkTime()));
			
			if(nameButton.getBoolean() != saveData.isTimedNames())
				nameButton.setBoolean(saveData.isTimedNames());
			
			if(nameTimerField.getText() != String.valueOf(saveData.getNameTimer()) && nameTimerField.isFocused() == false)
				nameTimerField.setText(String.valueOf(saveData.getNameTimer()));
			
			if(glowButton.getBoolean() != saveData.isTimedGlow())
				glowButton.setBoolean(saveData.isTimedGlow());
			
			if(glowTimerField.getText() != String.valueOf(saveData.getGlowTime()) && glowTimerField.isFocused() == false)
				glowTimerField.setText(String.valueOf(saveData.getGlowTime()));
		}
		
		if(this.currPage == 3)
		{
			if(regenPotionsButton.getBoolean() != saveData.isRegenPotions())
				regenPotionsButton.setBoolean(saveData.isRegenPotions());
			
			if(level2PotionsButton.getBoolean() != saveData.isLevel2Potions())
				level2PotionsButton.setBoolean(saveData.isLevel2Potions());
			
			if(notchApplesButton.getBoolean() != saveData.isNotchApples())
				notchApplesButton.setBoolean(saveData.isNotchApples());
			
			if(autoCookButton.getBoolean() != saveData.isAutoCook())
				autoCookButton.setBoolean(saveData.isAutoCook());
			
			if(itemConvertButton.getBoolean() != saveData.isItemConversion())
				itemConvertButton.setBoolean(saveData.isItemConversion());
			
			if(netherButton.getBoolean() != saveData.isNetherEnabled())
				netherButton.setBoolean(saveData.isNetherEnabled());
			
			if(healthTabButton.getBoolean() != saveData.isHealthInTab())
				healthTabButton.setBoolean(saveData.isHealthInTab());
			
			if(healthSideButton.getBoolean() != saveData.isHealthOnSide())
				healthSideButton.setBoolean(saveData.isHealthOnSide());
			
			if(healthNameButton.getBoolean() != saveData.isHealthUnderName())
				healthNameButton.setBoolean(saveData.isHealthUnderName());
		}
		
		if(this.currPage == 4)
		{
			if(weatherButton.getBoolean() != saveData.isWeatherEnabled())
				weatherButton.setBoolean(saveData.isWeatherEnabled());
			
			if(mobGriefingButton.getBoolean() != saveData.isMobGriefing())
				mobGriefingButton.setBoolean(saveData.isMobGriefing());
			
			if(customHealthButton.getBoolean() != saveData.isApplyCustomHealth())
				customHealthButton.setBoolean(saveData.isApplyCustomHealth());
			
			if(maxHealthField.getText() != String.valueOf(saveData.getMaxHealth()) && maxHealthField.isFocused() == false)
				maxHealthField.setText(String.valueOf(saveData.getMaxHealth()));
			
			if(randomSpawnButton.getBoolean() != saveData.isRandomSpawns())
				randomSpawnButton.setBoolean(saveData.isRandomSpawns());
			
			if(spreadDistanceField.getText() != String.valueOf(saveData.getMaxHealth()) && spreadDistanceField.isFocused() == false)
				spreadDistanceField.setText(String.valueOf(saveData.getSpreadDistance()));
			
			if(spreadMaxRangeField.getText() != String.valueOf(saveData.getMaxHealth()) && spreadMaxRangeField.isFocused() == false)
				spreadMaxRangeField.setText(String.valueOf(saveData.getSpreadMaxRange()));
			
			if(spreadRespectTeamButton.getBoolean() != saveData.isSpreadRespectTeam())
				spreadRespectTeamButton.setBoolean(saveData.isSpreadRespectTeam());
		}
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
    	this.nameTimerField.setVisible(this.currPage == 2);
    	this.nameTimerField.setEnabled(this.currPage == 2);
    	this.glowTimerField.setVisible(this.currPage == 2);
    	this.glowTimerField.setEnabled(this.currPage == 2);
    	
    	this.minuteMarkButton.visible = this.currPage == 2;
    	this.resetMinuteMarkTimerButton.visible = this.currPage == 2;
    	this.nameButton.visible = this.currPage == 2;
    	this.resetNameTimerButton.visible = this.currPage == 2;
    	this.glowButton.visible = this.currPage == 2;
    	this.resetGlowTimerButton.visible = this.currPage == 2;
    	
    	this.netherButton.visible = this.currPage == 3;
    	
    	this.healthTabButton.visible = this.currPage == 3;
    	this.healthSideButton.visible = this.currPage == 3;
    	this.healthNameButton.visible = this.currPage == 3;
    	
    	this.regenPotionsButton.visible = this.currPage == 3;
    	this.level2PotionsButton.visible = this.currPage == 3;
    	this.notchApplesButton.visible = this.currPage == 3;
    	this.autoCookButton.visible = this.currPage == 3;
    	this.itemConvertButton.visible = this.currPage == 3;
    	
    	this.weatherButton.visible = this.currPage == 4;
    	this.mobGriefingButton.visible = this.currPage == 4;
    	this.customHealthButton.visible = this.currPage == 4;
    	this.spreadRespectTeamButton.visible = this.currPage == 4;
    	this.randomSpawnButton.visible = this.currPage == 4;
    	this.maxHealthField.setVisible(this.currPage == 4);
    	this.maxHealthField.setEnabled(this.currPage == 4);
    	
    	this.UHCStartButton.visible = this.currPage == 5;
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
    	//System.out.println(friendlyFire);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BOOK_TEXTURE);
        int i = (this.width - 192) / 2;
        int j = 2;
        this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);

        String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);

        int j1 = this.fontRenderer.getStringWidth(s4);
        this.fontRenderer.drawString(s4, i - j1 + 192 - 44, 18, 0);
        
        List<String> ShrinkModeShrink = new ArrayList<>();
		ShrinkModeShrink.add(I18n.format("book.uhc.explain.shrinkmodeshrink"));
		ShrinkModeShrink.add(I18n.format("book.uhc.explain.shrinkmodeshrink2"));
		List<String> ShrinkModeArena = new ArrayList<>();
		ShrinkModeArena.add(I18n.format("book.uhc.explain.shrinkmodearena"));
		ShrinkModeArena.add(I18n.format("book.uhc.explain.shrinkmodearena2"));
		List<String> ShrinkModeControl = new ArrayList<>();
		ShrinkModeControl.add(I18n.format("book.uhc.explain.shrinkmodecontrol"));
		ShrinkModeControl.add(I18n.format("book.uhc.explain.shrinkmodecontrol2"));
		
		String minuteMessageString = I18n.format("book.uhc.option.minutes");
		String locationString = I18n.format("book.uhc.option.location");
		String resetString = I18n.format("book.uhc.option.reset");
    	String TimeLockString = I18n.format("book.uhc.option.timelock");
		String minMarkString = I18n.format("book.uhc.option.minmark");
		String timedNameString = I18n.format("book.uhc.option.timedname");
		String timedGlowString = I18n.format("book.uhc.option.timedglow");
		String timeModeDayText = I18n.format("book.uhc.option.timemodeday"); 
		String timeModeNightText = I18n.format("book.uhc.option.timemodenight");
		String netherTravelString = I18n.format("book.uhc.option.nether");
    	String regenPotionsString = I18n.format("book.uhc.option.regenpotion");
		String level2PotionsString = I18n.format("book.uhc.option.level2potion");
		String notchApplesString = I18n.format("book.uhc.option.notchapples");
		String autoCookString = I18n.format("book.uhc.option.autocook");
		String itemConvertString = I18n.format("book.uhc.option.convertion");
		
		String weatherString = I18n.format("book.uhc.option.weather");
		String mobGriefingString = I18n.format("book.uhc.option.mobgriefing");
		String customHealthString = I18n.format("book.uhc.option.customhealth");
		String randomSpawnString = I18n.format("book.uhc.option.randomspawns");
		String spreadDistanceString = I18n.format("book.uhc.option.spreaddistance");
		String spreadMaxRangeString = I18n.format("book.uhc.option.spreadrange");
		String spreadRespectTeamString = I18n.format("book.uhc.option.spreadteams");

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

			boolean flag = hoverBoolean(mouseX, mouseY, maxTeamSizeField.x, maxTeamSizeField.y, maxTeamSizeField.width, maxTeamSizeField.height);
			String infinityString = I18n.format("book.uhc.option.infinite");
			
			if(isColorNotHovered() && !flag)
    	        this.fontRenderer.drawString(teamSelect, i + 65, j + 28, 0xFF555555);
			
			if(flag && maxTeamSizeField.isFocused() == false)
		        this.drawCenteredString(fontRenderer, infinityString, i + 91, j + 28, 0xFFFF5555);
			
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

			boolean flag = hoverBoolean(mouseX, mouseY, shrinkTimerField.x, shrinkTimerField.y, shrinkTimerField.width, shrinkTimerField.height);
			boolean flag1 = hoverBoolean(mouseX, mouseY, shrinkOvertimeField.x, shrinkOvertimeField.y, shrinkOvertimeField.width, shrinkOvertimeField.height);
			if((flag && shrinkTimerField.isFocused() == false) || (flag1 && shrinkOvertimeField.isFocused() == false))
		        this.drawCenteredString(fontRenderer, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
	    }
	    
	    if(this.currPage == 2)
	    {
	    	this.fontRenderer.drawString(TimeLockString, i + 38, j + 28, 0xFF555555);
	    	drawField(timeLockTimerField);
	    	
	    	String TimeLockTimerString = I18n.format("book.uhc.option.timelocktimer");
	    	this.fontRenderer.drawString(TimeLockTimerString, i + 44, j + 41, 0xFF555555);
	    	
	    	String TimeLockModeString = I18n.format("book.uhc.option.timelockmode");
	    	this.fontRenderer.drawString(TimeLockModeString, i + 44, j + 53, 0xFF555555);

	    	this.fontRenderer.drawString(minMarkString, i + 38, j + 68, 0xFF555555);

			String minMarkTimerString = I18n.format("book.uhc.option.minmarktime");
	    	this.fontRenderer.drawString(minMarkTimerString, i + 44, j + 80, 0xFF555555);
			drawField(minMarkTimerField);
			
	    	this.fontRenderer.drawString(timedNameString, i + 38, j + 94, 0xFF555555);
	    	
	    	String timedNameTimerString = I18n.format("book.uhc.option.timednametime");
	    	this.fontRenderer.drawString(timedNameTimerString, i + 44, j + 106, 0xFF555555);
			drawField(nameTimerField);
			
	    	this.fontRenderer.drawString(timedGlowString, i + 38, j + 122, 0xFF555555);
	    	
	    	String timedGlowStringTimerString = I18n.format("book.uhc.option.timedglowtime");
	    	this.fontRenderer.drawString(timedGlowStringTimerString, i + 44, j + 134, 0xFF555555);
			drawField(glowTimerField);
			
			boolean flag = hoverBoolean(mouseX, mouseY, timeLockTimerField.x, timeLockTimerField.y, timeLockTimerField.width, timeLockTimerField.height);
			boolean flag1 = hoverBoolean(mouseX, mouseY, minMarkTimerField.x, minMarkTimerField.y, minMarkTimerField.width, minMarkTimerField.height);
			if((flag && timeLockTimerField.isFocused() == false) || (flag1 && minMarkTimerField.isFocused() == false))
		        this.drawCenteredString(fontRenderer, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
	    }
	    
	    if(this.currPage == 3)
	    {
	        this.fontRenderer.drawString(regenPotionsString, i+38, j+28, 0xFF555555);

	        this.fontRenderer.drawString(level2PotionsString, i+38, j+40, 0xFF555555);

	        this.fontRenderer.drawString(notchApplesString, i+38, j+52, 0xFF555555);
	        
	        this.fontRenderer.drawString(autoCookString, i+38, j+68, 0xFF555555);
	        
	        this.fontRenderer.drawString(itemConvertString, i+38, j+80, 0xFF555555);
	        
	        this.fontRenderer.drawString(netherTravelString, i + 38, j + 98, 0xFF555555);

			String healthInTabString = I18n.format("book.uhc.option.healthtab");
	        this.fontRenderer.drawString(healthInTabString, i + 38, j + 114, 0xFF555555);
	        
			String healthOnSideString = I18n.format("book.uhc.option.healthside");
	        this.fontRenderer.drawString(healthOnSideString, i + 38, j + 126, 0xFF555555);

			String healthUnderNameString = I18n.format("book.uhc.option.healthname");
	        this.fontRenderer.drawString(healthUnderNameString, i + 38, j + 138, 0xFF555555);  
	    }
	    
	    if(this.currPage == 4)
	    {
	    	this.fontRenderer.drawString(weatherString, i+38, j+28, 0xFF555555);
	    	
	    	this.fontRenderer.drawString(mobGriefingString, i+38, j+40, 0xFF555555);
	    	
	    	this.fontRenderer.drawString(customHealthString, i+38, j+57, 0xFF555555);
	    	
			String healthMaxString = I18n.format("book.uhc.option.maxhealth");
	    	this.fontRenderer.drawString(healthMaxString, i+44, j+69, 0xFF555555);
	    	drawField(maxHealthField);
	    	
	    	this.fontRenderer.drawString(randomSpawnString, i+38, j+86, 0xFF555555);
	    	
	    	this.fontRenderer.drawString(spreadDistanceString, i+44, j+98, 0xFF555555);
	    	drawField(spreadDistanceField);

	    	this.fontRenderer.drawString(spreadMaxRangeString, i+44, j+110, 0xFF555555);
	    	drawField(spreadMaxRangeField);
	    	
	    	this.fontRenderer.drawString(spreadRespectTeamString, i+44, j+122, 0xFF555555);

			String healthExplain = I18n.format("book.uhc.explain.healthExplain");
	    	boolean flag2 = hoverBoolean(mouseX, mouseY, maxHealthField.x, maxHealthField.y, maxHealthField.width, maxHealthField.height);
			if(flag2 && maxHealthField.isFocused() == false)
		        this.drawCenteredString(fontRenderer, healthExplain, mouseX, mouseY + 5, 0xFFFF5555);
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
	    
		if(ShrinkModeButton.visible == true && ShrinkModeButton.isMouseOver() == true)
		{
			String ShrinkMode = saveData.getShrinkMode();
			if(ShrinkMode.equals("Shrink"))
			{
				this.drawInfoTooltip(ShrinkModeShrink, mouseX, mouseY);
			}
			if(ShrinkMode.equals("Arena"))
			{
				this.drawInfoTooltip(ShrinkModeArena, mouseX, mouseY);
			}
			if(ShrinkMode.equals("Control"))
			{
				this.drawInfoTooltip(ShrinkModeControl, mouseX, mouseY);
			}
		}
	    
	    if(timeModeButton.visible == true && timeModeButton.isMouseOver() == true)
		{
			String TimeMode = saveData.getTimeMode();
			if(TimeMode.equals("Day"))
		        this.drawInfoTooltip(timeModeDayText, mouseX, mouseY);
			if(TimeMode.equals("Night"))
		        this.drawInfoTooltip(timeModeNightText, mouseX, mouseY);
		}

	    if(this.currPage == 2 && hoverBoolean(mouseX, mouseY, i+38, j+28, fontRenderer.getStringWidth(TimeLockString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(I18n.format("book.uhc.explain.timelock"), mouseX, mouseY);
	    }
	    
        List<String> MinuteMarkExplain = new ArrayList<>();
        MinuteMarkExplain.add(I18n.format("book.uhc.explain.minmark"));
        MinuteMarkExplain.add(I18n.format("book.uhc.explain.minmark2"));
        List<String> timedNameExplain = new ArrayList<>();
        timedNameExplain.add(I18n.format("book.uhc.explain.timename"));
        timedNameExplain.add(I18n.format("book.uhc.explain.timename2"));
        List<String> timedGlowExplain = new ArrayList<>();
        timedGlowExplain.add(I18n.format("book.uhc.explain.timeglow"));
        timedGlowExplain.add(I18n.format("book.uhc.explain.timeglow2"));
        
	    if(this.currPage == 2 && hoverBoolean(mouseX, mouseY, i+38, j+68, fontRenderer.getStringWidth(minMarkString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(MinuteMarkExplain, mouseX, mouseY);
	    }
	    if(this.currPage == 2 && hoverBoolean(mouseX, mouseY, i+38, j+94, fontRenderer.getStringWidth(timedNameString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(timedNameExplain, mouseX, mouseY);
	    }
	    if(this.currPage == 2 && hoverBoolean(mouseX, mouseY, i+38, j+122, fontRenderer.getStringWidth(timedGlowString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(timedGlowExplain, mouseX, mouseY);
	    }
	    
        List<String> regenPotionExplain = new ArrayList<>();
        regenPotionExplain.add(I18n.format("book.uhc.explain.regenpotion"));
        regenPotionExplain.add(I18n.format("book.uhc.explain.regenpotion2"));
        List<String> level2PotionExplain = new ArrayList<>();
        level2PotionExplain.add(I18n.format("book.uhc.explain.level2potion"));
        level2PotionExplain.add(I18n.format("book.uhc.explain.level2potion2"));
        List<String> notchApplesExplain = new ArrayList<>();
        notchApplesExplain.add(I18n.format("book.uhc.explain.notchapple"));
        notchApplesExplain.add(I18n.format("book.uhc.explain.notchapple2"));
        
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+28, fontRenderer.getStringWidth(regenPotionsString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(regenPotionExplain, mouseX, mouseY);
	    }
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+40, fontRenderer.getStringWidth(level2PotionsString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(level2PotionExplain, mouseX, mouseY);
	    }
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+52, fontRenderer.getStringWidth(notchApplesString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(notchApplesExplain, mouseX, mouseY);
	    }
	    
        List<String> autoSmeltExplain = new ArrayList<>();
        autoSmeltExplain.add(I18n.format("book.uhc.explain.autocook"));
        autoSmeltExplain.add(I18n.format("book.uhc.explain.autocook2"));
        autoSmeltExplain.add(I18n.format("book.uhc.explain.autocook3"));
        autoSmeltExplain.add(I18n.format("book.uhc.explain.autocook4"));
        List<String> itemConvertExplain = new ArrayList<>();
        itemConvertExplain.add(I18n.format("book.uhc.explain.itemconvert"));
        itemConvertExplain.add(I18n.format("book.uhc.explain.itemconvert2"));
        itemConvertExplain.add(I18n.format("book.uhc.explain.itemconvert3"));
        itemConvertExplain.add(I18n.format("book.uhc.explain.itemconvert4"));
        itemConvertExplain.add(I18n.format("book.uhc.explain.itemconvert5"));
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+68, fontRenderer.getStringWidth(autoCookString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(autoSmeltExplain, mouseX, mouseY);
	    }
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+80, fontRenderer.getStringWidth(itemConvertString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(itemConvertExplain, mouseX, mouseY);
	    }
	    
        String netherTravelExplain = I18n.format("book.uhc.explain.nether");
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+35, j+98, fontRenderer.getStringWidth(netherTravelString), fontRenderer.FONT_HEIGHT))
        {
			this.drawInfoTooltip(netherTravelExplain, mouseX, mouseY);
        }
	    
	    String weatherExplain = I18n.format("book.uhc.explain.weather");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+38, j+28, fontRenderer.getStringWidth(weatherString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(weatherExplain, mouseX, mouseY);
	    }
	    String mobGriefingExplain = I18n.format("book.uhc.explain.mobgriefing");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+38, j+40, fontRenderer.getStringWidth(mobGriefingString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(mobGriefingExplain, mouseX, mouseY);
	    }
	    String customHealthExplain = I18n.format("book.uhc.explain.customhealth");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+38, j+57, fontRenderer.getStringWidth(customHealthString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(customHealthExplain, mouseX, mouseY);
	    }
	    List<String> randomSpawnsExplain = new ArrayList<>();
	    randomSpawnsExplain.add(I18n.format("book.uhc.explain.randomspawns"));
	    randomSpawnsExplain.add(I18n.format("book.uhc.explain.randomspawns2"));
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+38, j+86, fontRenderer.getStringWidth(randomSpawnString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(randomSpawnsExplain, mouseX, mouseY);
	    }
	    String spreadDistanceExplain = I18n.format("book.uhc.explain.spreaddistance");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+44, j+98, fontRenderer.getStringWidth(spreadDistanceString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(spreadDistanceExplain, mouseX, mouseY);
	    }
	    String spreadMaxRangeExplain = I18n.format("book.uhc.explain.spreadrange");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+44, j+110, fontRenderer.getStringWidth(spreadMaxRangeString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(spreadMaxRangeExplain, mouseX, mouseY);
	    }
	    String spreadRespectTeamExplain = I18n.format("book.uhc.explain.spreadteams");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+44, j+122, fontRenderer.getStringWidth(spreadRespectTeamString), fontRenderer.FONT_HEIGHT))
	    {
	    	this.drawInfoTooltip(spreadRespectTeamExplain, mouseX, mouseY);
	    }
    }
    
    public void drawInfoTooltip(String text, int x, int y)
    {
        this.drawInfoTooltip(Arrays.asList(text), x, y + fontRenderer.FONT_HEIGHT);
    }
    
    public void drawInfoTooltip(List<String> textLines, int x, int y)
    {
    	drawInfoTooltip(textLines, x, y, fontRenderer);
    }
    
    protected void drawInfoTooltip(List<String> textLines, int x, int y, FontRenderer font)
    {
        if (!textLines.isEmpty())
        {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines)
            {
                int j = this.fontRenderer.getStringWidth(s);

                if (j > i)
                    i = j;
            }

            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;

            if (textLines.size() > 1)
            {
                k += 2 + (textLines.size() - 1) * 10;
            }

            if (l1 + i > this.width)
                l1 -= 28 + i;

            if (i2 + k + 6 > this.height)
                i2 = this.height - k - 6;

            this.zLevel = 300.0F;
            this.itemRender.zLevel = 300.0F;
            this.drawRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, 0xFFf9eed1);
            this.drawRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, 0xFF1c0f00);
            this.drawRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, 0xFF1c0f00);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, 0xFF75321e, 0xFF5b2312);
            this.drawRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, 0xFF1c0f00);
            this.drawRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, 0xFF1c0f00);
            this.drawRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 0xFF4c1a0b);
            this.drawRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 0xFF4c1a0b);
            this.drawRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 0xFF4c1a0b);
            this.drawRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 0xFF4c1a0b);
            
            for (int k1 = 0; k1 < textLines.size(); ++k1)
            {
                String s1 = textLines.get(k1);
                this.fontRenderer.drawStringWithShadow(s1, (float)l1, (float)i2, -1);

                if (k1 == 0)
                    i2 += 2;

                i2 += 10;
            }
            this.zLevel = 0.0F;
            this.itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
    
    public boolean hoverBoolean(int mouseX, int mouseY, int x, int y, int widthIn, int heigthIn) {
    	return mouseX >= x && mouseY >= y && mouseX < x + widthIn && mouseY < y + heigthIn;
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
            	sendTeamPacket(TextFormatting.DARK_RED.getFriendlyName(), I18n.format("color.darkred.name"), TextFormatting.DARK_RED.getColorIndex());
            }
            else if (button.id == 4)
            {
            	sendTeamPacket(TextFormatting.GOLD.getFriendlyName(), I18n.format("color.gold.name"), TextFormatting.GOLD.getColorIndex());
            }
            else if (button.id == 5)
            {
            	sendTeamPacket(TextFormatting.DARK_GREEN.getFriendlyName(), I18n.format("color.darkgreen.name"), TextFormatting.DARK_GREEN.getColorIndex());
            }
            else if (button.id == 6)
            {
				sendTeamPacket(TextFormatting.DARK_AQUA.getFriendlyName(), I18n.format("color.darkaqua.name"), TextFormatting.DARK_AQUA.getColorIndex());
            }
            else if (button.id == 7)
            {
				sendTeamPacket(TextFormatting.DARK_BLUE.getFriendlyName(), I18n.format("color.darkblue.name"), TextFormatting.DARK_BLUE.getColorIndex());
            }
            else if (button.id == 8)
            {
				sendTeamPacket(TextFormatting.DARK_PURPLE.getFriendlyName(), I18n.format("color.darkpurple.name"), TextFormatting.DARK_PURPLE.getColorIndex());
            }
            else if (button.id == 9)
            {
				sendTeamPacket(TextFormatting.DARK_GRAY.getFriendlyName(), I18n.format("color.darkgray.name"), TextFormatting.DARK_GRAY.getColorIndex());
            }
            else if (button.id == 10)
            {
				sendTeamPacket(TextFormatting.RED.getFriendlyName(), I18n.format("color.red.name"), TextFormatting.RED.getColorIndex());
            }
            else if (button.id == 11)
            {
				sendTeamPacket(TextFormatting.YELLOW.getFriendlyName(), I18n.format("color.yellow.name"), TextFormatting.YELLOW.getColorIndex());
            }
            else if (button.id == 12)
            {
				sendTeamPacket(TextFormatting.GREEN.getFriendlyName(), I18n.format("color.green.name"), TextFormatting.GREEN.getColorIndex());
            }
            else if (button.id == 13)
            {
				sendTeamPacket(TextFormatting.AQUA.getFriendlyName(), I18n.format("color.aqua.name"), TextFormatting.AQUA.getColorIndex());
            }
            else if (button.id == 14)
            {
				sendTeamPacket(TextFormatting.BLUE.getFriendlyName(), I18n.format("color.blue.name"), TextFormatting.BLUE.getColorIndex());
            }
            else if (button.id == 15)
            {
				sendTeamPacket(TextFormatting.LIGHT_PURPLE.getFriendlyName(), I18n.format("color.lightpurple.name"), TextFormatting.LIGHT_PURPLE.getColorIndex());
            }
            else if (button.id == 16)
            {
				sendTeamPacket(TextFormatting.GRAY.getFriendlyName(), I18n.format("color.gray.name"), TextFormatting.GRAY.getColorIndex());
            }
            else if (button.id == 17)
            {
				sendTeamPacket("spectator", I18n.format("color.black.name"), TextFormatting.BLACK.getColorIndex());
            }
            else if (button.id == 18)
            {
				sendTeamPacket("solo", I18n.format("color.white.name"), TextFormatting.WHITE.getColorIndex());
            }
            else if (button.id == 19)
            {
            	sendTeamRandomizerPacket();
            }
            
            else if(button.id == 20)
            {
            	randomTeamSize = 6;
            	sendPage1Packet();
            }
            else if(button.id == 21)
            {
            	maxTeamSize = -1;
            	sendPage1Packet();
            }
            else if(button.id == 22)
            {
            	boolean flag = saveData.isTeamCollision();
            	teamCollision = !flag;
            	sendPage1Packet();
            }
            else if(button.id == 23)
            {
            	boolean flag = saveData.isFriendlyFire();
            	friendlyFire = !flag;
            	sendPage1Packet();
            }
            else if(button.id == 24)
            {
            	if(saveData.isHealthInTab() == false)
            	{
            		healthInTab = true;
            		healthOnSide = false;
            		healthUnderName = false;
            		sendPage4Packet();
            	}
            }
            else if(button.id == 25)
            {
            	if(saveData.isHealthOnSide() == false)
            	{
            		healthInTab = false;
            		healthOnSide = true;
            		healthUnderName = false;
            		sendPage4Packet();
            	}
            }
            else if(button.id == 26)
            {
            	if(saveData.isHealthUnderName() == false)
            	{
            		healthInTab = false;
            		healthOnSide = false;
            		healthUnderName = true;
            		sendPage4Packet();
            	}
            }
            
            else if(button.id == 27)
            {
            	borderSize = 2048;
            	sendPage2Packet();
            }
            else if(button.id == 28)
            {
            	borderCenterX = originalBorderCenterX;
            	sendPage2Packet();
            }
            else if(button.id == 29)
            {
            	borderCenterZ = originalBorderCenterZ;
            	sendPage2Packet();
            }
            
            else if(button.id == 30)
            {
            	double playerX = editingPlayer.posX;
            	borderCenterX = playerX;
            	sendPage2Packet();
            }
            else if(button.id == 31)
            {
            	double playerZ = editingPlayer.posZ;
            	borderCenterZ = playerZ;
            	sendPage2Packet();
            }
            else if(button.id == 32)
            {
            	boolean flag = saveData.isShrinkEnabled();
            	shrinkEnabled = !flag;
            	sendPage2Packet();
            }
            
            else if(button.id == 33)
            {
            	shrinkTimer = 60;
            	sendPage2Packet();
            }
            else if(button.id == 34)
            {
            	shrinkSize = 256;
            	sendPage2Packet();
            }
            else if(button.id == 35)
            {
            	shrinkOvertime = 60;
            	sendPage2Packet();
            }
            else if(button.id == 36)
            {
            	String mode = shrinkMode;
            	if(mode.equals("Shrink"))
            	{
            		shrinkMode = "Arena";
            		sendPage2Packet();
            	}
            	if(mode.equals("Arena"))
            	{
            		shrinkMode = "Control";
            		sendPage2Packet();
            	}
            	if(mode.equals("Control"))
            	{
            		shrinkMode = "Shrink";
            		sendPage2Packet();
            	}
            }
            else if(button.id == 37)
            {
            	boolean flag = saveData.isTimeLock();
            	timeLock = !flag;
            	sendPage3Packet();
            }
            else if(button.id == 38)
            {
            	String mode = timeMode;
            	if(mode.equals("Day"))
            	{
            		timeMode = "Night";
            		sendPage3Packet();
            	}
            	if(mode.equals("Night"))
            	{
            		timeMode = "Day";
            		sendPage3Packet();
            	}
            }
            else if(button.id == 39)
            {
            	timeLockTimer = 60;
            	sendPage3Packet();
            }
            else if(button.id == 40)
            {
            	minuteMarkTime = 30;
            	sendPage3Packet();
            }
            else if(button.id == 41)
            {
            	boolean flag = saveData.isMinuteMark();
            	minuteMark = !flag;
            	sendPage3Packet();
            }
            else if(button.id == 42)
            {
            	boolean flag = saveData.isNetherEnabled();
            	netherEnabled = !flag;
            	sendPage4Packet();
            }
            else if(button.id == 43)
            {
            	boolean flag = saveData.isRegenPotions();
            	regenPotions = !flag;
            	sendPage4Packet();
            }
            else if(button.id == 44)
            {
            	boolean flag = saveData.isLevel2Potions();
            	level2Potions = !flag;
            	sendPage4Packet();
            }
            else if(button.id == 45)
            {
            	boolean flag = saveData.isNotchApples();
            	notchApples = !flag;
            	sendPage4Packet();
            }
            else if(button.id == 46)
            {
            	boolean flag = saveData.isTimedNames();
            	timedNames = !flag;
            	sendPage3Packet();
            }
            else if(button.id == 47)
            {
            	nameTimer = 30;
            	sendPage3Packet();
            }
            else if(button.id == 48)
            {
            	boolean flag = saveData.isTimedGlow();
            	timedGlow = !flag;
            	sendPage3Packet();
            }
            else if(button.id == 49)
            {
            	glowTime = 30;
            	sendPage3Packet();
            }
            else if(button.id == 50)
            {
            	boolean flag = saveData.isAutoCook();
            	autoCook = !flag;
            	sendPage4Packet();
            }
            else if(button.id == 51)
            {
            	boolean flag = saveData.isItemConversion();
            	itemConversion = !flag;
            	sendPage4Packet();
            }
            else if(button.id == 52)
            {
            	boolean flag = saveData.isWeatherEnabled();
            	weatherEnabled = !flag;
            	sendPage5Packet();
            }
            else if(button.id == 53)
            {
            	boolean flag = saveData.isMobGriefing();
            	mobGriefing = !flag;
            	sendPage5Packet();
            }
            else if(button.id == 54)
            {
            	startPacket();
            	this.mc.displayGuiScreen((GuiScreen)null);
            }
            else if(button.id == 55)
            {
            	boolean flag = saveData.isApplyCustomHealth();
            	applyCustomHealth = !flag;
            	sendPage5Packet();
            }
            else if(button.id == 56)
            {
            	boolean flag = saveData.isRandomSpawns();
            	randomSpawns = !flag;
            	sendPage5Packet();
            }
            else if(button.id == 57)
            {
            	boolean flag = saveData.isSpreadRespectTeam();
            	spreadRespectTeam = !flag;
            	sendPage5Packet();
            }
            this.updateButtons();
        }
    }
    
    public void sendTeamPacket(String team, String teamFormat, int indexColor) {
    	ModPackethandler.INSTANCE.sendToServer(new UHCPacketTeam(editingPlayer.getName(), team, teamFormat, indexColor));
    }
    
    public void sendTeamRandomizerPacket() {
    	ModPackethandler.INSTANCE.sendToServer(new UHCPacketTeamRandomizer());
    }
    
    public void sendPage1Packet()
    {
    	ModPackethandler.INSTANCE.sendToServer(new UHCPage1Packet(randomTeamSize, maxTeamSize, teamCollision, friendlyFire, difficulty));
    }
    
    public void sendPage2Packet()
    {
    	ModPackethandler.INSTANCE.sendToServer(new UHCPage2Packet(borderSize, borderCenterX, borderCenterZ, shrinkEnabled, shrinkTimer, shrinkSize, shrinkOvertime, shrinkMode));
    }
    
    public void sendPage3Packet()
    {
    	ModPackethandler.INSTANCE.sendToServer(new UHCPage3Packet(timeLock, timeLockTimer, timeMode, minuteMark, minuteMarkTime, timedNames, nameTimer, timedGlow, glowTime));
    }
    
    public void sendPage4Packet()
    {
    	ModPackethandler.INSTANCE.sendToServer(new UHCPage4Packet(regenPotions, level2Potions, notchApples, autoCook, itemConversion, netherEnabled, healthInTab, healthOnSide, healthUnderName));
    }
    
    public void sendPage5Packet()
    {
    	ModPackethandler.INSTANCE.sendToServer(new UHCPage5Packet(weatherEnabled, mobGriefing, applyCustomHealth, maxHealth, randomSpawns, spreadDistance, spreadMaxRange, spreadRespectTeam));
    }
    
    public void startPacket()
    {
    	ModPackethandler.INSTANCE.sendToServer(new UHCStartPacket());
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
        		if(randSizeField.mouseClicked(mouseX, mouseY, mouseButton))
            		randSizeField.setText("");
            	if(maxTeamSizeField.mouseClicked(mouseX, mouseY, mouseButton))
            		maxTeamSizeField.setText("");
            	if(difficultyField.mouseClicked(mouseX, mouseY, mouseButton))
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
        		if(borderSizeField.mouseClicked(mouseX, mouseY, mouseButton))
            		borderSizeField.setText("");
            	if(borderCenterXField.mouseClicked(mouseX, mouseY, mouseButton))
            		borderCenterXField.setText("");
            	if(borderCenterZField.mouseClicked(mouseX, mouseY, mouseButton))
            		borderCenterZField.setText("");
            	if(shrinkTimerField.mouseClicked(mouseX, mouseY, mouseButton))
            		shrinkTimerField.setText("");
            	if(shrinkSizeField.mouseClicked(mouseX, mouseY, mouseButton))
            		shrinkSizeField.setText("");
            	if(shrinkOvertimeField.mouseClicked(mouseX, mouseY, mouseButton))
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
        		if(timeLockTimerField.mouseClicked(mouseX, mouseY, mouseButton))
        			timeLockTimerField.setText("");
        		if(minMarkTimerField.mouseClicked(mouseX, mouseY, mouseButton))
        			minMarkTimerField.setText("");
        		if(nameTimerField.mouseClicked(mouseX, mouseY, mouseButton))
        			nameTimerField.setText("");
        		if(glowTimerField.mouseClicked(mouseX, mouseY, mouseButton))
        			glowTimerField.setText("");
        		
        		if(timeLockTimerField.isFocused() == false)
        			timeLockTimerField.setText(String.valueOf(saveData.getTimeLockTimer()));
        		if(minMarkTimerField.isFocused() == false)
        			minMarkTimerField.setText(String.valueOf(saveData.getMinuteMarkTime()));
        		if(nameTimerField.isFocused() == false)
        			nameTimerField.setText(String.valueOf(saveData.getNameTimer()));
        		if(glowTimerField.isFocused() == false)
        			glowTimerField.setText(String.valueOf(saveData.getGlowTime()));
        	}
        	
        	if(this.currPage == 4)
        	{
        		if(maxHealthField.mouseClicked(mouseX, mouseY, mouseButton))
        			maxHealthField.setText("");
        		if(spreadDistanceField.mouseClicked(mouseX, mouseY, mouseButton))
        			spreadDistanceField.setText("");
        		if(spreadMaxRangeField.mouseClicked(mouseX, mouseY, mouseButton))
        			spreadMaxRangeField.setText("");
        		
        		if(maxHealthField.isFocused() == false)
        			maxHealthField.setText(String.valueOf(saveData.getMaxHealth()));
        		if(spreadDistanceField.isFocused() == false)
        			spreadDistanceField.setText(String.valueOf(saveData.getSpreadDistance()));
        		if(spreadMaxRangeField.isFocused() == false)
        			spreadMaxRangeField.setText(String.valueOf(saveData.getSpreadMaxRange()));
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
    		
    		if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER)
    		{
    			if(randSizeField.isFocused())
    			{
    				String randText = randSizeField.getText();
    				
    				if(randText.isEmpty())
    					randSizeField.setText(String.valueOf(randomTeamSize));
    				else
    				{
    					randomTeamSize = Integer.parseInt(randText);
    					sendPage1Packet();
    				}
    				
    				randSizeField.setFocused(false);
    			}
    			
    			if(maxTeamSizeField.isFocused())
    			{
    				String teamSize = maxTeamSizeField.getText();
    				
    				if(teamSize.isEmpty() || Integer.parseInt(teamSize) > 14)
    					maxTeamSizeField.setText(String.valueOf(maxTeamSize));
    				else
    				{
    					maxTeamSize = Integer.parseInt(teamSize);
    					sendPage1Packet();
    				}
    				
    				maxTeamSizeField.setFocused(false);
    			}
    			
    			if(difficultyField.isFocused())
    			{
    				String difficultyText = difficultyField.getText();
    				
    				if(difficultyText.isEmpty() || Integer.parseInt(difficultyText) > 3)
    					difficultyField.setText(String.valueOf(difficulty));
    				else
    				{
    					difficulty = Integer.parseInt(difficultyText);
    					sendPage1Packet();
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
    		
    		if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER)
    		{
    			/* Border Size Field */
    			if(borderSizeField.isFocused())
    			{
    				String borderSizeText = borderSizeField.getText();
    				
    				if(borderSizeText.isEmpty())
    					borderSizeField.setText(String.valueOf(borderSize));
    				else
    				{
    					borderSize = Integer.parseInt(borderSizeText);
    					sendPage2Packet();
    				}
    				
    				borderSizeField.setFocused(false);
    			}
    			/* Border Center X Field */
    			if(borderCenterXField.isFocused())
    			{
    				String borderX = borderCenterXField.getText();
    				
    				if(borderX.isEmpty())
    					borderCenterXField.setText(String.valueOf(borderCenterX));
    				else
    				{
    					borderCenterX = Integer.parseInt(borderX);
    					sendPage2Packet();
    				}
    			
    				borderCenterXField.setFocused(false);
    			}
    			/* Border Center Z Field */
    			if(borderCenterZField.isFocused())
    			{
    				String borderZ = borderCenterZField.getText();
    				
    				if(borderZ.isEmpty())
    					borderCenterZField.setText(String.valueOf(borderCenterZ));
    				else
    				{
    					borderCenterZ = Integer.parseInt(borderZ);
    					sendPage2Packet();
    				}
    			
    				borderCenterZField.setFocused(false);
    			}
    			/* Shrink Timer Field */
    			if(shrinkTimerField.isFocused())
    			{
    				String shrinkTimerText = shrinkTimerField.getText();
    				
    				if(shrinkTimerText.isEmpty())
    					shrinkTimerField.setText(String.valueOf(shrinkTimer));
    				else
    				{
    					shrinkTimer = Integer.parseInt(shrinkTimerText);
    					sendPage2Packet();
    				}
    				
    				shrinkTimerField.setFocused(false);
    			}
    			/* Shrink Size Field */
    			if(shrinkSizeField.isFocused())
    			{
    				String shrinkSizeText = shrinkSizeField.getText();
    				
    				if(shrinkSizeText.isEmpty())
    					shrinkSizeField.setText(String.valueOf(shrinkSize));
    				else
    				{
    					shrinkSize = Integer.parseInt(shrinkSizeText);
    					sendPage2Packet();
    				}
    				
    				shrinkSizeField.setFocused(false);
    			}
    			/* Shrink Over Time Field */
    			if(shrinkOvertimeField.isFocused())
    			{
    				String ShrinkOverTimeText = shrinkOvertimeField.getText();
    				
    				if(ShrinkOverTimeText.isEmpty())
    					shrinkOvertimeField.setText(String.valueOf(shrinkOvertime));
    				else
    				{
    					shrinkOvertime = Integer.parseInt(ShrinkOverTimeText);
    					sendPage2Packet();
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
    		if(nameTimerField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			nameTimerField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(glowTimerField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			glowTimerField.textboxKeyTyped(typedChar, keyCode);
    		}
    		
    		if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER)
    		{
    			/* Shrink Timer Field */
    			if(timeLockTimerField.isFocused())
    			{
    				String timeLockTimerText = timeLockTimerField.getText();
    				
    				if(timeLockTimerText.isEmpty())
    					timeLockTimerField.setText(String.valueOf(timeLockTimer));
    				else
    				{
    					timeLockTimer = Integer.parseInt(timeLockTimerText);
    					sendPage3Packet();
    				}
    				
    				timeLockTimerField.setFocused(false);
    			}
    			/* Minute Mark Timer Field */
    			if(minMarkTimerField.isFocused())
    			{
    				String minuteMarkTimerText = minMarkTimerField.getText();
    				
    				if(minuteMarkTimerText.isEmpty())
    					minMarkTimerField.setText(String.valueOf(minuteMarkTime));
    				else
    				{
    					minuteMarkTime = Integer.parseInt(minuteMarkTimerText);
    					sendPage3Packet();
    				}
    				
    				minMarkTimerField.setFocused(false);
    			}
    			/* Name Timer Field */
    			if(nameTimerField.isFocused())
    			{
    				String nameTimerText = nameTimerField.getText();
    				
    				if(nameTimerText.isEmpty())
    					nameTimerField.setText(String.valueOf(nameTimer));
    				else
    				{
    					nameTimer = Integer.parseInt(nameTimerText);
    					sendPage3Packet();
    				}
    				
    				nameTimerField.setFocused(false);
    			}
    			/* Glow Timer Field */
    			if(glowTimerField.isFocused())
    			{
    				String glowTimer = glowTimerField.getText();
    				
    				if(glowTimer.isEmpty())
    					glowTimerField.setText(String.valueOf(glowTime));
    				else
    				{
    					glowTime = Integer.parseInt(glowTimer);
    					sendPage3Packet();
    				}
    				
    				glowTimerField.setFocused(false);
    			}
    		}
    	}
    	if (this.currPage == 4)
    	{
    		if(maxHealthField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			maxHealthField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(spreadDistanceField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			spreadDistanceField.textboxKeyTyped(typedChar, keyCode);
    		}
    		if(spreadMaxRangeField.isFocused() && (charNumeric(typedChar) || keyCode == Keyboard.KEY_BACK))
    		{
    			spreadMaxRangeField.textboxKeyTyped(typedChar, keyCode);
    		}
    		
			if(keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER)
			{
				/* Max Health Field */
				if(maxHealthField.isFocused())
				{
					String maxHealthText = maxHealthField.getText();
					
					if(maxHealthText.isEmpty())
						maxHealthField.setText(String.valueOf(maxHealth));
					else
					{
						maxHealth = Integer.parseInt(maxHealthText);
						sendPage5Packet();
					}
					
					maxHealthField.setFocused(false);
				}
				/* Spread Distance Field */
				if(spreadDistanceField.isFocused())
				{
					String maxDistanceText = spreadDistanceField.getText();
					
					if(maxDistanceText.isEmpty())
						spreadDistanceField.setText(String.valueOf(spreadDistance));
					else
					{
						spreadDistance = Integer.parseInt(maxDistanceText);
						sendPage5Packet();
					}
					
					spreadDistanceField.setFocused(false);
				}
				/* Spread Max Range Field */
				if(spreadMaxRangeField.isFocused())
				{
					String maxRangeText = spreadMaxRangeField.getText();
					
					if(maxRangeText.isEmpty())
						spreadMaxRangeField.setText(String.valueOf(spreadMaxRange));
					else
					{
						spreadMaxRange = Integer.parseInt(maxRangeText);
						sendPage5Packet();
					}
					
					spreadMaxRangeField.setFocused(false);
				}
			}
    	}
    }
    
    public boolean charNumeric(char typedChar)
    {
    	return (typedChar >= '0' && typedChar <= '9');
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
	static class StartButton extends GuiButton
	{	
		private final FontRenderer render;
		
		public StartButton(int buttonId, int x, int y, FontRenderer renderIn)
		{
			super(buttonId, x, y, 85, 22, "");
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
				int textureX = 30;
				int textureY = 232;
				if (this.hovered)
					textureX += 85;
				drawTexturedModalRect(x, y,  textureX, textureY, 85, 22);
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