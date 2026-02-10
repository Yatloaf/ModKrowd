package dev.yatloaf.modkrowd;

import com.mojang.blaze3d.platform.InputConstants;
import dev.yatloaf.modkrowd.config.Config;
import dev.yatloaf.modkrowd.config.SyncedConfig;
import dev.yatloaf.modkrowd.config.screen.ConfigScreen;
import dev.yatloaf.modkrowd.cubekrowd.common.CubeKrowd;
import dev.yatloaf.modkrowd.cubekrowd.message.KickedMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.message.UnavailableMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.WhereamiMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabDecoCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.mixin.ClientCommonPacketListenerImplAccessor;
import dev.yatloaf.modkrowd.mixin.KeyMappingAccessor;
import dev.yatloaf.modkrowd.util.Util;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Random;

public class ModKrowd implements ClientModInitializer {
	public static final String MODID = "modkrowd";
	public static final Version VERSION = FabricLoader.getInstance()
			.getModContainer(MODID).orElseThrow(AssertionError::new).getMetadata().getVersion();
    public static final String[] HELLO = {
            "[ModKrowd] Haiii world :3",
            "[ModKrowd] Hewwo world ^w^",
            "[ModKrowd] Greetings Earth!",
            "[ModKrowd] Stay Hydrintegrated!",
    };
    public static final Object USELESS = new Object();
    public static final FontDescription NARROW_FONT = new FontDescription.Resource(ResourceLocation.fromNamespaceAndPath("modkrowd", "narrow"));
    public static final Style NARROW_FONT_STYLE = Style.EMPTY.withFont(NARROW_FONT);

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(MODID + ".json").toFile();
	public static final SyncedConfig CONFIG = new SyncedConfig();
    public static final TabListCache TAB_LIST = new TabListCache();
    public static final TabDecoCache TAB_DECO = new TabDecoCache();

    public static Subserver currentSubserver = Subservers.NONE;
	public static long tick = 0; // Chances are this won't overflow in your lifetime

	public static KeyMapping OPTIONS_KEY;
	public static KeyMapping NEXT_SUBSERVER_KEY;
	public static boolean INIT = false; // TODO: Better, somehow

	private static SwitchStatus switchStatus = SwitchIdle.INSTANCE;

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Thank you, mod template. I will make sure to run only the safest code in this method.

		LOGGER.info(HELLO[new Random().nextInt(HELLO.length)]);

		KeyMapping.Category category = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("modkrowd", "modkrowd"));
		OPTIONS_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.modkrowd.options",
				InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, category));
		NEXT_SUBSERVER_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.modkrowd.next_subserver",
				InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, category));
		INIT = true;

		ClientConfigurationConnectionEvents.COMPLETE.register(ModKrowd::onConfigurationComplete);
		ClientPlayConnectionEvents.JOIN.register(ModKrowd::onJoin);
		ClientPlayConnectionEvents.DISCONNECT.register(ModKrowd::onDisconnect);
		ClientTickEvents.END_CLIENT_TICK.register(ModKrowd::onEndClientTick);
		ClientLifecycleEvents.CLIENT_STOPPING.register(ModKrowd::onClientStopping);

		CONFIG.tryDeserialize(CONFIG_FILE);
		CONFIG.onInitEnable(Minecraft.getInstance());
	}

	public static ConfigScreen createConfigScreen(Screen parent) {
		return new ConfigScreen(parent, CONFIG, ModKrowd::saveConfig, ModKrowd::cancelConfig);
	}

	private static void saveConfig(Config config) {
		CONFIG.imitate(config);
		CONFIG.trySerialize(CONFIG_FILE);
	}

	private static void cancelConfig(Config config) {
		CONFIG.updateTab(config);
	}

	public static void startSwitchingMissileWarsLobby(int delay) {
		if (!(switchStatus instanceof SwitchConnecting)) {
			long switchTick = tick + delay;
			long forceTick = tick + 280; // 14 seconds
			switchStatus = new SwitchDelay(0, switchTick, forceTick);
			tickSwitchingMissileWarsLobby();
		}
	}

	private static void tickSwitchingMissileWarsLobby() {
		Minecraft minecraft = Minecraft.getInstance();

		// Wait until no ChatScreen is open or the force tick has been reached
		if (switchStatus instanceof SwitchDelay(int index, long switchTick, long forceTick) && (
				forceTick <= tick || switchTick <= tick && !(minecraft.screen instanceof ChatScreen)
		)) {
            ClientPacketListener listener = minecraft.getConnection();
            if (listener != null) {
                Subserver destination = currentSubserver.tryConnectNext(listener, index);
                if (destination != null) {
                    switchStatus = new SwitchConnecting(index, currentSubserver, destination);
                } else {
                    switchStatus = SwitchIdle.INSTANCE;
                }
            }
		}
	}

	private static void onConfigurationComplete(ClientConfigurationPacketListenerImpl listener, Minecraft minecraft) {
		ServerData info = ((ClientCommonPacketListenerImplAccessor) listener).getServerData();
		if (info != null && CubeKrowd.addressIsCubeKrowd(info.ip)) {
			currentSubserver = Subservers.PENDING;
		} else {
			currentSubserver = Subservers.NONE;
		}
		CONFIG.updateFeatures();

		CONFIG.onConfigurationComplete(listener, minecraft);
	}

	private static void onJoin(ClientPacketListener listener, PacketSender sender, Minecraft minecraft) {
		TAB_LIST.invalidateAll();

		switchStatus = SwitchIdle.INSTANCE;

		if (currentSubserver.isCubeKrowd) {
			Util.sendCommandPacket(listener, CubeKrowd.SUBSERVER_COMMAND);
		}
		CONFIG.onJoin(listener, minecraft);
	}

	private static void onDisconnect(ClientPacketListener listener, Minecraft minecraft) {
		currentSubserver = Subservers.NONE;
		CONFIG.onDisconnect(listener, minecraft);
		CONFIG.updateFeatures();
	}

	private static void onEndClientTick(Minecraft minecraft) {
		tick++;

		tickSwitchingMissileWarsLobby();
		tickKeys(minecraft);

		CONFIG.onEndTick(minecraft);
	}

	private static void tickKeys(Minecraft minecraft) {
		if (OPTIONS_KEY.consumeClick()) {
			minecraft.setScreen(createConfigScreen(minecraft.screen));
			((KeyMappingAccessor) OPTIONS_KEY).callRelease();
		}
		if (NEXT_SUBSERVER_KEY.consumeClick()) {
			startSwitchingMissileWarsLobby(0);
			((KeyMappingAccessor) NEXT_SUBSERVER_KEY).callRelease();
		}
	}

	private static void onClientStopping(Minecraft minecraft) {
		CONFIG.trySerialize(CONFIG_FILE);
	}

	public static void onMessage(MessageCache message) {
		Minecraft minecraft = Minecraft.getInstance();

        if (currentSubserver == Subservers.PENDING) {
            if (message.result() instanceof WhereamiMessage whereamiMessage) {
                currentSubserver = whereamiMessage.subserver();
                CONFIG.updateFeatures();
                CONFIG.onJoinUpdated(minecraft.getConnection(), minecraft);
                message.setBlocked(true);
                return;
            }
        }

        if (switchStatus instanceof SwitchConnecting(int index, Subserver sender, Subserver destination) && sender == currentSubserver) {
            Message result = message.result();
            if (result instanceof UnavailableMessage unavailableMessage && unavailableMessage.subserver() == destination
                    || result instanceof KickedMessage kickedMessage && kickedMessage.subserver() == destination) {

                int nextIndex = index + 1;
                Subserver nextDestination = currentSubserver.tryConnectNext(minecraft.getConnection(), nextIndex);
                if (nextDestination != null) {
                    switchStatus = new SwitchConnecting(nextIndex, sender, nextDestination);
                } else {
                    switchStatus = SwitchIdle.INSTANCE;
                }
            }
        }

		CONFIG.onMessage(message, minecraft);
	}

    private interface SwitchStatus {}
    private record SwitchIdle() implements SwitchStatus {
        private static final SwitchIdle INSTANCE = new SwitchIdle();
    }
    private record SwitchDelay(int index, long switchTick, long forceTick) implements SwitchStatus {}
    private record SwitchConnecting(int index, Subserver sender, Subserver destination) implements SwitchStatus {}
}