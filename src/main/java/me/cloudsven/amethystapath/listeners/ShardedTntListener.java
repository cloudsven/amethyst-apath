package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ShardedTntListener implements Listener {
    private final AmethystApath plugin;
    private final CustomItem shardedTnt = CustomItem.SHARDED_TNT;

    public ShardedTntListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item == null || !item.hasItemMeta()) return;
        if (!ItemRegistry.is(item, shardedTnt)) return;

        item.setAmount(item.getAmount() - 1);

        event.setCancelled(true);

        Location loc = event.getBlockPlaced().getLocation().add(0.5, 0, 0.5);
        event.getBlockPlaced().setType(Material.AIR);

        TNTPrimed tnt = loc.getWorld().spawn(loc, TNTPrimed.class);
        tnt.setFuseTicks(80); // normal fuse

        tnt.getPersistentDataContainer().set(
                new NamespacedKey(plugin, shardedTnt.id),
                PersistentDataType.BYTE,
                (byte) 1
        );
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed tnt)) return;

        if (!tnt.getPersistentDataContainer().has(
                new NamespacedKey(plugin, shardedTnt.id),
                PersistentDataType.BYTE
        )) return;

        event.setCancelled(true);

        Location loc = tnt.getLocation();

        // 4x TNT power (normal TNT = 4F)
        loc.getWorld().createExplosion(tnt, loc, 16F, false, true);
    }
}