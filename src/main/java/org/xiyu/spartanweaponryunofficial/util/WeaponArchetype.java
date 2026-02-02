package org.xiyu.spartanweaponryunofficial.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.xiyu.spartanweaponryunofficial.api.IReloadable;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.tags.ModWeaponTraitTags;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * This class contains all the data that are constant for every weapon of a certain type (e.g. Dagger, Longsword, etc.)<br>
 * This should reduce redundant values on each weapon item.<br>
 * Currently only filters and pre-caches traits to be used for Weapon items to improve performance
 * in addition to updating each weapon archetype's config values
 * @author ObliviousSpartan
 */
public class WeaponArchetype implements IReloadable
{
	public static final WeaponArchetype DAGGER = new WeaponArchetype("Dagger", true, ModWeaponTraitTags.DAGGER, WeaponType.MELEE, 
			() -> Config.INSTANCE.daggers.speed.get(), () -> Config.INSTANCE.daggers.baseDamage.get().floatValue(), () -> Config.INSTANCE.daggers.damageMultipler.get().floatValue());
	public static final WeaponArchetype PARRYING_DAGGER = new WeaponArchetype("Parrying Dagger", true, ModWeaponTraitTags.PARRYING_DAGGER, WeaponType.MELEE, 
			() -> Config.INSTANCE.parryingDaggers.speed.get(), () -> Config.INSTANCE.parryingDaggers.baseDamage.get().floatValue(), () -> Config.INSTANCE.parryingDaggers.damageMultipler.get().floatValue());
	public static final WeaponArchetype LONGSWORD = new WeaponArchetype("Longsword", true, ModWeaponTraitTags.LONGSWORD, WeaponType.MELEE, 
			() -> Config.INSTANCE.longswords.speed.get(), () -> Config.INSTANCE.longswords.baseDamage.get().floatValue(), () -> Config.INSTANCE.longswords.damageMultipler.get().floatValue(), ItemAbilities.SWORD_DIG);
	public static final WeaponArchetype KATANA = new WeaponArchetype("Katana", true, ModWeaponTraitTags.KATANA, WeaponType.MELEE, 
			() -> Config.INSTANCE.katanas.speed.get(), () -> Config.INSTANCE.katanas.baseDamage.get().floatValue(), () -> Config.INSTANCE.katanas.damageMultipler.get().floatValue(), ItemAbilities.SWORD_DIG);
	public static final WeaponArchetype SABER = new WeaponArchetype("Saber", true, ModWeaponTraitTags.SABER, WeaponType.MELEE, 
			() -> Config.INSTANCE.sabers.speed.get(), () -> Config.INSTANCE.sabers.baseDamage.get().floatValue(), () -> Config.INSTANCE.sabers.damageMultipler.get().floatValue(), ItemAbilities.SWORD_DIG);
	public static final WeaponArchetype RAPIER = new WeaponArchetype("Rapier", true, ModWeaponTraitTags.RAPIER, WeaponType.MELEE, 
			() -> Config.INSTANCE.rapiers.speed.get(), () -> Config.INSTANCE.rapiers.baseDamage.get().floatValue(), () -> Config.INSTANCE.rapiers.damageMultipler.get().floatValue());
	public static final WeaponArchetype GREATSWORD = new WeaponArchetype("Greatsword", true, ModWeaponTraitTags.GREATSWORD, WeaponType.MELEE, 
			() -> Config.INSTANCE.greatswords.speed.get(), () -> Config.INSTANCE.greatswords.baseDamage.get().floatValue(), () -> Config.INSTANCE.greatswords.damageMultipler.get().floatValue(), ItemAbilities.SWORD_DIG);
	public static final WeaponArchetype CLUB = new WeaponArchetype("Club", false, ModWeaponTraitTags.CLUB, WeaponType.MELEE, 
			() -> Config.INSTANCE.clubs.speed.get(), () -> Config.INSTANCE.clubs.baseDamage.get().floatValue(), () -> Config.INSTANCE.clubs.damageMultipler.get().floatValue());
	public static final WeaponArchetype CESTUS = new WeaponArchetype("Cestus", false, ModWeaponTraitTags.CESTUS, WeaponType.MELEE, 
			() -> Config.INSTANCE.cestus.speed.get(), () -> Config.INSTANCE.cestus.baseDamage.get().floatValue(), () -> Config.INSTANCE.cestus.damageMultipler.get().floatValue());
	public static final WeaponArchetype BATTLE_HAMMER = new WeaponArchetype("Battle Hammer", false, ModWeaponTraitTags.BATTLE_HAMMER, WeaponType.MELEE, 
			() -> Config.INSTANCE.battleHammers.speed.get(), () -> Config.INSTANCE.battleHammers.baseDamage.get().floatValue(), () -> Config.INSTANCE.battleHammers.damageMultipler.get().floatValue());
	public static final WeaponArchetype WARHAMMER = new WeaponArchetype("Warhammer", false, ModWeaponTraitTags.WARHAMMER, WeaponType.MELEE, 
			() -> Config.INSTANCE.warhammers.speed.get(), () -> Config.INSTANCE.warhammers.baseDamage.get().floatValue(), () -> Config.INSTANCE.warhammers.damageMultipler.get().floatValue());
	public static final WeaponArchetype SPEAR = new WeaponArchetype("Spear", false, ModWeaponTraitTags.SPEAR, WeaponType.MELEE, 
			() -> Config.INSTANCE.spears.speed.get(), () -> Config.INSTANCE.spears.baseDamage.get().floatValue(), () -> Config.INSTANCE.spears.damageMultipler.get().floatValue());
	public static final WeaponArchetype HALBERD = new WeaponArchetype("Halberd", false, ModWeaponTraitTags.HALBERD, WeaponType.MELEE, 
			() -> Config.INSTANCE.halberds.speed.get(), () -> Config.INSTANCE.halberds.baseDamage.get().floatValue(), () -> Config.INSTANCE.halberds.damageMultipler.get().floatValue());
	public static final WeaponArchetype PIKE = new WeaponArchetype("Pike", false, ModWeaponTraitTags.PIKE, WeaponType.MELEE, 
			() -> Config.INSTANCE.pikes.speed.get(), () -> Config.INSTANCE.pikes.baseDamage.get().floatValue(), () -> Config.INSTANCE.pikes.damageMultipler.get().floatValue());
	public static final WeaponArchetype LANCE = new WeaponArchetype("Lance", false, ModWeaponTraitTags.LANCE, WeaponType.MELEE, 
			() -> Config.INSTANCE.lances.speed.get(), () -> Config.INSTANCE.lances.baseDamage.get().floatValue(), () -> Config.INSTANCE.lances.damageMultipler.get().floatValue());
	public static final WeaponArchetype THROWING_KNIFE = new WeaponArchetype("Throwing Knife", true, ModWeaponTraitTags.THROWING_KNIFE, WeaponType.THROWING, 
			() -> Config.INSTANCE.throwingKnives.speed.get(), () -> Config.INSTANCE.throwingKnives.baseDamage.get().floatValue(), () -> Config.INSTANCE.throwingKnives.damageMultipler.get().floatValue(), () -> Config.INSTANCE.throwingKnives.chargeTicks.get());
	public static final WeaponArchetype TOMAHAWK = new WeaponArchetype("Tomahawk", false, ModWeaponTraitTags.TOMAHAWK, WeaponType.THROWING, 
			() -> Config.INSTANCE.tomahawks.speed.get(), () -> Config.INSTANCE.tomahawks.baseDamage.get().floatValue(), () -> Config.INSTANCE.tomahawks.damageMultipler.get().floatValue(), () -> Config.INSTANCE.tomahawks.chargeTicks.get());
	public static final WeaponArchetype JAVELIN = new WeaponArchetype("Javelin", false, ModWeaponTraitTags.JAVELIN, WeaponType.THROWING, 
			() -> Config.INSTANCE.javelins.speed.get(), () -> Config.INSTANCE.javelins.baseDamage.get().floatValue(), () -> Config.INSTANCE.javelins.damageMultipler.get().floatValue(), () -> Config.INSTANCE.javelins.chargeTicks.get());
	public static final WeaponArchetype BOOMERANG = new WeaponArchetype("Boomerang", false, ModWeaponTraitTags.BOOMERANG, WeaponType.THROWING, 
			() -> Config.INSTANCE.boomerangs.speed.get(), () -> Config.INSTANCE.boomerangs.baseDamage.get().floatValue(), () -> Config.INSTANCE.boomerangs.damageMultipler.get().floatValue(), () -> Config.INSTANCE.boomerangs.chargeTicks.get());
	public static final WeaponArchetype BATTLEAXE = new WeaponArchetype("Battleaxe", false, ModWeaponTraitTags.BATTLEAXE, WeaponType.MELEE, 
			() -> Config.INSTANCE.battleaxes.speed.get(), () -> Config.INSTANCE.battleaxes.baseDamage.get().floatValue(), () -> Config.INSTANCE.battleaxes.damageMultipler.get().floatValue(), ItemAbilities.DEFAULT_AXE_ACTIONS);
	public static final WeaponArchetype FLANGED_MACE = new WeaponArchetype("Flanged Mace", false, ModWeaponTraitTags.FLANGED_MACE, WeaponType.MELEE, 
			() -> Config.INSTANCE.flangedMaces.speed.get(), () -> Config.INSTANCE.flangedMaces.baseDamage.get().floatValue(), () -> Config.INSTANCE.flangedMaces.damageMultipler.get().floatValue());
	public static final WeaponArchetype GLAIVE = new WeaponArchetype("Glaive", true, ModWeaponTraitTags.GLAIVE, WeaponType.MELEE, 
			() -> Config.INSTANCE.glaives.speed.get(), () -> Config.INSTANCE.glaives.baseDamage.get().floatValue(), () -> Config.INSTANCE.glaives.damageMultipler.get().floatValue());
	public static final WeaponArchetype QUARTERSTAFF = new WeaponArchetype("Quarterstaff", false, ModWeaponTraitTags.QUARTERSTAFF, WeaponType.MELEE, 
			() -> Config.INSTANCE.quarterstaves.speed.get(), () -> Config.INSTANCE.quarterstaves.baseDamage.get().floatValue(), () -> Config.INSTANCE.quarterstaves.damageMultipler.get().floatValue());
	public static final WeaponArchetype SCYTHE = new WeaponArchetype("Scythe", false, ModWeaponTraitTags.SCYTHE, WeaponType.MELEE, 
			() -> Config.INSTANCE.scythes.speed.get(), () -> Config.INSTANCE.scythes.baseDamage.get().floatValue(), () -> Config.INSTANCE.scythes.damageMultipler.get().floatValue());
	
	public static final List<WeaponArchetype> ALL_ARCHETYPES = ImmutableList.of(DAGGER, PARRYING_DAGGER, LONGSWORD, KATANA, SABER, RAPIER, GREATSWORD, CLUB, CESTUS, BATTLE_HAMMER, WARHAMMER,
			SPEAR, HALBERD, PIKE, LANCE, THROWING_KNIFE, TOMAHAWK, JAVELIN, BOOMERANG, BATTLEAXE, FLANGED_MACE, GLAIVE, QUARTERSTAFF, SCYTHE);
	
	protected final String name;
	protected final TagKey<WeaponTrait> traitsTag;
	protected boolean isValidTag = true;
	protected List<WeaponTrait> traits = ImmutableList.of();
	protected Optional<WeaponTrait> actionTrait = Optional.empty();
	protected Optional<List<Pair<WeaponTrait, WeaponTrait.InvalidReason>>> invalidTraits = Optional.empty();
//	protected final Predicate<WeaponTrait> traitFilter;
	protected final WeaponType type;
	protected final boolean isBladed;						// Used to determine if the weapon has a blade can cut through things such as Cobwebs
	protected final Set<ItemAbility> toolActions;
	protected final Supplier<Double> speedValue;
	protected final Supplier<Float> baseDamage;
	protected final Supplier<Float> damageMultiplier;
	protected final Supplier<Integer> chargeTicks;  // For throwing weapons only

	public WeaponArchetype(String nameIn, boolean isBladedIn, TagKey<WeaponTrait> traitsTagIn, WeaponType typeIn, Supplier<Double> speedValueIn, Supplier<Float> baseDamageIn, 
			Supplier<Float> damageMultiplierIn, Set<ItemAbility> toolActionsIn)
	{
		name = nameIn;
		traitsTag = traitsTagIn;
		type = typeIn;
		isBladed = isBladedIn;
		toolActions = toolActionsIn;
		
		speedValue = speedValueIn;
		baseDamage = baseDamageIn;
		damageMultiplier = damageMultiplierIn;
		chargeTicks = null;  // Melee weapons don't have charge ticks
//		ReloadableHandler.addToItemReloadList(this);
	}
	
	public WeaponArchetype(String nameIn, boolean isBladedIn, TagKey<WeaponTrait> traitsTagIn, WeaponType typeIn, Supplier<Double> speedValueIn, Supplier<Float> baseDamageIn, 
			Supplier<Float> damageMultiplierIn, ItemAbility... toolActionsIn)
	{
		this(nameIn, isBladedIn, traitsTagIn, typeIn, speedValueIn, baseDamageIn, damageMultiplierIn, ImmutableSet.copyOf(toolActionsIn));
	}
	
	// Constructor for throwing weapons with chargeTicks
	public WeaponArchetype(String nameIn, boolean isBladedIn, TagKey<WeaponTrait> traitsTagIn, WeaponType typeIn, Supplier<Double> speedValueIn, Supplier<Float> baseDamageIn, 
			Supplier<Float> damageMultiplierIn, Supplier<Integer> chargeTicksIn)
	{
		name = nameIn;
		traitsTag = traitsTagIn;
		type = typeIn;
		isBladed = isBladedIn;
		toolActions = ImmutableSet.of();
		
		speedValue = speedValueIn;
		baseDamage = baseDamageIn;
		damageMultiplier = damageMultiplierIn;
		chargeTicks = chargeTicksIn;
	}

	@Override
	public void reload() 
	{
		RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		Registry<WeaponTrait> registry = registryAccess.registry(WeaponTraits.REGISTRY_KEY).orElse(null);
		if(registry == null)
		{
			Log.error("Weapon Trait registry couldn't be found for weapon archetype \"" + name + "\"!");
			return;
		}
		isValidTag = registry.getTag(traitsTag).isPresent();

		if(!isValidTag)
		{
			Log.error("Weapon Trait tag \"" + traitsTag.location() +  "\" couldn't be found for weapon archetype \"" + name + "\"!");
			return;
		}
		
		Iterable<Holder<WeaponTrait>> tag = registry.getTagOrEmpty(traitsTag);

		invalidTraits = Optional.empty();
		List<Pair<WeaponTrait, WeaponTrait.InvalidReason>> invalidTraitList = new ArrayList<>();
		List<String> invalidTraitValues = new ArrayList<>();
		AtomicReference<WeaponTrait> actionTraitRef = new AtomicReference<WeaponTrait>(null);
		
		List<WeaponTrait> traitList = new ArrayList<>();
		for(Holder<WeaponTrait> holder : tag)
		{
			WeaponTrait trait = holder.value();
			boolean isValid = type.getTraitFilter().test(trait);
			if(isValid && trait.isActionTrait())
			{
				if(actionTraitRef.get() == null)
					actionTraitRef.set(trait);
				else
				{
					invalidTraitList.add(Pair.of(trait, WeaponTrait.InvalidReason.MULTIPLE_ACTION_TRAITS));
					invalidTraitValues.add(String.valueOf(registry.getKey(trait)));
					continue;
				}
			}
			else if(!isValid)
			{
				WeaponTrait.InvalidReason reason = trait.isMeleeTrait() ? WeaponTrait.InvalidReason.WEAPON_NOT_MELEE :
													trait.isRangedTrait() ? WeaponTrait.InvalidReason.WEAPON_NOT_RANGED :
													trait.isThrowingTrait() ? WeaponTrait.InvalidReason.WEAPON_NOT_THROWING :
														WeaponTrait.InvalidReason.WEAPON_NOT_SUPPORTED;
				
				invalidTraitList.add(Pair.of(trait, reason));
				invalidTraitValues.add(String.valueOf(registry.getKey(trait)));
				continue;
			}
			traitList.add(trait);
		}
		traits = traitList.stream().collect(Collectors.toUnmodifiableList());
		
		WeaponTrait trait = actionTraitRef.get();
			actionTrait = trait != null ? Optional.of(actionTraitRef.get()) : Optional.empty();
		
		if(!invalidTraitList.isEmpty())
		{
			Log.warn("Found invalid Weapon Traits for weapon archetype \"" + name + "\" which have not been added: " + String.join(", ", invalidTraitValues));
			invalidTraits = Optional.of(invalidTraitList);
		}
	}
	
	public boolean isBladed() 
	{
		return isBladed;
	}
	
	public boolean canPerformToolAction(ItemAbility toolAction)
	{
		return toolActions.contains(toolAction);
	}
	
	public List<WeaponTrait> getTraits()
	{
		return traits;
	}
	
	public Optional<List<Pair<WeaponTrait, WeaponTrait.InvalidReason>>> getInvalidTraits() {
		return invalidTraits;
	}
	
	public Optional<WeaponTrait> getActionTrait()
	{
		return actionTrait;
	}
	
	public WeaponType getType() 
	{
		return type;
	}
	
	public void addTagErrorTooltip(ItemStack stack, List<Component> tooltip)
	{
		if(!isValidTag)
			tooltip.add(Component.translatable(String.format("tooltip.%s.trait.invalid.archetype_tag", SpartanWeaponryAPI.MOD_ID), name, traitsTag.location().toString()).withStyle(ChatFormatting.DARK_RED));
	}
	
	public void addTraitsToTooltip(ItemStack stack, List<Component> tooltip, boolean isShiftPressed)
	{
		getTraits().forEach((trait) -> trait.addTooltip(stack, tooltip, isShiftPressed, WeaponTrait.InvalidReason.NONE));
		if(invalidTraits.isPresent())
			invalidTraits.get().forEach((traitPair) -> traitPair.getLeft().addTooltip(stack, tooltip, isShiftPressed, traitPair.getRight()));
	}
	
	public double getAttackSpeed()
	{
		return speedValue.get().doubleValue();
	}
	
	public float getBaseDamage()
	{
		return baseDamage.get().floatValue();
	}
	
	public float getDamageMultiplier()
	{
		return damageMultiplier.get().floatValue();
	}
	
	public int getChargeTicks()
	{
		return chargeTicks != null ? chargeTicks.get() : 0;
	}
}
