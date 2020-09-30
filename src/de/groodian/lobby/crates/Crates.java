package de.groodian.lobby.crates;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.Hologram;
import de.groodian.hyperiorcore.util.Particle;
import de.groodian.lobby.main.Main;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Crates {

    private Main plugin;
    private Hologram hologram;
    private Location location;
    private Double x = 0D;
    private Double z = 0D;
    private boolean working;

    public Crates(Main plugin) {
        this.plugin = plugin;

        working = false;

        create();
    }

    private void create() {
        ConfigLocation util = new ConfigLocation(plugin, "Crates");
        if (util.loadLocation() != null) {
            location = util.loadLocation();
            hologram = new Hologram(location.subtract(0, 1.2, 0), true, "�a�lTRAILS KISTE", "�f�ffne Kisten und erhalte coole Gegenst�nde.");
            location.add(0, 0.7, 0);
            working = true;
            run();
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "�4Die Crates-Location wurde noch nicht gesetzt!");
        }
    }

    private void run() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Location temp = location.clone();
            if (x >= 6.4D) {
                x = 0D;
                z = 0D;
            }
            temp.add(Math.sin(x), 0, Math.cos(z));
            Particle particle0 = new Particle(EnumParticle.FLAME, temp, true, 0, 0, 0, 0, 1, 0);
            particle0.sendAll();
            x += 0.2;
            z += 0.2;
        }, 40, 2);
    }

    public boolean isWorking() {
        return working;
    }
}
