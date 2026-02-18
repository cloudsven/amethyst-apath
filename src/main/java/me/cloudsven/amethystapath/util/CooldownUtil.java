package me.cloudsven.amethystapath.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CooldownUtil {
    // Structure: Map<AbilityName, Map<PlayerUUID, ExpirationTime>>
    private static final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    /**
     * sets a cooldown for a specific player and ability
     * @param playerUUID the player UUID
     * @param ability the name of the ability (i.e. "scarlet_haste")
     * @param seconds the duration in seconds
     */

    public static void setCooldown(UUID playerUUID, String ability, int seconds) {
        long delay = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.computeIfAbsent(ability, k -> new HashMap<>()).put(playerUUID, delay);
    }

    // checks if the player is still on cooldown
    public static boolean isOnCooldown(UUID playerUUID, String ability) {
        return getRemainingTime(playerUUID, ability) > 0;
    }

    // returns remaining seconds or 0
    public static long getRemainingTime(UUID playerUUID, String ability) {
        Map<UUID, Long> abilityMap = cooldowns.get(ability);
        if (abilityMap == null || !abilityMap.containsKey(playerUUID)) {
            return 0;
        }

        long remaining = (abilityMap.get(playerUUID) - System.currentTimeMillis()) / 1000;
        return Math.max(remaining, 0);
    }

    // returns a string like "1m 40s" or "15s"
    public static String getFormattedRemainingTime(UUID playerUUID, String ability) {
        long totalSeconds = getRemainingTime(playerUUID, ability);

        if (totalSeconds <= 0) {
            return "0s";
        }

        if (totalSeconds < 60) {
            return totalSeconds + "s";
        }

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        // i.e. returns "1m 40s"
        return minutes + "m " + seconds + "s";
    }
}