package me.cloudsven.amethystapath.util;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class DamageUtil {
    private DamageUtil() {}

    public static void applyTrueDamage(LivingEntity entity, double amount) {
        entity.setHealth(Math.max(0, entity.getHealth() - amount));
    }

    public static void damageArmor(LivingEntity target, double percent) {
        if (target.getEquipment() == null) return;

        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        }) {
            ItemStack armor = target.getEquipment().getItem(slot);
            if (armor == null || !(armor.getItemMeta() instanceof Damageable meta)) continue;

            int max = armor.getType().getMaxDurability();
            int damage = (int) Math.ceil(max * percent);

            meta.setDamage(meta.getDamage() + damage);
            armor.setItemMeta(meta);

            if (meta.getDamage() >= max) {
                target.getEquipment().setItem(slot, null);
            }
        }
    }

    public static void applyWithering(LivingEntity target, int ticks, int amplifier) {
        target.addPotionEffect(
                new PotionEffect(PotionEffectType.WITHER, ticks, amplifier)
        );
    }
}
