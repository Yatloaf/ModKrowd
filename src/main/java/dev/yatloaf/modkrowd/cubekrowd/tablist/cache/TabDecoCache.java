package dev.yatloaf.modkrowd.cubekrowd.tablist.cache;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabFooter;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabHeader;
import dev.yatloaf.modkrowd.mixinduck.PlayerTabOverlayDuck;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;

public class TabDecoCache {
    public TextCache header;
    public TextCache footer;

    private TabHeader tabHeader;
    private TabFooter tabFooter;

    private TextCache headerThemed;
    private TextCache footerThemed;

    public TabDecoCache() {
        this.invalidateAll();
    }

    @SuppressWarnings("ConstantValue")
    public void invalidateAll() {
        Minecraft minecraft = Minecraft.getInstance();
        // This can happen due to circular class loading
        if (minecraft == null || minecraft.gui == null) {
            this.header = TextCache.EMPTY;
            this.footer = TextCache.EMPTY;
        } else {
            PlayerTabOverlayDuck hudDuck = (PlayerTabOverlayDuck) minecraft.gui.getTabList();

            MutableComponent header = hudDuck.modKrowd$getHeader();
            this.header = header != null ? TextCache.of(header) : TextCache.EMPTY;
            MutableComponent footer = hudDuck.modKrowd$getFooter();
            this.footer = footer != null ? TextCache.of(footer) : TextCache.EMPTY;
        }

        this.tabHeader = null;
        this.tabFooter = null;

        this.invalidateThemed();
    }

    public void invalidateThemed() {
        this.headerThemed = null;
        this.footerThemed = null;

        ModKrowd.CONFIG.onTabDeco(this);
    }

    public TabHeader tabHeaderSoft() {
        if (this.tabHeader == null) {
            this.tabHeader = TabHeader.readSoft(StyledStringReader.of(this.header.styledString()));
        }
        return this.tabHeader;
    }

    public TabFooter tabFooterSoft() {
        if (this.tabFooter == null) {
            this.tabFooter = TabFooter.readSoft(StyledStringReader.of(this.footer.styledString()));
        }
        return this.tabFooter;
    }

    public void setHeaderThemed(TextCache themed) {
        this.headerThemed = themed;
    }

    public TextCache getHeaderThemed() {
        return this.headerThemed != null ? this.headerThemed : this.header;
    }

    public void setFooterThemed(TextCache themed) {
        this.footerThemed = themed;
    }

    public TextCache getFooterThemed() {
        return this.footerThemed != null ? this.footerThemed : this.footer;
    }
}
