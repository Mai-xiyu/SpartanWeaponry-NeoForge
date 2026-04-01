package org.xiyu.spartanweaponryunofficial.client.color;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

public record OilTintSource() implements ItemTintSource {
    public static final MapCodec<OilTintSource> MAP_CODEC = MapCodec.unit(OilTintSource::new);

    @Override
    public int calculate(ItemStack stack, ClientLevel level, LivingEntity entity) {
        OilEffect oilEffect = OilHelper.getOilFromStack(stack);
        return 0xFF000000 | oilEffect.getColor(stack);
    }

    @Override
    public MapCodec<OilTintSource> type() {
        return MAP_CODEC;
    }
}
