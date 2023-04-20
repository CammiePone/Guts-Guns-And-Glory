package dev.cammiescorner.guts_guns_glory.common.registry;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import dev.cammiescorner.guts_guns_glory.common.effects.ModStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModStatusEffects {
	//-----Status Effect Map-----//
	public static final LinkedHashMap<StatusEffect, Identifier> STATUS_EFFECTS = new LinkedHashMap<>();

	//-----Status Effects-----//
	public static final StatusEffect BLEED = create("bleed", new ModStatusEffect(StatusEffectType.HARMFUL, 0x610004));

	//-----Registry-----//
	public static void register() {
		STATUS_EFFECTS.keySet().forEach(item -> Registry.register(Registry.STATUS_EFFECT, STATUS_EFFECTS.get(item), item));
	}

	private static <T extends StatusEffect> T create(String name, T statusEffect) {
		STATUS_EFFECTS.put(statusEffect, GutsGunsGlory.id(name));
		return statusEffect;
	}
}
