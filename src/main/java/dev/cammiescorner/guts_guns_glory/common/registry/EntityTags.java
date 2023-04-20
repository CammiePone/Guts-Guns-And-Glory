package dev.cammiescorner.guts_guns_glory.common.registry;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class EntityTags {
	public static final TagKey<EntityType<?>> HAS_HEAD = TagKey.of(Registry.ENTITY_TYPE_KEY, GutsGunsGlory.id("has_head"));
	public static final TagKey<EntityType<?>> BLEED_IMMUNITY = TagKey.of(Registry.ENTITY_TYPE_KEY, GutsGunsGlory.id("bleed_immunity"));
	public static final TagKey<EntityType<?>> UNCONSCIOUS_IMMUNITY = TagKey.of(Registry.ENTITY_TYPE_KEY, GutsGunsGlory.id("unconscious_immunity"));
}
