package de.jutechs.dupePlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    private static ModConfig config;

    // Initialize with the plugin instance to access the data folder
    public static void initialize(JavaPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "dupeCommandConfig.json");
        loadConfig();
    }

    public static void loadConfig() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                config = GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            config = new ModConfig();
            saveConfig();
        }
    }

    public static void saveConfig() {
        // Ensure the config directory exists
        File configDir = configFile.getParentFile();
        if (!configDir.exists()) {
            configDir.mkdirs(); // Create the directory if it doesn't exist
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
            System.out.println("Config saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModConfig getConfig() {
        return config;
    }
}