package org.xiyu.spartanweaponryunofficial.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinition.Sound;
import net.neoforged.neoforge.common.data.SoundDefinition.SoundType;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import org.jetbrains.annotations.NotNull;
import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.init.ModSounds;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {

    public ModSoundDefinitionsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ModSpartanWeaponry.ID, existingFileHelper);
    }

    @Override
    public void registerSounds() {
        this.add(ModSounds.THROWN_WEAPON_THROW, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.thrown_weapon_throw").
                with(Sound.sound(ResourceLocation.parse("item/trident/throw1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/throw2"), SoundType.SOUND)));
        this.add(ModSounds.THROWN_WEAPON_HIT_MOB, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.thrown_weapon_hit_mob").
                with(Sound.sound(ResourceLocation.parse("item/trident/pierce1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce2"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce3"), SoundType.SOUND)));
        this.add(ModSounds.THROWN_WEAPON_HIT_GROUND, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.thrown_weapon_hit_ground").
                with(Sound.sound(ResourceLocation.parse("item/trident/ground_impact1"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact2"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact3"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact4"), SoundType.SOUND).volume(0.9)));

        this.add(ModSounds.THROWING_KNIFE_THROW, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.throwing_knife_throw").
                with(Sound.sound(ResourceLocation.parse("item/trident/throw1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/throw2"), SoundType.SOUND)));
        this.add(ModSounds.THROWING_KNIFE_HIT_MOB, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.throwing_knife_hit_mob").
                with(Sound.sound(ResourceLocation.parse("item/trident/pierce1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce2"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce3"), SoundType.SOUND)));
        this.add(ModSounds.THROWING_KNIFE_HIT_GROUND, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.throwing_knife_hit_ground").
                with(Sound.sound(ResourceLocation.parse("item/trident/ground_impact1"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact2"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact3"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact4"), SoundType.SOUND).volume(0.9)));

        this.add(ModSounds.TOMAHAWK_THROW, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.tomahawk_throw").
                with(Sound.sound(ResourceLocation.parse("item/trident/throw1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/throw2"), SoundType.SOUND)));
        this.add(ModSounds.TOMAHAWK_HIT_MOB, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.tomahawk_hit_mob").
                with(Sound.sound(ResourceLocation.parse("item/trident/pierce1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce2"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce3"), SoundType.SOUND)));
        this.add(ModSounds.TOMAHAWK_HIT_GROUND, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.tomahawk_hit_ground").
                with(Sound.sound(ResourceLocation.parse("item/trident/ground_impact1"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact2"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact3"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact4"), SoundType.SOUND).volume(0.9)));

        this.add(ModSounds.JAVELIN_THROW, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.javelin_throw").
                with(Sound.sound(ResourceLocation.parse("item/trident/throw1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/throw2"), SoundType.SOUND)));
        this.add(ModSounds.JAVELIN_HIT_MOB, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.javelin_hit_mob").
                with(Sound.sound(ResourceLocation.parse("item/trident/pierce1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce2"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce3"), SoundType.SOUND)));
        this.add(ModSounds.JAVELIN_HIT_GROUND, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.javelin_hit_ground").
                with(Sound.sound(ResourceLocation.parse("item/trident/ground_impact1"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact2"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact3"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact4"), SoundType.SOUND).volume(0.9)));

        this.add(ModSounds.BOOMERANG_THROW, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.boomerang_throw").
                with(Sound.sound(ResourceLocation.parse("item/trident/throw1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/throw2"), SoundType.SOUND)));
        this.add(ModSounds.BOOMERANG_FLY, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.boomerang_fly").
                with(Sound.sound(ResourceLocation.parse("random/bow"), SoundType.SOUND)));
        this.add(ModSounds.BOOMERANG_HIT_MOB, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.boomerang_hit_mob").
                with(Sound.sound(ResourceLocation.parse("item/trident/pierce1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce2"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("item/trident/pierce3"), SoundType.SOUND)));
        this.add(ModSounds.BOOMERANG_BOUNCE, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.boomerang_bounce").
                with(Sound.sound(ResourceLocation.parse("entity/player/attack/weak1"), SoundType.SOUND).volume(0.7),
                        Sound.sound(ResourceLocation.parse("entity/player/attack/weak2"), SoundType.SOUND).volume(0.7),
                        Sound.sound(ResourceLocation.parse("entity/player/attack/weak3"), SoundType.SOUND).volume(0.7),
                        Sound.sound(ResourceLocation.parse("entity/player/attack/weak4"), SoundType.SOUND).volume(0.7)));
        this.add(ModSounds.BOOMERANG_HIT_GROUND, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.boomerang_hit_ground").
                with(Sound.sound(ResourceLocation.parse("item/trident/ground_impact1"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact2"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact3"), SoundType.SOUND).volume(0.9),
                        Sound.sound(ResourceLocation.parse("item/trident/ground_impact4"), SoundType.SOUND).volume(0.9)));

        this.add(ModSounds.THROWING_WEAPON_LOYALTY_RETURN, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.throwing_weapon_loyalty_return").
                with(Sound.sound(ResourceLocation.parse("item/trident/return1"), SoundType.SOUND).volume(0.8),
                        Sound.sound(ResourceLocation.parse("item/trident/return2"), SoundType.SOUND).pitch(1.2).volume(0.8),
                        Sound.sound(ResourceLocation.parse("item/trident/return3"), SoundType.SOUND).pitch(0.8).volume(0.8),
                        Sound.sound(ResourceLocation.parse("item/trident/return2"), SoundType.SOUND).volume(0.8),
                        Sound.sound(ResourceLocation.parse("item/trident/return2"), SoundType.SOUND).pitch(1.2).volume(0.8),
                        Sound.sound(ResourceLocation.parse("item/trident/return2"), SoundType.SOUND).pitch(0.8).volume(0.8),
                        Sound.sound(ResourceLocation.parse("item/trident/return3"), SoundType.SOUND).volume(0.8),
                        Sound.sound(ResourceLocation.parse("item/trident/return3"), SoundType.SOUND).pitch(1.2).volume(0.8),
                        Sound.sound(ResourceLocation.parse("item/trident/return3"), SoundType.SOUND).pitch(0.8).volume(0.8)));

        this.add(ModSounds.OIL_APPLIED, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.oil_applied").
                with(Sound.sound(ResourceLocation.parse("block/brewing_stand/brew1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("block/brewing_stand/brew2"), SoundType.SOUND)));

        this.add(ModSounds.HAMMER_SLAMS_INTO_GROUND, SoundDefinition.definition().subtitle("subtitle.spartan_weaponry_unofficial.hammer_slams_into_ground").
                with(Sound.sound(ResourceLocation.parse("random/explode1"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("random/explode2"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("random/explode3"), SoundType.SOUND),
                        Sound.sound(ResourceLocation.parse("random/explode4"), SoundType.SOUND)));

    }

    @Override
    public @NotNull String getName() {
        return ModSpartanWeaponry.NAME + " Sound Definitions";
    }
}
