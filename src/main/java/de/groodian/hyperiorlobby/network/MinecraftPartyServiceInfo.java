package de.groodian.hyperiorlobby.network;

public class MinecraftPartyServiceInfo {

    private final String gameState;
    private final int onlinePlayers;
    private final int maxPlayers;

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
