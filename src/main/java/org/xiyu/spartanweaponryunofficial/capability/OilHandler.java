package org.xiyu.spartanweaponryunofficial.capability;

import java.util.Optional;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;

public class OilHandler implements IOilHandler
{
	public static final String NBT_OIL = "Oil";
	public static final String NBT_OIL_EFFECT = "Effect";
	public static final String NBT_POTION = "Potion";
	public static final String NBT_USES_LEFT = "UsesLeft";
	
	private final ItemStack stack;
	private Optional<OilEffect> effect;
	private Optional<Potion> potion;
	private int usesLeft;
	
	public OilHandler(ItemStack stackIn)
	{
		stack = stackIn;
		effect = Optional.empty();
		potion = Optional.empty();
		usesLeft = 0;
		// Load the values contained in the NBT if available
		if(ItemStackDataHelper.getTag(stack).contains(NBT_OIL))
			deserializeNBT(getRegistryAccess(), ItemStackDataHelper.getTag(stack).getCompound(NBT_OIL));
	}

	@Override
	public Optional<OilEffect> getEffect() 
	{
		return effect;
	}
	
	@Override
	public Optional<Potion> getPotion() 
	{
		return potion;
	}

	@Override
	public void setEffect(OilEffect effectIn, ItemStack oilStackIn) 
	{
		effect = Optional.of(effectIn);
		usesLeft = effectIn.getMaxUses();
		potion = effectIn == OilEffects.POTION.get() ? Optional.ofNullable(OilHelper.getPotionFromStack(oilStackIn)) : Optional.empty();
		ItemStackDataHelper.updateTag(stack, tag -> tag.put(NBT_OIL, serializeNBT(getRegistryAccess())));
	}

	@Override
	public void setPotion(Potion potionIn, ItemStack oilStackIn) 
	{
		effect = Optional.of(OilEffects.POTION.get());
		usesLeft = OilEffects.POTION.get().getMaxUses();
		potion = Optional.of(potionIn);
		ItemStackDataHelper.updateTag(stack, tag -> tag.put(NBT_OIL, serializeNBT(getRegistryAccess())));
	}
	
	@Override
	public void clearEffect() 
	{
		effect = Optional.empty();
		usesLeft = 0;
		ItemStackDataHelper.updateTag(stack, tag -> tag.remove(NBT_OIL));
	}
	
	@Override
	public float useEffect(float baseDamageIn, Level levelIn, LivingEntity targetIn, LivingEntity userIn, ItemStack userWeaponIn) 
	{
		OilEffect oilEffect = effect.get();
		ItemStack oilStack = oilEffect == OilEffects.POTION.get() ? OilHelper.makePotionOilStack(potion.get()) : OilHelper.makeOilStack(oilEffect);
		float resultDamage = oilEffect.onUse(baseDamageIn, levelIn, targetIn, userIn, oilStack);
		
		usesLeft--;
		ItemStackDataHelper.updateTag(stack, tag -> tag.put(NBT_OIL, serializeNBT(getRegistryAccess())));
		
		if(usesLeft <= 0)
		{
			if(userIn instanceof Player)
				((Player)userIn).displayClientMessage(Component.translatable("message." + ModSpartanWeaponry.ID + ".oil_depleted", oilStack.getHoverName(), userWeaponIn.getHoverName()), true);
			clearEffect();
		}
		return resultDamage;
	}

	@Override
	public boolean isOiled() 
	{
		return effect.isPresent() && effect.get() != OilEffects.NONE.get();
	}

	@Override
	public int getUsesLeft()
	{
		return usesLeft;
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) 
	{
		CompoundTag nbt = new CompoundTag();
		Registry<OilEffect> registry = getOilRegistry();
		if(registry != null && effect.isPresent())
		{
			if(potion.isPresent())
			{
				ResourceLocation potionLoc = BuiltInRegistries.POTION.getKey(potion.get());
				nbt.putString(NBT_POTION, potionLoc.toString());
			}
			ResourceLocation loc = registry.getKey(effect.get());
			nbt.putString(NBT_OIL_EFFECT, loc.toString());
			nbt.putInt(NBT_USES_LEFT, usesLeft);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) 
	{
		String oilEffectReg = nbt.getString(NBT_OIL_EFFECT);
		Registry<OilEffect> registry = getOilRegistry();
		if(registry != null)
		{
			effect = !oilEffectReg.isEmpty() ? Optional.ofNullable(registry.get(ResourceLocation.parse(oilEffectReg))) : Optional.empty();
			if(nbt.contains(NBT_POTION))
			{
				String potionReg = nbt.getString(NBT_POTION);
				potion = !potionReg.isEmpty() ? Optional.ofNullable(BuiltInRegistries.POTION.get(ResourceLocation.parse(potionReg))) : Optional.empty();
			}
		}
		usesLeft = nbt.getInt(NBT_USES_LEFT);
	}

	@SuppressWarnings("unchecked")
	private static Registry<OilEffect> getOilRegistry()
	{
		return (Registry<OilEffect>)BuiltInRegistries.REGISTRY.get(OilEffects.REGISTRY_KEY.location());
	}

	private static HolderLookup.Provider getRegistryAccess()
	{
		return net.minecraft.core.RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
	}
}
