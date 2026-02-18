package me.cloudsven.amethystapath.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class GiveUtil {
    private GiveUtil() {}

    private static String getArticle(String text) {
        if (text == null || text.isEmpty()) return "a";

        // convert to lowercase to check sounds
        String lower = text.toLowerCase();

        // list of vowel sounds
        // includes 'h' for words like 'honor' or 'hour'
        if (lower.startsWith("a") || lower.startsWith("e") ||
                lower.startsWith("i") || lower.startsWith("o") ||
                lower.startsWith("u")) {

            // exception i.e. 'user' or 'university' sound like 'y'
            if (lower.startsWith("user") || lower.startsWith("uni")) {
                return "a";
            }
            return "an";
        }

        // check for silent 'h' i.e. 'an heirloom'
        if (lower.startsWith("heir") || lower.startsWith("hon")) {
            return "an";
        }

        return "a";
    }

    public static boolean giveItem(CommandSender sender, ItemStack item) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (!sender.hasPermission("amethystapath.admin")) {
            sender.sendMessage(
                    Component.text("You do not have permission.")
                            .color(NamedTextColor.RED)
            );
            return true;
        }

        player.getInventory().addItem(item);

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            sender.sendMessage(
                    Component.text("Internal error.")
                            .color(NamedTextColor.RED)
            );
            return true;
        }

        Component name = meta.displayName();

        assert name != null;

        String plainName = PlainTextComponentSerializer.plainText().serialize(name);
        String article = getArticle(plainName);

        player.sendMessage(
                Component.text("You were given " + article + " ")
                        .append(name)
                        .color(NamedTextColor.LIGHT_PURPLE)
        );

        return true;
    }
}
