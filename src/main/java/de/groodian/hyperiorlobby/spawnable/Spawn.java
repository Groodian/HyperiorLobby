package de.groodian.hyperiorlobby.spawnable;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.HParticle;
import de.groodian.hyperiorlobby.main.Main;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Spawn {

    private final Main plugin;
    private final Random random;

    private Double pos0;
    private Double pos1;

    public Spawn(Main plugin) {
        this.plugin = plugin;
        this.random = new Random();

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

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (all.getLocation().distance(location) < 50) {
                        all.sendBlockChange(location.clone().add(1, -2, 0), getRandomTerracotta());
                        all.sendBlockChange(location.clone().add(-1, -2, 0), getRandomTerracotta());
                        all.sendBlockChange(location.clone().add(0, -2, 1), getRandomTerracotta());
                        all.sendBlockChange(location.clone().add(0, -2, -1), getRandomTerracotta());
                    }
                }
            }
        }.runTaskTimer(plugin, 40, 20);

    }

    private BlockData getRandomTerracotta() {
        int i = 0;
        int item = random.nextInt(Tag.TERRACOTTA.getValues().size());
        for (Material material : Tag.TERRACOTTA.getValues()) {
            if (i == item) {
                return material.createBlockData();
            }
            i++;
        }

        return Material.TERRACOTTA.createBlockData();
    }

}
