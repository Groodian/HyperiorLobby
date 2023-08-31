package de.groodian.hyperiorlobby.spawnable;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.spawnable.Hologram;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.HParticle;
import de.groodian.hyperiorlobby.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class Crate {

    private final Main plugin;
    private Location location;
    private Double x = 0D;
    private Double z = 0D;

    public Crate(Main plugin) {
        this.plugin = plugin;

        create();
        run();
    }

    private void create() {
        Location location = new ConfigLocation(plugin, "Crates").loadLocation();

        if (location == null) {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX_LEGACY + "§4Die Crates-Location wurde noch nicht gesetzt!");
            return;
        }

        Hologram hologram = new Hologram(location.subtract(0, 1.2, 0), "§a§lCOSMETICS KISTE",
                "§fÖffne Kisten und erhalte coole Gegenstände.");
        hologram.showAll();

        HyperiorCore.getPaper().getSpawnAbleManager().registerSpawnAble(hologram);

        location.add(0, 0.7, 0);
        this.location = location;
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

}
