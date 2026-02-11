package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.entity.projectile.JavelinEntity;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingWeaponEntity;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

public class JavelinItem extends ThrowingWeaponItem {
    public JavelinItem(Item.Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn) {
        super(prop, material, archetypeIn, Defaults.DamageBaseJavelin, Defaults.DamageMultiplierJavelin, Defaults.MeleeSpeedJavelin, 4, Defaults.ChargeTicksJavelin);
        this.throwVelocity = 2.4f;
    }

    public JavelinItem(Item.Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn, String customDisplayName) {
        this(prop, material, archetypeIn);
        if (material.useCustomDisplayName())
            this.customDisplayName = customDisplayName;
    }

    @Override
    public ThrowingWeaponEntity createThrowingWeaponEntity(Level level, Player player, ItemStack stack, int charge) {
        return new JavelinEntity(level, player, stack);
    }

    @Override
    protected SoundEvent getThrowingSound() {
        return ModSounds.JAVELIN_THROW.get();
    }
}
