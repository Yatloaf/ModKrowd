package dev.yatloaf.modkrowd.cubekrowd.common.cache;

import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public abstract class TextCache {
    /**
     * The empty {@link TextCache}.
     */
    public static final TextCache EMPTY = new OfEmpty();

    /**
     * Get the {@link MutableComponent} representation after generating it if required.
     * <p>
     * <b>Do not mutate this object without copying it first.</b>
     *
     * @return The {@link MutableComponent} representation of the original.
     */
    public abstract MutableComponent text();

    /**
     * Get the {@link StyledString} representation after generating it if required.
     *
     * @return The {@link StyledString} representation of the original.
     */
    public abstract StyledString styledString();

    /**
     * Get the plain {@link String} representation after generating it if required.
     *
     * @return The plain {@link String} representation of the original.
     */
    public abstract String string();

    /**
     * Box a {@link MutableComponent}, deferring and caching its conversion into {@link StyledString} or {@link String}.
     * <p>
     * <b>Do not mutate the source after boxing without copying it first.</b>
     *
     * @param text The original {@link MutableComponent}.
     * @return A cache of the original and its converted forms.
     */
    public static TextCache of(@NotNull MutableComponent text) {
        return new OfText(text);
    }

    /**
     * Box a {@link StyledString}, deferring and caching its conversion into {@link MutableComponent} or {@link String}.
     *
     * @param styledString The original {@link StyledString}.
     * @return A cache of the original and its converted forms.
     */
    public static TextCache of(@NotNull StyledString styledString) {
        return new OfStyledString(styledString);
    }

    private static class OfText extends TextCache {
        private final MutableComponent text;

        private StyledString styledString;
        private String string;

        private OfText(MutableComponent text) {
            this.text = text;
        }

        @Override
        public final MutableComponent text() {
            return this.text;
        }

        @Override
        public final StyledString styledString() {
            if (this.styledString == null) {
                this.styledString = StyledString.fromText(this.text);
            }
            return this.styledString;
        }

        @Override
        public final String string() {
            if (this.string == null) {
                this.string = this.text.getString();
            }
            return this.string;
        }
    }

    private static class OfStyledString extends TextCache {
        private final StyledString styledString;

        private MutableComponent text;
        private String string;

        private OfStyledString(StyledString styledString) {
            this.styledString = styledString;
        }

        @Override
        public MutableComponent text() {
            if (this.text == null) {
                this.text = this.styledString.toText();
            }
            return this.text;
        }

        @Override
        public StyledString styledString() {
            return this.styledString;
        }

        @Override
        public String string() {
            if (this.string == null) {
                this.string = this.styledString.toUnstyledString();
            }
            return this.string;
        }

    }

    private static class OfEmpty extends TextCache {
        private static final MutableComponent EMPTY_TEXT = Component.empty();

        @Override
        public MutableComponent text() {
            return EMPTY_TEXT;
        }

        @Override
        public StyledString styledString() {
            return StyledString.EMPTY;
        }

        @Override
        public String string() {
            return "";
        }
    }
}
