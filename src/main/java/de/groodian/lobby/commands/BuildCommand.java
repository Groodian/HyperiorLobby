package de.groodian.lobby.commands;

import de.groodian.hyperiorcore.command.HCommandPaper;
import de.groodian.lobby.main.Main;
import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import static de.groodian.lobby.main.Main.PREFIX;

public class BuildCommand extends HCommandPaper<Player> {

    private final Main plugin;

    public BuildCommand(Main plugin) {
        super(Player.class, "build", "Build in the Lobby", PREFIX, "lobby.build", List.of());
        this.plugin = plugin;
    }

    @Override
    protected void onCall(Player player) {
        if (!plugin.getBuild().contains(player)) {
            plugin.getBuild().add(player);
            player.setGameMode(GameMode.CREATIVE);
            sendMsg(player, "You can now build.", NamedTextColor.GREEN);
        } else {
            plugin.getBuild().remove(player);
            player.setGameMode(GameMode.SURVIVAL);
            sendMsg(player, "You can no longer build.", NamedTextColor.RED);
        }
    }

}
