package org.xiyu.spartanweaponryunofficial.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PotionArgument implements ArgumentType<PotionInput> {
    private static final Collection<String> EXAMPLES = List.of("spartanweaponryunofficial:undead");

    public static PotionArgument potion() {
        return new PotionArgument();
    }

    @Override
    public PotionInput parse(StringReader reader) throws CommandSyntaxException {
        PotionParser parser = new PotionParser(reader).parse();
        return new PotionInput(parser.getEffect());
    }

    public static <S> PotionInput getPotion(CommandContext<S> context, String string) {
        return context.getArgument(string, PotionInput.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        PotionParser parser = new PotionParser(reader);

        try {
            parser.parse();
        } catch (CommandSyntaxException ignored) {
        }

        return parser.fillSuggestions(builder, BuiltInRegistries.POTION);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
