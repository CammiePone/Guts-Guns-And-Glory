package dev.cammiescorner.guts_guns_glory.mixin;

import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import dev.cammiescorner.guts_guns_glory.common.entities.VitalEntityPart;
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
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
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
	@Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

	@Unique private final LivingEntity self = (LivingEntity) (Object) this;
	@Unique private final VitalEntityPart headPart = new VitalEntityPart(self, getWidth() + 0.04F, getHeight() * (0.2F) + 0.02F, true);
	@Unique private final VitalEntityPart vitalPart = new VitalEntityPart(self, getWidth() + 0.04F, getHeight() * (0.2F) + 0.02F, false);

	public LivingEntityMixin(EntityType<?> variant, World world) { super(variant, world); }

	@Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
	private void ggg$noTarget(LivingEntity target, CallbackInfoReturnable<Boolean> info) {
		if(ModComponents.isUnconscious(target))
			info.setReturnValue(false);
	}

	@Inject(method = "canSee", at = @At("HEAD"), cancellable = true)
	private void ggg$noSee(Entity entity, CallbackInfoReturnable<Boolean> info) {
		if(entity instanceof LivingEntity livingEntity && ModComponents.isUnconscious(livingEntity))
			info.setReturnValue(false);
	}

	@Override
	public EntityPart<?>[] getEntityParts() {
		if(getType().isIn(EntityTags.HAS_VITALS))
			return new VitalEntityPart[] { headPart, vitalPart };

		return new EntityPart[0];
	}

	@Inject(method = "heal", at = @At("HEAD"), cancellable = true)
	private void ggg$ceaseHealing(float amount, CallbackInfo info) {
		if(hasStatusEffect(ModStatusEffects.BLEED))
			info.cancel();
	}

	@Inject(method = "damage", at = @At("HEAD"))
	private void ggg$bleedOnHit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		if(amount > 0) {
			int amplifier = hasStatusEffect(ModStatusEffects.BLEED) ? Math.min(4, getStatusEffect(ModStatusEffects.BLEED).getAmplifier() + 1) : 0;

			// TODO make vanilla armour block bleed
			if(source.getSource() instanceof LivingEntity attacker) {
				Item item = attacker.getMainHandStack().getItem();

				if(item instanceof ToolItem && GGGConfig.toolsCanCauseBleeding)
					addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, Integer.MAX_VALUE, amplifier, true, false));
			}
			else if(source.getSource() instanceof ArrowEntity) {
				addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, Integer.MAX_VALUE, amplifier, true, false));
			}
			else if(source.isExplosive()) {
				addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, Integer.MAX_VALUE, 4, true, false));
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
		if(ModComponents.isUnconscious(self) && GGGConfig.losingBloodCausesUnconsciousness && hasVehicle())
			dismountVehicle();

		if(getType().isIn(EntityTags.HAS_VITALS)) {
			headPart.rotate(getPitch(), getHeadYaw(), getRoll());
			headPart.setPosition(getPos().add(0, getHeight() - headPart.getHeight() + 0.02F, 0));
			headPart.setDimensions(getWidth() + 0.04F, getHeight() * (0.2F) + 0.02F);
			vitalPart.rotate(getPitch(), getYaw(), getRoll());
			vitalPart.setPosition(getPos().add(0, getHeight() - (vitalPart.getHeight() * 2) + 0.02F, 0));
			vitalPart.setDimensions(getWidth() + 0.04F, getHeight() * (0.2F) + 0.02F);
		}
	}

	@Inject(method = "onSpawnPacket", at = @At("TAIL"))
	private void ggg$onSpawnPacket(EntitySpawnS2CPacket packet, CallbackInfo info) {
		if(getType().isIn(EntityTags.HAS_VITALS)) {
			headPart.setId(1 + packet.getId());
			vitalPart.setId(2 + packet.getId());
		}
	}
}
