package de.groodian.lobby.spawnable;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.HParticle;
import de.groodian.hyperiorcore.util.Hologram;
import de.groodian.lobby.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class Crate {

    private final Main plugin;
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
            hologram = new Hologram(location.subtract(0, 1.2, 0), "§a§lCOSMETICS KISTE", "§fÖffne Kisten und erhalte coole Gegenstände.");
            hologram.spawnHologram();
            location.add(0, 0.7, 0);
            working = true;
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX_LEGACY + "§4Die Crates-Location wurde noch nicht gesetzt!");
        }
    }

    private void run() {
        HParticle particle = new HParticle(Particle.FLAME, 0, null);

        new BukkitRunnable() {
            @Override
            public void run() {
                Location temp = location.clone();
                if (x >= 6.4D) {
                    x = 0D;
                    z = 0D;
                }
                temp.add(Math.sin(x), 0, Math.cos(z));
                particle.send(temp);
                x += 0.2;
                z += 0.2;
            }
        }.runTaskTimer(plugin, 40, 2);
    }

    public boolean isWorking() {
        return working;
    }

}
