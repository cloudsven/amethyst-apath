package me.cloudsven.amethystapath.items;

import me.cloudsven.amethystapath.util.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemFactory {
    private static NamespacedKey key;

    public static void init(JavaPlugin plugin) {
        key = new NamespacedKey(plugin, "amethyst_apath_id");
    }

    public static NamespacedKey getKey() {
        return key;
    }

    private static UUID generateUUID(String base, String type) {
        return UUID.nameUUIDFromBytes((base + ":" + type).getBytes());
    }

    public static ItemStack create(CustomItem item) {
        ItemStack stack = new ItemStack(item.material);
        ItemMeta meta = stack.getItemMeta();

        // display name
        meta.displayName(TextUtil.text(item.name, item.color));

        // custom model data (for resource packs)
        CustomModelDataComponent component = meta.getCustomModelDataComponent();
        component.setStrings(List.of(item.id));
        meta.setCustomModelDataComponent(component);

        meta.getPersistentDataContainer().set(
                key,
                PersistentDataType.STRING,
                item.id
        );

        // apply attributes if present
        applyAttributes(meta, item);

        // fix component inconsistencies
        if (meta instanceof Damageable damageable) {
            damageable.setDamage(0);
        }

        // generate lore
        meta.lore(generateLore(item));

        stack.setItemMeta(meta);
        return stack;
    }

    private static void applyAttributes(ItemMeta meta, CustomItem item) {
        if (item.attackDamage != null || item.attackSpeed != null) {
            // hide vanilla attributes
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }

        if (item.attackDamage != null) {
            meta.addAttributeModifier(
                    Attribute.ATTACK_DAMAGE,
                    new AttributeModifier(
                            generateUUID(item.id, "attack_damage"),
                            "custom_attack_damage",
                            item.attackDamage,
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlotGroup.MAINHAND
                    )
            );
        }
        if (item.attackSpeed != null) {
            meta.addAttributeModifier(
                    Attribute.ATTACK_SPEED,
                    new AttributeModifier(
                            generateUUID(item.id, "attack_speed"),
                            "custom_attack_speed",
                            item.attackSpeed - 4.0, // adjust base if needed
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlotGroup.MAINHAND
                    )
            );
        }
    }

    private static List<Component> generateLore(CustomItem item) {
        List<Component> lore = new ArrayList<>(item.baseLore);

        if (item.attackDamage != null || item.attackSpeed != null) {
            lore.add(TextUtil.empty());
            lore.add(TextUtil.text("When in Main Hand:"));
        }

        if (item.attackDamage != null) {
            lore.add(
                    TextUtil.text(" " + item.attackDamage + " Attack Damage",
                            NamedTextColor.DARK_GREEN)
            );
        }

        if (item.attackSpeed != null) {
            lore.add(
                    TextUtil.text(" " + item.attackSpeed + " Attack Speed",
                            NamedTextColor.DARK_GREEN)
            );
        }

        return lore;
    }
}
