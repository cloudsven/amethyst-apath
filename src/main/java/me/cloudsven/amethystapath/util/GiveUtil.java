package me.cloudsven.amethystapath.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class GiveUtil {
    private GiveUtil() {}

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
        player.sendMessage(
                Component.text("You were given an ")
                        .append(name)
                        .color(NamedTextColor.LIGHT_PURPLE)
        );

        return true;
    }
}
