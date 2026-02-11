package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingKnifeEntity;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingWeaponEntity;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

public class ThrowingKnifeItem extends ThrowingWeaponItem {

    public ThrowingKnifeItem(Item.Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn) {
        super(prop, material, archetypeIn, Defaults.DamageBaseThrowingKnife, Defaults.DamageMultiplierThrowingKnife, Defaults.MeleeSpeedThrowingKnife, 16, Defaults.ChargeTicksThrowingKnife);
    }

    public ThrowingKnifeItem(Item.Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn, String customDisplayName) {
        this(prop, material, archetypeIn);
        if (material.useCustomDisplayName())
            this.customDisplayName = customDisplayName;
    }

    @Override
    public ThrowingWeaponEntity createThrowingWeaponEntity(Level level, Player player, ItemStack stack, int charge) {
        return new ThrowingKnifeEntity(level, player, stack);
    }

    @Override
    protected SoundEvent getThrowingSound() {
        return ModSounds.THROWING_KNIFE_THROW.get();
    }
}
