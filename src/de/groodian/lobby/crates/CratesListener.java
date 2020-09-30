package de.groodian.lobby.crates;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.lobby.main.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CratesListener implements Listener {

    private Main plugin;

    public CratesListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onIntercat(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType() != null) {
                if (e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
                    ConfigLocation util = new ConfigLocation(plugin, "Crates");
                    if (e.getClickedBlock().getLocation().distance(util.loadLocation()) < 3) {
                        e.setCancelled(true);
                        new CratesGUI(plugin).openGUI(e.getPlayer());
                    }
                }
            }
        }
    }
}
