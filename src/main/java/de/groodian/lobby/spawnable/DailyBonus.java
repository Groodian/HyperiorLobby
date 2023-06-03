package de.groodian.lobby.spawnable;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.spawnable.NPC;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.Hologram;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.lobby.gui.DailyBonusGUI;
import de.groodian.lobby.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class DailyBonus {

    private final Main plugin;

    public DailyBonus(Main plugin) {
        this.plugin = plugin;

        create();
    }

    private void create() {
        Location location = new ConfigLocation(plugin, "DailyBonus").loadLocation();

        if (location == null) {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4Die DailyBonus-Location wurde noch nicht gesetzt!");
            return;
        }

        NPC npc = new NPC(location, "§f");
        npc.setItemMainHand(new ItemBuilder(Material.DIAMOND).addGlow().build());
        npc.setSkin(
                "eyJ0aW1lc3RhbXAiOjE1NjU3ODI1MDQ4MTcsInByb2ZpbGVJZCI6ImFiNTQwMDc4OGQ4NTQwZjJhMzMwMmI0NjkyYjY1NzZmIiwicHJvZmlsZU5hbWUiOiJHcm9vZGlhbiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmJjN2U4ZWY0ZWQ5YjgyNmM0NTRiYmY1MmUxYWYyOGJhMTg3ZmFhYTI3NDFmNWNlOTQzOGE5ZTY0OGIxNjdlNiJ9fX0=",
                "grkUh2sQ56Al8dnfHnqIcQI3hKUxeidMJ153gCJk0ng4OpgvxkFj7A3zYO9CklbeXuWPtHxIbDzzdxv6RDqUVIsuGYNQJFWMDRBvcZaUGkIvd5buv1XftKR5JWDjxINwQXauAs8YPZw3LcPFdEnjzgnf1BdPKJ7c/81yr3UyNmnCG6mtuEVL/CmGN1wRGQ7ylkVgeSBi/Hutixr4hnz83b1koBeJENrazsVos/ZWvKr8chrgJ/HSyo362QKfnjMvdz0Io4BW67jjo7KcChRjg2+0jpM+FeVUQV3voP6bQEEBMSkTUMixh9QlHMuT047ho8T5/Oc16TYgj6oy2MY5291iXjUIpru5i6GmTLnitsVsM+Uj6gJLxZ+ARnomOFLmnwle+LQDRI5bAnnuVbKX7BEsyE3kWkbmA8T1hqcGVPQIS7FpK0SX9PEnHxUNFfHUt0gFxKjzwzPA/uTB2aal5Ek0N+445WEoHSZMmLZiIo1B+ZiXzUOawiJdxF8DEVtkFNUW8Y/pn9CkbypO+hi1AmA9qX4wynFhcjJJUqPwTLszkQMdqJxPUHVa3NkFBhuDeGX98o2XvKt0hxZ77eZeExg5IAocBbkscxv9gDLwGvRYCJMfYi1B1dkmYCqTMrX2diNfE9/bEaeGF0Nmj692QD4q/e44tS2MNVDNnAi7m4Q=");
        npc.setInteract(player -> {
            plugin.getDailyBonusGUIManager().open(player, new DailyBonusGUI());
        });
        npc.showAll();

        HyperiorCore.getSpawnAbleManager().registerSpawnAble(npc);

        Hologram hologram = new Hologram(location.add(0, 0.2, 0), "§b§lTÄGLICHE BELOHNUNG", "§fHol dir deine tägliche Belohnung ab!");
        hologram.spawnHologram();
    }

}
