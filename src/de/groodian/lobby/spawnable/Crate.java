package de.groodian.lobby.spawnable;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.Hologram;
import de.groodian.hyperiorcore.util.Particle;
import de.groodian.lobby.main.Main;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Crate {

    private Main plugin;
    private Hologram hologram;
    private Location location;
    private Double x = 0D;
    private Double z = 0D;
    private boolean working;

    public Crate(Main plugin) {
        this.plugin = plugin;

        working = false;

        create();
        run();
    }

    private void create() {
        ConfigLocation util = new ConfigLocation(plugin, "Crates");
        if (util.loadLocation() != null) {
            location = util.loadLocation();
            hologram = new Hologram(location.subtract(0, 1.2, 0), "§a§lTRAILS KISTE", "§fÖffne Kisten und erhalte coole Gegenstände.");
            hologram.showAll();
            location.add(0, 0.7, 0);
            working = true;
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4Die Crates-Location wurde noch nicht gesetzt!");
        }
    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Location temp = location.clone();
                if (x >= 6.4D) {
                    x = 0D;
                    z = 0D;
                }
                temp.add(Math.sin(x), 0, Math.cos(z));
                new Particle(EnumParticle.FLAME).send(temp);
                x += 0.2;
                z += 0.2;
            }
        }.runTaskTimer(plugin, 40, 2);
    }

    public boolean isWorking() {
        return working;
    }
}
