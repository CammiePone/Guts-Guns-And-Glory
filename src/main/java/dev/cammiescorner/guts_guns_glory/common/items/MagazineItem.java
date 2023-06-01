package dev.cammiescorner.guts_guns_glory.common.items;

import com.google.common.collect.ImmutableList;
import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.ArrayList;
import java.util.List;

public class MagazineItem extends Item {
	private final BulletItem.Calibre calibre;
	private final int capacity;
	private final float reloadModifier;

	// TODO make magazines work like bundles
	public MagazineItem(BulletItem.Calibre calibre, int capacity, float reloadModifier) {
		super(new QuiltItemSettings().maxCount(1));
		this.calibre = calibre;
		this.capacity = capacity;
		this.reloadModifier = reloadModifier;
	}

	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if(otherStack.getItem() instanceof BulletItem bullet && bullet.getCalibre() == getCalibre()) {
			int bulletCount = getBullets(thisStack).size();

			switch(clickType) {
				case LEFT -> {
					ItemStack bulletStack = otherStack.split(Math.min(capacity - bulletCount, otherStack.getCount()));
					addBullets(thisStack, bulletStack);
				}
				case RIGHT -> {
					ItemStack bulletStack = otherStack.split(Math.min(capacity - bulletCount, 1));
					addBullets(thisStack, bulletStack);
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean onClickedOnOther(ItemStack thisStack, Slot otherSlot, ClickType clickType, PlayerEntity player) {
		return false;
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return 0x44ccff;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return (int) ((getBullets(stack).size() / (float) getCapacity()) * 13);
	}

	public BulletItem.Calibre getCalibre() {
		return calibre;
	}

	public int getCapacity() {
		return capacity;
	}

	public static ImmutableList<BulletItem> getBullets(ItemStack stack) {
		NbtCompound tag = stack.getSubNbt(GutsGunsGlory.MOD_ID);
		return tag != null ? getBullets(tag) : ImmutableList.of();
	}

	public static ImmutableList<BulletItem> getBullets(NbtCompound tag) {
		List<BulletItem> bullets = new ArrayList<>();
		NbtList nbtList = tag.getList("Bullets", NbtElement.STRING_TYPE);

		for(int i = 0; i < nbtList.size(); i++) {
			Identifier itemId = new Identifier(nbtList.getString(i));

			if(Registry.ITEM.get(itemId) instanceof BulletItem bulletItem)
				bullets.add(bulletItem);
		}

		return ImmutableList.copyOf(bullets);
	}

	public static void addBullets(ItemStack magazine, ItemStack bullets) {
		NbtCompound tag = magazine.getOrCreateSubNbt(GutsGunsGlory.MOD_ID);
		NbtList nbtList = tag.getList("Bullets", NbtElement.STRING_TYPE) != null ? tag.getList("Bullets", NbtElement.STRING_TYPE) : new NbtList();

		for(int i = 0; i < bullets.getCount(); i++)
			nbtList.add(NbtString.of(Registry.ITEM.getId(bullets.getItem()).toString()));

		tag.put("Bullets", nbtList);
		magazine.writeNbt(tag);
	}

	public static NbtCompound addBullets(NbtCompound tag, ItemStack bullets) {

		return tag;
	}

	public static void shootNextBullet(LivingEntity shooter, ItemStack stack) {
		List<BulletItem> bullets = getBullets(stack);

		if(bullets.isEmpty())
			return;

		BulletItem nextBullet = bullets.get(0);
		nextBullet.shoot(shooter);
		bullets.remove(0);
	}


	public float getReloadModifier() {
		return reloadModifier;
	}
}
