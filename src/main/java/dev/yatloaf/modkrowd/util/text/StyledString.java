package dev.yatloaf.modkrowd.util.text;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PrimitiveIterator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

/**
 * Text but good
 */
public class StyledString {
    // TODO: More documentation

    private static final int[] EMPTY_CODEPOINTS = new int[0];
    private static final Style[] EMPTY_STYLES = new Style[0];
    private static final Style FALSE_STYLE = Style.EMPTY
            .withColor(Formatting.WHITE)
            .withObfuscated(false)
            .withBold(false)
            .withStrikethrough(false)
            .withUnderline(false)
            .withItalic(false)
            .withFont(MinecraftClient.DEFAULT_FONT_ID);

    public static final StyledString EMPTY = new StyledString(EMPTY_CODEPOINTS, EMPTY_STYLES);
    public static final StyledString SPACE = fromString(" ");
    public static final StyledString NEWLINE = fromString("\n");

    // Ranges to avoid copying data (this is a value class)

    private final int[] codePoints;
    private final int codePointsIndex;

    private final Style[] styles;
    private final int stylesIndex;

    private final int length;

    private boolean hashIsCalculated = false;
    private int hash = 0;

    private StyledString(int[] codePoints, Style[] styles) {
        this(codePoints, 0, styles, 0, codePoints.length);
    }

    private StyledString(int[] codePoints, int codePointsIndex, Style[] styles, int stylesIndex, int length) {
        // Assume the arguments are safe
        this.codePoints = codePoints;
        this.codePointsIndex = codePointsIndex;
        this.styles = styles;
        this.stylesIndex = stylesIndex;
        this.length = length;
    }

    /**
     * Construct a new {@link StyledString} from a {@link String} with default style.
     *
     * @param source The content
     * @return A new {@code StyledString} with default style
     */
    public static StyledString fromString(@NotNull String source) {
        return fromString(source, FALSE_STYLE);
    }

    /**
     * Construct a new {@link StyledString} from a {@link String} with a monotone {@link Style},
     * where the empty fields are filled with defaults.
     *
     * @param source The content
     * @param style  The style for every character
     * @return A new {@code StyledString} with a monotone style
     */
    public static StyledString fromString(@NotNull String source, @NotNull Style style) {
        Style filledStyle = style.withParent(FALSE_STYLE);

        int[] codePoints = source.codePoints().toArray();
        Style[] styles = new Style[codePoints.length];
        Arrays.fill(styles, filledStyle);

        return new StyledString(codePoints, styles);
    }

    public static StyledString fromText(@NotNull Text source) {
        return fromText(source, FALSE_STYLE);
    }

    public static StyledString fromText(@NotNull Text source, @NotNull Style parentStyle) {
        Style filledParentStyle = parentStyle.withParent(FALSE_STYLE);

        IntList codepoints = new IntArrayList();
        List<Style> styles = new ArrayList<>();

        source.visit((style, string) -> {
            Style filledStyle = style.withParent(filledParentStyle);
            string.codePoints().forEachOrdered(c -> {
                codepoints.add(c);
                styles.add(filledStyle);
            });
            return Optional.empty();
        }, filledParentStyle);

        return new StyledString(codepoints.toIntArray(), styles.toArray(EMPTY_STYLES));
        // This would use toArray(Style[]::new) if the ArrayList implementation was any good
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof StyledString thatStr) {
            return Arrays.equals(
                    this.codePoints, this.codePointsIndex, this.codePointsIndex + this.length,
                    thatStr.codePoints, thatStr.codePointsIndex, thatStr.codePointsIndex + thatStr.length
            ) && Arrays.equals(
                    this.styles, this.stylesIndex, this.stylesIndex + this.length,
                    thatStr.styles, thatStr.stylesIndex, thatStr.stylesIndex + thatStr.length
            );
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (this.hashIsCalculated) {
            return this.hash;
        } else {
            int result = 1;
            for (int i = 0; i < this.length; i++) {
                result = 31 * result + this.codePoints[this.codePointsIndex + i];
                result = 31 * result + this.styles[this.stylesIndex + i].hashCode();
            }
            this.hash = result;
            this.hashIsCalculated = true;
            return result;
        }
    }

    public boolean equalsString(String that) {
        // String length is only approximately known
        if (that.length() > this.length * 2 || that.length() < this.length) return false;

        PrimitiveIterator.OfInt thatIterator = that.codePoints().iterator();
        for (int i = 0; i < this.length; i++) {
            if (thatIterator.hasNext()) {
                if (thatIterator.nextInt() != this.codePointAt(i)) return false;
            } else return false;
        }
        // If true: that is longer than this
        // If false: that is equal to this
        return !thatIterator.hasNext();
    }

    public boolean equalsCodePoints(int[] that) {
        return Arrays.equals(
                this.codePoints, this.codePointsIndex, this.codePointsIndex + this.length,
                that, 0, that.length
        );
    }

    /**
     * Return {@code true} if {@link StyledString#length()} is {@code 0}.
     *
     * @return {@code true} if {@link StyledString#length()} is {@code 0}, otherwise {@code false}
     */
    public boolean isEmpty() {
        return this.length == 0;
    }

    public boolean startsWith(StyledString that) {
        return that.length <= this.length && this.subView(0, that.length).equals(that);
    }

    public boolean startsWith(String that) {
        // String length is only approximately known
        if (that.length() > this.length * 2) return false; // Confirmed too long
        PrimitiveIterator.OfInt thatIterator = that.codePoints().iterator();
        for (int i = 0; i < this.length; i++) {
            if (thatIterator.hasNext()) {
                if (thatIterator.nextInt() != this.codePointAt(i)) return false;
            } else return true;
        }
        // If true: that is longer than this
        // If false: that is equal to this
        return !thatIterator.hasNext();
    }

    public boolean startsWithCodePoints(int[] that) {
        return that.length <= this.length && this.subView(0, that.length).equalsCodePoints(that);
    }

    public boolean endsWith(StyledString that) {
        return that.length <= this.length && this.subView(this.length - that.length).equals(that);
    }

    public <V> V mapStartOrDefault(Map<StyledString, V> map, V fallback) {
        int keyMaxLength = map.keySet().stream().mapToInt(StyledString::length).max().orElse(0);

        int maxLength = Math.min(keyMaxLength, this.length);

        for (int index = 0; index <= maxLength; index++) {
            StyledString sub = this.subView(0, index);
            if (map.containsKey(sub)) {
                return map.get(sub);
            }
        }

        return fallback;
    }

    /**
     * Convert this into {@link MutableText}.
     * @return This {@link StyledString} as a {@link MutableText}
     */
    public MutableText toText() {
        MutableText text = Text.empty();
        int index = 0;
        while (index < this.length) {
            int sectionStart = index;
            Style sectionStyle = this.styles[this.stylesIndex + index];
            while (index < this.length && this.styles[this.stylesIndex + index].equals(sectionStyle)) {
                index++;
            }
            text.append(
                    Text.literal(
                            new String(this.codePoints, this.codePointsIndex + sectionStart, index - sectionStart)
                    ).setStyle(sectionStyle)
            );
        }
        return text;
    }

    /**
     * Convert this into a {@link String} that uses legacy formatting codes wherever possible.
     * @param formatChar The formatting code character
     * @return This {@link StyledString} as a legacy formatted {@link String} using a custom formatting character
     */
    public String toFormattedString(String formatChar) {
        StringBuilder builder = new StringBuilder(this.length);
        int index = 0;
        RollingStyle previousSectionStyle = RollingStyle.DEFAULT;
        while (index < this.length) {
            int sectionStart = index;
            RollingStyle sectionStyle = RollingStyle.fromStyle(this.styles[this.stylesIndex + index]);
            while (true) {
                index++;
                if (index >= this.length) {
                    break;
                }
                RollingStyle currentStyle = RollingStyle.fromStyle(this.styles[this.stylesIndex + index]);
                if (!currentStyle.equals(sectionStyle)) {
                    break;
                }
            }
            sectionStyle.difference(previousSectionStyle, formatChar, builder);
            builder.append(new String(this.codePoints, this.codePointsIndex + sectionStart, index - sectionStart));
            previousSectionStyle = sectionStyle;
        }
        return builder.toString();
    }

    public String toUnstyledString() {
        return new String(this.codePoints, this.codePointsIndex, this.length);
    }

    /**
     * Convert this into a {@link String} that uses legacy formatting codes with the default {@code §} wherever possible.
     * @return This {@link StyledString} as a legacy formatted {@link String}
     */
    @Override
    public String toString() {
        return this.toFormattedString("§");
    }

    public StyledString subView(int beginIndex) {
        return this.subView(beginIndex, this.length);
    }

    public StyledString subView(int beginIndex, int endIndex) throws IndexOutOfBoundsException {
        if (beginIndex > this.length || endIndex > this.length || beginIndex > endIndex || beginIndex < 0) {
            throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + ", endIndex: " + endIndex + ", length: " + this.length);
        }
        return new StyledString(
                this.codePoints, this.codePointsIndex + beginIndex,
                this.styles, this.stylesIndex + beginIndex,
                endIndex - beginIndex
        );
    }

    /**
     * Creates a copy of the string that references a fitted array.
     * Use this for substrings that will be used for a longer time so the original array can be garbage collected.
     * @return The maximally space-efficient version of the string.
     */
    public StyledString isolate() {
        if (this.codePointsIndex == 0 && this.stylesIndex == 0 && this.codePoints.length == this.length && this.styles.length == this.length) {
            return this;
        } else {
            return new StyledString(
                    Arrays.copyOfRange(this.codePoints, this.codePointsIndex, this.codePointsIndex + this.length), 0,
                    Arrays.copyOfRange(this.styles, this.stylesIndex, this.stylesIndex + this.length), 0,
                    this.length
            );
        }
    }

    public static StyledString concat(StyledString... strings) {
        int totalLength = 0;
        for (StyledString str : strings) {
            totalLength += str.length;
        }

        int[] codePoints = new int[totalLength];
        Style[] styles = new Style[totalLength];

        int index = 0;
        for (StyledString str : strings) {
            System.arraycopy(str.codePoints, str.codePointsIndex, codePoints, index, str.length);
            System.arraycopy(str.styles, str.stylesIndex, styles, index, str.length);
            index += str.length;
        }

        return new StyledString(codePoints, styles);
    }

    public StyledString mapCodePoints(IntUnaryOperator mapper) {
        int[] codePoints = new int[this.length];

        for (int i = 0; i < codePoints.length; i++) {
            codePoints[i] = mapper.applyAsInt(this.codePointAt(i));
        }

        return new StyledString(codePoints, 0, this.styles, this.stylesIndex, this.length);
    }

    public StyledString mapStyle(Function<Style, Style> mapper) {
        Style[] styles = new Style[this.length];
        Map<Style, Style> identities = new HashMap<>(); // Remove if Style becomes a value class someday

        for (int i = 0; i < styles.length; i++) {
            Style currentStyle = mapper.apply(this.styles[i + this.stylesIndex]);
            if (identities.containsKey(currentStyle)) {
                styles[i] = identities.get(currentStyle);
            } else {
                identities.put(currentStyle, currentStyle);
                styles[i] = currentStyle;
            }
        }

        return new StyledString(this.codePoints, this.codePointsIndex, styles, 0, this.length);
    }

    public StyledString fillStrikethrough() {
        return this.mapStyle(style -> style.withStrikethrough(true));
    }

    public StyledString fillColor(TextColor color) {
        return this.mapStyle(style -> style.withColor(color));
    }

    public StyledString strip() {
        if (this.isEmpty()) {
            return EMPTY;
        }

        int start = 0;
        while (Character.isWhitespace(this.codePointAt(start))) {
            start++;
            if (start == this.length) return EMPTY;
        }

        int end = this.length;
        while (Character.isWhitespace(this.codePointAt(end - 1))) {
            end--;
        }

        return this.subView(start, end);
    }

    public StyledString insert(int index, String replacement) {
        int[] codePoints = new int[this.length];
        System.arraycopy(this.codePoints, this.codePointsIndex, codePoints, 0, index);

        int i = index;
        PrimitiveIterator.OfInt replacementIterator = replacement.codePoints().iterator();
        while (replacementIterator.hasNext()) {
            codePoints[i] = replacementIterator.nextInt();
            i += 1;
        }

        System.arraycopy(this.codePoints, this.codePointsIndex + i, codePoints, i, this.length - i);
        return new StyledString(codePoints, 0, this.styles, this.stylesIndex, this.length);
    }

    public boolean contains(StyledString that) {
        return this.firstIndexOf(that) != -1;
    }

    public int firstIndexOf(StyledString that) {
        int startIndex = 0;
        while (startIndex + that.length <= this.length) {
            if (this.subView(startIndex, startIndex + that.length).equals(that)) {
                return startIndex;
            }
            startIndex++;
        }
        return -1;
    }

    public int firstIndexOf(String that) {
        return this.firstIndexOfCodePoints(that.codePoints().toArray());
    }

    public int firstIndexOfCodePoints(int[] that) {
        int startIndex = 0;
        while (startIndex + that.length <= this.length) {
            if (this.subView(startIndex, startIndex + that.length).equalsCodePoints(that)) {
                return startIndex;
            }
            startIndex++;
        }
        return -1;
    }

    public int lastIndexOf(String that) {
        int[] thatCodePoints = that.codePoints().toArray();
        int startIndex = this.length - thatCodePoints.length;
        while (startIndex >= 0) {
            if (this.subView(startIndex, startIndex + thatCodePoints.length).equalsCodePoints(thatCodePoints)) {
                return startIndex;
            }
            startIndex--;
        }
        return -1;
    }

    public Style firstStyle() {
        if (this.length > 0) {
            return this.styleAt(0);
        } else {
            return FALSE_STYLE;
        }
    }

    public int codePointAt(int index) {
        return this.codePoints[this.codePointsIndex + index];
    }

    public Style styleAt(int index) {
        return this.styles[this.stylesIndex + index];
    }

    public int length() {
        return this.length;
    }

    private record RollingStyle(@Nullable Formatting color, boolean obfuscated, boolean bold, boolean strikethrough,
                                boolean underline, boolean italic) {
        public static final RollingStyle DEFAULT =
                new RollingStyle(null, false, false, false, false, false);

        public static RollingStyle fromStyle(Style style) {
            TextColor textColor = style.getColor();
            Formatting color = textColor == null ? null : Formatting.byName(textColor.getName());
            return new RollingStyle(color, style.isObfuscated(), style.isBold(), style.isStrikethrough(), style.isUnderlined(), style.isItalic());
        }

        @SuppressWarnings("UnusedReturnValue")
        public StringBuilder difference(RollingStyle previous, String formatChar, StringBuilder builder) {
            if (this.color != null && this.color != previous.color) {
                builder.append(formatChar);
                builder.append(this.color.getCode());
            } else if (this.lessThan(previous)) {
                builder.append(formatChar);
                builder.append(Formatting.RESET.getCode());
            } else {
                if (this.obfuscated && !previous.obfuscated)
                    this.formatting(formatChar, Formatting.OBFUSCATED, builder);
                if (this.bold && !previous.bold) this.formatting(formatChar, Formatting.BOLD, builder);
                if (this.strikethrough && !previous.strikethrough)
                    this.formatting(formatChar, Formatting.STRIKETHROUGH, builder);
                if (this.underline && !previous.underline) this.formatting(formatChar, Formatting.UNDERLINE, builder);
                if (this.italic && !previous.italic) this.formatting(formatChar, Formatting.ITALIC, builder);
                return builder;
            }
            if (this.obfuscated) this.formatting(formatChar, Formatting.OBFUSCATED, builder);
            if (this.bold) this.formatting(formatChar, Formatting.BOLD, builder);
            if (this.strikethrough) this.formatting(formatChar, Formatting.STRIKETHROUGH, builder);
            if (this.underline) this.formatting(formatChar, Formatting.UNDERLINE, builder);
            if (this.italic) this.formatting(formatChar, Formatting.ITALIC, builder);
            return builder;
        }

        private boolean lessThan(RollingStyle that) {
            return !this.obfuscated && that.obfuscated
                    || !this.bold && that.bold
                    || !this.strikethrough && that.strikethrough
                    || !this.underline && that.underline
                    || !this.italic && that.italic;
        }

        @SuppressWarnings("UnusedReturnValue")
        private StringBuilder formatting(String formatChar, Formatting formatting, StringBuilder builder) {
            builder.append(formatChar);
            builder.append(formatting.getCode());
            return builder;
        }
    }
}
