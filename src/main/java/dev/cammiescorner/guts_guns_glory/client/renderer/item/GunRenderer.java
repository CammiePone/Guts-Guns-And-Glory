package dev.cammiescorner.guts_guns_glory.client.renderer.item;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.guts_guns_glory.common.items.GunItem;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.DefaultedItemGeoModel;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class GunRenderer extends GeoItemRenderer<GunItem> {
	public GunRenderer(Identifier itemId) {
		super(new DefaultedItemGeoModel<>(itemId));
	}

	@Override
	public void renderRecursively(MatrixStack poseStack, GunItem animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		// TODO handle all the different attachments
//		if(bone.getName().equals("scope"))
//			bone.setHidden(true);

		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
