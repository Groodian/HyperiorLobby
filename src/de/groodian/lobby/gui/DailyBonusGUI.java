package de.groodian.lobby.gui;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.HSound;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.hyperiorcore.util.Task;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.util.DailyBonus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class DailyBonusGUI implements Listener {

    private static final String GUI_NAME = "Tägliche Belohnung";
    private static final String PLAYER_ITEM_NAME = "§7§lSpieler-Belohnung";
    private static final String VIP_ITEM_NAME = "§e§lVIP-Belohnung";

    private Main plugin;

    public DailyBonusGUI(Main plugin) {
        this.plugin = plugin;
    }

    public void openGUI(final Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 27, GUI_NAME);
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

                new Task(plugin) {
                    @Override
                    public void executeAsync() {
                        cache.add(DailyBonus.canCollect(player, "dailybonus"));
                        cache.add(DailyBonus.canCollect(player, "dailybonusvip"));
                    }

                    @Override
                    public void executeSyncOnFinish() {
                        if ((boolean) cache.get(0)) {
                            inventory.setItem(12, new ItemBuilder(Material.SUGAR).setName(PLAYER_ITEM_NAME).setLore("", "§7Belohnung: §e+500 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
                        } else {
                            inventory.setItem(12, new ItemBuilder(Material.SUGAR).setName(PLAYER_ITEM_NAME).setLore("", "§7Belohnung: §e+500 §6Coins", "", "§cDu hast die Belohnung heute schon eingesammelt.").build());
                        }
                        if (HyperiorCore.getRanks().has(player.getUniqueId(), "dailybonus.vip")) {
                            if ((boolean) cache.get(1)) {
                                inventory.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST).setName(VIP_ITEM_NAME).setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
                            } else {
                                inventory.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST).setName(VIP_ITEM_NAME).setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§cDu hast die Belohnung heute schon eingesammelt.").build());
                            }
                        } else {
                            inventory.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST).setName(VIP_ITEM_NAME).setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§7Diese Belohnung kannst du nur", "§7mit dem §eVIP-Rang §7einsammeln!").build());
                        }
                    }
                };
            }
        }.runTaskLater(plugin, 12);
        player.openInventory(inventory);
        new HSound(Sound.HORSE_ARMOR).playFor(player);
    }

    @EventHandler
    public void handleGUIClick(final InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
        final Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().getTitle() != null) {
                if (e.getClickedInventory().getTitle().equals(GUI_NAME)) {
                    e.setCancelled(true);
                    if (e.getCurrentItem().getItemMeta() != null) {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equals(PLAYER_ITEM_NAME)) {
                                new Task(plugin) {
                                    @Override
                                    public void executeAsync() {
                                        cache.add(DailyBonus.collect(player, "dailybonus"));
                                    }

                                    @Override
                                    public void executeSyncOnFinish() {
                                        if ((boolean) cache.get(0)) {
                                            HyperiorCore.getCoinSystem().addCoins(player, 500, false);
                                            player.closeInventory();
                                            new HSound(Sound.LEVEL_UP).playFor(player);
                                        }
                                    }
                                };
                            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals(VIP_ITEM_NAME)) {
                                if (HyperiorCore.getRanks().has(player.getUniqueId(), "dailybonus.vip")) {
                                    new Task(plugin) {
                                        @Override
                                        public void executeAsync() {
                                            cache.add(DailyBonus.collect(player, "dailybonusvip"));
                                        }

                                        @Override
                                        public void executeSyncOnFinish() {
                                            if ((boolean) cache.get(0)) {
                                                HyperiorCore.getCoinSystem().addCoins(player, 1000, false);
                                                player.closeInventory();
                                                new HSound(Sound.LEVEL_UP).playFor(player);
                                            }
                                        }
                                    };
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}