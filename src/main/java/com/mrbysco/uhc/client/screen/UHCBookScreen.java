package com.mrbysco.uhc.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

	protected final ArrayList<EditBox> textBoxList = new ArrayList<>();

	/**
	 * Buttons
	 */
	private PageButton buttonNextPage;
	private PageButton buttonPreviousPage;
	private Button buttonDone;

	private final ColorButton[] colorButtons = new ColorButton[17];

	private EditBox randSizeField;
	private EditBox maxTeamSizeField;

	private EditBox borderSizeField, borderCenterXField, borderCenterZField, difficultyField;
	private EditBox shrinkTimerField, shrinkSizeField, shrinkOvertimeField, timeLockTimerField, minMarkTimerField, nameTimerField, glowTimerField;
	private EditBox maxHealthField, spreadDistanceField, spreadMaxRangeField;
	private EditBox graceTimeField;
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
		minecraft.keyboardHandler.setSendRepeatsToGui(true);

		initValues();

		this.buttonDone = this.addRenderableWidget(new Button(this.width / 2 - 100, 196, 200, 20, Component.translatable("gui.done"),
				(button) -> this.minecraft.setScreen((Screen) null)));

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

		this.resetRandButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 94, j + 85, (button) -> {
			randomTeamSize = 6;
			sendPage1Packet();
			this.updateButtons();
		}));
		this.resetTeamSizeButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 94, j + 99, (button) -> {
			maxTeamSize = -1;
			sendPage1Packet();
			this.updateButtons();
		}));
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

		this.resetBorderSizeButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 68, j + 36, (button) -> {
			borderSize = 2048;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetBorderCenterXButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 92, j + 60, (button) -> {
			borderCenterX = originalBorderCenterX;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetBorderCenterZButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 92, j + 74, (button) -> {
			borderCenterZ = originalBorderCenterZ;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.centerCurrentXButton = this.addRenderableWidget(new UHCBookScreen.LocationButton(i + 43 + 76, j + 60, (button) -> {
			double playerX = editingPlayer.getX();
			borderCenterX = playerX;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.centerCurrentZButton = this.addRenderableWidget(new UHCBookScreen.LocationButton(i + 43 + 76, j + 74, (button) -> {
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
		this.resetShrinkTimerButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 92, j + 104, (button) -> {
			shrinkTimer = 60;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetShrinkSizeButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 92, j + 116, (button) -> {
			shrinkSize = 256;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.resetShrinkOverTimeButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 92, j + 128, (button) -> {
			shrinkOvertime = 60;
			sendPage2Packet();
			this.updateButtons();
		}));
		this.shrinkModeButton = this.addRenderableWidget(new TextButton(i + 43 + 31, j + 140, Component.literal(saveData.getShrinkMode()), minecraft, (button) -> {
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
		this.resetTimeLockTimerButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 80, j + 38, (button) -> {
			timeLockTimer = 60;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.resetMinuteMarkTimerButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 80, j + 77, (button) -> {
			minuteMarkTime = 30;
			sendPage3Packet();
			this.updateButtons();
		}));
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
		this.resetNameTimerButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 80, j + 103, (button) -> {
			nameTimer = 30;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.glowButton = this.addRenderableWidget(new BooleanButton(i + 43 + 60, j + 119, saveData.isTimedGlow(), (button) -> {
			boolean flag = saveData.isTimedGlow();
			timedGlow = !flag;
			sendPage3Packet();
			this.updateButtons();
		}));
		this.resetGlowTimerButton = this.addRenderableWidget(new UHCBookScreen.ResetButton(i + 43 + 80, j + 131, (button) -> {
			glowTime = 30;
			sendPage3Packet();
			this.updateButtons();
		}));


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

		this.UHCStartButton = this.addRenderableWidget(new UHCBookScreen.StartButton(i + 43 + 7, j + 132, (button) -> {
			startPacket();
			this.minecraft.setScreen((Screen) null);
		}));

		randSizeField = new EditBox(font, i + 43 + 75, j + 89, 20, 8, Component.empty());
		setupField(randSizeField, 2, 0xFFFFAA00, String.valueOf(saveData.getRandomTeamSize()));

		maxTeamSizeField = new EditBox(font, i + 43 + 75, j + 101, 20, 8, Component.empty());
		setupField(maxTeamSizeField, 2, 0xFFFFAA00, String.valueOf(saveData.getMaxTeamSize()));

		borderSizeField = new EditBox(font, i + 43 + 36, j + 40, 32, 8, Component.empty());
		setupField(borderSizeField, 4, 0xFFFFAA00, String.valueOf(saveData.getBorderSize()));

		borderCenterXField = new EditBox(font, i + 55, j + 64, 42, 8, Component.empty());
		setupField(borderCenterXField, 6, 0xFFFFAA00, String.valueOf(saveData.getBorderCenterX()));

		borderCenterZField = new EditBox(font, i + 55, j + 76, 42, 8, Component.empty());
		setupField(borderCenterZField, 6, 0xFFFFAA00, String.valueOf(saveData.getBorderCenterZ()));

		difficultyField = new EditBox(font, i + 43 + 52, j + 144, 14, 8, Component.empty());
		setupField(difficultyField, 1, 0xFFFFAA00, String.valueOf(saveData.getDifficulty()));

		shrinkTimerField = new EditBox(font, i + 43 + 52, j + 107, 32, 8, Component.empty());
		setupField(shrinkTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkTimer()));

		shrinkSizeField = new EditBox(font, i + 43 + 28, j + 118, 32, 8, Component.empty());
		setupField(shrinkSizeField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkSize()));

		shrinkOvertimeField = new EditBox(font, i + 43 + 32, j + 129, 32, 8, Component.empty());
		setupField(shrinkOvertimeField, 4, 0xFFFFAA00, String.valueOf(saveData.getShrinkOvertime()));

		timeLockTimerField = new EditBox(font, i + 43 + 52, j + 41, 32, 8, Component.empty());
		setupField(timeLockTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getTimeLockTimer()));

		minMarkTimerField = new EditBox(font, i + 43 + 38, j + 80, 32, 8, Component.empty());
		setupField(minMarkTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getMinuteMarkTime()));

		nameTimerField = new EditBox(font, i + 43 + 38, j + 106, 32, 8, Component.empty());
		setupField(nameTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getNameTimer()));

		glowTimerField = new EditBox(font, i + 43 + 38, j + 133, 32, 8, Component.empty());
		setupField(glowTimerField, 4, 0xFFFFAA00, String.valueOf(saveData.getGlowTime()));

		maxHealthField = new EditBox(font, i + 43 + 60, j + 69, 32, 8, Component.empty());
		setupField(maxHealthField, 4, 0xFFFFAA00, String.valueOf(saveData.getMaxHealth()));

		spreadDistanceField = new EditBox(font, i + 43 + 60, j + 98, 32, 8, Component.empty());
		setupField(spreadDistanceField, 4, 0xFFFFAA00, String.valueOf(saveData.getSpreadDistance()));

		spreadMaxRangeField = new EditBox(font, i + 43 + 60, j + 110, 32, 8, Component.empty());
		setupField(spreadMaxRangeField, 4, 0xFFFFAA00, String.valueOf(saveData.getSpreadMaxRange()));

		graceTimeField = new EditBox(font, i + 43 + 60, j + 41, 32, 8, Component.empty());
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

	public void setupField(EditBox field, int maxLength, int color, String text) {
		field.setFocus(false);
		field.setCanLoseFocus(true);
		field.setMaxLength(maxLength);
		field.setValue(text);
		field.setBordered(false);
		field.setTextColor(color);
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
			if (!randSizeField.getMessage().getString().equals(String.valueOf(saveData.getRandomTeamSize())) && !randSizeField.isFocused())
				randSizeField.setValue(String.valueOf(saveData.getRandomTeamSize()));

			if (!maxTeamSizeField.getMessage().getString().equals(String.valueOf(saveData.getMaxTeamSize())) && !maxTeamSizeField.isFocused())
				maxTeamSizeField.setValue(String.valueOf(saveData.getMaxTeamSize()));

			if (!difficultyField.getMessage().getString().equals(String.valueOf(saveData.getDifficulty())) && !difficultyField.isFocused())
				difficultyField.setValue(String.valueOf(saveData.getDifficulty()));

			if (collisionButton.getBoolean() != saveData.isTeamCollision())
				collisionButton.setBoolean(saveData.isTeamCollision());

			if (damageButton.getBoolean() != saveData.isFriendlyFire())
				damageButton.setBoolean(saveData.isFriendlyFire());

			if (teamsLockedButton.getBoolean() != saveData.areTeamsLocked())
				teamsLockedButton.setBoolean(saveData.areTeamsLocked());
		}

		if (this.currPage == 1) {
			if (!borderSizeField.getMessage().getString().equals(String.valueOf(saveData.getBorderSize())) && !borderSizeField.isFocused())
				borderSizeField.setValue(String.valueOf(saveData.getBorderSize()));

			if (!borderCenterXField.getMessage().getString().equals(String.valueOf(saveData.getBorderCenterX())) && !borderCenterXField.isFocused())
				borderCenterXField.setValue(String.valueOf(saveData.getBorderCenterX()));

			if (!borderCenterZField.getMessage().getString().equals(String.valueOf(saveData.getBorderCenterZ())) && !borderCenterZField.isFocused())
				borderCenterZField.setValue(String.valueOf(saveData.getBorderCenterZ()));

			if (shrinkButton.getBoolean() != saveData.isShrinkEnabled())
				shrinkButton.setBoolean(saveData.isShrinkEnabled());

			if (!shrinkTimerField.getMessage().getString().equals(String.valueOf(saveData.getShrinkTimer())) && !shrinkTimerField.isFocused())
				shrinkTimerField.setValue(String.valueOf(saveData.getShrinkTimer()));

			if (!shrinkSizeField.getMessage().getString().equals(String.valueOf(saveData.getShrinkSize())) && !shrinkSizeField.isFocused())
				shrinkSizeField.setValue(String.valueOf(saveData.getShrinkSize()));

			if (!shrinkOvertimeField.getMessage().getString().equals(String.valueOf(saveData.getShrinkOvertime())) && !shrinkOvertimeField.isFocused())
				shrinkOvertimeField.setValue(String.valueOf(saveData.getShrinkOvertime()));

			if (!shrinkModeButton.getMessage().getString().equals(saveData.getShrinkMode()))
				shrinkModeButton.setMessage(Component.literal(saveData.getShrinkMode()));
		}

		if (this.currPage == 2) {
			if (timeLockButton.getBoolean() != saveData.isTimeLock())
				timeLockButton.setBoolean(saveData.isTimeLock());

			if (!Objects.equals(timeLockTimerField.getMessage().getString(), String.valueOf(saveData.getTimeLockTimer())) && !timeLockTimerField.isFocused())
				timeLockTimerField.setValue(String.valueOf(saveData.getTimeLockTimer()));

			if (!timeModeButton.getMessage().getString().equals(saveData.getTimeMode()))
				timeModeButton.setMessage(Component.literal(saveData.getTimeMode()));

			if (minuteMarkButton.getBoolean() != saveData.isMinuteMark())
				minuteMarkButton.setBoolean(saveData.isMinuteMark());

			if (!minMarkTimerField.getMessage().getString().equals(String.valueOf(saveData.getMinuteMarkTime())) && !minMarkTimerField.isFocused())
				minMarkTimerField.setValue(String.valueOf(saveData.getMinuteMarkTime()));

			if (nameButton.getBoolean() != saveData.isTimedNames())
				nameButton.setBoolean(saveData.isTimedNames());

			if (!nameTimerField.getMessage().getString().equals(String.valueOf(saveData.getNameTimer())) && !nameTimerField.isFocused())
				nameTimerField.setValue(String.valueOf(saveData.getNameTimer()));

			if (glowButton.getBoolean() != saveData.isTimedGlow())
				glowButton.setBoolean(saveData.isTimedGlow());

			if (!glowTimerField.getMessage().getString().equals(String.valueOf(saveData.getGlowTime())) && !glowTimerField.isFocused())
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

			if (!maxHealthField.getMessage().getString().equals(String.valueOf(saveData.getMaxHealth())) && !maxHealthField.isFocused())
				maxHealthField.setValue(String.valueOf(saveData.getMaxHealth()));

			if (randomSpawnButton.getBoolean() != saveData.isRandomSpawns())
				randomSpawnButton.setBoolean(saveData.isRandomSpawns());

			if (!spreadDistanceField.getMessage().getString().equals(String.valueOf(saveData.getMaxHealth())) && !spreadDistanceField.isFocused())
				spreadDistanceField.setValue(String.valueOf(saveData.getSpreadDistance()));

			if (!spreadMaxRangeField.getMessage().getString().equals(String.valueOf(saveData.getMaxHealth())) && !spreadMaxRangeField.isFocused())
				spreadMaxRangeField.setValue(String.valueOf(saveData.getSpreadMaxRange()));

			if (spreadRespectTeamButton.getBoolean() != saveData.isSpreadRespectTeam())
				spreadRespectTeamButton.setBoolean(saveData.isSpreadRespectTeam());
		}

		if (this.currPage == 5) {
			if (graceTimeButton.getBoolean() != saveData.isGraceEnabled())
				graceTimeButton.setBoolean(saveData.isGraceEnabled());

			if (!graceTimeField.getMessage().getString().equals(String.valueOf(saveData.getGraceTime())) && !graceTimeField.isFocused())
				graceTimeField.setValue(String.valueOf(saveData.getGraceTime()));
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void removed() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}

	private void updateButtons() {
		this.buttonNextPage.visible = (this.currPage < this.bookTotalPages - 1);
		this.buttonPreviousPage.visible = this.currPage > 0;
		this.buttonDone.visible = true;

		for (ColorButton colorButton : colorButtons) {
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

		this.borderSizeField.setEditable(this.currPage == 1);
		this.borderCenterXField.setEditable(this.currPage == 1);
		this.borderCenterZField.setEditable(this.currPage == 1);

		this.shrinkTimerField.setVisible(this.currPage == 1);
		this.shrinkTimerField.setEditable(this.currPage == 1);
		this.shrinkSizeField.setVisible(this.currPage == 1);
		this.shrinkSizeField.setEditable(this.currPage == 1);
		this.shrinkOvertimeField.setVisible(this.currPage == 1);
		this.shrinkOvertimeField.setEditable(this.currPage == 1);

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
		this.timeLockTimerField.setEditable(this.currPage == 2);
		this.minMarkTimerField.setVisible(this.currPage == 2);
		this.minMarkTimerField.setEditable(this.currPage == 2);
		this.nameTimerField.setVisible(this.currPage == 2);
		this.nameTimerField.setEditable(this.currPage == 2);
		this.glowTimerField.setVisible(this.currPage == 2);
		this.glowTimerField.setEditable(this.currPage == 2);

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
		this.maxHealthField.setEditable(this.currPage == 4);

		this.graceTimeButton.visible = this.currPage == 5;
		this.graceTimeField.setVisible(this.currPage == 5);
		this.graceTimeField.setEditable(this.currPage == 5);
		this.UHCStartButton.visible = this.currPage == 5;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.setFocused((GuiEventListener) null);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BookViewScreen.BOOK_LOCATION);

		int i = (this.width - 192) / 2;
		int j = 2;
		this.blit(matrixStack, i, 2, 0, 0, 192, 192);

		String s4 = I18n.get("book.pageIndicator", this.currPage + 1, this.bookTotalPages);

		int j1 = this.font.width(s4);
		this.font.draw(matrixStack, s4, i - j1 + 192 - 44, 18, 0);

		List<FormattedCharSequence> ShrinkModeShrink = new ArrayList<>();
		ShrinkModeShrink.add(Component.translatable("book.uhc.explain.shrinkmodeshrink").getVisualOrderText());
		ShrinkModeShrink.add(Component.translatable("book.uhc.explain.shrinkmodeshrink2").getVisualOrderText());
		List<FormattedCharSequence> ShrinkModeArena = new ArrayList<>();
		ShrinkModeArena.add(Component.translatable("book.uhc.explain.shrinkmodearena").getVisualOrderText());
		ShrinkModeArena.add(Component.translatable("book.uhc.explain.shrinkmodearena2").getVisualOrderText());
		List<FormattedCharSequence> ShrinkModeControl = new ArrayList<>();
		ShrinkModeControl.add(Component.translatable("book.uhc.explain.shrinkmodecontrol").getVisualOrderText());
		ShrinkModeControl.add(Component.translatable("book.uhc.explain.shrinkmodecontrol2").getVisualOrderText());

		Component minuteMessageString = Component.translatable("book.uhc.option.minutes");
		Component locationString = Component.translatable("book.uhc.option.location");
		Component resetString = Component.translatable("book.uhc.option.reset");
		Component TimeLockString = Component.translatable("book.uhc.option.timelock");
		Component minMarkString = Component.translatable("book.uhc.option.minmark");
		Component timedNameString = Component.translatable("book.uhc.option.timedname");
		Component timedGlowString = Component.translatable("book.uhc.option.timedglow");
		Component timeModeDayText = Component.translatable("book.uhc.option.timemodeday");
		Component timeModeNightText = Component.translatable("book.uhc.option.timemodenight");
		Component netherTravelString = Component.translatable("book.uhc.option.nether");
		Component regenPotionsString = Component.translatable("book.uhc.option.regenpotion");
		Component level2PotionsString = Component.translatable("book.uhc.option.level2potion");
		Component notchApplesString = Component.translatable("book.uhc.option.notchapples");
		Component autoCookString = Component.translatable("book.uhc.option.autocook");
		Component itemConvertString = Component.translatable("book.uhc.option.convertion");

		Component weatherString = Component.translatable("book.uhc.option.weather");
		Component mobGriefingString = Component.translatable("book.uhc.option.mobgriefing");
		Component customHealthString = Component.translatable("book.uhc.option.customhealth");
		Component randomSpawnString = Component.translatable("book.uhc.option.randomspawns");
		Component spreadDistanceString = Component.translatable("book.uhc.option.spreaddistance");
		Component spreadMaxRangeString = Component.translatable("book.uhc.option.spreadrange");
		Component spreadRespectTeamString = Component.translatable("book.uhc.option.spreadteams");

		Component graceString = Component.translatable("book.uhc.option.grace");

		super.render(matrixStack, mouseX, mouseY, partialTicks);
		if (this.currPage == 0) {
			Component teamSelect = Component.translatable("book.uhc.team.select");

			Component randSizeString = Component.translatable("book.uhc.option.randsize");
			this.font.draw(matrixStack, randSizeString, i + 43, j + 89, 0xFF555555);
			randSizeField.render(matrixStack, mouseX, mouseY, partialTicks);


			Component maxTeamSizeString = Component.translatable("book.uhc.option.maxteams");
			this.font.draw(matrixStack, maxTeamSizeString, i + 43, j + 101, 0xFF555555);
			maxTeamSizeField.render(matrixStack, mouseX, mouseY, partialTicks);

			boolean flag = hoverBoolean(mouseX, mouseY, maxTeamSizeField.x, maxTeamSizeField.y, maxTeamSizeField.getWidth(), maxTeamSizeField.getHeight());
			Component infinityString = Component.translatable("book.uhc.option.infinite");

			if (isColorNotHovered() && !flag && !teamsLockedButton.isHoveredOrFocused())
				this.font.draw(matrixStack, teamSelect, i + 65, j + 28, 0xFF555555);

			if (flag && !maxTeamSizeField.isFocused())
				drawCenteredString(matrixStack, font, infinityString, i + 91, j + 28, 0xFFFF5555);

			Component lockButton = Component.translatable("book.uhc.option.locked");

			if (teamsLockedButton.isHoveredOrFocused())
				drawCenteredString(matrixStack, font, lockButton, i + 93, j + 27, 0xFFFF5555);

			Component teamCollisionString = Component.translatable("book.uhc.option.collision");
			this.font.draw(matrixStack, teamCollisionString, i + 43, j + 118, 0xFF555555);

			Component teamDamageString = Component.translatable("book.uhc.option.damage");
			this.font.draw(matrixStack, teamDamageString, i + 43, j + 131, 0xFF555555);

			Component difficultyString = Component.translatable("book.uhc.option.difficulty");
			this.font.draw(matrixStack, difficultyString, i + 43, j + 144, 0xFF555555);
			difficultyField.render(matrixStack, mouseX, mouseY, partialTicks);
		}

		if (this.currPage == 1) {
			Component borderSizeString = Component.translatable("book.uhc.option.bordersize");
			this.font.draw(matrixStack, borderSizeString, i + 48, j + 28, 0xFF555555);
			borderSizeField.render(matrixStack, mouseX, mouseY, partialTicks);

			Component centerString = Component.translatable("book.uhc.option.bordercenter");
			this.font.draw(matrixStack, centerString, i + 42, j + 53, 0xFF555555);

			Component centerxString = Component.translatable("book.uhc.option.bordercenterx");
			this.font.draw(matrixStack, centerxString, i + 42, j + 64, 0xFF555555);
			borderCenterXField.render(matrixStack, mouseX, mouseY, partialTicks);

			Component centerZString = Component.translatable("book.uhc.option.bordercenterz");
			this.font.draw(matrixStack, centerZString, i + 42, j + 76, 0xFF555555);
			borderCenterZField.render(matrixStack, mouseX, mouseY, partialTicks);

			Component ShrinkString = Component.translatable("book.uhc.option.shrinkenabled");
			this.font.draw(matrixStack, ShrinkString, i + 38, j + 94, 0xFF555555);

			Component ShrinkTimerString = Component.translatable("book.uhc.option.shrinktimer");
			this.font.draw(matrixStack, ShrinkTimerString, i + 44, j + 107, 0xFF555555);
			shrinkTimerField.render(matrixStack, mouseX, mouseY, partialTicks);

			Component ShrinkSizeString = Component.translatable("book.uhc.option.shrinksize");
			this.font.draw(matrixStack, ShrinkSizeString, i + 44, j + 118, 0xFF555555);
			shrinkSizeField.render(matrixStack, mouseX, mouseY, partialTicks);

			Component ShrinkOvertimeString = Component.translatable("book.uhc.option.shrinkovertime");
			this.font.draw(matrixStack, ShrinkOvertimeString, i + 44, j + 129, 0xFF555555);
			shrinkOvertimeField.render(matrixStack, mouseX, mouseY, partialTicks);

			Component ShrinkModeString = Component.translatable("book.uhc.option.shrinkmode");
			this.font.draw(matrixStack, ShrinkModeString, i + 44, j + 140, 0xFF555555);

			boolean flag = hoverBoolean(mouseX, mouseY, shrinkTimerField.x, shrinkTimerField.y, shrinkTimerField.getWidth(), shrinkTimerField.getHeight());
			boolean flag1 = hoverBoolean(mouseX, mouseY, shrinkOvertimeField.x, shrinkOvertimeField.y, shrinkOvertimeField.getWidth(), shrinkOvertimeField.getHeight());
			if ((flag && !shrinkTimerField.isFocused()) || (flag1 && !shrinkOvertimeField.isFocused()))
				drawCenteredString(matrixStack, font, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
		}

		if (this.currPage == 2) {
			this.font.draw(matrixStack, TimeLockString, i + 38, j + 28, 0xFF555555);
			timeLockTimerField.render(matrixStack, mouseX, mouseY, partialTicks);

			Component TimeLockTimerString = Component.translatable("book.uhc.option.timelocktimer");
			this.font.draw(matrixStack, TimeLockTimerString, i + 44, j + 41, 0xFF555555);

			Component TimeLockModeString = Component.translatable("book.uhc.option.timelockmode");
			this.font.draw(matrixStack, TimeLockModeString, i + 44, j + 53, 0xFF555555);

			this.font.draw(matrixStack, minMarkString, i + 38, j + 68, 0xFF555555);

			Component minMarkTimerString = Component.translatable("book.uhc.option.minmarktime");
			this.font.draw(matrixStack, minMarkTimerString, i + 44, j + 80, 0xFF555555);
			minMarkTimerField.render(matrixStack, mouseX, mouseY, partialTicks);

			this.font.draw(matrixStack, timedNameString, i + 38, j + 94, 0xFF555555);

			Component timedNameTimerString = Component.translatable("book.uhc.option.timednametime");
			this.font.draw(matrixStack, timedNameTimerString, i + 44, j + 106, 0xFF555555);
			nameTimerField.render(matrixStack, mouseX, mouseY, partialTicks);

			this.font.draw(matrixStack, timedGlowString, i + 38, j + 122, 0xFF555555);

			Component timedGlowStringTimerString = Component.translatable("book.uhc.option.timedglowtime");
			this.font.draw(matrixStack, timedGlowStringTimerString, i + 44, j + 134, 0xFF555555);
			glowTimerField.render(matrixStack, mouseX, mouseY, partialTicks);

			boolean flag = hoverBoolean(mouseX, mouseY, timeLockTimerField.x, timeLockTimerField.y, timeLockTimerField.getWidth(), timeLockTimerField.getHeight());
			boolean flag1 = hoverBoolean(mouseX, mouseY, minMarkTimerField.x, minMarkTimerField.y, minMarkTimerField.getWidth(), minMarkTimerField.getHeight());
			if ((flag && !timeLockTimerField.isFocused()) || (flag1 && !minMarkTimerField.isFocused()))
				drawCenteredString(matrixStack, font, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
		}

		if (this.currPage == 3) {
			this.font.draw(matrixStack, regenPotionsString, i + 38, j + 28, 0xFF555555);

			this.font.draw(matrixStack, level2PotionsString, i + 38, j + 40, 0xFF555555);

			this.font.draw(matrixStack, notchApplesString, i + 38, j + 52, 0xFF555555);

			this.font.draw(matrixStack, autoCookString, i + 38, j + 68, 0xFF555555);

			this.font.draw(matrixStack, itemConvertString, i + 38, j + 80, 0xFF555555);

			this.font.draw(matrixStack, netherTravelString, i + 38, j + 98, 0xFF555555);

			Component healthInTabString = Component.translatable("book.uhc.option.healthtab");
			this.font.draw(matrixStack, healthInTabString, i + 38, j + 114, 0xFF555555);

			Component healthOnSideString = Component.translatable("book.uhc.option.healthside");
			this.font.draw(matrixStack, healthOnSideString, i + 38, j + 126, 0xFF555555);

			Component healthUnderNameString = Component.translatable("book.uhc.option.healthname");
			this.font.draw(matrixStack, healthUnderNameString, i + 38, j + 138, 0xFF555555);
		}

		if (this.currPage == 4) {
			this.font.draw(matrixStack, weatherString, i + 38, j + 28, 0xFF555555);

			this.font.draw(matrixStack, mobGriefingString, i + 38, j + 40, 0xFF555555);

			this.font.draw(matrixStack, customHealthString, i + 38, j + 57, 0xFF555555);

			Component healthMaxString = Component.translatable("book.uhc.option.maxhealth");
			this.font.draw(matrixStack, healthMaxString, i + 44, j + 69, 0xFF555555);
			maxHealthField.render(matrixStack, mouseX, mouseY, partialTicks);

			this.font.draw(matrixStack, randomSpawnString, i + 38, j + 86, 0xFF555555);

			this.font.draw(matrixStack, spreadDistanceString, i + 44, j + 98, 0xFF555555);
			spreadDistanceField.render(matrixStack, mouseX, mouseY, partialTicks);

			this.font.draw(matrixStack, spreadMaxRangeString, i + 44, j + 110, 0xFF555555);
			spreadMaxRangeField.render(matrixStack, mouseX, mouseY, partialTicks);

			this.font.draw(matrixStack, spreadRespectTeamString, i + 44, j + 122, 0xFF555555);

			Component healthExplain = Component.translatable("book.uhc.explain.healthExplain");
			boolean flag2 = hoverBoolean(mouseX, mouseY, maxHealthField.x, maxHealthField.y, maxHealthField.getWidth(), maxHealthField.getHeight());
			if (flag2 && !maxHealthField.isFocused())
				drawCenteredString(matrixStack, font, healthExplain, mouseX, mouseY + 5, 0xFFFF5555);
		}

		if (this.currPage == 5) {
			this.font.draw(matrixStack, graceString, i + 38, j + 28, 0xFF555555);

			graceTimeField.render(matrixStack, mouseX, mouseY, partialTicks);

			Component GraceTimerString = Component.translatable("book.uhc.option.gracetimer");
			this.font.draw(matrixStack, GraceTimerString, i + 44, j + 41, 0xFF555555);

			boolean flag = hoverBoolean(mouseX, mouseY, graceTimeField.x, graceTimeField.y, graceTimeField.getWidth(), graceTimeField.getHeight());
			if (flag && !timeLockTimerField.isFocused())
				drawCenteredString(matrixStack, font, minuteMessageString, mouseX, mouseY + 5, 0xFFFF5555);
		}

		if (shrinkModeButton.visible && shrinkModeButton.isHoveredOrFocused()) {
			Component ShrinkMode = Component.literal(saveData.getShrinkMode());
			if (ShrinkMode.getString().equals("Shrink")) {
				this.renderTooltip(matrixStack, ShrinkModeShrink, mouseX, mouseY);
			}
			if (ShrinkMode.getString().equals("Arena")) {
				this.renderTooltip(matrixStack, ShrinkModeArena, mouseX, mouseY);
			}
			if (ShrinkMode.getString().equals("Control")) {
				this.renderTooltip(matrixStack, ShrinkModeControl, mouseX, mouseY);
			}
		}

		if (timeModeButton.visible && timeModeButton.isHoveredOrFocused()) {
			Component TimeMode = Component.literal(saveData.getTimeMode());
			if (TimeMode.getString().equals("Day"))
				this.renderTooltip(matrixStack, timeModeDayText, mouseX, mouseY);
			if (TimeMode.getString().equals("Night"))
				this.renderTooltip(matrixStack, timeModeNightText, mouseX, mouseY);
		}

		if (this.currPage == 2 && hoverBoolean(mouseX, mouseY, i + 38, j + 28, font.width(TimeLockString), font.lineHeight)) {
			this.renderTooltip(matrixStack, Component.translatable("book.uhc.explain.timelock"), mouseX, mouseY);
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

		if (this.currPage == 2 && hoverBoolean(mouseX, mouseY, i + 38, j + 68, font.width(minMarkString), font.lineHeight)) {
			this.renderTooltip(matrixStack, MinuteMarkExplain, mouseX, mouseY);
		}
		if (this.currPage == 2 && hoverBoolean(mouseX, mouseY, i + 38, j + 94, font.width(timedNameString), font.lineHeight)) {
			this.renderTooltip(matrixStack, timedNameExplain, mouseX, mouseY);
		}
		if (this.currPage == 2 && hoverBoolean(mouseX, mouseY, i + 38, j + 122, font.width(timedGlowString), font.lineHeight)) {
			this.renderTooltip(matrixStack, timedGlowExplain, mouseX, mouseY);
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

		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 28, font.width(regenPotionsString), font.lineHeight)) {
			this.renderTooltip(matrixStack, regenPotionExplain, mouseX, mouseY);
		}
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 40, font.width(level2PotionsString), font.lineHeight)) {
			this.renderTooltip(matrixStack, level2PotionExplain, mouseX, mouseY);
		}
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 52, font.width(notchApplesString), font.lineHeight)) {
			this.renderTooltip(matrixStack, notchApplesExplain, mouseX, mouseY);
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
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 68, font.width(autoCookString), font.lineHeight)) {
			this.renderTooltip(matrixStack, autoSmeltExplain, mouseX, mouseY);
		}
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 38, j + 80, font.width(itemConvertString), font.lineHeight)) {
			this.renderTooltip(matrixStack, itemConvertExplain, mouseX, mouseY);
		}

		Component netherTravelExplain = Component.translatable("book.uhc.explain.nether");
		if (this.currPage == 3 && hoverBoolean(mouseX, mouseY, i + 35, j + 98, font.width(netherTravelString), font.lineHeight)) {
			this.renderTooltip(matrixStack, netherTravelExplain, mouseX, mouseY);
		}

		Component weatherExplain = Component.translatable("book.uhc.explain.weather");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 38, j + 28, font.width(weatherString), font.lineHeight)) {
			this.renderTooltip(matrixStack, weatherExplain, mouseX, mouseY);
		}
		Component mobGriefingExplain = Component.translatable("book.uhc.explain.mobgriefing");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 38, j + 40, font.width(mobGriefingString), font.lineHeight)) {
			this.renderTooltip(matrixStack, mobGriefingExplain, mouseX, mouseY);
		}
		Component customHealthExplain = Component.translatable("book.uhc.explain.customhealth");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 38, j + 57, font.width(customHealthString), font.lineHeight)) {
			this.renderTooltip(matrixStack, customHealthExplain, mouseX, mouseY);
		}
		List<FormattedCharSequence> randomSpawnsExplain = new ArrayList<>();
		randomSpawnsExplain.add(Component.translatable("book.uhc.explain.randomspawns").getVisualOrderText());
		randomSpawnsExplain.add(Component.translatable("book.uhc.explain.randomspawns2").getVisualOrderText());
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 38, j + 86, font.width(randomSpawnString), font.lineHeight)) {
			this.renderTooltip(matrixStack, randomSpawnsExplain, mouseX, mouseY);
		}
		Component spreadDistanceExplain = Component.translatable("book.uhc.explain.spreaddistance");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 44, j + 98, font.width(spreadDistanceString), font.lineHeight)) {
			this.renderTooltip(matrixStack, spreadDistanceExplain, mouseX, mouseY);
		}
		Component spreadMaxRangeExplain = Component.translatable("book.uhc.explain.spreadrange");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 44, j + 110, font.width(spreadMaxRangeString), font.lineHeight)) {
			this.renderTooltip(matrixStack, spreadMaxRangeExplain, mouseX, mouseY);
		}
		Component spreadRespectTeamExplain = Component.translatable("book.uhc.explain.spreadteams");
		if (this.currPage == 4 && hoverBoolean(mouseX, mouseY, i + 44, j + 122, font.width(spreadRespectTeamString), font.lineHeight)) {
			this.renderTooltip(matrixStack, spreadRespectTeamExplain, mouseX, mouseY);
		}
		List<FormattedCharSequence> gracePeriodExplain = new ArrayList<>();
		gracePeriodExplain.add(Component.translatable("book.uhc.explain.graceperiod").getVisualOrderText());
		gracePeriodExplain.add(Component.translatable("book.uhc.explain.graceperiod2").getVisualOrderText());
		if (this.currPage == 5 && hoverBoolean(mouseX, mouseY, i + 38, j + 28, font.width(graceString), font.lineHeight)) {
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
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		int middleWidth = (this.width - 192) / 2;
		int middleHeigth = 2;

		if (mouseButton == 0) {
			if (this.currPage == 0) {
				if (randSizeField.mouseClicked(mouseX, mouseY, mouseButton))
					randSizeField.setValue("");
				if (maxTeamSizeField.mouseClicked(mouseX, mouseY, mouseButton))
					maxTeamSizeField.setValue("");
				if (difficultyField.mouseClicked(mouseX, mouseY, mouseButton))
					difficultyField.setValue("");

				if (!randSizeField.isFocused())
					randSizeField.setValue(String.valueOf(saveData.getRandomTeamSize()));
				if (!maxTeamSizeField.isFocused())
					maxTeamSizeField.setValue(String.valueOf(saveData.getMaxTeamSize()));
				if (!difficultyField.isFocused())
					difficultyField.setValue(String.valueOf(saveData.getDifficulty()));
			}

			if (this.currPage == 1) {
				if (borderSizeField.mouseClicked(mouseX, mouseY, mouseButton))
					borderSizeField.setValue("");
				if (borderCenterXField.mouseClicked(mouseX, mouseY, mouseButton))
					borderCenterXField.setValue("");
				if (borderCenterZField.mouseClicked(mouseX, mouseY, mouseButton))
					borderCenterZField.setValue("");
				if (shrinkTimerField.mouseClicked(mouseX, mouseY, mouseButton))
					shrinkTimerField.setValue("");
				if (shrinkSizeField.mouseClicked(mouseX, mouseY, mouseButton))
					shrinkSizeField.setValue("");
				if (shrinkOvertimeField.mouseClicked(mouseX, mouseY, mouseButton))
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
				if (timeLockTimerField.mouseClicked(mouseX, mouseY, mouseButton))
					timeLockTimerField.setValue("");
				if (minMarkTimerField.mouseClicked(mouseX, mouseY, mouseButton))
					minMarkTimerField.setValue("");
				if (nameTimerField.mouseClicked(mouseX, mouseY, mouseButton))
					nameTimerField.setValue("");
				if (glowTimerField.mouseClicked(mouseX, mouseY, mouseButton))
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
				if (maxHealthField.mouseClicked(mouseX, mouseY, mouseButton))
					maxHealthField.setValue("");
				if (spreadDistanceField.mouseClicked(mouseX, mouseY, mouseButton))
					spreadDistanceField.setValue("");
				if (spreadMaxRangeField.mouseClicked(mouseX, mouseY, mouseButton))
					spreadMaxRangeField.setValue("");

				if (!maxHealthField.isFocused())
					maxHealthField.setValue(String.valueOf(saveData.getMaxHealth()));
				if (!spreadDistanceField.isFocused())
					spreadDistanceField.setValue(String.valueOf(saveData.getSpreadDistance()));
				if (!spreadMaxRangeField.isFocused())
					spreadMaxRangeField.setValue(String.valueOf(saveData.getSpreadMaxRange()));
			}

			if (this.currPage == 5) {
				if (graceTimeField.mouseClicked(mouseX, mouseY, mouseButton))
					graceTimeField.setValue("");

				if (!graceTimeField.isFocused())
					graceTimeField.setValue(String.valueOf(saveData.getGraceTime()));
			}
		}

		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		boolean flag = super.charTyped(typedChar, keyCode);

		if (this.currPage == 0) {
			if (randSizeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				randSizeField.charTyped(typedChar, keyCode);
			}
			if (maxTeamSizeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				maxTeamSizeField.charTyped(typedChar, keyCode);
			}
			if (difficultyField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				difficultyField.charTyped(typedChar, keyCode);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				if (randSizeField.isFocused()) {
					String randText = randSizeField.getMessage().getString();

					if (randText.isEmpty())
						randSizeField.setValue(String.valueOf(randomTeamSize));
					else {
						randomTeamSize = Integer.parseInt(randText);
						sendPage1Packet();
					}

					randSizeField.setFocus(false);
				}

				if (maxTeamSizeField.isFocused()) {
					String teamSize = maxTeamSizeField.getMessage().getString();

					if (teamSize.isEmpty() || Integer.parseInt(teamSize) > 14)
						maxTeamSizeField.setValue(String.valueOf(maxTeamSize));
					else {
						maxTeamSize = Integer.parseInt(teamSize);
						sendPage1Packet();
					}

					maxTeamSizeField.setFocus(false);
				}

				if (difficultyField.isFocused()) {
					String difficultyText = difficultyField.getMessage().getString();

					if (difficultyText.isEmpty() || Integer.parseInt(difficultyText) > 3)
						difficultyField.setValue(String.valueOf(difficulty));
					else {
						difficulty = Integer.parseInt(difficultyText);
						sendPage1Packet();
					}

					difficultyField.setFocus(false);
				}
			}
		}

		if (this.currPage == 1) {
			if (borderSizeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				borderSizeField.charTyped(typedChar, keyCode);
			}
			if (borderCenterXField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE
					|| keyCode == GLFW.GLFW_KEY_PERIOD || keyCode == GLFW.GLFW_KEY_MINUS)) {
				borderCenterXField.charTyped(typedChar, keyCode);
			}
			if (borderCenterZField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE
					|| keyCode == GLFW.GLFW_KEY_PERIOD || keyCode == GLFW.GLFW_KEY_MINUS)) {
				borderCenterZField.charTyped(typedChar, keyCode);
			}
			if (shrinkTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				shrinkTimerField.charTyped(typedChar, keyCode);
			}
			if (shrinkSizeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				shrinkSizeField.charTyped(typedChar, keyCode);
			}
			if (shrinkOvertimeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				shrinkOvertimeField.charTyped(typedChar, keyCode);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Border Size Field */
				if (borderSizeField.isFocused()) {
					String borderSizeText = borderSizeField.getMessage().getString();

					if (borderSizeText.isEmpty())
						borderSizeField.setValue(String.valueOf(borderSize));
					else {
						borderSize = Integer.parseInt(borderSizeText);
						sendPage2Packet();
					}

					borderSizeField.setFocus(false);
				}
				/* Border Center X Field */
				if (borderCenterXField.isFocused()) {
					String borderX = borderCenterXField.getMessage().getString();

					if (borderX.isEmpty())
						borderCenterXField.setValue(String.valueOf(borderCenterX));
					else {
						borderCenterX = Integer.parseInt(borderX);
						sendPage2Packet();
					}

					borderCenterXField.setFocus(false);
				}
				/* Border Center Z Field */
				if (borderCenterZField.isFocused()) {
					String borderZ = borderCenterZField.getMessage().getString();

					if (borderZ.isEmpty())
						borderCenterZField.setValue(String.valueOf(borderCenterZ));
					else {
						borderCenterZ = Integer.parseInt(borderZ);
						sendPage2Packet();
					}

					borderCenterZField.setFocus(false);
				}
				/* Shrink Timer Field */
				if (shrinkTimerField.isFocused()) {
					String shrinkTimerText = shrinkTimerField.getMessage().getString();

					if (shrinkTimerText.isEmpty())
						shrinkTimerField.setValue(String.valueOf(shrinkTimer));
					else {
						shrinkTimer = Integer.parseInt(shrinkTimerText);
						sendPage2Packet();
					}

					shrinkTimerField.setFocus(false);
				}
				/* Shrink Size Field */
				if (shrinkSizeField.isFocused()) {
					String shrinkSizeText = shrinkSizeField.getMessage().getString();

					if (shrinkSizeText.isEmpty())
						shrinkSizeField.setValue(String.valueOf(shrinkSize));
					else {
						shrinkSize = Integer.parseInt(shrinkSizeText);
						sendPage2Packet();
					}

					shrinkSizeField.setFocus(false);
				}
				/* Shrink Over Time Field */
				if (shrinkOvertimeField.isFocused()) {
					String ShrinkOverTimeText = shrinkOvertimeField.getMessage().getString();

					if (ShrinkOverTimeText.isEmpty())
						shrinkOvertimeField.setValue(String.valueOf(shrinkOvertime));
					else {
						shrinkOvertime = Integer.parseInt(ShrinkOverTimeText);
						sendPage2Packet();
					}

					shrinkOvertimeField.setFocus(false);
				}
			}
		}
		if (this.currPage == 2) {
			if (timeLockTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				timeLockTimerField.charTyped(typedChar, keyCode);
			}
			if (minMarkTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				minMarkTimerField.charTyped(typedChar, keyCode);
			}
			if (nameTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				nameTimerField.charTyped(typedChar, keyCode);
			}
			if (glowTimerField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				glowTimerField.charTyped(typedChar, keyCode);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Shrink Timer Field */
				if (timeLockTimerField.isFocused()) {
					String timeLockTimerText = timeLockTimerField.getMessage().getString();

					if (timeLockTimerText.isEmpty())
						timeLockTimerField.setValue(String.valueOf(timeLockTimer));
					else {
						timeLockTimer = Integer.parseInt(timeLockTimerText);
						sendPage3Packet();
					}

					timeLockTimerField.setFocus(false);
				}
				/* Minute Mark Timer Field */
				if (minMarkTimerField.isFocused()) {
					String minuteMarkTimerText = minMarkTimerField.getMessage().getString();

					if (minuteMarkTimerText.isEmpty())
						minMarkTimerField.setValue(String.valueOf(minuteMarkTime));
					else {
						minuteMarkTime = Integer.parseInt(minuteMarkTimerText);
						sendPage3Packet();
					}

					minMarkTimerField.setFocus(false);
				}
				/* Name Timer Field */
				if (nameTimerField.isFocused()) {
					String nameTimerText = nameTimerField.getMessage().getString();

					if (nameTimerText.isEmpty())
						nameTimerField.setValue(String.valueOf(nameTimer));
					else {
						nameTimer = Integer.parseInt(nameTimerText);
						sendPage3Packet();
					}

					nameTimerField.setFocus(false);
				}
				/* Glow Timer Field */
				if (glowTimerField.isFocused()) {
					String glowTimer = glowTimerField.getMessage().getString();

					if (glowTimer.isEmpty())
						glowTimerField.setValue(String.valueOf(glowTime));
					else {
						glowTime = Integer.parseInt(glowTimer);
						sendPage3Packet();
					}

					glowTimerField.setFocus(false);
				}
			}
		}
		if (this.currPage == 4) {
			if (maxHealthField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				maxHealthField.charTyped(typedChar, keyCode);
			}
			if (spreadDistanceField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				spreadDistanceField.charTyped(typedChar, keyCode);
			}
			if (spreadMaxRangeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				spreadMaxRangeField.charTyped(typedChar, keyCode);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Max Health Field */
				if (maxHealthField.isFocused()) {
					String maxHealthText = maxHealthField.getMessage().getString();

					if (maxHealthText.isEmpty())
						maxHealthField.setValue(String.valueOf(maxHealth));
					else {
						maxHealth = Integer.parseInt(maxHealthText);
						sendPage5Packet();
					}

					maxHealthField.setFocus(false);
				}
				/* Spread Distance Field */
				if (spreadDistanceField.isFocused()) {
					String maxDistanceText = spreadDistanceField.getMessage().getString();

					if (maxDistanceText.isEmpty())
						spreadDistanceField.setValue(String.valueOf(spreadDistance));
					else {
						spreadDistance = Integer.parseInt(maxDistanceText);
						sendPage5Packet();
					}

					spreadDistanceField.setFocus(false);
				}
				/* Spread Max Range Field */
				if (spreadMaxRangeField.isFocused()) {
					String maxRangeText = spreadMaxRangeField.getMessage().getString();

					if (maxRangeText.isEmpty())
						spreadMaxRangeField.setValue(String.valueOf(spreadMaxRange));
					else {
						spreadMaxRange = Integer.parseInt(maxRangeText);
						sendPage5Packet();
					}

					spreadMaxRangeField.setFocus(false);
				}
			}
		}
		if (this.currPage == 5) {
			if (graceTimeField.isFocused() && (charNumeric(typedChar) || keyCode == GLFW.GLFW_KEY_BACKSPACE)) {
				graceTimeField.charTyped(typedChar, keyCode);
			}

			if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
				/* Grace Timer Field */
				if (graceTimeField.isFocused()) {
					String graceTimeText = graceTimeField.getMessage().getString();

					if (graceTimeText.isEmpty())
						graceTimeField.setValue(String.valueOf(graceTime));
					else {
						graceTime = Integer.parseInt(graceTimeText);
						sendPage6Packet();
					}

					graceTimeField.setFocus(false);
				}
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

	static class ResetButton extends Button {
		public ResetButton(int x, int y, Button.OnPress onPressIn) {
			super(x, y, 16, 13, Component.empty(), onPressIn);
		}

		/**
		 * Draws this button to the screen.
		 */
		public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, BookViewScreen.BOOK_LOCATION);
			int textureX = 0;
			int textureY = 218;
			if (this.isMouseOver(mouseX, mouseY))
				textureX += 16;

			blit(matrixStack, x, y, textureX, textureY, 16, 13);
			if (this.isHoveredOrFocused()) {
				this.renderToolTip(matrixStack, mouseX, mouseY);
			}
		}
	}

	static class LocationButton extends Button {
		public LocationButton(int x, int y, Button.OnPress onPressIn) {
			super(x, y, 14, 13, Component.empty(), onPressIn);
		}

		/**
		 * Draws this button to the screen.
		 */
		public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, BookViewScreen.BOOK_LOCATION);
			int textureX = 0;
			int textureY = 232;
			if (this.isMouseOver(mouseX, mouseY))
				textureX += 15;

			blit(matrixStack, x, y, textureX, textureY, 15, 13);
			if (this.isHoveredOrFocused()) {
				this.renderToolTip(matrixStack, mouseX, mouseY);
			}
		}
	}

	static class StartButton extends Button {

		public StartButton(int x, int y, Button.OnPress onPressIn) {
			super(x, y, 85, 22, Component.empty(), onPressIn);
		}

		/**
		 * Draws this button to the screen.
		 */
		public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, BookViewScreen.BOOK_LOCATION);
			int textureX = 30;
			int textureY = 232;
			if (this.isMouseOver(mouseX, mouseY))
				textureX += 85;

			blit(matrixStack, x, y, textureX, textureY, 85, 22);
			if (this.isHoveredOrFocused()) {
				this.renderToolTip(matrixStack, mouseX, mouseY);
			}
		}
	}

	public static class ColorButton extends Button {
		private final boolean solo;
		private final boolean randomize;
		public final int color;
		public final Component name;

		public final int textX;
		public final int textY;

		public ColorButton(int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, Component nameIn, Button.OnPress onPressIn) {
			super(x, y, widthIn, heightIn, Component.empty(), onPressIn);
			this.textX = textXIn;
			this.textY = textYIn;
			this.color = colorIn;
			this.name = nameIn;
			this.solo = false;
			this.randomize = false;
		}

		public ColorButton(int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, Component nameIn, boolean soloIn, Button.OnPress onPressIn) {
			super(x, y, widthIn, heightIn, Component.empty(), onPressIn);
			this.textX = textXIn;
			this.textY = textYIn;
			this.color = colorIn;
			this.name = nameIn;
			this.solo = soloIn;
			this.randomize = false;
		}

		public ColorButton(int x, int y, int widthIn, int heightIn, int textXIn, int textYIn, int colorIn, Component nameIn, boolean soloIn, boolean randomizeIn, Button.OnPress onPressIn) {
			super(x, y, widthIn, heightIn, Component.empty(), onPressIn);
			this.textX = textXIn;
			this.textY = textYIn;
			this.color = colorIn;
			this.name = nameIn;
			this.solo = false;
			this.randomize = randomizeIn;
		}

		@Override
		public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				Font font = Minecraft.getInstance().font;
				this.isHovered = isMouseOver(mouseX, mouseY);

				matrixStack.pushPose();
				RenderSystem.setShader(GameRenderer::getPositionColorShader);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

				String randomizeMessage = I18n.get("book.uhc.team.randomizer");

				if (!this.solo && !this.randomize) {
					GuiComponent.fill(matrixStack, this.x, this.y, this.x + width, this.y + height, color);
				} else {
					GuiComponent.fill(matrixStack, this.x, this.y, this.x + width, this.y + height, 0xFF555555);
					GuiComponent.fill(matrixStack, this.x + 1, this.y + 1, this.x + width - 1, this.y + height - 1, color);

					if (this.randomize)
						drawCenteredString(matrixStack, font, randomizeMessage, textX - 6, textY, 0xFFFFFF55);
				}

				String joinMessage = I18n.get("book.uhc.team.hover", name.getString());
				if (this.randomize) {
					joinMessage = I18n.get("book.uhc.team.randomize");
				}

				if (this.isHovered) {
					if (this.randomize)
						drawCenteredString(matrixStack, font, joinMessage, textX, textY - 44, 0xFFFF5555);
					else
						drawCenteredString(matrixStack, font, joinMessage, textX, textY, color);
				}


				matrixStack.popPose();
			}
		}
	}
}