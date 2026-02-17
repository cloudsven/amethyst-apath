package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class PickaxeListener implements Listener {
    private final AmethystApath plugin;

    private static final List<Material> breakable = List.of(
            Material.BEDROCK,
            Material.END_PORTAL_FRAME,
            Material.REINFORCED_DEEPSLATE,
            Material.TRIAL_SPAWNER,
            Material.VAULT,
            Material.SPAWNER
    );

    public PickaxeListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUnbreakableClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null || !ItemRegistry.is(event.getItem(), CustomItem.SCARLET_PICKAXE)) return;

        if (breakable.contains(block.getType())) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                block.setType(Material.AIR);
                event.setCancelled(true);
            }, 20 * 5L);
        }
    }
}