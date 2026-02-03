package org.xiyu.spartanweaponryunofficial.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.tags.ModDamageTypeTags;
import org.xiyu.spartanweaponryunofficial.init.ModDamageTypes;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<Provider> registry, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registry, ModSpartanWeaponry.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registry) {
        this.tag(DamageTypeTags.IS_PROJECTILE).add(ModDamageTypes.KEY_THROWN_WEAPON_PLAYER, ModDamageTypes.KEY_THROWN_WEAPON_MOB, ModDamageTypes.KEY_ARMOR_PIERCING_BOLT);
        this.tag(ModDamageTypeTags.IS_ARMOR_PIERCING).add(ModDamageTypes.KEY_ARMOR_PIERCING_MELEE, ModDamageTypes.KEY_ARMOR_PIERCING_BOLT);
    }
}
