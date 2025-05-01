package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.CubeKrowdSubserver;
import dev.yatloaf.modkrowd.cubekrowd.tablist.UnknownTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.mixinduck.PlayerListHudDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.GameOptions;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
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

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin implements PlayerListHudDuck {
	// PING_DISPLAY
	// DINNERBONE_GRUMM

	// Caution: May trigger severe headaches in functional programmers

	@Shadow @Final private MinecraftClient client;
	@Shadow private @Nullable Text header;
	@Shadow private @Nullable Text footer;

	@Unique private TabEntryCache[] currentEntries;
	@Unique private int currentIndex;
	@Unique private TabEntryCache currentEntry;

	@Shadow protected abstract List<PlayerListEntry> collectPlayerEntries();
	@Shadow protected abstract void renderLatencyIcon(DrawContext context, int width, int x, int y, PlayerListEntry entry);
	@Shadow public abstract Text getPlayerName(PlayerListEntry entry);

	@Unique @Override
	public @Nullable MutableText modKrowd$getHeader() {
		return (MutableText) this.header;
	}

	@Unique @Override
	public @Nullable MutableText modKrowd$getFooter() {
		return (MutableText) this.footer;
	}

	@Unique @Override
	public @NotNull List<PlayerListEntry> modKrowd$collectEntries() {
		return this.collectPlayerEntries();
	}

	// ----------------------------
	// ---------- LOOP 1 ----------
	// ----------------------------

	// Reset custom counter at the start (incremented at the start of the loop)
	@Inject(method = "render", at = @At("HEAD"))
	private void renderInjectHead(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
		ModKrowd.checkTabListCache();
		this.currentIndex = -1;
		this.currentEntries = ModKrowd.currentTabListCache.result().entries();
	}

	// Increment custom counter, capture current entry, modify name for theme
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/hud/PlayerListHud;getPlayerName(Lnet/minecraft/client/network/PlayerListEntry;)Lnet/minecraft/text/Text;"))
	private Text getPlayerNameRedirect(PlayerListHud instance, PlayerListEntry entry) {
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
			int nameWidth = this.client.textRenderer.getWidth(this.currentEntry.themedOrDefault().text());
			if (this.currentEntry.isPlayer()) {
				return Math.max(a, nameWidth + this.client.textRenderer.getWidth(this.currentEntry.latencyThemedOrDefault().text()) - 10);
			} else {
				return Math.max(a, nameWidth - 12);
			}
		}
		return Math.max(a, b);
	}

	// Theme header
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/font/TextRenderer;wrapLines(Lnet/minecraft/text/StringVisitable;I)Ljava/util/List;"))
	private List<OrderedText> wrapLinesRedirect0(TextRenderer instance, StringVisitable text, int width) {
        return ModKrowd.currentSubserver instanceof CubeKrowdSubserver
				? instance.wrapLines(ModKrowd.currentTabListCache.tabHeaderCache().themedOrDefault().text(), width)
				: instance.wrapLines(text, width);
    }

	// Theme footer
	@Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/font/TextRenderer;wrapLines(Lnet/minecraft/text/StringVisitable;I)Ljava/util/List;"))
	private List<OrderedText> wrapLinesRedirect1(TextRenderer instance, StringVisitable text, int width) {
        return ModKrowd.currentSubserver instanceof CubeKrowdSubserver
				? instance.wrapLines(ModKrowd.currentTabListCache.tabFooterCache().themedOrDefault().text(), width)
				: instance.wrapLines(text, width);
    }

	// ----------------------------
	// ---------- LOOP 2 ----------
	// ----------------------------

	// Capture current entry and index, very convenient
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
	private <E> E getRedirect(List<E> instance, int i) {
		E element = instance.get(i);
		if (element instanceof PlayerListEntry entry) {
			this.currentIndex = i;
			this.currentEntry = this.getCurrentEntry(entry);
		}
		return element;
	}

	// Assume the face is upside-down
	@ModifyArg(method = "render", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/PlayerSkinDrawer;draw(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;IIIZZI)V"))
	private boolean upsideDownArg(boolean upsideDown) {
		return upsideDown || ModKrowd.CONFIG.DINNERBONE_GRUMM.enabled && this.currentEntry.isPlayer();
	}

	// Draw ping instead
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;renderLatencyIcon(Lnet/minecraft/client/gui/DrawContext;IIILnet/minecraft/client/network/PlayerListEntry;)V"))
	private void renderLatencyIconRedirect(PlayerListHud instance, DrawContext context, int width, int x, int y, PlayerListEntry entry) {
		if (ModKrowd.CONFIG.PING_DISPLAY.enabled) {
			if (this.currentEntry.isPlayer()) {
				MutableText text = this.currentEntry.latencyThemedOrDefault().text();
				context.getMatrices().push();
				context.getMatrices().translate(0.0f, 0.0f, 100.0f);
				context.drawTextWithShadow(this.client.textRenderer, text, x + width - this.client.textRenderer.getWidth(text), y, 0);
				context.getMatrices().pop();
			}
		} else {
			this.renderLatencyIcon(context, width, x, y, entry);
		}
	}

	// ----------------------------
	// ---------- OTHERS ----------
	// ----------------------------

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getTextBackgroundColor(I)I"))
	private int getTextBackgroundColorRedirect(GameOptions instance, int fallbackColor) {
		return ModKrowd.currentTabListCache.entryColorOr(fallbackColor);
	}

	// DRY fans hate this trick

	@ModifyArg(method = "render", index = 4, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
	private int fillArg0(int color) {
		return ModKrowd.currentTabListCache.hudColorOr(color);
	}

	@ModifyArg(method = "render", index = 4, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
	private int fillArg1(int color) {
		return ModKrowd.currentTabListCache.hudColorOr(color);
	}

	@ModifyArg(method = "render", index = 4, at = @At(value = "INVOKE", ordinal = 3, target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
	private int fillArg3(int color) {
		return ModKrowd.currentTabListCache.hudColorOr(color);
	}

	@Unique
	private TabEntryCache getCurrentEntry(PlayerListEntry fallback) {
		if (this.currentIndex < this.currentEntries.length) {
			return this.currentEntries[this.currentIndex];
		} else {
			ModKrowd.LOGGER.error("[PlayerListHudMixin] Tab list index out of bounds!"); // Incredibly unlucky timing issue?
			return new UnknownTabList.EntryCache(
                    TextCache.of((MutableText) this.getPlayerName(fallback)),
					fallback.getLatency()
			);
		}
	}
}