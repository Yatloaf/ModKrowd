package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.function.UnaryOperator;

public record FishslapChatMessage(RankName sender, StyledString content, boolean isReal) implements Message {
    public static final FishslapChatMessage FAILURE = new FishslapChatMessage(RankName.FAILURE, StyledString.EMPTY, false);

    public static FishslapChatMessage readFast(StyledStringReader source) {
        if (!source.skipIfNext("<")) return FAILURE;

        RankName sender = RankName.readFast(source);
        if (!sender.isReal()) return FAILURE;

        source.skipUntilAfter("> ");

        StyledString content = source.readAll().isolate();
        if (content.isEmpty()) return FAILURE;

        return new FishslapChatMessage(sender, content, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                StyledString.fromString("<"),
                this.sender.appearance(),
                StyledString.fromString("> "),
                this.content
        );
    }

    public FishslapChatMessage mapSender(UnaryOperator<RankName> mapper) {
        return new FishslapChatMessage(mapper.apply(this.sender), this.content, this.isReal);
    }
}
