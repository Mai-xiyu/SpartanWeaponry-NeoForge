package org.xiyu.spartanweaponryunofficial.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

import java.util.List;

public class WeaponOilItem extends BasicItem {
    public WeaponOilItem() {
        super(new Item.Properties().stacksTo(6).craftRemainder(Items.GLASS_BOTTLE));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext tooltipContext, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
        OilEffect oil = OilHelper.getOilFromStack(stack);
        if (oil != OilEffects.NONE.get()) {
            tooltip.add(Component.empty());
            oil.getTooltip(stack, tooltip);
            tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".weapon_oil.uses", oil.getMaxUses()).withStyle(ChatFormatting.DARK_GREEN));
        } else {
            tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".weapon_oil.base"));
        }
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        OilEffect oil = OilHelper.getOilFromStack(stack);
        Potion potion = OilHelper.getPotionFromStack(stack);
        Registry<OilEffect> registry = getOilRegistry();
        ResourceLocation itemLoc = BuiltInRegistries.ITEM.getKey(this);
        Component baseName = Component.translatable("item." + itemLoc.getNamespace() + "." + itemLoc.getPath() + "." + (registry != null ? registry.getKey(oil).getPath() : "unknown"));
        if (potion == null)
            return baseName;
        ResourceLocation potionKey = BuiltInRegistries.POTION.getKey(potion);
        return potionKey == null ? baseName : Component.translatable("item.spartan_weaponry_unofficial.proj_tipped.effect." + potionKey.getPath(), baseName);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level levelIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        OilEffect oil = OilHelper.getOilFromStack(stack);
        if (oil != OilEffects.NONE.get()) {
            InteractionHand oppositeHand = handIn == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            ItemStack oppositeStack = playerIn.getItemInHand(oppositeHand);

            if (oppositeStack.is(ModItemTags.OILABLE_WEAPONS)) {
                // Apply the oil to the stack unless there is already oil on the stack
                IOilHandler handler = oppositeStack.getCapability(ModCapabilities.OIL_CAPABILITY);
                if (handler != null) {
                    if (!handler.isOiled()) {
                        if (oil == OilEffects.POTION.get()) {
                            Potion potion = OilHelper.getPotionFromStack(stack);
                            if (potion != null)
                                handler.setPotion(potion, stack);
                        } else
                            handler.setEffect(oil, stack);
                        playerIn.displayClientMessage(Component.translatable("message." + ModSpartanWeaponry.ID + ".oil_applied", stack.getHoverName(), oppositeStack.getHoverName()), true);
                        playerIn.playSound(ModSounds.OIL_APPLIED.get(), 1.0f, 1.0f);
                        // Remove one from the stack and replace the container back into the inventory (a glass bottle)
                        ItemStack bottleStack = this.getCraftingRemainingItem(stack);
                        stack.shrink(1);
                        if (stack.getCount() == 0)
                            playerIn.setItemInHand(handIn, ItemStack.EMPTY);
                        playerIn.getInventory().placeItemBackInInventory(bottleStack);
                    } else
                        playerIn.displayClientMessage(Component.translatable("message." + ModSpartanWeaponry.ID + ".weapon_already_oiled", stack.getHoverName(), oppositeStack.getHoverName()).withStyle(ChatFormatting.RED), true);
                }
            } else
                playerIn.displayClientMessage(Component.translatable("message." + ModSpartanWeaponry.ID + ".no_oilable_weapon", stack.getHoverName()).withStyle(ChatFormatting.RED), true);
        }
        return super.use(levelIn, playerIn, handIn);
    }

    @SuppressWarnings("unchecked")
    private static Registry<OilEffect> getOilRegistry() {
        return (Registry<OilEffect>) BuiltInRegistries.REGISTRY.get(OilEffects.REGISTRY_KEY.location());
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_41409_, @NotNull Level p_41410_, @NotNull LivingEntity p_41411_) {
        return super.finishUsingItem(p_41409_, p_41410_, p_41411_);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        OilEffect oilEffect = OilHelper.getOilFromStack(stack);
        return super.isFoil(stack) || oilEffect == OilEffects.POTION.get();
    }
}
