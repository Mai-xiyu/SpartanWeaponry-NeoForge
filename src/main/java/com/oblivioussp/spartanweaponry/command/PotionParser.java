package com.oblivioussp.spartanweaponry.command;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;

import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;

public class PotionParser 
{
	private static final List<ResourceLocation> invalidPotionNames = Stream.of("water", "mundane", "thick", "awkward")
			.map(ResourceLocation::withDefaultNamespace)
			.collect(Collectors.toUnmodifiableList());
	
	public static final DynamicCommandExceptionType ERROR_UNKNOWN_POTION = new DynamicCommandExceptionType((object) -> 
	{
		return Component.translatable("command." + ModSpartanWeaponry.ID + ".apply_oil.error.unknown_potion", object);
	});
	
	public static final DynamicCommandExceptionType ERROR_INVALID_POTION = new DynamicCommandExceptionType((object) -> 
	{
		return Component.translatable("command." + ModSpartanWeaponry.ID + ".apply_oil.error.invalid_potion", object);
	});
	
	private static final BiFunction<SuggestionsBuilder, Registry<Potion>, CompletableFuture<Suggestions>> SUGGEST_NOTHING = (builder, registry) -> builder.buildFuture();
	
	private final StringReader reader;
	@Nullable
	private Potion potion;
	
	private BiFunction<SuggestionsBuilder, Registry<Potion>, CompletableFuture<Suggestions>> suggestionFunc;
	
	public PotionParser(StringReader readerIn)
	{
		reader = readerIn;
	}

	@Nullable
	public Potion getEffect()
	{
		return potion;
	}
	
	public void read() throws CommandSyntaxException
	{
		int idx = reader.getCursor();
		ResourceLocation loc = ResourceLocation.read(reader);
		potion = BuiltInRegistries.POTION.get(loc);
		
		if(potion == null)
		{
			reader.setCursor(idx);
			throw ERROR_UNKNOWN_POTION.createWithContext(reader, loc.toString());
		}
		else if(invalidPotionNames.contains(BuiltInRegistries.POTION.getKey(potion)))
		{
			reader.setCursor(idx);
			throw ERROR_INVALID_POTION.createWithContext(reader, loc.toString());
		}
	}
	
	public PotionParser parse() throws CommandSyntaxException
	{
		suggestionFunc = this::suggestPotionEffect;
		read();
		suggestionFunc = SUGGEST_NOTHING;
		return this;
	}
	
	private CompletableFuture<Suggestions> suggestPotionEffect(SuggestionsBuilder builderIn, Registry<Potion> potionRegistryIn)
	{
			Set<ResourceLocation> suggestions = potionRegistryIn.keySet().stream().filter((potion) -> !invalidPotionNames.contains(potion)).collect(Collectors.toSet());
		return SharedSuggestionProvider.suggestResource(suggestions, builderIn);
	}
	
	public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builderIn, Registry<Potion> oilRegistryIn)
	{
		return suggestionFunc.apply(builderIn.createOffset(reader.getCursor()), oilRegistryIn);
	}
}
