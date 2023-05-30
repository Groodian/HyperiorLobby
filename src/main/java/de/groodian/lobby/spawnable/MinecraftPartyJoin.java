/*package de.groodian.lobby.spawnable;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.Hologram;
import de.groodian.hyperiorcore.util.NPC;
import de.groodian.lobby.main.Main;
import de.groodian.lobby.network.MinecraftPartyServiceInfo;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Map;

public class MinecraftPartyJoin {

    private Main plugin;
    private NPC npc;
    private Hologram hologram;
    private Location location;
    private int waiting;
    private int playing;
    private boolean working;

    public MinecraftPartyJoin(Main plugin) {
        this.plugin = plugin;

        working = false;

        create();
    }

    private void create() {
        ConfigLocation util = new ConfigLocation(plugin, "MinecraftPartyJoin");
        if (util.loadLocation() != null) {
            location = util.loadLocation();
            npc = new NPC(location, plugin, "§r");
            npc.showAll();
            npc.equip(0, new ItemStack(Items.DIAMOND_SWORD));
            npc.changeSkin("eyJ0aW1lc3RhbXAiOjE1NjU3ODI1MDQ4MTcsInByb2ZpbGVJZCI6ImFiNTQwMDc4OGQ4NTQwZjJhMzMwMmI0NjkyYjY1NzZmIiwicHJvZmlsZU5hbWUiOiJHcm9vZGlhbiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmJjN2U4ZWY0ZWQ5YjgyNmM0NTRiYmY1MmUxYWYyOGJhMTg3ZmFhYTI3NDFmNWNlOTQzOGE5ZTY0OGIxNjdlNiJ9fX0=", "grkUh2sQ56Al8dnfHnqIcQI3hKUxeidMJ153gCJk0ng4OpgvxkFj7A3zYO9CklbeXuWPtHxIbDzzdxv6RDqUVIsuGYNQJFWMDRBvcZaUGkIvd5buv1XftKR5JWDjxINwQXauAs8YPZw3LcPFdEnjzgnf1BdPKJ7c/81yr3UyNmnCG6mtuEVL/CmGN1wRGQ7ylkVgeSBi/Hutixr4hnz83b1koBeJENrazsVos/ZWvKr8chrgJ/HSyo362QKfnjMvdz0Io4BW67jjo7KcChRjg2+0jpM+FeVUQV3voP6bQEEBMSkTUMixh9QlHMuT047ho8T5/Oc16TYgj6oy2MY5291iXjUIpru5i6GmTLnitsVsM+Uj6gJLxZ+ARnomOFLmnwle+LQDRI5bAnnuVbKX7BEsyE3kWkbmA8T1hqcGVPQIS7FpK0SX9PEnHxUNFfHUt0gFxKjzwzPA/uTB2aal5Ek0N+445WEoHSZMmLZiIo1B+ZiXzUOawiJdxF8DEVtkFNUW8Y/pn9CkbypO+hi1AmA9qX4wynFhcjJJUqPwTLszkQMdqJxPUHVa3NkFBhuDeGX98o2XvKt0hxZ77eZeExg5IAocBbkscxv9gDLwGvRYCJMfYi1B1dkmYCqTMrX2diNfE9/bEaeGF0Nmj692QD4q/e44tS2MNVDNnAi7m4Q=");
            updateHologram();
            working = true;
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4Die MinecraftPartyJoin-Location wurde noch nicht gesetzt!");
        }
    }

    public void updateHologram() {
        waiting = 0;
        playing = 0;

        if (plugin.getClient() != null) {
            for (Map.Entry<String, MinecraftPartyServiceInfo> info : plugin.getClient().getMinecraftPartyServiceInfos().entrySet()) {
                if (info.getValue().getGameState().equalsIgnoreCase("LOBBY")) {
                    waiting += info.getValue().getOnlinePlayers();
                } else {
                    playing += info.getValue().getOnlinePlayers();
                }
            }
        }

        String waitingMSG = "";
        String playingMSG = "";
        if (waiting == 1) {
            waitingMSG = "§71 wartender Spieler";
        } else {
            waitingMSG = "§7" + waiting + " wartende Spieler";
        }
        if (playing == 1) {
            playingMSG = "§71 spielender Spieler";
        } else {
            playingMSG = "§7" + playing + " spielende Spieler";
        }

        if (hologram != null) {
            hologram.destroy();
        }
        hologram = new Hologram(location.clone().add(0, 0.4, 0), "§6§lMinecraftParty", waitingMSG, playingMSG);
        hologram.showAll();
    }

    public int getEntityID() {
        return npc.getEntityID();
    }

    public boolean isWorking() {
        return working;
    }

}*/
