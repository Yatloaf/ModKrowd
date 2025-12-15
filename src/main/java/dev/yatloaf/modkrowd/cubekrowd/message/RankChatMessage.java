package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.function.UnaryOperator;

public record RankChatMessage(RankName sender, StyledString content, boolean isReal) implements Message {
    public static final RankChatMessage FAILURE = new RankChatMessage(RankName.FAILURE, StyledString.EMPTY, false);

    public static RankChatMessage readFast(StyledStringReader source) {
        RankName sender = RankName.readFast(source);
        if (!sender.isReal()) return FAILURE;

        if (!source.skipIfNext(" ")) return FAILURE;

        StyledString content = source.readAll().isolate();

        return new RankChatMessage(sender, content, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                this.sender.appearance(),
                StyledString.SPACE,
                this.content
        );
    }

    public RankChatMessage mapSender(UnaryOperator<RankName> mapper) {
        return new RankChatMessage(mapper.apply(this.sender), this.content, this.isReal);
    }
}
