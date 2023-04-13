package dev.cammiescorner.guts_guns_glory.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile gameProfile, @Nullable PlayerPublicKey playerPublicKey) { super(world, gameProfile, playerPublicKey); }

	@Inject(method = "canMoveVoluntarily", at = @At("HEAD"), cancellable = true)
	private void ggg$unconsciousness(CallbackInfoReturnable<Boolean> info) {
		if(ModComponents.isUnconscious(this) && GGGConfig.losingBloodCausesUnconsciousness)
			info.setReturnValue(false);
	}
}
