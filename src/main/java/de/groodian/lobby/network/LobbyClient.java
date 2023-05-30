package de.groodian.lobby.network;

import de.groodian.lobby.main.Main;
import de.groodian.network.Client;
import de.groodian.network.DataPackage;
import java.util.HashMap;
import java.util.Map;

public class LobbyClient extends Client {

    private final Main plugin;

    private final Map<Integer, LobbyServiceInfo> lobbyServiceInfos;
    private final Map<Integer, MinecraftPartyServiceInfo> minecraftPartyServiceInfos;

    public LobbyClient(Main plugin, String hostname, int port, DataPackage loginPack) {
        super(hostname, port, loginPack);
        this.plugin = plugin;
        lobbyServiceInfos = new HashMap<>();
        minecraftPartyServiceInfos = new HashMap<>();
    }

    @Override
    protected void handleDataPackage(DataPackage dataPackage) {
        String header = dataPackage.get(0).toString();
        if (header.equalsIgnoreCase("UPDATE")) {
            String group = dataPackage.get(1).toString();
            int groupNumber = (int) dataPackage.get(2);
            if (group.equalsIgnoreCase("MINECRAFTPARTY")) {
                minecraftPartyServiceInfos.put(groupNumber,
                        new MinecraftPartyServiceInfo(dataPackage.get(3).toString(), (int) dataPackage.get(4), (int) dataPackage.get(5)));
            } else if (group.equalsIgnoreCase("LOBBY")) {
                lobbyServiceInfos.put(groupNumber, new LobbyServiceInfo((int) dataPackage.get(3), (int) dataPackage.get(4)));
            } else {
                System.out.println("[Client] Unknown group: " + group);
            }
        } else if (header.equalsIgnoreCase("DISCONNECTED")) {
            String group = dataPackage.get(1).toString();
            int groupNumber = (int) dataPackage.get(2);
            if (group.equalsIgnoreCase("MINECRAFTPARTY")) {
                minecraftPartyServiceInfos.remove(groupNumber);
            } else if (group.equalsIgnoreCase("LOBBY")) {
                lobbyServiceInfos.remove(groupNumber);
            } else {
                System.out.println("[Client] Unknown group: " + group);
            }
        } else {
            System.out.println("[Client] Unknown header: " + header);
        }

        plugin.getMinecraftPartyGUIManager().update();
        //plugin.getMpJoin().updateHologram();
        plugin.getLobbyGUIManager().update();
    }

    @Override
    protected void onSuccessfulLogin() {
        sendUpdate(0);
    }

    public void sendUpdate(int size) {
        sendMessage(new DataPackage("SERVICE_INFO", size, Main.MAX_PLAYERS));
    }

    public Map<Integer, LobbyServiceInfo> getLobbyServiceInfos() {
        return lobbyServiceInfos;
    }

    public Map<Integer, MinecraftPartyServiceInfo> getMinecraftPartyServiceInfos() {
        return minecraftPartyServiceInfos;
    }

}
