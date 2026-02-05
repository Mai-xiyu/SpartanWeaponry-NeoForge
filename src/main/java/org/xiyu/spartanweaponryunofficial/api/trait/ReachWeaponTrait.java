package org.xiyu.spartanweaponryunofficial.api.trait;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;

public class ReachWeaponTrait extends MeleeCallbackWeaponTrait {
    public static final ResourceLocation ATTACK_REACH_MODIFIER = ResourceLocation.fromNamespaceAndPath(SpartanWeaponryAPI.MOD_ID, "attack_reach_modifier");

    public ReachWeaponTrait(String propType, String propModId) {
        super(propType, propModId, TraitQuality.POSITIVE);
    }

    @Override
    public void onModifyAttributesMelee(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
        builder.put(Attributes.ENTITY_INTERACTION_RANGE.value(), new AttributeModifier(ATTACK_REACH_MODIFIER, this.getMagnitude() - 5.0, AttributeModifier.Operation.ADD_VALUE));
    }
}
