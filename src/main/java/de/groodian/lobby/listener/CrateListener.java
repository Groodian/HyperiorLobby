package de.groodian.lobby.listener;

import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.lobby.main.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CrateListener implements Listener {

    private final Main plugin;

    public CrateListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
                ConfigLocation util = new ConfigLocation(plugin, "Crates");
                if (e.getClickedBlock().getLocation().distance(util.loadLocation()) < 3) {
                    e.setCancelled(true);
                    //HyperiorCosmetic.openCrateMenu(e.getPlayer());
                }
            }
        }
    }
}
