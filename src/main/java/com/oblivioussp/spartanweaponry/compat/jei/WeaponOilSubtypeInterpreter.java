package com.oblivioussp.spartanweaponry.compat.jei;

import java.util.List;

import com.oblivioussp.spartanweaponry.api.OilEffects;
import com.oblivioussp.spartanweaponry.api.oil.OilEffect;
import com.oblivioussp.spartanweaponry.util.OilHelper;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import com.oblivioussp.spartanweaponry.util.ItemStackDataHelper;

public class WeaponOilSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack>
{
	public static final WeaponOilSubtypeInterpreter INSTANCE = new WeaponOilSubtypeInterpreter();
	
	private WeaponOilSubtypeInterpreter() {}

	@Override
	public String apply(ItemStack itemStack, UidContext context)
	{
		if (!ItemStackDataHelper.hasTag(itemStack))
			return null;
		
		OilEffect weaponOil = OilHelper.getOilFromStack(itemStack);
		Potion potion = OilHelper.getPotionFromStack(itemStack);
		
		Registry<OilEffect> registry = (Registry<OilEffect>)BuiltInRegistries.REGISTRY.get(OilEffects.REGISTRY_KEY.location());
		String result = registry.getKey(weaponOil).getPath();
		if(weaponOil == OilEffects.POTION.get())
		{
			StringBuilder stringBuilder = new StringBuilder(result);
			if(potion != null)
			{
				stringBuilder.append(":").append(BuiltInRegistries.POTION.getKey(potion).getPath());
				for(MobEffectInstance mobEffect : potion.getEffects())
				{
					stringBuilder.append(";").append(mobEffect);
				}
			}
			result = stringBuilder.toString();
		}
		return result;
	}

}
