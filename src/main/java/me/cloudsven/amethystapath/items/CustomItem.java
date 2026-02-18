package me.cloudsven.amethystapath.items;

import me.cloudsven.amethystapath.util.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

import java.util.List;

public enum CustomItem {
    AMIRITE_SHARD(
            "amirite_shard",
            Material.AMETHYST_SHARD,
            "Amirite Shard",
            NamedTextColor.LIGHT_PURPLE
    ),

    AMIRITE_TOTEM(
            "amirite_totem",
            Material.TOTEM_OF_UNDYING,
            "Totem of Amirite",
            NamedTextColor.LIGHT_PURPLE,
            List.of(
                    TextUtil.text("This totem explodes with 2.5 hearts of true damage"),
                    TextUtil.text("to any nearby player or mob.")
            )
    ),

    AMIRITE_CROSSBOW(
            "amirite_crossbow",
            Material.CROSSBOW,
            "Amirite Crossbow",
            NamedTextColor.LIGHT_PURPLE,
            List.of(
                    TextUtil.empty(),
                    TextUtil.text("This crossbow makes arrows explode"),
                    TextUtil.text("with 2 hearts of true damage.")
            )
    ),

    AMIRITE_CLEAVER(
            "amirite_cleaver",
            Material.NETHERITE_AXE,
            "Amirite Cleaver",
            NamedTextColor.LIGHT_PURPLE,
            List.of(
                    TextUtil.empty(),
                    TextUtil.text("This axe guarantees a head on kill"),
                    TextUtil.text("and deals 2 hearts of true damage.")
            ),
            null,
            16,
            0.4
    ),

    AMIRITE_SHIELD(
            "amirite_shield",
            Material.SHIELD,
            "Amirite Shield",
            NamedTextColor.LIGHT_PURPLE,
            List.of(
                    TextUtil.empty(),
                    TextUtil.text("This shield reduces damage taken by 25%"),
                    TextUtil.text("and reflects 10% of damage."),
                    TextUtil.empty(),
                    TextUtil.text("Abilities:"),
                    TextUtil.text("• Knock 'Em", NamedTextColor.GOLD)
            ),
            10
    ),

    SCARLET_HEART(
            "scarlet_heart",
            Material.HEART_OF_THE_SEA,
            "Scarlet Heart",
            NamedTextColor.RED
    ),

    SCARLET_HEART_CHARGED(
            "scarlet_heart_charged",
            Material.HEART_OF_THE_SEA,
            "Charged Scarlet Heart",
            NamedTextColor.RED
    ),

    SCARLET_BLADE(
            "scarlet_blade",
            Material.NETHERITE_SWORD,
            "Scarlet Blade",
            NamedTextColor.RED,
            List.of(
                    TextUtil.empty(),
                    TextUtil.text("This sword can give you life!"),
                    TextUtil.text("But not your armor."),
                    TextUtil.empty(),
                    TextUtil.text("Abilities:"),
                    TextUtil.text("• Scarlet Drain", NamedTextColor.DARK_RED),
                    TextUtil.text("• Scarlet Snatch", NamedTextColor.DARK_RED),
                    TextUtil.text("• Critical Fire", NamedTextColor.GOLD),
                    TextUtil.text("• Glory Kill", NamedTextColor.GOLD)
            ),
            null,
            10,
            1.6
    ),

    SCARLET_MACE(
            "scarlet_mace",
            Material.MACE,
            "Scarlet Mace",
            NamedTextColor.RED,
            List.of(
                    TextUtil.empty(),
                    TextUtil.text("This mace can rupture anyones armor."),
                    TextUtil.text("So use it wisely."),
                    TextUtil.empty(),
                    TextUtil.text("Abilities:"),
                    TextUtil.text("• Scarlet Drain", NamedTextColor.DARK_RED),
                    TextUtil.text("• Scarlet Launch", NamedTextColor.DARK_RED)
            ),
            20
    ),

    SCARLET_PICKAXE(
            "scarlet_pickaxe",
            Material.NETHERITE_PICKAXE,
            "Scarlet Pickaxe",
            NamedTextColor.RED,
            List.of(
                    TextUtil.empty(),
                    TextUtil.text("This pickaxe can break normally unbreakable blocks."),
                    TextUtil.text("There's no catch for this one."),
                    TextUtil.empty(),
                    TextUtil.text("Abilities:"),
                    TextUtil.text("• Scarlet Haste", NamedTextColor.DARK_RED)
            ),
            5 * 60
    );

    public final String id;
    public final Material material;
    public final String name;
    public final NamedTextColor color;
    public final List<Component> baseLore;

    public final Integer cooldown;
    public final Integer attackDamage;
    public final Double attackSpeed;

    // full constructor
    CustomItem(String id,
               Material material,
               String name,
               NamedTextColor color,
               List<Component> baseLore,
               Integer cooldown,
               Integer attackDamage,
               Double attackSpeed) {

        this.id = id;
        this.material = material;
        this.name = name;
        this.color = color;
        this.baseLore = baseLore == null ? List.of() : baseLore;
        this.cooldown = cooldown;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    // without stats
    CustomItem(String id,
               Material material,
               String name,
               NamedTextColor color,
               List<Component> baseLore,
               Integer cooldown) {

        this(id, material, name, color, baseLore, cooldown, null, null);
    }

    // without cooldown
    CustomItem(String id,
               Material material,
               String name,
               NamedTextColor color,
               List<Component> baseLore) {

        this(id, material, name, color, baseLore, null, null, null);
    }

    // minimal
    CustomItem(String id,
               Material material,
               String name,
               NamedTextColor color) {

        this(id, material, name, color, List.of(), null, null, null);
    }
}
