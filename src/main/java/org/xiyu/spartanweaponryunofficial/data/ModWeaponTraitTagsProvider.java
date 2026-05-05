package org.xiyu.spartanweaponryunofficial.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.tags.ModWeaponTraitTags;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

import java.util.concurrent.CompletableFuture;

public class ModWeaponTraitTagsProvider extends IntrinsicHolderTagsProvider<WeaponTrait> {

    public ModWeaponTraitTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registry, ExistingFileHelper existingFileHelper) {
        super(output, WeaponTraits.REGISTRY_KEY, registry,
                (weaponTrait) -> WeaponTraits.REGISTRY.getRegistry().get().getResourceKey(weaponTrait).orElseThrow(),
                ModSpartanWeaponry.ID, existingFileHelper);
    }

    @Override
    public @NotNull String getName() {
        return ModSpartanWeaponry.NAME + " Weapon Trait Tags";
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registry) {
        this.tag(ModWeaponTraitTags.DAGGER).add(WeaponTraits.THROWABLE.get(), WeaponTraits.DAMAGE_BONUS_BACKSTAB.get());
        this.tag(ModWeaponTraitTags.PARRYING_DAGGER).add(WeaponTraits.BLOCK_MELEE.get());
        this.tag(ModWeaponTraitTags.LONGSWORD).add(WeaponTraits.SWEEP_1.get());
        this.tag(ModWeaponTraitTags.KATANA).add(WeaponTraits.DAMAGE_BONUS_CHEST.get(), WeaponTraits.SWEEP_1.get());
        this.tag(ModWeaponTraitTags.SABER).add(WeaponTraits.DAMAGE_ABSORB.get(), WeaponTraits.DAMAGE_BONUS_CHEST.get(), WeaponTraits.SWEEP_1.get());
        this.tag(ModWeaponTraitTags.RAPIER).add(WeaponTraits.DAMAGE_ABSORB.get(), WeaponTraits.DAMAGE_BONUS_UNARMORED.get());
        this.tag(ModWeaponTraitTags.GREATSWORD).add(WeaponTraits.REACH_1.get(), WeaponTraits.SWEEP_3.get());
        this.tag(ModWeaponTraitTags.CLUB).add(WeaponTraits.NAUSEA.get());
        this.tag(ModWeaponTraitTags.CESTUS).add(WeaponTraits.QUICK_STRIKE.get());
        this.tag(ModWeaponTraitTags.BATTLE_HAMMER).add(WeaponTraits.KNOCKBACK.get(), WeaponTraits.NAUSEA.get(), WeaponTraits.HAMMER_SLAM.get());
        this.tag(ModWeaponTraitTags.WARHAMMER).add(WeaponTraits.ARMOR_PIERCING.get());
        this.tag(ModWeaponTraitTags.SPEAR).add(WeaponTraits.REACH_2.get());
        this.tag(ModWeaponTraitTags.HALBERD).add(WeaponTraits.REACH_1_5.get(), WeaponTraits.SHIELD_BREACH.get());
        this.tag(ModWeaponTraitTags.PIKE).add(WeaponTraits.REACH_2_5.get());
        this.tag(ModWeaponTraitTags.LANCE).add(WeaponTraits.REACH_2.get(), WeaponTraits.DAMAGE_BONUS_RIDING.get(), WeaponTraits.SWEEP_1.get());
        this.tag(ModWeaponTraitTags.THROWING_KNIFE).add(WeaponTraits.DAMAGE_BONUS_THROWN_1.get());
        this.tag(ModWeaponTraitTags.TOMAHAWK).add(WeaponTraits.DAMAGE_BONUS_THROWN_1.get());
        this.tag(ModWeaponTraitTags.JAVELIN).add(WeaponTraits.DAMAGE_BONUS_THROWN_2.get(), WeaponTraits.REACH_1.get());
        this.tag(ModWeaponTraitTags.BOOMERANG);
        this.tag(ModWeaponTraitTags.BATTLEAXE).add(WeaponTraits.VERSATILE_AXE.get());
        this.tag(ModWeaponTraitTags.FLANGED_MACE).add(WeaponTraits.DAMAGE_BONUS_UNDEAD.get());
        this.tag(ModWeaponTraitTags.GLAIVE).add(WeaponTraits.REACH_1_5.get(), WeaponTraits.SWEEP_2.get());
        this.tag(ModWeaponTraitTags.QUARTERSTAFF).add(WeaponTraits.SWEEP_2.get(), WeaponTraits.REACH_1.get());
        this.tag(ModWeaponTraitTags.SCYTHE).add(WeaponTraits.DAMAGE_BONUS_HEAD.get(), WeaponTraits.DECAPITATE.get(), WeaponTraits.REACH_1.get());

        this.tag(ModWeaponTraitTags.WOOD);
        this.tag(ModWeaponTraitTags.STONE);
        this.tag(ModWeaponTraitTags.LEATHER);
        this.tag(ModWeaponTraitTags.COPPER);
        this.tag(ModWeaponTraitTags.IRON);
        this.tag(ModWeaponTraitTags.GOLD);
        this.tag(ModWeaponTraitTags.DIAMOND);
        this.tag(ModWeaponTraitTags.NETHERITE).add(WeaponTraits.FIREPROOF.get());

        this.tag(ModWeaponTraitTags.TIN);
        this.tag(ModWeaponTraitTags.BRONZE);
        this.tag(ModWeaponTraitTags.STEEL);
        this.tag(ModWeaponTraitTags.SILVER).add(WeaponTraits.DAMAGE_BONUS_UNDEAD.get());
        this.tag(ModWeaponTraitTags.LEAD).add(WeaponTraits.HEAVY_2.get());
        this.tag(ModWeaponTraitTags.ELECTRUM);
        this.tag(ModWeaponTraitTags.NICKEL);
        this.tag(ModWeaponTraitTags.INVAR);
        this.tag(ModWeaponTraitTags.CONSTANTAN);
        this.tag(ModWeaponTraitTags.PLATINUM);
        this.tag(ModWeaponTraitTags.ALUMINUM).add(WeaponTraits.LIGHTWEIGHT_2.get());
    }

}
