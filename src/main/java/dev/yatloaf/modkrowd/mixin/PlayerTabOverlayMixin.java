package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.UnknownTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.mixinduck.PlayerTabOverlayDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

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

	@Shadow protected abstract List<PlayerInfo> getPlayerInfos();
	@Shadow protected abstract void renderPingIcon(GuiGraphics context, int width, int x, int y, PlayerInfo entry);
	@Shadow public abstract Component getNameForDisplay(PlayerInfo entry);

	@Unique @Override
	public @Nullable MutableComponent modKrowd$getHeader() {
		return (MutableComponent) this.header;
	}

	@Unique @Override
	public @Nullable MutableComponent modKrowd$getFooter() {
		return (MutableComponent) this.footer;
	}

	@Unique @Override
	public @NotNull List<PlayerInfo> modKrowd$collectEntries() {
		return this.getPlayerInfos();
	}

	// ----------------------------
	// ---------- LOOP 1 ----------
	// ----------------------------

	// Reset custom counter at the start (incremented at the start of the loop)
	@Inject(method = "render", at = @At("HEAD"))
	private void renderInjectHead(GuiGraphics context, int scaledWindowWidth, Scoreboard scoreboard, Objective objective, CallbackInfo ci) {
		ModKrowd.checkTabListCache();
		this.currentIndex = -1;
		this.currentEntries = ModKrowd.currentTabListCache.result().entries();
	}

	// Increment custom counter, capture current entry, modify name for theme
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;getNameForDisplay(Lnet/minecraft/client/multiplayer/PlayerInfo;)Lnet/minecraft/network/chat/Component;"))
	private Component getNameForDisplayRedirect(PlayerTabOverlay instance, PlayerInfo entry) {
		this.currentIndex += 1;
		this.currentEntry = this.getCurrentEntry(entry);
        return this.currentEntry.themedOrDefault().text();
    }

	// Adjust width
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/lang/Math;max(II)I"))
	private int maxRedirect(int a, int b) {
		if (ModKrowd.CONFIG.PING_DISPLAY.enabled) {
			// Hardcoded width of the Vanilla ping bars: 10
			// Hardcoded space between name and ping bars: 2
			int nameWidth = this.minecraft.font.width(this.currentEntry.themedOrDefault().text());
			if (this.currentEntry.isPlayer()) {
				return Math.max(a, nameWidth + this.minecraft.font.width(this.currentEntry.latencyThemedOrDefault().text()) - 10);
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
				? instance.split(ModKrowd.currentTabListCache.tabHeaderCache().themedOrDefault().text(), width)
				: instance.split(text, width);
    }

	// Theme footer
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"))
	private List<FormattedCharSequence> splitRedirect1(Font instance, FormattedText text, int width) {
        return ModKrowd.currentSubserver.isCubeKrowd
				? instance.split(ModKrowd.currentTabListCache.tabFooterCache().themedOrDefault().text(), width)
				: instance.split(text, width);
    }

	// ----------------------------
	// ---------- LOOP 2 ----------
	// ----------------------------

	// Capture current entry and index, very convenient
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
	private <E> E getRedirect(List<E> instance, int i) {
		E element = instance.get(i);
		if (element instanceof PlayerInfo entry) {
			this.currentIndex = i;
			this.currentEntry = this.getCurrentEntry(entry);
		}
		return element;
	}

	// Assume the face is upside-down
	@ModifyArg(method = "render", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerFaceRenderer;draw(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIIZZI)V"))
	private boolean upsideDownArg(boolean upsideDown) {
		return upsideDown || ModKrowd.CONFIG.DINNERBONE_GRUMM.enabled && this.currentEntry.isPlayer();
	}

	// Draw ping instead
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;renderPingIcon(Lnet/minecraft/client/gui/GuiGraphics;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V"))
	private void renderPingIconRedirect(PlayerTabOverlay instance, GuiGraphics context, int width, int x, int y, PlayerInfo entry) {
		if (ModKrowd.CONFIG.PING_DISPLAY.enabled) {
			if (this.currentEntry.isPlayer()) {
				MutableComponent text = this.currentEntry.latencyThemedOrDefault().text();
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
		return ModKrowd.currentTabListCache.entryColorOr(fallbackColor);
	}

	// DRY fans hate this trick

	@ModifyArg(method = "render", index = 4, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
	private int fillArg0(int color) {
		return ModKrowd.currentTabListCache.hudColorOr(color);
	}

	@ModifyArg(method = "render", index = 4, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
	private int fillArg1(int color) {
		return ModKrowd.currentTabListCache.hudColorOr(color);
	}

	@ModifyArg(method = "render", index = 4, at = @At(value = "INVOKE", ordinal = 3, target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"))
	private int fillArg3(int color) {
		return ModKrowd.currentTabListCache.hudColorOr(color);
	}

	@Unique
	private TabEntryCache getCurrentEntry(PlayerInfo fallback) {
		if (this.currentIndex < this.currentEntries.length) {
			return this.currentEntries[this.currentIndex];
		} else {
			ModKrowd.LOGGER.error("[PlayerTabOverlayMixin] Tab list index out of bounds!"); // Incredibly unlucky timing issue?
			return new UnknownTabList.EntryCache(
                    TextCache.of((MutableComponent) this.getNameForDisplay(fallback)),
					fallback.getLatency()
			);
		}
	}
}