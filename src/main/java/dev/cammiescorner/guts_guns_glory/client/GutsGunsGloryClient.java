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
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class GutsGunsGloryClient implements ClientModInitializer {
	public static final Identifier BLOOD_TEXTURE = GutsGunsGlory.id("textures/gui/hud/blood_bar.png");
	private static final Identifier UNCONSCIOUS_WAKE = GutsGunsGlory.id("textures/gui/hud/unconscious_wake.png");
	private static final Identifier UNCONSCIOUS_BLINK = GutsGunsGlory.id("textures/gui/hud/unconscious_blink.png");

	@Override
	public void onInitializeClient(ModContainer mod) {
		final MinecraftClient client = MinecraftClient.getInstance();

		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			if(client.cameraEntity instanceof LivingEntity entity && !entity.isSpectator()) {
				if(entity instanceof PlayerEntity && ModComponents.isUnconscious(entity)) {
					renderOverlay(UNCONSCIOUS_WAKE, 1F);
					renderOverlay(UNCONSCIOUS_BLINK, (float) Math.max(0, Math.sin((entity.age + tickDelta) * 0.1)));
				}
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
}
