package de.groodian.lobby.gui;

import de.groodian.hyperiorcore.util.HSound;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.network.MinecraftPartyServiceInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinecraftPartyGUI implements Listener {

    private static final String MAIN_GUI_NAME = "Wähle einen Server:";
    private static final String RUNNING_SERVERS_NAME = "Alle laufende Runden:";

    private Main plugin;
    private Map<Player, Inventory> inventories;
    private ItemStack animationItem;

    public MinecraftPartyGUI(Main plugin) {
        this.plugin = plugin;
        inventories = new HashMap<>();
        animationItem = (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build();
    }

    private void setLobbyServices(Inventory inventory) {
        int i = 0;
        for (Map.Entry<String, MinecraftPartyServiceInfo> info : plugin.getClient().getMinecraftPartyServiceInfos().entrySet()) {
            if (info.getValue().getGameState().equalsIgnoreCase("Lobby")) {
                inventory.setItem(i, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 4))
                        .setName("§a" + info.getKey())
                        .setLore("§7Status: §6" + info.getValue().getGameState(), "§7Spieler: §6" + info.getValue().getOnlinePlayers() + "/" + info.getValue().getMaxPlayers())
                        .build()
                );
                i++;
            }
        }
    }

    private void setRunningServices(Inventory inventory) {
        int i = 0;
        for (Map.Entry<String, MinecraftPartyServiceInfo> info : plugin.getClient().getMinecraftPartyServiceInfos().entrySet()) {
            if (!info.getValue().getGameState().equalsIgnoreCase("Lobby")) {
                inventory.setItem(i, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 5))
                        .setName("§a" + info.getKey())
                        .setLore("§7Status: §6" + info.getValue().getGameState(), "§7Spieler: §6" + info.getValue().getOnlinePlayers() + "/" + info.getValue().getMaxPlayers())
                        .build()
                );
                i++;
            }
        }
    }

    private void animation(Inventory inventory) {
        setDelayedSlot(inventory, 27, animationItem, 2);
        setDelayedSlot(inventory, 28, animationItem, 3);
        setDelayedSlot(inventory, 29, animationItem, 4);
        setDelayedSlot(inventory, 30, animationItem, 5);
        setDelayedSlot(inventory, 31, animationItem, 6);
        setDelayedSlot(inventory, 32, animationItem, 7);
        setDelayedSlot(inventory, 33, animationItem, 8);
        setDelayedSlot(inventory, 34, animationItem, 9);
        setDelayedSlot(inventory, 35, animationItem, 10);
    }

    private void setDelayedSlot(Inventory inventory, int slot, ItemStack item, int delay) {
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(slot, item);
            }
        }.runTaskLater(plugin, delay);
    }

    public void update() {
        List<Player> playersToRemove = new ArrayList<>();
        for (Map.Entry<Player, Inventory> players : inventories.entrySet()) {
            Player player = players.getKey();
            if (player.isOnline()) {
                if (player.getOpenInventory() != null) {
                    if (player.getOpenInventory().getTitle() != null) {
                        if (player.getOpenInventory().getTitle().equals(MAIN_GUI_NAME)) {
                            Inventory inventory = inventories.get(player);
                            for (int i = 0; i < 27; i++) {
                                inventory.clear(i);
                            }
                            setLobbyServices(inventory);
                            player.updateInventory();
                            System.out.println("Update " + MAIN_GUI_NAME + " for: " + player.getName());
                            continue;
                        } else if (player.getOpenInventory().getTitle().equals(RUNNING_SERVERS_NAME)) {
                            Inventory inventory = inventories.get(player);
                            for (int i = 0; i < 27; i++) {
                                inventory.clear(i);
                            }
                            setRunningServices(inventory);
                            player.updateInventory();
                            System.out.println("Update " + RUNNING_SERVERS_NAME + " for: " + player.getName());
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

    public void openMainGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, MAIN_GUI_NAME);
        animation(inventory);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(42, new ItemBuilder(Material.ITEM_FRAME).setName("§aLaufende Runden").setLore("", "§7Klicke hier, um alle", "§7laufenden Runden zu sehen.").build());
                inventory.setItem(38, new ItemBuilder(Material.WOOL, (short) 5).setName("§aQuick Play").setLore("", "§7Klicke hier, um eine", "§7zufällige Lobby zu betreten.").build());
                setLobbyServices(inventory);
                inventories.put(player, inventory);
            }
        }.runTaskLater(plugin, 8L);
        player.openInventory(inventory);
        new HSound(Sound.HORSE_SADDLE).playFor(player);
    }

    public void openRunningServersGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 45, RUNNING_SERVERS_NAME);
        animation(inventory);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(36, new ItemBuilder(Material.ARROW).setName("§cZurück").build());
                setRunningServices(inventory);
                inventories.put(player, inventory);
            }
        }.runTaskLater(plugin, 8L);
        player.openInventory(inventory);
        new HSound(Sound.HORSE_SADDLE).playFor(player);
    }

    @EventHandler
    public void handleGUIClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().getTitle() != null) {

                if (e.getClickedInventory().getTitle().equals(MAIN_GUI_NAME)) {
                    e.setCancelled(true);

                    if (e.getCurrentItem().getItemMeta() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {

                            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aLaufende Runden")) {
                                openRunningServersGUI(player);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aQuick Play")) {
                                for (Map.Entry<String, MinecraftPartyServiceInfo> info : plugin.getClient().getMinecraftPartyServiceInfos().entrySet()) {
                                    if (info.getValue().getGameState().equalsIgnoreCase("Lobby")) {
                                        sendToServer(player, info.getKey());
                                    }
                                }
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().contains("MINECRAFTPARTY")) {
                                sendToServer(player, e.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
                            }

                        }
                    }

                } else if (e.getClickedInventory().getTitle().equals(RUNNING_SERVERS_NAME)) {
                    e.setCancelled(true);

                    if (e.getCurrentItem().getItemMeta() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {

                            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cZurück")) {
                                openMainGUI(player);
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().toUpperCase().contains("MINECRAFTPARTY")) {
                                sendToServer(player, e.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
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