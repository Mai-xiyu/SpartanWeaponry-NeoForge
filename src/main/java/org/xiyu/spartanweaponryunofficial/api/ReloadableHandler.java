package org.xiyu.spartanweaponryunofficial.api;

import java.util.ArrayList;
import java.util.List;
import org.xiyu.spartanweaponryunofficial.util.WeaponArchetype;

/**
 * Handler for reloading values when tags get updated. This gets added to and used automatically.
 *
 * @author ObliviousSpartan
 */
public class ReloadableHandler {
    private static final List<WeaponMaterial> materialReloadList = new ArrayList<>(100);
    private static final List<IReloadable> itemReloadList = new ArrayList<>(768);
    private static volatile boolean tagsLoaded;

    public static void addToMaterialReloadList(WeaponMaterial material) {
        materialReloadList.add(material);
    }

    public static void addToItemReloadList(IReloadable item) {
        itemReloadList.add(item);
    }

    public static List<WeaponMaterial> getMaterialReloadList() {
        return materialReloadList;
    }

    public static List<IReloadable> getItemReloadList() {
        return itemReloadList;
    }

    public static boolean areTagsLoaded() {
        return tagsLoaded;
    }

    public static void markTagsLoaded() {
        tagsLoaded = true;
    }

    public static void reloadAll() {
        materialReloadList.forEach(WeaponMaterial::reload);
        WeaponArchetype.ALL_ARCHETYPES.forEach(WeaponArchetype::reload);
        itemReloadList.forEach(IReloadable::reload);
    }
}
