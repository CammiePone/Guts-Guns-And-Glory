package dev.cammiescorner.guts_guns_glory.common.items;

import com.google.common.collect.Sets;
import dev.cammiescorner.guts_guns_glory.client.renderer.item.GunRenderer;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.SingletonGeoAnimatable;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class GunItem extends Item implements GeoItem {
	public static final String SAFE_ANIM = "safe";
	public static final String FIRING_ANIM = "firing";
	public static final String AIMING_ANIM = "aiming";
	public static final String RELOAD_ANIM = "reload";

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
	private final Set<MagazineItem> validMags;
	private final BulletItem.Calibre calibre;
	private final FireType type;
	private final float baseRange, baseRecoil;
	private final int baseFireRate, baseReloadTime;
	private final boolean handgun;

	public GunItem(BulletItem.Calibre calibre, FireType type, float baseRange, float baseRecoil, int baseFireRate, int baseReloadTime, boolean handgun, MagazineItem... validMags) {
		super(new QuiltItemSettings().maxCount(1));
		this.calibre = calibre;
		this.type = type;
		this.baseRange = baseRange;
		this.baseRecoil = baseRecoil;
		this.baseFireRate = baseFireRate;
		this.baseReloadTime = baseReloadTime;
		this.handgun = handgun;
		this.validMags = Sets.newHashSet(validMags);

		SingletonGeoAnimatable.registerSyncedAnimatable(this);
	}

	@Override
	public void createRenderer(Consumer<Object> consumer) {
		final GunItem gunItem = this;

		consumer.accept(new RenderProvider() {
			private GunRenderer gunRenderer = null;

			@Override
			public BuiltinModelItemRenderer getCustomRenderer() {
				if(gunRenderer == null)
					gunRenderer = new GunRenderer(Registry.ITEM.getId(gunItem));

				return gunRenderer;
			}
		});
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(addAnims(new AnimationController<>(this, "shoot_controller", state -> PlayState.CONTINUE)));
	}

	@Override
	public Supplier<Object> getRenderProvider() {
		return renderProvider;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	public abstract AnimationController<?> addAnims(AnimationController<?> controller);

	public BulletItem.Calibre getCalibre() {
		return calibre;
	}

	public FireType getType() {
		return type;
	}

	public float getRange(ItemStack stack) {
		return baseRange;
	}

	public float getRecoil(ItemStack stack) {
		return baseRecoil;
	}

	public int getFireRate(ItemStack stack) {
		return baseFireRate;
	}

	public int getReloadTime(ItemStack stack) {
		return baseReloadTime;
	}

	public boolean isHandgun() {
		return handgun;
	}

	public Set<MagazineItem> getValidMags() {
		return validMags;
	}

	public enum FireType {
		BOLT, SEMI_AUTO, BURST, FULL_AUTO
	}
}
