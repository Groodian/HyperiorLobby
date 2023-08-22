package de.groodian.hyperiorlobby.gui;

import de.groodian.hyperiorcore.gui.GUI;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.HSound;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.hyperiorlobby.main.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class NavigatorGUI extends GUI {

    public NavigatorGUI() {
        super(Component.text("Wähle ein Ziel"), 27);
    }

    @Override
    protected void onOpen() {
        putItem(new ItemBuilder(Material.RED_MUSHROOM_BLOCK).setName("§8» §eMinecraft Party").build(), 10,
                () -> teleport("MinecraftPartyWarp"));
        putItem(new ItemBuilder(Material.ENDER_CHEST).setName("§8» §aCosmetics Kiste").build(), 12, () -> teleport("CratesWarp"));
        putItem(new ItemBuilder(Material.PRISMARINE_SHARD).setName("§8» §bTägliche Belohnungen").build(), 14,
                () -> teleport("DailyBonusWarp"));
        putItem(new ItemBuilder(Material.GLOWSTONE_DUST).setName("§8» §6Spawn").build(), 16, () -> teleport("SpawnWarp"));
        new HSound(Sound.ENTITY_PLAYER_LEVELUP, 10, 4).playFor(player);
    }

    private void teleport(String locationName) {
        ConfigLocation locationUtil = new ConfigLocation(plugin, locationName);
        if (locationUtil.loadLocation() != null) {
            new HSound(Sound.ENTITY_ENDER_DRAGON_FLAP).playFor(player);
            player.setVelocity(new Vector(0, 2, 0));
            player.closeInventory();
            new BukkitRunnable() {
                public void run() {
                    player.teleport(locationUtil.loadLocation());
                }
            }.runTaskLater(plugin, 15);
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX_LEGACY + "§cDie " + locationName + "-Location wurde noch nicht gesetzt!");
        }
    }

    @Override
    public void onUpdate() {
    }

}
