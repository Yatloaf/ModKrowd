package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public abstract class TextCache {
    /**
     * The empty {@link TextCache}.
     */
    public static final TextCache EMPTY = new OfEmpty();

    /**
     * Get the {@link Component} representation after generating it if required.
     * <p>
     * <b>Do not mutate this object without copying it first.</b>
     *
     * @return The {@link Component} representation of the original.
     */
    public abstract Component text();

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
     * Box a {@link Component}, deferring and caching its conversion into {@link StyledString} or {@link String}.
     * <p>
     * <b>Do not mutate the source after boxing without copying it first.</b>
     *
     * @param text The original {@link Component}.
     * @return A cache of the original and its converted forms.
     */
    public static TextCache of(@NonNull Component text) {
        return new OfText(text);
    }

    /**
     * Box a {@link StyledString}, deferring and caching its conversion into {@link Component} or {@link String}.
     *
     * @param styledString The original {@link StyledString}.
     * @return A cache of the original and its converted forms.
     */
    public static TextCache of(@NonNull StyledString styledString) {
        return new OfStyledString(styledString);
    }

    private static class OfText extends TextCache {
        private final Component text;

        private StyledString styledString;
        private String string;

        private OfText(Component text) {
            this.text = text;
        }

        @Override
        public final Component text() {
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

        private Component text;
        private String string;

        private OfStyledString(StyledString styledString) {
            this.styledString = styledString;
        }

        @Override
        public Component text() {
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
        private static final Component EMPTY_TEXT = Component.empty();

        @Override
        public Component text() {
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
