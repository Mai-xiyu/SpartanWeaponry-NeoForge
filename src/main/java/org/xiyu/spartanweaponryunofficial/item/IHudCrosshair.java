package org.xiyu.spartanweaponryunofficial.item;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.xiyu.spartanweaponryunofficial.client.gui.ICrosshairOverlay;

public interface IHudCrosshair {
    @OnlyIn(Dist.CLIENT)
    ICrosshairOverlay getCrosshairHudElement();
}
