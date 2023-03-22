package dev.cammiescorner.guts_guns_glory.common.registry;

import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;
import dev.cammiescorner.guts_guns_glory.common.items.BandageItem;
import dev.cammiescorner.guts_guns_glory.common.items.BloodBagItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

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
		FabricItemGroup.builder(GutsGunsGlory.id("general")).icon(() -> new ItemStack(ModItems.BANDAGE)).entries((enabledFeatures, entries, operatorsEnabled) -> {
			entries.addItem(BANDAGE);
			entries.addItem(BLOOD_BAG);
			entries.addItem(EMPTY_BLOOD_BAG);
		}).build();

		ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
	}

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, GutsGunsGlory.id(name));
		return item;
	}
}
