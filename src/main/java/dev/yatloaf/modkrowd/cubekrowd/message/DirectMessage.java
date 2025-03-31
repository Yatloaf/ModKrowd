package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.CubeKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record DirectMessage(Direction direction, String sender, String recipient, StyledString content, boolean isReal)
        implements Message {
    public static final DirectMessage FAILURE = new DirectMessage(
            Direction.UNKNOWN, "", "", StyledString.EMPTY, false
    );

    public static final String ME = "me";

    public static DirectMessage readFast(StyledStringReader source) {
        StyledString senderStyled = source.readUntil(" ");
        if (senderStyled.isEmpty()) return FAILURE;
        String sender = senderStyled.toUnstyledString();

        source.skipUntilAfter(CubeKrowd.RIGHT_ARROW);
        source.skipUntilAfter(" ");

        StyledString recipientStyled = source.readUntil(" ");
        if (recipientStyled.isEmpty()) return FAILURE;
        String recipient = recipientStyled.toUnstyledString();

        source.skipUntilAfter(" ");
        StyledString content = source.readAll().isolate();
        if (content.isEmpty()) return FAILURE;

        if (ME.equals(sender)) {
            if (ME.equals(recipient)) {
                return new DirectMessage(Direction.LOOP, sender, recipient, content, true);
            } else {
                return new DirectMessage(Direction.OUTGOING, sender, recipient, content, true);
            }
        } else {
            if (ME.equals(recipient)) {
                return new DirectMessage(Direction.INCOMING, sender, recipient, content, true);
            } else return FAILURE; // How did this happen? Socialspy?
        }
    }

    public String other() {
        return switch (this.direction) {
            case INCOMING -> this.sender;
            case OUTGOING -> this.recipient;
            case LOOP -> SelfPlayer.username();
            default -> "";
        };
    }

    public StyledString appearance() {
        return StyledString.concat(
                StyledString.fromString(this.sender + " " + CubeKrowd.RIGHT_ARROW + " " + this.recipient + " ", CKColor.GRAY.style),
                this.content
        );
    }
}
