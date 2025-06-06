package gg.rosie;

import gg.rosie.items.*;
import net.minecraft.item.Item;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ModItems {
    private static final LinkedHashMap<String, Item> items = new LinkedHashMap<>();

    public static void register() {
        registerItem("bamboo_jungle_map", Registries.ITEM, new BambooJungleMap(new Item.Settings()));
        registerItem("cherry_grove_map", Registries.ITEM, new CherryGroveMap(new Item.Settings()));
        registerItem("warm_ocean_map", Registries.ITEM, new WarmOceanMap(new Item.Settings()));
        registerItem("eroded_badlands_map", Registries.ITEM, new ErodedBadlandsMap(new Item.Settings()));
        registerItem("ice_spikes_map", Registries.ITEM, new IceSpikesMap(new Item.Settings()));
        registerItem("mushroom_fields_map", Registries.ITEM, new MushroomFieldsMap(new Item.Settings()));
    }

    public static void registerItem(String itemName, DefaultedRegistry<Item> itemType, Item item) {
        items.put(itemName, Registry.register(itemType, Identifier.of(MoreCartography.MOD_ID, itemName), item));
    }

    public static ArrayList<Item> getItems() {
        ArrayList<Item> itemList = new ArrayList<>();
        items.forEach((key, value) -> itemList.add(value));
        return itemList;
    }
}
