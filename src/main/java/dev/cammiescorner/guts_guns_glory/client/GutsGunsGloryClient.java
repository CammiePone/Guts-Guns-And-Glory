package dev.cammiescorner.guts_guns_glory.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class GutsGunsGloryClient implements ClientModInitializer {
	private static final Identifier BLOOD_TEXTURE = GutsGunsGlory.id("textures/gui/hud/blood_bar.png");
	private static final Identifier UNCONSCIOUS_WAKE = GutsGunsGlory.id("textures/gui/hud/unconscious_wake.png");
	private static final Identifier UNCONSCIOUS_BLINK = GutsGunsGlory.id("textures/gui/hud/unconscious_blink.png");
	public static BloodTimer bloodTimer = new BloodTimer();

	@Override
	public void onInitializeClient(ModContainer mod) {
		final MinecraftClient client = MinecraftClient.getInstance();

		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			if(client.cameraEntity instanceof LivingEntity entity && !entity.isSpectator()) {
				if(!(entity instanceof PlayerEntity player) || !player.isCreative()) {
					int blood = ModComponents.getBlood(entity);
					int maxBlood = ModComponents.getMaxBlood(entity);
					int q = MathHelper.ceil((Math.max(entity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH), MathHelper.ceil(entity.getHealth())) + MathHelper.ceil(entity.getAbsorptionAmount())) / 2F / 10F);
					int r = Math.max(10 - (q - 2), 3);
					int offsetY = (q - 1) * r;

					if(blood < maxBlood)
						bloodTimer.value = Math.min(bloodTimer.value + 1, 40);
					else
						bloodTimer.value = Math.max(bloodTimer.value - 1, 0);

					if(bloodTimer.value > 0) {
						int scaledWidth = client.getWindow().getScaledWidth();
						int scaledHeight = client.getWindow().getScaledHeight();
						int x = scaledWidth / 2 - 91;
						int y = scaledHeight - 49 - offsetY;
						float alpha = bloodTimer.value > 20 ? 1F : bloodTimer.value / 20F;

						RenderSystem.enableBlend();
						RenderSystem.setShaderTexture(0, BLOOD_TEXTURE);
						RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

						for(int i = 0; i < 10; i++)
							DrawableHelper.drawTexture(matrices, x + (i * 8), y, 0, 0, 8, 9, 256, 256);

						for(int i = 0; i < blood / 2; i++)
							DrawableHelper.drawTexture(matrices, x + (i * 8), y, 16, 0, 8, 9, 256, 256);

						if(blood % 2 == 1)
							DrawableHelper.drawTexture(matrices, x + ((blood / 2) * 8), y, 8, 0, 8, 9, 256, 256);
					}
				}

				if(entity instanceof PlayerEntity && ModComponents.isUnconscious(entity)) {
					renderOverlay(UNCONSCIOUS_WAKE, 1F);
					renderOverlay(UNCONSCIOUS_BLINK, (float) Math.max(0, Math.sin((entity.age + tickDelta) * 0.1)));
				}

				// TODO replace this with a better solution later
				client.options.cinematicCamera = ModComponents.isUnconscious(entity) && entity instanceof PlayerEntity;
			}
		});
	}

	private void renderOverlay(Identifier texture, float opacity) {
		MinecraftClient client = MinecraftClient.getInstance();
		double scaledHeight = client.getWindow().getScaledHeight();
		double scaledWidth = client.getWindow().getScaledWidth();

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
		RenderSystem.setShaderTexture(0, texture);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(0.0, scaledHeight, -90.0).uv(0.0F, 1.0F).next();
		bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0).uv(1.0F, 1.0F).next();
		bufferBuilder.vertex(scaledWidth, 0.0, -90.0).uv(1.0F, 0.0F).next();
		bufferBuilder.vertex(0.0, 0.0, -90.0).uv(0.0F, 0.0F).next();
		tessellator.draw();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static class BloodTimer {
		public int value;
	}
}
