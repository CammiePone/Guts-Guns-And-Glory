package dev.cammiescorner.guts_guns_glory.common.registry;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import dev.cammiescorner.guts_guns_glory.common.items.BandageItem;
import dev.cammiescorner.guts_guns_glory.common.items.BloodBagItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;

public class ModItems {
	//-----Item Map-----//
	public static final LinkedHashMap<Item, Identifier> ITEMS = new LinkedHashMap<>();

	//-----Items-----//
	public static final Item BANDAGE = create("bandage", new BandageItem());
	public static final Item BLOOD_BAG = create("blood_bag", new BloodBagItem(true));
	public static final Item EMPTY_BLOOD_BAG = create("empty_blood_bag", new BloodBagItem(false));

	//-----Registry-----//
	public static void register() {
		FabricItemGroupBuilder.create(GutsGunsGlory.id("general")).icon(() -> new ItemStack(ModItems.BANDAGE)).appendItems(entries -> {
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
