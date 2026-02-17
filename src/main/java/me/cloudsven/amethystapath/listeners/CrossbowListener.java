package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import me.cloudsven.amethystapath.util.DamageUtil;
import me.cloudsven.amethystapath.util.ParticleUtil;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class CrossbowListener implements Listener {
    private final AmethystApath plugin;

    public CrossbowListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;

        ItemStack item = arrow.getWeapon();
        if (item == null || !ItemRegistry.is(item, CustomItem.AMIRITE_CROSSBOW)) return;

        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        arrow.setDamage(0);
        arrow.setGravity(false);
        arrow.setVelocity(arrow.getVelocity().multiply(3.5));

        NamespacedKey key = new NamespacedKey(plugin, "amirite_projectile");
        arrow.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        ParticleUtil.startTrail(arrow, plugin);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;
        NamespacedKey key = new NamespacedKey(plugin, "amirite_projectile");

        if (!arrow.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
            return; // not the custom projectile
        }

        Location loc = arrow.getLocation();
        World world = loc.getWorld();

        Vector offset = arrow.getVelocity()
                .clone()
                .normalize()
                .multiply(0.3);

        Location explosionLoc = loc.add(offset);

        Firework firework = ParticleUtil.spawnFireworkExplosion(world, explosionLoc);
        Bukkit.getScheduler().runTaskLater(plugin, firework::detonate, 1L);

        if (event.getHitEntity() instanceof LivingEntity target) {
            DamageUtil.applyTrueDamage(target, 4.0);
        }

        arrow.remove();
    }

}
