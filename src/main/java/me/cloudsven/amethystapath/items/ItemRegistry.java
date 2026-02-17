package me.cloudsven.amethystapath.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {
    public static Map<String, CustomItem> getAll() {
        Map<String, CustomItem> map = new HashMap<>();

        for (CustomItem item : CustomItem.values()) {
            map.put(item.id, item);
        }

        return map;
    }

    public static CustomItem getCustomItem(String id) {
        return getAll().get(id);
    }

    public static ItemStack getItem(CustomItem item) {
        return ItemFactory.create(item);
    }

    public static ItemStack getItem(String id) {
        CustomItem item = getCustomItem(id);
        if (item == null) return null;

        return ItemFactory.create(item);
    }

    public static String getId(ItemStack stack) {
        if (!isCustomItem(stack)) return null;

        return stack.getItemMeta()
                .getPersistentDataContainer()
                .get(ItemFactory.getKey(), PersistentDataType.STRING);
    }

    public static boolean isCustomItem(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) return false;

        ItemMeta meta = stack.getItemMeta();

        return meta.getPersistentDataContainer()
                .has(ItemFactory.getKey(), PersistentDataType.STRING);
    }

    public static boolean is(ItemStack stack, CustomItem item) {
        if (!isCustomItem(stack)) return false;

        String id = getId(stack);

        return item.id.equals(id);
    }
}