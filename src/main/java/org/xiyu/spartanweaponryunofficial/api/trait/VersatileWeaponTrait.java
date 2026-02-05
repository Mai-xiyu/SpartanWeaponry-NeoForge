package org.xiyu.spartanweaponryunofficial.api.trait;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;

import java.util.List;

public class VersatileWeaponTrait extends WeaponTrait {
    private final TagKey<Block> effectiveBlocks;
    private final String toolName;

    public VersatileWeaponTrait(String type, String modId, TagKey<Block> effectiveBlocksTag, String effectiveToolName) {
        super(type, modId, TraitQuality.POSITIVE);
        this.effectiveBlocks = effectiveBlocksTag;
        this.toolName = effectiveToolName;
        this.isMelee = true;
    }

    @Override
    protected void addTooltipTitle(ItemStack stack, List<Component> tooltip, ChatFormatting... formatting) {
        MutableComponent titleText = Component.literal("- ").withStyle(formatting);
        String toolType = this.effectiveBlocks != null && this.toolName != null && !this.toolName.isBlank() ?
                String.format("tooltip.%s.trait.versatile." + this.toolName, SpartanWeaponryAPI.MOD_ID) :
                String.format("tooltip.%s.trait.versatile.nothing", SpartanWeaponryAPI.MOD_ID);
        tooltip.add(titleText.append(Component.translatable(String.format("tooltip.%s.trait.%s", this.modId, this.type), Component.translatable(toolType)).withStyle(formatting)));
    }

    @Override
    protected void addTooltipDescription(ItemStack stack, List<Component> tooltip) {
        tooltip.add(tooltipIndent().append(Component.translatable(String.format("tooltip.%s.trait.%s.desc", SpartanWeaponryAPI.MOD_ID, this.type))).withStyle(WeaponTrait.DESCRIPTION_FORMAT));
    }

    public TagKey<Block> getEffectiveBlocks() {
        return this.effectiveBlocks;
    }

    @Override
    public boolean isEnchantmentCompatible(Enchantment enchantIn) {
        // In 1.21, Enchantment is data-driven and isSupportedItem no longer exists
        // Return false as the base behavior, specific enchantment compatibility can be added if needed
        return false;
    }
}
