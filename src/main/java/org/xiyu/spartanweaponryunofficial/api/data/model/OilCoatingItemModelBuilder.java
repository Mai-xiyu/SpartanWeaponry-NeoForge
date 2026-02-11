package org.xiyu.spartanweaponryunofficial.api.data.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;

public class OilCoatingItemModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    protected OilCoatingItemModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "oil_coated_item"), parent, existingFileHelper);
    }
}
