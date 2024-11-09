package de.jutechs.dupePlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DupeInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final long COOLDOWN_TIME = ConfigManager.getConfig().DupeCommandCooldown;
        commandSender.sendMessage(ChatColor.GOLD+"The current delay for using /dupe is "+ChatColor.YELLOW+COOLDOWN_TIME+ ChatColor.GOLD +" milliseconds.");
        return true;
    }
}
