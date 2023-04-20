package dev.cammiescorner.guts_guns_glory.mixin.client;

import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import dev.cammiescorner.guts_guns_glory.common.registry.EntityTags;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
	protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) { super(ctx); }

	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/LivingEntity;isAlive()Z"
	))
	private void ggg$sleepyTimes(T livingEntity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
		if(!livingEntity.getType().isIn(EntityTags.UNCONSCIOUS_IMMUNITY) && ModComponents.isUnconscious(livingEntity) && GGGConfig.losingBloodCausesUnconsciousness) {
			if(livingEntity.getType().isIn(EntityTags.HAS_HEAD)) {
				matrices.translate(0, 1.3, 0.75);
				matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
			}
			else {
				matrices.translate(-0.75, 1.2, 0);
				matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(90));
			}
		}
	}
}
