package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import me.cloudsven.amethystapath.util.DamageUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class MaceListener implements Listener {
    private final AmethystApath plugin;

    public MaceListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    private static final NamespacedKey RUPTURE_KEY =
            new NamespacedKey("amethystapath", "rupture");

    public static int getRuptureLevel(ItemStack item) {
        if (item == null) return 0;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;

        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            if (entry.getKey().getKey().equals(RUPTURE_KEY)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    private double getArmorDamagePercent(ItemStack mace) {
        int level = getRuptureLevel(mace);

        return (level * 0.025) + 0.5;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemRegistry.is(item, CustomItem.SCARLET_MACE)) return;

        double percent = getArmorDamagePercent(item);

        DamageUtil.damageArmor(target, percent);
        DamageUtil.applyTrueDamage(target, 2);
        DamageUtil.applyWithering(target, 60, 1);
    }
}
