package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.text.StyledString;

import java.util.function.Function;

public record TabCentered<E extends TabEntry>(StyledString prefix, E content, StyledString suffix) implements TabEntry {
    // Some tab list entries use regular and bold spaces to center text

    public static <E extends TabEntry> TabCentered<E> parse(StyledString source, Function<StyledString, E> contentParser) {
        int begin = 0;
        while (begin < source.length() && source.codePointAt(begin) == ' ') {
            begin++;
        }

        int end = source.length();
        if (begin < end) {
            while (end > 0 && source.codePointAt(end - 1) == ' ') {
                end--;
            }
        }

        StyledString prefix = source.subView(0, begin).isolate();
        E content = contentParser.apply(source.subView(begin, end));
        StyledString suffix = source.subView(end).isolate();

        return new TabCentered<>(prefix, content, suffix);
    }
}
