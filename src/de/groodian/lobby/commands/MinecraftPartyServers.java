package de.groodian.lobby.commands;

import de.groodian.lobby.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinecraftPartyServers implements CommandExecutor {

    public Main plugin;

    public MinecraftPartyServers(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                plugin.getGui().openMainGUI(player);
            }
        }
        return false;
    }

}
