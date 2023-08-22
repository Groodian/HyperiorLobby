package de.groodian.hyperiorlobby.main;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.groodian.hyperiorcore.command.HCommandManagerPaper;
import de.groodian.hyperiorcore.gui.GUIManager;
import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorlobby.boards.LobbyScoreboard;
import de.groodian.hyperiorlobby.commands.BuildCommand;
import de.groodian.hyperiorlobby.commands.SetupCommand;
import de.groodian.hyperiorlobby.gui.DailyBonusGUI;
import de.groodian.hyperiorlobby.gui.LobbyGUI;
import de.groodian.hyperiorlobby.gui.MinecraftPartyGUI;
import de.groodian.hyperiorlobby.gui.NavigatorGUI;
import de.groodian.hyperiorlobby.listener.CrateListener;
import de.groodian.hyperiorlobby.listener.MainListener;
import de.groodian.hyperiorlobby.network.LobbyClient;
import de.groodian.hyperiorlobby.spawnable.DailyBonus;
import de.groodian.hyperiorlobby.spawnable.MinecraftPartyJoin;
import de.groodian.network.DataPackage;
import java.util.ArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements PluginMessageListener {

    public static final Component PREFIX = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text("Lobby", NamedTextColor.YELLOW))
            .append(Component.text("] ", NamedTextColor.GRAY));

    public static final String PREFIX_LEGACY = "§7[§eLobby§7] §r";
    public static final int MAX_PLAYERS = 50;

    private int groupNumber;
    private MinecraftPartyJoin mpJoin;
    private DailyBonus dailyBonus;
    private LobbyScoreboard lobbyScoreboard;
    private ArrayList<Player> build;
    private GUIManager minecraftPartyGUIManager;
    private GUIManager dailyBonusGUIManager;
    private GUIManager lobbyGUIManager;
    private GUIManager navigatorGUIManager;
    private LobbyClient client;
    private int onlineCount;

    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX_LEGACY + "§aDas Plugin wird geladen...");

        groupNumber = Integer.parseInt(PlainTextComponentSerializer.plainText().serialize(Bukkit.motd()));
        lobbyScoreboard = new LobbyScoreboard(this);
        build = new ArrayList<>();

        minecraftPartyGUIManager = new GUIManager(MinecraftPartyGUI.class, this);
        dailyBonusGUIManager = new GUIManager(DailyBonusGUI.class, this);
        lobbyGUIManager = new GUIManager(LobbyGUI.class, this);
        navigatorGUIManager = new GUIManager(NavigatorGUI.class, this);

        HCommandManagerPaper hCommandManagerPaper = HyperiorCore.getHCommandManagerPaper();
        hCommandManagerPaper.registerCommand(this, new SetupCommand(this));
        hCommandManagerPaper.registerCommand(this, new BuildCommand(this));

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(minecraftPartyGUIManager, this);
        pluginManager.registerEvents(dailyBonusGUIManager, this);
        pluginManager.registerEvents(lobbyGUIManager, this);
        pluginManager.registerEvents(navigatorGUIManager, this);
        pluginManager.registerEvents(new MainListener(this), this);
        pluginManager.registerEvents(new CrateListener(this), this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        updateScoreboard();

        client = new LobbyClient(this, "localhost", 4444, new DataPackage("LOGIN", "Lobby", groupNumber));

        Bukkit.getConsoleSender().sendMessage(PREFIX_LEGACY + "§aGeladen!");
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
        Bukkit.getConsoleSender().sendMessage(PREFIX_LEGACY + "§cDas Plugin wird gestoppt...");

        client.stop();

        Bukkit.getConsoleSender().sendMessage(PREFIX_LEGACY + "§cGestoppt!");
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public LobbyClient getClient() {
        return client;
    }

    public MinecraftPartyJoin getMpJoin() {
        return mpJoin;
    }

    public void setMpJoin(MinecraftPartyJoin mpJoin) {
        this.mpJoin = mpJoin;
    }

    public DailyBonus getDailyBonus() {
        return dailyBonus;
    }

    public void setDailyBonus(DailyBonus dailyBonus) {
        this.dailyBonus = dailyBonus;
    }

    public LobbyScoreboard getLobbyScoreboard() {
        return lobbyScoreboard;
    }

    public ArrayList<Player> getBuild() {
        return build;
    }

    public GUIManager getMinecraftPartyGUIManager() {
        return minecraftPartyGUIManager;
    }

    public GUIManager getDailyBonusGUIManager() {
        return dailyBonusGUIManager;
    }

    public GUIManager getLobbyGUIManager() {
        return lobbyGUIManager;
    }

    public GUIManager getNavigatorGUIManager() {
        return navigatorGUIManager;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public Plugin getPlugin() {
        return getPlugin(Main.class);
    }

}