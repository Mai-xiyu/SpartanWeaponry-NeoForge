package org.xiyu.spartanweaponryunofficial.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.alchemy.Potion;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PotionParser {
    private static final List<Identifier> invalidPotionNames = Stream.of("water", "mundane", "thick", "awkward")
            .map(Identifier::withDefaultNamespace)
            .toList();

    public static final DynamicCommandExceptionType ERROR_UNKNOWN_POTION = new DynamicCommandExceptionType((object) ->
            Component.translatable("command." + ModSpartanWeaponry.ID + ".apply_oil.error.unknown_potion", object));

    public static final DynamicCommandExceptionType ERROR_INVALID_POTION = new DynamicCommandExceptionType((object) ->
            Component.translatable("command." + ModSpartanWeaponry.ID + ".apply_oil.error.invalid_potion", object));

    private static final BiFunction<SuggestionsBuilder, Registry<Potion>, CompletableFuture<Suggestions>> SUGGEST_NOTHING = (builder, registry) -> builder.buildFuture();

    private final StringReader reader;
    @Nullable
    private Potion potion;

    private BiFunction<SuggestionsBuilder, Registry<Potion>, CompletableFuture<Suggestions>> suggestionFunc;

    public PotionParser(StringReader readerIn) {
        this.reader = readerIn;
    }

    @Nullable
    public Potion getEffect() {
        return this.potion;
    }

    public void read() throws CommandSyntaxException {
        int idx = this.reader.getCursor();
        Identifier loc = Identifier.read(this.reader);
        this.potion = BuiltInRegistries.POTION.getValue(loc);

        if (this.potion == null) {
            this.reader.setCursor(idx);
            throw ERROR_UNKNOWN_POTION.createWithContext(this.reader, loc.toString());
        } else if (invalidPotionNames.contains(BuiltInRegistries.POTION.getKey(this.potion))) {
            this.reader.setCursor(idx);
            throw ERROR_INVALID_POTION.createWithContext(this.reader, loc.toString());
        }
    }

    public PotionParser parse() throws CommandSyntaxException {
        this.suggestionFunc = this::suggestPotionEffect;
        this.read();
        this.suggestionFunc = SUGGEST_NOTHING;
        return this;
    }

    private CompletableFuture<Suggestions> suggestPotionEffect(SuggestionsBuilder builderIn, Registry<Potion> potionRegistryIn) {
        Set<Identifier> suggestions = potionRegistryIn.keySet().stream().filter((potion) -> !invalidPotionNames.contains(potion)).collect(Collectors.toSet());
        return SharedSuggestionProvider.suggestResource(suggestions, builderIn);
    }

    public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builderIn, Registry<Potion> oilRegistryIn) {
        return this.suggestionFunc.apply(builderIn.createOffset(this.reader.getCursor()), oilRegistryIn);
    }
}
