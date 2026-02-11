package org.xiyu.spartanweaponryunofficial.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.client.renderer.entity.ModelLayers;

/**
 * Curios Renderer - disabled for Forge 1.21.1 as Curios doesn't support this version.
 * TODO: Re-enable when Curios adds Forge 1.21.1 support
 */
public class CurioRenderer implements ResourceManagerReloadListener {
    public static final CurioRenderer INSTANCE = new CurioRenderer();

    public static SmallArrowQuiverModel smallArrowQuiverModel;
    protected static final ResourceLocation smallArrowQuiverTexture = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/model/quiver_arrow_small.png");
    public static MediumArrowQuiverModel mediumArrowQuiverModel;
    protected static final ResourceLocation mediumArrowQuiverTexture = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/model/quiver_arrow_medium.png");
    public static LargeArrowQuiverModel largeArrowQuiverModel;
    protected static final ResourceLocation largeArrowQuiverTexture = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/model/quiver_arrow_large.png");
    protected static final ResourceLocation hugeArrowQuiverTexture = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/model/quiver_arrow_huge.png");

    public static SmallBoltQuiverModel smallBoltQuiverModel;
    protected static final ResourceLocation smallBoltQuiverTexture = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/model/quiver_bolt_small.png");
    public static MediumBoltQuiverModel mediumBoltQuiverModel;
    protected static final ResourceLocation mediumBoltQuiverTexture = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/model/quiver_bolt_medium.png");
    public static LargeBoltQuiverModel largeBoltQuiverModel;
    protected static final ResourceLocation largeBoltQuiverTexture = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/model/quiver_bolt_large.png");
    protected static final ResourceLocation hugeBoltQuiverTexture = ResourceLocation.tryBuild(ModSpartanWeaponry.ID, "textures/model/quiver_bolt_huge.png");

    private CurioRenderer() {
    }

    public static void register() {
        // Disabled - Curios not available for Forge 1.21.1
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager p_10758_) {
        Minecraft mc = Minecraft.getInstance();
        smallArrowQuiverModel = new SmallArrowQuiverModel(mc.getEntityModels().bakeLayer(ModelLayers.SMALL_ARROW_QUIVER));
        mediumArrowQuiverModel = new MediumArrowQuiverModel(mc.getEntityModels().bakeLayer(ModelLayers.MEDIUM_ARROW_QUIVER));
        largeArrowQuiverModel = new LargeArrowQuiverModel(mc.getEntityModels().bakeLayer(ModelLayers.LARGE_ARROW_QUIVER));

        smallBoltQuiverModel = new SmallBoltQuiverModel(mc.getEntityModels().bakeLayer(ModelLayers.SMALL_BOLT_QUIVER));
        mediumBoltQuiverModel = new MediumBoltQuiverModel(mc.getEntityModels().bakeLayer(ModelLayers.MEDIUM_BOLT_QUIVER));
        largeBoltQuiverModel = new LargeBoltQuiverModel(mc.getEntityModels().bakeLayer(ModelLayers.LARGE_BOLT_QUIVER));
    }
}
