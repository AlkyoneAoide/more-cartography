package gg.rosie;

import gg.rosie.items.*;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static gg.rosie.MoreCartography.MOD_ID;

public class ModItems {
    private static final LinkedHashMap<String, Item> items = new LinkedHashMap<>();

    public static void register() {
        registerItem("bamboo_jungle_map",
                new BambooJungleMap(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "bamboo_jungle_map")))));
        registerItem("cherry_grove_map",
                new CherryGroveMap(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "cherry_grove_map")))));
        registerItem("warm_ocean_map",
                new WarmOceanMap(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "warm_ocean_map")))));
        registerItem("eroded_badlands_map",
                new ErodedBadlandsMap(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "eroded_badlands_map")))));
        registerItem("ice_spikes_map",
                new IceSpikesMap(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "ice_spikes_map")))));
        registerItem("mushroom_fields_map",
                new MushroomFieldsMap(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "mushroom_fields_map")))));
    }

    public static void registerItem(String itemName, Item item) {
        items.put(itemName, Registry.register(Registries.ITEM, RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, itemName)), item));
    }

    public static ArrayList<Item> getItems() {
        ArrayList<Item> itemList = new ArrayList<>();
        items.forEach((key, value) -> itemList.add(value));
        return itemList;
    }
}
