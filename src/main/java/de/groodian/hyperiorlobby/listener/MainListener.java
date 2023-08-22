package de.groodian.hyperiorlobby.listener;

import de.groodian.hyperiorcore.boards.Tablist;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.hyperiorlobby.gui.LobbyGUI;
import de.groodian.hyperiorlobby.gui.NavigatorGUI;
import de.groodian.hyperiorlobby.main.Main;
import de.groodian.hyperiorlobby.spawnable.Crate;
import de.groodian.hyperiorlobby.spawnable.DailyBonus;
import de.groodian.hyperiorlobby.spawnable.MinecraftPartyJoin;
import de.groodian.hyperiorlobby.spawnable.Spawn;
import java.util.Calendar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
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
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class MainListener implements Listener {

    private final Main plugin;

    public MainListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleWorldLoadEvent(WorldLoadEvent e) {
        if (e.getWorld().getName().equals("world")) {
            new Crate(plugin);
            new Spawn(plugin);
            plugin.setDailyBonus(new DailyBonus(plugin));
            plugin.setMpJoin(new MinecraftPartyJoin(plugin));
        }
    }

    @EventHandler
    public void handlePlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        e.getDrops().clear();
        player.spigot().respawn();
    }

    @EventHandler
    public void handlePlayerRespawn(PlayerRespawnEvent e) {
        ConfigLocation locationUtil = new ConfigLocation(plugin, "Spawn");
        if (locationUtil.loadLocation() != null) {
            e.setRespawnLocation(locationUtil.loadLocation());
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX_LEGACY + "§cDie Spawn-Location wurde noch nicht gesetzt!");
        }
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            String interact = ItemBuilder.getCustomData(itemInMainHand, "interact", PersistentDataType.STRING);
            if (interact != null) {
                if (interact.equals("Navigator")) {
                    plugin.getNavigatorGUIManager().open(player, new NavigatorGUI());
                } else if (interact.equals("Lobby")) {
                    plugin.getLobbyGUIManager().open(player, new LobbyGUI(plugin));
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
        new Tablist("[§6HYPERIOR.DE§r]\n§eLobby - " + plugin.getGroupNumber() + "\n",
                "\n§7Lust auf tolle Vorteile und Features?\n§6Dann Besuche unseren Shop unter /shop!").sendTo(player);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.setLevel(Calendar.getInstance().get(Calendar.YEAR));
        player.setExp((1.0f / 365.0f) * (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1));

        player.getInventory()
                .setItem(0, new ItemBuilder(Material.COMPASS).setName("§c§lNavigator §7(Rechtsklick)")
                        .addCustomData("interact", PersistentDataType.STRING, "Navigator")
                        .build());
        player.getInventory()
                .setItem(1, new ItemBuilder(Material.PLAYER_HEAD).setName("§a§lProfil §7(Rechtsklick)")
                        .setSkullOwner(player.getUniqueId())
                        .build());
        player.getInventory()
                .setItem(7, new ItemBuilder(Material.NETHER_STAR).setName("§b§lLobby wechsler §7(Rechtsklick)")
                        .addCustomData("interact", PersistentDataType.STRING, "Lobby")
                        .build());

        HyperiorCore.getSB().registerScoreboard(player, Component.text("HYPERIOR").decorate(TextDecoration.BOLD), 12, 1, 100);
        plugin.getLobbyScoreboard().set(player);

        event.joinMessage(null);

        ConfigLocation locationUtil = new ConfigLocation(plugin, "SpawnWarp");
        if (locationUtil.loadLocation() != null) {
            player.teleport(locationUtil.loadLocation());
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX_LEGACY + "§cDie Spawn-Warp-Location wurde noch nicht gesetzt!");
        }

        plugin.getClient().sendUpdate(Bukkit.getOnlinePlayers().size());
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);

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
        if (!(e.getWhoClicked() instanceof Player player))
            return;
        if (plugin.getBuild().contains(player))
            return;
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
        if (!(e.getEntity() instanceof Player player))
            return;
        if (plugin.getBuild().contains(player))
            return;
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
                Bukkit.getConsoleSender().sendMessage(Main.PREFIX_LEGACY + "§cDie Spawn-Location wurde noch nicht gesetzt!");
            }
        }
    }

}
