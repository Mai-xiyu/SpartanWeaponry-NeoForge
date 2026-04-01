package org.xiyu.spartanweaponryunofficial.util;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.capability.OilHandler;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

import java.util.List;

public class OilHelper {

    private static final Component NO_EFFECT = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);

    public static OilEffect getOilFromStack(ItemStack stackIn) {
        CompoundTag tag = ItemStackDataHelper.getTag(stackIn).getCompoundOrEmpty(OilHandler.NBT_OIL);
        String oilId = tag.getStringOr(OilHandler.NBT_OIL_EFFECT, "");
        if (oilId.endsWith(":potion") || oilId.equals("potion"))
            return OilEffects.POTION.get();
        return OilEffects.NONE.get();
    }

    public static ItemStack makeOilStack(OilEffect oilIn) {
        ItemStack stack = new ItemStack(ModItems.WEAPON_OIL.get());
        CompoundTag tag = new CompoundTag();
        String effectId = oilIn == OilEffects.POTION.get() ? "spartan_weaponry_unofficial:potion" : "spartan_weaponry_unofficial:none";
        tag.putString(OilHandler.NBT_OIL_EFFECT, effectId);
        ItemStackDataHelper.updateTag(stack, stackTag -> stackTag.put(OilHandler.NBT_OIL, tag));
        return stack;
    }

    public static Potion getPotionFromStack(ItemStack stackIn) {
        CompoundTag tag = ItemStackDataHelper.getTag(stackIn).getCompoundOrEmpty(OilHandler.NBT_OIL);
        String potionId = tag.getStringOr(OilHandler.NBT_POTION, "");
        if (potionId.isEmpty())
            return null;
        return net.minecraft.core.registries.BuiltInRegistries.POTION.getValue(Identifier.parse(potionId));
    }

    public static ItemStack makePotionOilStack(Potion potionIn) {
        ItemStack stack = makeOilStack(OilEffects.POTION.get());
        CompoundTag tag = ItemStackDataHelper.getTag(stack).getCompoundOrEmpty(OilHandler.NBT_OIL);
        tag.putString(OilHandler.NBT_POTION, net.minecraft.core.registries.BuiltInRegistries.POTION.getKey(potionIn).toString());
        ItemStackDataHelper.updateTag(stack, stackTag -> stackTag.put(OilHandler.NBT_OIL, tag));
        return stack;
    }

    public static boolean isValidPotion(Potion potionIn) {
        boolean isValidPotion = true;
        if (potionIn.getEffects().isEmpty() || Config.INSTANCE.potionOilBlacklist.get().contains(net.minecraft.core.registries.BuiltInRegistries.POTION.getKey(potionIn).toString()))
            return false;

        if (Config.INSTANCE.potionOilWhitelist.get().contains(net.minecraft.core.registries.BuiltInRegistries.POTION.getKey(potionIn).toString()))
            return true;

        for (MobEffectInstance effect : potionIn.getEffects()) {
            // Block non-harmful effects
            if (effect.getEffect().value().getCategory() != MobEffectCategory.HARMFUL) {
                isValidPotion = false;
                break;
            }
        }
        return isValidPotion;
    }

    public static void addPotionTooltip(ItemStack stackIn, List<Component> tooltipListIn, float durationModifierIn) {
        addPotionTooltip(ItemStackDataHelper.getTag(stackIn).getCompoundOrEmpty(OilHandler.NBT_OIL), tooltipListIn, durationModifierIn);
    }

    public static void addPotionTooltip(CompoundTag tagIn, List<Component> tooltipListIn, float durationModifierIn) {
        Potion potion = getPotionFromTag(tagIn);
        if (potion == null) {
            tooltipListIn.add(NO_EFFECT);
            return;
        }
        PotionContents.addPotionTooltip(potion.getEffects(), tooltipListIn::add, durationModifierIn, 20.0F);
    }

    private static Potion getPotionFromTag(CompoundTag tag) {
        String potionId = tag.getStringOr(OilHandler.NBT_POTION, "");
        if (potionId.isEmpty())
            return null;
        return net.minecraft.core.registries.BuiltInRegistries.POTION.getValue(Identifier.parse(potionId));
    }
}
