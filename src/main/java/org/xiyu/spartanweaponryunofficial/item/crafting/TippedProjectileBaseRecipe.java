package org.xiyu.spartanweaponryunofficial.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.init.ModRecipeSerializers;

public class TippedProjectileBaseRecipe extends CustomRecipe {
    protected final Item projectileIn;
    protected final Item projectileOut;

    public static final MapCodec<TippedProjectileBaseRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("projectile").forGetter(recipe -> recipe.projectileIn),
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("result").forGetter(recipe -> recipe.projectileOut)
    ).apply(instance, TippedProjectileBaseRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TippedProjectileBaseRecipe> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    public TippedProjectileBaseRecipe(Item arrowIn, Item arrowOut) {
        super();
        this.projectileIn = arrowIn;
        this.projectileOut = arrowOut;
    }

    @Override
    public boolean matches(CraftingInput container, @NotNull Level level) {
        if (container.width() == 3 && container.height() == 3) {
            for (int i = 0; i < container.width(); i++) {
                for (int j = 0; j < container.height(); j++) {
                    ItemStack stack = container.getItem(j * container.width() + i);

                    if (stack.isEmpty())
                        return false;

                    Item item = stack.getItem();

                    if (i == 1 && j == 1) {
                        if (item != Items.LINGERING_POTION)
                            return false;
                    } else if (item != this.projectileIn)
                        return false;
                }
            }
            return true;
        } else
            return false;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput inv) {
        ItemStack potionStack = inv.getItem(1 + inv.width());
        if (potionStack.getItem() == Items.LINGERING_POTION) {
            PotionContents contents = potionStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            Potion potion = contents.potion().map(Holder::value).orElse(null);
            if (potion != null && !potion.getEffects().isEmpty()) {
                ItemStack arrowResult = new ItemStack(this.projectileOut, 8);
                arrowResult.set(DataComponents.POTION_CONTENTS, contents);
                return arrowResult;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipeSerializers.TIPPED_PROJECTILE_BASE.get();
    }
}