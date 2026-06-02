package org.xiyu.spartanweaponryunofficial.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.capability.IOilHandler;
import org.xiyu.spartanweaponryunofficial.client.model.OilCoatedItemModel;
import org.xiyu.spartanweaponryunofficial.init.ModCapabilities;
import org.xiyu.spartanweaponryunofficial.util.WeaponOilConfig;

public class OilCoatingColours {
    private static final int COATING_TINT_ALPHA = 0x88000000;
    private static final int RGB_MASK = 0x00FFFFFF;
    private static final int TRANSPARENT_WHITE = 0x00FFFFFF;

    public static final ItemColor OIL_COATED_WEAPON =
            (stack, idx) -> {
                if (idx != OilCoatedItemModel.COATING_TINT_INDEX) return 0xFFFFFFFF;
                if (!WeaponOilConfig.isEnabled()) return TRANSPARENT_WHITE;

                IOilHandler oilHandler = stack.getCapability(ModCapabilities.OIL_CAPABILITY);
                if (oilHandler == null || !oilHandler.isOiled() || oilHandler.getEffect().isEmpty())
                    return TRANSPARENT_WHITE;

                return COATING_TINT_ALPHA
                        | (oilHandler.getEffect().get().getColor(stack) & RGB_MASK);
            };

    @SuppressWarnings("deprecation")
    public static void reload() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            BuiltInRegistries.ITEM.stream()
                    .filter(item -> item.builtInRegistryHolder().is(ModItemTags.OILABLE_WEAPONS))
                    .forEach(
                            item ->
                                    Minecraft.getInstance()
                                            .getItemColors()
                                            .register(OIL_COATED_WEAPON, item));
        }
    }
}
