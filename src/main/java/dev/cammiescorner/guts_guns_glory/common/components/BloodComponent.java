package dev.cammiescorner.guts_guns_glory.common.components;

import dev.cammiescorner.guts_guns_glory.common.registry.EntityTags;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import dev.cammiescorner.guts_guns_glory.common.registry.ModDamageSource;
import dev.cammiescorner.guts_guns_glory.common.registry.ModStatusEffects;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class BloodComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private long lastBloodRegen = 0;
	private int blood = 20;

	public BloodComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		long timer = entity.getWorld().getTime() - lastBloodRegen;

		if(timer % 1200 == 0 && !entity.hasStatusEffect(ModStatusEffects.BLEED) && incrementBlood(1, true))
			incrementBlood(1, false);

		if(!ModComponents.isUnconscious(entity) && getBlood() <= 0 && timer % 80 == 0)
			entity.damage(ModDamageSource.BLOOD_LOSS, 1);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		lastBloodRegen = tag.getLong("LastBloodRegen");
		blood = tag.getInt("Blood");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putLong("LastBloodRegen", lastBloodRegen);
		tag.putInt("Blood", blood);
	}

	public int getMaxBlood() {
		return 20;
	}

	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		if(blood <= 0 && !entity.getType().isIn(EntityTags.UNCONSCIOUS_IMMUNITY))
			ModComponents.setUnconscious(entity, true);

		this.blood = blood;
		this.lastBloodRegen = entity.getWorld().getTime();
		ModComponents.BLOOD.sync(entity);
	}

	public boolean incrementBlood(int amount, boolean simulate) {
		if(getBlood() < getMaxBlood() && !entity.isUndead()) {
			if(!simulate)
				setBlood(Math.min(getMaxBlood(), getBlood() + amount));

			return true;
		}

		return false;
	}

	public boolean decrementBlood(int amount, boolean simulate) {
		if(getBlood() > 0 && !entity.isUndead()) {
			if(!simulate)
				setBlood(Math.max(0, getBlood() - amount));

			return true;
		}

		return false;
	}
}
