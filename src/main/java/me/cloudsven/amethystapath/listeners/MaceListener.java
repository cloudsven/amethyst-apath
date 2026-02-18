package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import me.cloudsven.amethystapath.util.CooldownUtil;
import me.cloudsven.amethystapath.util.DamageUtil;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Map;

public class MaceListener implements Listener {
    private final AmethystApath plugin;

    private static final CustomItem mace = CustomItem.SCARLET_MACE;
    private final String MACE_BOOST_TAG = "scarlet_mace_boosted";
    private static final NamespacedKey RUPTURE_KEY = new NamespacedKey("amethystapath", "rupture");

    public MaceListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!ItemRegistry.is(item, mace)) return;

        // handle smash durability cost
        // if player has the tag they are currently in a launch-smash
        if (player.hasMetadata(MACE_BOOST_TAG)) {
            consumeDurability(item, 15); // i.e. high cost for smash
            player.removeMetadata(MACE_BOOST_TAG, plugin);
        } else {
            consumeDurability(item, 1); // i.e. normal hit cost
        }

        double percent = getArmorDamagePercent(item);
        DamageUtil.damageArmor(target, percent);
        DamageUtil.applyTrueDamage(target, 2);
        DamageUtil.applyWithering(target, 60, 1);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!ItemRegistry.is(item, mace)) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        String abilityKey = "scarlet_mace_launch";

        if (CooldownUtil.isOnCooldown(player.getUniqueId(), abilityKey)) {
            String warningText = CooldownUtil.getFormattedRemainingTime(player.getUniqueId(), abilityKey);
            player.sendMessage(ChatColor.RED + "Wait " + warningText + " before using this again!");
            return;
        }

        // get world & location
        World world = player.getWorld();
        Location loc = player.getLocation();

        // consume durability for launch
        consumeDurability(item, 25); // i.e. medium cost for launch

        // boost player
        Vector boost = loc.getDirection().multiply(1.5).setY(1.0);
        player.setVelocity(boost);

        // give the player the boost tag & play a wing charge sfx
        player.setMetadata(MACE_BOOST_TAG, new FixedMetadataValue(plugin, true));
        world.playSound(loc, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.0f, 1.0f);

        // spawn particles
        world.spawnParticle(
                Particle.CRIMSON_SPORE,
                loc,
                80,
                0.5, 0.5, 0.5
        );

        // send a message
        player.sendMessage(ChatColor.GOLD + "You feel the launch of the Scarlet Mace!");

        // set cooldown
        CooldownUtil.setCooldown(player.getUniqueId(), abilityKey, mace.cooldown);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (player.hasMetadata(MACE_BOOST_TAG)) {
                event.setCancelled(true);
                player.removeMetadata(MACE_BOOST_TAG, plugin);
            }
        }
    }

    // helper method to damage the item
    private void consumeDurability(ItemStack item, int amount) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable damageable) {
            damageable.setDamage(damageable.getDamage() + amount);
            item.setItemMeta(damageable);

            // i.e. break item if durability is too low
            if (damageable.getDamage() >= item.getType().getMaxDurability()) {
                item.setAmount(0);
            }
        }
    }

    public static int getRuptureLevel(ItemStack item) {
        if (item == null) return 0;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;
        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            if (entry.getKey().getKey().equals(RUPTURE_KEY)) return entry.getValue();
        }
        return 0;
    }

    private double getArmorDamagePercent(ItemStack item) {
        int level = getRuptureLevel(item);
        return (level * 0.025) + 0.5;
    }
}