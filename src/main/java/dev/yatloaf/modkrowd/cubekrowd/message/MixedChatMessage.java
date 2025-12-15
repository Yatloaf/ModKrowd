package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.function.UnaryOperator;

public record MixedChatMessage(RankName sender, StyledString content, boolean isReal) implements Message {
    public static final MixedChatMessage FAILURE = new MixedChatMessage(RankName.FAILURE, StyledString.EMPTY, false);

    public static MixedChatMessage readFast(StyledStringReader source) {
        if (!source.skipIfNext("<")) return FAILURE;

        RankName sender = RankName.readFast(source);
        if (!sender.isReal()) return FAILURE;

        source.skipUntilAfter("> ");

        StyledString content = source.readAll().isolate();
        if (content.isEmpty()) return FAILURE;

        return new MixedChatMessage(sender, content, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                StyledString.fromString("<"),
                this.sender.appearance(),
                StyledString.fromString("> "),
                this.content
        );
    }

    public MixedChatMessage mapSender(UnaryOperator<RankName> mapper) {
        return new MixedChatMessage(mapper.apply(this.sender), this.content, this.isReal);
    }
}
