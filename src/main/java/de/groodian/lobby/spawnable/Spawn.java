package de.groodian.lobby.spawnable;

import de.groodian.hyperiorcore.util.ColoredMaterial;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.HParticle;
import de.groodian.lobby.main.Main;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Spawn {

    private final Main plugin;
    private final List<Material> clay;
    private Double pos0;
    private Double pos1;

    public Spawn(Main plugin) {
        this.plugin = plugin;
        this.clay = ColoredMaterial.get("_TERRACOTTA");

        pos0 = 0.0D;
        pos1 = 3.2D;

        spawnParticleAndClay();
    }

    private void spawnParticleAndClay() {
        ConfigLocation locationUtil = new ConfigLocation(plugin, "Spawn");
        Location location = locationUtil.loadLocation();

        if (location == null) {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX_LEGACY + "§cDie Spawn-Location wurde noch nicht gesetzt!");
            return;
        }

        location.add(0.0D, 1.5D, 0.0D);

        HParticle particle = new HParticle(Particle.FIREWORKS_SPARK, 0, null);

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

                particle.send(tempLoc0);
                particle.send(tempLoc1);

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
                        all.sendBlockChange(location.clone().add(1, -2, 0), clay.get(random.nextInt(clay.size())).createBlockData());
                        all.sendBlockChange(location.clone().add(-1, -2, 0), clay.get(random.nextInt(clay.size())).createBlockData());
                        all.sendBlockChange(location.clone().add(0, -2, 1), clay.get(random.nextInt(clay.size())).createBlockData());
                        all.sendBlockChange(location.clone().add(0, -2, -1), clay.get(random.nextInt(clay.size())).createBlockData());
                    }
                }
            }
        }.runTaskTimer(plugin, 40, 20);

    }

}
