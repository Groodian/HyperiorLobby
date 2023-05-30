package de.groodian.lobby.network;

public class LobbyServiceInfo {

    private final int onlinePlayers;
    private final int maxPlayers;

    public LobbyServiceInfo(int onlinePlayers, int maxPlayers) {
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
