package org.xiyu.spartanweaponryunofficial.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.skull.PiglinHeadModel;
import net.minecraft.client.model.object.skull.SkullModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
// TODO: ItemProperties removed in 26.1 - item models are now data-driven
// import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.ModToolActions;
import org.xiyu.spartanweaponryunofficial.api.ModelOverrides;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.block.ExtendedSkullBlock;
import org.xiyu.spartanweaponryunofficial.capability.CuriosHelper;
import org.xiyu.spartanweaponryunofficial.client.color.OilTintSource;
import org.xiyu.spartanweaponryunofficial.client.gui.HudCrosshair;
import org.xiyu.spartanweaponryunofficial.client.gui.HudLoadState;
import org.xiyu.spartanweaponryunofficial.client.gui.HudOilUses;
import org.xiyu.spartanweaponryunofficial.client.gui.HudQuiverAmmo;
import org.xiyu.spartanweaponryunofficial.client.gui.container.QuiverArrowScreen;
import org.xiyu.spartanweaponryunofficial.client.gui.container.QuiverBoltScreen;
import org.xiyu.spartanweaponryunofficial.client.inventory.ClientOilCoatingTooltip;
import org.xiyu.spartanweaponryunofficial.client.inventory.ClientQuiverTooltip;
import org.xiyu.spartanweaponryunofficial.client.model.*;
import org.xiyu.spartanweaponryunofficial.client.renderer.entity.*;
import org.xiyu.spartanweaponryunofficial.init.ModBlockEntities;
import org.xiyu.spartanweaponryunofficial.init.ModEntities;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.init.ModMenus;
import org.xiyu.spartanweaponryunofficial.inventory.tooltip.OilCoatingTooltip;
import org.xiyu.spartanweaponryunofficial.inventory.tooltip.QuiverTooltip;
import org.xiyu.spartanweaponryunofficial.item.*;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.Log;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

@EventBusSubscriber(modid = ModSpartanWeaponry.ID, value = Dist.CLIENT)
public class ClientHelper {

    // Holds the last opened config screen instance for Discord button injection
    public static net.minecraft.client.gui.screens.Screen lastConfigScreen = null;

    /**
     * Registers the NeoForge ConfigurationScreen extension point (client-only).
     * Called from ModSpartanWeaponry constructor behind a dist check.
     */
    public static void registerConfigScreen(ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) -> {
            ConfigurationScreen screen = new ConfigurationScreen(container, parent);
            lastConfigScreen = screen;
            return screen;
        });
    }

    public static final GuiLayer LOAD_STATE = HudLoadState::render;
    public static final GuiLayer QUIVER_AMMO = HudQuiverAmmo::render;
    public static final GuiLayer OIL_USES = HudOilUses::render;
    public static final GuiLayer NEW_CROSSHAIR = HudCrosshair::render;

    @SubscribeEvent
    public static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources ev) {
        ev.register(Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "oil"), OilTintSource.MAP_CODEC);
    }

    public static void registerCurioRenders() {
        if (CuriosHelper.LOADED) CurioRenderer.register();
    }

    /* TODO: ItemProperties removed in 26.1 - item models are now data-driven
    public static void registerMeleeWeaponPropertyOverrides(SwordBaseItem meleeWeapon) {
        ItemProperties.register(meleeWeapon, ModelOverrides.BLOCKING, (stack, world, living, value) ->
                meleeWeapon.canPerformAction(stack, ModToolActions.MELEE_BLOCK) && living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
        Item weapon = meleeWeapon.getAsItem();
        ItemProperties.register(weapon, ModelOverrides.THROWING, (stack, world, living, value) ->
        {
            if (living == null || !meleeWeapon.hasWeaponTrait(WeaponTraits.THROWABLE.get()) || !stack.is(living.getUseItem().getItem()))
                return 0.0f;
            return living.getTicksUsingItem() > 0 ? 1.0f : 0.0f;
        });
    }

    public static void registerHeavyCrossbowPropertyOverrides(HeavyCrossbowItem crossbow) {
        ItemProperties.register(crossbow, ModelOverrides.PULL, (stack, world, living, value) ->
        {
            if (living != null //&& stack.getItem() == crossbow
            )
                return crossbow.isLoaded(stack) ? 0.0f : (float) (crossbow.getLoadingTicks(stack, living)) / crossbow.getFullLoadTicks(stack, world);
            return 0.0f;
        });
        ItemProperties.register(crossbow, ModelOverrides.PULLING, (stack, world, living, value) ->
                living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
        ItemProperties.register(crossbow, ModelOverrides.CHARGED, (stack, world, living, value) ->
                crossbow.isLoaded(stack) ? 1.0f : 0.0f);
    }

    public static void registerLongbowPropertyOverrides(LongbowItem longbow) {
        ItemProperties.register(longbow, ModelOverrides.PULLING, (stack, world, living, value) ->
                living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
        ItemProperties.register(longbow, ModelOverrides.PULL, (stack, world, shooter, value) ->
                shooter != null && shooter.getUseItem() == stack ? longbow.getNockProgress(stack, shooter) : 0.0f);
    }

    public static void registerThrowingWeaponPropertyOverrides(ThrowingWeaponItem throwingWeapon) {
//		Log.debug("Registering Throwing Weapon Property Overrides for item: \"" + throwingWeapon.getRegistryName().toString() + "\"");
        ItemProperties.register(throwingWeapon, ModelOverrides.THROWING, (stack, world, living, value) ->
        {
            if (living == null || !stack.is(living.getUseItem().getItem())) return 0.0f;
            return living.getTicksUsingItem() > 0 ? 1.0f : 0.0f;
        });
        ItemProperties.register(throwingWeapon, ModelOverrides.EMPTY, (stack, world, living, value) ->
        {
            Level level = world != null ? world : Minecraft.getInstance().level;
            return ItemStackDataHelper.getTag(stack).getInt(ThrowingWeaponItem.NBT_AMMO_USED) == throwingWeapon.getMaxAmmo(stack, level) ? 1 : 0;
        });
    }

    public static void registerQuiverPropertyOverrides(QuiverBaseItem quiver) {
        ItemProperties.register(quiver, ModelOverrides.ARROW, (stack, world, living, value) ->
                quiver.getAmmoCount(stack));
    }
    */
	
/*	public static void registerMeleeWeaponBlockingPropertyOverrides(Item meleeWeapon)
	{
		ItemProperties.register(meleeWeapon, Const.MODEL, (stack, world, living, value) ->
		{
			return GearData.getModelIndex(stack);
		});
		ItemProperties.register(meleeWeapon, Const.BROKEN_PROPERTY, (stack, world, living, value) ->
		{
			return GearHelper.isBroken(stack) ? 0 : 1;
		});
		ItemProperties.register(meleeWeapon, ModelOverrides.BLOCKING, (stack, world, living, value) ->
		{
			return meleeWeapon.canPerformAction(stack, ModToolActions.MELEE_BLOCK) && living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f;
		});
	}*/

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers ev) {
        Log.info("Registering Entity Renderers!");
        ev.registerEntityRenderer(ModEntities.ARROW_SW.get(), ArrowBaseRenderer::new);
        ev.registerEntityRenderer(ModEntities.ARROW_EXPLOSIVE.get(), (rendererProvider) ->
                new SimpleArrowRenderer<>(rendererProvider, Identifier.tryBuild(ModSpartanWeaponry.ID, "textures/entity/projectiles/explosive_arrow.png")));
        ev.registerEntityRenderer(ModEntities.BOLT.get(), BoltRenderer::new);
        ev.registerEntityRenderer(ModEntities.BOLT_SPECTRAL.get(), BoltRenderer::new);
        ev.registerEntityRenderer(ModEntities.THROWING_WEAPON.get(), ThrowingWeaponRenderer::new);
        ev.registerEntityRenderer(ModEntities.THROWING_KNIFE.get(), ThrowingWeaponRenderer::new);
        ev.registerEntityRenderer(ModEntities.TOMAHAWK.get(), TomahawkRenderer::new);
        ev.registerEntityRenderer(ModEntities.JAVELIN.get(), JavelinRenderer::new);
        ev.registerEntityRenderer(ModEntities.BOOMERANG.get(), BoomerangRenderer::new);
        ev.registerEntityRenderer(ModEntities.DYNAMITE.get(), ThrownItemRenderer::new);

        ev.registerBlockEntityRenderer(ModBlockEntities.EXTENDED_SKULL_TYPE.get(), SkullBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerModelLayers(EntityRenderersEvent.RegisterLayerDefinitions ev) {
        Log.info("Registering Model Layers!");
        ev.registerLayerDefinition(ModelLayers.SMALL_ARROW_QUIVER, SmallArrowQuiverModel::createLayer);
        ev.registerLayerDefinition(ModelLayers.MEDIUM_ARROW_QUIVER, MediumArrowQuiverModel::createLayer);
        ev.registerLayerDefinition(ModelLayers.LARGE_ARROW_QUIVER, LargeArrowQuiverModel::createLayer);

        ev.registerLayerDefinition(ModelLayers.SMALL_BOLT_QUIVER, SmallBoltQuiverModel::createLayer);
        ev.registerLayerDefinition(ModelLayers.MEDIUM_BOLT_QUIVER, MediumBoltQuiverModel::createLayer);
        ev.registerLayerDefinition(ModelLayers.LARGE_BOLT_QUIVER, LargeBoltQuiverModel::createLayer);

        ev.registerLayerDefinition(ModelLayers.BLAZE_HEAD, ExtendedSkullHelper::createBlazeLayer);
        ev.registerLayerDefinition(ModelLayers.ENDERMAN_HEAD, EndermanHeadModel::createLayer);
        ev.registerLayerDefinition(ModelLayers.SPIDER_HEAD, ExtendedSkullHelper::createSpiderLayer);
        ev.registerLayerDefinition(ModelLayers.CAVE_SPIDER_HEAD, ExtendedSkullHelper::createSpiderLayer);
        ev.registerLayerDefinition(ModelLayers.ZOMBIFIED_PIGLIN_HEAD, () -> LayerDefinition.create(PiglinHeadModel.createHeadModel(), 64, 64));
        ev.registerLayerDefinition(ModelLayers.HUSK_HEAD, ExtendedSkullHelper::createHuskLayer);
        ev.registerLayerDefinition(ModelLayers.STRAY_SKULL, ExtendedSkullHelper::createHeadWithHatLayer);
        ev.registerLayerDefinition(ModelLayers.DROWNED_HEAD, ExtendedSkullHelper::createHeadWithHatLayer);
        ev.registerLayerDefinition(ModelLayers.ILLAGER_HEAD, IllagerHeadModel::createLayer);
        ev.registerLayerDefinition(ModelLayers.WITCH_HEAD, WitchHeadModel::createLayer);

        Log.info("Model Layer registration complete!");
    }

    @SubscribeEvent
    public static void registerSkullModels(EntityRenderersEvent.CreateSkullModels ev) {
        // In 26.1, registerSkullModel takes (Type, ModelLayerLocation, texture) for default SkullModel,
        // or (Type, ModelLayerLocation, Function<ModelPart, SkullModelBase>, texture) for custom models.
        // getEntityModelSet() and SkullBlockRenderer.SKIN_BY_TYPE are removed.
        ev.registerSkullModel(ExtendedSkullBlock.Types.BLAZE, ModelLayers.BLAZE_HEAD, Identifier.tryBuild("minecraft", "textures/entity/blaze/blaze.png"));
        ev.registerSkullModel(ExtendedSkullBlock.Types.ENDERMAN, ModelLayers.ENDERMAN_HEAD, EndermanHeadModel::new, Identifier.tryBuild(ModSpartanWeaponry.ID, "textures/entity/skull/enderman_head.png"));
        ev.registerSkullModel(ExtendedSkullBlock.Types.SPIDER, ModelLayers.SPIDER_HEAD, Identifier.tryBuild("minecraft", "textures/entity/spider/spider.png"));
        ev.registerSkullModel(ExtendedSkullBlock.Types.CAVE_SPIDER, ModelLayers.CAVE_SPIDER_HEAD, Identifier.tryBuild("minecraft", "textures/entity/spider/cave_spider.png"));
        ev.registerSkullModel(ExtendedSkullBlock.Types.ZOMBIE_PIGLIN, ModelLayers.ZOMBIFIED_PIGLIN_HEAD, PiglinHeadModel::new, Identifier.tryBuild("minecraft", "textures/entity/piglin/zombified_piglin.png"));
        ev.registerSkullModel(ExtendedSkullBlock.Types.HUSK, ModelLayers.HUSK_HEAD, Identifier.fromNamespaceAndPath("minecraft", "textures/entity/zombie/husk.png"));
        ev.registerSkullModel(ExtendedSkullBlock.Types.STRAY, ModelLayers.STRAY_SKULL, Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/entity/skull/stray_skull.png"));
        ev.registerSkullModel(ExtendedSkullBlock.Types.DROWNED, ModelLayers.DROWNED_HEAD, Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/entity/skull/drowned_head.png"));
        ev.registerSkullModel(ExtendedSkullBlock.Types.ILLAGER, ModelLayers.ILLAGER_HEAD, IllagerHeadModel::new, Identifier.fromNamespaceAndPath("minecraft", "textures/entity/illager/pillager.png"));
        ev.registerSkullModel(ExtendedSkullBlock.Types.WITCH, ModelLayers.WITCH_HEAD, WitchHeadModel::new, Identifier.fromNamespaceAndPath("minecraft", "textures/entity/witch/witch.png"));
    }

    // registerSkullTextures() removed - textures are now registered via registerSkullModel() in 26.1

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent ev) {
        ev.register(ModMenus.QUIVER_ARROW.get(), QuiverArrowScreen::new);
        ev.register(ModMenus.QUIVER_BOLT.get(), QuiverBoltScreen::new);
    }

    @SubscribeEvent
    public static void registerHudOverlays(RegisterGuiLayersEvent ev) {
        ev.registerAboveAll(Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "load_state"), LOAD_STATE);
        ev.registerAboveAll(Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "quiver_ammo"), QUIVER_AMMO);
        ev.registerAboveAll(Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "oil_uses"), OIL_USES);
        ev.registerAbove(VanillaGuiLayers.CROSSHAIR, Identifier.fromNamespaceAndPath(ModSpartanWeaponry.ID, "crosshair"), NEW_CROSSHAIR);
    }

    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent ev) {
        ev.register(QuiverTooltip.class, ClientQuiverTooltip::new);
        ev.register(OilCoatingTooltip.class, ClientOilCoatingTooltip::new);
    }
}
