package org.xiyu.spartanweaponryunofficial.item;

import com.google.common.collect.ImmutableMultimap;
import java.util.Collection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.xiyu.spartanweaponryunofficial.api.trait.WeaponTrait;

final class WeaponAttributeBuilder {
    private WeaponAttributeBuilder() {}

    static ItemAttributeModifiers buildMainHandAttributes(
            float attackDamage, double attackSpeed, Collection<WeaponTrait> traits) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers =
                ImmutableMultimap.builder();
        modifiers.put(
                Attributes.ATTACK_DAMAGE.value(),
                new AttributeModifier(
                        Item.BASE_ATTACK_DAMAGE_ID,
                        attackDamage,
                        AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(
                Attributes.ATTACK_SPEED.value(),
                new AttributeModifier(
                        Item.BASE_ATTACK_SPEED_ID,
                        attackSpeed - 4.0D,
                        AttributeModifier.Operation.ADD_VALUE));

        traits.forEach(
                trait ->
                        WeaponTraitResolver.getGenericCallback(trait)
                                .ifPresent(callback -> callback.onModifyAttributes(modifiers)));

        ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();
        modifiers
                .build()
                .forEach(
                        (attribute, modifier) ->
                                attributeBuilder.add(
                                        BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute),
                                        modifier,
                                        EquipmentSlotGroup.MAINHAND));
        return attributeBuilder.build();
    }

    static ItemAttributeModifiers buildGenericTraitItemAttributes(Collection<WeaponTrait> traits) {
        ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();
        buildGenericTraitAttributeMap(traits)
                .forEach(
                        (attribute, modifier) ->
                                attributeBuilder.add(
                                        BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute),
                                        modifier,
                                        EquipmentSlotGroup.MAINHAND));
        return attributeBuilder.build();
    }

    static ImmutableMultimap<Attribute, AttributeModifier> buildGenericTraitAttributeMap(
            Collection<WeaponTrait> traits) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> modifiers =
                ImmutableMultimap.builder();
        traits.forEach(
                trait ->
                        trait.getGenericCallback()
                                .ifPresent(callback -> callback.onModifyAttributes(modifiers)));
        return modifiers.build();
    }
}
