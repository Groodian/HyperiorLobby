/*
package de.groodian.lobby.util;

import de.groodian.hyperiorcore.util.PacketReader;
import de.groodian.lobby.main.Main;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

public class LobbyPacketReader extends PacketReader {

    private Main plugin;

    public LobbyPacketReader(Player player, Main plugin) {
        super(player);
        this.plugin = plugin;
    }

    @Override
    protected void readPacket(Packet<?> packet) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            int id = (int) getValue(packet, "a");
            if (plugin.getMpJoin().getEntityID() == id) {
                if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {
                    plugin.getMinecraftPartyGUI().openMainGUI(player);
                }
            }
            if (plugin.getDailyBonus().getEntityID() == id) {
                if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {
                    plugin.getDailyBonusGUI().openGUI(player);
                }
            }
        }
    }

}

 */
