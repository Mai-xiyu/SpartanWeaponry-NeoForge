package org.xiyu.spartanweaponryunofficial.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;

public class OilParser {
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_OIL_EFFECT =
            new DynamicCommandExceptionType(
                    (object) ->
                            Component.translatable(
                                    "command."
                                            + ModSpartanWeaponry.ID
                                            + ".apply_oil.error.unknown_oil_effect",
                                    object));

    public static final DynamicCommandExceptionType ERROR_INVALID_OIL_EFFECT =
            new DynamicCommandExceptionType(
                    (object) ->
                            Component.translatable(
                                    "command."
                                            + ModSpartanWeaponry.ID
                                            + ".apply_oil.error.invalid_oil_effect",
                                    object));

    private static final BiFunction<
                    SuggestionsBuilder, Registry<OilEffect>, CompletableFuture<Suggestions>>
            SUGGEST_NOTHING = (builder, registry) -> builder.buildFuture();

    private final StringReader reader;
    @Nullable private OilEffect oilEffect;

    private BiFunction<SuggestionsBuilder, Registry<OilEffect>, CompletableFuture<Suggestions>>
            suggestionFunc;

    public OilParser(StringReader readerIn) {
        this.reader = readerIn;
    }

    @Nullable public OilEffect getEffect() {
        return this.oilEffect;
    }

    public void read() throws CommandSyntaxException {
        int idx = this.reader.getCursor();
        ResourceLocation loc = ResourceLocation.read(this.reader);
        Registry<OilEffect> registry = getOilRegistry();
        this.oilEffect = registry != null ? registry.get(loc) : null;
        if (this.oilEffect == null) {
            this.reader.setCursor(idx);
            throw ERROR_UNKNOWN_OIL_EFFECT.createWithContext(this.reader, loc.toString());
        } else if (this.oilEffect == OilEffects.NONE.get()) {
            this.reader.setCursor(idx);
            throw ERROR_INVALID_OIL_EFFECT.createWithContext(this.reader, loc.toString());
        }
    }

    public OilParser parse() throws CommandSyntaxException {
        this.suggestionFunc = this::suggestOilEffect;
        this.read();
        this.suggestionFunc = SUGGEST_NOTHING;
        return this;
    }

    private CompletableFuture<Suggestions> suggestOilEffect(
            SuggestionsBuilder builderIn, Registry<OilEffect> oilRegistryIn) {
        if (oilRegistryIn == null) return builderIn.buildFuture();
        Set<ResourceLocation> suggestions =
                oilRegistryIn.keySet().stream()
                        .filter(
                                (oil) ->
                                        !oil.equals(oilRegistryIn.getKey(OilEffects.NONE.get()))
                                                && !oil.equals(
                                                        oilRegistryIn.getKey(
                                                                OilEffects.POTION.get())))
                        .collect(Collectors.toSet());
        return SharedSuggestionProvider.suggestResource(suggestions, builderIn);
    }

    private static Registry<OilEffect> getOilRegistry() {
        return OilEffects.registry();
    }

    public CompletableFuture<Suggestions> fillSuggestions(
            SuggestionsBuilder builderIn, Registry<OilEffect> oilRegistryIn) {
        return this.suggestionFunc.apply(
                builderIn.createOffset(this.reader.getCursor()), oilRegistryIn);
    }
}
