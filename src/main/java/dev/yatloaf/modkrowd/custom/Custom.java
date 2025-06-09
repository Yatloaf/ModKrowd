package dev.yatloaf.modkrowd.custom;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.Text;

public interface Custom {
    MessageIndicator MESSAGE_INDICATOR = new MessageIndicator(
            CKColor.DARK_AQUA.textColor.getRgb(),
            null,
            Text.literal("ModKrowd message"),
            "ModKrowd"
    );

    TextCache appearance();
}
