package de.groodian.lobby.boards;

import org.bukkit.entity.Player;

import de.groodian.hyperiorcore.coinsystem.CoinSystem;
import de.groodian.hyperiorcore.ranks.Ranks;
import de.groodian.lobby.main.Main;

public class LobbyScoreboard {

	private Main plugin;

	public LobbyScoreboard(Main plugin) {
		this.plugin = plugin;
	}

	public void updateLobbyScoreboard(Player player) {
		String rank = Ranks.getRank(player.getUniqueId().toString().replaceAll("-", ""));
		plugin.getScoreborad().unregisterObjectiveFor(player);
		plugin.getScoreborad().send(player, "LobbyBoard", "§f§lHYPERIOR.DE");
		plugin.getScoreborad().add("", 11);
		plugin.getScoreborad().add("§fDein Rang§7:", 10);
		plugin.getScoreborad().add(((rank == null) ? "§aSpieler" : rank), 9);
		plugin.getScoreborad().add(" ", 8);
		plugin.getScoreborad().add("§fCoins§7:", 7);
		plugin.getScoreborad().add("§e" + CoinSystem.getCoins(player), 6);
		plugin.getScoreborad().add("  ", 5);
		plugin.getScoreborad().add("§fOnline§7:", 4);
		plugin.getScoreborad().add("§7" + plugin.getOnlineCount(), 3);
		plugin.getScoreborad().add("   ", 2);
		plugin.getScoreborad().add("§fTeamSpeak§7:", 1);
		plugin.getScoreborad().add("§ehyperior.de", 0);
	}

}
