package org.xiyu.spartanweaponryunofficial.item.crafting;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface ITagCookingRecipe {
    TagKey<Item> getResultTag();
}