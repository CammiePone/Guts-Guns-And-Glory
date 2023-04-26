package dev.cammiescorner.guts_guns_glory.common.components;

import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class UnconsciousComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity entity;
	private boolean isUnconscious;
	private int timer;

	public UnconsciousComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		if(isUnconscious()) {
			if(getTimer() > 0)
				setTimer(getTimer() - 1);
			else
				setUnconscious(false);
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		isUnconscious = tag.getBoolean("IsUnconscious");
		timer = tag.getInt("UnconsciousTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("IsUnconscious", isUnconscious);
		tag.putInt("UnconsciousTimer", timer);
	}

	public boolean isUnconscious() {
		return isUnconscious;
	}

	public void setUnconscious(boolean unconscious) {
		ScaleData heightData = ScaleTypes.HITBOX_HEIGHT.getScaleData(entity);
		ScaleData eyeData = ScaleTypes.EYE_HEIGHT.getScaleData(entity);

		if(unconscious) {
			setTimer(600);

			// TODO figure out how to tilt the head up
			heightData.setScale(0.3F);
			eyeData.setScale(0.3F);
		}
		else {
			heightData.resetScale();
			eyeData.resetScale();
		}

		isUnconscious = unconscious;
		ModComponents.UNCONSCIOUS.sync(entity);
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}
}
