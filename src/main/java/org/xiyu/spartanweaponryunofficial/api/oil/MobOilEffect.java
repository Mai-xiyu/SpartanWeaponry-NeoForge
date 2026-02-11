package org.xiyu.spartanweaponryunofficial.api.oil;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

import java.util.List;
import java.util.function.Supplier;

/**
 * An {@link OilEffect} that also inflicts a Mob Effect when hitting a mob with the weapon this is applied to<br>
 *
 * @author ObliviousSpartan
 */
public class MobOilEffect extends OilEffect {
    private final Supplier<Holder<MobEffect>> mobEffectSupplier;
    private final int effectDuration;
    private final int effectLevel;

    public MobOilEffect(String nameIn, OilEffectType typeIn, int colorIn, int maxUsesIn, float damageModifierIn, IUsePredicate usePredicateIn, Supplier<Holder<MobEffect>> mobEffectSupplierIn, int effectDurationIn, int effectLevelIn) {
        super(nameIn, typeIn, colorIn, maxUsesIn, damageModifierIn, usePredicateIn);
        this.mobEffectSupplier = mobEffectSupplierIn;
        this.effectDuration = effectDurationIn;
        this.effectLevel = effectLevelIn;
    }

    public MobOilEffect(String nameIn, OilEffectType typeIn, int colorIn, int maxUsesIn, Holder<MobEffect> mobEffectIn, int effectDurationIn, int effectLevelIn) {
        this(nameIn, typeIn, colorIn, maxUsesIn, 0.0f, OilEffect.USE_NOTHING, () -> mobEffectIn, effectDurationIn, effectLevelIn);
    }

    @Override
    public float onUse(float baseDamageIn, Level levelIn, LivingEntity targetIn, LivingEntity userIn, ItemStack oilStackIn) {
        targetIn.addEffect(new MobEffectInstance(this.mobEffectSupplier.get(), this.effectDuration, this.effectLevel), userIn);
        return super.onUse(baseDamageIn, levelIn, targetIn, userIn, oilStackIn);
    }

    @Override
    public void getTooltip(ItemStack stackIn, List<Component> tooltipListIn) {
        MutableComponent mobEffectComponent = this.mobEffectSupplier.get().value().getDisplayName().copy().withStyle(ChatFormatting.YELLOW);
        if (this.effectLevel > 0)
            mobEffectComponent.append(" ").append(Component.translatable("enchantment.level." + (this.effectLevel + 1)));
        if (this.damageModifier == 0.0f)
            tooltipListIn.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".weapon_oil.applied." + this.name, mobEffectComponent, (float) this.effectDuration / 20.0f).withStyle(ChatFormatting.BLUE));
        else
            tooltipListIn.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".weapon_oil.applied." + this.name, (this.getDamageModifier() * 100.0f), mobEffectComponent, (float) this.effectDuration / 20.0f).withStyle(ChatFormatting.BLUE));
    }
}
