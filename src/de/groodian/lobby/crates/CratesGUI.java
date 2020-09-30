package de.groodian.lobby.crates;

import de.groodian.hyperiorcore.main.HyperiorCore;
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

public class CratesGUI implements Listener {

    private final String GUI_NAME_SHOP = "Kisten Shop";

    private Main plugin;
    private int price = 1000;

    public CratesGUI(Main plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, GUI_NAME_SHOP);
        new BukkitRunnable() {

            @Override
            public void run() {
                inventory.setItem(0, new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§a").build());
                inventory.setItem(8, new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§a").build());
                inventory.setItem(18, new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§a").build());
                inventory.setItem(26, new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§a").build());
            }
        }.runTaskLater(plugin, 3);
        new BukkitRunnable() {

            @Override
            public void run() {
                inventory.setItem(1, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
                inventory.setItem(7, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
                inventory.setItem(19, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
                inventory.setItem(25, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
            }
        }.runTaskLater(plugin, 6);
        new BukkitRunnable() {

            @Override
            public void run() {
                inventory.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
                inventory.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
                inventory.setItem(20, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
                inventory.setItem(24, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
                inventory.setItem(9, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
                inventory.setItem(17, new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
            }
        }.runTaskLater(plugin, 9);
        new BukkitRunnable() {

            @Override
            public void run() {
                inventory.setItem(12, new ItemBuilder(Material.BOOK).setName("§a§lInfos").setLore("", "§7Gewöhnlich §7- 57%", "§aUngewöhnlich §7- 25%", "§9Selten §7- 13%", "§dEpisch §7- 3%", "§bLegendär §7- 1,5%", "§6Mystisch §7- 0,4%", "§cSchwarzmarktware §7- 0,1%").build());
                inventory.setItem(14, new ItemBuilder(Material.ENDER_CHEST).setName("§a§lÖffne Kiste").setLore("", "§7Öffne diese Kiste für", "§e1.000 §6Coins §7und erhalte einen", "§7zufälligen Kosmetik Gegenstand.").build());
            }
        }.runTaskLater(plugin, 12);
        player.openInventory(inventory);
        new HSound(Sound.CHEST_OPEN).playFor(player);
    }

    @EventHandler
    public void handleGUIClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().getTitle() != null) {
                if (e.getClickedInventory().getTitle().equals(GUI_NAME_SHOP)) {
                    e.setCancelled(true);
                    if (e.getCurrentItem().getItemMeta() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§a§lÖffne Kiste")) {
                                long coins = HyperiorCore.getCoinSystem().getCoins(player);
                                if (coins >= price) {
                                    HyperiorCore.getCoinSystem().removeCoins(player, 1000, true);
                                    new OpenCrates(plugin, player);
                                } else {
                                    player.closeInventory();
                                    player.sendMessage(Main.PREFIX + "§cDir fehlen noch §e" + (price - coins) + " §6Coins §cum diese Kiste zu öffnen.");
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
