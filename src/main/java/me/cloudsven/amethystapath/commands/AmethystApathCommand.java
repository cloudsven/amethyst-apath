package me.cloudsven.amethystapath.commands;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

import static me.cloudsven.amethystapath.util.GiveUtil.giveItem;

public class AmethystApathCommand implements CommandExecutor {
    private final AmethystApath plugin;

    public AmethystApathCommand(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        Map<String, CustomItem> items = ItemRegistry.getAll();

        // /aa
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(
                    Component.text("AmethystApath v" + plugin.getPluginMeta().getVersion())
                            .color(NamedTextColor.LIGHT_PURPLE)
            );

            // check permission
            if (sender.hasPermission("amethystapath.admin")) {
                sender.sendMessage(
                        Component.text("/aa give <item>")
                                .color(NamedTextColor.GRAY)
                );

                // auto-list items
                items.keySet().stream()
                        .sorted()
                        .forEach(key ->
                                sender.sendMessage(
                                        Component.text("  - " + key)
                                                .color(NamedTextColor.DARK_GRAY)
                                )
                        );

                sender.sendMessage(
                        Component.text("/aa reload")
                                .color(NamedTextColor.GRAY)
                );
            }

            return true;
        }

        // /aa reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("amethystapath.admin")) {
                sender.sendMessage(
                        Component.text("You do not have permission.")
                                .color(NamedTextColor.RED)
                );
                return true;
            }

            plugin.reloadConfig();

            sender.sendMessage(
                    Component.text("AmethystApath reloaded.")
                            .color(NamedTextColor.GREEN)
            );
            return true;
        }

        // /aa give <item>
        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                sender.sendMessage(
                        Component.text("Usage: /aa give <item>")
                                .color(NamedTextColor.RED)
                );
                return true;
            }

            String key = args[1].toLowerCase();

            if (!items.containsKey(key)) {
                sender.sendMessage(
                        Component.text("Unknown item: " + key)
                                .color(NamedTextColor.RED)
                );
                return true;
            }

            return giveItem(sender, ItemRegistry.getItem(key));
        }

        sender.sendMessage(
                Component.text("Unknown subcommand. Use /aa help")
                        .color(NamedTextColor.RED)
        );
        return true;
    }
}
