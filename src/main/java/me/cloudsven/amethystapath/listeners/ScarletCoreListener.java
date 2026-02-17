package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ScarletCoreListener implements Listener {
    private final AmethystApath plugin;

    public ScarletCoreListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        if (event.getLootTable().getKey().toString().equals("amethystapath:chests/scarlet_core")) {
            ItemStack heart = ItemFactory.create(CustomItem.SCARLET_HEART);

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (event.getInventoryHolder() instanceof Container container) {
                    Inventory inv = container.getInventory();

                    inv.setItem(13, heart);
                }
            });
        }
    }
}