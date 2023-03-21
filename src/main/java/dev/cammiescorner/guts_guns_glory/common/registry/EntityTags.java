package dev.cammiescorner.guts_guns_glory.common.registry;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class EntityTags {
	public static final TagKey<EntityType<?>> HAS_HEAD = TagKey.of(RegistryKeys.ENTITY_TYPE, GutsGunsGlory.id("has_head"));
}
