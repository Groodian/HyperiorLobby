package de.groodian.lobby.network;

import de.groodian.lobby.main.Main;
import de.groodian.network.Client;
import de.groodian.network.DataPackage;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class LobbyClient extends Client {

    private Map<String, LobbyServiceInfo> lobbyServiceInfos;
    private Map<String, MinecraftPartyServiceInfo> minecraftPartyServiceInfos;

    public LobbyClient(String hostname, int port, DataPackage loginPack) {
        super(hostname, port, loginPack);
        lobbyServiceInfos = new HashMap<>();
        minecraftPartyServiceInfos = new HashMap<>();
    }

    @Override
    protected void handleDataPackage(DataPackage dataPackage) {
        String header = dataPackage.get(0).toString();
        if (header.equalsIgnoreCase("UPDATE")) {
            String group = dataPackage.get(1).toString();
            int groupNumber = (int) dataPackage.get(2);
            if (group.equals("MINECRAFTPARTY")) {
                minecraftPartyServiceInfos.put(group + "-" + groupNumber, new MinecraftPartyServiceInfo(
                        dataPackage.get(3).toString(),
                        (int) dataPackage.get(4),
                        (int) dataPackage.get(5)
                ));
            } else if (group.equals("LOBBY")) {
                lobbyServiceInfos.put(group + "-" + groupNumber, new LobbyServiceInfo(
                        (int) dataPackage.get(3),
                        (int) dataPackage.get(4)
                ));
            } else {
                System.out.println("[Client] Unknown group: " + group);
            }
        } else if (header.equalsIgnoreCase("DISCONNECTED")) {
            String group = dataPackage.get(1).toString();
            int groupNumber = (int) dataPackage.get(2);
            if (group.equals("MINECRAFTPARTY")) {
                if (minecraftPartyServiceInfos.containsKey(group + "-" + groupNumber)) {
                    minecraftPartyServiceInfos.remove(group + "-" + groupNumber);
                }
            } else if (group.equals("LOBBY")) {
                if (lobbyServiceInfos.containsKey(group + "-" + groupNumber)) {
                    lobbyServiceInfos.remove(group + "-" + groupNumber);
                }
            } else {
                System.out.println("[Client] Unknown group: " + group);
            }
        } else {
            System.out.println("[Client] Unknown header: " + header);
        }
    }

    public void sendUpdate() {
        sendMessage(new DataPackage("SERVICE_INFO", Bukkit.getOnlinePlayers().size(), Main.MAX_PLAYERS));
    }

    public Map<String, LobbyServiceInfo> getLobbyServiceInfos() {
        return lobbyServiceInfos;
    }

    public Map<String, MinecraftPartyServiceInfo> getMinecraftPartyServiceInfos() {
        return minecraftPartyServiceInfos;
    }

}
