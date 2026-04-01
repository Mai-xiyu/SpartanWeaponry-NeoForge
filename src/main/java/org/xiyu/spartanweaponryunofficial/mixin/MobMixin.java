package org.xiyu.spartanweaponryunofficial.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.xiyu.spartanweaponryunofficial.util.ItemRandomizer;

import java.util.List;

@Mixin(Mob.class)
public class MobMixin extends LivingEntityMixin {
    @Shadow
    @Final
    public GoalSelector goalSelector;

    @Shadow
    public void setItemSlot(EquipmentSlot slotIn, ItemStack item) {
        throw new IllegalStateException("Mixin failed to shadow the \"Mob.setItemSlot(...)\" method!");
    }

    @Shadow
    public ItemStack getItemBySlot(EquipmentSlot p_21467_) {
        throw new IllegalStateException("Mixin failed to shadow the \"Mob.getItemBySlot(...)\" method!");
    }

    @Unique
    protected void spartanWeaponry$attemptReplacingMainHandItemRandom(@NotNull TagKey<Item> itemTagIn, DifficultyInstance difficultyIn, boolean disabledIn, float chanceNormalIn, float chanceHardIn) {
        if (!disabledIn) {
            float rand = this.random.nextFloat();
            float chance = difficultyIn.isHard() ? chanceHardIn : chanceNormalIn;

            if (rand > 1 - chance) {
                Level level = this.level();
                var tag = BuiltInRegistries.ITEM.getTagOrEmpty(itemTagIn);
                List<Item> possibleWeapons = new java.util.ArrayList<>();
                for (Holder<Item> holder : tag) {
                    possibleWeapons.add(holder.value());
                }
                if (!possibleWeapons.isEmpty()) {
                    ItemStack weapon = ItemRandomizer.generate(level, possibleWeapons);
                    this.setItemSlot(EquipmentSlot.MAINHAND, weapon);
                }
            }
        }
    }
}
