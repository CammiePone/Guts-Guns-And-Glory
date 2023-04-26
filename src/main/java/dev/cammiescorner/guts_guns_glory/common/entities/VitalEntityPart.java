package dev.cammiescorner.guts_guns_glory.common.entities;

import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.quiltmc.qsl.entity.multipart.api.AbstractEntityPart;

public class VitalEntityPart extends AbstractEntityPart<LivingEntity> {
	private final boolean isHead;

	public VitalEntityPart(LivingEntity owner, float width, float height, boolean isHead) {
		super(owner, width, height);
		this.isHead = isHead;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if(!world.isClient() && !getOwner().isSpectator() && (!(getOwner() instanceof PlayerEntity player) || !player.isCreative()) && ((source.isProjectile() && amount > 0) || GGGConfig.meleeCanHitVitals)) {
			if(source.getAttacker() instanceof PlayerEntity && isHead)
				ModComponents.decrementBlood(getOwner(), ModComponents.getBlood(getOwner()), false);

			if(GGGConfig.vitalsHitBoostsDamage)
				amount *= GGGConfig.vitalsHitDamageMultiplier;
		}

		return super.damage(source, amount);
	}
}
