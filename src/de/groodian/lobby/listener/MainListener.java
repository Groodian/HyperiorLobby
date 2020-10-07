package de.groodian.lobby.listener;

import de.groodian.cosmetics.trails.GUI;
import de.groodian.hyperiorcore.boards.Tablist;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.util.LobbyPacketReader;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
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

import java.util.Calendar;

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
        ConfigLocation locationUtil = new ConfigLocation(plugin, "Spawn");
        if (locationUtil.loadLocation() != null) {
            e.setRespawnLocation(locationUtil.loadLocation());
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie Spawn-Location wurde noch nicht gesetzt!");
        }
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand() != null) {
                if (player.getItemInHand().getItemMeta() != null) {
                    if (player.getItemInHand().getItemMeta().getDisplayName() != null) {
                        if (player.getItemInHand().getItemMeta().getDisplayName().equals("§6§lTrails §7(Rechtsklick)")) {
                            GUI.openGUI(player);
                        } else if (player.getItemInHand().getItemMeta().getDisplayName().equals("§c§lNavigator §7(Rechtsklick)")) {
                            plugin.getNavigator().open(player);
                        } else if (player.getItemInHand().getItemMeta().getDisplayName().equals("§b§lLobby wechsler §7(Rechtsklick)")) {
                            plugin.getLobbyGUI().openGUI(player);
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
        new Tablist("[§6HYPERIOR.DE§r]\n§eLobby - " + Bukkit.getServerName() + "\n", "\n§7Lust auf tolle Vorteile und Features?\n§6Dann Besuche unseren Shop unter /shop!").sendTo(player);
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
        meta.setDisplayName("§a§lProfil §7(Rechtsklick)");
        skull.setItemMeta(meta);
        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS).setName("§c§lNavigator §7(Rechtsklick)").build());
        player.getInventory().setItem(1, skull);
        player.getInventory().setItem(4, new ItemBuilder(Material.CHEST).setName("§6§lTrails §7(Rechtsklick)").build());
        player.getInventory().setItem(7, new ItemBuilder(Material.FIREWORK_CHARGE).setName("§cKein Gadget ausgewählt!").build());
        player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).setName("§b§lLobby wechsler §7(Rechtsklick)").build());

        HyperiorCore.getSB().registerScoreboard(player, "§lHYPERIOR.DE", 12, 1, 100);
        plugin.getLobbyScoreboard().set(player);

        event.setJoinMessage(null);

        ConfigLocation locationUtil = new ConfigLocation(plugin, "SpawnWarp");
        if (locationUtil.loadLocation() != null) {
            player.teleport(locationUtil.loadLocation());
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie Spawn-Warp-Location wurde noch nicht gesetzt!");
        }

        if (plugin.getMpJoin().isWorking() && plugin.getDailyBonus().isWorking() && plugin.getCrates().isWorking()) {
            LobbyPacketReader lpr = new LobbyPacketReader(player, plugin);
            lpr.inject();
        }

        plugin.getClient().sendUpdate(Bukkit.getOnlinePlayers().size());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        plugin.getClient().sendUpdate(Bukkit.getOnlinePlayers().size() - 1);
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
        Player player = (Player) e.getWhoClicked();
        if (plugin.getBuild().contains(player)) {
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
        if (!(e.getEntity() instanceof Player))
            return;
        Player player = (Player) e.getEntity();
        if (plugin.getBuild().contains(player)) {
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
            ConfigLocation locationUtil = new ConfigLocation(plugin, "Spawn");
            if (locationUtil.loadLocation() != null) {
                e.getPlayer().teleport(locationUtil.loadLocation());
            } else {
                Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie Spawn-Location wurde noch nicht gesetzt!");
            }
        }
    }

}
