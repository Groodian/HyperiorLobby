package de.groodian.hyperiorlobby.boards;

import de.groodian.hyperiorcore.boards.HScoreboard;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.user.Rank;
import de.groodian.hyperiorcore.user.User;
import de.groodian.hyperiorlobby.main.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class LobbyScoreboard {

    private final Main plugin;
    private final HScoreboard sb;

    public LobbyScoreboard(Main plugin) {
        this.plugin = plugin;
        this.sb = HyperiorCore.getPaper().getScoreboard();
    }

    public void set(Player player) {
        User user = HyperiorCore.getPaper().getUserManager().get(player.getUniqueId());
        Rank rank = user.getRank();

        sb.updateLine(0, player, Component.text(""));
        sb.updateLine(1, player, Component.text("Dein Rang", NamedTextColor.WHITE).append(Component.text(":", NamedTextColor.GRAY)));
        sb.updateLine(2, player, Component.text(rank.name(), rank.color()));
        sb.updateLine(3, player, Component.text(" "));
        sb.updateLine(4, player, Component.text("Coins", NamedTextColor.WHITE).append(Component.text(":", NamedTextColor.GRAY)));
        sb.updateLine(5, player, Component.text(user.getCoins(), NamedTextColor.YELLOW));
        sb.updateLine(6, player, Component.text("  "));
        sb.updateLine(7, player, Component.text("Online", NamedTextColor.WHITE).append(Component.text(":", NamedTextColor.GRAY)));
        sb.updateLine(8, player, Component.text(plugin.getOnlineCount(), NamedTextColor.GRAY));
        sb.updateLine(9, player, Component.text("   "));
        sb.updateLine(10, player, Component.text("Discord", NamedTextColor.WHITE).append(Component.text(":", NamedTextColor.GRAY)));
        sb.updateLine(11, player, Component.text("/discord", NamedTextColor.YELLOW));
    }

    public void update(Player player) {
        User user = HyperiorCore.getPaper().getUserManager().get(player.getUniqueId());
        Rank rank = user.getRank();

        sb.updateLine(2, player, Component.text(rank.name(), rank.color()));
        sb.updateLine(5, player, Component.text(user.getCoins(), NamedTextColor.YELLOW));
        sb.updateLine(8, player, Component.text(plugin.getOnlineCount(), NamedTextColor.GRAY));
    }

}
