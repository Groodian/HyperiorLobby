package de.groodian.lobby.gui;

import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.network.LobbyServiceInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyGUI implements Listener {

    private static final String GUI_NAME = "Lobbys";

    private Main plugin;
    private Map<Player, Inventory> inventories;

    public LobbyGUI(Main plugin) {
        this.plugin = plugin;
        inventories = new HashMap<>();
    }

    public void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, GUI_NAME);
        inventories.put(player, inventory);
        setLobbyServices(inventory);
        player.openInventory(inventory);
    }

    public void update() {
        List<Player> playersToRemove = new ArrayList<>();
        for (Map.Entry<Player, Inventory> players : inventories.entrySet()) {
            Player player = players.getKey();
            if (player.isOnline()) {
                if (player.getOpenInventory() != null) {
                    if (player.getOpenInventory().getTitle() != null) {
                        if (player.getOpenInventory().getTitle().equals(GUI_NAME)) {
                            Inventory inventory = inventories.get(player);
                            inventory.clear();
                            setLobbyServices(inventory);
                            player.updateInventory();
                            System.out.println("Update " + GUI_NAME + " for: " + player.getName());
                            continue;
                        }
                    }
                }
            }
            playersToRemove.add(player);
        }
        for (Player player : playersToRemove) {
            System.out.println("Removed " + player.getName());
            inventories.remove(player);
        }
    }

    private void setLobbyServices(Inventory inventory) {
        int i = 0;
        for (Map.Entry<String, LobbyServiceInfo> info : plugin.getClient().getLobbyServiceInfos().entrySet()) {
            short color = 0;
            if (info.getKey().equalsIgnoreCase(Bukkit.getMotd())) {
                color = 5;
            }
            inventory.setItem(i, (new ItemBuilder(Material.STAINED_GLASS_PANE, color))
                    .setName("§a" + info.getKey())
                    .setLore("§7Spieler: §6" + info.getValue().getOnlinePlayers() + "/" + info.getValue().getMaxPlayers())
                    .build()
            );
            i++;
        }
    }

    @EventHandler
    public void handleGUIClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().getTitle() != null) {
                if (e.getClickedInventory().getTitle().equals(GUI_NAME)) {
                    e.setCancelled(true);
                    if (e.getCurrentItem().getItemMeta() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            String cleanItemName = e.getCurrentItem().getItemMeta().getDisplayName().replace("§a", "");
                            if (cleanItemName.toUpperCase().contains("LOBBY")) {
                                if(cleanItemName.equalsIgnoreCase(Bukkit.getMotd())) {
                                    player.sendMessage(Main.PREFIX + "§cDu bist bereits auf dieser Lobby!");
                                } else {
                                    sendToServer(player, cleanItemName);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendToServer(Player player, String serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }

}
