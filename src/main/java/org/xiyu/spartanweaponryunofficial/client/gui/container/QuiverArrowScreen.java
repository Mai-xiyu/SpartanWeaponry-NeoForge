package org.xiyu.spartanweaponryunofficial.client.gui.container;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverArrowMenu;

public class QuiverArrowScreen extends QuiverBaseScreen<QuiverArrowMenu> {
    public QuiverArrowScreen(QuiverArrowMenu screenContainer, Inventory inv, Component title) {
        super(screenContainer, inv, title);
    }
}
