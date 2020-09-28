package de.groodian.lobby.listener;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.groodian.cosmetics.trails.GUI;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.util.ConfigLocationUtil;
import de.groodian.lobby.util.ItemBuilder;
import de.groodian.lobby.util.PacketReader;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class MainListener implements Listener {

	private Main plugin;

	public MainListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void handlePlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		e.getDrops().clear();
		PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
		((CraftPlayer) player).getHandle().playerConnection.a(packet);
	}

	@EventHandler
	public void handlePlayerRespawn(PlayerRespawnEvent e) {
		ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "Spawn");
		if (locationUtil.loadLocation() != null) {
			e.setRespawnLocation(locationUtil.loadLocation());
		} else
			Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "�cDie Spawn-Location wurde noch nicht gesetzt!");
	}

	@EventHandler
	public void handleInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getPlayer().getItemInHand() != null) {
				if (e.getPlayer().getItemInHand().getItemMeta() != null) {
					if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null) {
						if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("�6�lTrails �7(Rechtsklick)")) {
							GUI.openGUI(e.getPlayer());
						} else if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("�a�lProfil �7(Rechtsklick)")) {
							// e.getPlayer().sendMessage(Main.PREFIX + "�cDas Feature wird bald
							// hinzugef�gt.");
						} else if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("�c�lNavigator �7(Rechtsklick)")) {
							plugin.getNavigator().open(e.getPlayer());
						}
					}
				}
			}
		}
		if (plugin.getBuild().contains(e.getPlayer()))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void handlePlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		plugin.getTablist().sendTab(player, "[�6HYPERIOR.DE�r]\n�eLobby - 1\n", "\n�7Lust auf tolle Vorteile und Features?\n�6Dann Besuche unseren Shop unter /shop!");
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setGameMode(GameMode.SURVIVAL);
		player.setLevel(Calendar.getInstance().get(Calendar.YEAR));
		player.setExp((1.0f / 365.0f) * (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1));
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(player.getName());
		meta.setDisplayName("�a�lProfil �7(Rechtsklick)");
		skull.setItemMeta(meta);
		player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).setName("�c�lNavigator �7(Rechtsklick)").build());
		player.getInventory().setItem(1, skull);
		player.getInventory().setItem(7, new ItemBuilder(Material.FIREWORK_CHARGE).setName("�cKein Gadget ausgew�hlt!").build());
		player.getInventory().setItem(8, new ItemBuilder(Material.CHEST).setName("�6�lTrails �7(Rechtsklick)").build());
		plugin.getLobbyScoreboard().updateLobbyScoreboard(player);
		event.setJoinMessage(null);
		ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "SpawnWarp");
		if (locationUtil.loadLocation() != null) {
			player.teleport(locationUtil.loadLocation());
		} else {
			Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "�cDie Spawn-Warp-Location wurde noch nicht gesetzt!");
		}

		if (plugin.getMpJoin().isWorking() && plugin.getDailyBonus().isWorking() && plugin.getCrates().isWorking()) {
			PacketReader pr = new PacketReader(player, plugin);
			pr.inject();

			plugin.getMpJoin().getHologram().show(player);
			plugin.getDailyBonus().getHologram().show(player);
			plugin.getCrates().getHologram().show(player);
		}
	}

	@EventHandler
	public void handlePlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}

	@EventHandler
	public void handlePlayerBuild(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		if (plugin.getBuild().contains(player))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void handlePlayerBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (plugin.getBuild().contains(player))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void handlePlayerDropItem(PlayerDropItemEvent e) {
		if (plugin.getBuild().contains(e.getPlayer()))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void handlePlayerInventoryClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player))
			return;
		if (e.getWhoClicked() instanceof Player) {
			if (plugin.getBuild().contains(e.getWhoClicked()))
				return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void handlePlayerFoodLevelChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void handleCreatureSpawn(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void handleBedEnter(PlayerBedEnterEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void handleBowShot(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player) {
			if (plugin.getBuild().contains(e.getEntity()))
				return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void handleEntityDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void handlePlayerMove(PlayerMoveEvent e) {
		if (e.getPlayer().getLocation().getY() <= 0) {
			ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "Spawn");
			if (locationUtil.loadLocation() != null) {
				e.getPlayer().teleport(locationUtil.loadLocation());
			} else
				Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "�cDie Spawn-Location wurde noch nicht gesetzt!");
		}
	}

}
