package me.cloudsven.amethystapath.commands;

import me.cloudsven.amethystapath.items.ItemRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Stream;

public class AmethystApathTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {
        // check permission
        if (!sender.hasPermission("amethystapath.admin")) {
            return List.of(); // Return empty to hide suggestions
        }

        // /aa <subcommand>
        if (args.length == 1) {
            return Stream.of("give", "reload", "help")
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .sorted()
                    .toList();
        }

        // /aa give <item>
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return ItemRegistry.getAll().keySet().stream()
                    .filter(key -> key.startsWith(args[1].toLowerCase()))
                    .sorted()
                    .toList();
        }

        return List.of();
    }
}