package de.groodian.lobby.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.groodian.hyperiorcore.ranks.Ranks;
import de.groodian.lobby.main.Main;

public class BuildCommand implements CommandExecutor {

	private Main plugin;

	public BuildCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Ranks.has(player.getUniqueId().toString().replaceAll("-", ""), "lobby.build")) {
				if (!plugin.getBuild().contains(player)) {
					plugin.getBuild().add(player);
					player.setGameMode(GameMode.CREATIVE);
					player.sendMessage(Main.PREFIX + "§aDu bist nun im Bau-Modus.");
				} else {
					plugin.getBuild().remove(player);
					player.setGameMode(GameMode.SURVIVAL);
					player.sendMessage(Main.PREFIX + "§cDu bist nun nicht mehr im Bau-Modus.");
				}
			} else {
				player.sendMessage(Main.PREFIX + "§cDazu hast du keine Rechte.");
			}
		} else
			sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler ausgeführt werden.");
		return false;
	}

}