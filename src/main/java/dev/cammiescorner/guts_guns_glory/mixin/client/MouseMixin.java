package dev.cammiescorner.guts_guns_glory.mixin.client;

import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Shadow @Final private MinecraftClient client;
	@Shadow public abstract boolean isCursorLocked();

	@Inject(method = "onCursorPos", at = @At("HEAD"), cancellable = true)
	private void ggg$lockHead(long window, double x, double y, CallbackInfo info) {
		if(isCursorLocked() && client.player != null && ModComponents.isUnconscious(client.player) && GGGConfig.losingBloodCausesUnconsciousness)
			info.cancel();
	}
}
