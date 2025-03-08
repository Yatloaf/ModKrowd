package dev.yatloaf.modkrowd;

import dev.yatloaf.modkrowd.config.FeatureTree;
import dev.yatloaf.modkrowd.config.Config;
import dev.yatloaf.modkrowd.config.screen.ConfigScreen;
import dev.yatloaf.modkrowd.config.SyncedConfig;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.KickedMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.WhereamiMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.CubeKrowdMessageCache;
import dev.yatloaf.modkrowd.cubekrowd.common.CKStuff;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MissileWarsSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.mixin.KeyBindingAccessor;
import dev.yatloaf.modkrowd.util.Util;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class ModKrowd implements ClientModInitializer {
	public static final String MODID = "modkrowd";
	public static final Version VERSION = FabricLoader.getInstance()
			.getModContainer(MODID).orElseThrow(AssertionError::new).getMetadata().getVersion();
    public static final Object USELESS = new Object();

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(MODID + ".json").toFile();
	public static final SyncedConfig CONFIG = new SyncedConfig();

	public static Subserver currentSubserver = Subservers.NONE;
	public static TabListCache currentTabListCache = null; // Else ExceptionInInitializerError
	public static long tick = 0; // Chances are this won't overflow in your lifetime

	public static KeyBinding OPTIONS_KEY;
	public static KeyBinding TOGGLE_MESSAGE_PREVIEW_KEY;
	public static KeyBinding NEXT_SUBSERVER_KEY;
	public static boolean INIT = false; // TODO: Better, somehow

	private static boolean pendingTabListCache = true;
	private static MessageCache currentMessage = null;
	private static MwSwitchStatus mwSwitchStatus = MwSwitchStatus.IDLE;
	private static long mwSwitchTick = 0;
	private static int mwSwitchIndex = 0;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Thank you, mod template. I will make sure to run only the safest code in this method.

		String[] hello = {
				"[ModKrowd] Haiii world :3",
				"[ModKrowd] Hewwo world ^w^",
				"[ModKrowd] Greetings Earth!"
		};
		LOGGER.info(hello[ThreadLocalRandom.current().nextInt(hello.length)]);

		OPTIONS_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.modkrowd.options",
				InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.modkrowd"));
		TOGGLE_MESSAGE_PREVIEW_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.modkrowd.toggle_message_preview",
				InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.modkrowd"));
		NEXT_SUBSERVER_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.modkrowd.next_subserver",
				InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.modkrowd"));
		INIT = true;

		ClientLifecycleEvents.CLIENT_STARTED.register(ModKrowd::onClientStarted);
		ClientPlayConnectionEvents.JOIN.register(ModKrowd::onJoin);
		ClientPlayConnectionEvents.DISCONNECT.register(ModKrowd::onDisconnect);
		ClientTickEvents.END_CLIENT_TICK.register(ModKrowd::onEndClientTick);
		ClientReceiveMessageEvents.ALLOW_GAME.register(ModKrowd::allowMessage);
		ClientReceiveMessageEvents.MODIFY_GAME.register(ModKrowd::modifyMessage);
		ClientLifecycleEvents.CLIENT_STOPPING.register(ModKrowd::onClientStopping);
	}

	public static void invalidateTabListCache() {
		pendingTabListCache = true;
	}

	public static void checkTabListCache() {
		if (pendingTabListCache) {
			TabListCache candidate = TabListCache.tryNew();
			if (candidate != null) {
				pendingTabListCache = false;
				currentTabListCache = candidate;
				CONFIG.onTabList(currentTabListCache);
			}
		}
	}

	public static ConfigScreen createConfigScreen(Screen parent) {
		return new ConfigScreen(parent, CONFIG, ModKrowd::saveConfig, ModKrowd::cancelConfig);
	}

	private static void saveConfig(Config config) {
		CONFIG.copyFromConfig(config);
		CONFIG.trySerialize(CONFIG_FILE);
	}

	private static void cancelConfig(Config config) {
		CONFIG.updateTab(config);
	}

	public static void startSwitchingMissileWarsLobby(int delay) {
		if (mwSwitchStatus != MwSwitchStatus.CONNECTING) {
			mwSwitchTick = tick + delay;
			mwSwitchStatus = MwSwitchStatus.DELAY;
			tickSwitchingMissileWarsLobby();
		}
	}

	private static void tickSwitchingMissileWarsLobby() {
		if (mwSwitchStatus == MwSwitchStatus.DELAY && mwSwitchTick <= tick) {
			mwSwitchStatus = MwSwitchStatus.CONNECTING;
			if (currentSubserver instanceof MissileWarsSubserver mwSubserver) {
				mwSubserver.tryConnectNext(MinecraftClient.getInstance().getNetworkHandler(), mwSwitchIndex);
			}
		}
	}

	private static void onClientStarted(MinecraftClient client) {
		// Every mod should have had a chance to register within onInitialize or onInitializeClient by now
		FeatureTree.closeRegisteringExtensions();

		CONFIG.tryDeserialize(CONFIG_FILE);
		CONFIG.onInitEnable(client);
	}

	private static void onJoin(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
		invalidateTabListCache(); // *Hopefully* late enough for inGameHud to be initialized

		mwSwitchStatus = MwSwitchStatus.IDLE;
		mwSwitchTick = 0;
		mwSwitchIndex = 0;

		ServerInfo info = handler.getServerInfo();
		if (info != null && CKStuff.addressIsCubeKrowd(info.address)) {
			currentSubserver = Subservers.PENDING;
			Util.sendCommandPacket(handler, CKStuff.SUBSERVER_COMMAND);
		} else {
			currentSubserver = Subservers.NONE;
		}
		CONFIG.onJoin(handler, client);
	}

	private static void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
		currentSubserver = Subservers.NONE;
		CONFIG.onDisconnect(handler, client);
		CONFIG.updateFeatures();
	}

	private static void onEndClientTick(MinecraftClient client) {
		tick++;

		tickSwitchingMissileWarsLobby();
		tickKeys(client);
		checkTabListCache();

		CONFIG.onEndTick(client);
	}

	private static void tickKeys(MinecraftClient client) {
		if (OPTIONS_KEY.wasPressed()) {
			client.setScreen(createConfigScreen(client.currentScreen));
			((KeyBindingAccessor) OPTIONS_KEY).callReset();
		}
		if (NEXT_SUBSERVER_KEY.wasPressed()) {
			startSwitchingMissileWarsLobby(0);
			((KeyBindingAccessor) NEXT_SUBSERVER_KEY).callReset();
		}
	}

	private static boolean allowMessage(Text message, boolean overlay) {
		currentMessage = MessageCache.of(TextCache.of((MutableText) message), currentSubserver);

		if (currentMessage instanceof CubeKrowdMessageCache ckCache) {
			MinecraftClient client = MinecraftClient.getInstance();

			if (currentSubserver == Subservers.PENDING) {
				WhereamiMessage whereamiMessage = ckCache.whereamiMessageFast();
				if (whereamiMessage.isReal()) {
					currentSubserver = whereamiMessage.subserver();
					CONFIG.updateFeatures();
					CONFIG.onJoinUpdated(client.getNetworkHandler(), client);
					return false;
				}
			}

			if (mwSwitchStatus == MwSwitchStatus.CONNECTING && currentSubserver instanceof MissileWarsSubserver mwSubserver) {
				KickedMessage kickedMessage = ckCache.kickedMessageFast();
				if (kickedMessage.isReal() && kickedMessage.subserver() instanceof MissileWarsSubserver || ckCache.unavailableMessageFast().isReal()) {
					mwSwitchIndex += 1;
					if (!mwSubserver.tryConnectNext(client.getNetworkHandler(), mwSwitchIndex)) {
						mwSwitchStatus = MwSwitchStatus.IDLE;
						mwSwitchTick = 0;
						mwSwitchIndex = 0;
					}
				}
			}
		}

		return CONFIG.allowMessage(currentMessage, overlay);
	}

	private static Text modifyMessage(Text message, boolean overlay) {
		if (currentMessage != null && currentMessage.original.text() == message) {
			MutableText value = currentMessage.themedOrDefault().text();
			currentMessage = null;
            return value;
        } else {
			// Already modified by some other mod
			return message;
		}
	}

	private static void onClientStopping(MinecraftClient client) {
		CONFIG.trySerialize(CONFIG_FILE);
	}

	private enum MwSwitchStatus {
		IDLE,
		DELAY,
		CONNECTING
	}
}