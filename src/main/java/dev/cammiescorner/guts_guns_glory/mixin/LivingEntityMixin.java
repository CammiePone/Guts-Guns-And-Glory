package dev.cammiescorner.guts_guns_glory.mixin;

import dev.cammiescorner.guts_guns_glory.common.entity.HeadEntityPart;
import dev.cammiescorner.guts_guns_glory.common.registry.EntityTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.quiltmc.qsl.entity.multipart.api.EntityPart;
import org.quiltmc.qsl.entity.multipart.api.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements MultipartEntity {
	@Shadow public abstract float getHeadYaw();

	@Unique private final LivingEntity self = (LivingEntity) (Object) this;
	@Unique private final HeadEntityPart headPart = new HeadEntityPart(self, getWidth() + 0.02F, getHeight() / 4 + 0.01F, new Vec3d(0, getHeight() * 0.75, 0), new Vec3d(0d, getHeight() * 0.75, 0));

	public LivingEntityMixin(EntityType<?> variant, World world) { super(variant, world); }

	@Override
	public EntityPart<?>[] getEntityParts() {
		if(getType().isIn(EntityTags.HAS_HEAD))
			return new HeadEntityPart[] { headPart };

		return new EntityPart[0];
	}

	@Inject(method = "tickMovement", at = @At("TAIL"))
	private void ggg$tickMovement(CallbackInfo info) {
		if(getType().isIn(EntityTags.HAS_HEAD))
			headPart.rotate(getPitch(), getHeadYaw(), true);
	}

	@Inject(method = "onSpawnPacket", at = @At("TAIL"))
	private void ggg$onSpawnPacket(EntitySpawnS2CPacket packet, CallbackInfo info) {
		if(getType().isIn(EntityTags.HAS_HEAD))
			headPart.setId(1 + packet.getId());
	}
}
