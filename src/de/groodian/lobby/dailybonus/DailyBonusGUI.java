package de.groodian.lobby.dailybonus;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyBonusGUI implements Listener {

    private Main plugin;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");

    private static final String GUI_NAME = "Tägliche Belohnung";

    private static final String PLAYER_ITEM_NAME = "§7§lSpieler-Belohnung";
    private static final String VIP_ITEM_NAME = "§e§lVIP-Belohnung";

    public DailyBonusGUI(Main plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, GUI_NAME);
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
                if (DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonus") == null) {
                    inventory.setItem(12, new ItemBuilder(Material.SUGAR).setName(PLAYER_ITEM_NAME).setLore("", "§7Belohnung: §e+500 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
                } else {
                    LocalDate date = LocalDate.parse(DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonus"), dateFormatter);
                    if (!date.equals(LocalDate.now())) {
                        inventory.setItem(12, new ItemBuilder(Material.SUGAR).setName(PLAYER_ITEM_NAME).setLore("", "§7Belohnung: §e+500 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
                    } else {
                        inventory.setItem(12, new ItemBuilder(Material.SUGAR).setName(PLAYER_ITEM_NAME).setLore("", "§7Belohnung: §e+500 §6Coins", "", "§cDu hast die Belohnung heute schon eingesammelt.").build());
                    }
                }
                if (HyperiorCore.getRanks().has(player.getUniqueId(), "dailybonus.vip")) {
                    if (DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusvip") == null) {
                        inventory.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST).setName(VIP_ITEM_NAME).setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
                    } else {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
                        LocalDate date = LocalDate.parse(DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusvip"), dateFormatter);
                        if (!date.equals(LocalDate.now())) {
                            inventory.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST).setName(VIP_ITEM_NAME).setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
                        } else {
                            inventory.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST).setName(VIP_ITEM_NAME).setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§cDu hast die Belohnung heute schon eingesammelt.").build());
                        }
                    }
                } else {
                    inventory.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST).setName(VIP_ITEM_NAME).setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§7Diese Belohnung kannst du nur", "§7mit dem §eVIP-Rang §7einsammeln!").build());
                }
            }
        }.runTaskLater(plugin, 12);
        player.openInventory(inventory);
        new HSound(Sound.HORSE_ARMOR).playFor(player);
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
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(PLAYER_ITEM_NAME)) {
                                if (DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonus") == null) {
                                    DailyBonusMySQL.set(player, "dailybonus", dateFormatter.format(LocalDate.now()));
                                    HyperiorCore.getCoinSystem().addCoins(player, 500, false);
                                    player.closeInventory();
                                    new HSound(Sound.LEVEL_UP).playFor(player);
                                } else {
                                    LocalDate date = LocalDate.parse(DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonus"), dateFormatter);
                                    if (!date.equals(LocalDate.now())) {
                                        DailyBonusMySQL.set(player, "dailybonus", dateFormatter.format(LocalDate.now()));
                                        HyperiorCore.getCoinSystem().addCoins(player, 500, false);
                                        player.closeInventory();
                                        new HSound(Sound.LEVEL_UP).playFor(player);
                                    }
                                }
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(VIP_ITEM_NAME)) {
                                if (HyperiorCore.getRanks().has(player.getUniqueId(), "dailybonus.vip")) {
                                    if (DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusvip") == null) {
                                        DailyBonusMySQL.set(player, "dailybonusvip", dateFormatter.format(LocalDate.now()));
                                        HyperiorCore.getCoinSystem().addCoins(player, 1000, false);
                                        player.closeInventory();
                                        new HSound(Sound.LEVEL_UP).playFor(player);
                                    } else {
                                        LocalDate date = LocalDate.parse(DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusvip"), dateFormatter);
                                        if (!date.equals(LocalDate.now())) {
                                            DailyBonusMySQL.set(player, "dailybonusvip", dateFormatter.format(LocalDate.now()));
                                            HyperiorCore.getCoinSystem().addCoins(player, 1000, false);
                                            player.closeInventory();
                                            new HSound(Sound.LEVEL_UP).playFor(player);
                                        }
                                    }
                                }
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
}