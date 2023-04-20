package dev.cammiescorner.guts_guns_glory.mixin.client;

import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow @Nullable public ClientPlayerEntity player;
	@Shadow @Final public Mouse mouse;

	@Inject(method = "handleInputEvents", at = @At("HEAD"), cancellable = true)
	private void ggg$noMovement(CallbackInfo info) {
		if(mouse.isCursorLocked() && player != null && ModComponents.isUnconscious(player) && GGGConfig.losingBloodCausesUnconsciousness)
			info.cancel();
	}
}
