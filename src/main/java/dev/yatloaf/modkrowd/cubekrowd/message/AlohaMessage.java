package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

public record AlohaMessage(Aloha aloha, StyledString name, boolean isReal) implements Message {
    public static final AlohaMessage FAILED = new AlohaMessage(Aloha.UNKNOWN, StyledString.EMPTY, false);

    public static AlohaMessage parseFast(TranslatableContents content) {
        Aloha aloha = Aloha.parse(content);
        if (!aloha.isReal()) return FAILED;

        Object[] args = content.getArgs();
        if (args.length < 1) return FAILED;

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
