package org.xiyu.spartanweaponryunofficial.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.capability.IQuiverItemHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.init.ModRecipeSerializers;
import org.xiyu.spartanweaponryunofficial.item.QuiverBaseItem;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

import java.util.Optional;

public class QuiverUpgradeRecipe extends SmithingTransformRecipe {
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStackTemplate resultTemplate;

    public static final MapCodec<QuiverUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("template").forGetter(QuiverUpgradeRecipe::getTemplate),
            Ingredient.CODEC.fieldOf("base").forGetter(QuiverUpgradeRecipe::getBase),
            Ingredient.CODEC.fieldOf("addition").forGetter(QuiverUpgradeRecipe::getAddition),
            ItemStackTemplate.MAP_CODEC.forGetter(QuiverUpgradeRecipe::getResultTemplate)
    ).apply(instance, QuiverUpgradeRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverUpgradeRecipe> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    public QuiverUpgradeRecipe(Ingredient templateIn, Ingredient baseIn, Ingredient additionIn, ItemStackTemplate resultIn) {
        super(new Recipe.CommonInfo(true), Optional.of(templateIn), baseIn, Optional.of(additionIn), resultIn);
        this.template = templateIn;
        this.base = baseIn;
        this.addition = additionIn;
        this.resultTemplate = resultIn;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SmithingRecipeInput inv) {
        ItemStack origOutputStack = this.resultTemplate.create();
        ItemStack outputStack = super.assemble(inv);
        // Resize the output tag
        IQuiverItemHandler itemHandler = outputStack.getCapability(ModCapabilities.QUIVER_ITEM_CAPABILITY);
        if (itemHandler != null) {
            int size = ItemStackDataHelper.getTag(origOutputStack)
                    .getCompound(QuiverBaseItem.NBT_AMMO)
                    .map(tag -> tag.getIntOr("Size", itemHandler.getSlots()))
                    .orElse(itemHandler.getSlots());
            itemHandler.resize(size);
        }

        return outputStack;
    }

    @Override
    public @NotNull RecipeSerializer<SmithingTransformRecipe> getSerializer() {
        return (RecipeSerializer<SmithingTransformRecipe>) (RecipeSerializer<?>) ModRecipeSerializers.QUIVER_UPGRADE_SMITHING.get();
    }

    @Override
    public @NotNull RecipeType<SmithingRecipe> getType() {
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

    public ItemStackTemplate getResultTemplate() {
        return this.resultTemplate;
    }
}