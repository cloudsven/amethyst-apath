package me.cloudsven.amethystapath.recipes;

import me.cloudsven.amethystapath.items.CustomItem;
import me.cloudsven.amethystapath.items.ItemFactory;
import me.cloudsven.amethystapath.items.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class RecipeManager {
    private RecipeManager() {
    }

    public static void registerAll(JavaPlugin plugin) {
        registerAmethystCluster(plugin);
        registerAmiriteShard(plugin);
        registerAmiriteTotem(plugin);
        registerAmiriteCrossbow(plugin);
        registerAmiriteCleaver(plugin);
        registerAmiriteShield(plugin);
        registerShardedTNT(plugin);
        registerScarletHeart(plugin);
        registerScarletMace(plugin);
        registerScarletPickaxe(plugin);
        registerScarletBlade(plugin);
    }

    private static void shaped(
            JavaPlugin plugin,
            CustomItem item,
            Integer resultAmount,
            String[] shape,
            Object... ingredients
    ) {
        ItemStack result = ItemRegistry.getItem(item);

        result.setAmount(resultAmount);
        ShapedRecipe recipe = new ShapedRecipe(
                new NamespacedKey(plugin, item.id),
                result
        );

        recipe.shape(shape);

        for (int i = 0; i < ingredients.length; i += 2) {
            char symbol = (char) ingredients[i];
            Object value = ingredients[i + 1];

            if (value instanceof Material material) {
                recipe.setIngredient(symbol, material);
            } else if (value instanceof ItemStack itemStack) {
                recipe.setIngredient(symbol, new RecipeChoice.ExactChoice(itemStack.clone()));
            } else if (value instanceof CustomItem customItem) {
                recipe.setIngredient(symbol, new RecipeChoice.ExactChoice(ItemRegistry.getItem(customItem)));
            } else if (value instanceof RecipeChoice choice) {
                recipe.setIngredient(symbol, choice);
            }
        }

        Bukkit.addRecipe(recipe);
    }

    private static void registerAmethystCluster(JavaPlugin plugin) {
        ItemStack result = new ItemStack(Material.AMETHYST_CLUSTER);

        ShapedRecipe recipe = new ShapedRecipe(
                new NamespacedKey(plugin, "amethyst_cluster"),
                result
        );

        recipe.shape(
                " A ",
                "AAA",
                " A "
        );

        recipe.setIngredient('A', Material.AMETHYST_SHARD);

        Bukkit.addRecipe(recipe);
    }

    private static void registerAmiriteShard(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.AMIRITE_SHARD,
                4,
                new String[]{
                        "QAQ",
                        "ANA",
                        "QAQ"
                },
                'N', Material.NETHERITE_INGOT,
                'A', Material.AMETHYST_CLUSTER,
                'Q', Material.QUARTZ
        );
    }

    private static void registerAmiriteTotem(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.AMIRITE_TOTEM,
                1,
                new String[]{
                        " A ",
                        "ATA",
                        " A "
                },
                'A', ItemFactory.create(CustomItem.AMIRITE_SHARD),
                'T', Material.TOTEM_OF_UNDYING
        );
    }

    private static void registerAmiriteShield(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.AMIRITE_SHIELD,
                1,
                new String[]{
                        " A ",
                        "ASA",
                        " A "
                },
                'A', ItemFactory.create(CustomItem.AMIRITE_SHARD),
                'S', Material.SHIELD
        );
    }

    private static void registerAmiriteCrossbow(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.AMIRITE_CROSSBOW,
                1,
                new String[]{
                        " A ",
                        "ACA",
                        " A "
                },
                'A', ItemFactory.create(CustomItem.AMIRITE_SHARD),
                'C', Material.CROSSBOW
        );
    }

    private static void registerAmiriteCleaver(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.AMIRITE_CLEAVER,
                1,
                new String[]{
                        "AA ",
                        "AA ",
                        " B "
                },
                'A', ItemFactory.create(CustomItem.AMIRITE_SHARD),
                'B', Material.BLAZE_ROD
        );
    }

    private static void registerShardedTNT(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.SHARDED_TNT,
                1,
                new String[]{
                        " A ",
                        "ATA",
                        " A "
                },
                'A', ItemFactory.create(CustomItem.AMIRITE_SHARD),
                'T', Material.TNT
        );
    }

    private static void registerScarletHeart(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.SCARLET_HEART_CHARGED,
                1,
                new String[]{
                        "NAN",
                        "AHA",
                        "NAN"
                },
                'A', ItemFactory.create(CustomItem.AMIRITE_SHARD),
                'H', ItemFactory.create(CustomItem.SCARLET_HEART),
                'N', Material.NETHER_STAR
        );
    }

    private static void registerScarletBlade(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.SCARLET_BLADE,
                1,
                new String[]{
                        "AHA",
                        "NSN",
                        " B "
                },
                'A', ItemFactory.create(CustomItem.AMIRITE_SHARD),
                'H', ItemFactory.create(CustomItem.SCARLET_HEART_CHARGED),
                'S', Material.NETHERITE_SWORD,
                'N', Material.NETHERITE_INGOT,
                'B', Material.BLAZE_ROD
        );
    }

    private static void registerScarletPickaxe(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.SCARLET_PICKAXE,
                1,
                new String[]{
                        "AHA",
                        "NPN",
                        " B "
                },
                'A', ItemFactory.create(CustomItem.AMIRITE_SHARD),
                'H', ItemFactory.create(CustomItem.SCARLET_HEART_CHARGED),
                'P', Material.NETHERITE_PICKAXE,
                'N', Material.NETHERITE_INGOT,
                'B', Material.BLAZE_ROD
        );
    }

    private static void registerScarletMace(JavaPlugin plugin) {
        shaped(
                plugin,
                CustomItem.SCARLET_MACE,
                1,
                new String[]{
                        "AHA",
                        "NMN",
                        " B "
                },
                'A', ItemFactory.create(CustomItem.AMIRITE_SHARD),
                'H', ItemFactory.create(CustomItem.SCARLET_HEART_CHARGED),
                'M', Material.MACE,
                'B', Material.BLAZE_ROD
        );
    }
}