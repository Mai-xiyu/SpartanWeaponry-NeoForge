package org.xiyu.spartanweaponryunofficial.compat.jei;

import java.util.List;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public class TippedProjectileSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> 
{
	public static final TippedProjectileSubtypeInterpreter INSTANCE = new TippedProjectileSubtypeInterpreter();

	private TippedProjectileSubtypeInterpreter() {}

	@Override
	public String apply(ItemStack itemStack, UidContext context) 
	{
		if (!itemStack.has(DataComponents.POTION_CONTENTS))
			return null;

		Optional<Holder<Potion>> potionHolder = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
				.potion();
		if(potionHolder.isEmpty())
			return null;
		Potion potionType = potionHolder.get().value();
		String potionTypeString = Potion.getName(potionHolder, "");
		StringBuilder stringBuilder = new StringBuilder(potionTypeString);
		for (MobEffectInstance effect : potionType.getEffects())
		{
			stringBuilder.append(";").append(effect);
		}

		return stringBuilder.toString();
	}
}
