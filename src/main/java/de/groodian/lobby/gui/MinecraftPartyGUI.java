package de.groodian.lobby.gui;

import de.groodian.hyperiorcore.gui.GUI;
import de.groodian.hyperiorcore.gui.GUIRunnable;
import de.groodian.hyperiorcore.util.HSound;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.network.MinecraftPartyServiceInfo;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MinecraftPartyGUI extends GUI {

    private final Main plugin;
    private final ItemStack animationItem;
    private boolean main;

    public MinecraftPartyGUI(Main plugin) {
        super(Component.text("Minecraft Party"), 45);
        this.plugin = plugin;
        this.animationItem = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§a").build();
    }

    @Override
    protected void onOpen() {
        openMain();
    }

    private void openMain() {
        main = true;
        inventory.clear();
        animation();
        new BukkitRunnable() {
            public void run() {
                putItem(new ItemBuilder(Material.ITEM_FRAME).setName("§aLaufende Runden")
                        .setLore("", "§7Klicke hier, um alle", "§7laufenden Runden zu sehen.")
                        .build(), 42, () -> openRunningServers());
                GUIRunnable guiRunnable = () -> {
                    for (Map.Entry<Integer, MinecraftPartyServiceInfo> info : plugin.getClient()
                            .getMinecraftPartyServiceInfos()
                            .entrySet()) {
                        if (info.getValue().getGameState().equalsIgnoreCase("Lobby")) {
                            player.sendMessage("§4TODO! " + info.getKey());
                        }
                    }
                };
                putItem(new ItemBuilder(Material.LIME_WOOL).setName("§aQuick Play")
                        .setLore("", "§7Klicke hier, um eine", "§7zufällige Lobby zu betreten.")
                        .build(), 38, guiRunnable);
                setLobbyServices();
            }
        }.runTaskLater(plugin, 8);
        new HSound(Sound.ENTITY_HORSE_SADDLE).playFor(player);
    }

    private void openRunningServers() {
        main = false;
        inventory.clear();
        animation();
        new BukkitRunnable() {
            public void run() {
                putItem(new ItemBuilder(Material.ARROW).setName("§cZurück").build(), 36, () -> openMain());
                setRunningServices();
            }
        }.runTaskLater(plugin, 8);
        new HSound(Sound.ENTITY_HORSE_SADDLE).playFor(player);
    }

    private void setLobbyServices() {
        int i = 0;
        for (Map.Entry<Integer, MinecraftPartyServiceInfo> info : plugin.getClient().getMinecraftPartyServiceInfos().entrySet()) {
            if (info.getValue().getGameState().equalsIgnoreCase("Lobby")) {
                putItem(new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§a" + info.getKey())
                        .setLore("§7Status: §6" + info.getValue().getGameState(),
                                "§7Spieler: §6" + info.getValue().getOnlinePlayers() + "/" + info.getValue().getMaxPlayers())
                        .build(), i, () -> player.sendMessage("§4TODO! " + info.getKey()));
                i++;
            }
        }
    }

    private void setRunningServices() {
        int i = 0;
        for (Map.Entry<Integer, MinecraftPartyServiceInfo> info : plugin.getClient().getMinecraftPartyServiceInfos().entrySet()) {
            if (!info.getValue().getGameState().equalsIgnoreCase("Lobby")) {
                putItem(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§a" + info.getKey())
                        .setLore("§7Status: §6" + info.getValue().getGameState(),
                                "§7Spieler: §6" + info.getValue().getOnlinePlayers() + "/" + info.getValue().getMaxPlayers())
                        .build(), i, () -> player.sendMessage("§4TODO!" + info.getKey()));
                i++;
            }
        }
    }

    private void animation() {
        putItemDelayed(animationItem, 27, 2);
        putItemDelayed(animationItem, 28, 3);
        putItemDelayed(animationItem, 29, 4);
        putItemDelayed(animationItem, 30, 5);
        putItemDelayed(animationItem, 31, 6);
        putItemDelayed(animationItem, 32, 7);
        putItemDelayed(animationItem, 33, 8);
        putItemDelayed(animationItem, 34, 9);
        putItemDelayed(animationItem, 35, 10);
    }

    @Override
    public void onUpdate() {
        for (int i = 0; i < 27; i++) {
            inventory.clear(i);
        }
        if (main) {
            setLobbyServices();
        } else {
            setRunningServices();
        }
    }

}
