package dev.yatloaf.modkrowd.custom;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.network.chat.Component;

public interface Custom {
    GuiMessageTag MESSAGE_INDICATOR = new GuiMessageTag(
            CKColor.DARK_AQUA.textColor.getValue(),
            null,
            Component.literal("ModKrowd message"),
            "ModKrowd"
    );

    TextCache appearance();
}
