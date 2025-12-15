package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;

public record AlohaMessage(Aloha aloha, StyledString name, boolean isReal) implements Message {
    public static final AlohaMessage FAILURE = new AlohaMessage(Aloha.UNKNOWN, StyledString.EMPTY, false);

    public static AlohaMessage parseFast(ComponentContents contents) {
        if (!(contents instanceof TranslatableContents translatable)) return FAILURE;

        Aloha aloha = Aloha.parse(translatable);
        if (!aloha.isReal()) return FAILURE;

        Object[] args = translatable.getArgs();
        if (args.length < 1) return FAILURE;

        StyledString name;
        if (args[0] instanceof Component text) {
            name = StyledString.fromText(text);
        } else {
            name = StyledString.fromString(args[0].toString());
        }

        return new AlohaMessage(aloha, name, true);
    }

    public StyledString appearance() {
        return StyledString.fromText(Component.translatable(this.aloha.key, this.name.toText()), CKColor.YELLOW.style);
    }
}
