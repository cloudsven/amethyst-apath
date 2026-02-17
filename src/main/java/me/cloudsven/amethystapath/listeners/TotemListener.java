package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemRegistry;
import me.cloudsven.amethystapath.util.DamageUtil;
import me.cloudsven.amethystapath.util.ParticleUtil;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.key.Key;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class TotemListener implements Listener {
    private final AmethystApath plugin;

    public TotemListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTotem(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack item = event.getHand() != null
                ? player.getInventory().getItem(event.getHand())
                : null;

        if (item == null || item.getType() != Material.TOTEM_OF_UNDYING) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (!ItemRegistry.is(item, CustomItem.AMIRITE_TOTEM)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 2));

        Location loc = player.getLocation();
        double damage = plugin.getConfig().getInt("amirite-totem.damage");
        int radius = plugin.getConfig().getInt("amirite-totem.radius");

        for (LivingEntity entity : loc.getNearbyLivingEntities(radius)) {
            if (entity.equals(player)) continue;
            DamageUtil.applyTrueDamage(entity, damage);
        }

        // particles
        World world = loc.getWorld();

        Firework firework = ParticleUtil.spawnFireworkExplosion(world, loc);

        // instant detonation
        firework.detonate();

        for (int i = 0; i < 40; i++) {
            Vector dir = new Vector(
                    Math.random() - 0.5,
                    Math.random() - 0.1,
                    Math.random() - 0.5
            ).normalize().multiply(1.6);

            world.spawnParticle(
                    Particle.END_ROD,
                    loc,
                    0,
                    dir.getX(),
                    dir.getY(),
                    dir.getZ(),
                    0.25
            );
        }

        // soundscape
        player.playSound(
                Sound.sound(
                        Key.key("item.totem.use"),
                        Sound.Source.PLAYER,
                        1f,
                        1f
                )
        );
    }
}
