package org.xiyu.spartanweaponryunofficial.util;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.capability.OilHandler;
import org.xiyu.spartanweaponryunofficial.init.ModItems;

public class OilHelper {

    private static final Component NO_EFFECT =
            Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);

    public static OilEffect getOilFromStack(ItemStack stackIn) {
        CompoundTag tag = ItemStackDataHelper.getTag(stackIn).getCompound(OilHandler.NBT_OIL);
        ResourceLocation oil =
                ResourceLocation.tryParse(tag.getString(OilHandler.NBT_OIL_EFFECT));
        Registry<OilEffect> registry = OilEffects.registry();
        if (oil != null && registry.containsKey(oil)) return registry.get(oil);
        return OilEffects.NONE.get();
    }

    public static ItemStack makeOilStack(OilEffect oilIn) {
        ItemStack stack = new ItemStack(ModItems.WEAPON_OIL.get());
        CompoundTag tag = new CompoundTag();
        ResourceLocation oilId = OilEffects.registry().getKey(oilIn);
        if (oilId != null) tag.putString(OilHandler.NBT_OIL_EFFECT, oilId.toString());
        ItemStackDataHelper.updateTag(stack, stackTag -> stackTag.put(OilHandler.NBT_OIL, tag));
        return stack;
    }

    public static Potion getPotionFromStack(ItemStack stackIn) {
        CompoundTag tag = ItemStackDataHelper.getTag(stackIn).getCompound(OilHandler.NBT_OIL);
        String potionId = tag.getString(OilHandler.NBT_POTION);
        if (potionId.isEmpty()) return null;
        return BuiltInRegistries.POTION.get(ResourceLocation.parse(potionId));
    }

    public static ItemStack makePotionOilStack(Potion potionIn) {
        ItemStack stack = makeOilStack(OilEffects.POTION.get());
        CompoundTag tag = ItemStackDataHelper.getTag(stack).getCompound(OilHandler.NBT_OIL);
        tag.putString(OilHandler.NBT_POTION, BuiltInRegistries.POTION.getKey(potionIn).toString());
        ItemStackDataHelper.updateTag(stack, stackTag -> stackTag.put(OilHandler.NBT_OIL, tag));
        return stack;
    }

    public static boolean isValidPotion(Potion potionIn) {
        boolean isValidPotion = true;
        if (potionIn.getEffects().isEmpty()
                || Config.INSTANCE
                        .potionOilBlacklist
                        .get()
                        .contains(BuiltInRegistries.POTION.getKey(potionIn).toString()))
            return false;

        if (Config.INSTANCE
                .potionOilWhitelist
                .get()
                .contains(BuiltInRegistries.POTION.getKey(potionIn).toString())) return true;

        for (MobEffectInstance effect : potionIn.getEffects()) {
            // Block non-harmful effects
            if (effect.getEffect().value().getCategory() != MobEffectCategory.HARMFUL) {
                isValidPotion = false;
                break;
            }
        }
        return isValidPotion;
    }

    public static void addPotionTooltip(
            ItemStack stackIn, List<Component> tooltipListIn, float durationModifierIn) {
        addPotionTooltip(
                ItemStackDataHelper.getTag(stackIn).getCompound(OilHandler.NBT_OIL),
                tooltipListIn,
                durationModifierIn);
    }

    public static void addPotionTooltip(
            CompoundTag tagIn, List<Component> tooltipListIn, float durationModifierIn) {
        Potion potion = getPotionFromTag(tagIn);
        if (potion == null) {
            tooltipListIn.add(NO_EFFECT);
            return;
        }
        PotionContents.addPotionTooltip(
                potion.getEffects(), tooltipListIn::add, durationModifierIn, 20.0F);
    }

    private static Potion getPotionFromTag(CompoundTag tag) {
        String potionId = tag.getString(OilHandler.NBT_POTION);
        if (potionId.isEmpty()) return null;
        return BuiltInRegistries.POTION.get(ResourceLocation.parse(potionId));
    }
}
