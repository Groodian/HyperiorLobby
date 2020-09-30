package de.groodian.lobby.gui;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.HSound;
import de.groodian.hyperiorcore.util.ItemBuilder;
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

import de.groodian.lobby.main.Main;

public class Navigator implements Listener {
	
	private Main plugin;
	
	public Navigator(Main plugin) {
		this.plugin = plugin;
	}

	private final String GUI_NAME = "Wähle ein Ziel";

	public void open(Player player) {
		Inventory inventory = Bukkit.createInventory(null, 27, GUI_NAME);
		inventory.setItem(10, new ItemBuilder(Material.HUGE_MUSHROOM_2).setName("§8» §eMinecraft Party").build());
		inventory.setItem(12, new ItemBuilder(Material.ENDER_CHEST).setName("§8» §aTrails Kiste").build());
		inventory.setItem(14, new ItemBuilder(Material.PRISMARINE_SHARD).setName("§8» §bTägliche Belohnungen").build());
		inventory.setItem(16, new ItemBuilder(Material.GLOWSTONE_DUST).setName("§8» §6Spawn").build());
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
								ConfigLocation locationUtil = new ConfigLocation(plugin, "MinecraftPartyWarp");
								if (locationUtil.loadLocation() != null) {
									new HSound(Sound.ENDERDRAGON_WINGS).playFor(player);
									player.setVelocity(new Vector(0, 5, 0));
									player.closeInventory();
									new BukkitRunnable() {
										public void run() {
											player.teleport(locationUtil.loadLocation());
										}
									}.runTaskLater(plugin, 15L);
								} else
									Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie MinecraftParty-Warp-Location wurde noch nicht gesetzt!");
								return;
							}
							if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §aTrails Kiste")) {
								ConfigLocation locationUtil = new ConfigLocation(plugin, "CratesWarp");
								if (locationUtil.loadLocation() != null) {
									new HSound(Sound.ENDERDRAGON_WINGS).playFor(player);
									player.setVelocity(new Vector(0, 5, 0));
									player.closeInventory();
									new BukkitRunnable() {
										public void run() {
											player.teleport(locationUtil.loadLocation());
										}
									}.runTaskLater(plugin, 15L);
								} else
									Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie Crates-Warp-Location wurde noch nicht gesetzt!");
								return;
							}
							if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §bTägliche Belohnungen")) {
								ConfigLocation locationUtil = new ConfigLocation(plugin, "DailyBonusWarp");
								if (locationUtil.loadLocation() != null) {
									new HSound(Sound.ENDERDRAGON_WINGS).playFor(player);
									player.setVelocity(new Vector(0, 5, 0));
									player.closeInventory();
									new BukkitRunnable() {
										public void run() {
											player.teleport(locationUtil.loadLocation());
										}
									}.runTaskLater(plugin, 15L);
								} else
									Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie DailyBonus-Warp-Location wurde noch nicht gesetzt!");
								return;
							}
							if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §6Spawn")) {
								ConfigLocation locationUtil = new ConfigLocation(plugin, "SpawnWarp");
								if (locationUtil.loadLocation() != null) {
									new HSound(Sound.ENDERDRAGON_WINGS).playFor(player);
									player.setVelocity(new Vector(0, 5, 0));
									player.closeInventory();
									new BukkitRunnable() {
										public void run() {
											player.teleport(locationUtil.loadLocation());
										}
									}.runTaskLater(plugin, 15L);
								} else
									Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie Spawn-Warp-Location wurde noch nicht gesetzt!");
								return;
							}
						}
					}
				}
			}
		}
	}
}
