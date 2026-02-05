package org.xiyu.spartanweaponryunofficial.api.trait;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;
import org.xiyu.spartanweaponryunofficial.api.WeaponMaterial;

import java.util.Optional;

public class SpeedModifierWeaponTrait extends WeaponTrait implements IMeleeTraitCallback, IRangedTraitCallback, IThrowingTraitCallback {
    public static final ResourceLocation SPEED_MODIFIER = ResourceLocation.fromNamespaceAndPath(SpartanWeaponryAPI.MOD_ID, "speed_modifier");

    public SpeedModifierWeaponTrait(String type, TraitQuality quality) {
        super(type, SpartanWeaponryAPI.MOD_ID, quality);
    }

    @Override
    public Optional<IMeleeTraitCallback> getMeleeCallback() {
        return Optional.of(this);
    }

    @Override
    public void onModifyAttributesMelee(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
        builder.put(Attributes.ATTACK_SPEED.value(), new AttributeModifier(SPEED_MODIFIER, this.getMagnitude(), Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    public Optional<IRangedTraitCallback> getRangedCallback() {
        return Optional.of(this);
    }

    @Override
    public float modifyLongbowDrawTime(WeaponMaterial material, float baseDraw) {
        return baseDraw * (1.0f + (this.getMagnitude() * -1.0f));
    }

    @Override
    public int modifyHeavyCrossbowLoadTime(WeaponMaterial material, int baseLoad) {
        return Mth.floor(baseLoad * (1.0f + (this.getMagnitude() * -1.0f)));
    }

    @Override
    public int modifyHeavyCrossbowAimTime(WeaponMaterial material, int baseAim) {
        return Mth.floor(baseAim * (1.0f + (this.getMagnitude() * -1.0f)));
    }

    @Override
    public Optional<IThrowingTraitCallback> getThrowingCallback() {
        return Optional.of(this);
    }

    @Override
    public int modifyThrowingChargeTime(WeaponMaterial material, int baseCharge) {
        return Mth.floor(baseCharge * (1.0f + (this.getMagnitude() * -1.0f)));
    }
}
