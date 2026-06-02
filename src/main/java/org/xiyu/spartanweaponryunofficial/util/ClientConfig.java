package org.xiyu.spartanweaponryunofficial.util;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.EnumValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.client.gui.AlignmentHelper.Alignment;

// NeoForge 1.21: Removed @EventBusSubscriber - this is a config class with no event handlers
public class ClientConfig {
    public static final ClientConfig INSTANCE;
    public static final ModConfigSpec CONFIG_SPEC;

    static {
        final Pair<ClientConfig, ModConfigSpec> specPair =
                new ModConfigSpec.Builder().configure(ClientConfig::new);
        INSTANCE = specPair.getLeft();
        CONFIG_SPEC = specPair.getRight();
    }

    // Client settings
    public BooleanValue disableNewCrosshairsCrossbow,
            disableNewCrosshairsThrowingWeapon,
            forceCompatibilityCrosshairs;
    public BooleanValue disableShoulderSurfingIntegration;
    public EnumValue<Alignment> quiverHudAlignment, oilUsesHudAlignment;
    public IntValue quiverHudOffsetX, quiverHudOffsetY, oilUsesHudOffsetX, oilUsesHudOffsetY;
    public BooleanValue forceDisableUncraftableTooltips;

    public ClientConfig(ModConfigSpec.Builder builder) {
        builder.push("general");
        this.forceDisableUncraftableTooltips =
                builder.comment(
                                "Requires game restart! Set to true to remove the uncraftable tooltips for any uncraftable weapon (highlighted in red). Useful for modpack makers who wish to change recipes.|需要重启游戏！设置为 true 可移除所有不可合成武器（红色高亮）的提示，适合需要修改配方的整合包制作者。")
                        .translation(
                                "config."
                                        + ModSpartanWeaponry.ID
                                        + ".client.force_disable_uncraftable_tooltips")
                        .worldRestart()
                        .define("force_disable_uncraftable_tooltips", false);
        builder.pop();
        builder.push("integration");
        this.disableShoulderSurfingIntegration =
                builder.comment(
                                "Set to true to fully disable integration with Shoulder Surfing Reloaded|设为 true 以完全禁用与 Shoulder Surfing Reloaded 的联动")
                        .translation(
                                "config."
                                        + ModSpartanWeaponry.ID
                                        + ".client.disable_shoulder_surfing_integration")
                        .define("disable_shoulder_surfing_integration", false);
        builder.pop();
        builder.push("hud");
        this.disableNewCrosshairsCrossbow =
                builder.comment(
                                "Set to true to disable a new Crosshair for the Crossbow which visually shows inaccuracy, using the default Crosshair instead; false otherwise|设为 true 禁用弩的新版准星（显示散布），改用默认准星")
                        .translation(
                                "config."
                                        + ModSpartanWeaponry.ID
                                        + ".client.disable_new_crosshairs_crossbow")
                        .define("disable_new_crosshairs_crossbow", false);
        this.disableNewCrosshairsThrowingWeapon =
                builder.comment(
                                "Set to true to disable a new Crosshair for Throwing Weapons which show the charge for them, using the default Crosshair instead; false otherwise|设为 true 禁用投掷武器的新版准星（显示蓄力），改用默认准星")
                        .translation(
                                "config."
                                        + ModSpartanWeaponry.ID
                                        + ".client.disable_new_crosshairs_throwing_weapons")
                        .define("disable_new_crosshairs_throwing_weapons", false);
        this.forceCompatibilityCrosshairs =
                builder.comment(
                                "Set to force compatibility crosshairs for Crosshairs and Throwing Weapons. This won't work if the new crosshairs are disabled|设为 true 强制使用兼容准星（十字准星与投掷武器）。若已禁用新版准星则无效")
                        .translation(
                                "config."
                                        + ModSpartanWeaponry.ID
                                        + ".client.force_compatibility_crosshairs")
                        .define("force_compatibility_crosshairs", false);
        builder.push("quiver");
        this.quiverHudAlignment =
                builder.comment(
                                "Sets where the Quiver HUD Element should be aligned|设置箭袋 HUD 的对齐位置")
                        .translation(
                                "config." + ModSpartanWeaponry.ID + ".client.quiver_hud_alignment")
                        .defineEnum("quiver_hud_alignment", Alignment.BOTTOM_CENTER);
        this.quiverHudOffsetX =
                builder.comment(
                                "Sets where on the X-axis the Quiver HUD element should be off-set from it's alignment point|设置箭袋 HUD 在 X 轴相对对齐点的偏移")
                        .translation(
                                "config." + ModSpartanWeaponry.ID + ".client.quiver_hud_offset_x")
                        .defineInRange(
                                "quiver_hud_offset_x", Defaults.DefaultQuiverHudOffsetX, -400, 400);
        this.quiverHudOffsetY =
                builder.comment(
                                "Sets where on the Y-axis the Quiver HUD element should be off-set from it's alignment point|设置箭袋 HUD 在 Y 轴相对对齐点的偏移")
                        .translation(
                                "config." + ModSpartanWeaponry.ID + ".client.quiver_hud_offset_y")
                        .defineInRange(
                                "quiver_hud_offset_y", Defaults.DefaultQuiverHudOffsetY, -400, 400);
        builder.pop();
        builder.push("oil_uses");
        this.oilUsesHudAlignment =
                builder.comment(
                                "Sets where the Oil Uses HUD Element should be aligned|设置油使用次数 HUD 的对齐位置")
                        .translation(
                                "config."
                                        + ModSpartanWeaponry.ID
                                        + ".client.oil_uses_hud_alignment")
                        .defineEnum("oil_uses_alignment", Alignment.CENTER);
        this.oilUsesHudOffsetX =
                builder.comment(
                                "Sets where on the X-axis the Oil Uses HUD element should be off-set from it's alignment point|设置油使用次数 HUD 在 X 轴相对对齐点的偏移")
                        .translation(
                                "config." + ModSpartanWeaponry.ID + ".client.oil_uses_hud_offset_x")
                        .defineInRange(
                                "oil_uses_hud_offset_x",
                                Defaults.DefaultOilUsesHudOffsetX,
                                -400,
                                400);
        this.oilUsesHudOffsetY =
                builder.comment(
                                "Sets where on the Y-axis the Oil Uses HUD element should be off-set from it's alignment point|设置油使用次数 HUD 在 Y 轴相对对齐点的偏移")
                        .translation(
                                "config." + ModSpartanWeaponry.ID + ".client.oil_uses_offset_y")
                        .defineInRange(
                                "oil_uses_hud_offset_y",
                                Defaults.DefaultOilUsesHudOffsetY,
                                -400,
                                400);
        builder.pop();
        builder.pop();
    }
}
