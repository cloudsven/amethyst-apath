package me.cloudsven.amethystapath.listeners;

import me.cloudsven.amethystapath.AmethystApath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class VillagerListener implements Listener {
    private final AmethystApath plugin;

    public VillagerListener(AmethystApath plugin) {
        this.plugin = plugin;
    }

    public ItemStack getItemFromLootTable(Villager villager, String namespace, String key) {
        NamespacedKey lootKey = new NamespacedKey(namespace, key);
        LootTable lootTable = Bukkit.getLootTable(lootKey);

        if (lootTable == null) return null;

        LootContext context = new LootContext.Builder(villager.getLocation())
                .lootedEntity(villager)
                .build();

        Collection<ItemStack> items = lootTable.populateLoot(new Random(), context);
        return items.stream().findFirst().orElse(null);
    }

    @EventHandler
    public void onVillagerTrade(VillagerAcquireTradeEvent event) {
        if (!(event.getEntity() instanceof Villager villager)) return;
        if (villager.getProfession() != Villager.Profession.CARTOGRAPHER) return;

        // using a 1-tick delay to let vanilla trades generate first
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // check for Level 3 (Journeyman)
            if (villager.getVillagerLevel() == 3) {

                ItemStack result = getItemFromLootTable(villager, "amethystapath", "amethyst_chasm_map");

                if (result != null) {
                    MerchantRecipe customTrade = new MerchantRecipe(result, 1, 5, true);
                    customTrade.addIngredient(new ItemStack(Material.EMERALD, 20));
                    customTrade.addIngredient(new ItemStack(Material.COMPASS, 1));

                    // directly add to the villager's recipe list
                    List<MerchantRecipe> recipes = new ArrayList<>(villager.getRecipes());
                    recipes.add(customTrade);
                    villager.setRecipes(recipes);
                }
            }
        }, 1L);
    }
}