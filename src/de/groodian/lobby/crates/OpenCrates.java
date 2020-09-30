package de.groodian.lobby.crates;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.HSound;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.lobby.cosmetics.Cosmetics;
import de.groodian.lobby.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class OpenCrates {

    private final Main plugin;
    private final Inventory inventory;
    private boolean isRunning;
    private final int[] delay = {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 10, 15, 16};
    private final int[] frame = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
    private final String gewoehnlichName = "§7Gewöhnlich";
    private final String unngewoehnlichName = "§aUngewöhnlich";
    private final String seltenName = "§9Selten";
    private final String epischName = "§dEpisch";
    private final String legendaerName = "§bLegendär";
    private final String mystischName = "§6Mystisch";
    private final String schwarzmarkwareName = "§cSchwarzmarktware";

    private final String[] gewoehnlich = {
            "Lederschuhe",
            "Lederhose",
            "Lederbrustplatte",
            "Lederhelm",
            "Kettenschuhe",
            "Kettenhose",
            "Kettenbrustplatte",
            "Kettenhelm"
    };

    private final String[] ungewoehnlich = {
            "Goldschuhe",
            "Goldhose",
            "Goldbrustplatte",
            "Goldhelm",
            "Diamantschuhe",
            "Diamanthose",
            "Diamantbrustplatte",
            "Diamanthelm"
    };

    private final String[] selten = {
            "GoldBlockSpur",
            "WasserPartikel",
            "LavaPartikel",
            "BlazePartikel"
    };

    private final String[] episch = {
            "RegenbogenWolleSpur",
            "RegenbogenGlasSpur",
            "RegenbogenLehmSpur",
            "FlammenPartikel",
            "GruenePartikel",
            "Regenbogenschuhe",
            "Regenbogenhose",
            "Regenbogenbrustplatte",
            "Regenbogenhelm"
    };

    private final String[] legendaer = {
            "GetreideSpur",
            "RegenbogenPartikel",
            "RoteRinge"
    };

    private final String[] mystisch = {
            "Feuerwerk"
    };

    private final String[] schwarzmarktware = {
            "FlugStab"
    };

    private final ItemBuilder[] gewoehnlichItem = {
            new ItemBuilder(Material.LEATHER_BOOTS).setName("§7§lLederschuhe"),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setName("§7§lLederhose"),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§7§lLederbrustplatte"),
            new ItemBuilder(Material.LEATHER_HELMET).setName("§7§lLederhelm"),
            new ItemBuilder(Material.CHAINMAIL_BOOTS).setName("§7§lKettenschuhe"),
            new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setName("§7§lKettenhose"),
            new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setName("§7§lKettenbrustplatte"),
            new ItemBuilder(Material.CHAINMAIL_HELMET).setName("§7§lKettenhelm")
    };

    private final ItemBuilder[] ungewoehnlichItem = {
            new ItemBuilder(Material.GOLD_BOOTS).setName("§a§lGoldschuhe"),
            new ItemBuilder(Material.GOLD_LEGGINGS).setName("§a§lGoldhose"),
            new ItemBuilder(Material.GOLD_CHESTPLATE).setName("§a§lGoldbrustplatte"),
            new ItemBuilder(Material.GOLD_HELMET).setName("§a§lGoldhelm"),
            new ItemBuilder(Material.DIAMOND_BOOTS).setName("§a§lDiamantschuhe"),
            new ItemBuilder(Material.DIAMOND_LEGGINGS).setName("§a§lDiamanthose"),
            new ItemBuilder(Material.DIAMOND_CHESTPLATE).setName("§a§lDiamantbrustplatte"),
            new ItemBuilder(Material.DIAMOND_HELMET).setName("§a§lDiamanthelm")
    };

    private final ItemBuilder[] seltenItem = {
            new ItemBuilder(Material.GOLD_BLOCK).setName("§9§lGoldBlockSpur"),
            new ItemBuilder(Material.WATER_BUCKET).setName("§9§lWasserPartikel"),
            new ItemBuilder(Material.LAVA_BUCKET).setName("§9§lLavaPartikel"),
            new ItemBuilder(Material.BLAZE_ROD).setName("§9§lBlazePartikel")
    };

    private final ItemBuilder[] epischItem = {
            new ItemBuilder(Material.WOOL).setName("§d§lRegenbogenWolleSpur"),
            new ItemBuilder(Material.GLASS).setName("§d§lRegenbogenGlasSpur"),
            new ItemBuilder(Material.STAINED_CLAY).setName("§d§lRegenbogenLehmSpur"),
            new ItemBuilder(Material.BLAZE_POWDER).setName("§d§lFlammenPartikel"),
            new ItemBuilder(Material.EMERALD).setName("§d§lGrünePartikel"),
            new ItemBuilder(Material.LEATHER_BOOTS).setName("§d§lRegenbogenschuhe"),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setName("§d§lRegenbogenhose"),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§d§lRegenbogenbrustplatte"),
            new ItemBuilder(Material.LEATHER_HELMET).setName("§d§lRegenbogenhelm")
    };

    private final ItemBuilder[] legendaerItem = {
            new ItemBuilder(Material.WHEAT).setName("§b§lGetreideSpur"),
            new ItemBuilder(Material.GLOWSTONE_DUST).setName("§b§lRegenbogenPartikel"),
            new ItemBuilder(Material.REDSTONE).setName("§b§lRoteRinge")
    };

    private final ItemBuilder[] mystischItem = {
            new ItemBuilder(Material.FIREWORK).setName("§6§lFeuerwerk")
    };

    private final ItemBuilder[] schwarzmarktwareItem = {
            new ItemBuilder(Material.BLAZE_ROD).setName("§c§lFlugStab")
    };

    private int openChestTaskID;
    private int count;
    private boolean stopped;
    private int item;

    public OpenCrates(Main plugin, Player player) {
        this.plugin = plugin;
        inventory = Bukkit.createInventory(null, 27, "Öffne Kiste...");
        isRunning = false;
        stopped = false;
        openChest(player);
    }

    private ItemStack chooseRarities() {
        int i = 1 + new Random().nextInt(9999);
        if (i < 5732) {
            return new ItemBuilder(Material.STAINED_CLAY, (short) 8).setName(gewoehnlichName).build();
        } else if (i < 8232) {
            return new ItemBuilder(Material.STAINED_CLAY, (short) 5).setName(unngewoehnlichName).build();
        } else if (i < 9482) {
            return new ItemBuilder(Material.STAINED_CLAY, (short) 11).setName(seltenName).build();
        } else if (i < 9795) {
            return new ItemBuilder(Material.STAINED_CLAY, (short) 2).setName(epischName).build();
        } else if (i < 9951) {
            return new ItemBuilder(Material.STAINED_CLAY, (short) 3).setName(legendaerName).build();
        } else if (i < 9990) {
            return new ItemBuilder(Material.STAINED_CLAY, (short) 4).setName(mystischName).build();
        } else {
            return new ItemBuilder(Material.STAINED_CLAY, (short) 14).setName(schwarzmarkwareName).build();
        }
    }

    private void delayedRarities(int delay, Player player) {
        isRunning = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            while (true) {
                ItemStack temp = chooseRarities();
                if (inventory.getItem(13) != null) {
                    if (inventory.getItem(13).getItemMeta().getDisplayName().equals(temp.getItemMeta().getDisplayName())) {
                        continue;
                    }
                }
                inventory.setItem(13, temp);
                new HSound(Sound.CLICK).playFor(player);
                isRunning = false;
                break;
            }
        }, delay);
    }

    private void delayedItems(int delay, Player player, ItemStack itemStack) {
        isRunning = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            inventory.setItem(13, itemStack);
            new HSound(Sound.CLICK).playFor(player);
            isRunning = false;
        }, delay);
    }

    private void chooseCosmetic(String rarities, Player player) {
        stopped = false;
        isRunning = false;
        count = 0;
        item = 0;
        switch (rarities) {
            case gewoehnlichName:
                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 0).setName("§7Du bekommst einen §7Gewöhnlichen §7Gegenstand.").build());
                openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!isRunning) {
                        count++;
                        if (count >= (delay.length - 1)) {
                            Bukkit.getScheduler().cancelTask(openChestTaskID);
                            stopped = true;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                int ran = new Random().nextInt(gewoehnlichItem.length);
                                play3LevelUpFor(player);
                                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 0).setName("§7Dein Gewinn: " + gewoehnlichItem[ran].build().getItemMeta().getDisplayName()).build());
                                if (Cosmetics.has(player, gewoehnlich[ran])) {
                                    inventory.setItem(13, gewoehnlichItem[ran].setLore("§7Seltenheit: Gewöhnlich", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e200 §6Coins §cverkauft.").build());
                                    HyperiorCore.getCoinSystem().addCoins(player, 200, false);
                                } else {
                                    inventory.setItem(13, gewoehnlichItem[ran].setLore("§7Seltenheit: Gewöhnlich").build());
                                    Cosmetics.add(player, gewoehnlich[ran]);
                                }
                            }, delay[count]);
                        }
                        if (!stopped) {
                            if (item >= gewoehnlichItem.length) {
                                item = 0;
                            }
                            delayedItems(delay[count], player, gewoehnlichItem[item].build());
                            item++;
                        }
                    }
                }, 1, 1);
                break;
            case unngewoehnlichName:
                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 5).setName("§7Du bekommst einen §aUngewöhnlichen §7Gegenstand.").build());
                openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!isRunning) {
                        count++;
                        if (count >= (delay.length - 1)) {
                            Bukkit.getScheduler().cancelTask(openChestTaskID);
                            stopped = true;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                int ran = new Random().nextInt(ungewoehnlichItem.length);
                                play3LevelUpFor(player);
                                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 5).setName("§7Dein Gewinn: " + ungewoehnlichItem[ran].build().getItemMeta().getDisplayName()).build());
                                if (Cosmetics.has(player, ungewoehnlich[ran])) {
                                    inventory.setItem(13, ungewoehnlichItem[ran].setLore("§7Seltenheit: §aUngewöhnlich", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e400 §6Coins §cverkauft.").build());
                                    HyperiorCore.getCoinSystem().addCoins(player, 400, false);
                                } else {
                                    inventory.setItem(13, ungewoehnlichItem[ran].setLore("§7Seltenheit: §aUngewöhnlich").build());
                                    Cosmetics.add(player, ungewoehnlich[ran]);
                                }
                            }, delay[count]);
                        }
                        if (!stopped) {
                            if (item >= ungewoehnlichItem.length) {
                                item = 0;
                            }
                            delayedItems(delay[count], player, ungewoehnlichItem[item].build());
                            item++;
                        }
                    }
                }, 1, 1);
                break;
            case seltenName:
                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 11).setName("§7Du bekommst einen §9Seltenen §7Gegenstand.").build());
                openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!isRunning) {
                        count++;
                        if (count >= (delay.length - 1)) {
                            Bukkit.getScheduler().cancelTask(openChestTaskID);
                            stopped = true;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                int ran = new Random().nextInt(seltenItem.length);
                                play3LevelUpFor(player);
                                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 11).setName("§7Dein Gewinn: " + seltenItem[ran].build().getItemMeta().getDisplayName()).build());
                                if (Cosmetics.has(player, selten[ran])) {
                                    inventory.setItem(13, seltenItem[ran].setLore("§7Seltenheit: §9Selten", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e800 §6Coins §cverkauft.").build());
                                    HyperiorCore.getCoinSystem().addCoins(player, 800, false);
                                } else {
                                    inventory.setItem(13, seltenItem[ran].setLore("§7Seltenheit: §9Selten").build());
                                    Cosmetics.add(player, selten[ran]);
                                }
                            }, delay[count]);
                        }
                        if (!stopped) {
                            if (item >= seltenItem.length) {
                                item = 0;
                            }
                            delayedItems(delay[count], player, seltenItem[item].build());
                            item++;
                        }
                    }
                }, 1, 1);
                break;
            case epischName:
                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 2).setName("§7Du bekommst einen §dEpischen §7Gegenstand.").build());
                openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!isRunning) {
                        count++;
                        if (count >= (delay.length - 1)) {
                            Bukkit.getScheduler().cancelTask(openChestTaskID);
                            stopped = true;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                int ran = new Random().nextInt(epischItem.length);
                                play3LevelUpFor(player);
                                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 2).setName("§7Dein Gewinn: " + epischItem[ran].build().getItemMeta().getDisplayName()).build());
                                if (Cosmetics.has(player, episch[ran])) {
                                    inventory.setItem(13, epischItem[ran].setLore("§7Seltenheit: §dEpisch", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e3.200 §6Coins §cverkauft.").build());
                                    HyperiorCore.getCoinSystem().addCoins(player, 3200, false);
                                } else {
                                    inventory.setItem(13, epischItem[ran].setLore("§7Seltenheit: §dEpisch").build());
                                    Cosmetics.add(player, episch[ran]);
                                }
                            }, delay[count]);
                        }
                        if (!stopped) {
                            if (item >= epischItem.length) {
                                item = 0;
                            }
                            if (epischItem[item].build().getItemMeta().getDisplayName().contains("Regenbogenschuhe") || epischItem[item].build().getItemMeta().getDisplayName().contains("Regenbogenhose")
                                    || epischItem[item].build().getItemMeta().getDisplayName().contains("Regenbogenbrustplatte") || epischItem[item].build().getItemMeta().getDisplayName().contains("Regenbogenhelm")) {
                                delayedItems(delay[count], player, epischItem[item].setColorAndBuild(148, 0, 211));
                            } else {
                                delayedItems(delay[count], player, epischItem[item].build());
                            }
                            item++;
                        }
                    }
                }, 1, 1);
                break;
            case legendaerName:
                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 3).setName("§7Du bekommst einen §bLegendären §7Gegenstand.").build());
                openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!isRunning) {
                        count++;
                        if (count >= (delay.length - 1)) {
                            Bukkit.getScheduler().cancelTask(openChestTaskID);
                            stopped = true;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                int ran = new Random().nextInt(legendaerItem.length);
                                play3LevelUpFor(player);
                                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 3).setName("§7Dein Gewinn: " + legendaerItem[ran].build().getItemMeta().getDisplayName()).build());
                                if (Cosmetics.has(player, legendaer[ran])) {
                                    inventory.setItem(13, legendaerItem[ran].setLore("§7Seltenheit: §bLegendär", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e6.400 §6Coins §cverkauft.").build());
                                    HyperiorCore.getCoinSystem().addCoins(player, 6400, false);
                                } else {
                                    inventory.setItem(13, legendaerItem[ran].setLore("§7Seltenheit: §bLegendär").build());
                                    Cosmetics.add(player, legendaer[ran]);
                                }
                            }, delay[count]);
                        }
                        if (!stopped) {
                            if (item >= legendaerItem.length) {
                                item = 0;
                            }
                            delayedItems(delay[count], player, legendaerItem[item].build());
                            item++;
                        }
                    }
                }, 1, 1);
                break;
            case mystischName:
                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 4).setName("§7Du bekommst einen §6Mystischen §7Gegenstand.").build());
                openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!isRunning) {
                        count++;
                        if (count >= (delay.length - 1)) {
                            Bukkit.getScheduler().cancelTask(openChestTaskID);
                            stopped = true;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                int ran = new Random().nextInt(mystischItem.length);
                                play3LevelUpFor(player);
                                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 4).setName("§7Dein Gewinn: " + mystischItem[ran].build().getItemMeta().getDisplayName()).build());
                                if (Cosmetics.has(player, mystisch[ran])) {
                                    inventory.setItem(13, mystischItem[ran].setLore("§7Seltenheit: §6Mystisch", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e51.200 §6Coins §cverkauft.").build());
                                    HyperiorCore.getCoinSystem().addCoins(player, 51200, false);
                                } else {
                                    inventory.setItem(13, mystischItem[ran].setLore("§7Seltenheit: §6Mystisch").build());
                                    Cosmetics.add(player, mystisch[ran]);
                                }
                            }, delay[count]);
                        }
                        if (!stopped) {
                            if (item >= mystischItem.length) {
                                item = 0;
                            }
                            delayedItems(delay[count], player, mystischItem[item].build());
                            item++;
                        }
                    }
                }, 1, 1);
                break;
            default:
                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 14).setName("§7Du bekommst einen §cSchwarzmarktwaren §7Gegenstand.").build());
                openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    if (!isRunning) {
                        count++;
                        if (count >= (delay.length - 1)) {
                            Bukkit.getScheduler().cancelTask(openChestTaskID);
                            stopped = true;
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                int ran = new Random().nextInt(schwarzmarktwareItem.length);
                                play3LevelUpFor(player);
                                setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 14).setName("§7Dein Gewinn: " + schwarzmarktwareItem[ran].build().getItemMeta().getDisplayName()).build());
                                if (Cosmetics.has(player, schwarzmarktware[ran])) {
                                    inventory.setItem(13, schwarzmarktwareItem[ran].setLore("§7Seltenheit: §cSchwarzmarktware", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e204.800 §6Coins §cverkauft.").build());
                                    HyperiorCore.getCoinSystem().addCoins(player, 204800, false);
                                } else {
                                    inventory.setItem(13, schwarzmarktwareItem[ran].setLore("§7Seltenheit: §cSchwarzmarktware").build());
                                    Cosmetics.add(player, schwarzmarktware[ran]);
                                }
                            }, delay[count]);
                        }
                        if (!stopped) {
                            if (item >= schwarzmarktwareItem.length) {
                                item = 0;
                            }
                            delayedItems(delay[count], player, schwarzmarktwareItem[item].build());
                            item++;
                        }
                    }
                }, 1, 1);
                break;
        }
    }

    private void play3LevelUpFor(Player player) {
        new HSound(Sound.LEVEL_UP).playFor(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> new HSound(Sound.LEVEL_UP).playFor(player), 2);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> new HSound(Sound.LEVEL_UP).playFor(player), 4);
    }

    private void setFrame(ItemStack itemStack) {
        for (int j : frame) {
            inventory.setItem(j, itemStack);
        }
    }

    public void openChest(Player player) {
        player.openInventory(inventory);
        setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
        openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!isRunning) {
                count++;
                if (count >= (delay.length - 1)) {
                    Bukkit.getScheduler().cancelTask(openChestTaskID);
                    stopped = true;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        ItemStack win = chooseRarities();
                        inventory.setItem(13, win);
                        chooseCosmetic(win.getItemMeta().getDisplayName(), player);
                        new HSound(Sound.LEVEL_UP).playFor(player);
                    }, delay[count]);
                }
                if (!stopped) {
                    delayedRarities(delay[count], player);
                }
            }
        }, 1, 1);
    }

}
