package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.function.UnaryOperator;

public record MainChatMessage(RankName sender, StyledString content, boolean isReal) implements Message {
    public static final MainChatMessage FAILURE = new MainChatMessage(RankName.FAILURE, StyledString.EMPTY, false);

    public static MainChatMessage readFast(StyledStringReader source) {
        RankName sender = RankName.readFast(source);
        if (!sender.isReal()) return FAILURE;

        source.skipUntilAfter(" ");
        StyledString content = source.readAll().isolate();
        if (content.isEmpty()) return FAILURE;

        return new MainChatMessage(sender, content, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                this.sender.appearance(),
                StyledString.SPACE,
                this.content
        );
    }

    public MainChatMessage mapSender(UnaryOperator<RankName> mapper) {
        return new MainChatMessage(mapper.apply(this.sender), this.content, this.isReal);
    }
}
