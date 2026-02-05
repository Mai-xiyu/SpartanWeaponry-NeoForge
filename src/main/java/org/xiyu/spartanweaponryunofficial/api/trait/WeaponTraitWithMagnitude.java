package org.xiyu.spartanweaponryunofficial.api.trait;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WeaponTraitWithMagnitude extends WeaponTrait {
    public WeaponTraitWithMagnitude(String type, String modId, TraitQuality quality) {
        super(type, modId, quality);
    }

    @Override
    protected void addTooltipDescription(ItemStack stack, List<Component> tooltip) {
        tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.desc", this.modId, this.type), this.magnitude).withStyle(WeaponTrait.DESCRIPTION_FORMAT)));
    }

}
