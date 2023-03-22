package dev.cammiescorner.guts_guns_glory.common.registry;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import net.minecraft.entity.damage.DamageSource;

public class ModDamageSource extends DamageSource {
	public static final DamageSource BLOOD_LOSS = new DamageSource("blood_loss").setBypassesArmor().setUnblockable();

	public ModDamageSource(String name) { super(GutsGunsGlory.MOD_ID + "." + name); }
}
