package de.groodian.lobby.crates;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.groodian.lobby.main.Main;
import de.groodian.lobby.util.ConfigLocationUtil;
import de.groodian.lobby.util.Holograms;
import de.groodian.lobby.util.Particle;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Crates {

	private Holograms hologram;
	private Location location;
	private Main plugin;
	private Double x = 0D;
	private Double z = 0D;
	private boolean working = false;

	public Crates(Main plugin) {
		this.plugin = plugin;
		set();
	}

	private void set() {
		ConfigLocationUtil util = new ConfigLocationUtil(plugin, "Crates");
		if (util.loadLocation() != null) {
			location = util.loadLocation();
			hologram = new Holograms(location.subtract(0, 1.2, 0), "§a§lTRAILS KISTE", "§fÖffne Kisten und erhalte coole Gegenstände.");
			location.add(0, 0.7, 0);
			working = true;
			run();
		} else {
			Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4Die Crates-Location wurde noch nicht gesetzt!");
		}
	}

	private void run() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
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
			}
		}, 40, 2);
	}

	public Holograms getHologram() {
		return hologram;
	}
	
	public boolean isWorking() {
		return working;
	}
}
