package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public record AlohaMessage(Aloha aloha, StyledString name, boolean isReal) implements Message {
    public static final AlohaMessage FAILED = new AlohaMessage(Aloha.UNKNOWN, StyledString.EMPTY, false);

    public static AlohaMessage parseFast(TranslatableTextContent content) {
        Aloha aloha = Aloha.parse(content);
        if (!aloha.isReal()) return FAILED;

        Object[] args = content.getArgs();
        if (args.length < 1) return FAILED;

        StyledString name;
        if (args[0] instanceof Text text) {
            name = StyledString.fromText(text);
        } else {
            name = StyledString.fromString(args[0].toString());
        }

        return new AlohaMessage(aloha, name, true);
    }

    public StyledString appearance() {
        return StyledString.fromText(Text.translatable(this.aloha.key, this.name.toText()), CKColor.YELLOW.style);
    }
}
