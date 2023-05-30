package de.groodian.lobby.commands;

import de.groodian.hyperiorcore.command.HCommandPaper;
import de.groodian.hyperiorcore.command.HSubCommandPaper;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.lobby.main.Main;
import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import static de.groodian.lobby.main.Main.PREFIX;

public class SetupCommand extends HCommandPaper<Player> {

    private final Main plugin;

    public SetupCommand(Main plugin) {
        super(Player.class, "lsetup", "Setup the Lobby", PREFIX, "lobby.setup",
                List.of(new SetLocation("MinecraftPartyJoin"), new SetLocation("Spawn"), new SetLocation("Crates"),
                        new SetLocation("DailyBonus"), new SetLocation("SpawnWarp"), new SetLocation("MinecraftPartyWarp"),
                        new SetLocation("CratesWarp"), new SetLocation("DailyBonusWarp")));
        this.plugin = plugin;
    }

    @Override
    protected void onCall(Player player) {
    }

    private static class SetLocation extends HSubCommandPaper<SetupCommand, Player> {

        private final String locationName;

        public SetLocation(String locationName) {
            super(SetupCommand.class, Player.class, locationName, List.of());
            this.locationName = locationName;
        }

        @Override
        public void onCall(Player player, String[] strings) {
            new ConfigLocation(hCommand.plugin, player.getLocation(), locationName).saveLocation();
            sendMsg(player, "The location '" + locationName + "' was set.", NamedTextColor.GREEN);
        }
    }

}
