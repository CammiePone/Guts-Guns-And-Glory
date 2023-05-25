package dev.cammiescorner.guts_guns_glory.mixin;

import dev.cammiescorner.guts_guns_glory.common.entities.VitalEntityPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity {
	@Shadow @Nullable public abstract Entity getOwner();

	public ProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) { super(entityType, world); }

	@Inject(method = "canHit", at = @At("RETURN"), cancellable = true)
	private void ggg$canHitHead(Entity entity, CallbackInfoReturnable<Boolean> info) {
		if(entity instanceof VitalEntityPart part && part.getOwner().equals(getOwner()) && age < 5)
			info.setReturnValue(false);
	}
}
