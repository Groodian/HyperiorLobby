package de.groodian.lobby.crates;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.groodian.hyperiorcore.coinsystem.CoinSystem;
import de.groodian.lobby.cosmetics.Cosmetics;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.util.ItemBuilder;

public class OpenCrates {

	private final String GUI_NAME_OPEN = "Öffne Kiste...";

	private Main plugin;
	private Inventory inventory;
	private boolean isRunning;
	private int[] delay = { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 10, 15, 16 };
	private int[] frame = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };
	private String gewoehnlichName = "§7Gewöhnlich";
	private String unngewoehnlichName = "§aUngewöhnlich";
	private String seltenName = "§9Selten";
	private String epischName = "§dEpisch";
	private String legendaerName = "§bLegendär";
	private String mystischName = "§6Mystisch";
	private String schwarzmarkwareName = "§cSchwarzmarktware";
	
	private String[] gewoehnlich = { "Lederschuhe",
									 "Lederhose",
									 "Lederbrustplatte",
									 "Lederhelm",
									 "Kettenschuhe",
									 "Kettenhose",
									 "Kettenbrustplatte",
									 "Kettenhelm"};
	
	private String[] ungewoehnlich = {"Goldschuhe",
									  "Goldhose",
									  "Goldbrustplatte",
									  "Goldhelm",
									  "Diamantschuhe",
									  "Diamanthose",
									  "Diamantbrustplatte",
									  "Diamanthelm"};
	
	private String[] selten = {"GoldBlockSpur",
							   "WasserPartikel",
							   "LavaPartikel",
							   "BlazePartikel"};
	
	private String[] episch = {"RegenbogenWolleSpur",
							   "RegenbogenGlasSpur",
							   "RegenbogenLehmSpur",
							   "FlammenPartikel",
							   "GruenePartikel",
							   "Regenbogenschuhe",
							   "Regenbogenhose",
							   "Regenbogenbrustplatte",
							   "Regenbogenhelm"};
	
	private String[] legendaer = {"GetreideSpur",
								  "RegenbogenPartikel",
								  "RoteRinge"};
	
	private String[] mystisch = {"Feuerwerk"};
	
	private String[] schwarzmarktware = {"FlugStab"};
	
	private ItemBuilder[] gewoehnlichItem = { new ItemBuilder(Material.LEATHER_BOOTS).setName("§7§lLederschuhe"),
											  new ItemBuilder(Material.LEATHER_LEGGINGS).setName("§7§lLederhose"),
											  new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§7§lLederbrustplatte"),
											  new ItemBuilder(Material.LEATHER_HELMET).setName("§7§lLederhelm"),
											  new ItemBuilder(Material.CHAINMAIL_BOOTS).setName("§7§lKettenschuhe"),
											  new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setName("§7§lKettenhose"),
											  new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setName("§7§lKettenbrustplatte"),
											  new ItemBuilder(Material.CHAINMAIL_HELMET).setName("§7§lKettenhelm")};
	
	private ItemBuilder[] ungewoehnlichItem = { new ItemBuilder(Material.GOLD_BOOTS).setName("§a§lGoldschuhe"),
			  									new ItemBuilder(Material.GOLD_LEGGINGS).setName("§a§lGoldhose"),
			  									new ItemBuilder(Material.GOLD_CHESTPLATE).setName("§a§lGoldbrustplatte"),
			  									new ItemBuilder(Material.GOLD_HELMET).setName("§a§lGoldhelm"),
			  									new ItemBuilder(Material.DIAMOND_BOOTS).setName("§a§lDiamantschuhe"),
			  									new ItemBuilder(Material.DIAMOND_LEGGINGS).setName("§a§lDiamanthose"),
			  									new ItemBuilder(Material.DIAMOND_CHESTPLATE).setName("§a§lDiamantbrustplatte"),
			  									new ItemBuilder(Material.DIAMOND_HELMET).setName("§a§lDiamanthelm")};
	
	private ItemBuilder[] seltenItem = { new ItemBuilder(Material.GOLD_BLOCK).setName("§9§lGoldBlockSpur"),
										 new ItemBuilder(Material.WATER_BUCKET).setName("§9§lWasserPartikel"),
										 new ItemBuilder(Material.LAVA_BUCKET).setName("§9§lLavaPartikel"),
										 new ItemBuilder(Material.BLAZE_ROD).setName("§9§lBlazePartikel")};
	
	private ItemBuilder[] epischItem = { new ItemBuilder(Material.WOOL).setName("§d§lRegenbogenWolleSpur"),
			 						     new ItemBuilder(Material.GLASS).setName("§d§lRegenbogenGlasSpur"),
			 						     new ItemBuilder(Material.STAINED_CLAY).setName("§d§lRegenbogenLehmSpur"),
			 						     new ItemBuilder(Material.BLAZE_POWDER).setName("§d§lFlammenPartikel"),
			 						     new ItemBuilder(Material.EMERALD).setName("§d§lGrünePartikel"),
			 						     new ItemBuilder(Material.LEATHER_BOOTS).setName("§d§lRegenbogenschuhe"),
			 						     new ItemBuilder(Material.LEATHER_LEGGINGS).setName("§d§lRegenbogenhose"),
			 						     new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§d§lRegenbogenbrustplatte"),
			 						     new ItemBuilder(Material.LEATHER_HELMET).setName("§d§lRegenbogenhelm")};
	
	private ItemBuilder[] legendaerItem = { new ItemBuilder(Material.WHEAT).setName("§b§lGetreideSpur"),
		     								new ItemBuilder(Material.GLOWSTONE_DUST).setName("§b§lRegenbogenPartikel"),
		     								new ItemBuilder(Material.REDSTONE).setName("§b§lRoteRinge")};
	
	private ItemBuilder[] mystischItem = {new ItemBuilder(Material.FIREWORK).setName("§6§lFeuerwerk")};
	
	private ItemBuilder[] schwarzmarktwareItem = {new ItemBuilder(Material.BLAZE_ROD).setName("§c§lFlugStab")};
	
	private int openChestTaskID;
	private int count;
	private boolean stopped;
	private int item;

	public OpenCrates(Main plugin, Player player) {
		this.plugin = plugin;
		inventory = Bukkit.createInventory(null, 27, GUI_NAME_OPEN);
		isRunning = false;
		stopped = false;
		openChest(player);
	}

	public ItemStack chooseRarities() {
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

	public void delayedRarities(int delay, Player player) {
		isRunning = true;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				while (true) {
					ItemStack temp = chooseRarities();
					if (inventory.getItem(13) != null) {
						if (inventory.getItem(13).getItemMeta().getDisplayName().equals(temp.getItemMeta().getDisplayName())) {
							continue;
						}
					}
					inventory.setItem(13, temp);
					plugin.getPlaySound().playSoundFor(Sound.CLICK, player);
					isRunning = false;
					break;
				}
			}
		}, delay);
	}

	public void delayedItems(int delay, Player player, ItemStack itemStack) {
		isRunning = true;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				inventory.setItem(13, itemStack);
				plugin.getPlaySound().playSoundFor(Sound.CLICK, player);
				isRunning = false;
			}
		}, delay);
	}

	public void chooseCosmetic(String rarities, Player player) {
		stopped = false;
		isRunning = false;
		count = 0;
		item = 0;
		if (rarities.equals(gewoehnlichName)) {
			setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 0).setName("§7Du bekommst einen §7Gewöhnlichen §7Gegenstand.").build());
			openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (!isRunning) {
						count++;
						if (count >= (delay.length - 1)) {
							Bukkit.getScheduler().cancelTask(openChestTaskID);
							stopped = true;
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									int ran = new Random().nextInt(gewoehnlichItem.length);
									play3LevelUpFor(player);
									setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 0).setName("§7Dein Gewinn: " + gewoehnlichItem[ran].build().getItemMeta().getDisplayName()).build());
									if (Cosmetics.has(player, gewoehnlich[ran])) {
										inventory.setItem(13, gewoehnlichItem[ran].setLore("§7Seltenheit: Gewöhnlich", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e200 §6Coins §cverkauft.").build());
										CoinSystem.addCoins(player, 200, false);
									} else {
										inventory.setItem(13, gewoehnlichItem[ran].setLore("§7Seltenheit: Gewöhnlich").build());
										Cosmetics.add(player, gewoehnlich[ran]);
									}
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
				}
			}, 1, 1);
		} else if (rarities.equals(unngewoehnlichName)) {
			setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 5).setName("§7Du bekommst einen §aUngewöhnlichen §7Gegenstand.").build());
			openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (!isRunning) {
						count++;
						if (count >= (delay.length - 1)) {
							Bukkit.getScheduler().cancelTask(openChestTaskID);
							stopped = true;
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									int ran = new Random().nextInt(ungewoehnlichItem.length);
									play3LevelUpFor(player);
									setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 5).setName("§7Dein Gewinn: " + ungewoehnlichItem[ran].build().getItemMeta().getDisplayName()).build());
									if (Cosmetics.has(player, ungewoehnlich[ran])) {
										inventory.setItem(13, ungewoehnlichItem[ran].setLore("§7Seltenheit: §aUngewöhnlich", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e400 §6Coins §cverkauft.").build());
										CoinSystem.addCoins(player, 400, false);
									} else {
										inventory.setItem(13, ungewoehnlichItem[ran].setLore("§7Seltenheit: §aUngewöhnlich").build());
										Cosmetics.add(player, ungewoehnlich[ran]);
									}
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
				}
			}, 1, 1);
		} else if (rarities.equals(seltenName)) {
			setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 11).setName("§7Du bekommst einen §9Seltenen §7Gegenstand.").build());
			openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (!isRunning) {
						count++;
						if (count >= (delay.length - 1)) {
							Bukkit.getScheduler().cancelTask(openChestTaskID);
							stopped = true;
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									int ran = new Random().nextInt(seltenItem.length);
									play3LevelUpFor(player);
									setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 11).setName("§7Dein Gewinn: " + seltenItem[ran].build().getItemMeta().getDisplayName()).build());
									if (Cosmetics.has(player, selten[ran])) {
										inventory.setItem(13, seltenItem[ran].setLore("§7Seltenheit: §9Selten", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e800 §6Coins §cverkauft.").build());
										CoinSystem.addCoins(player, 800, false);
									} else {
										inventory.setItem(13, seltenItem[ran].setLore("§7Seltenheit: §9Selten").build());
										Cosmetics.add(player, selten[ran]);
									}
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
				}
			}, 1, 1);
		} else if (rarities.equals(epischName)) {
			setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 2).setName("§7Du bekommst einen §dEpischen §7Gegenstand.").build());
			openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (!isRunning) {
						count++;
						if (count >= (delay.length - 1)) {
							Bukkit.getScheduler().cancelTask(openChestTaskID);
							stopped = true;
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									int ran = new Random().nextInt(epischItem.length);
									play3LevelUpFor(player);
									setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 2).setName("§7Dein Gewinn: " + epischItem[ran].build().getItemMeta().getDisplayName()).build());
									if (Cosmetics.has(player, episch[ran])) {
										inventory.setItem(13, epischItem[ran].setLore("§7Seltenheit: §dEpisch", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e3.200 §6Coins §cverkauft.").build());
										CoinSystem.addCoins(player, 3200, false);
									} else {
										inventory.setItem(13, epischItem[ran].setLore("§7Seltenheit: §dEpisch").build());
										Cosmetics.add(player, episch[ran]);
									}
								}
							}, delay[count]);
						}
						if (!stopped) {
							if (item >= epischItem.length) {
								item = 0;
							}
							if(epischItem[item].build().getItemMeta().getDisplayName().contains("Regenbogenschuhe") || epischItem[item].build().getItemMeta().getDisplayName().contains("Regenbogenhose")
									|| epischItem[item].build().getItemMeta().getDisplayName().contains("Regenbogenbrustplatte") || epischItem[item].build().getItemMeta().getDisplayName().contains("Regenbogenhelm")) {
								delayedItems(delay[count], player, epischItem[item].setColorAndBuild(148, 0, 211));
							} else {
								delayedItems(delay[count], player, epischItem[item].build());	
							}
							item++;
						}
					}
				}
			}, 1, 1);
		} else if (rarities.equals(legendaerName)) {
			setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 3).setName("§7Du bekommst einen §bLegendären §7Gegenstand.").build());
			openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (!isRunning) {
						count++;
						if (count >= (delay.length - 1)) {
							Bukkit.getScheduler().cancelTask(openChestTaskID);
							stopped = true;
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									int ran = new Random().nextInt(legendaerItem.length);
									play3LevelUpFor(player);
									setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 3).setName("§7Dein Gewinn: " + legendaerItem[ran].build().getItemMeta().getDisplayName()).build());
									if (Cosmetics.has(player, legendaer[ran])) {
										inventory.setItem(13, legendaerItem[ran].setLore("§7Seltenheit: §bLegendär", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e6.400 §6Coins §cverkauft.").build());
										CoinSystem.addCoins(player, 6400, false);
									} else {
										inventory.setItem(13, legendaerItem[ran].setLore("§7Seltenheit: §bLegendär").build());
										Cosmetics.add(player, legendaer[ran]);
									}
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
				}
			}, 1, 1);
		} else if (rarities.equals(mystischName)) {
			setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 4).setName("§7Du bekommst einen §6Mystischen §7Gegenstand.").build());
			openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (!isRunning) {
						count++;
						if (count >= (delay.length - 1)) {
							Bukkit.getScheduler().cancelTask(openChestTaskID);
							stopped = true;
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									int ran = new Random().nextInt(mystischItem.length);
									play3LevelUpFor(player);
									setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 4).setName("§7Dein Gewinn: " + mystischItem[ran].build().getItemMeta().getDisplayName()).build());
									if (Cosmetics.has(player, mystisch[ran])) {
										inventory.setItem(13, mystischItem[ran].setLore("§7Seltenheit: §6Mystisch", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e51.200 §6Coins §cverkauft.").build());
										CoinSystem.addCoins(player, 51200, false);
									} else {
										inventory.setItem(13, mystischItem[ran].setLore("§7Seltenheit: §6Mystisch").build());
										Cosmetics.add(player, mystisch[ran]);
									}
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
				}
			}, 1, 1);
		} else {
			setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 14).setName("§7Du bekommst einen §cSchwarzmarktwaren §7Gegenstand.").build());
			openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				@Override
				public void run() {
					if (!isRunning) {
						count++;
						if (count >= (delay.length - 1)) {
							Bukkit.getScheduler().cancelTask(openChestTaskID);
							stopped = true;
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									int ran = new Random().nextInt(schwarzmarktwareItem.length);
									play3LevelUpFor(player);
									setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 14).setName("§7Dein Gewinn: " + schwarzmarktwareItem[ran].build().getItemMeta().getDisplayName()).build());
									if (Cosmetics.has(player, schwarzmarktware[ran])) {
										inventory.setItem(13, schwarzmarktwareItem[ran].setLore("§7Seltenheit: §cSchwarzmarktware", "", "§cDu besitzt diesen Gegenstand bereits.", "§cDeswegen wurde der Gegenstand", "§cfür §e204.800 §6Coins §cverkauft.").build());
										CoinSystem.addCoins(player, 204800, false);
									} else {
										inventory.setItem(13, schwarzmarktwareItem[ran].setLore("§7Seltenheit: §cSchwarzmarktware").build());
										Cosmetics.add(player, schwarzmarktware[ran]);
									}
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
				}
			}, 1, 1);
		}
	}

	public void play3LevelUpFor(Player player) {
		plugin.getPlaySound().playSoundFor(Sound.LEVEL_UP, player);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				plugin.getPlaySound().playSoundFor(Sound.LEVEL_UP, player);
			}
		}, 2);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				plugin.getPlaySound().playSoundFor(Sound.LEVEL_UP, player);
			}
		}, 4);
	}

	public void setFrame(ItemStack itemStack) {
		for (int i = 0; i < frame.length; i++) {
			inventory.setItem(frame[i], itemStack);
		}
	}

	public void openChest(Player player) {
		player.openInventory(inventory);
		setFrame(new ItemBuilder(Material.STAINED_GLASS_PANE, (short) 7).setName("§a").build());
		openChestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				if (!isRunning) {
					count++;
					if (count >= (delay.length - 1)) {
						Bukkit.getScheduler().cancelTask(openChestTaskID);
						stopped = true;
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								ItemStack win = chooseRarities();
								inventory.setItem(13, win);
								chooseCosmetic(win.getItemMeta().getDisplayName(), player);
								plugin.getPlaySound().playSoundFor(Sound.LEVEL_UP, player);
							}
						}, delay[count]);
					}
					if (!stopped) {
						delayedRarities(delay[count], player);
					}
				}
			}
		}, 1, 1);
	}

}
