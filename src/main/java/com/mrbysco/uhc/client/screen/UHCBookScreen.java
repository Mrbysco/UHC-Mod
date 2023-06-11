package com.mrbysco.uhc.client.screen;

import com.mrbysco.uhc.Reference;
import com.mrbysco.uhc.client.screen.widget.BooleanButton;
import com.mrbysco.uhc.client.screen.widget.ColorButton;
import com.mrbysco.uhc.client.screen.widget.LocationButton;
import com.mrbysco.uhc.client.screen.widget.LockButton;
import com.mrbysco.uhc.client.screen.widget.NumberEditbox;
import com.mrbysco.uhc.client.screen.widget.ResetButton;
import com.mrbysco.uhc.client.screen.widget.StartButton;
import com.mrbysco.uhc.client.screen.widget.TextButton;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UHCBookScreen extends Screen {
	/**
	 * Book texture
	 */
	public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/book.png");
	/**
	 * The player editing the UHC Settings
	 */
	private final Player editingPlayer;
	/**
	 * The total amount of pages
	 */
	private final int bookTotalPages = 6;
	private int currPage;

	protected final List<EditBox> textBoxList = new ArrayList<>();

	/**
	 * Buttons
	 */
	private PageButton buttonNextPage;
	private PageButton buttonPreviousPage;
	private Button buttonDone;

	private final ColorButton[] colorButtons = new ColorButton[17];

	private NumberEditbox randSizeField;
	private NumberEditbox maxTeamSizeField;

	private NumberEditbox borderSizeField, borderCenterXField, borderCenterZField, difficultyField;
	private NumberEditbox shrinkTimerField, shrinkSizeField, shrinkOvertimeField, timeLockTimerField, minMarkTimerField, nameTimerField, glowTimerField;
	private NumberEditbox maxHealthField, spreadDistanceField, spreadMaxRangeField;
	private NumberEditbox graceTimeField;
	private ResetButton resetRandButton, resetTeamSizeButton;
	private BooleanButton collisionButton, damageButton;
	private BooleanButton healthTabButton, healthSideButton, healthNameButton;

	private ResetButton resetBorderSizeButton, resetBorderCenterXButton, resetBorderCenterZButton;
	private LocationButton centerCurrentXButton, centerCurrentZButton;
	private ResetButton resetShrinkTimerButton, resetShrinkSizeButton, resetShrinkOverTimeButton;
	private TextButton shrinkModeButton;

	private BooleanButton shrinkButton;
	private BooleanButton timeLockButton;
	private TextButton timeModeButton;
	private ResetButton resetTimeLockTimerButton;
	private BooleanButton minuteMarkButton;
	private ResetButton resetMinuteMarkTimerButton;
	private BooleanButton nameButton;
	private ResetButton resetNameTimerButton;
	private BooleanButton glowButton;
	private ResetButton resetGlowTimerButton;

	private BooleanButton autoCookButton, itemConvertButton;
	private BooleanButton netherButton, regenPotionsButton, level2PotionsButton, notchApplesButton;
	private BooleanButton weatherButton, mobGriefingButton;
	private BooleanButton customHealthButton, randomSpawnButton, spreadRespectTeamButton;

	private BooleanButton graceTimeButton;
	private LockButton teamsLockedButton;

	private StartButton UHCStartButton;

	/**
	 * UHC save data
	 */
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

	private final CompoundTag playerData;

	public UHCBookScreen(Player player) {
		super(Component.literal("U H C Book"));
		this.editingPlayer = player;
		this.playerData = player.getPersistentData();
	}

	public static void openScreen(Player player) {
		Minecraft.getInstance().setScreen(new UHCBookScreen(player));
	}

	@Override
	public void init() {
		super.init();

		initValues();

		this.addRenderableWidget(this.buttonDone = new Button.Builder(Component.translatable("gui.done"), (button) -> {
			this.minecraft.setScreen((Screen) null);
		}).bounds(this.width / 2 - 100, 196, 200, 20).build());

		int i = (this.width - 192) / 2;
		int j = 2;

		this.buttonNextPage = this.addRenderableWidget(new PageButton(i + 120, 156, true, (button) -> {
			if (this.currPage < this.bookTotalPages - 1) {
				++this.currPage;
			}
			this.updateButtons();
		}, true));
		this.buttonPreviousPage = this.addRenderableWidget(new PageButton(i + 38, 156, false, (button) -> {
			if (this.currPage > 0) {
				--this.currPage;
			}
			this.updateButtons();
		}, true));

		this.colorButtons[0] = this.addRenderableWidget(new ColorButton(i + 43, j + 40, 10, 10, i + 92, j + 27, 0xFFAA0000,
				Component.translatable("color.darkred.name"), (button) -> {
			sendTeamPacket(ChatFormatting.DARK_RED.getName(), I18n.get("color.darkred.name"), ChatFormatting.DARK_RED.getId());
			this.updateButtons();
		}));
		this.colorButtons[1] = this.addRenderableWidget(new ColorButton(i + 43 + 15, j + 40, 10, 10, i + 92, j + 27, 0xFFFFAA00,
				Component.translatable("color.gold.name"), (button) -> {
			sendTeamPacket(ChatFormatting.GOLD.getName(), I18n.get("color.gold.name"), ChatFormatting.GOLD.getId());
			this.updateButtons();
		}));
		this.colorButtons[2] = this.addRenderableWidget(new ColorButton(i + 43 + 30, j + 40, 10, 10, i + 92, j + 27, 0xFF00AA00,
				Component.translatable("color.darkgreen.name"), (button) -> {
			sendTeamPacket(ChatFormatting.DARK_GREEN.getName(), I18n.get("color.darkgreen.name"), ChatFormatting.DARK_GREEN.getId());
			this.updateButtons();
		}));
		this.colorButtons[3] = this.addRenderableWidget(new ColorButton(i + 43 + 45, j + 40, 10, 10, i + 92, j + 27, 0xFF00AAAA,
				Component.translatable("color.darkaqua.name"), (button) -> {
			sendTeamPacket(ChatFormatting.DARK_AQUA.getName(), I18n.get("color.darkaqua.name"), ChatFormatting.DARK_AQUA.getId());
			this.updateButtons();
		}));
		this.colorButtons[4] = this.addRenderableWidget(new ColorButton(i + 43 + 60, j + 40, 10, 10, i + 92, j + 27, 0xFF0000AA,
				Component.translatable("color.darkblue.name"), (button) -> {
			sendTeamPacket(ChatFormatting.DARK_BLUE.getName(), I18n.get("color.darkblue.name"), ChatFormatting.DARK_BLUE.getId());
			this.updateButtons();
		}));
		this.colorButtons[5] = this.addRenderableWidget(new ColorButton(i + 43 + 75, j + 40, 10, 10, i + 92, j + 27, 0xFFAA00AA,
				Component.translatable("color.darkpurple.name"), (button) -> {
			sendTeamPacket(ChatFormatting.DARK_PURPLE.getName(), I18n.get("color.darkpurple.name"), ChatFormatting.DARK_PURPLE.getId());
			this.updateButtons();
		}));
		this.colorButtons[6] = this.addRenderableWidget(new ColorButton(i + 43 + 90, j + 40, 10, 10, i + 92, j + 27, 0xFF555555,
				Component.translatable("color.darkgray.name"), (button) -> {
			sendTeamPacket(ChatFormatting.DARK_GRAY.getName(), I18n.get("color.darkgray.name"), ChatFormatting.DARK_GRAY.getId());
			this.updateButtons();
		}));
		this.colorButtons[7] = this.addRenderableWidget(new ColorButton(i + 43, j + 55, 10, 10, i + 92, j + 27, 0xFFFF5555,
				Component.translatable("color.red.name"), (button) -> {
			sendTeamPacket(ChatFormatting.RED.getName(), I18n.get("color.red.name"), ChatFormatting.RED.getId());
			this.updateButtons();
		}));
		this.colorButtons[8] = this.addRenderableWidget(new ColorButton(i + 43 + 15, j + 55, 10, 10, i + 92, j + 27, 0xFFFFFF55,
				Component.translatable("color.yellow.name"), (button) -> {
			sendTeamPacket(ChatFormatting.YELLOW.getName(), I18n.get("color.yellow.name"), ChatFormatting.YELLOW.getId());
			this.updateButtons();
		}));
		this.colorButtons[9] = this.addRenderableWidget(new ColorButton(i + 43 + 30, j + 55, 10, 10, i + 92, j + 27, 0xFF55FF55,
				Component.translatable("color.green.name"), (button) -> {
			sendTeamPacket(ChatFormatting.GREEN.getName(), I18n.get("color.green.name"), ChatFormatting.GREEN.getId());
			this.updateButtons();
		}));
		this.colorButtons[10] = this.addRenderableWidget(new ColorButton(i + 43 + 45, j + 55, 10, 10, i + 92, j + 27, 0xFF55FFFF,
				Component.translatable("color.aqua.name"), (button) -> {
			sendTeamPacket(ChatFormatting.AQUA.getName(), I18n.get("color.aqua.name"), ChatFormatting.AQUA.getId());
			this.updateButtons();
		}));
		this.colorButtons[11] = this.addRenderableWidget(new ColorButton(i + 43 + 60, j + 55, 10, 10, i + 92, j + 27, 0xFF5555FF,
				Component.translatable("color.blue.name"), (button) -> {
			sendTeamPacket(ChatFormatting.BLUE.getName(), I18n.get("color.blue.name"), ChatFormatting.BLUE.getId());
			this.updateButtons();
		}));
		this.colorButtons[12] = this.addRenderableWidget(new ColorButton(i + 43 + 75, j + 55, 10, 10, i + 92, j + 27, 0xFFFF55FF,
				Component.translatable("color.lightpurple.name"), (button) -> {
			sendTeamPacket(ChatFormatting.LIGHT_PURPLE.getName(), I18n.get("color.lightpurple.name"), ChatFormatting.LIGHT_PURPLE.getId());
			this.updateButtons();
		}));
		this.colorButtons[13] = this.addRenderableWidget(new ColorButton(i + 43 + 90, j + 55, 10, 10, i + 92, j + 27, 0xFFAAAAAA,
				Component.translatable("color.gray.name"), (button) -> {
			sendTeamPacket(ChatFormatting.GRAY.getName(), I18n.get("color.gray.name"), ChatFormatting.GRAY.getId());
			this.updateButtons();
		}));
		this.colorButtons[14] = this.addRenderableWidget(new ColorButton(i + 43, j + 70, 10, 10, i + 92, j + 28, 0xFF000000,
				Component.translatable("color.black.name"), (button) -> {
			sendTeamPacket("spectator", I18n.get("color.black.name"), ChatFormatting.BLACK.getId());
			this.updateButtons();
		}));
		this.colorButtons[15] = this.addRenderableWidget(new ColorButton(i + 43 + 90, j + 70, 10, 10, i + 92, j + 28, 0xFFFFFFFF,
				Component.translatable("color.white.name"), true, (button) -> {
			sendTeamPacket("solo", I18n.get("color.white.name"), ChatFormatting.WHITE.getId());
			this.updateButtons();
		}));
		this.colorButtons[16] = this.addRenderableWidget(new ColorButton(i + 43 + 15, j + 70, 56, 10, i + 92, j + 71, 0xFFAAAAAA,
				Component.translatable("color.random"), false, true, (button) -> {
			sendTeamRandomizerPacket();
			this.updateButtons();
		}));

		this.resetRandButton = this.addRenderableWidget(new ResetButton(i + 43 + 94, j + 85, (button) -> {
			randomTeamSize = 6;
			sendPage1Packet();
			this.updateButtons();
		}));
		this.resetRandButton.setTooltip(Tooltip.create(Reference.resetString));
		this.resetTeamSizeButton = this.addRenderableWidget(new ResetButton(i + 43 + 94, j + 99, (button) -> {
			maxTeamSize = -1;
			sendPage1Packet();
			this.updateButtons();
		}));
		this.resetTeamSizeButton.setTooltip(Tooltip.create(Reference.resetString));
		this.collisionButton = this.addRenderableWidget(new BooleanButton(i + 43 + 74, j + 113, saveData.isTeamCollision(), (button) -> {
			boolean flag = saveData.isTeamCollision();
			teamCollision = !flag;
			sendPage1Packet();
			this.updateButtons();
		}));
		this.damageButton = this.addRenderableWidget(new BooleanButton(i + 43 + 74, j + 127, saveData.isFriendlyFire(), (button) -> {
			boolean flag = saveData.isFriendlyFire();
			friendlyFire = !flag;
			sendPage1Packet();
			this.updateButtons();
		}));

		this.resetBorderSizeButton = this.addRenderableWidget(new ResetButton(i + 43 + 68, j + 36, (button) -> {
			borderSize = 2048;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetBorderSizeButton.setTooltip(Tooltip.create(Reference.resetString));
		this.resetBorderCenterXButton = this.addRenderableWidget(new ResetButton(i + 43 + 92, j + 60, (button) -> {
			borderCenterX = originalBorderCenterX;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetBorderCenterXButton.setTooltip(Tooltip.create(Reference.resetString));
		this.resetBorderCenterZButton = this.addRenderableWidget(new ResetButton(i + 43 + 92, j + 74, (button) -> {
			borderCenterZ = originalBorderCenterZ;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetBorderCenterZButton.setTooltip(Tooltip.create(Reference.resetString));
		this.centerCurrentXButton = this.addRenderableWidget(new LocationButton(i + 43 + 76, j + 60, (button) -> {
			double playerX = editingPlayer.getX();
			borderCenterX = playerX;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.centerCurrentZButton = this.addRenderableWidget(new LocationButton(i + 43 + 76, j + 74, (button) -> {
			double playerZ = editingPlayer.getZ();
			borderCenterZ = playerZ;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.shrinkButton = this.addRenderableWidget(new BooleanButton(i + 43 + 70, j + 90, saveData.isShrinkEnabled(), (button) -> {
			boolean flag = saveData.isShrinkEnabled();
			shrinkEnabled = !flag;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetShrinkTimerButton = this.addRenderableWidget(new ResetButton(i + 43 + 92, j + 104, (button) -> {
			shrinkTimer = 60;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetShrinkTimerButton.setTooltip(Tooltip.create(Reference.resetString));
		this.resetShrinkSizeButton = this.addRenderableWidget(new ResetButton(i + 43 + 92, j + 116, (button) -> {
			shrinkSize = 256;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetShrinkSizeButton.setTooltip(Tooltip.create(Reference.resetString));
		this.resetShrinkOverTimeButton = this.addRenderableWidget(new ResetButton(i + 43 + 92, j + 128, (button) -> {
			shrinkOvertime = 60;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetShrinkOverTimeButton.setTooltip(Tooltip.create(Reference.resetString));
		this.shrinkModeButton = this.addRenderableWidget(new TextButton(i + 43 + 31, j + 140,
				Component.literal(saveData.getShrinkMode()), minecraft, (button) -> {
			if (saveData.getShrinkMode().equals("Shrink")) {
				shrinkMode = "Arena";
				sendPage2Packet();
			}
			if (saveData.getShrinkMode().equals("Arena")) {
				shrinkMode = "Control";
				sendPage2Packet();
			}
			if (saveData.getShrinkMode().equals("Control")) {
				shrinkMode = "Shrink";
				sendPage2Packet();
			}
			this.updateButtons();
		}));

		this.timeLockButton = this.addRenderableWidget(new BooleanButton(i + 43 + 60, j + 25, saveData.isTimeLock(), (button) -> {
			boolean flag = saveData.isTimeLock();
			timeLock = !flag;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.timeModeButton = this.addRenderableWidget(new TextButton(i + 43 + 32, j + 52, Component.literal(saveData.getTimeMode()), minecraft, (button) -> {
			if (saveData.getTimeMode().equals("Day")) {
				timeMode = "Night";
				sendPage3Packet();
			}
			if (saveData.getTimeMode().equals("Night")) {
				timeMode = "Day";
				sendPage3Packet();
			}
			this.updateButtons();
		}));
		this.resetTimeLockTimerButton = this.addRenderableWidget(new ResetButton(i + 43 + 80, j + 38, (button) -> {
			timeLockTimer = 60;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.resetTimeLockTimerButton.setTooltip(Tooltip.create(Reference.resetString));
		this.resetMinuteMarkTimerButton = this.addRenderableWidget(new ResetButton(i + 43 + 80, j + 77, (button) -> {
			minuteMarkTime = 30;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.resetMinuteMarkTimerButton.setTooltip(Tooltip.create(Reference.resetString));
		this.minuteMarkButton = this.addRenderableWidget(new BooleanButton(i + 43 + 60, j + 64, saveData.isMinuteMark(), (button) -> {
			boolean flag = saveData.isMinuteMark();
			minuteMark = !flag;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.nameButton = this.addRenderableWidget(new BooleanButton(i + 43 + 60, j + 91, saveData.isTimedNames(), (button) -> {
			boolean flag = saveData.isTimedNames();
			timedNames = !flag;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.resetNameTimerButton = this.addRenderableWidget(new ResetButton(i + 43 + 80, j + 103, (button) -> {
			nameTimer = 30;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.resetNameTimerButton.setTooltip(Tooltip.create(Reference.resetString));
		this.glowButton = this.addRenderableWidget(new BooleanButton(i + 43 + 60, j + 119, saveData.isTimedGlow(), (button) -> {
			boolean flag = saveData.isTimedGlow();
			timedGlow = !flag;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.resetGlowTimerButton = this.addRenderableWidget(new ResetButton(i + 43 + 80, j + 131, (button) -> {
			glowTime = 30;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.resetGlowTimerButton.setTooltip(Tooltip.create(Reference.resetString));


		this.healthTabButton = this.addRenderableWidget(new BooleanButton(i + 43 + 90, j + 109, saveData.isHealthInTab(), (button) -> {
			if (!saveData.isHealthInTab()) {
				healthInTab = true;
				healthOnSide = false;
				healthUnderName = false;
				sendPage4Packet();
				this.updateButtons();
			}
		}));
		this.healthSideButton = this.addRenderableWidget(new BooleanButton(i + 43 + 90, j + 122, saveData.isHealthOnSide(), (button) -> {
			if (!saveData.isHealthOnSide()) {
				healthInTab = false;
				healthOnSide = true;
				healthUnderName = false;
				sendPage4Packet();
				this.updateButtons();
			}
		}));
		this.healthNameButton = this.addRenderableWidget(new BooleanButton(i + 43 + 90, j + 135, saveData.isHealthUnderName(), (button) -> {
			if (!saveData.isHealthUnderName()) {
				healthInTab = false;
				healthOnSide = false;
				healthUnderName = true;
				sendPage4Packet();
				this.updateButtons();
			}
		}));
		this.netherButton = this.addRenderableWidget(new BooleanButton(i + 43 + 90, j + 94, saveData.isNetherEnabled(), (button) -> {
			boolean flag = saveData.isNetherEnabled();
			netherEnabled = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
		this.regenPotionsButton = this.addRenderableWidget(new BooleanButton(i + 43 + 90, j + 23, saveData.isRegenPotions(), (button) -> {
			boolean flag = saveData.isRegenPotions();
			regenPotions = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
		this.level2PotionsButton = this.addRenderableWidget(new BooleanButton(i + 43 + 90, j + 35, saveData.isLevel2Potions(), (button) -> {
			boolean flag = saveData.isLevel2Potions();
			level2Potions = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
		this.notchApplesButton = this.addRenderableWidget(new BooleanButton(i + 43 + 90, j + 48, saveData.isNotchApples(), (button) -> {
			boolean flag = saveData.isNotchApples();
			notchApples = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
		this.autoCookButton = this.addRenderableWidget(new BooleanButton(i + 43 + 90, j + 63, saveData.isAutoCookEnabled(), (button) -> {
			boolean flag = saveData.isAutoCookEnabled();
			autoCook = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));
		this.itemConvertButton = this.addRenderableWidget(new BooleanButton(i + 43 + 90, j + 77, saveData.isItemConversion(), (button) -> {
			boolean flag = saveData.isItemConversion();
			itemConversion = !flag;
			sendPage4Packet();
			this.updateButtons();
		}));

		this.weatherButton = this.addRenderableWidget(new BooleanButton(i + 43 + 80, j + 23, saveData.isWeatherEnabled(), (button) -> {
			boolean flag = saveData.isWeatherEnabled();
			weatherEnabled = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));
		this.mobGriefingButton = this.addRenderableWidget(new BooleanButton(i + 43 + 80, j + 35, saveData.isMobGriefing(), (button) -> {
			boolean flag = saveData.isMobGriefing();
			mobGriefing = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));
		this.customHealthButton = this.addRenderableWidget(new BooleanButton(i + 43 + 80, j + 54, saveData.isMobGriefing(), (button) -> {
			boolean flag = saveData.isApplyCustomHealth();
			applyCustomHealth = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));
		this.randomSpawnButton = this.addRenderableWidget(new BooleanButton(i + 43 + 80, j + 81, saveData.isRandomSpawns(), (button) -> {
			boolean flag = saveData.isRandomSpawns();
			randomSpawns = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));
		this.spreadRespectTeamButton = this.addRenderableWidget(new BooleanButton(i + 43 + 80, j + 118, saveData.isSpreadRespectTeam(), (button) -> {
			boolean flag = saveData.isSpreadRespectTeam();
			spreadRespectTeam = !flag;
			sendPage5Packet();
			this.updateButtons();
		}));

		this.graceTimeButton = this.addRenderableWidget(new BooleanButton(i + 43 + 64, j + 25, saveData.isGraceEnabled(), (button) -> {
			boolean flag = saveData.isGraceEnabled();
			graceEnabled = !flag;
			sendPage6Packet();
			this.updateButtons();
		}));
		this.teamsLockedButton = this.addRenderableWidget(new LockButton(i + 43 + 73, j + 68, saveData.areTeamsLocked(), (button) -> {
			boolean flag = saveData.areTeamsLocked();
			teamsLocked = !flag;
			sendPage1Packet();
			this.updateButtons();
		}));

		this.UHCStartButton = this.addRenderableWidget(new StartButton(i + 43 + 7, j + 132, (button) -> {
			startPacket();
			this.minecraft.setScreen((Screen) null);
		}));

		randSizeField = new NumberEditbox(font, i + 43 + 75, j + 89, 20, 8, Component.empty(), 0);
		setupField(randSizeField, 2, 0xFFFFAA00, String.valueOf(saveData.getRandomTeamSize()));

		maxTeamSizeField = new NumberEditbox(font, i + 43 + 75, j + 101, 20, 8, Component.empty(), 0);
		setupField(maxTeamSizeField, 2, 0xFFFFAA00, String.valueOf(saveData.getMaxTeamSize()));

		borderSizeField = new NumberEditbox(font, i + 43 + 36, j + 40, 32, 8, Component.empty(), 0);
		setupField(borderSizeField, 4, 0xFFFFAA00, String.valueOf(saveData.getBorderSize()));

		borderCenterXField = new NumberEditbox(font, i + 55, j + 64, 42, 8, Component.empty(), true);
		setupField(borderCenterXField, 6, 0xFFFFAA00, String.valueOf(saveData.getBorderCenterX()));

		borderCenterZField = new NumberEditbox(font, i + 55, j + 76, 42, 8, Component.empty(), true);
		setupField(borderCenterZField, 6, 0xFFFFAA00, String.valueOf(saveData.getBorderCenterZ()));

		difficultyField = new NumberEditbox(font, i + 43 + 52, j + 144, 14, 8, Component.empty(), 0);
		setupField(difficultyField, 1, 0xFFFFAA00, String.valueOf(saveData.getDifficulty()));

		shrinkTimerField = new NumberEditbox(font, i + 43 + 52, j + 107, 32, 8, Component.empty(), 0);
		setupField(shrinkTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkTimer()), Reference.minuteMessageString);

		shrinkSizeField = new NumberEditbox(font, i + 43 + 28, j + 118, 32, 8, Component.empty(), 0);
		setupField(shrinkSizeField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkSize()));

		shrinkOvertimeField = new NumberEditbox(font, i + 43 + 32, j + 129, 32, 8, Component.empty(), 0);
		setupField(shrinkOvertimeField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkOvertime()), Reference.minuteMessageString);

		timeLockTimerField = new NumberEditbox(font, i + 43 + 52, j + 41, 32, 8, Component.empty(), 0);
		setupField(timeLockTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getTimeLockTimer()), Reference.minuteMessageString);

		minMarkTimerField = new NumberEditbox(font, i + 43 + 38, j + 80, 32, 8, Component.empty(), 0);
		setupField(minMarkTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getMinuteMarkTime()), Reference.minuteMessageString);

		nameTimerField = new NumberEditbox(font, i + 43 + 38, j + 106, 32, 8, Component.empty(), 0);
		setupField(nameTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getNameTimer()));

		glowTimerField = new NumberEditbox(font, i + 43 + 38, j + 133, 32, 8, Component.empty(), 0);
		setupField(glowTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getGlowTime()));

		maxHealthField = new NumberEditbox(font, i + 43 + 60, j + 69, 32, 8, Component.empty(), 0);
		setupField(maxHealthField, 4, 0xFFFFAA00, String.valueOf(saveData.getMaxHealth()));

		spreadDistanceField = new NumberEditbox(font, i + 43 + 60, j + 98, 32, 8, Component.empty(), 0);
		setupField(spreadDistanceField, 4, 0xFFFFAA00, String.valueOf(saveData.getSpreadDistance()));

		spreadMaxRangeField = new NumberEditbox(font, i + 43 + 60, j + 110, 32, 8, Component.empty(), 0);
		setupField(spreadMaxRangeField, 4, 0xFFFFAA00, String.valueOf(saveData.getSpreadMaxRange()));

		graceTimeField = new NumberEditbox(font, i + 43 + 60, j + 41, 32, 8, Component.empty(), 0);
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

		this.doDaylightCycle = saveData.doDaylightCycle();
		this.autoCook = saveData.isAutoCookEnabled();
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

	public void setupField(NumberEditbox field, int maxLength, int textColor, String initialValue,
						   Component tooltipComponent) {
		field.setFocused(false);
		field.setCanLoseFocus(true);
		field.setMaxLength(maxLength);
		field.setValue(initialValue);
		field.setBordered(false);
		field.setTextColor(textColor);
		field.setTooltip(Tooltip.create(tooltipComponent));
		textBoxList.add(field);
	}

	public void setupField(NumberEditbox field, int maxLength, int textColor, String initialValue) {
		field.setFocused(false);
		field.setCanLoseFocus(true);
		field.setMaxLength(maxLength);
		field.setValue(initialValue);
		field.setBordered(false);
		field.setTextColor(textColor);
		textBoxList.add(field);
	}

	@Override
	public void tick() {
		if (this.currPage == 0) {
			if (randSizeField != null)
				randSizeField.tick();
			if (maxTeamSizeField != null)
				maxTeamSizeField.tick();
			if (difficultyField != null)
				difficultyField.tick();
		}

		if (this.currPage == 1) {
			if (borderSizeField != null)
				borderSizeField.tick();
			if (borderCenterXField != null)
				borderCenterXField.tick();
			if (borderCenterZField != null)
				borderCenterZField.tick();
			if (shrinkTimerField != null)
				shrinkTimerField.tick();
			if (shrinkSizeField != null)
				shrinkSizeField.tick();
			if (shrinkOvertimeField != null)
				shrinkOvertimeField.tick();
		}

		if (this.currPage == 2) {
			if (timeLockTimerField != null)
				timeLockTimerField.tick();
			if (minMarkTimerField != null)
				minMarkTimerField.tick();
			if (nameTimerField != null)
				nameTimerField.tick();
			if (glowTimerField != null)
				glowTimerField.tick();
		}

		if (this.currPage == 4) {
			if (maxHealthField != null)
				maxHealthField.tick();
			if (spreadDistanceField != null)
				spreadDistanceField.tick();
			if (spreadMaxRangeField != null)
				spreadMaxRangeField.tick();
		}

		if (this.currPage == 5) {
			if (graceTimeField != null)
				graceTimeField.tick();
		}

		syncData();

		super.tick();
	}

	public void syncData() {
		if (this.currPage == 0) {
			if (!randSizeField.getValue().equals(String.valueOf(saveData.getRandomTeamSize())) && !randSizeField.isFocused())
				randSizeField.setValue(String.valueOf(saveData.getRandomTeamSize()));

			if (!maxTeamSizeField.getValue().equals(String.valueOf(saveData.getMaxTeamSize())) && !maxTeamSizeField.isFocused())
				maxTeamSizeField.setValue(String.valueOf(saveData.getMaxTeamSize()));

			if (!difficultyField.getValue().equals(String.valueOf(saveData.getDifficulty())) && !difficultyField.isFocused())
				difficultyField.setValue(String.valueOf(saveData.getDifficulty()));

			if (collisionButton.getBoolean() != saveData.isTeamCollision())
				collisionButton.setBoolean(saveData.isTeamCollision());

			if (damageButton.getBoolean() != saveData.isFriendlyFire())
				damageButton.setBoolean(saveData.isFriendlyFire());

			if (teamsLockedButton.getBoolean() != saveData.areTeamsLocked())
				teamsLockedButton.setBoolean(saveData.areTeamsLocked());
		}

		if (this.currPage == 1) {
			if (!borderSizeField.getValue().equals(String.valueOf(saveData.getBorderSize())) && !borderSizeField.isFocused())
				borderSizeField.setValue(String.valueOf(saveData.getBorderSize()));

			if (!borderCenterXField.getValue().equals(String.valueOf(saveData.getBorderCenterX())) && !borderCenterXField.isFocused())
				borderCenterXField.setValue(String.valueOf(saveData.getBorderCenterX()));

			if (!borderCenterZField.getValue().equals(String.valueOf(saveData.getBorderCenterZ())) && !borderCenterZField.isFocused())
				borderCenterZField.setValue(String.valueOf(saveData.getBorderCenterZ()));

			if (shrinkButton.getBoolean() != saveData.isShrinkEnabled())
				shrinkButton.setBoolean(saveData.isShrinkEnabled());

			if (!shrinkTimerField.getValue().equals(String.valueOf(saveData.getShrinkTimer())) && !shrinkTimerField.isFocused())
				shrinkTimerField.setValue(String.valueOf(saveData.getShrinkTimer()));

			if (!shrinkSizeField.getValue().equals(String.valueOf(saveData.getShrinkSize())) && !shrinkSizeField.isFocused())
				shrinkSizeField.setValue(String.valueOf(saveData.getShrinkSize()));

			if (!shrinkOvertimeField.getValue().equals(String.valueOf(saveData.getShrinkOvertime())) && !shrinkOvertimeField.isFocused())
				shrinkOvertimeField.setValue(String.valueOf(saveData.getShrinkOvertime()));

			if (!shrinkModeButton.getMessage().getString().equals(saveData.getShrinkMode()))
				shrinkModeButton.setMessage(Component.literal(saveData.getShrinkMode()));

			if (shrinkModeButton.visible && shrinkModeButton.isHoveredOrFocused()) {
				Component ShrinkMode = Component.literal(saveData.getShrinkMode());
				if (ShrinkMode.getString().equals("Shrink")) {
					MutableComponent shrinkComponent = Component.translatable("book.uhc.explain.shrinkmodeshrink").append("\n")
							.append(Component.translatable("book.uhc.explain.shrinkmodeshrink2"));
					shrinkModeButton.setTooltip(Tooltip.create(shrinkComponent));
				}
				if (ShrinkMode.getString().equals("Arena")) {
					MutableComponent arenaComponent = Component.translatable("book.uhc.explain.shrinkmodearena")
							.append(Component.translatable("book.uhc.explain.shrinkmodearena2"));
					shrinkModeButton.setTooltip(Tooltip.create(arenaComponent));
				}
				if (ShrinkMode.getString().equals("Control")) {
					MutableComponent controlComponent = Component.translatable("book.uhc.explain.shrinkmodecontrol")
							.append(Component.translatable("book.uhc.explain.shrinkmodecontrol2"));
					shrinkModeButton.setTooltip(Tooltip.create(controlComponent));
				}
			}
		}

		if (this.currPage == 2) {
			if (timeLockButton.getBoolean() != saveData.isTimeLock())
				timeLockButton.setBoolean(saveData.isTimeLock());

			if (!Objects.equals(timeLockTimerField.getValue(), String.valueOf(saveData.getTimeLockTimer())) && !timeLockTimerField.isFocused())
				timeLockTimerField.setValue(String.valueOf(saveData.getTimeLockTimer()));

			if (!timeModeButton.getMessage().getString().equals(saveData.getTimeMode()))
				timeModeButton.setMessage(Component.literal(saveData.getTimeMode()));

			Component TimeMode = Component.literal(saveData.getTimeMode());
			if (TimeMode.getString().equals("Day"))
				timeModeButton.setTooltip(Tooltip.create(Reference.timeModeDayText));
			if (TimeMode.getString().equals("Night"))
				timeModeButton.setTooltip(Tooltip.create(Reference.timeModeNightText));

			if (minuteMarkButton.getBoolean() != saveData.isMinuteMark())
				minuteMarkButton.setBoolean(saveData.isMinuteMark());

			if (!minMarkTimerField.getValue().equals(String.valueOf(saveData.getMinuteMarkTime())) && !minMarkTimerField.isFocused())
				minMarkTimerField.setValue(String.valueOf(saveData.getMinuteMarkTime()));

			if (nameButton.getBoolean() != saveData.isTimedNames())
				nameButton.setBoolean(saveData.isTimedNames());

			if (!nameTimerField.getValue().equals(String.valueOf(saveData.getNameTimer())) && !nameTimerField.isFocused())
				nameTimerField.setValue(String.valueOf(saveData.getNameTimer()));

			if (glowButton.getBoolean() != saveData.isTimedGlow())
				glowButton.setBoolean(saveData.isTimedGlow());

			if (!glowTimerField.getValue().equals(String.valueOf(saveData.getGlowTime())) && !glowTimerField.isFocused())
				glowTimerField.setValue(String.valueOf(saveData.getGlowTime()));
		}

		if (this.currPage == 3) {
			if (regenPotionsButton.getBoolean() != saveData.isRegenPotions())
				regenPotionsButton.setBoolean(saveData.isRegenPotions());

			if (level2PotionsButton.getBoolean() != saveData.isLevel2Potions())
				level2PotionsButton.setBoolean(saveData.isLevel2Potions());

			if (notchApplesButton.getBoolean() != saveData.isNotchApples())
				notchApplesButton.setBoolean(saveData.isNotchApples());

			if (autoCookButton.getBoolean() != saveData.isAutoCookEnabled())
				autoCookButton.setBoolean(saveData.isAutoCookEnabled());

			if (itemConvertButton.getBoolean() != saveData.isItemConversion())
				itemConvertButton.setBoolean(saveData.isItemConversion());

			if (netherButton.getBoolean() != saveData.isNetherEnabled())
				netherButton.setBoolean(saveData.isNetherEnabled());

			if (healthTabButton.getBoolean() != saveData.isHealthInTab())
				healthTabButton.setBoolean(saveData.isHealthInTab());

			if (healthSideButton.getBoolean() != saveData.isHealthOnSide())
				healthSideButton.setBoolean(saveData.isHealthOnSide());

			if (healthNameButton.getBoolean() != saveData.isHealthUnderName())
				healthNameButton.setBoolean(saveData.isHealthUnderName());
		}

		if (this.currPage == 4) {
			if (weatherButton.getBoolean() != saveData.isWeatherEnabled())
				weatherButton.setBoolean(saveData.isWeatherEnabled());

			if (mobGriefingButton.getBoolean() != saveData.isMobGriefing())
				mobGriefingButton.setBoolean(saveData.isMobGriefing());

			if (customHealthButton.getBoolean() != saveData.isApplyCustomHealth())
				customHealthButton.setBoolean(saveData.isApplyCustomHealth());

			if (!maxHealthField.getValue().equals(String.valueOf(saveData.getMaxHealth())) && !maxHealthField.isFocused())
				maxHealthField.setValue(String.valueOf(saveData.getMaxHealth()));

			if (randomSpawnButton.getBoolean() != saveData.isRandomSpawns())
				randomSpawnButton.setBoolean(saveData.isRandomSpawns());

			if (!spreadDistanceField.getValue().equals(String.valueOf(saveData.getMaxHealth())) && !spreadDistanceField.isFocused())
				spreadDistanceField.setValue(String.valueOf(saveData.getSpreadDistance()));

			if (!spreadMaxRangeField.getValue().equals(String.valueOf(saveData.getMaxHealth())) && !spreadMaxRangeField.isFocused())
				spreadMaxRangeField.setValue(String.valueOf(saveData.getSpreadMaxRange()));

			if (spreadRespectTeamButton.getBoolean() != saveData.isSpreadRespectTeam())
				spreadRespectTeamButton.setBoolean(saveData.isSpreadRespectTeam());
		}

		if (this.currPage == 5) {
			if (graceTimeButton.getBoolean() != saveData.isGraceEnabled())
				graceTimeButton.setBoolean(saveData.isGraceEnabled());

			if (!graceTimeField.getValue().equals(String.valueOf(saveData.getGraceTime())) && !graceTimeField.isFocused())
				graceTimeField.setValue(String.valueOf(saveData.getGraceTime()));
		}
	}

	private void updateButtons() {
		this.buttonNextPage.visible = (this.currPage < this.bookTotalPages - 1);
		this.buttonPreviousPage.visible = this.currPage > 0;
		this.buttonDone.visible = true;

		boolean isPage0 = this.currPage == 0;
		for (ColorButton colorButton : colorButtons) {
			colorButton.visible = isPage0;
		}

		this.resetRandButton.visible = isPage0;
		this.resetTeamSizeButton.visible = isPage0;

		this.collisionButton.visible = isPage0;
		this.damageButton.visible = isPage0;
		this.teamsLockedButton.visible = isPage0;

		this.randSizeField.setVisible(isPage0);
		this.maxTeamSizeField.setVisible(isPage0);
		this.difficultyField.setVisible(isPage0);

		boolean isPage1 = this.currPage == 1;
		this.borderSizeField.setVisible(isPage1);
		this.borderCenterXField.setVisible(isPage1);
		this.borderCenterZField.setVisible(isPage1);

		this.borderSizeField.setEditable(isPage1);
		this.borderCenterXField.setEditable(isPage1);
		this.borderCenterZField.setEditable(isPage1);

		this.shrinkTimerField.setVisible(isPage1);
		this.shrinkTimerField.setEditable(isPage1);
		this.shrinkSizeField.setVisible(isPage1);
		this.shrinkSizeField.setEditable(isPage1);
		this.shrinkOvertimeField.setVisible(isPage1);
		this.shrinkOvertimeField.setEditable(isPage1);

		this.resetBorderSizeButton.visible = isPage1;
		this.resetBorderCenterXButton.visible = isPage1;
		this.resetBorderCenterZButton.visible = isPage1;
		this.centerCurrentXButton.visible = isPage1;
		this.centerCurrentZButton.visible = isPage1;

		this.shrinkButton.visible = isPage1;
		this.resetShrinkTimerButton.visible = isPage1;
		this.resetShrinkSizeButton.visible = isPage1;
		this.resetShrinkOverTimeButton.visible = isPage1;
		this.shrinkModeButton.visible = isPage1;

		boolean isPage2 = this.currPage == 2;
		this.timeLockButton.visible = isPage2;
		this.timeModeButton.visible = isPage2;
		this.resetTimeLockTimerButton.visible = isPage2;
		this.timeLockTimerField.setVisible(isPage2);
		this.timeLockTimerField.setEditable(isPage2);
		this.minMarkTimerField.setVisible(isPage2);
		this.minMarkTimerField.setEditable(isPage2);
		this.nameTimerField.setVisible(isPage2);
		this.nameTimerField.setEditable(isPage2);
		this.glowTimerField.setVisible(isPage2);
		this.glowTimerField.setEditable(isPage2);

		this.minuteMarkButton.visible = isPage2;
		this.resetMinuteMarkTimerButton.visible = isPage2;
		this.nameButton.visible = isPage2;
		this.resetNameTimerButton.visible = isPage2;
		this.glowButton.visible = isPage2;
		this.resetGlowTimerButton.visible = isPage2;

		boolean isPage3 = this.currPage == 3;
		this.netherButton.visible = isPage3;

		this.healthTabButton.visible = isPage3;
		this.healthSideButton.visible = isPage3;
		this.healthNameButton.visible = isPage3;

		this.regenPotionsButton.visible = isPage3;
		this.level2PotionsButton.visible = isPage3;
		this.notchApplesButton.visible = isPage3;
		this.autoCookButton.visible = isPage3;
		this.itemConvertButton.visible = isPage3;

		boolean isPage4 = this.currPage == 4;
		this.weatherButton.visible = isPage4;
		this.mobGriefingButton.visible = isPage4;
		this.customHealthButton.visible = isPage4;
		this.spreadRespectTeamButton.visible = isPage4;
		this.randomSpawnButton.visible = isPage4;
		this.maxHealthField.setVisible(isPage4);
		this.maxHealthField.setEditable(isPage4);

		boolean isPage5 = this.currPage == 5;
		this.graceTimeButton.visible = isPage5;
		this.graceTimeField.setVisible(isPage5);
		this.graceTimeField.setEditable(isPage5);
		this.UHCStartButton.visible = isPage5;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		this.setFocused((GuiEventListener) null);

		int i = (this.width - 192) / 2;
		int j = 2;
		guiGraphics.blit(BOOK_TEXTURE, i, 2, 0, 0, 192, 192);

		String s4 = I18n.get("book.pageIndicator", this.currPage + 1, this.bookTotalPages);

		int j1 = this.font.width(s4);
		guiGraphics.drawString(font, s4, i - j1 + 192 - 44, 18, 0, false);

		//TODO: Check if we can use .setTooltip instead of manually drawing the tooltip upon hover

		final Component graceString = Component.translatable("book.uhc.option.grace");

		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		if (this.currPage == 0) {
			Component teamSelect = Component.translatable("book.uhc.team.select");

			Component randSizeString = Component.translatable("book.uhc.option.randsize");
			guiGraphics.drawString(this.font, randSizeString, i + 43, j + 89, 0xFF555555, false);
			randSizeField.render(guiGraphics, mouseX, mouseY, partialTicks);


			Component maxTeamSizeString = Component.translatable("book.uhc.option.maxteams");
			guiGraphics.drawString(this.font, maxTeamSizeString, i + 43, j + 101, 0xFF555555, false);
			maxTeamSizeField.render(guiGraphics, mouseX, mouseY, partialTicks);

			boolean flag = hoverBoolean(mouseX, mouseY, maxTeamSizeField.getX(), maxTeamSizeField.getY(),
					maxTeamSizeField.getWidth(), maxTeamSizeField.getHeight());
			Component infinityString = Component.translatable("book.uhc.option.infinite");

			if (isColorNotHovered() && !flag && !teamsLockedButton.isHoveredOrFocused())
				guiGraphics.drawString(this.font, teamSelect, i + 65, j + 28, 0xFF555555, false);

			if (flag && !maxTeamSizeField.isFocused())
				guiGraphics.drawCenteredString(font, infinityString, i + 91, j + 28, 0xFFFF5555);

			Component lockButton = Component.translatable("book.uhc.option.locked");

			if (teamsLockedButton.isHoveredOrFocused())
				guiGraphics.drawCenteredString(font, lockButton, i + 93, j + 27, 0xFFFF5555);

			Component teamCollisionString = Component.translatable("book.uhc.option.collision");
			guiGraphics.drawString(this.font, teamCollisionString, i + 43, j + 118, 0xFF555555, false);

			Component teamDamageString = Component.translatable("book.uhc.option.damage");
			guiGraphics.drawString(this.font, teamDamageString, i + 43, j + 131, 0xFF555555, false);

			Component difficultyString = Component.translatable("book.uhc.option.difficulty");
			guiGraphics.drawString(this.font, difficultyString, i + 43, j + 144, 0xFF555555, false);
			difficultyField.render(guiGraphics, mouseX, mouseY, partialTicks);
		}

		if (this.currPage == 1) {
			Component borderSizeString = Component.translatable("book.uhc.option.bordersize");
			guiGraphics.drawString(this.font, borderSizeString, i + 48, j + 28, 0xFF555555, false);
			borderSizeField.render(guiGraphics, mouseX, mouseY, partialTicks);

			Component centerString = Component.translatable("book.uhc.option.bordercenter");
			guiGraphics.drawString(this.font, centerString, i + 42, j + 53, 0xFF555555, false);

			Component centerxString = Component.translatable("book.uhc.option.bordercenterx");
			guiGraphics.drawString(this.font, centerxString, i + 42, j + 64, 0xFF555555, false);
			borderCenterXField.render(guiGraphics, mouseX, mouseY, partialTicks);

			Component centerZString = Component.translatable("book.uhc.option.bordercenterz");
			guiGraphics.drawString(this.font, centerZString, i + 42, j + 76, 0xFF555555, false);
			borderCenterZField.render(guiGraphics, mouseX, mouseY, partialTicks);

			Component ShrinkString = Component.translatable("book.uhc.option.shrinkenabled");
			guiGraphics.drawString(this.font, ShrinkString, i + 38, j + 94, 0xFF555555, false);

			Component ShrinkTimerString = Component.translatable("book.uhc.option.shrinktimer");
			guiGraphics.drawString(this.font, ShrinkTimerString, i + 44, j + 107, 0xFF555555, false);
			shrinkTimerField.render(guiGraphics, mouseX, mouseY, partialTicks);

			Component ShrinkSizeString = Component.translatable("book.uhc.option.shrinksize");
			guiGraphics.drawString(this.font, ShrinkSizeString, i + 44, j + 118, 0xFF555555, false);
			shrinkSizeField.render(guiGraphics, mouseX, mouseY, partialTicks);

			Component ShrinkOvertimeString = Component.translatable("book.uhc.option.shrinkovertime");
			guiGraphics.drawString(this.font, ShrinkOvertimeString, i + 44, j + 129, 0xFF555555, false);
			shrinkOvertimeField.render(guiGraphics, mouseX, mouseY, partialTicks);

			Component ShrinkModeString = Component.translatable("book.uhc.option.shrinkmode");
			guiGraphics.drawString(this.font, ShrinkModeString, i + 44, j + 140, 0xFF555555, false);
		}

		if (this.currPage == 2) {
			guiGraphics.drawString(this.font, Reference.TimeLockString, i + 38, j + 28, 0xFF555555, false);
			timeLockTimerField.render(guiGraphics, mouseX, mouseY, partialTicks);

			Component TimeLockTimerString = Component.translatable("book.uhc.option.timelocktimer");
			guiGraphics.drawString(this.font, TimeLockTimerString, i + 44, j + 41, 0xFF555555, false);

			Component TimeLockModeString = Component.translatable("book.uhc.option.timelockmode");
			guiGraphics.drawString(this.font, TimeLockModeString, i + 44, j + 53, 0xFF555555, false);

			guiGraphics.drawString(this.font, Reference.minMarkString, i + 38, j + 68, 0xFF555555, false);

			Component minMarkTimerString = Component.translatable("book.uhc.option.minmarktime");
			guiGraphics.drawString(this.font, minMarkTimerString, i + 44, j + 80, 0xFF555555, false);
			minMarkTimerField.render(guiGraphics, mouseX, mouseY, partialTicks);

			guiGraphics.drawString(this.font, Reference.timedNameString, i + 38, j + 94, 0xFF555555, false);

			Component timedNameTimerString = Component.translatable("book.uhc.option.timednametime");
			guiGraphics.drawString(this.font, timedNameTimerString, i + 44, j + 106, 0xFF555555, false);
			nameTimerField.render(guiGraphics, mouseX, mouseY, partialTicks);

			guiGraphics.drawString(this.font, Reference.timedGlowString, i + 38, j + 122, 0xFF555555, false);

			Component timedGlowStringTimerString = Component.translatable("book.uhc.option.timedglowtime");
			guiGraphics.drawString(this.font, timedGlowStringTimerString, i + 44, j + 134, 0xFF555555, false);
			glowTimerField.render(guiGraphics, mouseX, mouseY, partialTicks);
		}

		if (this.currPage == 3) {
			guiGraphics.drawString(this.font, Reference.regenPotionsString, i + 38, j + 28, 0xFF555555, false);

			guiGraphics.drawString(this.font, Reference.level2PotionsString, i + 38, j + 40, 0xFF555555, false);

			guiGraphics.drawString(this.font, Reference.notchApplesString, i + 38, j + 52, 0xFF555555, false);

			guiGraphics.drawString(this.font, Reference.autoCookString, i + 38, j + 68, 0xFF555555, false);

			guiGraphics.drawString(this.font, Reference.itemConvertString, i + 38, j + 80, 0xFF555555, false);

			guiGraphics.drawString(this.font, Reference.netherTravelString, i + 38, j + 98, 0xFF555555, false);

			Component healthInTabString = Component.translatable("book.uhc.option.healthtab");
			guiGraphics.drawString(this.font, healthInTabString, i + 38, j + 114, 0xFF555555, false);

			Component healthOnSideString = Component.translatable("book.uhc.option.healthside");
			guiGraphics.drawString(this.font, healthOnSideString, i + 38, j + 126, 0xFF555555, false);

			Component healthUnderNameString = Component.translatable("book.uhc.option.healthname");
			guiGraphics.drawString(this.font, healthUnderNameString, i + 38, j + 138, 0xFF555555, false);
		}

		if (this.currPage == 4) {
			guiGraphics.drawString(this.font, Reference.weatherString, i + 38, j + 28, 0xFF555555, false);

			guiGraphics.drawString(this.font, Reference.mobGriefingString, i + 38, j + 40, 0xFF555555, false);

			guiGraphics.drawString(this.font, Reference.customHealthString, i + 38, j + 57, 0xFF555555, false);

			Component healthMaxString = Component.translatable("book.uhc.option.maxhealth");
			guiGraphics.drawString(this.font, healthMaxString, i + 44, j + 69, 0xFF555555, false);
			maxHealthField.render(guiGraphics, mouseX, mouseY, partialTicks);

			guiGraphics.drawString(this.font, Reference.randomSpawnString, i + 38, j + 86, 0xFF555555, false);

			guiGraphics.drawString(this.font, Reference.spreadDistanceString, i + 44, j + 98, 0xFF555555, false);
			spreadDistanceField.render(guiGraphics, mouseX, mouseY, partialTicks);

			guiGraphics.drawString(this.font, Reference.spreadMaxRangeString, i + 44, j + 110, 0xFF555555, false);
			spreadMaxRangeField.render(guiGraphics, mouseX, mouseY, partialTicks);

			guiGraphics.drawString(this.font, Reference.spreadRespectTeamString, i + 44, j + 122, 0xFF555555, false);

			Component healthExplain = Component.translatable("book.uhc.explain.healthExplain");
			boolean flag2 = hoverBoolean(mouseX, mouseY, maxHealthField.getX(), maxHealthField.getY(),
					maxHealthField.getWidth(), maxHealthField.getHeight());
			if (flag2 && !maxHealthField.isFocused())
				guiGraphics.drawCenteredString(font, healthExplain, mouseX, mouseY + 5, 0xFFFF5555);
		}

		if (this.currPage == 5) {
			guiGraphics.drawString(this.font, graceString, i + 38, j + 28, 0xFF555555, false);

			graceTimeField.render(guiGraphics, mouseX, mouseY, partialTicks);

			Component GraceTimerString = Component.translatable("book.uhc.option.gracetimer");
			guiGraphics.drawString(this.font, GraceTimerString, i + 44, j + 41, 0xFF555555, false);
		}

		if (this.currPage == 2 && hoverBoolean(mouseX, mouseY, i + 38, j + 28, font.width(Reference.TimeLockString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, Component.translatable("book.uhc.explain.timelock"), mouseX, mouseY);
		}

		List<FormattedCharSequence> MinuteMarkExplain = new ArrayList<>();
		MinuteMarkExplain.add(Component.translatable("book.uhc.explain.minmark").getVisualOrderText());
		MinuteMarkExplain.add(Component.translatable("book.uhc.explain.minmark2").getVisualOrderText());
		List<FormattedCharSequence> timedNameExplain = new ArrayList<>();
		timedNameExplain.add(Component.translatable("book.uhc.explain.timename").getVisualOrderText());
		timedNameExplain.add(Component.translatable("book.uhc.explain.timename2").getVisualOrderText());
		List<FormattedCharSequence> timedGlowExplain = new ArrayList<>();
		timedGlowExplain.add(Component.translatable("book.uhc.explain.timeglow").getVisualOrderText());
		timedGlowExplain.add(Component.translatable("book.uhc.explain.timeglow2").getVisualOrderText());

		if (this.currPage == 2 && hoverBoolean(mouseX, mouseY, i + 38, j + 68, font.width(Reference.minMarkString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, MinuteMarkExplain, mouseX, mouseY);
		}
		if (this.currPage == 2 && hoverBoolean(mouseX, mouseY, i + 38, j + 94, font.width(Reference.timedNameString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, timedNameExplain, mouseX, mouseY);
		}
		if (this.currPage == 2 && hoverBoolean(mouseX, mouseY, i + 38, j + 122, font.width(Reference.timedGlowString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, timedGlowExplain, mouseX, mouseY);
		}

		List<FormattedCharSequence> regenPotionExplain = new ArrayList<>();
		regenPotionExplain.add(Component.translatable("book.uhc.explain.regenpotion").getVisualOrderText());
		regenPotionExplain.add(Component.translatable("book.uhc.explain.regenpotion2").getVisualOrderText());
		List<FormattedCharSequence> level2PotionExplain = new ArrayList<>();
		level2PotionExplain.add(Component.translatable("book.uhc.explain.level2potion").getVisualOrderText());
		level2PotionExplain.add(Component.translatable("book.uhc.explain.level2potion2").getVisualOrderText());
		List<FormattedCharSequence> notchApplesExplain = new ArrayList<>();
		notchApplesExplain.add(Component.translatable("book.uhc.explain.notchapple").getVisualOrderText());
		notchApplesExplain.add(Component.translatable("book.uhc.explain.notchapple2").getVisualOrderText());

		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 28, font.width(Reference.regenPotionsString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, regenPotionExplain, mouseX, mouseY);
		}
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 40, font.width(Reference.level2PotionsString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, level2PotionExplain, mouseX, mouseY);
		}
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 52, font.width(Reference.notchApplesString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, notchApplesExplain, mouseX, mouseY);
		}

		List<FormattedCharSequence> autoSmeltExplain = new ArrayList<>();
		autoSmeltExplain.add(Component.translatable("book.uhc.explain.autocook").getVisualOrderText());
		autoSmeltExplain.add(Component.translatable("book.uhc.explain.autocook2").getVisualOrderText());
		autoSmeltExplain.add(Component.translatable("book.uhc.explain.autocook3").getVisualOrderText());
		autoSmeltExplain.add(Component.translatable("book.uhc.explain.autocook4").getVisualOrderText());
		List<FormattedCharSequence> itemConvertExplain = new ArrayList<>();
		itemConvertExplain.add(Component.translatable("book.uhc.explain.itemconvert").getVisualOrderText());
		itemConvertExplain.add(Component.translatable("book.uhc.explain.itemconvert2").getVisualOrderText());
		itemConvertExplain.add(Component.translatable("book.uhc.explain.itemconvert3").getVisualOrderText());
		itemConvertExplain.add(Component.translatable("book.uhc.explain.itemconvert4").getVisualOrderText());
		itemConvertExplain.add(Component.translatable("book.uhc.explain.itemconvert5").getVisualOrderText());
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 68, font.width(Reference.autoCookString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, autoSmeltExplain, mouseX, mouseY);
		}
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 80, font.width(Reference.itemConvertString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, itemConvertExplain, mouseX, mouseY);
		}

		Component netherTravelExplain = Component.translatable("book.uhc.explain.nether");
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 35, j + 98, font.width(Reference.netherTravelString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, netherTravelExplain, mouseX, mouseY);
		}

		Component weatherExplain = Component.translatable("book.uhc.explain.weather");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 38, j + 28, font.width(Reference.weatherString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, weatherExplain, mouseX, mouseY);
		}
		Component mobGriefingExplain = Component.translatable("book.uhc.explain.mobgriefing");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 38, j + 40, font.width(Reference.mobGriefingString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, mobGriefingExplain, mouseX, mouseY);
		}
		Component customHealthExplain = Component.translatable("book.uhc.explain.customhealth");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 38, j + 57, font.width(Reference.customHealthString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, customHealthExplain, mouseX, mouseY);
		}
		List<FormattedCharSequence> randomSpawnsExplain = new ArrayList<>();
		randomSpawnsExplain.add(Component.translatable("book.uhc.explain.randomspawns").getVisualOrderText());
		randomSpawnsExplain.add(Component.translatable("book.uhc.explain.randomspawns2").getVisualOrderText());
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 38, j + 86, font.width(Reference.randomSpawnString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, randomSpawnsExplain, mouseX, mouseY);
		}
		Component spreadDistanceExplain = Component.translatable("book.uhc.explain.spreaddistance");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 44, j + 98, font.width(Reference.spreadDistanceString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, spreadDistanceExplain, mouseX, mouseY);
		}
		Component spreadMaxRangeExplain = Component.translatable("book.uhc.explain.spreadrange");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 44, j + 110, font.width(Reference.spreadMaxRangeString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, spreadMaxRangeExplain, mouseX, mouseY);
		}
		Component spreadRespectTeamExplain = Component.translatable("book.uhc.explain.spreadteams");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 44, j + 122, font.width(Reference.spreadRespectTeamString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, spreadRespectTeamExplain, mouseX, mouseY);
		}
		List<FormattedCharSequence> gracePeriodExplain = new ArrayList<>();
		gracePeriodExplain.add(Component.translatable("book.uhc.explain.graceperiod").getVisualOrderText());
		gracePeriodExplain.add(Component.translatable("book.uhc.explain.graceperiod2").getVisualOrderText());
		if (this.currPage == 5 && hoverBoolean(mouseX, mouseY, i + 38, j + 28, font.width(graceString), font.lineHeight)) {
			guiGraphics.renderTooltip(this.font, gracePeriodExplain, mouseX, mouseY);
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
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int delta) {
		if (delta == 0) {
			if (this.currPage == 0) {
				if (randSizeField.mouseClicked(mouseX, mouseY, delta))
					randSizeField.setValue("");
				if (maxTeamSizeField.mouseClicked(mouseX, mouseY, delta))
					maxTeamSizeField.setValue("");
				if (difficultyField.mouseClicked(mouseX, mouseY, delta))
					difficultyField.setValue("");

				if (!randSizeField.isFocused())
					randSizeField.setValue(String.valueOf(saveData.getRandomTeamSize()));
				if (!maxTeamSizeField.isFocused())
					maxTeamSizeField.setValue(String.valueOf(saveData.getMaxTeamSize()));
				if (!difficultyField.isFocused())
					difficultyField.setValue(String.valueOf(saveData.getDifficulty()));
			}

			if (this.currPage == 1) {
				if (borderSizeField.mouseClicked(mouseX, mouseY, delta))
					borderSizeField.setValue("");
				if (borderCenterXField.mouseClicked(mouseX, mouseY, delta))
					borderCenterXField.setValue("");
				if (borderCenterZField.mouseClicked(mouseX, mouseY, delta))
					borderCenterZField.setValue("");
				if (shrinkTimerField.mouseClicked(mouseX, mouseY, delta))
					shrinkTimerField.setValue("");
				if (shrinkSizeField.mouseClicked(mouseX, mouseY, delta))
					shrinkSizeField.setValue("");
				if (shrinkOvertimeField.mouseClicked(mouseX, mouseY, delta))
					shrinkOvertimeField.setValue("");

				if (!borderSizeField.isFocused())
					borderSizeField.setValue(String.valueOf(saveData.getBorderSize()));
				if (!borderCenterXField.isFocused())
					borderCenterXField.setValue(String.valueOf(saveData.getBorderCenterX()));
				if (!borderCenterZField.isFocused())
					borderCenterZField.setValue(String.valueOf(saveData.getBorderCenterZ()));
				if (!shrinkTimerField.isFocused())
					shrinkTimerField.setValue(String.valueOf(saveData.getShrinkTimer()));
				if (!shrinkSizeField.isFocused())
					shrinkSizeField.setValue(String.valueOf(saveData.getShrinkSize()));
				if (!shrinkOvertimeField.isFocused())
					shrinkOvertimeField.setValue(String.valueOf(saveData.getShrinkOvertime()));
			}

			if (this.currPage == 2) {
				if (timeLockTimerField.mouseClicked(mouseX, mouseY, delta))
					timeLockTimerField.setValue("");
				if (minMarkTimerField.mouseClicked(mouseX, mouseY, delta))
					minMarkTimerField.setValue("");
				if (nameTimerField.mouseClicked(mouseX, mouseY, delta))
					nameTimerField.setValue("");
				if (glowTimerField.mouseClicked(mouseX, mouseY, delta))
					glowTimerField.setValue("");

				if (!timeLockTimerField.isFocused())
					timeLockTimerField.setValue(String.valueOf(saveData.getTimeLockTimer()));
				if (!minMarkTimerField.isFocused())
					minMarkTimerField.setValue(String.valueOf(saveData.getMinuteMarkTime()));
				if (!nameTimerField.isFocused())
					nameTimerField.setValue(String.valueOf(saveData.getNameTimer()));
				if (!glowTimerField.isFocused())
					glowTimerField.setValue(String.valueOf(saveData.getGlowTime()));
			}

			if (this.currPage == 4) {
				if (maxHealthField.mouseClicked(mouseX, mouseY, delta))
					maxHealthField.setValue("");
				if (spreadDistanceField.mouseClicked(mouseX, mouseY, delta))
					spreadDistanceField.setValue("");
				if (spreadMaxRangeField.mouseClicked(mouseX, mouseY, delta))
					spreadMaxRangeField.setValue("");

				if (!maxHealthField.isFocused())
					maxHealthField.setValue(String.valueOf(saveData.getMaxHealth()));
				if (!spreadDistanceField.isFocused())
					spreadDistanceField.setValue(String.valueOf(saveData.getSpreadDistance()));
				if (!spreadMaxRangeField.isFocused())
					spreadMaxRangeField.setValue(String.valueOf(saveData.getSpreadMaxRange()));
			}

			if (this.currPage == 5) {
				if (graceTimeField.mouseClicked(mouseX, mouseY, delta))
					graceTimeField.setValue("");

				if (!graceTimeField.isFocused())
					graceTimeField.setValue(String.valueOf(saveData.getGraceTime()));
			}
		}

		return super.mouseClicked(mouseX, mouseY, delta);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		boolean flag = super.keyPressed(keyCode, scanCode, modifiers);
		if (this.currPage == 0) {
			if (randSizeField.isFocused()) {
				randSizeField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (maxTeamSizeField.isFocused()) {
				maxTeamSizeField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (difficultyField.isFocused()) {
				difficultyField.keyPressed(keyCode, scanCode, modifiers);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				if (randSizeField.isFocused()) {
					String randText = randSizeField.getValue();

					if (randText.isEmpty())
						randSizeField.setValue(String.valueOf(randomTeamSize));
					else {
						randomTeamSize = Integer.parseInt(randText);
						sendPage1Packet();
					}

					randSizeField.setFocused(false);
				}

				if (maxTeamSizeField.isFocused()) {
					String teamSize = maxTeamSizeField.getValue();

					if (teamSize.isEmpty() || Integer.parseInt(teamSize) > 14)
						maxTeamSizeField.setValue(String.valueOf(maxTeamSize));
					else {
						maxTeamSize = Integer.parseInt(teamSize);
						sendPage1Packet();
					}

					maxTeamSizeField.setFocused(false);
				}

				if (difficultyField.isFocused()) {
					String difficultyText = difficultyField.getValue();

					if (difficultyText.isEmpty() || Integer.parseInt(difficultyText) > 3)
						difficultyField.setValue(String.valueOf(difficulty));
					else {
						difficulty = Integer.parseInt(difficultyText);
						sendPage1Packet();
					}

					difficultyField.setFocused(false);
				}
			}
		}

		if (this.currPage == 1) {
			if (borderSizeField.isFocused()) {
				borderSizeField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (borderCenterXField.isFocused()) {
				borderCenterXField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (borderCenterZField.isFocused()) {
				borderCenterZField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (shrinkTimerField.isFocused()) {
				shrinkTimerField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (shrinkSizeField.isFocused()) {
				shrinkSizeField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (shrinkOvertimeField.isFocused()) {
				shrinkOvertimeField.keyPressed(keyCode, scanCode, modifiers);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Border Size Field */
				if (borderSizeField.isFocused()) {
					String borderSizeText = borderSizeField.getValue();

					if (borderSizeText.isEmpty())
						borderSizeField.setValue(String.valueOf(borderSize));
					else {
						borderSize = Integer.parseInt(borderSizeText);
						sendPage2Packet();
					}

					borderSizeField.setFocused(false);
				}
				/* Border Center X Field */
				if (borderCenterXField.isFocused()) {
					String borderX = borderCenterXField.getValue();

					if (borderX.isEmpty())
						borderCenterXField.setValue(String.valueOf(borderCenterX));
					else {
						borderCenterX = Integer.parseInt(borderX);
						sendPage2Packet();
					}

					borderCenterXField.setFocused(false);
				}
				/* Border Center Z Field */
				if (borderCenterZField.isFocused()) {
					String borderZ = borderCenterZField.getValue();

					if (borderZ.isEmpty())
						borderCenterZField.setValue(String.valueOf(borderCenterZ));
					else {
						borderCenterZ = Integer.parseInt(borderZ);
						sendPage2Packet();
					}

					borderCenterZField.setFocused(false);
				}
				/* Shrink Timer Field */
				if (shrinkTimerField.isFocused()) {
					String shrinkTimerText = shrinkTimerField.getValue();

					if (shrinkTimerText.isEmpty())
						shrinkTimerField.setValue(String.valueOf(shrinkTimer));
					else {
						shrinkTimer = Integer.parseInt(shrinkTimerText);
						sendPage2Packet();
					}

					shrinkTimerField.setFocused(false);
				}
				/* Shrink Size Field */
				if (shrinkSizeField.isFocused()) {
					String shrinkSizeText = shrinkSizeField.getValue();

					if (shrinkSizeText.isEmpty())
						shrinkSizeField.setValue(String.valueOf(shrinkSize));
					else {
						shrinkSize = Integer.parseInt(shrinkSizeText);
						sendPage2Packet();
					}

					shrinkSizeField.setFocused(false);
				}
				/* Shrink Over Time Field */
				if (shrinkOvertimeField.isFocused()) {
					String ShrinkOverTimeText = shrinkOvertimeField.getValue();

					if (ShrinkOverTimeText.isEmpty())
						shrinkOvertimeField.setValue(String.valueOf(shrinkOvertime));
					else {
						shrinkOvertime = Integer.parseInt(ShrinkOverTimeText);
						sendPage2Packet();
					}

					shrinkOvertimeField.setFocused(false);
				}
			}
		}
		if (this.currPage == 2) {
			if (timeLockTimerField.isFocused()) {
				timeLockTimerField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (minMarkTimerField.isFocused()) {
				minMarkTimerField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (nameTimerField.isFocused()) {
				nameTimerField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (glowTimerField.isFocused()) {
				glowTimerField.keyPressed(keyCode, scanCode, modifiers);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Shrink Timer Field */
				if (timeLockTimerField.isFocused()) {
					String timeLockTimerText = timeLockTimerField.getValue();

					if (timeLockTimerText.isEmpty())
						timeLockTimerField.setValue(String.valueOf(timeLockTimer));
					else {
						timeLockTimer = Integer.parseInt(timeLockTimerText);
						sendPage3Packet();
					}

					timeLockTimerField.setFocused(false);
				}
				/* Minute Mark Timer Field */
				if (minMarkTimerField.isFocused()) {
					String minuteMarkTimerText = minMarkTimerField.getValue();

					if (minuteMarkTimerText.isEmpty())
						minMarkTimerField.setValue(String.valueOf(minuteMarkTime));
					else {
						minuteMarkTime = Integer.parseInt(minuteMarkTimerText);
						sendPage3Packet();
					}

					minMarkTimerField.setFocused(false);
				}
				/* Name Timer Field */
				if (nameTimerField.isFocused()) {
					String nameTimerText = nameTimerField.getValue();

					if (nameTimerText.isEmpty())
						nameTimerField.setValue(String.valueOf(nameTimer));
					else {
						nameTimer = Integer.parseInt(nameTimerText);
						sendPage3Packet();
					}

					nameTimerField.setFocused(false);
				}
				/* Glow Timer Field */
				if (glowTimerField.isFocused()) {
					String glowTimer = glowTimerField.getValue();

					if (glowTimer.isEmpty())
						glowTimerField.setValue(String.valueOf(glowTime));
					else {
						glowTime = Integer.parseInt(glowTimer);
						sendPage3Packet();
					}

					glowTimerField.setFocused(false);
				}
			}
		}
		if (this.currPage == 4) {
			if (maxHealthField.isFocused()) {
				maxHealthField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (spreadDistanceField.isFocused()) {
				spreadDistanceField.keyPressed(keyCode, scanCode, modifiers);
			}
			if (spreadMaxRangeField.isFocused()) {
				spreadMaxRangeField.keyPressed(keyCode, scanCode, modifiers);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Max Health Field */
				if (maxHealthField.isFocused()) {
					String maxHealthText = maxHealthField.getValue();

					if (maxHealthText.isEmpty())
						maxHealthField.setValue(String.valueOf(maxHealth));
					else {
						maxHealth = Integer.parseInt(maxHealthText);
						sendPage5Packet();
					}

					maxHealthField.setFocused(false);
				}
				/* Spread Distance Field */
				if (spreadDistanceField.isFocused()) {
					String maxDistanceText = spreadDistanceField.getValue();

					if (maxDistanceText.isEmpty())
						spreadDistanceField.setValue(String.valueOf(spreadDistance));
					else {
						spreadDistance = Integer.parseInt(maxDistanceText);
						sendPage5Packet();
					}

					spreadDistanceField.setFocused(false);
				}
				/* Spread Max Range Field */
				if (spreadMaxRangeField.isFocused()) {
					String maxRangeText = spreadMaxRangeField.getValue();

					if (maxRangeText.isEmpty())
						spreadMaxRangeField.setValue(String.valueOf(spreadMaxRange));
					else {
						spreadMaxRange = Integer.parseInt(maxRangeText);
						sendPage5Packet();
					}

					spreadMaxRangeField.setFocused(false);
				}
			}
		}
		if (this.currPage == 5) {
			if (graceTimeField.isFocused()) {
				graceTimeField.keyPressed(keyCode, scanCode, modifiers);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Grace Timer Field */
				if (graceTimeField.isFocused()) {
					String graceTimeText = graceTimeField.getValue();

					if (graceTimeText.isEmpty())
						graceTimeField.setValue(String.valueOf(graceTime));
					else {
						graceTime = Integer.parseInt(graceTimeText);
						sendPage6Packet();
					}

					graceTimeField.setFocused(false);
				}
			}
		}

		return flag;
	}

	@Override
	public boolean charTyped(char typedChar, int modifiers) {
		boolean flag = super.charTyped(typedChar, modifiers);
		if (this.currPage == 0) {
			if (randSizeField.isFocused()) {
				randSizeField.charTyped(typedChar, modifiers);
			}
			if (maxTeamSizeField.isFocused()) {
				maxTeamSizeField.charTyped(typedChar, modifiers);
			}
			if (difficultyField.isFocused()) {
				difficultyField.charTyped(typedChar, modifiers);
			}
		}

		if (this.currPage == 1) {
			if (borderSizeField.isFocused()) {
				borderSizeField.charTyped(typedChar, modifiers);
			}
			if (borderCenterXField.isFocused()) {
				borderCenterXField.charTyped(typedChar, modifiers);
			}
			if (borderCenterZField.isFocused()) {
				borderCenterZField.charTyped(typedChar, modifiers);
			}
			if (shrinkTimerField.isFocused()) {
				shrinkTimerField.charTyped(typedChar, modifiers);
			}
			if (shrinkSizeField.isFocused()) {
				shrinkSizeField.charTyped(typedChar, modifiers);
			}
			if (shrinkOvertimeField.isFocused()) {
				shrinkOvertimeField.charTyped(typedChar, modifiers);
			}

		}
		if (this.currPage == 2) {
			if (timeLockTimerField.isFocused()) {
				timeLockTimerField.charTyped(typedChar, modifiers);
			}
			if (minMarkTimerField.isFocused()) {
				minMarkTimerField.charTyped(typedChar, modifiers);
			}
			if (nameTimerField.isFocused()) {
				nameTimerField.charTyped(typedChar, modifiers);
			}
			if (glowTimerField.isFocused()) {
				glowTimerField.charTyped(typedChar, modifiers);
			}
		}
		if (this.currPage == 4) {
			if (maxHealthField.isFocused()) {
				maxHealthField.charTyped(typedChar, modifiers);
			}
			if (spreadDistanceField.isFocused()) {
				spreadDistanceField.charTyped(typedChar, modifiers);
			}
			if (spreadMaxRangeField.isFocused()) {
				spreadMaxRangeField.charTyped(typedChar, modifiers);
			}
		}
		if (this.currPage == 5) {
			if (graceTimeField.isFocused()) {
				graceTimeField.charTyped(typedChar, modifiers);
			}
		}

		return flag;
	}

	public boolean charNumeric(char typedChar) {
		return (typedChar >= '0' && typedChar <= '9');
	}

	public boolean isColorNotHovered() {
		for (ColorButton colorButton : colorButtons) {
			if (colorButton.isHoveredOrFocused())
				return false;
		}
		return true;
	}

}