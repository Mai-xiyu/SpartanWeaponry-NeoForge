package org.xiyu.spartanweaponryunofficial.gametest;

import java.lang.reflect.Method;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.api.tags.ModDamageTypeTags;
import org.xiyu.spartanweaponryunofficial.init.ModEnchantments;
import org.xiyu.spartanweaponryunofficial.init.ModItems;
import org.xiyu.spartanweaponryunofficial.item.SwordBaseItem;
import org.xiyu.spartanweaponryunofficial.item.ThrowingWeaponItem;
import org.xiyu.spartanweaponryunofficial.util.Config;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

@GameTestHolder(ModSpartanWeaponry.ID)
@PrefixGameTestTemplate(false)
public final class WeaponBehaviorGameTests {
    private WeaponBehaviorGameTests() {}

    @GameTest(templateNamespace = ModSpartanWeaponry.ID, template = "empty")
    public static void heavy_crossbow_arrows_pierce_armor(GameTestHelper helper) {
        Player shooter = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack crossbow = ModItems.HEAVY_CROSSBOWS.netherite.get().getDefaultInstance();
        float boltDamage =
                hitArmoredTarget(
                        helper,
                        ModItems.BOLT
                                .get()
                                .createBolt(
                                        helper.getLevel(),
                                        ModItems.BOLT.get().getDefaultInstance(),
                                        shooter,
                                        crossbow),
                        true);
        // These factories are also used by HeavyCrossbowItem.spawnProjectile. Keep vanilla and
        // Spartan projectiles so the damage-source hook cannot accidentally cover only one class.
        for (Item ammo :
                List.of(
                        Items.ARROW,
                        Items.TIPPED_ARROW,
                        Items.SPECTRAL_ARROW,
                        ModItems.COPPER_ARROW.get(),
                        ModItems.DIAMOND_ARROW.get(),
                        ModItems.NETHERITE_ARROW.get(),
                        ModItems.TIPPED_NETHERITE_ARROW.get())) {
            ArrowItem arrowItem = (ArrowItem) ammo;
            AbstractArrow arrow =
                    arrowItem.createArrow(
                            helper.getLevel(), ammo.getDefaultInstance(), shooter, crossbow);
            // The firing weapon must survive a save/reload, rather than relying on transient flags.
            CompoundTag saved = arrow.saveWithoutId(new CompoundTag());
            AbstractArrow restored = (AbstractArrow) arrow.getType().create(helper.getLevel());
            helper.assertTrue(restored != null, "Unable to reload " + ammo);
            restored.load(saved);
            float damage = hitArmoredTarget(helper, restored, true);
            helper.assertTrue(
                    Math.abs(damage - boltDamage) < 0.001f,
                    ammo + " must have the same armor penetration as bolts");
        }
        for (Item weapon : List.of(Items.BOW, Items.CROSSBOW, ModItems.LONGBOWS.netherite.get())) {
            float damage =
                    hitArmoredTarget(
                            helper,
                            ((ArrowItem) Items.ARROW)
                                    .createArrow(
                                            helper.getLevel(),
                                            Items.ARROW.getDefaultInstance(),
                                            shooter,
                                            weapon.getDefaultInstance()),
                            false);
            double fraction = Config.INSTANCE.armorPiercePercentage.get() / 100.0;
            if (fraction > 0.0)
                helper.assertTrue(
                        damage < boltDamage,
                        "Ordinary bows/crossbows must not gain heavy-crossbow armor penetration");
        }
        helper.succeed();
    }

    static float hitArmoredTarget(
            GameTestHelper helper, AbstractArrow projectile, boolean armorPiercing) {
        Husk target = helper.spawnWithNoFreeWill(EntityType.HUSK, 1, 1, 1);
        for (EquipmentSlot slot : EquipmentSlot.values()) target.setItemSlot(slot, ItemStack.EMPTY);
        target.getAttribute(Attributes.ARMOR).setBaseValue(20.0);
        target.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(0.0);
        projectile.setBaseDamage(8.0);
        projectile.setCritArrow(false);
        projectile.setDeltaMovement(new Vec3(1.0, 0.0, 0.0));
        float health = target.getHealth();
        try {
            Method hit =
                    AbstractArrow.class.getDeclaredMethod("onHitEntity", EntityHitResult.class);
            hit.setAccessible(true);
            hit.invoke(projectile, new EntityHitResult(target));
            helper.assertTrue(
                    target.getLastDamageSource() != null, "Projectile failed to hurt target");
            helper.assertTrue(
                    target.getLastDamageSource().is(ModDamageTypeTags.IS_ARMOR_PIERCING)
                            == armorPiercing,
                    "Unexpected armor-piercing damage type for " + projectile.getType());
            float damage = health - target.getHealth();
            // Vanilla armor formula for damage d, armor 20, toughness 0: d * (1 - (20 - d/2)/25).
            double fraction =
                    armorPiercing ? Config.INSTANCE.armorPiercePercentage.get() / 100.0 : 0.0;
            double regular = 8.0 * (1.0 - fraction);
            double expected = 8.0 * fraction + regular * (1.0 - (20.0 - regular / 2.0) / 25.0);
            helper.assertTrue(
                    Math.abs(damage - expected) < 0.001,
                    "Armor damage was " + damage + "; expected " + expected);
            return damage;
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Unable to invoke projectile impact", exception);
        } finally {
            target.discard();
            projectile.discard();
        }
    }

    @GameTest(templateNamespace = ModSpartanWeaponry.ID, template = "empty")
    public static void incendiary_and_hydrodynamic_are_exclusive(GameTestHelper helper) {
        var registry = helper.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var incendiary = registry.getHolderOrThrow(ModEnchantments.INCENDIARY);
        var hydrodynamic = registry.getHolderOrThrow(ModEnchantments.HYDRODYNAMIC);
        helper.assertFalse(
                Enchantment.areCompatible(incendiary, hydrodynamic),
                "Incendiary must reject Hydrodynamic");
        helper.assertFalse(
                Enchantment.areCompatible(hydrodynamic, incendiary),
                "Hydrodynamic must reject Incendiary");
        var unbreaking = registry.getHolderOrThrow(Enchantments.UNBREAKING);
        helper.assertTrue(
                Enchantment.areCompatible(incendiary, unbreaking)
                        && Enchantment.areCompatible(hydrodynamic, unbreaking),
                "Unrelated enchantments must remain compatible");
        helper.succeed();
    }

    @GameTest(templateNamespace = ModSpartanWeaponry.ID, template = "empty")
    public static void spears_have_reach_one(GameTestHelper helper) {
        for (var entry : ModItems.SPEARS.getAsList()) {
            // All materials must resolve the loaded archetype tag and its actual item modifiers.
            ItemStack stack = entry.getDefaultInstance();
            SwordBaseItem spear = (SwordBaseItem) stack.getItem();
            helper.assertTrue(
                    spear.hasWeaponTrait(WeaponTraits.REACH_1.get()), "Missing Reach I: " + spear);
            helper.assertFalse(
                    spear.hasWeaponTrait(WeaponTraits.REACH_2.get()),
                    "Unexpected Reach II: " + spear);
            double bonus =
                    spear.getDefaultAttributeModifiers(stack).modifiers().stream()
                            .filter(
                                    modifier ->
                                            modifier.attribute()
                                                    .equals(Attributes.ENTITY_INTERACTION_RANGE))
                            .mapToDouble(modifier -> modifier.modifier().amount())
                            .sum();
            helper.assertTrue(
                    Math.abs(bonus - (Config.INSTANCE.reach1Value.get() - 5.0)) < 0.001,
                    "Incorrect reach modifier for " + spear);
        }
        helper.succeed();
    }

    @GameTest(templateNamespace = ModSpartanWeaponry.ID, template = "empty")
    public static void throwing_weapon_normalization_handles_expanse(GameTestHelper helper) {
        ThrowingWeaponItem weapon = ModItems.THROWING_KNIVES.iron.get();
        ItemStack stack = weapon.getDefaultInstance();
        var level = helper.getLevel();
        var enchantments = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        stack.enchant(enchantments.getHolderOrThrow(ModEnchantments.EXPANSE), 1);
        int expandedAmmoUsed = weapon.getMaxAmmo(stack, level);
        helper.assertTrue(
                expandedAmmoUsed > weapon.getMaxAmmoBase(), "Expanse must increase capacity");
        ItemStackDataHelper.updateTag(
                stack, tag -> tag.putInt(ThrowingWeaponItem.NBT_AMMO_USED, expandedAmmoUsed));

        weapon.normalizeStackState(stack, null, false);

        int normalizedAmmoUsed =
                ItemStackDataHelper.getTag(stack).getInt(ThrowingWeaponItem.NBT_AMMO_USED);
        helper.assertTrue(
                normalizedAmmoUsed == expandedAmmoUsed,
                "Normalization without registry access must preserve expanded ammo usage");

        weapon.normalizeStackState(stack, level, false);
        helper.assertTrue(
                ItemStackDataHelper.getTag(stack).getInt(ThrowingWeaponItem.NBT_AMMO_USED)
                        == expandedAmmoUsed,
                "Normalization must respect the capacity granted by Expanse");

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        GrindstoneMenu grindstone = new GrindstoneMenu(0, player.getInventory());
        grindstone.getSlot(0).set(stack);
        ItemStack disenchanted = grindstone.getSlot(2).getItem();
        helper.assertTrue(
                disenchanted.is(weapon)
                        && weapon.getMaxAmmo(disenchanted, level) == weapon.getMaxAmmoBase(),
                "The grindstone must remove Expanse and restore base capacity");
        weapon.normalizeStackState(disenchanted, level, false);
        helper.assertTrue(
                ItemStackDataHelper.getTag(disenchanted).getInt(ThrowingWeaponItem.NBT_AMMO_USED)
                        == weapon.getMaxAmmoBase(),
                "After disenchanting, ammo used must be clamped to base capacity");
        helper.succeed();
    }
}
