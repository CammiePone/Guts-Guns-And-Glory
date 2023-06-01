package dev.cammiescorner.guts_guns_glory.client.renderer.item;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.guts_guns_glory.common.items.GunItem;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.DefaultedItemGeoModel;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class GunRenderer extends GeoItemRenderer<GunItem> {
	public GunRenderer(Identifier itemId) {
		super(new DefaultedItemGeoModel<>(itemId));
	}

	@Override
	public void renderRecursively(MatrixStack poseStack, GunItem animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		ItemStack stack = currentItemStack;

		// TODO handle all the different attachments
		bone.setHidden(switch(bone.getName()) {
			// attachment point 1
			case "12x_optic" -> true;
			case "8x_optic" -> true;
			case "4x_optic" -> true;
			case "2x_enhancer" -> true;

			// attachment point 2
			case "suppressor" -> true;
			case "compensator" -> true;
			case "long_barrel" -> true;

			// attachment point 3
			case "laser" -> true;
			case "flashlight" -> true;

			// attachment point 4
			case "foregrip" -> true;
			case "angled_grip" -> true;
			case "bipod" -> true;

			// misc
			case "magazine" -> true;
			case "camo_cover" -> true;
			case "top_rail" -> true;
			case "bottom_rail" -> true;
			case "iron_sight" -> false;
			default -> false;
		});

		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
