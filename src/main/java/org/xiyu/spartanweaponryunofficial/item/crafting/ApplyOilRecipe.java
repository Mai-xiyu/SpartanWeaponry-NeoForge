package org.xiyu.spartanweaponryunofficial.item.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.init.ModRecipeSerializers;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

public class ApplyOilRecipe extends CustomRecipe {

    public static final MapCodec<ApplyOilRecipe> CODEC = MapCodec.unit(ApplyOilRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, ApplyOilRecipe> STREAM_CODEC = StreamCodec.unit(new ApplyOilRecipe());

    public ApplyOilRecipe() {
        super();
    }

    @Override
    public boolean matches(CraftingInput containerIn, @NotNull Level levelIn) {
        boolean foundOil = false,
                foundWeapon = false;

        for (int i = 0; i < containerIn.width(); i++) {
            for (int j = 0; j < containerIn.height(); j++) {
                ItemStack stack = containerIn.getItem(j * containerIn.width() + i);
                // Oil found
                if (stack.is(ModItems.WEAPON_OIL.get())) {
                    // Already have an oil; not a valid recipe
                    if (foundOil)
                        return false;
                    foundOil = true;
                }
                // Oilable weapon found
                else if (stack.is(ModItemTags.OILABLE_WEAPONS)) {
                    // Already have an oilable weapon; not a valid recipe
                    if (foundWeapon)
                        return false;
                    foundWeapon = true;
                }
                // Other item found; not a valid recipe
                else if (!stack.isEmpty())
                    return false;
            }
        }
        return foundOil && foundWeapon;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput containerIn) {
        ItemStack oilStack = ItemStack.EMPTY,
                weaponStack = ItemStack.EMPTY;

        for (int i = 0; i < containerIn.width(); i++) {
            for (int j = 0; j < containerIn.height(); j++) {
                ItemStack stack = containerIn.getItem(j * containerIn.width() + i);
                // Oil found
                if (stack.is(ModItems.WEAPON_OIL.get())) {
                    // Already have an oil; not a valid recipe
                    if (!oilStack.isEmpty())
                        return ItemStack.EMPTY;
                    oilStack = stack;
                }
                // Oilable weapon found
                else if (stack.is(ModItemTags.OILABLE_WEAPONS)) {
                    // Already have an oilable weapon; not a valid recipe
                    if (!weaponStack.isEmpty())
                        return ItemStack.EMPTY;
                    weaponStack = stack;
                }
                // Other item found; not a valid recipe
                else if (!stack.isEmpty())
                    return ItemStack.EMPTY;
            }
        }
        if (!oilStack.isEmpty() && !weaponStack.isEmpty()) {
            ItemStack resultStack = weaponStack.copy();
            OilEffect effect = OilHelper.getOilFromStack(oilStack);
            IOilHandler handler = resultStack.getCapability(ModCapabilities.OIL_CAPABILITY);
            if (effect != OilEffects.NONE.get() && handler != null) {
                if (effect == OilEffects.POTION.get()) {
                    Potion potion = OilHelper.getPotionFromStack(oilStack);
                    if (potion != null)
                        handler.setPotion(potion, oilStack);
                } else
                    handler.setEffect(effect, oilStack);
                return resultStack.copy();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.APPLY_OIL.get();
    }
}