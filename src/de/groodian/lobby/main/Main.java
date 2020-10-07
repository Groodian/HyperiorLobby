package de.groodian.lobby.main;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.groodian.lobby.boards.LobbyScoreboard;
import de.groodian.lobby.commands.BuildCommand;
import de.groodian.lobby.commands.SetupCommand;
import de.groodian.lobby.gui.DailyBonusGUI;
import de.groodian.lobby.gui.LobbyGUI;
import de.groodian.lobby.gui.MinecraftPartyGUI;
import de.groodian.lobby.gui.NavigatorGUI;
import de.groodian.lobby.listener.CrateListener;
import de.groodian.lobby.listener.MainListener;
import de.groodian.lobby.network.LobbyClient;
import de.groodian.lobby.spawnable.Crate;
import de.groodian.lobby.spawnable.DailyBonus;
import de.groodian.lobby.spawnable.MinecraftPartyJoin;
import de.groodian.lobby.spawnable.Spawn;
import de.groodian.network.DataPackage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Main extends JavaPlugin implements PluginMessageListener {

    public static final String PREFIX = "§7[§eLobby§7] §r";
    public static final int MAX_PLAYERS = 50;

    private LobbyClient client;
    private MinecraftPartyGUI minecraftPartyGUI;
    private MinecraftPartyJoin mpJoin;
    private DailyBonus dailyBonus;
    private DailyBonusGUI dailyBonusGUI;
    private Crate crate;
    private Spawn spawn;
    private LobbyGUI lobbyGUI;
    private NavigatorGUI navigatorGUI;
    private LobbyScoreboard lobbyScoreboard;
    private ArrayList<Player> build;
    private int onlineCount;

    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§aDas Plugin wird geladen....");

        minecraftPartyGUI = new MinecraftPartyGUI(this);
        mpJoin = new MinecraftPartyJoin(this);
        dailyBonus = new DailyBonus(this);
        dailyBonusGUI = new DailyBonusGUI(this);
        crate = new Crate(this);
        spawn = new Spawn(this);
        lobbyGUI = new LobbyGUI(this);
        navigatorGUI = new NavigatorGUI(this);
        lobbyScoreboard = new LobbyScoreboard(this);
        build = new ArrayList<>();

        getCommand("lsetup").setExecutor(new SetupCommand(this));
        getCommand("build").setExecutor(new BuildCommand(this));

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(minecraftPartyGUI, this);
        pluginManager.registerEvents(new MainListener(this), this);
        pluginManager.registerEvents(dailyBonusGUI, this);
        pluginManager.registerEvents(lobbyGUI, this);
        pluginManager.registerEvents(navigatorGUI, this);
        pluginManager.registerEvents(new CrateListener(this), this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        updateScoreboard();

        client = new LobbyClient(this, "localhost", 4444, new DataPackage("LOGIN", "Lobby", Integer.parseInt(Bukkit.getServerName())));

        Bukkit.getConsoleSender().sendMessage(PREFIX + "§aGeladen!");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] message) {
        if (!channel.equals("BungeeCord"))
            return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (subChannel.equals("PlayerCount")) {
            in.readUTF();
            onlineCount = in.readInt();
        }
    }

    private void updateScoreboard() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Get BungeeCord Online Players
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("PlayerCount");
                out.writeUTF("ALL");
                if (Bukkit.getOnlinePlayers().size() > 0) {
                    Player player = Bukkit.getOnlinePlayers().iterator().next();
                    player.sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    lobbyScoreboard.update(player);
                }
            }
        }.runTaskTimer(this, 40, 20);
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§cDas Plugin wird gestoppt....");

        client.stop();

        Bukkit.getConsoleSender().sendMessage(PREFIX + "§cGestoppt!");
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public LobbyGUI getLobbyGUI() {
        return lobbyGUI;
    }

    public LobbyClient getClient() {
        return client;
    }

    public MinecraftPartyGUI getMinecraftPartyGUI() {
        return minecraftPartyGUI;
    }

    public MinecraftPartyJoin getMpJoin() {
        return mpJoin;
    }

    public DailyBonus getDailyBonus() {
        return dailyBonus;
    }

    public DailyBonusGUI getDailyBonusGUI() {
        return dailyBonusGUI;
    }

    public Crate getCrates() {
        return crate;
    }

    public NavigatorGUI getNavigator() {
        return navigatorGUI;
    }

    public LobbyScoreboard getLobbyScoreboard() {
        return lobbyScoreboard;
    }

    public ArrayList<Player> getBuild() {
        return build;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public Plugin getPlugin() {
        return getPlugin(Main.class);
    }

}