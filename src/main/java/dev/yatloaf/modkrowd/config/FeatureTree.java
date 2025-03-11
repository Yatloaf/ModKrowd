package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.config.api.FeatureExtender;
import dev.yatloaf.modkrowd.config.feature.AutoswitchFeature;
import dev.yatloaf.modkrowd.config.feature.BlockCutoutFeature;
import dev.yatloaf.modkrowd.config.feature.CherryLiteThemeFeature;
import dev.yatloaf.modkrowd.config.feature.CherryThemeFeature;
import dev.yatloaf.modkrowd.config.feature.ClickResponseFeature;
import dev.yatloaf.modkrowd.config.feature.Feature;
import dev.yatloaf.modkrowd.config.feature.HighContrastThemeFeature;
import dev.yatloaf.modkrowd.config.feature.MessagePreviewFeature;
import dev.yatloaf.modkrowd.config.feature.ReloadResourcesFeature;
import dev.yatloaf.modkrowd.config.feature.SeparateChatHistoryFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureTree {
    protected static final int FEATURE_ESTIMATION = 40;
    protected static final int TAB_ESTIMATION = 6;

    public final List<FeatureTab> tabs = new ArrayList<>(TAB_ESTIMATION);
    public final Map<String, FeatureTab> idToTab = new HashMap<>(TAB_ESTIMATION);

    public final List<Feature> features = new ArrayList<>(FEATURE_ESTIMATION);
    public final Map<String, Feature> idToFeature = new HashMap<>(FEATURE_ESTIMATION);

    public final FeatureTab APPEARANCE = this.tab("appearance");
    public final FeatureTab CHAT = this.tab("chat");
    public final FeatureTab CREATIVE = this.tab("creative");
    public final FeatureTab MISSILEWARS = this.tab("missilewars");
    public final FeatureTab THEME = this.tab("theme");

    // ----------------
    //  APPEARANCE
    // ----------------

    public final Feature DEVOID = this.APPEARANCE.feature(new Feature(
            "devoid", PredicateIndex.TERNARY_CK
    ));
    public final Feature PING_DISPLAY = this.APPEARANCE.feature(new Feature(
            "ping_display", PredicateIndex.TERNARY_CK
    ));
    public final ReloadResourcesFeature SLIM_ARMOR = this.APPEARANCE.feature(new ReloadResourcesFeature(
            "slim_armor", PredicateIndex.TERNARY_CK
    ));
    public final Feature DINNERBONE_GRUMM = this.APPEARANCE.feature(new Feature(
            "dinnerbone_grumm", PredicateIndex.TERNARY_CK
    ));
    public final Feature DEADMAU5 = this.APPEARANCE.feature(new Feature(
            "deadmau5", PredicateIndex.TERNARY_CK
    ));
    public final Feature SHOW_OWN_LABEL = this.APPEARANCE.feature(new Feature(
            "show_own_label", PredicateIndex.TERNARY_CK
    ));
    public final Feature ALWAYS_SHOW_LABELS = this.APPEARANCE.feature(new Feature(
            "show_every_label", PredicateIndex.TERNARY_CK
    ));
    public final Feature ALWAYS_HIDE_LABELS = this.APPEARANCE.feature(new Feature(
            "hide_every_label", PredicateIndex.TERNARY_CK
    ));
    public final Feature HIDE_PLAYERS = this.APPEARANCE.feature(new Feature(
            "hide_players", PredicateIndex.TERNARY_CK
    ));

    // ----------------
    //  CHAT
    // ----------------

    public final Feature DEJOIN = this.CHAT.feature(new Feature(
            "no_chat_clear_on_rejoin", PredicateIndex.BINARY
    ));
    public final SeparateChatHistoryFeature SEPARATE_CHAT_HISTORY = this.CHAT.feature(new SeparateChatHistoryFeature(
            "separate_chat_history", PredicateIndex.BINARY
    ));
    public final ClickResponseFeature CLICK_RESPOND = this.CHAT.feature(new ClickResponseFeature(
            "click_to_respond", PredicateIndex.BINARY_CK
    ));
    public final MessagePreviewFeature MESSAGE_PREVIEW = this.CHAT.feature(new MessagePreviewFeature(
            "message_preview", PredicateIndex.BINARY_CK
    ));

    // ----------------
    //  CREATIVE
    // ----------------

    public final Feature UNINVISIBILITY = this.CREATIVE.feature(new Feature(
            "anti_invisibility", PredicateIndex.BINARY_CR
    ));
    public final BlockCutoutFeature TANGIBLE_BARRIERS = this.CREATIVE.feature(new BlockCutoutFeature(
            "tangible_barrier", PredicateIndex.BINARY_CR,
            Blocks.BARRIER
    ));
    public final BlockCutoutFeature TANGIBLE_STRUCTURE_VOIDS = this.CREATIVE.feature(new BlockCutoutFeature(
            "tangible_structure_void", PredicateIndex.BINARY_CR,
            Blocks.STRUCTURE_VOID
    ));
    public final BlockCutoutFeature TANGIBLE_LIGHTS = this.CREATIVE.feature(new BlockCutoutFeature(
            "tangible_light", PredicateIndex.BINARY_CR,
            Blocks.LIGHT
    ));
    public final BlockCutoutFeature TANGIBLE_MOVING_PISTONS = this.CREATIVE.feature(new BlockCutoutFeature(
            "tangible_moving_piston", PredicateIndex.BINARY_CR,
            Blocks.MOVING_PISTON
    ));
    public final BlockCutoutFeature TANGIBLE_END_PORTALS = this.CREATIVE.feature(new BlockCutoutFeature(
            "tangible_end_portal_and_gateway", PredicateIndex.BINARY_CR,
            Blocks.END_PORTAL, Blocks.END_GATEWAY
    ));


    // ----------------
    //  MISSILEWARS
    // ----------------

    public final AutoswitchFeature AUTOSWITCH = this.MISSILEWARS.feature(new AutoswitchFeature(
            "auto_missilewars_lobby_switch", PredicateIndex.BINARY
    ));
    public final Feature RESPECTATE = this.MISSILEWARS.feature(new Feature(
            "respectate", PredicateIndex.TERNARY_MW
    ));

    // ----------------
    //  THEME
    // ----------------

    public final HighContrastThemeFeature HIGH_CONTRAST = this.THEME.feature(new HighContrastThemeFeature(
            "theme_high_contrast", PredicateIndex.TERNARY_CK
    ));
    public final CherryLiteThemeFeature CHERRY_LITE = this.THEME.feature(new CherryLiteThemeFeature(
            "theme_cherry_lite", PredicateIndex.TERNARY_CK
    ));
    public final CherryThemeFeature CHERRY = this.THEME.feature(new CherryThemeFeature(
            "theme_cherry", PredicateIndex.TERNARY_CK
    ));

    private boolean initExtenders = false;

    public FeatureTab tab(String id) {
        FeatureTab tab = new FeatureTab(this, this.tabs.size(), id);
        this.tabs.add(tab);
        this.idToTab.put(id, tab);
        return tab;
    }

    public void mergeState(FeatureTree source) {
        this.initExtenders();
        for (int i = 0; i < this.features.size(); i++) {
            this.features.get(i).mergeState(source.features.get(i));
        }
    }

    protected synchronized void initExtenders() {
        // If this is done in the constructor, there may be issues with circular references
        if (!this.initExtenders) {
            FabricLoader.getInstance().invokeEntrypoints(
                    "modkrowd:feature_extender",
                    FeatureExtender.class,
                    extender -> extender.extend(this)
            );
            this.initExtenders = true;
        }
    }
}
