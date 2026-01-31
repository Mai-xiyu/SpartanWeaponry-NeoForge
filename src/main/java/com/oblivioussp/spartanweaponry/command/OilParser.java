package com.oblivioussp.spartanweaponry.command;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.OilEffects;
import com.oblivioussp.spartanweaponry.api.oil.OilEffect;

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class OilParser 
{
	public static final DynamicCommandExceptionType ERROR_UNKNOWN_OIL_EFFECT = new DynamicCommandExceptionType((object) -> 
	{
		return Component.translatable("command." + ModSpartanWeaponry.ID + ".apply_oil.error.unknown_oil_effect", object);
	});
	
	public static final DynamicCommandExceptionType ERROR_INVALID_OIL_EFFECT = new DynamicCommandExceptionType((object) -> 
	{
		return Component.translatable("command." + ModSpartanWeaponry.ID + ".apply_oil.error.invalid_oil_effect", object);
	});
	
	private static final BiFunction<SuggestionsBuilder, Registry<OilEffect>, CompletableFuture<Suggestions>> SUGGEST_NOTHING = (builder, registry) -> builder.buildFuture();
	
	private final StringReader reader;
	@Nullable
	private OilEffect oilEffect;
	
	private BiFunction<SuggestionsBuilder, Registry<OilEffect>, CompletableFuture<Suggestions>> suggestionFunc;
	
	public OilParser(StringReader readerIn)
	{
		reader = readerIn;
	}

	@Nullable
	public OilEffect getEffect()
	{
		return oilEffect;
	}
	
	public void read() throws CommandSyntaxException
	{
		int idx = reader.getCursor();
		ResourceLocation loc = ResourceLocation.read(reader);
		Registry<OilEffect> registry = getOilRegistry();
		oilEffect = registry != null ? registry.get(loc) : null;
		if(oilEffect == null)
		{
			reader.setCursor(idx);
			throw ERROR_UNKNOWN_OIL_EFFECT.createWithContext(reader, loc.toString());
		}
		else if(oilEffect == OilEffects.NONE.get())
		{
			reader.setCursor(idx);
			throw ERROR_INVALID_OIL_EFFECT.createWithContext(reader, loc.toString());
		}
	}
	
	public OilParser parse() throws CommandSyntaxException
	{
		suggestionFunc = this::suggestOilEffect;
		read();
		suggestionFunc = SUGGEST_NOTHING;
		return this;
	}
	
	private CompletableFuture<Suggestions> suggestOilEffect(SuggestionsBuilder builderIn, Registry<OilEffect> oilRegistryIn)
	{
			if(oilRegistryIn == null)
				return builderIn.buildFuture();
			Set<ResourceLocation> suggestions = oilRegistryIn.keySet().stream()
					.filter((oil) -> !oil.equals(oilRegistryIn.getKey(OilEffects.NONE.get())) && !oil.equals(oilRegistryIn.getKey(OilEffects.POTION.get())))
					.collect(Collectors.toSet());
		return SharedSuggestionProvider.suggestResource(suggestions, builderIn);
	}

	private static Registry<OilEffect> getOilRegistry()
	{
		RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		return registryAccess.registry(OilEffects.REGISTRY_KEY).orElse(null);
	}
	
	public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builderIn, Registry<OilEffect> oilRegistryIn)
	{
		return suggestionFunc.apply(builderIn.createOffset(reader.getCursor()), oilRegistryIn);
	}
}
