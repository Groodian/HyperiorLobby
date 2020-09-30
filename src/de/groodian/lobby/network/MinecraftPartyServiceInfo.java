package de.groodian.lobby.network;

public class MinecraftPartyServiceInfo {

    private String gameState;
    private int onlinePlayers;
    private int maxPlayers;

    public MinecraftPartyServiceInfo(String gameState, int onlinePlayers, int maxPlayers) {
        this.gameState = gameState;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
    }

    public String getGameState() {
        return gameState;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

}
