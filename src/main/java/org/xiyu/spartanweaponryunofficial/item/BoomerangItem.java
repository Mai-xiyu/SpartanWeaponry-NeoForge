package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;
import org.xiyu.spartanweaponryunofficial.entity.projectile.BoomerangEntity;
import org.xiyu.spartanweaponryunofficial.entity.projectile.ThrowingWeaponEntity;
import org.xiyu.spartanweaponryunofficial.init.ModEnchantments;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;
import org.xiyu.spartanweaponryunofficial.util.Defaults;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

public class BoomerangItem extends ThrowingWeaponItem {

    public BoomerangItem(Item.Properties prop, WeaponMaterial materialIn, WeaponArchetype archetypeIn) {
        super(prop.durability(materialIn.getUses()), materialIn, archetypeIn, Defaults.DamageBaseBoomerang, Defaults.DamageMultiplierBoomerang, Defaults.MeleeSpeedBoomerang, 1, Defaults.ChargeTicksBoomerang);
    }

    public BoomerangItem(Item.Properties prop, WeaponMaterial material, WeaponArchetype archetypeIn, String customDisplayName) {
        this(prop.durability(material.getUses()), material, archetypeIn);
        if (material.useCustomDisplayName())
            this.customDisplayName = customDisplayName;
    }

    @Override
    public ThrowingWeaponEntity createThrowingWeaponEntity(Level levelIn, Player player, ItemStack stack, int charge) {
        BoomerangEntity boomerang = new BoomerangEntity(levelIn, player, stack);
        boomerang.setDistanceToReturn((charge / 5.0d) * (BoomerangEntity.DISTANCE_TO_RETURN - 3.0d) + 3.0d +
                ModEnchantments.getLevel(levelIn.registryAccess(), ModEnchantments.PROPEL, stack) * 3.0f);
        return boomerang;
    }

    @Override
    protected SoundEvent getThrowingSound() {
        return ModSounds.BOOMERANG_THROW.get();
    }
}
