package dev.cammiescorner.guts_guns_glory.mixin.client;

import dev.cammiescorner.guts_guns_glory.client.GutsGunsGloryClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Shadow @Final private MinecraftClient client;

	@ModifyVariable(method = "renderStatusBars", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I"
	), ordinal = 9)
	private int ggg$moveArmorBar(int s) {
		if(GutsGunsGloryClient.bloodTimer.value > 0 && client.cameraEntity instanceof LivingEntity entity && !entity.isSpectator() && (!(entity instanceof PlayerEntity player) || !player.isCreative()))
			return s - 10;

		return s;
	}
}
