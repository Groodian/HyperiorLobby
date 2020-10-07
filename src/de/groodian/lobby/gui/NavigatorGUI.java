package de.groodian.lobby.gui;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.HSound;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.lobby.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class NavigatorGUI implements Listener {

    private static final String GUI_NAME = "Wähle ein Ziel";

    private Main plugin;
    private Inventory inventory;

    public NavigatorGUI(Main plugin) {
        this.plugin = plugin;
        create();
    }

    public void open(Player player) {
        player.openInventory(inventory);
        new HSound(Sound.LEVEL_UP, 10, 4).playFor(player);
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
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §eMinecraft Party")) {
                                teleport(player, "MinecraftPartyWarp");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §aTrails Kiste")) {
                                teleport(player, "CratesWarp");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §bTägliche Belohnungen")) {
                                teleport(player, "DailyBonusWarp");
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §6Spawn")) {
                                teleport(player, "SpawnWarp");
                            }
                        }
                    }
                }
            }
        }
    }

    private void create() {
        inventory = Bukkit.createInventory(null, 27, GUI_NAME);
        inventory.setItem(10, new ItemBuilder(Material.HUGE_MUSHROOM_2).setName("§8» §eMinecraft Party").build());
        inventory.setItem(12, new ItemBuilder(Material.ENDER_CHEST).setName("§8» §aTrails Kiste").build());
        inventory.setItem(14, new ItemBuilder(Material.PRISMARINE_SHARD).setName("§8» §bTägliche Belohnungen").build());
        inventory.setItem(16, new ItemBuilder(Material.GLOWSTONE_DUST).setName("§8» §6Spawn").build());
    }

    private void teleport(Player player, String locationName) {
        ConfigLocation locationUtil = new ConfigLocation(plugin, locationName);
        if (locationUtil.loadLocation() != null) {
            new HSound(Sound.ENDERDRAGON_WINGS).playFor(player);
            player.setVelocity(new Vector(0, 5, 0));
            player.closeInventory();
            new BukkitRunnable() {
                public void run() {
                    player.teleport(locationUtil.loadLocation());
                }
            }.runTaskLater(plugin, 15);
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie " + locationName + "-Location wurde noch nicht gesetzt!");
        }
    }

}
