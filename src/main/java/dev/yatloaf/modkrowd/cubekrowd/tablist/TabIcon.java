package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.Util;

import java.util.Map;

public enum TabIcon {
    OAK_PLANKS("http://textures.minecraft.net/texture/24acc0078201335366085a3a0f3c8d4fcf117ca82f1e21ceb942ca295411a5ab"),
    OAK_RIGHT_ARROW("http://textures.minecraft.net/texture/bc2a245bf8882621b9d210d2db9cb61b33782449c794d832ae27beff40b3408e"),
    OAK_EXCLAMATION("http://textures.minecraft.net/texture/6d25daa7b26ab39764920cf9117333f3984536d17e484ea4240aba6824133fad"),
    DARK_GRAY("http://textures.minecraft.net/texture/ff9bb9e56125c8227b94bbda9f6e0f862931c229255ba8f1205d13c44c1bb561"),
    STONE_RIGHT_ARROW("http://textures.minecraft.net/texture/7201efcc7d834c6beb9e9bba9876db7cea6943c1173465de5b7cf814b6e021b2"),
    STONE_PLAYERS("http://textures.minecraft.net/texture/388ce66d0e945f64ab8d39fea77f690118ae16f29853bb81bf539083775782f0"),
    STONE_PING("http://textures.minecraft.net/texture/da81b643d75491a2f2b1bbfb57d5763081509015581c8d4bff33a984e9bd9a27"),
    UNKNOWN(""),
    ;

    public final String url;

    private static final Map<String, TabIcon> FROM_URL = Util.arrayToMap(values(), key -> key.url, value -> value);

    TabIcon(String url) {
        this.url = url;
    }

    public static TabIcon fromUrl(String url) {
        return FROM_URL.getOrDefault(url, UNKNOWN);
    }
}
