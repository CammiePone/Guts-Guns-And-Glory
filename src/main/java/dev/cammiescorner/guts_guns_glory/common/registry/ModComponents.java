package dev.cammiescorner.guts_guns_glory.common.registry;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import dev.cammiescorner.guts_guns_glory.common.components.BloodComponent;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;

public class ModComponents implements EntityComponentInitializer {
	public static final ComponentKey<BloodComponent> BLOOD_COMPONENT = createComponent("blood", BloodComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(LivingEntity.class, BLOOD_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(BloodComponent::new);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(GutsGunsGlory.id(name), component);
	}

	public static int getMaxBlood(LivingEntity entity) {
		return BLOOD_COMPONENT.get(entity).getMaxBlood();
	}

	public static int getBlood(LivingEntity entity) {
		return BLOOD_COMPONENT.get(entity).getBlood();
	}

	public static boolean decrementBlood(LivingEntity entity, int amount, boolean simulate) {
		return BLOOD_COMPONENT.get(entity).decrementBlood(amount, simulate);
	}
}
