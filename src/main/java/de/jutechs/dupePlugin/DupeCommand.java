package de.jutechs.dupePlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DupeCommand implements CommandExecutor {

    // Cooldown map for players
    private static final Map<UUID, Long> cooldownMap = new HashMap<>();
    private static final long COOLDOWN_TIME = ConfigManager.getConfig().DupeCommandCooldown; // Cooldown time in milliseconds

    // Command count map for players
    private static final Map<UUID, Integer> commandCountMap = new HashMap<>();
    private static final File COMMAND_COUNT_FILE = new File("commandCounts.json");

    static {
        loadCommandCounts();
    }

    // Load command counts from file
    private static void loadCommandCounts() {
        if (!COMMAND_COUNT_FILE.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(COMMAND_COUNT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(":");
                if (split.length == 2) {
                    UUID playerId = UUID.fromString(split[0]);
                    int count = Integer.parseInt(split[1]);
                    commandCountMap.put(playerId, count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save command counts to file
    private static void saveCommandCounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COMMAND_COUNT_FILE))) {
            for (Map.Entry<UUID, Integer> entry : commandCountMap.entrySet()) {
                writer.write(entry.getKey().toString() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Cooldown check
        if (cooldownMap.containsKey(playerId)) {
            long lastUseTime = cooldownMap.get(playerId);
            if (currentTime - lastUseTime < COOLDOWN_TIME) {
                long timeLeft = (COOLDOWN_TIME - (currentTime - lastUseTime)) / 1000;
                player.sendMessage(ChatColor.RED + "Please wait " + timeLeft + " more seconds before using /dupe again.");
                return true;
            }
        }

        // Duplicate the player's inventory
        dupeInventory(player);

        // Update cooldown
        cooldownMap.put(playerId, currentTime);

        // Increment command count for the player
        commandCountMap.put(playerId, commandCountMap.getOrDefault(playerId, 0) + 1);
        saveCommandCounts(); // Save updated counts to file

        // Notify the player how many times they've used the command
        int usageCount = commandCountMap.get(playerId);
        player.sendMessage(ChatColor.YELLOW + "You have used /dupe " + usageCount + " times.");

        return true;
    }

    private static void dupeInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                player.getWorld().dropItem(player.getLocation(), item.clone());
            }
        }
        player.sendMessage(ChatColor.GREEN + "Your inventory has been duplicated!");
    }
}