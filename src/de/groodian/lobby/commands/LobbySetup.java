package de.groodian.lobby.commands;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.lobby.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbySetup implements CommandExecutor {

    private Main plugin;

    public LobbySetup(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (HyperiorCore.getRanks().has(player.getUniqueId(), "lobby.setup")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("mpjoin")) {
                        if (args.length == 1) {
                            new ConfigLocation(plugin, player.getLocation(), "MinecraftPartyJoin").saveLocation();
                            player.sendMessage(Main.PREFIX + "§aDie MinecraftPartyJoin-Location wurde gesetzt.");
                        } else
                            player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup mpjoin");
                    } else if (args[0].equalsIgnoreCase("spawn")) {
                        if (args.length == 1) {
                            new ConfigLocation(plugin, player.getLocation(), "Spawn").saveLocation();
                            player.sendMessage(Main.PREFIX + "§aDie Spawn-Location wurde gesetzt.");
                        } else
                            player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup spawn§c!");
                    } else if (args[0].equalsIgnoreCase("spawnwarp")) {
                        if (args.length == 1) {
                            new ConfigLocation(plugin, player.getLocation(), "SpawnWarp").saveLocation();
                            player.sendMessage(Main.PREFIX + "§aDie Spawn-Warp-Location wurde gesetzt.");
                        } else
                            player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup spawnwarp§c!");
                    } else if (args[0].equalsIgnoreCase("dailybonus")) {
                        if (args.length == 1) {
                            new ConfigLocation(plugin, player.getLocation(), "DailyBonus").saveLocation();
                            player.sendMessage(Main.PREFIX + "§aDie DailyBonus-Location wurde gesetzt.");
                        } else
                            player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup dailybonus§c!");
                    } else if (args[0].equalsIgnoreCase("minecraftpartywarp")) {
                        if (args.length == 1) {
                            new ConfigLocation(plugin, player.getLocation(), "MinecraftPartyWarp").saveLocation();
                            player.sendMessage(Main.PREFIX + "§aDie MinecraftParty-Warp-Location wurde gesetzt.");
                        } else
                            player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup minecraftpartywarp§c!");
                    } else if (args[0].equalsIgnoreCase("crateswarp")) {
                        if (args.length == 1) {
                            new ConfigLocation(plugin, player.getLocation(), "CratesWarp").saveLocation();
                            player.sendMessage(Main.PREFIX + "§aDie Crates-Warp-Location wurde gesetzt.");
                        } else
                            player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup crateswarp§c!");
                    } else if (args[0].equalsIgnoreCase("dailybonuswarp")) {
                        if (args.length == 1) {
                            new ConfigLocation(plugin, player.getLocation(), "DailyBonusWarp").saveLocation();
                            player.sendMessage(Main.PREFIX + "§aDie DailyBonus-Warp-Location wurde gesetzt.");
                        } else
                            player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup dailybonuswarp§c!");
                    } else if (args[0].equalsIgnoreCase("crates")) {
                        if (args.length == 1) {
                            new ConfigLocation(plugin, player.getLocation(), "Crates").saveLocation();
                            player.sendMessage(Main.PREFIX + "§aDie Crates-Location wurde gesetzt.");
                        } else
                            player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup cates§c!");
                    } else
                        player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup <mpjoin/spawn/spawnwarp/dailybonus/minecraftpartywarp/crateswarp/dailybonuswarp/crates>§c!");
                } else
                    player.sendMessage(Main.PREFIX + "§cBenutze §6/lsetup <mpjoin/spawn/spawnwarp/dailybonus/minecraftpartywarp/crateswarp/dailybonuswarp/crates>§c!");
            } else
                player.sendMessage(Main.PREFIX + "§cDazu hast du keine Rechte.");
        } else
            sender.sendMessage(Main.PREFIX + "Dieser Befehl muss von einem Spieler ausgeführt werden.");
        return false;
    }
}