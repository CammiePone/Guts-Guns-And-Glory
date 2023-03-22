package dev.cammiescorner.guts_guns_glory.common.status_effects;

import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import dev.cammiescorner.guts_guns_glory.common.registry.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class ModStatusEffect extends StatusEffect {
	public ModStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if(this == ModStatusEffects.BLEED && ModComponents.decrementBlood(entity, 1, true))
			ModComponents.decrementBlood(entity, 1, false);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		if(this == ModStatusEffects.BLEED)
			return duration % (20 / (amplifier + 1)) == 0;

		return super.canApplyUpdateEffect(duration, amplifier);
	}
}
