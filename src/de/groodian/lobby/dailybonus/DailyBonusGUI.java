package de.groodian.lobby.dailybonus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import de.groodian.hyperiorcore.coinsystem.CoinSystem;
import de.groodian.hyperiorcore.ranks.Ranks;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.util.ItemBuilder;

public class DailyBonusGUI implements Listener {

	private Main plugin;
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");

	private final String GUI_NAME = "Tägliche Belohnung";

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
				if (DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusspieler") == null) {
					inventory.setItem(12, new ItemBuilder(Material.SUGAR).setName("§7§lSpieler-Belohnung").setLore("", "§7Belohnung: §e+500 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
				} else {
					LocalDate date = LocalDate.parse(DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusspieler"), dateFormatter);
					if (!date.equals(LocalDate.now())) {
						inventory.setItem(12, new ItemBuilder(Material.SUGAR).setName("§7§lSpieler-Belohnung").setLore("", "§7Belohnung: §e+500 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
					} else {
						inventory.setItem(12,
								new ItemBuilder(Material.SUGAR).setName("§7§lSpieler-Belohnung").setLore("", "§7Belohnung: §e+500 §6Coins", "", "§cDu hast die Belohung heute schon eingesammelt.").build());
					}
				}
				if (Ranks.has(player.getUniqueId().toString().replaceAll("-", ""), "dailybonus.founder")) {
					if (DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusgruender") == null) {
						inventory.setItem(14,
								new ItemBuilder(Material.GLOWSTONE_DUST).setName("§c§lGründer-Belohnung").setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
					} else {
						DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
						LocalDate date = LocalDate.parse(DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusgruender"), dateFormatter);
						if (!date.equals(LocalDate.now())) {
							inventory.setItem(14,
									new ItemBuilder(Material.GLOWSTONE_DUST).setName("§c§lGründer-Belohnung").setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§aKlicke um die Belohnung einzusammeln.").build());
						} else {
							inventory.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST).setName("§c§lGründer-Belohnung")
									.setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§cDu hast die Belohung heute schon eingesammelt.").build());
						}
					}
				} else {
					inventory.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST).setName("§c§lGründer-Belohnung")
							.setLore("", "§7Belohnung: §e+1.000 §6Coins", "", "§7Diese Belohnung kannst du nur", "§7mit dem §cGründer-Rang §7einsammeln!").build());
				}
			}
		}.runTaskLater(plugin, 12);
		player.openInventory(inventory);
		plugin.getPlaySound().playSoundFor(Sound.HORSE_ARMOR, player);
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
							if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7§lSpieler-Belohnung")) {
								if (DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusspieler") == null) {
									DailyBonusMySQL.set(player, "dailybonusspieler", dateFormatter.format(LocalDate.now()));
									CoinSystem.addCoins(player, 500, false);
									player.closeInventory();
									plugin.getPlaySound().playSoundFor(Sound.LEVEL_UP, player);
								} else {
									LocalDate date = LocalDate.parse(DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusspieler"), dateFormatter);
									if (!date.equals(LocalDate.now())) {
										DailyBonusMySQL.set(player, "dailybonusspieler", dateFormatter.format(LocalDate.now()));
										CoinSystem.addCoins(player, 500, false);
										player.closeInventory();
										plugin.getPlaySound().playSoundFor(Sound.LEVEL_UP, player);
									}
								}
							}
							if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§c§lGründer-Belohnung")) {
								if (Ranks.has(player.getUniqueId().toString().replaceAll("-", ""), "dailybonus.founder")) {
									if (DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusgruender") == null) {
										DailyBonusMySQL.set(player, "dailybonusgruender", dateFormatter.format(LocalDate.now()));
										CoinSystem.addCoins(player, 1000, false);
										player.closeInventory();
										plugin.getPlaySound().playSoundFor(Sound.LEVEL_UP, player);
									} else {
										LocalDate date = LocalDate.parse(DailyBonusMySQL.get(player.getUniqueId().toString().replaceAll("-", ""), "dailybonusgruender"), dateFormatter);
										if (!date.equals(LocalDate.now())) {
											DailyBonusMySQL.set(player, "dailybonusgruender", dateFormatter.format(LocalDate.now()));
											CoinSystem.addCoins(player, 1000, false);
											player.closeInventory();
											plugin.getPlaySound().playSoundFor(Sound.LEVEL_UP, player);
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