package dev.cammiescorner.guts_guns_glory.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.guts_guns_glory.client.GutsGunsGloryClient;
import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow @Final private MinecraftClient client;
	@Shadow protected abstract PlayerEntity getCameraPlayer();

	@Unique private int bloodTimer = 0;

	@Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
	private void ggg$hideHud(MatrixStack matrices, CallbackInfo info) {
		if(ModComponents.isUnconscious(getCameraPlayer()) && GGGConfig.losingBloodCausesUnconsciousness)
			info.cancel();
	}

	@Inject(method = "renderStatusBars", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
			ordinal = 0
	))
	private void ggg$renderBloodBar(MatrixStack matrices, CallbackInfo info) {
		client.getProfiler().swap("blood");
		PlayerEntity player = getCameraPlayer();

		if(!player.isCreative()) {
			int blood = ModComponents.getBlood(player);
			int maxBlood = ModComponents.getMaxBlood(player);
			int q = MathHelper.ceil((Math.max(player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH), MathHelper.ceil(player.getHealth())) + MathHelper.ceil(player.getAbsorptionAmount())) / 2F / 10F);
			int r = Math.max(10 - (q - 2), 3);
			int offsetY = (q - 1) * r;

			if(blood < maxBlood)
				bloodTimer = Math.min(bloodTimer + 1, 40);
			else
				bloodTimer = Math.max(bloodTimer - 1, 0);

			if(bloodTimer > 0) {
				int scaledWidth = client.getWindow().getScaledWidth();
				int scaledHeight = client.getWindow().getScaledHeight();
				int x = scaledWidth / 2 - 91;
				int y = scaledHeight - 49 - offsetY;
				float alpha = bloodTimer > 20 ? 1F : bloodTimer / 20F;

				RenderSystem.enableBlend();
				RenderSystem.setShaderTexture(0, GutsGunsGloryClient.BLOOD_TEXTURE);
				RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

				for(int i = 0; i < 10; i++)
					DrawableHelper.drawTexture(matrices, x + (i * 8), y, 0, 0, 8, 9, 256, 256);

				for(int i = 0; i < blood / 2; i++)
					DrawableHelper.drawTexture(matrices, x + (i * 8), y, 16, 0, 8, 9, 256, 256);

				if(blood % 2 == 1)
					DrawableHelper.drawTexture(matrices, x + ((blood / 2) * 8), y, 8, 0, 8, 9, 256, 256);
			}
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
	}

	@ModifyVariable(method = "renderStatusBars", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I"
	), ordinal = 9)
	private int ggg$moveArmorBar(int s) {
		if(bloodTimer > 0 && client.cameraEntity instanceof LivingEntity entity && !entity.isSpectator() && (!(entity instanceof PlayerEntity player) || !player.isCreative()))
			return s - 10;

		return s;
	}
}
