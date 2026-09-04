package org.xiyu.spartanweaponryunofficial.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModEnchantments;

public class ModEnchantmentTagsProvider extends TagsProvider<Enchantment> {

    public ModEnchantmentTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registry,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(
                output,
                net.minecraft.core.registries.Registries.ENCHANTMENT,
                registry,
                ModSpartanWeaponry.ID,
                existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registry) {
        tag(net.minecraft.tags.EnchantmentTags.IN_ENCHANTING_TABLE)
                .add(
                        ModEnchantments.PROPEL,
                        ModEnchantments.RAZORS_EDGE,
                        ModEnchantments.INCENDIARY,
                        ModEnchantments.LUCKY_THROW,
                        ModEnchantments.HYDRODYNAMIC,
                        ModEnchantments.SUPERCHARGE,
                        ModEnchantments.EXPANSE,
                        ModEnchantments.SHARPSHOOTER,
                        ModEnchantments.COLLECTORANG);

        tag(net.minecraft.tags.EnchantmentTags.TRADEABLE)
                .add(
                        ModEnchantments.PROPEL,
                        ModEnchantments.RAZORS_EDGE,
                        ModEnchantments.INCENDIARY,
                        ModEnchantments.LUCKY_THROW,
                        ModEnchantments.HYDRODYNAMIC,
                        ModEnchantments.SUPERCHARGE,
                        ModEnchantments.EXPANSE,
                        ModEnchantments.SHARPSHOOTER,
                        ModEnchantments.COLLECTORANG);
    }

    @Override
    public @NotNull String getName() {
        return ModSpartanWeaponry.NAME + " Enchantment Tags";
    }
}
