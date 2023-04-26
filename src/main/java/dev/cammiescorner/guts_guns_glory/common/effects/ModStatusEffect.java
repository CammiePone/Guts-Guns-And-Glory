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
		if(this == ModStatusEffects.BLEED && ModComponents.decrementBlood(entity, 1, true)) {
			ModComponents.decrementBlood(entity, 1, false);

			/*
			TODO when bleeding, blood particles should act as normal billboard particles until they hit the ground.
			 When they hit the ground, they should become flat staring straight up.
			 When coming from bleeding, blood particles just go in random directions from the bleeding entity, usually
			 in a downward direction.
			 Also make blood particles spawn when taking damage, going in the direction of the source of damage. Bullets
			 cause blood to come out both sides.
			 Blood particles only live for 5 minutes. They fade away after that.
			*/
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		if(this == ModStatusEffects.BLEED)
			return duration % (40 / (amplifier + 1)) == 0;

		return super.canApplyUpdateEffect(duration, amplifier);
	}
}
