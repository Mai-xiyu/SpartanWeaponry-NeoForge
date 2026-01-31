package com.oblivioussp.spartanweaponry.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.oblivioussp.spartanweaponry.api.OilEffects;
import com.oblivioussp.spartanweaponry.api.oil.OilEffect;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;

public class OilArgument implements ArgumentType<OilInput> 
{
	private static final Collection<String> EXAMPLES = Arrays.asList("spartanweaponry:undead");
	
	public static OilArgument oil()
	{
		return new OilArgument();
	}

	@Override
	public OilInput parse(StringReader reader) throws CommandSyntaxException 
	{
		OilParser parser = new OilParser(reader).parse();
		return new OilInput(parser.getEffect());
	}
	
	public static <S> OilInput getOil(CommandContext<S> context, String string)
	{
		return context.getArgument(string, OilInput.class);
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) 
	{
		StringReader reader = new StringReader(builder.getInput());
		reader.setCursor(builder.getStart());
		OilParser parser = new OilParser(reader);
		
		try
		{
			parser.parse();
		}
		catch(CommandSyntaxException e) {}
		
		RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		Registry<OilEffect> registry = registryAccess.registry(OilEffects.REGISTRY_KEY).orElse(null);
		return parser.fillSuggestions(builder, registry);
	}

	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
}
