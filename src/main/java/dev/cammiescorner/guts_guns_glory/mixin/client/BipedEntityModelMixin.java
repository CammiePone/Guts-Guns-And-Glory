package dev.cammiescorner.guts_guns_glory.mixin.client;

import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import dev.cammiescorner.guts_guns_glory.common.registry.EntityTags;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
	@Shadow @Final public ModelPart head;
	@Shadow @Final public ModelPart hat;
	@Shadow @Final public ModelPart leftArm;
	@Shadow @Final public ModelPart rightArm;
	@Shadow @Final public ModelPart body;
	@Shadow @Final public ModelPart leftLeg;
	@Shadow @Final public ModelPart rightLeg;

	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
	private void arcanuscontinuum$staffRunningAnim(T entity, float f, float g, float h, float i, float j, CallbackInfo info) {
		if(!entity.getType().isIn(EntityTags.UNCONSCIOUS_IMMUNITY) && ModComponents.isUnconscious(entity) && GGGConfig.losingBloodCausesUnconsciousness) {
			head.yaw = (float) Math.toRadians(60);
			head.pitch = (float) Math.toRadians(15);
			head.roll = 0;

			hat.copyTransform(head);

			leftArm.pitch = 0;
			leftArm.yaw = (float) Math.toRadians(70);
			leftArm.roll = (float) Math.toRadians(-60);

			rightArm.pitch = 0;
			rightArm.yaw = 0;
			rightArm.roll = (float) Math.toRadians(20);

			body.pitch = 0;
			body.yaw = 0;
			body.roll = 0;

			leftLeg.pitch = 0;
			leftLeg.yaw = 0;
			leftLeg.roll = (float) Math.toRadians(-25);

			rightLeg.pitch = 0;
			rightLeg.yaw = 0;
			rightLeg.roll = (float) Math.toRadians(35);
		}
	}
}
