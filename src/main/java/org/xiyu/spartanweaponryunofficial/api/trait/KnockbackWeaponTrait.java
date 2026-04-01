package org.xiyu.spartanweaponryunofficial.api.trait;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;

public class KnockbackWeaponTrait extends MeleeCallbackWeaponTrait {
    public static final Identifier KNOCKBACK_MODIFIER = Identifier.fromNamespaceAndPath(SpartanWeaponryAPI.MOD_ID, "knockback_modifier");

    public KnockbackWeaponTrait(String type, String modId) {
        super(type, modId, TraitQuality.POSITIVE);
    }

    @Override
    public void onModifyAttributesMelee(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
        builder.put(Attributes.ATTACK_KNOCKBACK.value(), new AttributeModifier(KNOCKBACK_MODIFIER, 1.0d, Operation.ADD_VALUE));
    }

}
