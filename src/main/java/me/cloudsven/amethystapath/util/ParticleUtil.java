package me.cloudsven.amethystapath.util;

import me.cloudsven.amethystapath.AmethystApath;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class ParticleUtil {
    private ParticleUtil() {}

    public static Firework spawnFireworkExplosion(World world, Location loc) {
        Firework firework = (Firework) world.spawnEntity(
                loc,
                EntityType.FIREWORK_ROCKET
        );

        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.addEffect(
                FireworkEffect.builder()
                        .with(FireworkEffect.Type.BURST)
                        .withColor(
                                Color.fromRGB(210, 112, 222), // purple
                                Color.WHITE
                        )
                        .withFade(
                                Color.fromRGB(210, 112, 222) // purple
                        )
                        .flicker(true)
                        .trail(true)
                        .build()
        );

        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);

        return firework;
    }

    public static Firework spawnFireworkExplosion(World world, Location loc, Color color) {
        Firework firework = (Firework) world.spawnEntity(
                loc,
                EntityType.FIREWORK_ROCKET
        );

        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.addEffect(
                FireworkEffect.builder()
                        .with(FireworkEffect.Type.BURST)
                        .withColor(
                                color,
                                Color.WHITE
                        )
                        .withFade(
                                color
                        )
                        .flicker(true)
                        .trail(true)
                        .build()
        );

        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);

        return firework;
    }

    public static void startTrail(Projectile projectile, AmethystApath plugin) {
        new BukkitRunnable() {
            private Location lastLoc = projectile.getLocation().clone();

            @Override
            public void run() {

                if (projectile.isDead() || projectile.isOnGround()) {
                    cancel();
                    return;
                }

                Location current = projectile.getLocation();
                World world = projectile.getWorld();

                Vector delta = current.toVector().subtract(lastLoc.toVector());

                int steps = 8; // MORE = smoother
                Vector step = delta.clone().multiply(1.0 / steps);

                for (int i = 0; i < steps; i++) {
                    lastLoc.add(step);

                    world.spawnParticle(
                            Particle.DUST,
                            lastLoc,
                            1,
                            new Particle.DustOptions(
                                    Color.fromRGB(180, 120, 255),
                                    1.4f
                            )
                    );
                }

                lastLoc = current.clone();
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
