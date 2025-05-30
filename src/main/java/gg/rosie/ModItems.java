package gg.rosie;

import gg.rosie.items.CoralReefMap;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ModItems {
    private static LinkedHashMap<String, Item> items = new LinkedHashMap<>();

    public static void register() {
        items.put("coral_map", Registry.register(Registries.ITEM,
                Identifier.of(MoreCartography.MOD_ID, "coral_map"),
                new CoralReefMap(new Item.Settings())));
    }

    public static ArrayList<Item> getItems() {
        ArrayList<Item> itemList = new ArrayList<>();
        items.forEach((key, value) -> itemList.add(value));
        return itemList;
    }

    public static Item getItem(String blockID) {
        if (items.containsKey(blockID))
            return items.get(blockID);
        return null;
    }
}
