package org.xiyu.spartanweaponryunofficial.api.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import org.xiyu.spartanweaponryunofficial.api.SpartanWeaponryAPI;

public class ModDamageTypeTags {
    public static final TagKey<DamageType> IS_ARMOR_PIERCING = create("is_armor_piercing");


    public static TagKey<DamageType> create(Identifier loc) {
        return TagKey.create(Registries.DAMAGE_TYPE, loc);
    }

    public static TagKey<DamageType> create(String namespace, String path) {
        return create(Identifier.tryBuild(namespace, path));
    }

    public static TagKey<DamageType> create(String path) {
        return create(Identifier.tryBuild(SpartanWeaponryAPI.MOD_ID, path));
    }
}
