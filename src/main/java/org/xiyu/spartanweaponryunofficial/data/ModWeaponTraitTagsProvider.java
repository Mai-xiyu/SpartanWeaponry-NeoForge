package org.xiyu.spartanweaponryunofficial.data;

import java.util.concurrent.CompletableFuture;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.tags.ModWeaponTraitTags;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModWeaponTraitTagsProvider extends IntrinsicHolderTagsProvider<WeaponTrait> 
{

	public ModWeaponTraitTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registry, ExistingFileHelper existingFileHelper)
	{
		super(output, WeaponTraits.REGISTRY_KEY, registry, 
				(weaponTrait) -> WeaponTraits.REGISTRY.getRegistry().get().getResourceKey(weaponTrait).orElseThrow(), 
				ModSpartanWeaponry.ID, existingFileHelper);
	}

	@Override
	public String getName() 
	{
		return ModSpartanWeaponry.NAME + " Weapon Trait Tags";
	}

	@Override
	protected void addTags(HolderLookup.Provider registry) 
	{
		tag(ModWeaponTraitTags.DAGGER).add(WeaponTraits.THROWABLE.get(), WeaponTraits.DAMAGE_BONUS_BACKSTAB.get());
		tag(ModWeaponTraitTags.PARRYING_DAGGER).add(WeaponTraits.BLOCK_MELEE.get());
		tag(ModWeaponTraitTags.LONGSWORD).add(WeaponTraits.TWO_HANDED_1.get(), WeaponTraits.SWEEP_1.get());
		tag(ModWeaponTraitTags.KATANA).add(WeaponTraits.TWO_HANDED_1.get(), WeaponTraits.DAMAGE_BONUS_CHEST.get(), WeaponTraits.SWEEP_1.get());
		tag(ModWeaponTraitTags.SABER).add(WeaponTraits.DAMAGE_ABSORB.get(), WeaponTraits.DAMAGE_BONUS_CHEST.get(), WeaponTraits.SWEEP_1.get());
		tag(ModWeaponTraitTags.RAPIER).add(WeaponTraits.DAMAGE_ABSORB.get(), WeaponTraits.DAMAGE_BONUS_UNARMOURED.get());
		tag(ModWeaponTraitTags.GREATSWORD).add(WeaponTraits.TWO_HANDED_2.get(), WeaponTraits.REACH_1.get(), WeaponTraits.SWEEP_3.get());
		tag(ModWeaponTraitTags.CLUB).add(WeaponTraits.NAUSEA.get());
		tag(ModWeaponTraitTags.CESTUS).add(WeaponTraits.QUICK_STRIKE.get());
		tag(ModWeaponTraitTags.BATTLE_HAMMER).add(WeaponTraits.KNOCKBACK.get(), WeaponTraits.NAUSEA.get(), WeaponTraits.HAMMER_SLAM.get());
		tag(ModWeaponTraitTags.WARHAMMER).add(WeaponTraits.TWO_HANDED_1.get(), WeaponTraits.ARMOUR_PIERCING.get());
		tag(ModWeaponTraitTags.SPEAR).add(WeaponTraits.REACH_1.get());
		tag(ModWeaponTraitTags.HALBERD).add(WeaponTraits.TWO_HANDED_2.get(), WeaponTraits.REACH_1.get(), WeaponTraits.SHIELD_BREACH.get());
		tag(ModWeaponTraitTags.PIKE).add(WeaponTraits.TWO_HANDED_1.get(), WeaponTraits.REACH_2.get());
		tag(ModWeaponTraitTags.LANCE).add(WeaponTraits.REACH_1.get(), WeaponTraits.DAMAGE_BONUS_RIDING.get(), WeaponTraits.SWEEP_1.get());
		tag(ModWeaponTraitTags.THROWING_KNIFE).add(WeaponTraits.DAMAGE_BONUS_THROWN_1.get());
		tag(ModWeaponTraitTags.TOMAHAWK).add(WeaponTraits.DAMAGE_BONUS_THROWN_1.get());
		tag(ModWeaponTraitTags.JAVELIN).add(WeaponTraits.DAMAGE_BONUS_THROWN_2.get());
		tag(ModWeaponTraitTags.BOOMERANG);
		tag(ModWeaponTraitTags.BATTLEAXE).add(WeaponTraits.TWO_HANDED_1.get(), WeaponTraits.VERSATILE_AXE.get());
		tag(ModWeaponTraitTags.FLANGED_MACE).add(WeaponTraits.DAMAGE_BONUS_UNDEAD.get());
		tag(ModWeaponTraitTags.GLAIVE).add(WeaponTraits.TWO_HANDED_1.get(), WeaponTraits.REACH_1.get(), WeaponTraits.SWEEP_2.get());
		tag(ModWeaponTraitTags.QUARTERSTAFF).add(WeaponTraits.TWO_HANDED_1.get(), WeaponTraits.SWEEP_2.get());
		tag(ModWeaponTraitTags.SCYTHE).add(WeaponTraits.DAMAGE_BONUS_HEAD.get(), WeaponTraits.DECAPITATE.get());
		
		tag(ModWeaponTraitTags.WOOD);
		tag(ModWeaponTraitTags.STONE);
		tag(ModWeaponTraitTags.LEATHER);
		tag(ModWeaponTraitTags.COPPER);
		tag(ModWeaponTraitTags.IRON);
		tag(ModWeaponTraitTags.GOLD);
		tag(ModWeaponTraitTags.DIAMOND);
		tag(ModWeaponTraitTags.NETHERITE).add(WeaponTraits.FIREPROOF.get());
		
		tag(ModWeaponTraitTags.TIN);
		tag(ModWeaponTraitTags.BRONZE);
		tag(ModWeaponTraitTags.STEEL);
		tag(ModWeaponTraitTags.SILVER).add(WeaponTraits.DAMAGE_BONUS_UNDEAD.get());
		tag(ModWeaponTraitTags.LEAD).add(WeaponTraits.HEAVY_2.get());
		tag(ModWeaponTraitTags.ELECTRUM);
		tag(ModWeaponTraitTags.NICKEL);
		tag(ModWeaponTraitTags.INVAR);
		tag(ModWeaponTraitTags.CONSTANTAN);
		tag(ModWeaponTraitTags.PLATINUM);
		tag(ModWeaponTraitTags.ALUMINUM).add(WeaponTraits.LIGHTWEIGHT_2.get());
	}

}
