package dev.cammiescorner.guts_guns_glory.common.items;

import dev.cammiescorner.guts_guns_glory.common.registry.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public abstract class BulletItem extends Item {
	private final Calibre calibre;
	private final int penetrationModifier, bleedAmount;

	public BulletItem(Calibre calibre, int penetrationModifier, int bleedAmount) {
		super(new QuiltItemSettings());
		this.calibre = calibre;
		this.penetrationModifier = penetrationModifier;
		this.bleedAmount = bleedAmount;
	}

	public Calibre getCalibre() {
		return calibre;
	}

	public int getBleedAmount() {
		return bleedAmount;
	}

	public int getPenetration() {
		return Math.max(calibre.penetration + penetrationModifier, 0);
	}

	public void shoot(LivingEntity owner) {
		// TODO custom raycast that can hit multiple entities instead of just one
	}

	public void onHit(LivingEntity owner, HitResult hitResult) {
		if(hitResult.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHit = (EntityHitResult) hitResult;

			if(entityHit.getEntity() instanceof LivingEntity target) {
				int amplifier = target.hasStatusEffect(ModStatusEffects.BLEED) ? Math.min(4, target.getStatusEffect(ModStatusEffects.BLEED).getAmplifier() + getBleedAmount()) : getBleedAmount() - 1;
				target.damage(DamageSource.CACTUS, getCalibre().damage);

				if(amplifier >= 0)
					target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, Integer.MAX_VALUE, amplifier, true, false));
			}
		}
	}

	public enum Calibre {
		// Handguns
		_22LR(3, 6, 0), _9x19_MM(5, 10, 1), _45_ACP(5, 10, 1), _57x28_MM(6, 12, 1), _357(7, 14, 1),

		// Rifles
		_556x45_MM(11, 22, 2), _545x39_MM(12, 24, 2), _762x39_MM(11, 22, 2), _762x54_MM(15, 30, 2),
		_308_WIN(15, 30, 2), _338_LAP(19, 38, 3), _700_EXP(22, 44, 3),

		// Shotguns/Flare Guns/Launchers
		_12_GAUGE(7.5F, 15, 1), FLARE(0, 0, 0), SHELL(0, 0, 0);

		public final float damage, vitalDamage;
		public final int penetration;

		Calibre(float damage, float vitalDamage, int penetration) {
			this.damage = damage;
			this.vitalDamage = vitalDamage;
			this.penetration = penetration;
		}
	}
}
