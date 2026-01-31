package org.xiyu.spartanweaponryunofficial.item;

import org.xiyu.spartanweaponryunofficial.client.gui.ICrosshairOverlay;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IHudCrosshair 
{
	@OnlyIn(Dist.CLIENT)
	public ICrosshairOverlay getCrosshairHudElement();
}
