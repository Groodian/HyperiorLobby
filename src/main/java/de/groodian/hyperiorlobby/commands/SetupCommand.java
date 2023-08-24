package de.groodian.hyperiorlobby.commands;

import de.groodian.hyperiorcore.command.HCommandPaper;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorlobby.main.Main;
import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class SetupCommand extends HCommandPaper<Player> {

    public SetupCommand(Main plugin) {
        super(Player.class, "lsetup", "Setup the Lobby", Main.PREFIX, "lobby.setup",
                List.of(new SetLocation(plugin, "MinecraftPartyJoin"), new SetLocation(plugin, "Spawn"),
                        new SetLocation(plugin, "Crates"), new SetLocation(plugin, "DailyBonus"),
                        new SetLocation(plugin, "SpawnWarp"), new SetLocation(plugin, "MinecraftPartyWarp"),
                        new SetLocation(plugin, "CratesWarp"), new SetLocation(plugin, "DailyBonusWarp")), List.of());
    }

    @Override
    protected void onCall(Player player, String[] args) {
    }

    private static class SetLocation extends HCommandPaper<Player> {

        private final Main plugin;
        private final String locationName;

        public SetLocation(Main plugin, String locationName) {
            super(Player.class, locationName, "Set the location '" + locationName + "'", Main.PREFIX, null, List.of(), List.of());
            this.plugin = plugin;
            this.locationName = locationName;
        }

        @Override
        public void onCall(Player player, String[] strings) {
            new ConfigLocation(plugin, player.getLocation(), locationName).saveLocation();
            sendMsg(player, "The location '" + locationName + "' was set.", NamedTextColor.GREEN);
        }
    }

}
