package dev.yatloaf.modkrowd.cubekrowd.common;

import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.minecraft.ChatFormatting;

import java.util.EnumSet;

/// See [`MessageKrowdVelocityPlugin#formatMessage`](https://gitlab.com/cubekrowd/plugins/messagekrowd/-/blob/master/velocity/src/main/java/net/cubekrowd/messagekrowd/velocity/MessageKrowdVelocityPlugin.java)
public enum FormattingPermission {
    NONE(
            EnumSet.noneOf(ChatFormatting.class),
            MiniMessage.builder().tags(TagResolver.empty()).build()
    ),
    LIMITED(
            EnumSet.of(
                    ChatFormatting.STRIKETHROUGH,
                    ChatFormatting.UNDERLINE,
                    ChatFormatting.ITALIC,
                    ChatFormatting.RESET
            ),
            MiniMessage.builder().tags(
                    TagResolver.builder().resolvers(
                            StandardTags.decorations(TextDecoration.ITALIC),
                            StandardTags.decorations(TextDecoration.UNDERLINED),
                            StandardTags.decorations(TextDecoration.STRIKETHROUGH)
                    ).build()
            ).build()
    ),
    ALL(
            EnumSet.allOf(ChatFormatting.class),
            MiniMessage.miniMessage()
    ),
    ;

    public final EnumSet<ChatFormatting> legacy;
    public final MiniMessage miniMessage;

    FormattingPermission(EnumSet<ChatFormatting> legacy, MiniMessage miniMessage) {
        this.legacy = legacy;
        this.miniMessage = miniMessage;
    }
}
