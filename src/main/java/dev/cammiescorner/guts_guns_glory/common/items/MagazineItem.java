package dev.cammiescorner.guts_guns_glory.common.items;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.ArrayList;
import java.util.List;

public class MagazineItem extends Item {
	private final BulletItem.Calibre calibre;
	private final int capacity;

	// TODO make magazines work like bundles
	public MagazineItem(BulletItem.Calibre calibre, int capacity) {
		super(new QuiltItemSettings().maxCount(1));
		this.calibre = calibre;
		this.capacity = capacity;
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return 0x00aaff;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.min(getBullets(stack).size(), getCapacity()) / getCapacity();
	}

	public BulletItem.Calibre getCalibre() {
		return calibre;
	}

	public int getCapacity() {
		return capacity;
	}

	public static List<BulletItem> getBullets(ItemStack stack) {
		NbtCompound tag = stack.getSubNbt(GutsGunsGlory.MOD_ID);
		return tag != null ? getBullets(tag) : List.of();
	}

	public static List<BulletItem> getBullets(NbtCompound tag) {
		List<BulletItem> bullets = new ArrayList<>();
		NbtList nbtList = tag.getList("Bullets", NbtElement.STRING_TYPE);

		for(int i = 0; i < nbtList.size(); i++) {
			Identifier itemId = new Identifier(nbtList.getString(i));

			if(Registry.ITEM.get(itemId) instanceof BulletItem bulletItem)
				bullets.add(bulletItem);
		}

		return bullets;
	}

	public static void shootNextBullet(LivingEntity shooter, ItemStack stack) {
		List<BulletItem> bullets = getBullets(stack);

		if(bullets.isEmpty())
			return;

		BulletItem nextBullet = bullets.get(0);
		nextBullet.shoot(shooter);
		bullets.remove(0);
	}
}
