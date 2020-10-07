package de.groodian.lobby.spawnable;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.Particle;
import de.groodian.lobby.main.Main;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Spawn {

    private Main plugin;
    private Double pos0;
    private Double pos1;

    public Spawn(Main plugin) {
        this.plugin = plugin;

        pos0 = 0.0D;
        pos1 = 3.2D;

        spawnParticleAndClay();
    }

    private void spawnParticleAndClay() {
        ConfigLocation locationUtil = new ConfigLocation(plugin, "Spawn");
        Location location;

        if (locationUtil.loadLocation() != null) {
            location = locationUtil.loadLocation();
            location.add(0.0D, 1.5D, 0.0D);
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie Spawn-Location wurde noch nicht gesetzt!");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Location tempLoc0 = location.clone();
                Location tempLoc1 = location.clone();
                if (pos0 >= 6.4D)
                    pos0 = 0.0D;
                if (pos1 >= 6.4D)
                    pos1 = 0.0D;
                tempLoc0.add(Math.sin(pos0), 0, Math.cos(pos0));
                tempLoc1.add(Math.sin(pos1), 0, Math.cos(pos1));
                Particle particle0 = new Particle(EnumParticle.FIREWORKS_SPARK, tempLoc0, true, 0, 0, 0, 0, 1, 0);
                Particle particle1 = new Particle(EnumParticle.FIREWORKS_SPARK, tempLoc1, true, 0, 0, 0, 0, 1, 0);
                particle0.sendAll();
                particle1.sendAll();
                pos0 += 0.2;
                pos1 += 0.2;
            }
        }.runTaskTimer(plugin, 40, 2);

        Random random = new Random();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (all.getLocation().distance(location) < 50) {
                        all.sendBlockChange(location.clone().add(1, -2, 0), Material.STAINED_CLAY, (byte) random.nextInt(15));
                        all.sendBlockChange(location.clone().add(-1, -2, 0), Material.STAINED_CLAY, (byte) random.nextInt(15));
                        all.sendBlockChange(location.clone().add(0, -2, 1), Material.STAINED_CLAY, (byte) random.nextInt(15));
                        all.sendBlockChange(location.clone().add(0, -2, -1), Material.STAINED_CLAY, (byte) random.nextInt(15));
                    }
                }
            }
        }.runTaskTimer(plugin, 40, 20);

    }

}
