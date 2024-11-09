package de.jutechs.dupePlugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager.initialize(this);
        getCommand("dupe").setExecutor(new DupeCommand());
        getCommand("dupeinfo").setExecutor(new DupeInfoCommand());
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        ConfigManager.saveConfig();
        // Plugin shutdown logic
    }
}
