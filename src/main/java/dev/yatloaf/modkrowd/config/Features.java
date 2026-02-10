package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.config.api.FeatureExtender;
import dev.yatloaf.modkrowd.config.feature.AutoswitchFeature;
import dev.yatloaf.modkrowd.config.feature.BlankTabIconsThemeFeature;
import dev.yatloaf.modkrowd.config.feature.BlockCutoutFeature;
import dev.yatloaf.modkrowd.config.feature.CherryLiteThemeFeature;
import dev.yatloaf.modkrowd.config.feature.CherryThemeFeature;
import dev.yatloaf.modkrowd.config.feature.DirectMessageSoundFeature;
import dev.yatloaf.modkrowd.config.feature.Feature;
import dev.yatloaf.modkrowd.config.feature.HighContrastThemeFeature;
import dev.yatloaf.modkrowd.config.feature.MessagePreviewFeature;
import dev.yatloaf.modkrowd.config.feature.PingDisplayFeature;
import dev.yatloaf.modkrowd.config.feature.ReloadResourcesFeature;
import dev.yatloaf.modkrowd.config.feature.SeparateChatHistoryFeature;
import dev.yatloaf.modkrowd.config.feature.ShadedTabThemeFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Features {
    public static final List<FeatureTab> TABS = new ArrayList<>();
    public static final Map<String, FeatureTab> ID_TO_TAB = new HashMap<>();

    public static final List<Feature> FEATURES = new ArrayList<>();
    public static final Map<String, Feature> ID_TO_FEATURE = new HashMap<>();

    public static final FeatureTab APPEARANCE = tab("appearance");
    public static final FeatureTab CHAT = tab("chat");
    public static final FeatureTab CREATIVE = tab("creative");
    public static final FeatureTab MISSILEWARS = tab("missilewars");
    public static final FeatureTab THEME = tab("theme");

    // ----------------
    //  APPEARANCE
    // ----------------

    public static final Feature DEVOID = APPEARANCE.feature(new Feature(
            "devoid", Restriction.ALWAYS
    ));
    public static final PingDisplayFeature PING_DISPLAY = APPEARANCE.feature(new PingDisplayFeature(
            "ping_display", Restriction.ALWAYS
    ));
    public static final ReloadResourcesFeature SLIM_ARMOR = APPEARANCE.feature(new ReloadResourcesFeature(
            "slim_armor", Restriction.ALWAYS
    ));
    public static final Feature DINNERBONE_GRUMM = APPEARANCE.feature(new Feature(
            "dinnerbone_grumm", Restriction.ALWAYS
    ));
    public static final Feature DEADMAU5 = APPEARANCE.feature(new Feature(
            "deadmau5", Restriction.ALWAYS
    ));
    public static final Feature SHOW_OWN_LABEL = APPEARANCE.feature(new Feature(
            "show_own_label", Restriction.ALWAYS
    ));
    public static final Feature ALWAYS_SHOW_LABELS = APPEARANCE.feature(new Feature(
            "show_every_label", Restriction.ALWAYS
    ));
    public static final Feature ALWAYS_HIDE_LABELS = APPEARANCE.feature(new Feature(
            "hide_every_label", Restriction.ALWAYS
    ));
    public static final Feature HIDE_PLAYERS = APPEARANCE.feature(new Feature(
            "hide_players", Restriction.ALWAYS
    ));
    public static final Feature HIDE_SELF = APPEARANCE.feature(new Feature(
            "hide_self", Restriction.ALWAYS
    ));

    // ----------------
    //  CHAT
    // ----------------

    public static final Feature DEJOIN = CHAT.feature(new Feature(
            "no_chat_clear_on_rejoin", Restriction.ALWAYS
    ));
    public static final SeparateChatHistoryFeature SEPARATE_CHAT_HISTORY = CHAT.feature(new SeparateChatHistoryFeature(
            "separate_chat_history", Restriction.ALWAYS
    ));
    public static final DirectMessageSoundFeature DIRECT_MESSAGE_SOUND = CHAT.feature(new DirectMessageSoundFeature(
            "direct_message_sound", Restriction.ALWAYS
    ));
    public static final MessagePreviewFeature MESSAGE_PREVIEW = CHAT.feature(new MessagePreviewFeature(
            "message_preview", Restriction.ALWAYS
    ));

    // ----------------
    //  CREATIVE
    // ----------------

    public static final Feature UNINVISIBILITY = CREATIVE.feature(new Feature(
            "anti_invisibility", Restriction.CREATIVE
    ));
    public static final BlockCutoutFeature TANGIBLE_BARRIERS = CREATIVE.feature(new BlockCutoutFeature(
            "tangible_barrier", Restriction.CREATIVE,
            Blocks.BARRIER
    ));
    public static final BlockCutoutFeature TANGIBLE_STRUCTURE_VOIDS = CREATIVE.feature(new BlockCutoutFeature(
            "tangible_structure_void", Restriction.CREATIVE,
            Blocks.STRUCTURE_VOID
    ));
    public static final BlockCutoutFeature TANGIBLE_LIGHTS = CREATIVE.feature(new BlockCutoutFeature(
            "tangible_light", Restriction.CREATIVE,
            Blocks.LIGHT
    ));
    public static final BlockCutoutFeature TANGIBLE_MOVING_PISTONS = CREATIVE.feature(new BlockCutoutFeature(
            "tangible_moving_piston", Restriction.CREATIVE,
            Blocks.MOVING_PISTON
    ));
    public static final BlockCutoutFeature TANGIBLE_END_PORTALS = CREATIVE.feature(new BlockCutoutFeature(
            "tangible_end_portal_and_gateway", Restriction.CREATIVE,
            Blocks.END_PORTAL, Blocks.END_GATEWAY
    ));


    // ----------------
    //  MISSILEWARS
    // ----------------

    public static final AutoswitchFeature AUTOSWITCH = MISSILEWARS.feature(new AutoswitchFeature(
            "auto_missilewars_lobby_switch", Restriction.ALWAYS
    ));
    public static final Feature TIE_DETECTOR = MISSILEWARS.feature(new Feature(
            "tie_detector", Restriction.ALWAYS
    ));
    public static final Feature RESPECTATE = MISSILEWARS.feature(new Feature(
            "respectate", Restriction.ALWAYS
    ));

    // ----------------
    //  THEME
    // ----------------

    public static final ShadedTabThemeFeature SHADED_TAB = THEME.feature(new ShadedTabThemeFeature(
            "theme_shaded_tab_list_background", Restriction.ALWAYS
    ));
    public static final BlankTabIconsThemeFeature BLANK_TAB_ICONS = THEME.feature(new BlankTabIconsThemeFeature(
            "theme_blank_tab_list_icon", Restriction.ALWAYS
    ));
    public static final HighContrastThemeFeature HIGH_CONTRAST = THEME.feature(new HighContrastThemeFeature(
            "theme_high_contrast", Restriction.ALWAYS
    ));
    public static final CherryLiteThemeFeature CHERRY_LITE = THEME.feature(new CherryLiteThemeFeature(
            "theme_cherry_lite", Restriction.ALWAYS
    ));
    public static final CherryThemeFeature CHERRY = THEME.feature(new CherryThemeFeature(
            "theme_cherry", Restriction.ALWAYS
    ));

    static {
        FabricLoader.getInstance().invokeEntrypoints(
                "modkrowd:feature_extender",
                FeatureExtender.class,
                FeatureExtender::extend
        );
    }

    public static FeatureTab tab(String id) {
        FeatureTab tab = new FeatureTab(TABS.size(), id);
        TABS.add(tab);
        ID_TO_TAB.put(id, tab);
        return tab;
    }
}
