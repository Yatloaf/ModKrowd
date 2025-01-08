package dev.yatloaf.modkrowd.cubekrowd.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.CKStuff;
import dev.yatloaf.modkrowd.cubekrowd.common.RankBrackets;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Direction;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MainSubserver;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class PreviewCommands {
    private static final CommandDispatcher<Object> DISPATCHER = new CommandDispatcher<>();

    static {
        command(
                literal("message")
                        .then(argument("target", StringArgumentType.word())
                                .then(argument("message", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String target = StringArgumentType.getString(context, "target");
                                            String message = StringArgumentType.getString(context, "message");
                                            throw result(new DirectMessage(
                                                    Direction.OUTGOING,
                                                    DirectMessage.ME,
                                                    target,
                                                    StyledString.fromString(message, CKColor.GRAY.style),
                                                    true
                                            ).appearance());
                                        }))),
                "msg", "m"
        );
        command(
                literal("reply")
                        .then(argument("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    if (ModKrowd.CONFIG.MESSAGE_PREVIEW.replyTarget == null) {
                                        throw result(Text.literal("No reply target").formatted(Formatting.RED));
                                    }
                                    String target = ModKrowd.CONFIG.MESSAGE_PREVIEW.replyTarget;
                                    String message = StringArgumentType.getString(context, "message");
                                    throw result(new DirectMessage(
                                            Direction.OUTGOING,
                                            DirectMessage.ME,
                                            target,
                                            StyledString.fromString(message, CKColor.GRAY.style),
                                            true
                                    ).appearance());
                                })),
                "r"
        );

        // CHATTYMOTES
        chattymote0("Sorry, %s is unauthorized access to use this command", "401");
        chattymoteV("%s appreciates it", "%s appreciates %s", "appreciate"); // UNDOCUMENTED
        chattymoteV("%s barks", "%s barks at %s", "bark");
        chattymoteV("%s will be right back", "%s will be right back %s", "brb"); // UNDOCUMENTED
        chattymote2("%s challenges %s to %s", "chal", "challenge");
        chattymote0("%s applauds", "clap", "claps");
        command(
                literal("coin").executes(context -> {
                    checkChattymote();
                    throw result(Text.literal(SelfPlayer.username() + " flips a coin. It shows ").formatted(Formatting.LIGHT_PURPLE)
                            .append(Text.literal("#####").formatted(Formatting.OBFUSCATED)));
                })
        );
        chattymote1("%s congratulates %s",
                "congratulations", "congrats", "congratz", "grats", "gratz", "gz");
        chattymoteV("%s gives out cookies", "%s gives %s a cookie", "cookie");
        chattymote0("%s cries", "cry", "cri", "cries");
        command(
                literal("dice")
                        .executes(context -> {
                            checkChattymote();
                            throw result(Text.literal(SelfPlayer.username() + " throws a 6 sided dice. It shows ").formatted(Formatting.LIGHT_PURPLE)
                                    .append(Text.literal("0").formatted(Formatting.OBFUSCATED)));
                        })
                        .then(argument("sides", StringArgumentType.word())
                                .executes(context -> {
                                    checkChattymote();
                                    String sides = Util.normalizeIntOr(StringArgumentType.getString(context, "sides"), "NaN");
                                    throw result(Text.literal(SelfPlayer.username() + " throws a " + sides + " sided dice. It shows ").formatted(Formatting.LIGHT_PURPLE)
                                            .append(Text.literal("0").formatted(Formatting.OBFUSCATED)));
                                }))
        );
        chattymote0("%s dances", "dance");
        chattymote0("%s is facepalming", "facepalm", "face", "fp");
        chattymote0("%s failed horribly", "fail");
        // /fall is documented, but does not actually exist
        chattymoteV("%s picks flowers", "%s picks flowers for %s", "flowers"); // UNDOCUMENTED
        chattymoteV("%s high fives", "%s high fives %s", "highfive", "highf", "h5"); // UNDOCUMENTED
        chattymote1("%s hugs %s", "hug");
        chattymote0("%s has an idea!", "idea", "lightbulb");
        chattymote0("%s is joking", "jk");
        chattymote0("%s laughs", "laugh", "lol");
        chattymote0("%s is forever alone", "lonely");
        chattymoteV("%s loses", "%s loses %s", "lose", "lost");
        chattymote1("%s loves %s", "love");
        chattymote0("%s doesn't like it", "meh");
        // chattymote0("%s tried to meow but coughed up a hairball!", "meow");
        chattymote0("%s thought they could be op", "opme");
        chattymote0("%s is having a party \\o/", "party", "partayy");
        chattymoteV("%s pats", "%s pats %s", "pat"); // UNDOCUMENTED
        chattymote0("%s goes poof!", "poof"); // UNDOCUMENTED
        chattymote0("%s grabs popcorn", "popcorn", "pc", "pop", "corn");
        chattymoteV("%s sheds a tear R.I.P.", "%s sheds a tear at %s", "rip"); // UNDOCUMENTED
        chattymote0("%s is rolling on the floor laughing", "rofl");
        chattymoteV("%s runs away!", "%s runs to %s", "run"); // UNDOCUMENTED
        chattymote2("%s slaps %s with %s", "slap");
        chattymote0("%s falls asleep", "sleep", "zzz");
        chattymote0("%s got slimed", "slime");
        chattymoteV("%s claps slowly", "%s claps slowly at %s", "slowclap"); // UNDOCUMENTED
        chattymoteV("%s says Soon\u2122", "%s says Soon\u2122 %s", "soon"); // UNDOCUMENTED
        chattymoteV("%s apologizes", "%s apologizes to %s", "sorry", "sowwy");
        chattymote0("%s is stunned", "stunned", "stun");
        chattymoteV("%s gives thanks", "%s gives thanks to %s", "thanks", "thx", "ty"); // UNDOCUMENTED
        chattymoteV("%s is thinking", "%s is thinking about %s", "think"); // UNDOCUMENTED
        chattymoteV("%s tickles", "%s tickles %s", "tickle"); // UNDOCUMENTED
        chattymoteV("%s is waiting", "%s is waiting for %s", "wait");
        chattymoteV("%s waves", "%s waves to %s", "wave", "waves");
        command(
                literal("welcome")
                        .executes(context -> {
                            checkChattymote();
                            throw result(Text.literal(SelfPlayer.username() + ": Welcome to ").formatted(Formatting.LIGHT_PURPLE)
                                    .append(Text.literal("Cube").formatted(Formatting.DARK_AQUA))
                                    .append(Text.literal("Krowd").formatted(Formatting.GOLD))
                                    .append(Text.literal("!").formatted(Formatting.LIGHT_PURPLE)));
                        })
                        .then(argument("player", StringArgumentType.greedyString())
                                .executes(context -> {
                                    checkChattymote();
                                    String player = StringArgumentType.getString(context, "player");
                                    throw result(Text.literal(SelfPlayer.username() + ": Welcome to ").formatted(Formatting.LIGHT_PURPLE)
                                            .append(Text.literal("Cube").formatted(Formatting.DARK_AQUA))
                                            .append(Text.literal("Krowd").formatted(Formatting.GOLD))
                                            .append(Text.literal(" " + player + "!").formatted(Formatting.LIGHT_PURPLE)));
                                })),
                "wel"
        );
        chattymoteV("%s wins!", "%s wins %s", "win", "won");
    }

    /**
     * Get the message preview for the current chat text
     *
     * @param args current chat text, stripped of multi-spaces
     * @return the message preview, or {@code EmptyCache.EMPTY} if there is none
     */
    public static @NotNull TextCache preview(@NotNull String args) {
        if (args.startsWith("/")) {
            try {
                DISPATCHER.execute(args.substring(1), ModKrowd.USELESS);
                return TextCache.EMPTY;
            } catch (PreviewResult r) {
                return r.value;
            } catch (CommandSyntaxException e) {
                return TextCache.EMPTY;
            }
        } else {
            return args.isEmpty() ? TextCache.EMPTY : CKStuff.censor(ModKrowd.currentSubserver.formatChat(args));
        }
    }

    /**
     * Register a new preview command, which should have the possibility of throwing a {@code PreviewResult}
     *
     * @param previewer The syntax tree for the command
     * @param aliases   The list of aliases that will redirect to the default command
     */
    public static void command(LiteralArgumentBuilder<Object> previewer, String... aliases) {
        LiteralCommandNode<Object> node = DISPATCHER.register(previewer);
        for (String alias : aliases) {
            DISPATCHER.register(literal(alias).redirect(node));
        }
    }

    /**
     * Automatically register a chattymote preview with no arguments
     *
     * @param format    The message format as in {@link String#format}, where the sole argument is the player name
     * @param canonical The default command
     * @param aliases   The list of aliases that will redirect to the default command
     */
    public static void chattymote0(String format, String canonical, String... aliases) {
        command(
                literal(canonical)
                        .executes(context -> {
                            checkChattymote();
                            throw result(Text.literal(format.formatted(SelfPlayer.username())).formatted(Formatting.LIGHT_PURPLE));
                        }),
                aliases
        );
    }

    /**
     * Automatically register a chattymote preview with one argument
     *
     * @param format    The message format as in {@link String#format}, where the arguments are player name and a player-specified string
     * @param canonical The default command
     * @param aliases   The list of aliases that will redirect to the default command
     */
    public static void chattymote1(String format, String canonical, String... aliases) {
        command(
                literal(canonical)
                        .then(argument("arg1", StringArgumentType.greedyString())
                                .executes(context -> {
                                    checkChattymote();
                                    String arg1 = StringArgumentType.getString(context, "arg1");
                                    throw result(Text.literal(format.formatted(SelfPlayer.username(), arg1)).formatted(Formatting.LIGHT_PURPLE));
                                })),
                aliases
        );
    }

    /**
     * Automatically register a chattymote preview with two arguments
     *
     * @param format    The message format as in {@link String#format}, where the arguments are player name and two player-specified strings
     * @param canonical The default command
     * @param aliases   The list of aliases that will redirect to the default command
     */
    public static void chattymote2(String format, String canonical, String... aliases) {
        command(
                literal(canonical)
                        .then(argument("arg1", StringArgumentType.word())
                                .executes(context -> {
                                    checkChattymote();
                                    String arg1 = StringArgumentType.getString(context, "arg1");
                                    String arg2 = "2";
                                    throw result(Text.literal(format.formatted(SelfPlayer.username(), arg1, arg2)).formatted(Formatting.LIGHT_PURPLE));
                                })
                                .then(argument("arg2", StringArgumentType.word())
                                        .executes(context -> {
                                            checkChattymote();
                                            String arg1 = StringArgumentType.getString(context, "arg1");
                                            String arg2 = StringArgumentType.getString(context, "arg2");
                                            throw result(Text.literal(format.formatted(SelfPlayer.username(), arg1, arg2)).formatted(Formatting.LIGHT_PURPLE));
                                        }))),
                aliases
        );
    }

    /**
     * Automatically register a chattymote preview with variably zero or one arguments
     *
     * @param format0   The message format as in {@link String#format}, where the sole argument is the player name;
     *                  used when no argument is specified
     * @param format1   The message format as in {@link String#format}, where the arguments are player name and a player-specified string;
     *                  used when an argument is specified
     * @param canonical The default command
     * @param aliases   The list of aliases that will redirect to the default command
     */
    public static void chattymoteV(String format0, String format1, String canonical, String... aliases) {
        command(
                literal(canonical)
                        .executes(context -> {
                            checkChattymote();
                            throw result(Text.literal(format0.formatted(SelfPlayer.username())).formatted(Formatting.LIGHT_PURPLE));
                        })
                        .then(argument("arg1", StringArgumentType.greedyString())
                                .executes(context -> {
                                    checkChattymote();
                                    String arg1 = StringArgumentType.getString(context, "arg1");
                                    throw result(Text.literal(format1.formatted(SelfPlayer.username(), arg1)).formatted(Formatting.LIGHT_PURPLE));
                                })),
                aliases
        );
    }

    /**
     * Check if chattymotes are available, and if not, throw a preview result outlining the issue
     * @throws PreviewResult If chattymotes are not available
     */
    public static void checkChattymote() throws PreviewResult {
        if (!(ModKrowd.currentSubserver instanceof MainSubserver)) {
            throw result(Text.literal("Not available on this server").formatted(Formatting.RED));
        }
        if (SelfPlayer.rankNameSoft().rank().brackets() == RankBrackets.NONE) {
            throw result(Text.literal("Yellow brackets required").formatted(Formatting.RED));
        }
    }

    /**
     * Construct a result for the preview generator, which is to be thrown.
     * This workaround is used because a command can only return an {@code int}.
     * @param value The preview text, or {@code null} if there is no preview
     * @return The exception to be thrown
     */
    public static PreviewResult result(MutableText value) {
        return new PreviewResult(TextCache.of(value));
    }

    /**
     * Construct a result for the preview generator, which is to be thrown.
     * This workaround is used because a command can only return an {@code int}.
     * @param value The preview text, or {@code null} if there is no preview
     * @return The exception to be thrown
     */
    public static PreviewResult result(StyledString value) {
        return new PreviewResult(TextCache.of(value));
    }

    /**
     * Construct a result for the preview generator, which is to be thrown.
     * This workaround is used because a command can only return an {@code int}.
     * @param value The preview text, or {@code null} if there is no preview
     * @return The exception to be thrown
     */
    public static PreviewResult result(TextCache value) {
        return new PreviewResult(value);
    }
}
