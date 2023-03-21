package dev.cammiescorner.guts_guns_glory.common.entity;

import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import org.quiltmc.qsl.entity.multipart.api.AbstractEntityPart;

public class HeadEntityPart extends AbstractEntityPart<LivingEntity> {
	public HeadEntityPart(LivingEntity owner, float width, float height, Vec3d relativePosition, Vec3d relativePivot) {
		super(owner, width, height);
		setRelativePosition(relativePosition);
		setPivot(relativePivot);
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if((source.isProjectile() || GGGConfig.meleeCanHeadshot) && GGGConfig.headshotsBoostDamage)
			amount *= GGGConfig.headshotDamageMultiplier;

		return super.damage(source, amount);
	}
}
