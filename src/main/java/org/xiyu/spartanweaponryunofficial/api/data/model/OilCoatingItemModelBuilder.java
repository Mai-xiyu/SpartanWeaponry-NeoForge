package org.xiyu.spartanweaponryunofficial.api.data.model;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

public class OilCoatingItemModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    protected OilCoatingItemModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "oil_coated_item"), parent, existingFileHelper, false);
    }
}
