package de.groodian.lobby.gui;

import de.groodian.hyperiorcore.gui.GUI;
import de.groodian.hyperiorcore.gui.GUIRunnable;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.network.LobbyServiceInfo;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class LobbyGUI extends GUI {

    private final Main plugin;

    public LobbyGUI(Main plugin) {
        super(Component.text("Lobbys"), 9);
        this.plugin = plugin;
    }

    @Override
    protected void onOpen() {
        setLobbyServices();
    }

    private void setLobbyServices() {
        int i = 0;
        for (Map.Entry<Integer, LobbyServiceInfo> info : plugin.getClient().getLobbyServiceInfos().entrySet()) {
            Material material = Material.WHITE_STAINED_GLASS_PANE;
            GUIRunnable guiRunnable = () -> {
                player.sendMessage(Main.PREFIX_LEGACY + "§cTODO!");
            };
            if (info.getKey() == plugin.getGroupNumber()) {
                material = Material.LIME_STAINED_GLASS_PANE;
                guiRunnable = () -> {
                    player.sendMessage(Main.PREFIX_LEGACY + "§cDu bist bereits auf dieser Lobby!");
                };
            }
            putItem(new ItemBuilder(material).setName("§a" + info.getKey())
                    .setLore("§7Spieler: §6" + info.getValue().getOnlinePlayers() + "/" + info.getValue().getMaxPlayers())
                    .build(), i, guiRunnable);
            i++;
        }
    }

    @Override
    public void onUpdate() {
        inventory.clear();
        setLobbyServices();
    }

}
