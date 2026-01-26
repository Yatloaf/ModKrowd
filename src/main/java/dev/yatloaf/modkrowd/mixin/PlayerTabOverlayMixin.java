package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.mixinduck.PlayerTabOverlayDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin implements PlayerTabOverlayDuck {
	// PING_DISPLAY
	// DINNERBONE_GRUMM

	// Caution: May trigger severe headaches in functional programmers

	@Shadow @Final private Minecraft minecraft;
	@Shadow private @Nullable Component header;
	@Shadow private @Nullable Component footer;

	@Unique private TabEntryCache[] currentEntries;
	@Unique private int currentIndex;
	@Unique private TabEntryCache currentEntry;

    @Unique private static final String OAK_PLANKS_URL = "http://textures.minecraft.net/texture/24acc0078201335366085a3a0f3c8d4fcf117ca82f1e21ceb942ca295411a5ab";
    @Unique private static final String OAK_RIGHT_ARROW_URL = "http://textures.minecraft.net/texture/bc2a245bf8882621b9d210d2db9cb61b33782449c794d832ae27beff40b3408e";
    @Unique private static final String OAK_EXCLAMATION_URL = "http://textures.minecraft.net/texture/6d25daa7b26ab39764920cf9117333f3984536d17e484ea4240aba6824133fad";
    @Unique private static final Map<String, ResourceLocation> SKIN_MAP = new HashMap<>();
    static {
        SKIN_MAP.put(OAK_PLANKS_URL, ResourceLocation.fromNamespaceAndPath("modkrowd", "textures/theme/cherry/cherry_planks.png"));
        SKIN_MAP.put(OAK_RIGHT_ARROW_URL, ResourceLocation.fromNamespaceAndPath("modkrowd", "textures/theme/cherry/cherry_right_arrow.png"));
        SKIN_MAP.put(OAK_EXCLAMATION_URL, ResourceLocation.fromNamespaceAndPath("modkrowd", "textures/theme/cherry/cherry_exclamation.png"));
    }

	@Shadow protected abstract List<PlayerInfo> getPlayerInfos();
	@Shadow protected abstract void renderPingIcon(GuiGraphics context, int width, int x, int y, PlayerInfo entry);

	@Unique @Override
	public @Nullable MutableComponent modKrowd$getHeader() {
		return (MutableComponent) this.header;
	}

	@Unique @Override
	public @Nullable MutableComponent modKrowd$getFooter() {
		return (MutableComponent) this.footer;
	}

	@Unique @Override
	public @NotNull List<PlayerInfo> modKrowd$getPlayerInfos() {
        if (this.minecraft.player == null) {
            return List.of();
        } else {
            return this.getPlayerInfos();
        }
    }

	// ----------------------------
	// ---------- LOOP 1 ----------
	// ----------------------------

    // Send this through TabListCache to avoid re-sorting every frame
    // Also reset custom counter (incremented at the start of the loop)
    // ClientPacketListener#handlePlayerInfo* should be on the same thread as this, so no synchronization needed
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getPlayerInfos()Ljava/util/List;"))
    private List<PlayerInfo> getPlayerInfosRedirect(PlayerTabOverlay instance) {
        this.currentIndex = -1;
        this.currentEntries = ModKrowd.TAB_LIST.entries;
        return ModKrowd.TAB_LIST.playerInfos;
    }

	// Increment custom counter, capture current entry, modify name for theme
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getNameForDisplay(Lnet/minecraft/client/multiplayer/PlayerInfo;)Lnet/minecraft/network/chat/Component;"))
	private Component getNameForDisplayRedirect(PlayerTabOverlay instance, PlayerInfo entry) {
		this.currentIndex += 1;
		this.currentEntry = this.currentEntries[this.currentIndex];
        return this.currentEntry.getNameThemed().text();
    }

	// Adjust width
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/lang/Math;max(II)I"))
	private int maxRedirect(int a, int b) {
		if (ModKrowd.CONFIG.PING_DISPLAY.enabled) {
			// Hardcoded width of the Vanilla ping bars: 10
			// Hardcoded space between name and ping bars: 2
			int nameWidth = this.minecraft.font.width(this.currentEntry.getNameThemed().text());
			if (this.currentEntry.result().isPlayer()) {
				return Math.max(a, nameWidth + this.minecraft.font.width(this.currentEntry.getLatencyThemed().text()) - 10);
			} else {
				return Math.max(a, nameWidth - 12);
			}
		}
		return Math.max(a, b);
	}

	// Theme header
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"))
	private List<FormattedCharSequence> splitRedirect0(Font instance, FormattedText text, int width) {
        return ModKrowd.currentSubserver.isCubeKrowd
				? instance.split(ModKrowd.TAB_DECO.getHeaderThemed().text(), width)
				: instance.split(text, width);
    }

	// Theme footer
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"))
	private List<FormattedCharSequence> splitRedirect1(Font instance, FormattedText text, int width) {
        return ModKrowd.currentSubserver.isCubeKrowd
				? instance.split(ModKrowd.TAB_DECO.getFooterThemed().text(), width)
				: instance.split(text, width);
    }

	// ----------------------------
	// ---------- LOOP 2 ----------
	// ----------------------------

	// Capture current entry and index, very convenient
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
	private <E> E getRedirect(List<E> instance, int i) {
		E element = instance.get(i);
		if (element instanceof PlayerInfo) {
			this.currentIndex = i;
			this.currentEntry = this.currentEntries[this.currentIndex];
		}
		return element;
	}

	// Assume the face is upside-down
	@ModifyArg(method = "render", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerFaceRenderer;draw(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIIZZI)V"))
	private boolean upsideDownArg(boolean upsideDown) {
		return upsideDown || ModKrowd.CONFIG.DINNERBONE_GRUMM.enabled && this.currentEntry.result().isPlayer();
	}

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/ClientAsset$Texture;texturePath()Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation texturePathRedirect(ClientAsset.Texture instance) {
        if (ModKrowd.CONFIG.CHERRY_LITE.enabled || ModKrowd.CONFIG.CHERRY.enabled) {
            if (instance instanceof ClientAsset.DownloadedTexture(ResourceLocation texturePath, String url) && SKIN_MAP.get(url) != null) {
                return SKIN_MAP.get(url);
            }
        }
        return instance.texturePath();
    }

	// Draw ping instead
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;renderPingIcon(Lnet/minecraft/client/gui/GuiGraphics;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V"))
	private void renderPingIconRedirect(PlayerTabOverlay instance, GuiGraphics context, int width, int x, int y, PlayerInfo entry) {
		if (ModKrowd.CONFIG.PING_DISPLAY.enabled) {
			if (this.currentEntry.result().isPlayer()) {
				MutableComponent text = this.currentEntry.getLatencyThemed().text();
				// color gets overridden by the text style anyway, but we need the opacity
				context.drawString(this.minecraft.font, text, x + width - this.minecraft.font.width(text), y, CommonColors.WHITE);
			}
		} else {
			this.renderPingIcon(context, width, x, y, entry);
		}
	}

	// ----------------------------
	// ---------- OTHERS ----------
	// ----------------------------

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getBackgroundColor(I)I"))
	private int getBackgroundColorRedirect(Options instance, int fallbackColor) {
		return ModKrowd.TAB_LIST.entryColorOr(fallbackColor);
	}

	// DRY fans hate this trick

	@ModifyArg(method = "render", index = 4, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
	private int fillArg0(int color) {
		return ModKrowd.TAB_LIST.hudColorOr(color);
	}

	@ModifyArg(method = "render", index = 4, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
	private int fillArg1(int color) {
		return ModKrowd.TAB_LIST.hudColorOr(color);
	}

	@ModifyArg(method = "render", index = 4, at = @At(value = "INVOKE", ordinal = 3, target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
	private int fillArg3(int color) {
		return ModKrowd.TAB_LIST.hudColorOr(color);
	}
}