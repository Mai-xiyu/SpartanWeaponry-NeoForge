package org.xiyu.spartanweaponryunofficial.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.OilEffects;
import org.xiyu.spartanweaponryunofficial.api.oil.OilEffect;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;
import org.xiyu.spartanweaponryunofficial.util.OilHelper;

import java.util.Optional;

public class OilHandler implements IOilHandler {
    public static final String NBT_OIL = "Oil";
    public static final String NBT_OIL_EFFECT = "Effect";
    public static final String NBT_POTION = "Potion";
    public static final String NBT_USES_LEFT = "UsesLeft";

    private final ItemStack stack;
    private Optional<OilEffect> effect;
    private Optional<Potion> potion;
    private int usesLeft;

    public OilHandler(ItemStack stackIn) {
        this.stack = stackIn;
        this.effect = Optional.empty();
        this.potion = Optional.empty();
        this.usesLeft = 0;
        // Load the values contained in the NBT if available
        if (ItemStackDataHelper.getTag(this.stack).contains(NBT_OIL))
            this.deserializeNBT(getRegistryAccess(), ItemStackDataHelper.getTag(this.stack).getCompound(NBT_OIL));
    }

    @Override
    public Optional<OilEffect> getEffect() {
        return this.effect;
    }

    @Override
    public Optional<Potion> getPotion() {
        return this.potion;
    }

    @Override
    public void setEffect(OilEffect effectIn, ItemStack oilStackIn) {
        this.effect = Optional.of(effectIn);
        this.usesLeft = effectIn.getMaxUses();
        this.potion = effectIn == OilEffects.POTION.get() ? Optional.ofNullable(OilHelper.getPotionFromStack(oilStackIn)) : Optional.empty();
        ItemStackDataHelper.updateTag(this.stack, tag -> tag.put(NBT_OIL, this.serializeNBT(getRegistryAccess())));
    }

    @Override
    public void setPotion(Potion potionIn, ItemStack oilStackIn) {
        this.effect = Optional.of(OilEffects.POTION.get());
        this.usesLeft = OilEffects.POTION.get().getMaxUses();
        this.potion = Optional.of(potionIn);
        ItemStackDataHelper.updateTag(this.stack, tag -> tag.put(NBT_OIL, this.serializeNBT(getRegistryAccess())));
    }

    @Override
    public void clearEffect() {
        this.effect = Optional.empty();
        this.usesLeft = 0;
        ItemStackDataHelper.updateTag(this.stack, tag -> tag.remove(NBT_OIL));
    }

    @Override
    public float useEffect(float baseDamageIn, Level levelIn, LivingEntity targetIn, LivingEntity userIn, ItemStack userWeaponIn) {
        if (this.effect.isEmpty() || this.potion.isEmpty()) return 0;//TODO::Should be 0?
        OilEffect oilEffect = this.effect.get();
        ItemStack oilStack = oilEffect == OilEffects.POTION.get() ? OilHelper.makePotionOilStack(this.potion.get()) : OilHelper.makeOilStack(oilEffect);
        float resultDamage = oilEffect.onUse(baseDamageIn, levelIn, targetIn, userIn, oilStack);

        this.usesLeft--;
        ItemStackDataHelper.updateTag(this.stack, tag -> tag.put(NBT_OIL, this.serializeNBT(getRegistryAccess())));

        if (this.usesLeft <= 0) {
            if (userIn instanceof Player)
                ((Player) userIn).displayClientMessage(Component.translatable("message." + ModSpartanWeaponry.ID + ".oil_depleted", oilStack.getHoverName(), userWeaponIn.getHoverName()), true);
            this.clearEffect();
        }
        return resultDamage;
    }

    @Override
    public boolean isOiled() {
        return this.effect.isPresent() && this.effect.get() != OilEffects.NONE.get();
    }

    @Override
    public int getUsesLeft() {
        return this.usesLeft;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        Registry<OilEffect> registry = getOilRegistry();
        if (registry != null && this.effect.isPresent()) {
            if (this.potion.isPresent()) {
                ResourceLocation potionLoc = BuiltInRegistries.POTION.getKey(this.potion.get());
                nbt.putString(NBT_POTION, potionLoc.toString());
            }
            ResourceLocation loc = registry.getKey(this.effect.get());
            nbt.putString(NBT_OIL_EFFECT, loc.toString());
            nbt.putInt(NBT_USES_LEFT, this.usesLeft);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        String oilEffectReg = nbt.getString(NBT_OIL_EFFECT);
        Registry<OilEffect> registry = getOilRegistry();
        if (registry != null) {
            this.effect = !oilEffectReg.isEmpty() ? Optional.ofNullable(registry.get(ResourceLocation.parse(oilEffectReg))) : Optional.empty();
            if (nbt.contains(NBT_POTION)) {
                String potionReg = nbt.getString(NBT_POTION);
                this.potion = !potionReg.isEmpty() ? Optional.ofNullable(BuiltInRegistries.POTION.get(ResourceLocation.parse(potionReg))) : Optional.empty();
            }
        }
        this.usesLeft = nbt.getInt(NBT_USES_LEFT);
    }

    @SuppressWarnings("unchecked")
    private static Registry<OilEffect> getOilRegistry() {
        return (Registry<OilEffect>) BuiltInRegistries.REGISTRY.get(OilEffects.REGISTRY_KEY.location());
    }

    private static HolderLookup.Provider getRegistryAccess() {
        return net.minecraft.core.RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }
}
