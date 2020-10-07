package de.groodian.lobby.boards;

import de.groodian.hyperiorcore.boards.HScoreboard;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.ranks.Rank;
import de.groodian.lobby.main.Main;
import org.bukkit.entity.Player;

public class LobbyScoreboard {

    private Main plugin;
    private HScoreboard sb;

    public LobbyScoreboard(Main plugin) {
        this.plugin = plugin;
        sb = HyperiorCore.getSB();
    }

    public void set(Player player) {
        Rank rank = HyperiorCore.getRanks().get(player.getUniqueId());
        sb.updateLine(0, player, "");
        sb.updateLine(1, player, "§fDein Rang§7:");
        sb.updateLine(2, player, (rank.getColor() + rank.getName()));
        sb.updateLine(3, player, " ");
        sb.updateLine(4, player, "§fCoins§7:");
        sb.updateLine(5, player, "§e" + HyperiorCore.getCoinSystem().getCoins(player));
        sb.updateLine(6, player, "  ");
        sb.updateLine(7, player, "§fOnline§7:");
        sb.updateLine(8, player, "§7" + plugin.getOnlineCount());
        sb.updateLine(9, player, "   ");
        sb.updateLine(10, player, "§fTeamSpeak§7:");
        sb.updateLine(11, player, "§ehyperior.de");
    }

    public void update(Player player) {
        Rank rank = HyperiorCore.getRanks().get(player.getUniqueId());
        sb.updateLine(2, player, (rank.getColor() + rank.getName()));
        sb.updateLine(5, player, "§e" + HyperiorCore.getCoinSystem().getCoins(player));
        sb.updateLine(8, player, "§7" + plugin.getOnlineCount());
    }

}
