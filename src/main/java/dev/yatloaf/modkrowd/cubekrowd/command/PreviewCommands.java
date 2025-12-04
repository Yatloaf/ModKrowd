package dev.yatloaf.modkrowd.cubekrowd.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.CubeKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.RankBrackets;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Direction;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class PreviewCommands {
    private static final Random RANDOM = new Random();
    private static String currentMeow;

    private static final CommandDispatcher<Object> DISPATCHER = new CommandDispatcher<>();

    static {
        advanceRandom();

        command(
                literal("message")
                        .then(argument("target", StringArgumentType.word())
                                .then(argument("message", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String target = StringArgumentType.getString(context, "target");
                                            String message = StringArgumentType.getString(context, "message");

                                            // &r also resets the gold color
                                            StyledString formatted = SelfPlayer.tryFormat(message, CKColor.GOLD.style);

                                            if (SelfPlayer.username().equals(target)) {
                                                throw result(new DirectMessage(
                                                        Direction.LOOP,
                                                        DirectMessage.ME,
                                                        DirectMessage.ME,
                                                        formatted,
                                                        true
                                                ).appearance());
                                            } else {
                                                throw result(new DirectMessage(
                                                        Direction.OUTGOING,
                                                        DirectMessage.ME,
                                                        target,
                                                        formatted,
                                                        true
                                                ).appearance());
                                            }
                                        }))),
                "msg", "m"
        );
        command(
                literal("reply")
                        .then(argument("message", StringArgumentType.greedyString())
                                .executes(context -> {
                                    if (ModKrowd.CONFIG.MESSAGE_PREVIEW.replyTarget == null) {
                                        throw result(Component.literal("No reply target").withStyle(ChatFormatting.RED));
                                    }
                                    String target = ModKrowd.CONFIG.MESSAGE_PREVIEW.replyTarget;
                                    String message = StringArgumentType.getString(context, "message");

                                    // &r also resets the gold color
                                    StyledString formatted = SelfPlayer.tryFormat(message, CKColor.GOLD.style);

                                    if (SelfPlayer.username().equals(target)) {
                                        throw result(new DirectMessage(
                                                Direction.LOOP,
                                                DirectMessage.ME,
                                                DirectMessage.ME,
                                                formatted,
                                                true
                                        ).appearance());
                                    } else {
                                        throw result(new DirectMessage(
                                                Direction.OUTGOING,
                                                DirectMessage.ME,
                                                target,
                                                formatted,
                                                true
                                        ).appearance());
                                    }
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
                    throw result(Component.literal(SelfPlayer.username() + " flips a coin. It shows ").withStyle(ChatFormatting.LIGHT_PURPLE)
                            .append(Component.literal("#####").withStyle(ChatFormatting.OBFUSCATED)));
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
                            throw result(Component.literal(SelfPlayer.username() + " throws a 6 sided dice. It shows ").withStyle(ChatFormatting.LIGHT_PURPLE)
                                    .append(Component.literal("0").withStyle(ChatFormatting.OBFUSCATED)));
                        })
                        .then(argument("sides", StringArgumentType.word())
                                .executes(context -> {
                                    checkChattymote();
                                    String sides = Util.normalizeIntOr(StringArgumentType.getString(context, "sides"), "NaN");
                                    throw result(Component.literal(SelfPlayer.username() + " throws a " + sides + " sided dice. It shows ").withStyle(ChatFormatting.LIGHT_PURPLE)
                                            .append(Component.literal("0").withStyle(ChatFormatting.OBFUSCATED)));
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
        command(
                literal("meow").executes(context -> {
                    checkChattymote();
                    throw result(Component.literal(SelfPlayer.username() + currentMeow).withStyle(ChatFormatting.LIGHT_PURPLE));
                })
        );
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
                            throw result(Component.literal(SelfPlayer.username() + ": Welcome to ").withStyle(ChatFormatting.LIGHT_PURPLE)
                                    .append(Component.literal("Cube").withStyle(ChatFormatting.DARK_AQUA))
                                    .append(Component.literal("Krowd").withStyle(ChatFormatting.GOLD))
                                    .append(Component.literal("!").withStyle(ChatFormatting.LIGHT_PURPLE)));
                        })
                        .then(argument("player", StringArgumentType.greedyString())
                                .executes(context -> {
                                    checkChattymote();
                                    String player = StringArgumentType.getString(context, "player");
                                    throw result(Component.literal(SelfPlayer.username() + ": Welcome to ").withStyle(ChatFormatting.LIGHT_PURPLE)
                                            .append(Component.literal("Cube").withStyle(ChatFormatting.DARK_AQUA))
                                            .append(Component.literal("Krowd").withStyle(ChatFormatting.GOLD))
                                            .append(Component.literal(" " + player + "!").withStyle(ChatFormatting.LIGHT_PURPLE)));
                                })),
                "wel"
        );
        chattymoteV("%s wins!", "%s wins %s", "win", "won");
    }

    /**
     * Get the message preview for the current chat text
     *
     * @param args current chat text, stripped of multi-spaces
     * @return the message preview, or {@link TextCache#EMPTY} if there is none
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
            if (args.isEmpty()) return TextCache.EMPTY;

            TextCache chat = ModKrowd.currentSubserver.formatChat(args);
            if (chat == TextCache.EMPTY) return TextCache.EMPTY;
            return CubeKrowd.censor(chat);
        }
    }

    /**
     * Advance the random number generator to get a new result for the next set of previews
     */
    public static void advanceRandom() {
        // As confirmed by zorua162: 1% meow, 9% furball, 90% cat sounds

        int meowIndex = RANDOM.nextInt(100);
        if (meowIndex == 0) {
            currentMeow = " meows";
        } else if (meowIndex < 10) {
            currentMeow = " tried to meow, but coughs up a furball";
        } else {
            currentMeow = " makes cat sounds";
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
            // See <https://github.com/Mojang/brigadier/issues/46>
            // This is worked around by manually copying the destination node, as in
            // <https://github.com/PaperMC/Velocity/blob/8abc9c80a69158ebae0121fda78b55c865c0abad/proxy/src/main/java/com/velocitypowered/proxy/util/BrigadierUtils.java#L38>

            LiteralArgumentBuilder<Object> builder = literal(alias)
                    .requires(node.getRequirement())
                    .forward(node.getRedirect(), node.getRedirectModifier(), node.isFork())
                    .executes(node.getCommand());
            for (CommandNode<Object> child : node.getChildren()) {
                builder.then(child);
            }
            DISPATCHER.register(builder);
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
                            throw result(Component.literal(format.formatted(SelfPlayer.username())).withStyle(ChatFormatting.LIGHT_PURPLE));
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
                                    throw result(Component.literal(format.formatted(SelfPlayer.username(), arg1)).withStyle(ChatFormatting.LIGHT_PURPLE));
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
                                    throw result(Component.literal(format.formatted(SelfPlayer.username(), arg1, arg2)).withStyle(ChatFormatting.LIGHT_PURPLE));
                                })
                                .then(argument("arg2", StringArgumentType.word())
                                        .executes(context -> {
                                            checkChattymote();
                                            String arg1 = StringArgumentType.getString(context, "arg1");
                                            String arg2 = StringArgumentType.getString(context, "arg2");
                                            throw result(Component.literal(format.formatted(SelfPlayer.username(), arg1, arg2)).withStyle(ChatFormatting.LIGHT_PURPLE));
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
                            throw result(Component.literal(format0.formatted(SelfPlayer.username())).withStyle(ChatFormatting.LIGHT_PURPLE));
                        })
                        .then(argument("arg1", StringArgumentType.greedyString())
                                .executes(context -> {
                                    checkChattymote();
                                    String arg1 = StringArgumentType.getString(context, "arg1");
                                    throw result(Component.literal(format1.formatted(SelfPlayer.username(), arg1)).withStyle(ChatFormatting.LIGHT_PURPLE));
                                })),
                aliases
        );
    }

    /**
     * Check if chattymotes are available, and if not, throw a preview result outlining the issue
     * @throws PreviewResult If chattymotes are not available
     */
    public static void checkChattymote() throws PreviewResult {
        if (!ModKrowd.currentSubserver.hasChattymotes) {
            throw result(Component.literal("Not available on this server").withStyle(ChatFormatting.RED));
        }
        if (SelfPlayer.rankNameSoft().rank().brackets() == RankBrackets.NONE) {
            throw result(Component.literal("Yellow brackets required").withStyle(ChatFormatting.RED));
        }
    }

    /**
     * Construct a result for the preview generator, which is to be thrown.
     * This workaround is used because a command can only return an {@code int}.
     * @param value The preview text, or {@code null} if there is no preview
     * @return The exception to be thrown
     */
    public static PreviewResult result(MutableComponent value) {
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
