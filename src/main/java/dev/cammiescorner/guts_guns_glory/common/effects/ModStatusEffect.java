package dev.cammiescorner.guts_guns_glory.common.effects;

import dev.cammiescorner.guts_guns_glory.common.registry.EntityTags;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import dev.cammiescorner.guts_guns_glory.common.registry.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.entity.effect.api.StatusEffectRemovalReason;

public class ModStatusEffect extends StatusEffect {
	public ModStatusEffect(StatusEffectType type, int color) {
		super(type, color);
	}

	@Override
	public boolean shouldRemove(@NotNull LivingEntity entity, @NotNull StatusEffectInstance effect, @NotNull StatusEffectRemovalReason reason) {
		if(entity.getType().isIn(EntityTags.BLEED_IMMUNITY))
			return true;

		return super.shouldRemove(entity, effect, reason);
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
