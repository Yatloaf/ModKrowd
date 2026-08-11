package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.Features;
import dev.yatloaf.modkrowd.config.feature.PingDisplayFeature;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabEntryCache;
import dev.yatloaf.modkrowd.mixinduck.PlayerTabOverlayDuck;
import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.core.ClientAsset;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin implements PlayerTabOverlayDuck {
	// PING_DISPLAY
	// DINNERBONE_GRUMM

	// Caution: May trigger severe headaches in functional programmers

    // At least the width of this string is reserved for the latency to avoid jittering
    @Unique private static final String MIN_RESERVED_LATENCY = Util.superscript(999, false);

	@Shadow @Final private Minecraft minecraft;
    @Shadow private @Nullable Component footer;
    @Shadow private @Nullable Component header;
    @Shadow protected abstract List<PlayerInfo> getPlayerInfos();
    @Shadow protected abstract void extractPingIcon(GuiGraphicsExtractor graphics, int slotWidth, int xo, int yo, PlayerInfo info);

    @Unique private int minReservedLatencyWidth;
	@Unique private TabEntryCache[] currentEntries;
	@Unique private int currentIndex;
	@Unique private TabEntryCache currentEntry;

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
    // Also reset custom counter (incremented at the start of the loop) and cache reserved ping width
    // ClientPacketListener#handlePlayerInfo* should be on the same thread as this, so no synchronization needed
    @Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getPlayerInfos()Ljava/util/List;"))
    private List<PlayerInfo> getPlayerInfosRedirect(PlayerTabOverlay instance) {
        PingDisplayFeature.State state = (PingDisplayFeature.State) ModKrowd.CONFIG.getState(Features.PING_DISPLAY);
        Component minReservedLatency = Component.literal(MIN_RESERVED_LATENCY).withStyle(state.style());
        this.minReservedLatencyWidth = this.minecraft.font.width(minReservedLatency);
        this.currentIndex = -1;
        this.currentEntries = ModKrowd.TAB_LIST.entries;
        return ModKrowd.TAB_LIST.playerInfos;
    }

	// Increment custom counter, capture current entry, modify name for theme
	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getNameForDisplay(Lnet/minecraft/client/multiplayer/PlayerInfo;)Lnet/minecraft/network/chat/Component;"))
	private Component getNameForDisplayRedirect(PlayerTabOverlay instance, PlayerInfo info) {
		this.currentIndex += 1;
		this.currentEntry = this.currentEntries[this.currentIndex];
        return this.currentEntry.getNameThemed().text();
    }

	// Adjust width
	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/lang/Math;max(II)I"))
	private int maxRedirect(int a, int b) {
		if (Features.PING_DISPLAY.active) {
			// Hardcoded width of the Vanilla ping bars: 10
			// Hardcoded space between name and ping bars: 2
			int nameWidth = this.minecraft.font.width(this.currentEntry.getNameThemed().text());
            int latencyWidth = this.minecraft.font.width(this.currentEntry.getLatencyThemed().text());
			if (this.currentEntry.result().isPlayer()) {
				return Math.max(a, nameWidth + Math.max(this.minReservedLatencyWidth, latencyWidth) - 10);
			} else {
				return Math.max(a, nameWidth - 12);
			}
		}
		return Math.max(a, b);
	}

	// Theme header
	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"))
	private List<FormattedCharSequence> splitRedirect0(Font instance, FormattedText input, int maxWidth) {
        return ModKrowd.currentSubserver.isCubeKrowd
				? instance.split(ModKrowd.TAB_DECO.getHeaderThemed().text(), maxWidth)
				: instance.split(input, maxWidth);
    }

	// Theme footer
	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"))
	private List<FormattedCharSequence> splitRedirect1(Font instance, FormattedText input, int maxWidth) {
        return ModKrowd.currentSubserver.isCubeKrowd
				? instance.split(ModKrowd.TAB_DECO.getFooterThemed().text(), maxWidth)
				: instance.split(input, maxWidth);
    }

	// ----------------------------
	// ---------- LOOP 2 ----------
	// ----------------------------

	// Capture current entry and index, very convenient
	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
	private <E> E getRedirect(List<E> instance, int i) {
		E element = instance.get(i);
		if (element instanceof PlayerInfo) {
			this.currentIndex = i;
			this.currentEntry = this.currentEntries[this.currentIndex];
		}
		return element;
	}

	// Assume the face is upside-down
	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;isPlayerUpsideDown(Lnet/minecraft/world/entity/player/Player;)Z"))
	private boolean isPlayerUpsideDownArg(Player player) {
		return Features.DINNERBONE_GRUMM.active && this.currentEntry.result().isPlayer() || AvatarRenderer.isPlayerUpsideDown(player);
	}

    @Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/ClientAsset$Texture;texturePath()Lnet/minecraft/resources/Identifier;"))
    private Identifier texturePathRedirect(ClientAsset.Texture instance) {
        return this.currentEntry.getSkinThemed();
    }

	// Draw ping instead
	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;extractPingIcon(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V"))
	private void renderPingIconRedirect(PlayerTabOverlay instance, GuiGraphicsExtractor graphics, int slotWidth, int xo, int yo, PlayerInfo info) {
		if (Features.PING_DISPLAY.active) {
			if (this.currentEntry.result().isPlayer()) {
				MutableComponent text = this.currentEntry.getLatencyThemed().text();
				// color gets overridden by the text style anyway, but we need the opacity
				graphics.text(this.minecraft.font, text, xo + slotWidth - this.minecraft.font.width(text), yo, CommonColors.WHITE);
			}
		} else {
			this.extractPingIcon(graphics, slotWidth, xo, yo, info);
		}
	}

	// ----------------------------
	// ---------- OTHERS ----------
	// ----------------------------

	@Redirect(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getBackgroundColor(I)I"))
	private int getBackgroundColorRedirect(Options instance, int defaultColor) {
		return ModKrowd.TAB_LIST.entryColorOr(defaultColor);
	}

	// DRY fans hate this trick

	@ModifyArg(method = "extractRenderState", index = 4, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"))
	private int fillArg0(int color) {
		return ModKrowd.TAB_LIST.hudColorOr(color);
	}

	@ModifyArg(method = "extractRenderState", index = 4, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"))
	private int fillArg1(int color) {
		return ModKrowd.TAB_LIST.hudColorOr(color);
	}

	@ModifyArg(method = "extractRenderState", index = 4, at = @At(value = "INVOKE", ordinal = 3, target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"))
	private int fillArg3(int color) {
		return ModKrowd.TAB_LIST.hudColorOr(color);
	}
}