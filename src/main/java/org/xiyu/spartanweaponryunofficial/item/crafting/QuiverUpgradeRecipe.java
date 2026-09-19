package org.xiyu.spartanweaponryunofficial.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.capability.IQuiverItemHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.init.ModRecipeSerializers;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;

public class QuiverUpgradeRecipe extends SmithingTransformRecipe {
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;

    public QuiverUpgradeRecipe(
            Ingredient templateIn, Ingredient baseIn, Ingredient additionIn, ItemStack resultIn) {
        super(templateIn, baseIn, additionIn, resultIn);
        this.template = templateIn;
        this.base = baseIn;
        this.addition = additionIn;
        this.result = resultIn;
    }

    @Override
    public @NotNull ItemStack assemble(
            @NotNull SmithingRecipeInput inv, HolderLookup.@NotNull Provider registryAccessIn) {
        ItemStack outputStack = super.assemble(inv, registryAccessIn);
        if (outputStack.getItem() instanceof QuiverBaseItem quiverItem) {
            IQuiverItemHandler itemHandler =
                    outputStack.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
            if (itemHandler != null) itemHandler.resize(quiverItem.getAmmoSlots());
        }

        return outputStack;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.QUIVER_UPGRADE_SMITHING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeType.SMITHING;
    }

    public Ingredient getTemplate() {
        return this.template;
    }

    public Ingredient getBase() {
        return this.base;
    }

    public Ingredient getAddition() {
        return this.addition;
    }

    public ItemStack getResultStack() {
        return this.result;
    }

    public static class Serializer implements RecipeSerializer<QuiverUpgradeRecipe> {
        public Serializer() {}

        private static final MapCodec<QuiverUpgradeRecipe> CODEC =
                RecordCodecBuilder.mapCodec(
                        instance ->
                                instance.group(
                                                Ingredient.CODEC
                                                        .fieldOf("template")
                                                        .forGetter(
                                                                QuiverUpgradeRecipe::getTemplate),
                                                Ingredient.CODEC
                                                        .fieldOf("base")
                                                        .forGetter(QuiverUpgradeRecipe::getBase),
                                                Ingredient.CODEC
                                                        .fieldOf("addition")
                                                        .forGetter(
                                                                QuiverUpgradeRecipe::getAddition),
                                                ItemStack.CODEC
                                                        .fieldOf("result")
                                                        .forGetter(
                                                                QuiverUpgradeRecipe
                                                                        ::getResultStack))
                                        .apply(instance, QuiverUpgradeRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, QuiverUpgradeRecipe>
                STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

        @Override
        public @NotNull MapCodec<QuiverUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, QuiverUpgradeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
