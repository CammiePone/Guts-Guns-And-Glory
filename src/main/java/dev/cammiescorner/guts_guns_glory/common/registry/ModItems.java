package dev.cammiescorner.guts_guns_glory.common.registry;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import dev.cammiescorner.guts_guns_glory.common.items.BandageItem;
import dev.cammiescorner.guts_guns_glory.common.items.BloodBagItem;
import dev.cammiescorner.guts_guns_glory.common.items.BulletItem;
import dev.cammiescorner.guts_guns_glory.common.items.MagazineItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.LinkedHashMap;

public class ModItems {
	//-----Item Map-----//
	public static final LinkedHashMap<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//-----Items-----//
	public static final Item BANDAGE = create("bandage", new BandageItem());
	public static final Item BLOOD_BAG = create("blood_bag", new BloodBagItem(true));
	public static final Item EMPTY_BLOOD_BAG = create("empty_blood_bag", new BloodBagItem(false));

	public static final Item _22_AUTOMATIC = create("22_automatic", new Item(new QuiltItemSettings().maxCount(1)));

	public static final Item _22LR_AUTO_MAG = create("22lr_auto_magazine", new MagazineItem(BulletItem.Calibre._22LR, 20));
	public static final Item _22LR_RIFLE_MAG = create("22lr_rifle_magazine", new MagazineItem(BulletItem.Calibre._22LR, 30));

	public static final Item _22LR_BULLET = create("22lr_bullet", new BulletItem(BulletItem.Calibre._22LR, 0, 1) {});
	public static final Item _22LR_SILVER_BULLET = create("22lr_silver_bullet", new BulletItem(BulletItem.Calibre._22LR, 0, 1) {});
	public static final Item _22LR_HP_BULLET = create("22lr_hollow_point_bullet", new BulletItem(BulletItem.Calibre._22LR, -1, 2) {});
	public static final Item _22LR_GREEN_TIP_BULLET = create("22lr_green_tip_bullet", new BulletItem(BulletItem.Calibre._22LR, 1, 1) {});
	public static final Item _22LR_TRACER_BULLET = create("22lr_tracer_bullet", new BulletItem(BulletItem.Calibre._22LR, 0, 1) {});
	public static final Item _22LR_HV_BULLET = create("22lr_high_velocity_bullet", new BulletItem(BulletItem.Calibre._22LR, 1, 1) {});
	public static final Item _22LR_AP_BULLET = create("22lr_armor_piercing_bullet", new BulletItem(BulletItem.Calibre._22LR, 2, 0) {});
	public static final Item _22LR_INCENDIARY_BULLET = create("22lr_incendiary_bullet", new BulletItem(BulletItem.Calibre._22LR, -1, 0) {
		@Override
		public void onHit(LivingEntity owner, HitResult hitResult) {
			if(hitResult.getType() == HitResult.Type.ENTITY) {
				EntityHitResult entityHit = (EntityHitResult) hitResult;

				if(entityHit.getEntity() instanceof LivingEntity target) {
					target.damage(DamageSource.CACTUS, getCalibre().damage);
					target.setFireTicks(80);
				}
			}
		}
	});

	//-----Registry-----//
	public static void register() {
		FabricItemGroupBuilder.create(GutsGunsGlory.id("guns")).icon(() -> new ItemStack(ModItems._22_AUTOMATIC)).appendItems(entries -> {
			entries.add(new ItemStack(_22_AUTOMATIC));
		}).build();

		FabricItemGroupBuilder.create(GutsGunsGlory.id("ammunition")).icon(() -> new ItemStack(ModItems._22LR_BULLET)).appendItems(entries -> {
			entries.add(new ItemStack(_22LR_AUTO_MAG));
			entries.add(new ItemStack(_22LR_RIFLE_MAG));
			entries.add(new ItemStack(_22LR_BULLET));
			entries.add(new ItemStack(_22LR_SILVER_BULLET));
			entries.add(new ItemStack(_22LR_HP_BULLET));
			entries.add(new ItemStack(_22LR_GREEN_TIP_BULLET));
			entries.add(new ItemStack(_22LR_TRACER_BULLET));
			entries.add(new ItemStack(_22LR_HV_BULLET));
			entries.add(new ItemStack(_22LR_AP_BULLET));
			entries.add(new ItemStack(_22LR_INCENDIARY_BULLET));
		}).build();

		FabricItemGroupBuilder.create(GutsGunsGlory.id("medical_supplies")).icon(() -> new ItemStack(ModItems.BANDAGE)).appendItems(entries -> {
			entries.add(new ItemStack(BANDAGE));
			entries.add(new ItemStack(BLOOD_BAG));
			entries.add(new ItemStack(EMPTY_BLOOD_BAG));
		}).build();

		ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
	}

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, GutsGunsGlory.id(name));
		return item;
	}
}
