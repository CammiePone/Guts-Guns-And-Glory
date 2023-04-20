package dev.cammiescorner.guts_guns_glory.mixin;

import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import dev.cammiescorner.guts_guns_glory.common.entities.HeadEntityPart;
import dev.cammiescorner.guts_guns_glory.common.registry.EntityTags;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import dev.cammiescorner.guts_guns_glory.common.registry.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements MultipartEntity {
	@Shadow public abstract float getHeadYaw();
	@Shadow public abstract boolean hasStatusEffect(StatusEffect effect);
	@Shadow public abstract int getArmor();
	@Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);
	@Shadow public abstract int getRoll();

	@Unique private final LivingEntity self = (LivingEntity) (Object) this;
	@Unique private final HeadEntityPart headPart = new HeadEntityPart(self, getWidth() + 0.04F, getHeight() / 4 + 0.02F, new Vec3d(0, getHeight() * 0.75, 0), new Vec3d(0d, getHeight() * 0.75, 0));

	public LivingEntityMixin(EntityType<?> variant, World world) { super(variant, world); }

	@Override
	public EntityPart<?>[] getEntityParts() {
		if(getType().isIn(EntityTags.HAS_HEAD))
			return new HeadEntityPart[] { headPart };

		return new EntityPart[0];
	}

	@Inject(method = "heal", at = @At("HEAD"), cancellable = true)
	private void ggg$ceaseHealing(float amount, CallbackInfo info) {
		if(hasStatusEffect(ModStatusEffects.BLEED))
			info.cancel();
	}

	@Inject(method = "damage", at = @At("HEAD"))
	private void ggg$bleedOnHit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		if(getArmor() <= 0 && amount > 0) {
			if(source.getSource() instanceof LivingEntity attacker) {
				Item item = attacker.getMainHandStack().getItem();

				if(item instanceof SwordItem)
					addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, 900, 1));
				if(item instanceof AxeItem)
					addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, 600, 0));
			}
			else if(source.getSource() instanceof ArrowEntity) {
				addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, 900, 1));
			}
		}
	}

	@Inject(method = "canMoveVoluntarily", at = @At("HEAD"), cancellable = true)
	private void ggg$unconsciousness(CallbackInfoReturnable<Boolean> info) {
		if(!(self instanceof PlayerEntity) && ModComponents.isUnconscious(self) && GGGConfig.losingBloodCausesUnconsciousness)
			info.setReturnValue(false);
	}

	@Inject(method = "tryUseTotem", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"
	))
	private void ggg$useTotem(DamageSource source, CallbackInfoReturnable<Boolean> info) {
		ModComponents.incrementBlood(self, 8, false);
		ModComponents.setUnconscious(self, false);
	}

	@Inject(method = "tickMovement", at = @At("TAIL"))
	private void ggg$tickMovement(CallbackInfo info) {
		if(getType().isIn(EntityTags.HAS_HEAD)) {
			headPart.rotate(getPitch(), getHeadYaw(), getRoll());
			headPart.setPosition(getPos().add(0, getHeight() - headPart.getHeight() + 0.02F, 0));
			headPart.setDimensions(getWidth() + 0.04F, getHeight() / 4 + 0.02F);
		}
	}

	@Inject(method = "onSpawnPacket", at = @At("TAIL"))
	private void ggg$onSpawnPacket(EntitySpawnS2CPacket packet, CallbackInfo info) {
		if(getType().isIn(EntityTags.HAS_HEAD))
			headPart.setId(1 + packet.getId());
	}
}
