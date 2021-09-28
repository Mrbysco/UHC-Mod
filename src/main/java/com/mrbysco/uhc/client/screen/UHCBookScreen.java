package com.mrbysco.uhc.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.data.UHCSaveData;
import com.mrbysco.uhc.packets.UHCPacketHandler;
import com.mrbysco.uhc.packets.UHCPacketTeam;
import com.mrbysco.uhc.packets.UHCPacketTeamRandomizer;
import com.mrbysco.uhc.packets.UHCPage1Packet;
import com.mrbysco.uhc.packets.UHCPage2Packet;
import com.mrbysco.uhc.packets.UHCPage3Packet;
import com.mrbysco.uhc.packets.UHCPage4Packet;
import com.mrbysco.uhc.packets.UHCPage5Packet;
import com.mrbysco.uhc.packets.UHCPage6Packet;
import com.mrbysco.uhc.packets.UHCStartPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UHCBookScreen extends Screen {
	/** Book texture */
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");
    /** The player editing the UHC Settings */
    private final PlayerEntity editingPlayer;
    /** The total amount of pages */
    private final int bookTotalPages = 6;
    private int currPage;
    
    protected ArrayList<TextFieldWidget> textBoxList = new ArrayList<>();
    
    /** Buttons */
    private ChangePageButton buttonNextPage;
    private ChangePageButton buttonPreviousPage;
    private Button buttonDone;

	private ColorButton[] colorButtons = new ColorButton[17];

    private TextFieldWidget randSizeField;
    private TextFieldWidget maxTeamSizeField;
    
    private TextFieldWidget borderSizeField, borderCenterXField, borderCenterZField, difficultyField;
    private TextFieldWidget shrinkTimerField, shrinkSizeField, shrinkOvertimeField, timeLockTimerField, minMarkTimerField, nameTimerField, glowTimerField;
    private TextFieldWidget maxHealthField, spreadDistanceField, spreadMaxRangeField;
    private TextFieldWidget graceTimeField;
    private ResetButton resetRandButton, resetTeamSizeButton;
    private BooleanWidget collisionButton, damageButton;
    private BooleanWidget healthTabButton, healthSideButton, healthNameButton;
    
    private ResetButton resetBorderSizeButton, resetBorderCenterXButton, resetBorderCenterZButton;
    private LocationButton centerCurrentXButton, centerCurrentZButton;
    private ResetButton resetShrinkTimerButton, resetShrinkSizeButton, resetShrinkOverTimeButton;
    private TextButton shrinkModeButton;
    
    private BooleanWidget shrinkButton;
    private BooleanWidget timeLockButton;
    private TextButton timeModeButton;
    private ResetButton resetTimeLockTimerButton;
    private BooleanWidget minuteMarkButton;
    private ResetButton resetMinuteMarkTimerButton;
    private BooleanWidget nameButton;
    private ResetButton resetNameTimerButton;
    private BooleanWidget glowButton;
    private ResetButton resetGlowTimerButton;
    
    private BooleanWidget autoCookButton, itemConvertButton;
    private BooleanWidget netherButton, regenPotionsButton, level2PotionsButton, notchApplesButton;
    private BooleanWidget weatherButton, mobGriefingButton;
    private BooleanWidget customHealthButton, randomSpawnButton, spreadRespectTeamButton;
    
    private BooleanWidget graceTimeButton;
    private LockWidget teamsLockedButton;

    private StartButton UHCStartButton;

    /** UHC save data */
    public static UHCSaveData saveData;

	private boolean uhcStarting;
	private boolean uhcOnGoing;
	private boolean teamsLocked;
	
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
	
	private boolean graceEnabled;
	private int graceTime;

	private CompoundNBT playerData;

	public UHCBookScreen(PlayerEntity player) {
		super(new StringTextComponent("U H C Book"));
        this.editingPlayer = player;
        this.playerData = player.getPersistentData();
	}

	public static void openScreen(PlayerEntity player) {
		Minecraft.getInstance().displayGuiScreen(new UHCBookScreen(player));
	}

	@Override
	public void init() {
		super.init();
		minecraft.keyboardListener.enableRepeatEvents(true);
        
        initValues();
        
        this.buttonDone = this.addButton(new Button(this.width / 2 - 100, 196, 200, 20, new TranslationTextComponent("gui.done"), (button) -> {
			this.minecraft.displayGuiScreen((Screen)null);
		}));

        int i = (this.width - 192) / 2;
        int j = 2;

		this.buttonNextPage = this.addButton(new ChangePageButton(i + 120, 156, true, (button) -> {
			if (this.currPage < this.bookTotalPages - 1) {
				++this.currPage;
			}
			this.updateButtons();
		}, true));
		this.buttonPreviousPage = this.addButton(new ChangePageButton(i + 38, 156, false, (button) -> {
			if (this.currPage < this.bookTotalPages - 1) {
				++this.currPage;
			}
			this.updateButtons();
		}, true));

		this.colorButtons[0] = this.addButton(new ColorButton(i + 43, j + 40, 10, 10, i + 92, j + 27, 0xFFAA0000,
				new TranslationTextComponent("color.darkred.name"), (button) -> {
			sendTeamPacket(TextFormatting.DARK_RED.getFriendlyName(), I18n.format("color.darkred.name"), TextFormatting.DARK_RED.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[1] = this.addButton(new ColorButton(i + 43 + 15, j + 40, 10, 10, i + 92, j + 27,  0xFFFFAA00,
				new TranslationTextComponent("color.gold.name"), (button) -> {
			sendTeamPacket(TextFormatting.GOLD.getFriendlyName(), I18n.format("color.gold.name"), TextFormatting.GOLD.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[2] = this.addButton(new ColorButton(i + 43 + 30, j + 40, 10, 10, i + 92, j + 27,  0xFF00AA00,
				new TranslationTextComponent("color.darkgreen.name"), (button) -> {
			sendTeamPacket(TextFormatting.DARK_GREEN.getFriendlyName(), I18n.format("color.darkgreen.name"), TextFormatting.DARK_GREEN.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[3] = this.addButton(new ColorButton(i + 43 + 45, j + 40, 10, 10, i + 92, j + 27,  0xFF00AAAA,
				new TranslationTextComponent("color.darkaqua.name"), (button) -> {
			sendTeamPacket(TextFormatting.DARK_AQUA.getFriendlyName(), I18n.format("color.darkaqua.name"), TextFormatting.DARK_AQUA.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[4] = this.addButton(new ColorButton(i + 43 + 60, j + 40, 10, 10, i + 92, j + 27,  0xFF0000AA,
				new TranslationTextComponent("color.darkblue.name"), (button) -> {
			sendTeamPacket(TextFormatting.DARK_BLUE.getFriendlyName(), I18n.format("color.darkblue.name"), TextFormatting.DARK_BLUE.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[5] = this.addButton(new ColorButton(i + 43 + 75, j + 40, 10, 10, i + 92, j + 27,  0xFFAA00AA,
				new TranslationTextComponent("color.darkpurple.name"), (button) -> {
			sendTeamPacket(TextFormatting.DARK_PURPLE.getFriendlyName(), I18n.format("color.darkpurple.name"), TextFormatting.DARK_PURPLE.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[6] = this.addButton(new ColorButton(i + 43 + 90, j + 40, 10, 10, i + 92, j + 27,  0xFF555555,
				new TranslationTextComponent("color.darkgray.name"), (button) -> {
			sendTeamPacket(TextFormatting.DARK_GRAY.getFriendlyName(), I18n.format("color.darkgray.name"), TextFormatting.DARK_GRAY.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[7] = this.addButton(new ColorButton(i + 43, j + 55, 10, 10, i + 92, j + 27,  0xFFFF5555,
				new TranslationTextComponent("color.red.name"), (button) -> {
			sendTeamPacket(TextFormatting.RED.getFriendlyName(), I18n.format("color.red.name"), TextFormatting.RED.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[8] = this.addButton(new ColorButton(i + 43 + 15, j + 55, 10, 10, i + 92, j + 27,  0xFFFFFF55,
				new TranslationTextComponent("color.yellow.name"), (button) -> {
			sendTeamPacket(TextFormatting.YELLOW.getFriendlyName(), I18n.format("color.yellow.name"), TextFormatting.YELLOW.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[9] = this.addButton(new ColorButton(i + 43 + 30, j + 55, 10, 10, i + 92, j + 27,  0xFF55FF55,
				new TranslationTextComponent("color.green.name"), (button) -> {
			sendTeamPacket(TextFormatting.GREEN.getFriendlyName(), I18n.format("color.green.name"), TextFormatting.GREEN.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[10] = this.addButton(new ColorButton(i + 43 + 45, j + 55, 10, 10, i + 92, j + 27,  0xFF55FFFF,
				new TranslationTextComponent("color.aqua.name"), (button) -> {
			sendTeamPacket(TextFormatting.AQUA.getFriendlyName(), I18n.format("color.aqua.name"), TextFormatting.AQUA.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[11] = this.addButton(new ColorButton(i + 43 + 60, j + 55, 10, 10, i + 92, j + 27,  0xFF5555FF,
				new TranslationTextComponent("color.blue.name"), (button) -> {
			sendTeamPacket(TextFormatting.BLUE.getFriendlyName(), I18n.format("color.blue.name"), TextFormatting.BLUE.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[12] = this.addButton(new ColorButton(i + 43 + 75, j + 55, 10, 10, i + 92, j + 27, 0xFFFF55FF,
				new TranslationTextComponent("color.lightpurple.name"), (button) -> {
			sendTeamPacket(TextFormatting.LIGHT_PURPLE.getFriendlyName(), I18n.format("color.lightpurple.name"), TextFormatting.LIGHT_PURPLE.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[13] = this.addButton(new ColorButton(i + 43 + 90, j + 55, 10, 10, i + 92, j + 27,  0xFFAAAAAA,
				new TranslationTextComponent("color.gray.name"), (button) -> {
			sendTeamPacket(TextFormatting.GRAY.getFriendlyName(), I18n.format("color.gray.name"), TextFormatting.GRAY.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[14] = this.addButton(new ColorButton(i + 43, j + 70, 10, 10, i + 92, j + 28,  0xFF000000,
				new TranslationTextComponent("color.black.name"), (button) -> {
			sendTeamPacket("spectator", I18n.format("color.black.name"), TextFormatting.BLACK.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[15] = this.addButton(new ColorButton(i + 43 + 90, j + 70, 10, 10, i + 92, j + 28,  0xFFFFFFFF,
				new TranslationTextComponent("color.white.name"), true, (button) -> {
			sendTeamPacket("solo", I18n.format("color.white.name"), TextFormatting.WHITE.getColorIndex());
			this.updateButtons();
		}));
		this.colorButtons[16] = this.addButton(new ColorButton(i + 43 + 15, j + 70, 56, 10, i + 92, j + 71,  0xFFAAAAAA,
				new TranslationTextComponent("color.random.name"), false, true, (button) -> {
			sendTeamRandomizerPacket();
			this.updateButtons();
		}));
    	
    	this.resetRandButton = this.addButton(new UHCBookScreen.ResetButton(i + 43 + 94, j + 85, (button) -> {
			randomTeamSize = 6;
			sendPage1Packet();
			this.updateButtons();
		}));
    	this.resetTeamSizeButton = this.addButton(new UHCBookScreen.ResetButton(i + 43 + 94, j + 99, (button) -> {
			maxTeamSize = -1;
			sendPage1Packet();
			this.updateButtons();
		}));
    	this.collisionButton = this.addButton(new BooleanWidget( i + 43 + 74, j + 113, saveData.isTeamCollision(), (button) -> {
			boolean flag = saveData.isTeamCollision();
			teamCollision = !flag;
			sendPage1Packet();
			this.updateButtons();
		}));
    	this.damageButton = this.addButton(new BooleanWidget( i + 43 + 74, j + 127, saveData.isFriendlyFire(), (button) -> {
			boolean flag = saveData.isFriendlyFire();
			friendlyFire = !flag;
			sendPage1Packet();
			this.updateButtons();
		}));
    	
    	this.resetBorderSizeButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 68, j + 36, (button) -> {
			borderSize = 2048;
			sendPage2Packet();
			this.updateButtons();
		}));
    	this.resetBorderCenterXButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 92, j + 60, (button) -> {
			borderCenterX = originalBorderCenterX;
			sendPage2Packet();
			this.updateButtons();
		}));
    	this.resetBorderCenterZButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 92, j + 74, (button) -> {
			borderCenterZ = originalBorderCenterZ;
			sendPage2Packet();
			this.updateButtons();
		}));
    	this.centerCurrentXButton = this.addButton(new UHCBookScreen.LocationButton( i + 43 + 76, j + 60, (button) -> {
			double playerX = editingPlayer.getPosX();
			borderCenterX = playerX;
			sendPage2Packet();
			this.updateButtons();
		}));
    	this.centerCurrentZButton = this.addButton(new UHCBookScreen.LocationButton( i + 43 + 76, j + 74, (button) -> {
			double playerZ = editingPlayer.getPosZ();
			borderCenterZ = playerZ;
			sendPage2Packet();
			this.updateButtons();
		}));
    	this.shrinkButton = this.addButton(new BooleanWidget( i + 43 + 70, j + 90, saveData.isShrinkEnabled(), (button) -> {
			boolean flag = saveData.isShrinkEnabled();
			shrinkEnabled = !flag;
			sendPage2Packet();
			this.updateButtons();
		}));
    	this.resetShrinkTimerButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 92, j + 104, (button) -> {
			shrinkTimer = 60;
			sendPage2Packet();
			this.updateButtons();
		}));
    	this.resetShrinkSizeButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 92, j + 116, (button) -> {
			shrinkSize = 256;
			sendPage2Packet();
			this.updateButtons();
		}));
    	this.resetShrinkOverTimeButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 92, j + 128, (button) -> {
			shrinkOvertime = 60;
			sendPage2Packet();
			this.updateButtons();
		}));
    	this.shrinkModeButton = this.addButton(new TextButton( i + 43 + 31, j + 140, new StringTextComponent(saveData.getShrinkMode()), minecraft, (button) -> {
			if(saveData.getShrinkMode().equals("Shrink")) {
				shrinkMode = "Arena";
				sendPage2Packet();
			}
			if(saveData.getShrinkMode().equals("Arena")) {
				shrinkMode = "Control";
				sendPage2Packet();
			}
			if(saveData.getShrinkMode().equals("Control")) {
				shrinkMode = "Shrink";
				sendPage2Packet();
			}
			this.updateButtons();
		}));
    	
    	this.timeLockButton = this.addButton(new BooleanWidget(i + 43 + 60, j + 25, saveData.isTimeLock(), (button) -> {
			boolean flag = saveData.isTimeLock();
			timeLock = !flag;
			sendPage3Packet();
			this.updateButtons();
		}));
    	this.timeModeButton = this.addButton(new TextButton( i + 43 + 32, j + 52, new StringTextComponent(saveData.getTimeMode()), minecraft, (button) -> {
			if(saveData.getTimeMode().equals("Day")) {
				timeMode = "Night";
				sendPage3Packet();
			}
			if(saveData.getTimeMode().equals("Night")) {
				timeMode = "Day";
				sendPage3Packet();
			}
			this.updateButtons();
		}));
    	this.resetTimeLockTimerButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 80, j + 38, (button) -> {
			timeLockTimer = 60;
			sendPage3Packet();
			this.updateButtons();
		}));
    	this.resetMinuteMarkTimerButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 80, j + 77, (button) -> {
			minuteMarkTime = 30;
			sendPage3Packet();
			this.updateButtons();
		}));
    	this.minuteMarkButton = this.addButton(new BooleanWidget( i + 43 + 60, j + 64, saveData.isMinuteMark(), (button) -> {
			boolean flag = saveData.isMinuteMark();
			minuteMark = !flag;
			sendPage3Packet();
			this.updateButtons();
		}));
    	this.nameButton = this.addButton(new BooleanWidget(i + 43 + 60, j + 91, saveData.isTimedNames(), (button) -> {
			boolean flag = saveData.isTimedNames();
			timedNames = !flag;
			sendPage3Packet();
			this.updateButtons();
		}));
    	this.resetNameTimerButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 80, j + 103, (button) -> {
			nameTimer = 30;
			sendPage3Packet();
			this.updateButtons();
		}));
    	this.glowButton = this.addButton(new BooleanWidget( i + 43 + 60, j + 119, saveData.isTimedGlow(), (button) -> {
			boolean flag = saveData.isTimedGlow();
			timedGlow = !flag;
			sendPage3Packet();
			this.updateButtons();
		}));
    	this.resetGlowTimerButton = this.addButton(new UHCBookScreen.ResetButton( i + 43 + 80, j + 131, (button) -> {
			glowTime = 30;
			sendPage3Packet();
			this.updateButtons();
		}));


		this.healthTabButton = this.addButton(new BooleanWidget(i + 43 + 90, j + 109, saveData.isHealthInTab(), (button) -> {
			if(!saveData.isHealthInTab()) {
				healthInTab = true;
				healthOnSide = false;
				healthUnderName = false;
				sendPage4Packet();
				this.updateButtons();
			}
		}));
		this.healthSideButton = this.addButton(new BooleanWidget( i + 43 + 90, j + 122, saveData.isHealthOnSide(), (button) -> {
			if(!saveData.isHealthOnSide()) {
				healthInTab = false;
				healthOnSide = true;
				healthUnderName = false;
				sendPage4Packet();
				this.updateButtons();
			}
		}));
		this.healthNameButton = this.addButton(new BooleanWidget(i + 43 + 90, j + 135, saveData.isHealthUnderName(), (button) -> {
			if(!saveData.isHealthUnderName()) {
				healthInTab = false;
				healthOnSide = false;
				healthUnderName = true;
				sendPage4Packet();
				this.updateButtons();
			}
		}));
		this.netherButton = this.addButton(new BooleanWidget( i + 43 + 90, j + 94, saveData.isNetherEnabled(), (button) -> {
			boolean flag = saveData.isNetherEnabled();
			netherEnabled = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
		this.regenPotionsButton = this.addButton(new BooleanWidget( i + 43 + 90, j + 23, saveData.isRegenPotions(), (button) -> {
			boolean flag = saveData.isRegenPotions();
			regenPotions = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
		this.level2PotionsButton = this.addButton(new BooleanWidget( i + 43 + 90, j + 35, saveData.isLevel2Potions(), (button) -> {
			boolean flag = saveData.isLevel2Potions();
			level2Potions = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
		this.notchApplesButton = this.addButton(new BooleanWidget( i + 43 + 90, j + 48, saveData.isNotchApples(), (button) -> {
			boolean flag = saveData.isNotchApples();
			notchApples = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
    	this.autoCookButton = this.addButton(new BooleanWidget( i + 43 + 90, j + 63, saveData.isAutoCook(), (button) -> {
			boolean flag = saveData.isAutoCook();
			autoCook = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
    	this.itemConvertButton = this.addButton(new BooleanWidget( i + 43 + 90, j + 77, saveData.isItemConversion(), (button) -> {
			boolean flag = saveData.isItemConversion();
			itemConversion = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
    	
    	this.weatherButton = this.addButton(new BooleanWidget( i + 43 + 80, j + 23, saveData.isWeatherEnabled(), (button) -> {
			boolean flag = saveData.isWeatherEnabled();
			weatherEnabled = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));
    	this.mobGriefingButton = this.addButton(new BooleanWidget( i + 43 + 80, j + 35, saveData.isMobGriefing(), (button) -> {
			boolean flag = saveData.isMobGriefing();
			mobGriefing = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));
    	this.customHealthButton = this.addButton(new BooleanWidget( i + 43 + 80, j + 54, saveData.isMobGriefing(), (button) -> {
			boolean flag = saveData.isApplyCustomHealth();
			applyCustomHealth = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));
    	this.randomSpawnButton = this.addButton(new BooleanWidget( i + 43 + 80, j + 81, saveData.isRandomSpawns(), (button) -> {
			boolean flag = saveData.isRandomSpawns();
			randomSpawns = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));
    	this.spreadRespectTeamButton = this.addButton(new BooleanWidget( i + 43 + 80, j + 118, saveData.isSpreadRespectTeam(), (button) -> {
			boolean flag = saveData.isSpreadRespectTeam();
			spreadRespectTeam = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));
    	
    	this.graceTimeButton = this.addButton(new BooleanWidget( i + 43 + 64, j + 25, saveData.isGraceEnabled(), (button) -> {
			boolean flag = saveData.isGraceEnabled();
			graceEnabled = !flag;
			sendPage6Packet();
			this.updateButtons();
		}));
    	this.teamsLockedButton = this.addButton(new LockWidget( i + 43 + 73, j + 68, saveData.areTeamsLocked(), (button) -> {
			boolean flag = saveData.areTeamsLocked();
			teamsLocked = !flag;
			sendPage1Packet();
			this.updateButtons();
		}));
    			
    	this.UHCStartButton = this.addButton(new UHCBookScreen.StartButton(i + 43 + 7, j + 132, (button) -> {
			startPacket();
			this.minecraft.displayGuiScreen((Screen) null);
		}));

    	randSizeField = new TextFieldWidget(font, i + 43 + 75, j + 89, 20, 8, StringTextComponent.EMPTY);
    	setupField(randSizeField, 2, 0xFFFFAA00, String.valueOf(saveData.getRandomTeamSize()));

		maxTeamSizeField = new TextFieldWidget(font, i + 43 + 75, j + 101, 20, 8, StringTextComponent.EMPTY);
		setupField(maxTeamSizeField, 2, 0xFFFFAA00, String.valueOf(saveData.getMaxTeamSize()));

		borderSizeField = new TextFieldWidget(font, i + 43 + 36, j + 40, 32, 8, StringTextComponent.EMPTY);
		setupField(borderSizeField, 4, 0xFFFFAA00, String.valueOf(saveData.getBorderSize()));
		
		borderCenterXField = new TextFieldWidget(font, i + 55, j + 64, 42, 8, StringTextComponent.EMPTY);
		setupField(borderCenterXField, 6, 0xFFFFAA00, String.valueOf(saveData.getBorderCenterX()));
		
		borderCenterZField = new TextFieldWidget(font, i + 55, j + 76, 42, 8, StringTextComponent.EMPTY);
		setupField(borderCenterZField, 6, 0xFFFFAA00, String.valueOf(saveData.getBorderCenterZ()));
		
		difficultyField = new TextFieldWidget(font, i + 43 + 52, j + 144, 14, 8, StringTextComponent.EMPTY);
		setupField(difficultyField, 1, 0xFFFFAA00, String.valueOf(saveData.getDifficulty()));
	    
		shrinkTimerField = new TextFieldWidget(font, i + 43 + 52, j + 107, 32, 8, StringTextComponent.EMPTY);
		setupField(shrinkTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkTimer()));
		
		shrinkSizeField = new TextFieldWidget(font, i + 43 + 28, j + 118, 32, 8, StringTextComponent.EMPTY);
		setupField(shrinkSizeField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkSize()));
		
		shrinkOvertimeField = new TextFieldWidget(font, i + 43 + 32, j + 129, 32, 8, StringTextComponent.EMPTY);
		setupField(shrinkOvertimeField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkOvertime()));
		
		timeLockTimerField = new TextFieldWidget(font, i + 43 + 52, j + 41, 32, 8, StringTextComponent.EMPTY);
		setupField(timeLockTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getTimeLockTimer()));
		
		minMarkTimerField = new TextFieldWidget(font, i + 43 + 38, j + 80, 32, 8, StringTextComponent.EMPTY);
		setupField(minMarkTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getMinuteMarkTime()));
		
		nameTimerField = new TextFieldWidget(font, i + 43 + 38, j + 106, 32, 8, StringTextComponent.EMPTY);
		setupField(nameTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getNameTimer()));
		
		glowTimerField = new TextFieldWidget(font, i + 43 + 38, j + 133, 32, 8, StringTextComponent.EMPTY);
		setupField(glowTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getGlowTime()));
		
		maxHealthField = new TextFieldWidget(font, i + 43 + 60, j + 69, 32, 8, StringTextComponent.EMPTY);
		setupField(maxHealthField, 4, 0xFFFFAA00, String.valueOf(saveData.getMaxHealth()));
		
		spreadDistanceField = new TextFieldWidget(font, i + 43 + 60, j + 98, 32, 8, StringTextComponent.EMPTY);
		setupField(spreadDistanceField, 4, 0xFFFFAA00, String.valueOf(saveData.getSpreadDistance()));
		
		spreadMaxRangeField = new TextFieldWidget(font, i + 43 + 60, j + 110, 32, 8, StringTextComponent.EMPTY);
		setupField(spreadMaxRangeField, 4, 0xFFFFAA00, String.valueOf(saveData.getSpreadMaxRange()));
		
		graceTimeField = new TextFieldWidget(font, i + 43 + 60, j + 41, 32, 8, StringTextComponent.EMPTY);
		setupField(graceTimeField, 4, 0xFFFFAA00, String.valueOf(saveData.getGraceTime()));
		
        this.updateButtons();
	}

	public void initValues() {
		if (saveData == null)
            saveData = new UHCSaveData();
		
		this.uhcStarting = saveData.isUhcStarting();
        this.uhcOnGoing = saveData.isUhcOnGoing();
        this.teamsLocked = saveData.areTeamsLocked();
        	
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
		
		this.graceEnabled = saveData.isGraceEnabled();
		this.graceTime = saveData.getGraceTime();
	}
	
	public void setupField(TextFieldWidget field, int maxLength, int color, String text) {
		field.setFocused2(false);
		field.setCanLoseFocus(true);
		field.setMaxStringLength(maxLength);
		field.setText(text);
		field.setEnableBackgroundDrawing(false);
		field.setTextColor(color);
		textBoxList.add(field);
	}

	@Override
	public void tick() {
		if(this.currPage == 0) {
			if(randSizeField != null)
				randSizeField.tick();
			if(maxTeamSizeField != null)
				maxTeamSizeField.tick();
			if(difficultyField != null)
				difficultyField.tick();
		}
		
		if(this.currPage == 1) {
			if(borderSizeField != null)
				borderSizeField.tick();
			if(borderCenterXField != null)
				borderCenterXField.tick();
			if(borderCenterZField != null)
				borderCenterZField.tick();
			if(shrinkTimerField != null)
				shrinkTimerField.tick();
			if(shrinkSizeField != null)
				shrinkSizeField.tick();
			if(shrinkOvertimeField != null)
				shrinkOvertimeField.tick();
		}
		
		if(this.currPage == 2) {
			if(timeLockTimerField != null)
				timeLockTimerField.tick();
			if(minMarkTimerField != null)
				minMarkTimerField.tick();
			if(nameTimerField != null)
				nameTimerField.tick();
			if(glowTimerField != null)
				glowTimerField.tick();
		}
		
		if(this.currPage == 4) {
			if(maxHealthField != null)
				maxHealthField.tick();
			if(spreadDistanceField != null)
				spreadDistanceField.tick();
			if(spreadMaxRangeField != null)
				spreadMaxRangeField.tick();
		}
		
		if(this.currPage == 5) {
			if(graceTimeField != null)
				graceTimeField.tick();
		}

		syncData();

		super.tick();
	}
	
	public void syncData() {
		if(this.currPage == 0) {
			if(!randSizeField.getMessage().toString().equals(String.valueOf(saveData.getRandomTeamSize())) && !randSizeField.isFocused())
				randSizeField.setText(String.valueOf(saveData.getRandomTeamSize()));
			
			if(!maxTeamSizeField.getMessage().toString().equals(String.valueOf(saveData.getMaxTeamSize())) && !maxTeamSizeField.isFocused())
				maxTeamSizeField.setText(String.valueOf(saveData.getMaxTeamSize()));
			
			if(!difficultyField.getMessage().toString().equals(String.valueOf(saveData.getDifficulty())) && !difficultyField.isFocused())
				difficultyField.setText(String.valueOf(saveData.getDifficulty()));
			
			if(collisionButton.getBoolean() != saveData.isTeamCollision())
				collisionButton.setBoolean(saveData.isTeamCollision());
			
			if(damageButton.getBoolean() != saveData.isFriendlyFire())
				damageButton.setBoolean(saveData.isFriendlyFire());
			
			if(teamsLockedButton.getBoolean() != saveData.areTeamsLocked())
				teamsLockedButton.setBoolean(saveData.areTeamsLocked());
		}
		
		if(this.currPage == 1) {
			if(!borderSizeField.getMessage().toString().equals(String.valueOf(saveData.getBorderSize())) && !borderSizeField.isFocused())
				borderSizeField.setText(String.valueOf(saveData.getBorderSize()));
			
			if(!borderCenterXField.getMessage().toString().equals(String.valueOf(saveData.getBorderCenterX())) && !borderCenterXField.isFocused())
				borderCenterXField.setText(String.valueOf(saveData.getBorderCenterX()));
			
			if(!borderCenterZField.getMessage().toString().equals(String.valueOf(saveData.getBorderCenterZ())) && !borderCenterZField.isFocused())
				borderCenterZField.setText(String.valueOf(saveData.getBorderCenterZ()));
			
			if(shrinkButton.getBoolean() != saveData.isShrinkEnabled())
				shrinkButton.setBoolean(saveData.isShrinkEnabled());
			
			if(!shrinkTimerField.getMessage().toString().equals(String.valueOf(saveData.getShrinkTimer())) && !shrinkTimerField.isFocused())
				shrinkTimerField.setText(String.valueOf(saveData.getShrinkTimer()));
			
			if(!shrinkSizeField.getMessage().toString().equals(String.valueOf(saveData.getShrinkSize())) && !shrinkSizeField.isFocused())
				shrinkSizeField.setText(String.valueOf(saveData.getShrinkSize()));
			
			if(!shrinkOvertimeField.getMessage().toString().equals(String.valueOf(saveData.getShrinkOvertime())) && !shrinkOvertimeField.isFocused())
				shrinkOvertimeField.setText(String.valueOf(saveData.getShrinkOvertime()));
			
			if(!shrinkModeButton.getMessage().toString().equals(saveData.getShrinkMode()))
				shrinkModeButton.setMessage(new StringTextComponent(saveData.getShrinkMode()));
		}
		
		if(this.currPage == 2)
		{			
			if(timeLockButton.getBoolean() != saveData.isTimeLock())
				timeLockButton.setBoolean(saveData.isTimeLock());
				
			if(!Objects.equals(timeLockTimerField.getMessage().toString(), String.valueOf(saveData.getTimeLockTimer())) && !timeLockTimerField.isFocused())
				timeLockTimerField.setText(String.valueOf(saveData.getTimeLockTimer()));
			
			if(!timeModeButton.getMessage().toString().equals(saveData.getTimeMode()))
				timeModeButton.setMessage(new StringTextComponent(saveData.getTimeMode()));
			
			if(minuteMarkButton.getBoolean() != saveData.isMinuteMark())
				minuteMarkButton.setBoolean(saveData.isMinuteMark());
			
			if(!minMarkTimerField.getMessage().toString().equals(String.valueOf(saveData.getMinuteMarkTime())) && !minMarkTimerField.isFocused())
				minMarkTimerField.setText(String.valueOf(saveData.getMinuteMarkTime()));
			
			if(nameButton.getBoolean() != saveData.isTimedNames())
				nameButton.setBoolean(saveData.isTimedNames());
			
			if(!nameTimerField.getMessage().toString().equals(String.valueOf(saveData.getNameTimer())) && !nameTimerField.isFocused())
				nameTimerField.setText(String.valueOf(saveData.getNameTimer()));
			
			if(glowButton.getBoolean() != saveData.isTimedGlow())
				glowButton.setBoolean(saveData.isTimedGlow());
			
			if(!glowTimerField.getMessage().toString().equals(String.valueOf(saveData.getGlowTime())) && !glowTimerField.isFocused())
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
			
			if(!maxHealthField.getMessage().toString().equals(String.valueOf(saveData.getMaxHealth())) && !maxHealthField.isFocused())
				maxHealthField.setText(String.valueOf(saveData.getMaxHealth()));
			
			if(randomSpawnButton.getBoolean() != saveData.isRandomSpawns())
				randomSpawnButton.setBoolean(saveData.isRandomSpawns());
			
			if(!spreadDistanceField.getMessage().toString().equals(String.valueOf(saveData.getMaxHealth())) && !spreadDistanceField.isFocused())
				spreadDistanceField.setText(String.valueOf(saveData.getSpreadDistance()));
			
			if(!spreadMaxRangeField.getMessage().toString().equals(String.valueOf(saveData.getMaxHealth())) && !spreadMaxRangeField.isFocused())
				spreadMaxRangeField.setText(String.valueOf(saveData.getSpreadMaxRange()));
			
			if(spreadRespectTeamButton.getBoolean() != saveData.isSpreadRespectTeam())
				spreadRespectTeamButton.setBoolean(saveData.isSpreadRespectTeam());
		}
		
		if(this.currPage == 5)
		{
			if(graceTimeButton.getBoolean() != saveData.isGraceEnabled())
				graceTimeButton.setBoolean(saveData.isGraceEnabled());
			
			if(!graceTimeField.getMessage().toString().equals(String.valueOf(saveData.getGraceTime())) && !graceTimeField.isFocused())
				graceTimeField.setText(String.valueOf(saveData.getGraceTime()));
		}
	}
	
	/**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
	public void onClose() {
		this.minecraft.keyboardListener.enableRepeatEvents(false);
	}

	private void updateButtons() {
        this.buttonNextPage.visible = (this.currPage < this.bookTotalPages - 1);
        this.buttonPreviousPage.visible = this.currPage > 0;
        this.buttonDone.visible = true;

		for(ColorButton colorButton : colorButtons) {
			colorButton.visible = this.currPage == 0;
		}
    	
    	this.resetRandButton.visible = this.currPage == 0;
    	this.resetTeamSizeButton.visible = this.currPage == 0;
    	
    	this.collisionButton.visible = this.currPage == 0;
    	this.damageButton.visible = this.currPage == 0;
    	this.teamsLockedButton.visible = this.currPage == 0;
    	    	
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
    	this.shrinkModeButton.visible = this.currPage == 1;
    	
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
    	
    	this.graceTimeButton.visible = this.currPage == 5;
    	this.graceTimeField.setVisible(this.currPage == 5);
    	this.graceTimeField.setEnabled(this.currPage == 5);
    	this.UHCStartButton.visible = this.currPage == 5;
    }
    
    /**
     * Draws the screen and all the components in it.
     */
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.setListener((IGuiEventListener)null);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
        int i = (this.width - 192) / 2;
        int j = 2;
        this.blit(matrixStack, i, 2, 0, 0, 192, 192);

        String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);

        int j1 = this.font.getStringWidth(s4);
        this.font.drawString(matrixStack, s4, i - j1 + 192 - 44, 18, 0);
        
        List<IReorderingProcessor> ShrinkModeShrink = new ArrayList<>();
		ShrinkModeShrink.add(new TranslationTextComponent("book.uhc.explain.shrinkmodeshrink").func_241878_f());
		ShrinkModeShrink.add(new TranslationTextComponent("book.uhc.explain.shrinkmodeshrink2").func_241878_f());
		List<IReorderingProcessor> ShrinkModeArena = new ArrayList<>();
		ShrinkModeArena.add(new TranslationTextComponent("book.uhc.explain.shrinkmodearena").func_241878_f());
		ShrinkModeArena.add(new TranslationTextComponent("book.uhc.explain.shrinkmodearena2").func_241878_f());
		List<IReorderingProcessor> ShrinkModeControl = new ArrayList<>();
		ShrinkModeControl.add(new TranslationTextComponent("book.uhc.explain.shrinkmodecontrol").func_241878_f());
		ShrinkModeControl.add(new TranslationTextComponent("book.uhc.explain.shrinkmodecontrol2").func_241878_f());
		
		TranslationTextComponent minuteMessageString = new TranslationTextComponent("book.uhc.option.minutes");
		TranslationTextComponent locationString = new TranslationTextComponent("book.uhc.option.location");
		TranslationTextComponent resetString = new TranslationTextComponent("book.uhc.option.reset");
    	TranslationTextComponent TimeLockString = new TranslationTextComponent("book.uhc.option.timelock");
		TranslationTextComponent minMarkString = new TranslationTextComponent("book.uhc.option.minmark");
		TranslationTextComponent timedNameString = new TranslationTextComponent("book.uhc.option.timedname");
		TranslationTextComponent timedGlowString = new TranslationTextComponent("book.uhc.option.timedglow");
		TranslationTextComponent timeModeDayText = new TranslationTextComponent("book.uhc.option.timemodeday");
		TranslationTextComponent timeModeNightText = new TranslationTextComponent("book.uhc.option.timemodenight");
		TranslationTextComponent netherTravelString = new TranslationTextComponent("book.uhc.option.nether");
    	TranslationTextComponent regenPotionsString = new TranslationTextComponent("book.uhc.option.regenpotion");
		TranslationTextComponent level2PotionsString = new TranslationTextComponent("book.uhc.option.level2potion");
		TranslationTextComponent notchApplesString = new TranslationTextComponent("book.uhc.option.notchapples");
		TranslationTextComponent autoCookString = new TranslationTextComponent("book.uhc.option.autocook");
		TranslationTextComponent itemConvertString = new TranslationTextComponent("book.uhc.option.convertion");
		
		TranslationTextComponent weatherString = new TranslationTextComponent("book.uhc.option.weather");
		TranslationTextComponent mobGriefingString = new TranslationTextComponent("book.uhc.option.mobgriefing");
		TranslationTextComponent customHealthString = new TranslationTextComponent("book.uhc.option.customhealth");
		TranslationTextComponent randomSpawnString = new TranslationTextComponent("book.uhc.option.randomspawns");
		TranslationTextComponent spreadDistanceString = new TranslationTextComponent("book.uhc.option.spreaddistance");
		TranslationTextComponent spreadMaxRangeString = new TranslationTextComponent("book.uhc.option.spreadrange");
		TranslationTextComponent spreadRespectTeamString = new TranslationTextComponent("book.uhc.option.spreadteams");

		TranslationTextComponent graceString = new TranslationTextComponent("book.uhc.option.grace");

		super.render(matrixStack, mouseX, mouseY, partialTicks);
	    if(this.currPage == 0) {
	        TranslationTextComponent teamSelect = new TranslationTextComponent("book.uhc.team.select");
	        
	        TranslationTextComponent randSizeString = new TranslationTextComponent("book.uhc.option.randsize");
	        this.font.drawText(matrixStack, randSizeString, i + 43, j + 89, 0xFF555555);
			randSizeField.render(matrixStack, mouseX, mouseY, partialTicks);

	        
	        TranslationTextComponent maxTeamSizeString = new TranslationTextComponent("book.uhc.option.maxteams");
	        this.font.drawText(matrixStack, maxTeamSizeString, i + 43, j + 101, 0xFF555555);
			maxTeamSizeField.render(matrixStack, mouseX, mouseY, partialTicks);

			boolean flag = hoverBoolean(mouseX, mouseY, maxTeamSizeField.x, maxTeamSizeField.y, maxTeamSizeField.getWidth(), maxTeamSizeField.getHeight());
			TranslationTextComponent infinityString = new TranslationTextComponent("book.uhc.option.infinite");
			
			if(isColorNotHovered() && !flag && !teamsLockedButton.isHovered())
    	        this.font.drawText(matrixStack, teamSelect, i + 65, j + 28, 0xFF555555);
			
			if(flag && !maxTeamSizeField.isFocused())
		        drawCenteredString(matrixStack, font, infinityString, i + 91, j + 28, 0xFFFF5555);

			TranslationTextComponent lockButton = new TranslationTextComponent("book.uhc.option.locked");

			if(teamsLockedButton.isHovered())
		        drawCenteredString(matrixStack, font, lockButton, i + 93, j + 27, 0xFFFF5555);

			TranslationTextComponent teamCollisionString = new TranslationTextComponent("book.uhc.option.collision");
	        this.font.drawText(matrixStack, teamCollisionString, i + 43, j + 118, 0xFF555555);

			TranslationTextComponent teamDamageString = new TranslationTextComponent("book.uhc.option.damage");
	        this.font.drawText(matrixStack, teamDamageString, i + 43, j + 131, 0xFF555555);

			TranslationTextComponent difficultyString = new TranslationTextComponent("book.uhc.option.difficulty");
	        this.font.drawText(matrixStack, difficultyString, i + 43, j + 144, 0xFF555555);
			difficultyField.render(matrixStack, mouseX, mouseY, partialTicks);
	    }
	    
	    if(this.currPage == 1) {
	    	TranslationTextComponent borderSizeString = new TranslationTextComponent("book.uhc.option.bordersize");
	        this.font.drawText(matrixStack, borderSizeString, i + 48, j + 28, 0xFF555555);
			borderSizeField.render(matrixStack, mouseX, mouseY, partialTicks);
	        
	        TranslationTextComponent centerString = new TranslationTextComponent("book.uhc.option.bordercenter");
	        this.font.drawText(matrixStack, centerString, i + 42, j + 53, 0xFF555555);
	        
	        TranslationTextComponent centerxString = new TranslationTextComponent("book.uhc.option.bordercenterx");
	        this.font.drawText(matrixStack, centerxString, i + 42, j + 64, 0xFF555555);
			borderCenterXField.render(matrixStack, mouseX, mouseY, partialTicks);
	        
	        TranslationTextComponent centerZString = new TranslationTextComponent("book.uhc.option.bordercenterz");
	        this.font.drawText(matrixStack, centerZString, i + 42, j + 76, 0xFF555555);
			borderCenterZField.render(matrixStack, mouseX, mouseY, partialTicks);
	        
	        TranslationTextComponent ShrinkString = new TranslationTextComponent("book.uhc.option.shrinkenabled");
	        this.font.drawText(matrixStack, ShrinkString, i + 38, j + 94, 0xFF555555);
	        
	        TranslationTextComponent ShrinkTimerString = new TranslationTextComponent("book.uhc.option.shrinktimer");
	        this.font.drawText(matrixStack, ShrinkTimerString, i + 44, j + 107, 0xFF555555);
			shrinkTimerField.render(matrixStack, mouseX, mouseY, partialTicks);
	        
	        TranslationTextComponent ShrinkSizeString = new TranslationTextComponent("book.uhc.option.shrinksize");
	        this.font.drawText(matrixStack, ShrinkSizeString, i + 44, j + 118, 0xFF555555);
			shrinkSizeField.render(matrixStack, mouseX, mouseY, partialTicks);
	        
	        TranslationTextComponent ShrinkOvertimeString = new TranslationTextComponent("book.uhc.option.shrinkovertime");
	        this.font.drawText(matrixStack, ShrinkOvertimeString, i + 44, j + 129, 0xFF555555);
			shrinkOvertimeField.render(matrixStack, mouseX, mouseY, partialTicks);
	        
	        TranslationTextComponent ShrinkModeString = new TranslationTextComponent("book.uhc.option.shrinkmode");
	        this.font.drawText(matrixStack, ShrinkModeString, i + 44, j + 140, 0xFF555555);

			boolean flag = hoverBoolean(mouseX, mouseY, shrinkTimerField.x, shrinkTimerField.y, shrinkTimerField.getWidth(), shrinkTimerField.getHeight());
			boolean flag1 = hoverBoolean(mouseX, mouseY, shrinkOvertimeField.x, shrinkOvertimeField.y, shrinkOvertimeField.getWidth(), shrinkOvertimeField.getHeight());
			if((flag && !shrinkTimerField.isFocused()) || (flag1 && !shrinkOvertimeField.isFocused()))
		        drawCenteredString(matrixStack, font, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
	    }
	    
	    if(this.currPage == 2) {
	    	this.font.drawText(matrixStack, TimeLockString, i + 38, j + 28, 0xFF555555);
			timeLockTimerField.render(matrixStack, mouseX, mouseY, partialTicks);

	    	TranslationTextComponent TimeLockTimerString = new TranslationTextComponent("book.uhc.option.timelocktimer");
	    	this.font.drawText(matrixStack, TimeLockTimerString, i + 44, j + 41, 0xFF555555);
	    	
	    	TranslationTextComponent TimeLockModeString = new TranslationTextComponent("book.uhc.option.timelockmode");
	    	this.font.drawText(matrixStack, TimeLockModeString, i + 44, j + 53, 0xFF555555);

	    	this.font.drawText(matrixStack, minMarkString, i + 38, j + 68, 0xFF555555);

			TranslationTextComponent minMarkTimerString = new TranslationTextComponent("book.uhc.option.minmarktime");
	    	this.font.drawText(matrixStack, minMarkTimerString, i + 44, j + 80, 0xFF555555);
			minMarkTimerField.render(matrixStack, mouseX, mouseY, partialTicks);
			
	    	this.font.drawText(matrixStack, timedNameString, i + 38, j + 94, 0xFF555555);
	    	
	    	TranslationTextComponent timedNameTimerString = new TranslationTextComponent("book.uhc.option.timednametime");
	    	this.font.drawText(matrixStack, timedNameTimerString, i + 44, j + 106, 0xFF555555);
			nameTimerField.render(matrixStack, mouseX, mouseY, partialTicks);
			
	    	this.font.drawText(matrixStack, timedGlowString, i + 38, j + 122, 0xFF555555);
	    	
	    	TranslationTextComponent timedGlowStringTimerString = new TranslationTextComponent("book.uhc.option.timedglowtime");
	    	this.font.drawText(matrixStack, timedGlowStringTimerString, i + 44, j + 134, 0xFF555555);
			glowTimerField.render(matrixStack, mouseX, mouseY, partialTicks);
			
			boolean flag = hoverBoolean(mouseX, mouseY, timeLockTimerField.x, timeLockTimerField.y, timeLockTimerField.getWidth(), timeLockTimerField.getHeight());
			boolean flag1 = hoverBoolean(mouseX, mouseY, minMarkTimerField.x, minMarkTimerField.y, minMarkTimerField.getWidth(), minMarkTimerField.getHeight());
			if((flag && !timeLockTimerField.isFocused()) || (flag1 && !minMarkTimerField.isFocused()))
		        drawCenteredString(matrixStack, font, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
	    }
	    
	    if(this.currPage == 3) {
	        this.font.drawText(matrixStack, regenPotionsString, i+38, j+28, 0xFF555555);

	        this.font.drawText(matrixStack, level2PotionsString, i+38, j+40, 0xFF555555);

	        this.font.drawText(matrixStack, notchApplesString, i+38, j+52, 0xFF555555);
	        
	        this.font.drawText(matrixStack, autoCookString, i+38, j+68, 0xFF555555);
	        
	        this.font.drawText(matrixStack, itemConvertString, i+38, j+80, 0xFF555555);
	        
	        this.font.drawText(matrixStack, netherTravelString, i + 38, j + 98, 0xFF555555);

			TranslationTextComponent healthInTabString = new TranslationTextComponent("book.uhc.option.healthtab");
	        this.font.drawText(matrixStack, healthInTabString, i + 38, j + 114, 0xFF555555);
	        
			TranslationTextComponent healthOnSideString = new TranslationTextComponent("book.uhc.option.healthside");
	        this.font.drawText(matrixStack, healthOnSideString, i + 38, j + 126, 0xFF555555);

			TranslationTextComponent healthUnderNameString = new TranslationTextComponent("book.uhc.option.healthname");
	        this.font.drawText(matrixStack, healthUnderNameString, i + 38, j + 138, 0xFF555555);
	    }
	    
	    if(this.currPage == 4) {
	    	this.font.drawText(matrixStack, weatherString, i+38, j+28, 0xFF555555);
	    	
	    	this.font.drawText(matrixStack, mobGriefingString, i+38, j+40, 0xFF555555);
	    	
	    	this.font.drawText(matrixStack, customHealthString, i+38, j+57, 0xFF555555);
	    	
			TranslationTextComponent healthMaxString = new TranslationTextComponent("book.uhc.option.maxhealth");
	    	this.font.drawText(matrixStack, healthMaxString, i+44, j+69, 0xFF555555);
			maxHealthField.render(matrixStack, mouseX, mouseY, partialTicks);
	    	
	    	this.font.drawText(matrixStack, randomSpawnString, i+38, j+86, 0xFF555555);
	    	
	    	this.font.drawText(matrixStack, spreadDistanceString, i+44, j+98, 0xFF555555);
			spreadDistanceField.render(matrixStack, mouseX, mouseY, partialTicks);

	    	this.font.drawText(matrixStack, spreadMaxRangeString, i+44, j+110, 0xFF555555);
			spreadMaxRangeField.render(matrixStack, mouseX, mouseY, partialTicks);
	    	
	    	this.font.drawText(matrixStack, spreadRespectTeamString, i+44, j+122, 0xFF555555);

			TranslationTextComponent healthExplain = new TranslationTextComponent("book.uhc.explain.healthExplain");
	    	boolean flag2 = hoverBoolean(mouseX, mouseY, maxHealthField.x, maxHealthField.y, maxHealthField.getWidth(), maxHealthField.getHeight());
			if(flag2 && !maxHealthField.isFocused())
		        drawCenteredString(matrixStack, font, healthExplain, mouseX, mouseY + 5, 0xFFFF5555);
		}
	    
	    if(this.currPage == 5) {
	    	this.font.drawText(matrixStack, graceString, i+38, j+28, 0xFF555555);

			graceTimeField.render(matrixStack, mouseX, mouseY, partialTicks);
	    	
	    	TranslationTextComponent GraceTimerString = new TranslationTextComponent("book.uhc.option.gracetimer");
	    	this.font.drawText(matrixStack, GraceTimerString, i + 44, j + 41, 0xFF555555);
	    	
	    	boolean flag = hoverBoolean(mouseX, mouseY, graceTimeField.x, graceTimeField.y, graceTimeField.getWidth(), graceTimeField.getHeight());
			if(flag && !timeLockTimerField.isFocused())
		        drawCenteredString(matrixStack, font, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
	    }
	    
		if(shrinkModeButton.visible && shrinkModeButton.isHovered()) {
			TextComponent ShrinkMode = new StringTextComponent(saveData.getShrinkMode());
			if(ShrinkMode.toString().equals("Shrink")) {
				this.renderTooltip(matrixStack, ShrinkModeShrink, mouseX, mouseY);
			}
			if(ShrinkMode.toString().equals("Arena")) {
				this.renderTooltip(matrixStack, ShrinkModeArena, mouseX, mouseY);
			}
			if(ShrinkMode.toString().equals("Control")) {
				this.renderTooltip(matrixStack, ShrinkModeControl, mouseX, mouseY);
			}
		}
	    
	    if(timeModeButton.visible && timeModeButton.isHovered()) {
			TextComponent TimeMode = new StringTextComponent(saveData.getTimeMode());
			if(TimeMode.toString().equals("Day"))
		        this.renderTooltip(matrixStack, timeModeDayText, mouseX, mouseY);
			if(TimeMode.toString().equals("Night"))
		        this.renderTooltip(matrixStack, timeModeNightText, mouseX, mouseY);
		}

	    if(this.currPage == 2 && hoverBoolean(mouseX, mouseY, i+38, j+28, font.getStringPropertyWidth(TimeLockString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, new TranslationTextComponent("book.uhc.explain.timelock"), mouseX, mouseY);
	    }
	    
        List<IReorderingProcessor> MinuteMarkExplain = new ArrayList<>();
        MinuteMarkExplain.add(new TranslationTextComponent("book.uhc.explain.minmark").func_241878_f());
        MinuteMarkExplain.add(new TranslationTextComponent("book.uhc.explain.minmark2").func_241878_f());
        List<IReorderingProcessor> timedNameExplain = new ArrayList<>();
        timedNameExplain.add(new TranslationTextComponent("book.uhc.explain.timename").func_241878_f());
        timedNameExplain.add(new TranslationTextComponent("book.uhc.explain.timename2").func_241878_f());
        List<IReorderingProcessor> timedGlowExplain = new ArrayList<>();
        timedGlowExplain.add(new TranslationTextComponent("book.uhc.explain.timeglow").func_241878_f());
        timedGlowExplain.add(new TranslationTextComponent("book.uhc.explain.timeglow2").func_241878_f());
        
	    if(this.currPage == 2 && hoverBoolean(mouseX, mouseY, i+38, j+68, font.getStringPropertyWidth(minMarkString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, MinuteMarkExplain, mouseX, mouseY);
	    }
	    if(this.currPage == 2 && hoverBoolean(mouseX, mouseY, i+38, j+94, font.getStringPropertyWidth(timedNameString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, timedNameExplain, mouseX, mouseY);
	    }
	    if(this.currPage == 2 && hoverBoolean(mouseX, mouseY, i+38, j+122, font.getStringPropertyWidth(timedGlowString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, timedGlowExplain, mouseX, mouseY);
	    }
	    
        List<IReorderingProcessor> regenPotionExplain = new ArrayList<>();
        regenPotionExplain.add(new TranslationTextComponent("book.uhc.explain.regenpotion").func_241878_f());
        regenPotionExplain.add(new TranslationTextComponent("book.uhc.explain.regenpotion2").func_241878_f());
        List<IReorderingProcessor> level2PotionExplain = new ArrayList<>();
        level2PotionExplain.add(new TranslationTextComponent("book.uhc.explain.level2potion").func_241878_f());
        level2PotionExplain.add(new TranslationTextComponent("book.uhc.explain.level2potion2").func_241878_f());
        List<IReorderingProcessor> notchApplesExplain = new ArrayList<>();
        notchApplesExplain.add(new TranslationTextComponent("book.uhc.explain.notchapple").func_241878_f());
        notchApplesExplain.add(new TranslationTextComponent("book.uhc.explain.notchapple2").func_241878_f());
        
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+28, font.getStringPropertyWidth(regenPotionsString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, regenPotionExplain, mouseX, mouseY);
	    }
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+40, font.getStringPropertyWidth(level2PotionsString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, level2PotionExplain, mouseX, mouseY);
	    }
		if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+52, font.getStringPropertyWidth(notchApplesString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, notchApplesExplain, mouseX, mouseY);
	    }
	    
        List<IReorderingProcessor> autoSmeltExplain = new ArrayList<>();
        autoSmeltExplain.add(new TranslationTextComponent("book.uhc.explain.autocook").func_241878_f());
        autoSmeltExplain.add(new TranslationTextComponent("book.uhc.explain.autocook2").func_241878_f());
        autoSmeltExplain.add(new TranslationTextComponent("book.uhc.explain.autocook3").func_241878_f());
        autoSmeltExplain.add(new TranslationTextComponent("book.uhc.explain.autocook4").func_241878_f());
        List<IReorderingProcessor> itemConvertExplain = new ArrayList<>();
        itemConvertExplain.add(new TranslationTextComponent("book.uhc.explain.itemconvert").func_241878_f());
        itemConvertExplain.add(new TranslationTextComponent("book.uhc.explain.itemconvert2").func_241878_f());
        itemConvertExplain.add(new TranslationTextComponent("book.uhc.explain.itemconvert3").func_241878_f());
        itemConvertExplain.add(new TranslationTextComponent("book.uhc.explain.itemconvert4").func_241878_f());
        itemConvertExplain.add(new TranslationTextComponent("book.uhc.explain.itemconvert5").func_241878_f());
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+68, font.getStringPropertyWidth(autoCookString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, autoSmeltExplain, mouseX, mouseY);
	    }
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+38, j+80, font.getStringPropertyWidth(itemConvertString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, itemConvertExplain, mouseX, mouseY);
	    }
	    
        TranslationTextComponent netherTravelExplain = new TranslationTextComponent("book.uhc.explain.nether");
	    if(this.currPage == 3 && hoverBoolean(mouseX, mouseY, i+35, j+98, font.getStringPropertyWidth(netherTravelString), font.FONT_HEIGHT)) {
			this.renderTooltip(matrixStack, netherTravelExplain, mouseX, mouseY);
        }
	    
	    TranslationTextComponent weatherExplain = new TranslationTextComponent("book.uhc.explain.weather");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+38, j+28, font.getStringPropertyWidth(weatherString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, weatherExplain, mouseX, mouseY);
	    }
	    TranslationTextComponent mobGriefingExplain = new TranslationTextComponent("book.uhc.explain.mobgriefing");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+38, j+40, font.getStringPropertyWidth(mobGriefingString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, mobGriefingExplain, mouseX, mouseY);
	    }
	    TranslationTextComponent customHealthExplain = new TranslationTextComponent("book.uhc.explain.customhealth");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+38, j+57, font.getStringPropertyWidth(customHealthString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, customHealthExplain, mouseX, mouseY);
	    }
	    List<IReorderingProcessor> randomSpawnsExplain = new ArrayList<>();
	    randomSpawnsExplain.add(new TranslationTextComponent("book.uhc.explain.randomspawns").func_241878_f());
	    randomSpawnsExplain.add(new TranslationTextComponent("book.uhc.explain.randomspawns2").func_241878_f());
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+38, j+86, font.getStringPropertyWidth(randomSpawnString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, randomSpawnsExplain, mouseX, mouseY);
	    }
		TranslationTextComponent spreadDistanceExplain = new TranslationTextComponent("book.uhc.explain.spreaddistance");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+44, j+98, font.getStringPropertyWidth(spreadDistanceString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, spreadDistanceExplain, mouseX, mouseY);
	    }
		TranslationTextComponent spreadMaxRangeExplain = new TranslationTextComponent("book.uhc.explain.spreadrange");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+44, j+110, font.getStringPropertyWidth(spreadMaxRangeString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, spreadMaxRangeExplain, mouseX, mouseY);
	    }
		TranslationTextComponent spreadRespectTeamExplain = new TranslationTextComponent("book.uhc.explain.spreadteams");
	    if(this.currPage == 4 && hoverBoolean(mouseX, mouseY, i+44, j+122, font.getStringPropertyWidth(spreadRespectTeamString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, spreadRespectTeamExplain, mouseX, mouseY);
	    }
	    List<IReorderingProcessor> gracePeriodExplain = new ArrayList<>();
	    gracePeriodExplain.add(new TranslationTextComponent("book.uhc.explain.graceperiod").func_241878_f());
	    gracePeriodExplain.add(new TranslationTextComponent("book.uhc.explain.graceperiod2").func_241878_f());
	    if(this.currPage == 5 && hoverBoolean(mouseX, mouseY, i+38, j+28, font.getStringPropertyWidth(graceString), font.FONT_HEIGHT)) {
	    	this.renderTooltip(matrixStack, gracePeriodExplain, mouseX, mouseY);
	    }
    }
    
    public boolean hoverBoolean(int mouseX, int mouseY, int x, int y, int widthIn, int heigthIn) {
    	return mouseX >= x && mouseY >= y && mouseX < x + widthIn && mouseY < y + heigthIn;
    }
    
    public void sendTeamPacket(String team, String teamFormat, int indexColor) {
    	UHCPacketHandler.INSTANCE.sendToServer(new UHCPacketTeam(editingPlayer.getName(), team, teamFormat, indexColor));
    }
    
    public void sendTeamRandomizerPacket() {
    	UHCPacketHandler.INSTANCE.sendToServer(new UHCPacketTeamRandomizer());
    }
    
    public void sendPage1Packet() {
    	UHCPacketHandler.INSTANCE.sendToServer(new UHCPage1Packet(randomTeamSize, maxTeamSize, teamCollision, friendlyFire, difficulty, teamsLocked));
    }
    
    public void sendPage2Packet() {
    	UHCPacketHandler.INSTANCE.sendToServer(new UHCPage2Packet(borderSize, borderCenterX, borderCenterZ, shrinkEnabled, shrinkTimer, shrinkSize, shrinkOvertime, shrinkMode));
    }
    
    public void sendPage3Packet() {
    	UHCPacketHandler.INSTANCE.sendToServer(new UHCPage3Packet(timeLock, timeLockTimer, timeMode, minuteMark, minuteMarkTime, timedNames, nameTimer, timedGlow, glowTime));
    }
    
    public void sendPage4Packet() {
    	UHCPacketHandler.INSTANCE.sendToServer(new UHCPage4Packet(regenPotions, level2Potions, notchApples, autoCook, itemConversion, netherEnabled, healthInTab, healthOnSide, healthUnderName));
    }
    
    public void sendPage5Packet() {
    	UHCPacketHandler.INSTANCE.sendToServer(new UHCPage5Packet(weatherEnabled, mobGriefing, applyCustomHealth, maxHealth, randomSpawns, spreadDistance, spreadMaxRange, spreadRespectTeam));
    }
    
    public void sendPage6Packet() {
    	UHCPacketHandler.INSTANCE.sendToServer(new UHCPage6Packet(graceEnabled, graceTime));
    }
    
    public void startPacket() {
    	UHCPacketHandler.INSTANCE.sendToServer(new UHCStartPacket());
    }

    @Override
    public boolean isPauseScreen(){
        return false;
    }

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
    	int middleWidth = (this.width - 192) / 2;
        int middleHeigth = 2;
        
        if (mouseButton == 0) {
        	if(this.currPage == 0) {
        		if(randSizeField.mouseClicked(mouseX, mouseY, mouseButton))
            		randSizeField.setText("");
            	if(maxTeamSizeField.mouseClicked(mouseX, mouseY, mouseButton))
            		maxTeamSizeField.setText("");
            	if(difficultyField.mouseClicked(mouseX, mouseY, mouseButton))
            		difficultyField.setText("");
            	
            	if(!randSizeField.isFocused())
            		randSizeField.setText(String.valueOf(saveData.getRandomTeamSize()));
            	if(!maxTeamSizeField.isFocused())
            		maxTeamSizeField.setText(String.valueOf(saveData.getMaxTeamSize()));
            	if(!difficultyField.isFocused())
            		difficultyField.setText(String.valueOf(saveData.getDifficulty()));
        	}
        	
        	if(this.currPage == 1) {
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
            	
            	if(!borderSizeField.isFocused())
            		borderSizeField.setText(String.valueOf(saveData.getBorderSize()));
            	if(!borderCenterXField.isFocused())
            		borderCenterXField.setText(String.valueOf(saveData.getBorderCenterX()));
            	if(!borderCenterZField.isFocused())
            		borderCenterZField.setText(String.valueOf(saveData.getBorderCenterZ()));
            	if(!shrinkTimerField.isFocused())
            		shrinkTimerField.setText(String.valueOf(saveData.getShrinkTimer()));
            	if(!shrinkSizeField.isFocused())
            		shrinkSizeField.setText(String.valueOf(saveData.getShrinkSize()));
            	if(!shrinkOvertimeField.isFocused())
            		shrinkOvertimeField.setText(String.valueOf(saveData.getShrinkOvertime()));
        	}
        	
        	if(this.currPage == 2) {
        		if(timeLockTimerField.mouseClicked(mouseX, mouseY, mouseButton))
        			timeLockTimerField.setText("");
        		if(minMarkTimerField.mouseClicked(mouseX, mouseY, mouseButton))
        			minMarkTimerField.setText("");
        		if(nameTimerField.mouseClicked(mouseX, mouseY, mouseButton))
        			nameTimerField.setText("");
        		if(glowTimerField.mouseClicked(mouseX, mouseY, mouseButton))
        			glowTimerField.setText("");
        		
        		if(!timeLockTimerField.isFocused())
        			timeLockTimerField.setText(String.valueOf(saveData.getTimeLockTimer()));
        		if(!minMarkTimerField.isFocused())
        			minMarkTimerField.setText(String.valueOf(saveData.getMinuteMarkTime()));
        		if(!nameTimerField.isFocused())
        			nameTimerField.setText(String.valueOf(saveData.getNameTimer()));
        		if(!glowTimerField.isFocused())
        			glowTimerField.setText(String.valueOf(saveData.getGlowTime()));
        	}
        	
        	if(this.currPage == 4) {
        		if(maxHealthField.mouseClicked(mouseX, mouseY, mouseButton))
        			maxHealthField.setText("");
        		if(spreadDistanceField.mouseClicked(mouseX, mouseY, mouseButton))
        			spreadDistanceField.setText("");
        		if(spreadMaxRangeField.mouseClicked(mouseX, mouseY, mouseButton))
        			spreadMaxRangeField.setText("");
        		
        		if(!maxHealthField.isFocused())
        			maxHealthField.setText(String.valueOf(saveData.getMaxHealth()));
        		if(!spreadDistanceField.isFocused())
        			spreadDistanceField.setText(String.valueOf(saveData.getSpreadDistance()));
        		if(!spreadMaxRangeField.isFocused())
        			spreadMaxRangeField.setText(String.valueOf(saveData.getSpreadMaxRange()));
        	}
        	
        	if(this.currPage == 5) {
        		if(graceTimeField.mouseClicked(mouseX, mouseY, mouseButton))
        			graceTimeField.setText("");
        		
        		if(!graceTimeField.isFocused())
        			graceTimeField.setText(String.valueOf(saveData.getGraceTime()));
        	}
        }
        
    	return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

	@Override
    public boolean charTyped(char typedChar, int keyCode) {
    	boolean flag = super.charTyped(typedChar, keyCode);
    	
    	if(this.currPage == 0) {
    		if(randSizeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
        		randSizeField.charTyped(typedChar, keyCode);
    		}
    		if(maxTeamSizeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
        		maxTeamSizeField.charTyped(typedChar, keyCode);
    		}
    		if(difficultyField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			difficultyField.charTyped(typedChar, keyCode);
    		}
    		
    		if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
    			if(randSizeField.isFocused()) {
    				String randText = randSizeField.getMessage().toString();
    				
    				if(randText.isEmpty())
    					randSizeField.setText(String.valueOf(randomTeamSize));
    				else {
    					randomTeamSize = Integer.parseInt(randText);
    					sendPage1Packet();
    				}
    				
    				randSizeField.setFocused2(false);
    			}
    			
    			if(maxTeamSizeField.isFocused()) {
    				String teamSize = maxTeamSizeField.getMessage().toString();
    				
    				if(teamSize.isEmpty() || Integer.parseInt(teamSize) > 14)
    					maxTeamSizeField.setText(String.valueOf(maxTeamSize));
    				else {
    					maxTeamSize = Integer.parseInt(teamSize);
    					sendPage1Packet();
    				}
    				
    				maxTeamSizeField.setFocused2(false);
    			}
    			
    			if(difficultyField.isFocused()) {
    				String difficultyText = difficultyField.getMessage().toString();
    				
    				if(difficultyText.isEmpty() || Integer.parseInt(difficultyText) > 3)
    					difficultyField.setText(String.valueOf(difficulty));
    				else {
    					difficulty = Integer.parseInt(difficultyText);
    					sendPage1Packet();
    				}
    				
    				difficultyField.setFocused2(false);
    			}
    		}
    	}
    	
    	if(this.currPage == 1) {
    		if(borderSizeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			borderSizeField.charTyped(typedChar, keyCode);
    		}
    		if(borderCenterXField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE 
    				|| keyCode == GLFW.GLFW_KEY_PERIOD || keyCode == GLFW.GLFW_KEY_MINUS)) {
    			borderCenterXField.charTyped(typedChar, keyCode);
    		}
    		if(borderCenterZField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE 
    				|| keyCode == GLFW.GLFW_KEY_PERIOD || keyCode == GLFW.GLFW_KEY_MINUS)) {
    			borderCenterZField.charTyped(typedChar, keyCode);
    		}
    		if(shrinkTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			shrinkTimerField.charTyped(typedChar, keyCode);
    		}
    		if(shrinkSizeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			shrinkSizeField.charTyped(typedChar, keyCode);
    		}
    		if(shrinkOvertimeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			shrinkOvertimeField.charTyped(typedChar, keyCode);
    		}
    		
    		if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
    			/* Border Size Field */
    			if(borderSizeField.isFocused()) {
    				String borderSizeText = borderSizeField.getMessage().toString();
    				
    				if(borderSizeText.isEmpty())
    					borderSizeField.setText(String.valueOf(borderSize));
    				else {
    					borderSize = Integer.parseInt(borderSizeText);
    					sendPage2Packet();
    				}
    				
    				borderSizeField.setFocused2(false);
    			}
    			/* Border Center X Field */
    			if(borderCenterXField.isFocused()) {
    				String borderX = borderCenterXField.getMessage().toString();
    				
    				if(borderX.isEmpty())
    					borderCenterXField.setText(String.valueOf(borderCenterX));
    				else {
    					borderCenterX = Integer.parseInt(borderX);
    					sendPage2Packet();
    				}
    			
    				borderCenterXField.setFocused2(false);
    			}
    			/* Border Center Z Field */
    			if(borderCenterZField.isFocused()) {
    				String borderZ = borderCenterZField.getMessage().toString();
    				
    				if(borderZ.isEmpty())
    					borderCenterZField.setText(String.valueOf(borderCenterZ));
    				else {
    					borderCenterZ = Integer.parseInt(borderZ);
    					sendPage2Packet();
    				}
    			
    				borderCenterZField.setFocused2(false);
    			}
    			/* Shrink Timer Field */
    			if(shrinkTimerField.isFocused()) {
    				String shrinkTimerText = shrinkTimerField.getMessage().toString();
    				
    				if(shrinkTimerText.isEmpty())
    					shrinkTimerField.setText(String.valueOf(shrinkTimer));
    				else {
    					shrinkTimer = Integer.parseInt(shrinkTimerText);
    					sendPage2Packet();
    				}
    				
    				shrinkTimerField.setFocused2(false);
    			}
    			/* Shrink Size Field */
    			if(shrinkSizeField.isFocused()) {
    				String shrinkSizeText = shrinkSizeField.getMessage().toString();
    				
    				if(shrinkSizeText.isEmpty())
    					shrinkSizeField.setText(String.valueOf(shrinkSize));
    				else {
    					shrinkSize = Integer.parseInt(shrinkSizeText);
    					sendPage2Packet();
    				}
    				
    				shrinkSizeField.setFocused2(false);
    			}
    			/* Shrink Over Time Field */
    			if(shrinkOvertimeField.isFocused()) {
    				String ShrinkOverTimeText = shrinkOvertimeField.getMessage().toString();
    				
    				if(ShrinkOverTimeText.isEmpty())
    					shrinkOvertimeField.setText(String.valueOf(shrinkOvertime));
    				else {
    					shrinkOvertime = Integer.parseInt(ShrinkOverTimeText);
    					sendPage2Packet();
    				}
    				
    				shrinkOvertimeField.setFocused2(false);
    			}
    		}
    	}
    	if (this.currPage == 2) {
    		if(timeLockTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			timeLockTimerField.charTyped(typedChar, keyCode);
    		}
    		if(minMarkTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			minMarkTimerField.charTyped(typedChar, keyCode);
    		}
    		if(nameTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			nameTimerField.charTyped(typedChar, keyCode);
    		}
    		if(glowTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			glowTimerField.charTyped(typedChar, keyCode);
    		}
    		
    		if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
    			/* Shrink Timer Field */
    			if(timeLockTimerField.isFocused()) {
    				String timeLockTimerText = timeLockTimerField.getMessage().toString();
    				
    				if(timeLockTimerText.isEmpty())
    					timeLockTimerField.setText(String.valueOf(timeLockTimer));
    				else {
    					timeLockTimer = Integer.parseInt(timeLockTimerText);
    					sendPage3Packet();
    				}
    				
    				timeLockTimerField.setFocused2(false);
    			}
    			/* Minute Mark Timer Field */
    			if(minMarkTimerField.isFocused()) {
    				String minuteMarkTimerText = minMarkTimerField.getMessage().toString();
    				
    				if(minuteMarkTimerText.isEmpty())
    					minMarkTimerField.setText(String.valueOf(minuteMarkTime));
    				else {
    					minuteMarkTime = Integer.parseInt(minuteMarkTimerText);
    					sendPage3Packet();
    				}
    				
    				minMarkTimerField.setFocused2(false);
    			}
    			/* Name Timer Field */
    			if(nameTimerField.isFocused()) {
    				String nameTimerText = nameTimerField.getMessage().toString();
    				
    				if(nameTimerText.isEmpty())
    					nameTimerField.setText(String.valueOf(nameTimer));
    				else {
    					nameTimer = Integer.parseInt(nameTimerText);
    					sendPage3Packet();
    				}
    				
    				nameTimerField.setFocused2(false);
    			}
    			/* Glow Timer Field */
    			if(glowTimerField.isFocused()) {
    				String glowTimer = glowTimerField.getMessage().toString();
    				
    				if(glowTimer.isEmpty())
    					glowTimerField.setText(String.valueOf(glowTime));
    				else {
    					glowTime = Integer.parseInt(glowTimer);
    					sendPage3Packet();
    				}
    				
    				glowTimerField.setFocused2(false);
    			}
    		}
    	}
    	if (this.currPage == 4) {
    		if(maxHealthField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			maxHealthField.charTyped(typedChar, keyCode);
    		}
    		if(spreadDistanceField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			spreadDistanceField.charTyped(typedChar, keyCode);
    		}
    		if(spreadMaxRangeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			spreadMaxRangeField.charTyped(typedChar, keyCode);
    		}
    		
			if(keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Max Health Field */
				if(maxHealthField.isFocused()) {
					String maxHealthText = maxHealthField.getMessage().toString();
					
					if(maxHealthText.isEmpty())
						maxHealthField.setText(String.valueOf(maxHealth));
					else {
						maxHealth = Integer.parseInt(maxHealthText);
						sendPage5Packet();
					}
					
					maxHealthField.setFocused2(false);
				}
				/* Spread Distance Field */
				if(spreadDistanceField.isFocused()) {
					String maxDistanceText = spreadDistanceField.getMessage().toString();
					
					if(maxDistanceText.isEmpty())
						spreadDistanceField.setText(String.valueOf(spreadDistance));
					else {
						spreadDistance = Integer.parseInt(maxDistanceText);
						sendPage5Packet();
					}
					
					spreadDistanceField.setFocused2(false);
				}
				/* Spread Max Range Field */
				if(spreadMaxRangeField.isFocused()) {
					String maxRangeText = spreadMaxRangeField.getMessage().toString();
					
					if(maxRangeText.isEmpty())
						spreadMaxRangeField.setText(String.valueOf(spreadMaxRange));
					else {
						spreadMaxRange = Integer.parseInt(maxRangeText);
						sendPage5Packet();
					}
					
					spreadMaxRangeField.setFocused2(false);
				}
			}
    	}
    	if (this.currPage == 5) {
    		if(graceTimeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
    			graceTimeField.charTyped(typedChar, keyCode);
    		}
    		
    		if(keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Grace Timer Field */
				if(graceTimeField.isFocused()) {
					String graceTimeText = graceTimeField.getMessage().toString();
					
					if(graceTimeText.isEmpty())
						graceTimeField.setText(String.valueOf(graceTime));
					else {
						graceTime = Integer.parseInt(graceTimeText);
						sendPage6Packet();
					}
					
					graceTimeField.setFocused2(false);
				}
			}
    	}
		
		return flag;
    }
    
    public boolean charNumeric(char typedChar) {
    	return (typedChar >= '0' && typedChar <= '9');
    }

    public boolean isColorNotHovered() {
		for(ColorButton colorButton : colorButtons) {
			if(colorButton.isHovered())
				return false;
		}
		return true;
    }

	static class ResetButton extends Button {
		public ResetButton(int x, int y, Button.IPressable onPressIn) {
			super(x, y, 16, 13, StringTextComponent.EMPTY, onPressIn);
		}
		
		/**
		 * Draws this button to the screen.
		 */
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			int textureX = 0;
			int textureY = 218;
			if (this.isMouseOver(mouseX, mouseY))
				textureX += 16;

			blit(matrixStack, x, y,  textureX, textureY, 16, 13);
			if (this.isHovered()) {
				this.renderToolTip(matrixStack, mouseX, mouseY);
			}
		}
	}

	static class LocationButton extends Button {
		public LocationButton(int x, int y, Button.IPressable onPressIn) {
			super(x, y, 14, 13, StringTextComponent.EMPTY, onPressIn);
		}
		
		/**
		 * Draws this button to the screen.
		 */
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			int textureX = 0;
			int textureY = 232;
			if (this.isMouseOver(mouseX, mouseY))
				textureX += 15;

			blit(matrixStack, x, y,  textureX, textureY, 15, 13);
			if (this.isHovered()) {
				this.renderToolTip(matrixStack, mouseX, mouseY);
			}
		}
	}
	static class StartButton extends Button {

		public StartButton(int x, int y, Button.IPressable onPressIn) {
			super(x, y, 85, 22, StringTextComponent.EMPTY, onPressIn);
		}
		
		/**
		 * Draws this button to the screen.
		 */
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getTextureManager().bindTexture(BOOK_TEXTURE);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			int textureX = 30;
			int textureY = 232;
			if (this.isMouseOver(mouseX, mouseY))
				textureX += 85;

			blit(matrixStack, x, y,  textureX, textureY, 85, 22);
			if (this.isHovered()) {
				this.renderToolTip(matrixStack, mouseX, mouseY);
			}
		}
	}

	public class ColorButton extends Button {
		private final boolean solo;
		private final boolean randomize;
		public int color;
		public ITextComponent name;
		
		public int textX;
		public int textY;
		
		public ColorButton(int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, ITextComponent nameIn, Button.IPressable onPressIn) {
			super(x, y, widthIn, heightIn, StringTextComponent.EMPTY, onPressIn);
			this.textX = textXIn;
			this.textY = textYIn;
			this.color = colorIn;
			this.name = nameIn;
			this.solo = false;
			this.randomize = false;
		}
		
		public ColorButton(int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, ITextComponent nameIn, boolean soloIn, Button.IPressable onPressIn) {
			super(x, y, widthIn, heightIn, StringTextComponent.EMPTY, onPressIn);
			this.textX = textXIn;
			this.textY = textYIn;
			this.color = colorIn;
			this.name = nameIn;
			this.solo = soloIn;
			this.randomize = false;
		}
		
		public ColorButton(int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, ITextComponent nameIn, boolean soloIn, boolean randomizeIn, Button.IPressable onPressIn) {
			super(x, y, widthIn, heightIn, StringTextComponent.EMPTY, onPressIn);
			this.textX = textXIn;
			this.textY = textYIn;
			this.color = colorIn;
			this.name = nameIn;
			this.solo = false;
			this.randomize = randomizeIn;
		}
		
		@Override
		public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
	            FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
				this.isHovered = isMouseOver(mouseX, mouseY);
				
				RenderSystem.pushMatrix();
				RenderSystem.color4f(1, 1, 1, 1);
				
				String randomizeMessage = I18n.format("book.uhc.team.randomizer");
		        
				if(!this.solo && !this.randomize) {
					AbstractGui.fill(matrixStack, this.x, this.y, this.x + width, this.y + height , color);
				} else {
					AbstractGui.fill(matrixStack, this.x, this.y, this.x + width, this.y + height , 0xFF555555);
					AbstractGui.fill(matrixStack, this.x + 1, this.y + 1, this.x + width - 1, this.y + height - 1 , color);
					
					if(this.randomize)
						drawCenteredString(matrixStack, fontrenderer, randomizeMessage, textX - 6, textY, 0xFFFFFF55);
				}
				
				String joinMessage = I18n.format("book.uhc.team.hover", name.toString());
				if(this.randomize) {
					joinMessage = I18n.format("book.uhc.team.randomize");
				}

				if (this.isHovered) {
					if(this.randomize)
						drawCenteredString(matrixStack, fontrenderer, joinMessage, textX, textY - 44, 0xFFFF5555);
					else
						drawCenteredString(matrixStack, fontrenderer, joinMessage, textX, textY, color);
				}
			        
				
				RenderSystem.popMatrix();
	        }
		}
	}
}