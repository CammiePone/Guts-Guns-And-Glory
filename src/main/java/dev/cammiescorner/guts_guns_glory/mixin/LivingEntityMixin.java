package dev.cammiescorner.guts_guns_glory.mixin;

import dev.cammiescorner.guts_guns_glory.common.entities.HeadEntityPart;
import dev.cammiescorner.guts_guns_glory.common.registry.EntityTags;
import dev.cammiescorner.guts_guns_glory.common.registry.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Vec3d;
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

	@Shadow
	public abstract int getArmor();

	@Shadow
	public abstract void endCombat();

	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source);

	@Unique private final LivingEntity self = (LivingEntity) (Object) this;
	@Unique private final HeadEntityPart headPart = new HeadEntityPart(self, getWidth() + 0.02F, getHeight() / 4 + 0.01F, new Vec3d(0, getHeight() * 0.75, 0), new Vec3d(0d, getHeight() * 0.75, 0));

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
					addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, 600, 1));
				if(item instanceof AxeItem)
					addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, 300, 0));
			}
			else if(source.getSource() instanceof ArrowEntity) {
				addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, 600, 1));
			}
		}
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
