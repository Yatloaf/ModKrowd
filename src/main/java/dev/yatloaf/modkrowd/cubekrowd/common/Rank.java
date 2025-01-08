package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record Rank(RankLetters letters, RankBrackets brackets, RankPipes pipes, boolean isReal) {
    public static final Rank FAILURE = new Rank(RankLetters.UNKNOWN, RankBrackets.UNKNOWN, RankPipes.UNKNOWN, false);

    public static Rank readSoft(StyledStringReader source) {
        RankPipes pipes = RankPipes.read(source);
        RankBrackets brackets = RankBrackets.readLeft(source);
        RankLetters letters = RankLetters.read(source);
        // move to end
        RankBrackets.readRight(source);
        RankPipes.read(source);

        boolean isReal = pipes.isReal() && brackets.isReal() && letters.isReal();
        return new Rank(letters, brackets, pipes, isReal);
    }

    public static Rank readFast(StyledStringReader source) {
        RankPipes pipes = RankPipes.read(source);
        if (!pipes.isReal()) return FAILURE;
        RankBrackets brackets = RankBrackets.readLeft(source);
        if (!brackets.isReal()) return FAILURE;
        RankLetters letters = RankLetters.read(source);
        if (!letters.isReal()) return FAILURE;
        // move to end
        if (!RankBrackets.readRight(source).isReal()) return FAILURE;
        if (!RankPipes.read(source).isReal()) return FAILURE;

        return new Rank(letters, brackets, pipes, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                this.pipes.pipe,
                this.brackets.leftBracket,
                this.letters.letter,
                this.brackets.rightBracket,
                this.pipes.pipe
        );
    }
}
