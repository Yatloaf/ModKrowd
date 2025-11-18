package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Map;

public enum Aloha {
    JOIN("multiplayer.player.joined"),
    LEAVE("multiplayer.player.left"),
    UNKNOWN("");

    public final String key;

    Aloha(String translationKey) {
        this.key = translationKey;
    }

    private static final Map<String, Aloha> FROM_KEY = Util.arrayToMap(values(), item -> item.key, item -> item);

    public static Aloha parse(TranslatableContents content) {
        return FROM_KEY.getOrDefault(content.getKey(), UNKNOWN);
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
