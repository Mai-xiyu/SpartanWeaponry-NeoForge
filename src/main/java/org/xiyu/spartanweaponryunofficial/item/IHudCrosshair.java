package org.xiyu.spartanweaponryunofficial.item;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.xiyu.spartanweaponryunofficial.client.gui.ICrosshairOverlay;

public interface IHudCrosshair {
    @OnlyIn(Dist.CLIENT)
    ICrosshairOverlay getCrosshairHudElement();
}
