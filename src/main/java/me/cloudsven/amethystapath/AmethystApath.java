package me.cloudsven.amethystapath;

import me.cloudsven.amethystapath.commands.AmethystApathCommand;
import me.cloudsven.amethystapath.commands.AmethystApathTabCompleter;
import me.cloudsven.amethystapath.items.ItemFactory;
import me.cloudsven.amethystapath.listeners.*;
import me.cloudsven.amethystapath.recipes.RecipeManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AmethystApath extends JavaPlugin {
    private static AmethystApath instance;

    private void registerListeners(Listener... listeners) {
        PluginManager pm = getServer().getPluginManager();
        for (Listener listener : listeners) {
            pm.registerEvents(listener, this);
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        if(getServer().getPluginManager().getPlugin("CarpetTIS") == null) {
            getLogger().severe("CarpetTIS not found! Disabling Amethyst Apath.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ItemFactory.init(this);
        RecipeManager.registerAll(this);

        registerListeners(
                new TotemListener(this),
                new CrossbowListener(this),
                new CleaverListener(this),
                new MaceListener(this),
                new CraftingListener(this),
                new ShardedTntListener(this),
                new ScarletBladeListener(this),
                new ShieldListener(this),
                new PickaxeListener(this),
                new ScarletCoreListener(this),
                new VillagerListener(this)
        );

        Objects.requireNonNull(getCommand("amethystapath")).setExecutor(
                new AmethystApathCommand(this)
        );

        var cmd = getCommand("amethystapath");
        Objects.requireNonNull(cmd).setExecutor(new AmethystApathCommand(this));
        cmd.setTabCompleter(new AmethystApathTabCompleter());

        // console registration message
        getLogger().info("====================================");
        getLogger().info(" Amethyst Apath has loaded successfully!");
        getLogger().info(" Version: " + getPluginMeta().getVersion());
        getLogger().info(" Author: cloudsven");
        getLogger().info("====================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("Amethyst Apath has been unloaded.");
    }

    public static AmethystApath getInstance() {
        return instance;
    }
}
