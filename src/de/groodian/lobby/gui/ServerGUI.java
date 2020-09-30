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
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerGUI implements Listener {

    private Main plugin;
    private final String MAIN_GUI_NAME = "Wähle einen Server:";
    private final String RUNNING_SERVERS_NAME = "Alle laufende Runden:";

    private Map<Player, Inventory> inventorys;

    public ServerGUI(Main plugin) {
        this.plugin = plugin;
        inventorys = new HashMap<>();
    }

    private void setLobbyGuiServices(Inventory inventory) {
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

    private void setRunningGuiServices(Inventory inventory) {
        int i = 0;
        for (Map.Entry<String, MinecraftPartyServiceInfo> info : plugin.getClient().getMinecraftPartyServiceInfos().entrySet()) {
            if (!info.getValue().getGameState().equalsIgnoreCase("Lobby")) {
                inventory.setItem(i, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 4))
                        .setName("§a" + info.getKey())
                        .setLore("§7Status: §6" + info.getValue().getGameState(), "§7Spieler: §6" + info.getValue().getOnlinePlayers() + "/" + info.getValue().getMaxPlayers())
                        .build()
                );
                i++;
            }
        }
    }

    private void animation(Inventory inventory) {
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(27, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build());
            }
        }.runTaskLater(plugin, 2L);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(28, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build());
            }
        }.runTaskLater(plugin, 3L);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(29, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build());
            }
        }.runTaskLater(plugin, 4L);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(30, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build());
            }
        }.runTaskLater(plugin, 5L);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(31, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build());
            }
        }.runTaskLater(plugin, 6L);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(32, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build());
            }
        }.runTaskLater(plugin, 7L);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(33, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build());
            }
        }.runTaskLater(plugin, 8L);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(34, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build());
            }
        }.runTaskLater(plugin, 9L);
        new BukkitRunnable() {
            public void run() {
                inventory.setItem(35, (new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7)).setName("§a").build());
            }
        }.runTaskLater(plugin, 10L);
    }

    public void update() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTitle().equals(MAIN_GUI_NAME)) {
                if (inventorys.containsKey(player)) {
                    final Inventory inventory = inventorys.get(player);
                    for (int i = 0; i < 27; i++) {
                        inventory.clear(i);
                    }
                    setLobbyGuiServices(inventory);
                    player.updateInventory();
                }
            } else if (player.getOpenInventory().getTitle().equals(RUNNING_SERVERS_NAME)) {
                if (inventorys.containsKey(player)) {
                    final Inventory inventory = inventorys.get(player);
                    for (int i = 0; i < 27; i++) {
                        inventory.clear(i);
                    }
                    setRunningGuiServices(inventory);
                    player.updateInventory();
                }
            }
        }
    }

    public void openMainGUI(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 45, MAIN_GUI_NAME);
        animation(inventory);
        new BukkitRunnable() {
            public void run() {
                int i = 0;
                inventory.setItem(42, new ItemBuilder(Material.ITEM_FRAME).setName("§aLaufende Runden").setLore("", "§7Klicke hier, um alle", "§7laufenden Runden zu sehen.").build());
                inventory.setItem(38, new ItemBuilder(Material.WOOL, (short) 5).setName("§aQuick Play").setLore("", "§7Klicke hier, um eine", "§7zufällige Lobby zu betreten.").build());
                setLobbyGuiServices(inventory);
                inventorys.put(player, inventory);
            }
        }.runTaskLater(plugin, 8L);
        player.openInventory(inventory);
        new HSound(Sound.HORSE_SADDLE).playFor(player);
    }

    public void openRunningServersGUI(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 45, RUNNING_SERVERS_NAME);
        animation(inventory);
        new BukkitRunnable() {
            public void run() {
                int i = 0;
                inventory.setItem(36, new ItemBuilder(Material.ARROW).setName("§cZurück").build());
                setRunningGuiServices(inventory);
                inventorys.put(player, inventory);
            }
        }.runTaskLater(plugin, 8L);
        player.openInventory(inventory);
        new HSound(Sound.HORSE_SADDLE).playFor(player);
    }

    @EventHandler
    public void handleGUIClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player))
            return;
        Player player = (Player) e.getPlayer();
        if (e.getInventory() != null) {
            if (e.getInventory().getTitle() != null) {
                if (e.getInventory().getTitle().equals(MAIN_GUI_NAME) || e.getInventory().getTitle().equals(RUNNING_SERVERS_NAME)) {
                    if (inventorys.containsKey(player)) {
                        inventorys.remove(player);
                    }
                }
            }
        }
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
                                return;
                            }
                        }
                    }

                    if (e.getCurrentItem().getItemMeta() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aQuick Play")) {
                                for (Map.Entry<String, MinecraftPartyServiceInfo> info : plugin.getClient().getMinecraftPartyServiceInfos().entrySet()) {
                                    if (info.getValue().getGameState().equalsIgnoreCase("Lobby")) {
                                        ByteArrayOutputStream b = new ByteArrayOutputStream();
                                        DataOutputStream out = new DataOutputStream(b);
                                        try {
                                            out.writeUTF("Connect");
                                            out.writeUTF(info.getKey());
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    if (e.getCurrentItem().getItemMeta() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("MinecraftParty")) {
                                ByteArrayOutputStream b = new ByteArrayOutputStream();
                                DataOutputStream out = new DataOutputStream(b);
                                try {
                                    out.writeUTF("Connect");
                                    out.writeUTF(e.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                                return;
                            }
                        }
                    }
                }

                if (e.getClickedInventory().getTitle().equals(RUNNING_SERVERS_NAME)) {
                    e.setCancelled(true);

                    if (e.getCurrentItem().getItemMeta() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cZurück")) {
                                openMainGUI(player);
                                return;
                            }
                        }
                    }

                    if (e.getCurrentItem().getItemMeta() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("MinecraftParty")) {
                                ByteArrayOutputStream b = new ByteArrayOutputStream();
                                DataOutputStream out = new DataOutputStream(b);
                                try {
                                    out.writeUTF("Connect");
                                    out.writeUTF(e.getCurrentItem().getItemMeta().getDisplayName().replace("§a", ""));
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}