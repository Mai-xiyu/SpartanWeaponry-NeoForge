package org.xiyu.spartanweaponryunofficial.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.api.trait.IGenericTraitCallback;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;
import org.xiyu.spartanweaponryunofficial.util.WeaponType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

final class WeaponTraitResolver {
    private WeaponTraitResolver() {
    }

    static List<WeaponTrait> resolveTraits(WeaponArchetype archetype, WeaponMaterial material) {
        ImmutableList.Builder<WeaponTrait> traits = ImmutableList.builder();
        traits.addAll(archetype.getTraits());
        traits.addAll(material.getBonusTraits(archetype.getType()));
        return traits.build();
    }

    static List<WeaponTrait> resolveMaterialTraits(WeaponMaterial material, WeaponType weaponType) {
        return material.getBonusTraits(weaponType);
    }

    static Optional<IGenericTraitCallback> getGenericCallback(WeaponTrait trait) {
        return trait.getMeleeCallback().isPresent()
                ? Optional.of(trait.getMeleeCallback().get())
                : trait.getGenericCallback();
    }

    static Optional<Boolean> getEnchantmentCompatibility(Collection<WeaponTrait> traits, Enchantment enchantment) {
        for (WeaponTrait trait : traits) {
            if (trait.isEnchantmentIncompatible(enchantment))
                return Optional.of(false);
            if (trait.isEnchantmentCompatible(enchantment))
                return Optional.of(true);
        }
        return Optional.empty();
    }

    static <T extends LivingEntity> int applyDamageCallbacks(Collection<WeaponTrait> traits, ItemStack stack, T entity, int amount) {
        int damage = amount;
        for (WeaponTrait trait : traits) {
            Optional<IGenericTraitCallback> callback = trait.getGenericCallback();
            if (callback.isPresent())
                damage = callback.get().onDamageItem(stack, entity, damage);
            if (damage <= 0)
                break;
        }
        return Math.max(0, damage);
    }
}
