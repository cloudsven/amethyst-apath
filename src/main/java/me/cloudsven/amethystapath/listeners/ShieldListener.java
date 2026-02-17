package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShieldListener implements Listener {
    private final AmethystApath plugin;
    public ShieldListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN = 10000;

    private void reflectDamage(LivingEntity attacker, double originalDamage) {
        double reflected = originalDamage * 0.10;

        attacker.damage(reflected);

        attacker.getWorld().spawnParticle(
                Particle.CRIT,
                attacker.getLocation().add(0, 1, 0),
                20,
                0.4, 0.4, 0.4
        );
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack shield = player.getInventory().getItemInOffHand();
        if (!ItemRegistry.is(shield, CustomItem.AMIRITE_SHIELD)) return;

        if (!player.isBlocking()) return;

        double originalDamage = event.getDamage();

        // reduce incoming damage by 25%
        event.setDamage(originalDamage * 0.75);

        Entity attacker = event.getDamager();

        // handle projectile shooter
        if (attacker instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof LivingEntity shooter) {
                reflectDamage(shooter, originalDamage);
            }
        }

        // handle melee
        else if (attacker instanceof LivingEntity livingAttacker) {
            reflectDamage(livingAttacker, originalDamage);
        }
    }

    @EventHandler
    public void onPulse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking() || !player.isBlocking()) return;

        ItemStack shield = player.getInventory().getItemInOffHand();
        if (!ItemRegistry.is(shield, CustomItem.AMIRITE_SHIELD)) return;

        long now = System.currentTimeMillis();
        if (cooldowns.containsKey(player.getUniqueId())
                && now - cooldowns.get(player.getUniqueId()) < COOLDOWN) return;

        cooldowns.put(player.getUniqueId(), now);

        player.getWorld().spawnParticle(Particle.ENCHANT,
                player.getLocation(), 60, 1, 1, 1);

        for (Entity e : player.getNearbyEntities(5,5,5)) {
            if (e instanceof LivingEntity target && target != player) {
                Vector knock = target.getLocation().toVector()
                        .subtract(player.getLocation().toVector())
                        .normalize()
                        .multiply(1.5);

                target.setVelocity(knock);
            }
        }
    }
}
