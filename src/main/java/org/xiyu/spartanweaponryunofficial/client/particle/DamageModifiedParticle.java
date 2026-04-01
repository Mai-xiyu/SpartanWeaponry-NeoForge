package org.xiyu.spartanweaponryunofficial.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class DamageModifiedParticle extends SingleQuadParticle {
    DamageModifiedParticle(ClientLevel levelIn, double xPos, double yPos, double zPos,
                           double xDel, double yDel, double zDel, TextureAtlasSprite sprite) {
        super(levelIn, xPos, yPos, zPos, xDel, yDel, zDel, sprite);
        this.friction = 0.7f;
        this.gravity = 0.5f;
        this.xd *= 0.1f;
        this.yd *= 0.1f;
        this.zd *= 0.1f;
        this.xd += xDel * 0.4d;
        this.yd += yDel * 0.4d;
        this.zd += zDel * 0.4d;
        float colour = (float) (Math.random() * 0.3d + 0.6d);
        this.rCol = colour;
        this.gCol = colour;
        this.bCol = colour;
        this.quadSize *= 0.75f;
        this.lifetime = Math.max(Mth.ceil(6.0d / (Math.random() * 0.8d + 0.6d)), 1);
        this.hasPhysics = false;
        this.tick();
    }

    @Override
    public void tick() {
        this.gCol *= 0.96f;
        this.bCol *= 0.9f;
        super.tick();
    }

    @Override
    public @NotNull Layer getLayer() {
        return Layer.OPAQUE;
    }

    public static class DamageBoostedProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public DamageBoostedProvider(SpriteSet spriteSetIn) {
            this.spriteSet = spriteSetIn;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel levelIn, double xPos,
                                       double yPos, double zPos, double xDel, double yDel, double zDel, @NotNull RandomSource random) {
            DamageModifiedParticle particle = new DamageModifiedParticle(levelIn, xPos, yPos, zPos, xDel, yDel, zDel, this.spriteSet.get(random));
            particle.rCol = 0.5f;
            particle.gCol = 1.0f;
            particle.bCol = 0.2f;
            return particle;
        }
    }

    public static class DamageReducedProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public DamageReducedProvider(SpriteSet spriteSetIn) {
            this.spriteSet = spriteSetIn;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel levelIn, double xPos,
                                       double yPos, double zPos, double xDel, double yDel, double zDel, @NotNull RandomSource random) {
            DamageModifiedParticle particle = new DamageModifiedParticle(levelIn, xPos, yPos, zPos, xDel, yDel, zDel, this.spriteSet.get(random));
            particle.rCol = 0.5f;
            particle.gCol = 0.2f;
            particle.bCol = 0.5f;
            return particle;
        }
    }

    public static class OilDamageBoostedProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public OilDamageBoostedProvider(SpriteSet spriteSetIn) {
            this.spriteSet = spriteSetIn;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel levelIn, double xPos,
                                       double yPos, double zPos, double xDel, double yDel, double zDel, @NotNull RandomSource random) {
            DamageModifiedParticle particle = new DamageModifiedParticle(levelIn, xPos, yPos, zPos, xDel, yDel, zDel, this.spriteSet.get(random));
            particle.rCol = 1.0f;
            particle.gCol = 0.75f;
            particle.bCol = 0.25f;
            return particle;
        }
    }
}
