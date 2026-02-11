package org.xiyu.spartanweaponryunofficial.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;

public class OilCoatingColours {
    public static final ItemColor OIL_COATED_WEAPON = (stack, idx) ->
    {
        if (idx != 100) return 0xFFFFFFFF;
        IOilHandler oilHandler = ModCapabilities.getOilHandler(stack);
        if (oilHandler != null && oilHandler.getEffect().isPresent())
            return oilHandler.isOiled() ? oilHandler.getEffect().get().getColor(stack) : 0x00000000;
        return 0;
    };

    @SuppressWarnings("deprecation")
    public static void reload() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            BuiltInRegistries.ITEM.stream()
                    .filter(item -> item.builtInRegistryHolder().is(ModItemTags.OILABLE_WEAPONS))
                    .forEach(item -> Minecraft.getInstance().getItemColors().register(OIL_COATED_WEAPON, item));
        }
    }

}
