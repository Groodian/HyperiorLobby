package de.groodian.lobby.spawnable;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.spawnable.NPC;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.Hologram;
import de.groodian.hyperiorcore.util.ItemBuilder;
import de.groodian.lobby.gui.MinecraftPartyGUI;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.network.MinecraftPartyServiceInfo;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class MinecraftPartyJoin {

    private final Main plugin;
    private Hologram hologram;
    private Location location;

    public MinecraftPartyJoin(Main plugin) {
        this.plugin = plugin;

        create();
        updateHologram();
    }

    private void create() {
        Location location = new ConfigLocation(plugin, "MinecraftPartyJoin").loadLocation();

        if (location == null) {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4Die MinecraftPartyJoin-Location wurde noch nicht gesetzt!");
            return;
        }

        NPC npc = new NPC(location, "§r");
        npc.setItemMainHand(new ItemBuilder(Material.DIAMOND_SWORD).addGlow().build());
        npc.setSkin(
                "eyJ0aW1lc3RhbXAiOjE1NjU3ODI1MDQ4MTcsInByb2ZpbGVJZCI6ImFiNTQwMDc4OGQ4NTQwZjJhMzMwMmI0NjkyYjY1NzZmIiwicHJvZmlsZU5hbWUiOiJHcm9vZGlhbiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmJjN2U4ZWY0ZWQ5YjgyNmM0NTRiYmY1MmUxYWYyOGJhMTg3ZmFhYTI3NDFmNWNlOTQzOGE5ZTY0OGIxNjdlNiJ9fX0=",
                "grkUh2sQ56Al8dnfHnqIcQI3hKUxeidMJ153gCJk0ng4OpgvxkFj7A3zYO9CklbeXuWPtHxIbDzzdxv6RDqUVIsuGYNQJFWMDRBvcZaUGkIvd5buv1XftKR5JWDjxINwQXauAs8YPZw3LcPFdEnjzgnf1BdPKJ7c/81yr3UyNmnCG6mtuEVL/CmGN1wRGQ7ylkVgeSBi/Hutixr4hnz83b1koBeJENrazsVos/ZWvKr8chrgJ/HSyo362QKfnjMvdz0Io4BW67jjo7KcChRjg2+0jpM+FeVUQV3voP6bQEEBMSkTUMixh9QlHMuT047ho8T5/Oc16TYgj6oy2MY5291iXjUIpru5i6GmTLnitsVsM+Uj6gJLxZ+ARnomOFLmnwle+LQDRI5bAnnuVbKX7BEsyE3kWkbmA8T1hqcGVPQIS7FpK0SX9PEnHxUNFfHUt0gFxKjzwzPA/uTB2aal5Ek0N+445WEoHSZMmLZiIo1B+ZiXzUOawiJdxF8DEVtkFNUW8Y/pn9CkbypO+hi1AmA9qX4wynFhcjJJUqPwTLszkQMdqJxPUHVa3NkFBhuDeGX98o2XvKt0hxZ77eZeExg5IAocBbkscxv9gDLwGvRYCJMfYi1B1dkmYCqTMrX2diNfE9/bEaeGF0Nmj692QD4q/e44tS2MNVDNnAi7m4Q=");
        npc.setInteract(player -> {
            plugin.getMinecraftPartyGUIManager().open(player, new MinecraftPartyGUI(plugin));
        });
        npc.showAll();

        HyperiorCore.getSpawnAbleManager().registerSpawnAble(npc);

        this.location = location;
    }

    public void updateHologram() {
        int waiting = 0;
        int playing = 0;

        if (plugin.getClient() != null) {
            for (Map.Entry<Integer, MinecraftPartyServiceInfo> info : plugin.getClient().getMinecraftPartyServiceInfos().entrySet()) {
                if (info.getValue().getGameState().equalsIgnoreCase("LOBBY")) {
                    waiting += info.getValue().getOnlinePlayers();
                } else {
                    playing += info.getValue().getOnlinePlayers();
                }
            }
        }

        String waitingMSG;
        if (waiting == 1) {
            waitingMSG = "§71 wartender Spieler";
        } else {
            waitingMSG = "§7" + waiting + " wartende Spieler";
        }

        String playingMSG;
        if (playing == 1) {
            playingMSG = "§71 spielender Spieler";
        } else {
            playingMSG = "§7" + playing + " spielende Spieler";
        }

        if (hologram != null) {
            hologram.destroyHologram();
        }

        hologram = new Hologram(location.clone().add(0, 0.4, 0), "§6§lMinecraftParty", waitingMSG, playingMSG);
        hologram.spawnHologram();
    }

}
