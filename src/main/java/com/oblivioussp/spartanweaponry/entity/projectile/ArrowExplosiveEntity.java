package com.oblivioussp.spartanweaponry.entity.projectile;

import com.oblivioussp.spartanweaponry.init.ModEntities;
import com.oblivioussp.spartanweaponry.util.Config;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;

public class ArrowExplosiveEntity extends ArrowEntitySW 
{
	public ArrowExplosiveEntity(EntityType<? extends ArrowEntitySW> type, Level level) 
	{
		super(type, level);
	}

	public ArrowExplosiveEntity(Level level, double x, double y, double z) 
	{
		super(ModEntities.ARROW_EXPLOSIVE.get(), level, x, y, z);
	}

	public ArrowExplosiveEntity(Level level, LivingEntity shooter) 
	{
		super(ModEntities.ARROW_EXPLOSIVE.get(), level, shooter);
	}
	
	@Override
	protected void initStats() {}

	@Override
	protected void doPostHurtEffects(LivingEntity living)
	{
		super.doPostHurtEffects(living);
		living.hurtTime = 0;
		explode();
	}
	
	@Override
    public void tick()
    {
		super.tick();

		Level level = level();
		if (level.isClientSide && !inGround)
        {
            level.addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
        }
		
		if(this.inGround)
		{
			explode();
		}
    }
	
	protected void explode()
	{
		Level level = level();
		if(!level.isClientSide)
		{
			boolean mobGriefing = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
			level.explode(this, xOld, yOld, zOld, Config.INSTANCE.arrowExplosiveExplosionStrength.get().floatValue(), !Config.INSTANCE.disableTerrainDamage.get() && mobGriefing ? ExplosionInteraction.TNT : ExplosionInteraction.NONE);
			discard();
		}
	}
}
