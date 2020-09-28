package de.groodian.lobby.dailybonus;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.groodian.lobby.main.Main;
import de.groodian.lobby.util.ConfigLocationUtil;
import de.groodian.lobby.util.Holograms;
import de.groodian.lobby.util.NPCManager;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;

public class DailyBonus {

	private Main plugin;
	private Location location;
	private NPCManager npc;
	private Holograms hologram;
	private boolean working = false;

	public DailyBonus(Main plugin) {
		this.plugin = plugin;
		set();
	}

	public void set() {
		ConfigLocationUtil util = new ConfigLocationUtil(plugin, "DailyBonus");
		if (util.loadLocation() != null) {
			location = util.loadLocation();
			npc = new NPCManager("§f", location, plugin);
			npc.equip(0, new ItemStack(Items.DIAMOND));
			npc.changeSkin(
					"eyJ0aW1lc3RhbXAiOjE1NjU3ODI1MDQ4MTcsInByb2ZpbGVJZCI6ImFiNTQwMDc4OGQ4NTQwZjJhMzMwMmI0NjkyYjY1NzZmIiwicHJvZmlsZU5hbWUiOiJHcm9vZGlhbiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmJjN2U4ZWY0ZWQ5YjgyNmM0NTRiYmY1MmUxYWYyOGJhMTg3ZmFhYTI3NDFmNWNlOTQzOGE5ZTY0OGIxNjdlNiJ9fX0=",
					"grkUh2sQ56Al8dnfHnqIcQI3hKUxeidMJ153gCJk0ng4OpgvxkFj7A3zYO9CklbeXuWPtHxIbDzzdxv6RDqUVIsuGYNQJFWMDRBvcZaUGkIvd5buv1XftKR5JWDjxINwQXauAs8YPZw3LcPFdEnjzgnf1BdPKJ7c/81yr3UyNmnCG6mtuEVL/CmGN1wRGQ7ylkVgeSBi/Hutixr4hnz83b1koBeJENrazsVos/ZWvKr8chrgJ/HSyo362QKfnjMvdz0Io4BW67jjo7KcChRjg2+0jpM+FeVUQV3voP6bQEEBMSkTUMixh9QlHMuT047ho8T5/Oc16TYgj6oy2MY5291iXjUIpru5i6GmTLnitsVsM+Uj6gJLxZ+ARnomOFLmnwle+LQDRI5bAnnuVbKX7BEsyE3kWkbmA8T1hqcGVPQIS7FpK0SX9PEnHxUNFfHUt0gFxKjzwzPA/uTB2aal5Ek0N+445WEoHSZMmLZiIo1B+ZiXzUOawiJdxF8DEVtkFNUW8Y/pn9CkbypO+hi1AmA9qX4wynFhcjJJUqPwTLszkQMdqJxPUHVa3NkFBhuDeGX98o2XvKt0hxZ77eZeExg5IAocBbkscxv9gDLwGvRYCJMfYi1B1dkmYCqTMrX2diNfE9/bEaeGF0Nmj692QD4q/e44tS2MNVDNnAi7m4Q=");
			hologram = new Holograms(location.add(0, 0.2, 0), "§b§lTÄGLICHE BELOHNUNG", "§fHol dir deine tägliche Belohnung ab!");
			working = true;
		} else {
			Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4Die DailyBonus-Location wurde noch nicht gesetzt!");
		}
	}

	public NPCManager getNpc() {
		return npc;
	}

	public Holograms getHologram() {
		return hologram;
	}

	public int getEntityID() {
		return npc.getEntityID();
	}

	public boolean isWorking() {
		return working;
	}

}
