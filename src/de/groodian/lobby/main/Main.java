package de.groodian.lobby.main;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.groodian.hyperiorcore.util.ConfigLocation;
import de.groodian.hyperiorcore.util.Particle;
import de.groodian.lobby.boards.LobbyScoreboard;
import de.groodian.lobby.commands.BuildCommand;
import de.groodian.lobby.commands.LobbySetup;
import de.groodian.lobby.commands.MinecraftPartyServers;
import de.groodian.lobby.cosmetics.MySQLCosmetics;
import de.groodian.lobby.crates.Crates;
import de.groodian.lobby.crates.CratesGUI;
import de.groodian.lobby.crates.CratesListener;
import de.groodian.lobby.dailybonus.DailyBonus;
import de.groodian.lobby.dailybonus.DailyBonusGUI;
import de.groodian.lobby.gui.Navigator;
import de.groodian.lobby.gui.ServerGUI;
import de.groodian.lobby.listener.MainListener;
import de.groodian.lobby.minecraftpartyjoin.MinecraftPartyJoin;
import de.groodian.lobby.network.LobbyClient;
import de.groodian.network.DataPackage;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Main extends JavaPlugin implements PluginMessageListener {

    public static final String PREFIX = "§eLobby §7>> §r";
    public static final int MAX_PLAYERS = 50;

    private LobbyClient client;
    private ServerGUI gui;
    private MinecraftPartyJoin mpJoin;
    private DailyBonus dailyBonus;
    private DailyBonusGUI dailyBonusGUI;
    private Crates crates;
    private Navigator navigator;
    private LobbyScoreboard lobbyScoreboard;
    private ArrayList<Player> build;
    private int onlineCount;

    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§aDas Plugin wird geladen....");

        client = new LobbyClient("localhost", 4444, new DataPackage("LOGIN", "Lobby", 1));
        client.sendUpdate();

        MySQLCosmetics.connect();

        try {
            PreparedStatement ps05 = MySQLCosmetics.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS cosmetics (UUID VARCHAR(100), playername VARCHAR(100), cosmetics VARCHAR(9999), dailybonus VARCHAR(100), dailybonusvip VARCHAR(100), trail VARCHAR(100), helmet VARCHAR(100), chestplate VARCHAR(100), pants VARCHAR(100), shoes VARCHAR(100), gadget VARCHAR(100))");
            ps05.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        gui = new ServerGUI(this);
        mpJoin = new MinecraftPartyJoin(this);
        dailyBonus = new DailyBonus(this);
        dailyBonusGUI = new DailyBonusGUI(this);
        crates = new Crates(this);
        navigator = new Navigator(this);
        lobbyScoreboard = new LobbyScoreboard(this);
        build = new ArrayList<>();

        getCommand("mpjoin").setExecutor(new MinecraftPartyServers(this));
        getCommand("lsetup").setExecutor(new LobbySetup(this));
        getCommand("build").setExecutor(new BuildCommand(this));

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(gui, this);
        pluginManager.registerEvents(new MainListener(this), this);
        pluginManager.registerEvents(new CratesGUI(this), this);
        pluginManager.registerEvents(dailyBonusGUI, this);
        pluginManager.registerEvents(navigator, this);
        pluginManager.registerEvents(new CratesListener(this), this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        updateScoreboard();
        spawnParticleAndClay();

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
                    lobbyScoreboard.updateLobbyScoreboard(player);
                }
            }
        }.runTaskTimerAsynchronously(this, 40, 20);
    }

    private Double pos0 = 0.0D;
    private Double pos1 = 3.2D;

    private void spawnParticleAndClay() {
        ConfigLocation locationUtil = new ConfigLocation(this, "Spawn");
        Location location;

        if (locationUtil.loadLocation() != null) {
            location = locationUtil.loadLocation();
            location.add(0.0D, 1.5D, 0.0D);
        } else {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie Spawn-Location wurde noch nicht gesetzt!");
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Location tempLoc0 = location.clone();
                Location tempLoc1 = location.clone();
                if (pos0 >= 6.4D)
                    pos0 = 0.0D;
                if (pos1 >= 6.4D)
                    pos1 = 0.0D;
                tempLoc0.add(Math.sin(pos0), 0, Math.cos(pos0));
                tempLoc1.add(Math.sin(pos1), 0, Math.cos(pos1));
                Particle particle0 = new Particle(EnumParticle.FIREWORKS_SPARK, tempLoc0, true, 0, 0, 0, 0, 1, 0);
                Particle particle1 = new Particle(EnumParticle.FIREWORKS_SPARK, tempLoc1, true, 0, 0, 0, 0, 1, 0);
                particle0.sendAll();
                particle1.sendAll();
                pos0 += 0.2;
                pos1 += 0.2;
            }
        }.runTaskTimerAsynchronously(this, 40, 2);

        Random random = new Random();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if(all.getLocation().distance(location) < 50) {
                        all.sendBlockChange(location.clone().add(1, -2, 0), Material.STAINED_CLAY, (byte) random.nextInt(15));
                        all.sendBlockChange(location.clone().add(-1, -2, 0), Material.STAINED_CLAY, (byte) random.nextInt(15));
                        all.sendBlockChange(location.clone().add(0, -2, 1), Material.STAINED_CLAY, (byte) random.nextInt(15));
                        all.sendBlockChange(location.clone().add(0, -2, -1), Material.STAINED_CLAY, (byte) random.nextInt(15));
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 40, 20);

    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "§cDas Plugin wird gestoppt....");

        MySQLCosmetics.disconnect();

        Bukkit.getConsoleSender().sendMessage(PREFIX + "§cGestoppt!");
    }

    public LobbyClient getClient() {
        return client;
    }

    public ServerGUI getGui() {
        return gui;
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

    public Crates getCrates() {
        return crates;
    }

    public Navigator getNavigator() {
        return navigator;
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