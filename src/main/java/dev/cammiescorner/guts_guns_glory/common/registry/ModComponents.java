package dev.cammiescorner.guts_guns_glory.common.registry;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import dev.cammiescorner.guts_guns_glory.common.components.BloodComponent;
import dev.cammiescorner.guts_guns_glory.common.components.UnconsciousComponent;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;

public class ModComponents implements EntityComponentInitializer {
	public static final ComponentKey<BloodComponent> BLOOD = createComponent("blood", BloodComponent.class);
	public static final ComponentKey<UnconsciousComponent> UNCONSCIOUS = createComponent("unconscious", UnconsciousComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(LivingEntity.class, BLOOD).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(BloodComponent::new);
		registry.beginRegistration(LivingEntity.class, UNCONSCIOUS).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(UnconsciousComponent::new);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(GutsGunsGlory.id(name), component);
	}

	public static int getMaxBlood(LivingEntity entity) {
		return BLOOD.get(entity).getMaxBlood();
	}

	public static int getBlood(LivingEntity entity) {
		return BLOOD.get(entity).getBlood();
	}

	public static boolean decrementBlood(LivingEntity entity, int amount, boolean simulate) {
		return BLOOD.get(entity).decrementBlood(amount, simulate);
	}

	public static boolean isUnconscious(LivingEntity entity) {
		return UNCONSCIOUS.get(entity).isUnconscious();
	}

	public static void setUnconscious(LivingEntity entity, boolean unconscious) {
		UNCONSCIOUS.get(entity).setUnconscious(unconscious);
	}
}
