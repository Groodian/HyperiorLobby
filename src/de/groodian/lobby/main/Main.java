package de.groodian.lobby.main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.groodian.lobby.boards.LobbyScoreboard;
import de.groodian.lobby.boards.ScoreboardAPI;
import de.groodian.lobby.boards.Tablist;
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
import de.groodian.lobby.util.ConfigLocationUtil;
import de.groodian.lobby.util.NPCManager;
import de.groodian.lobby.util.Particle;
import de.groodian.lobby.util.PlaySound;
import de.groodian.network.Client;
import de.groodian.network.Datapackage;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Main extends JavaPlugin implements PluginMessageListener {

	public static final String PREFIX = "§eLobby §7>> §r";

	private Client client;
	private PlaySound playSound;
	private ServerGUI gui;
	private NPCManager npcManager;
	private MinecraftPartyJoin mpJoin;
	private DailyBonus dailyBonus;
	private DailyBonusGUI dailyBonusGUI;
	private Crates crates;
	private Navigator navigator;
	private ScoreboardAPI scoreborad;
	private LobbyScoreboard lobbyScoreboard;
	private Tablist tablist;
	private ArrayList<Player> build;
	private int onlineCount;
	private Main plugin;

	public void onEnable() {
		plugin = this;
		Bukkit.getConsoleSender().sendMessage(PREFIX + "§aDas Plugin wird geladen....");

		client = new Client("localhost", 4444, new Datapackage("LOGIN", "Lobby-1", "Lobby", 1), this);

		MySQLCosmetics.connect();

		try {
			PreparedStatement ps05 = MySQLCosmetics.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS cosmetics (UUID VARCHAR(100), playername VARCHAR(100), cosmetics VARCHAR(9999), dailybonusspieler VARCHAR(100), dailybonusgruender VARCHAR(100), trail VARCHAR(100), helmet VARCHAR(100), chestplate VARCHAR(100), pants VARCHAR(100), shoes VARCHAR(100), gadget VARCHAR(100))");
			ps05.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		playSound = new PlaySound();
		gui = new ServerGUI(this);
		mpJoin = new MinecraftPartyJoin(this);
		dailyBonus = new DailyBonus(this);
		dailyBonusGUI = new DailyBonusGUI(this);
		crates = new Crates(this);
		navigator = new Navigator(this);
		scoreborad = new ScoreboardAPI();
		lobbyScoreboard = new LobbyScoreboard(this);
		tablist = new Tablist();
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

		day();
		spawnParticleAndClay();
		killAllMobs();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				// Get BungeeCord Online Players
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("PlayerCount");
				out.writeUTF("ALL");
				if (Bukkit.getOnlinePlayers().size() > 0) {
					Player player = Bukkit.getOnlinePlayers().iterator().next();
					player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
				}

				// Scoreboard and NPC update
				for (Player player : Bukkit.getOnlinePlayers()) {
					lobbyScoreboard.updateLobbyScoreboard(player);

					if (mpJoin.isWorking() && dailyBonus.isWorking() && crates.isWorking()) {
						mpJoin.getNpc().updateFor(player);
						dailyBonus.getNpc().updateFor(player);
					}
				}
			}
		}, 20, 20);

		Bukkit.getConsoleSender().sendMessage(PREFIX + "§aGeladen!");
	}

	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] message) {
		if (!channel.equals("BungeeCord"))
			return;
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("PlayerCount")) {
			in.readUTF();
			onlineCount = in.readInt();
		}
	}

	private void day() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (World w : Bukkit.getWorlds()) {
					w.setTime(2000L);
					w.setWeatherDuration(1200);
					w.setThunderDuration(1200);
					w.setStorm(false);
					w.setThundering(false);
				}
			}
		}, 0L, 1000L);
	}

	private Double pos0 = 0.0D;
	private Double pos1 = 3.2D;

	private void spawnParticleAndClay() {
		ConfigLocationUtil locationUtil = new ConfigLocationUtil(this, "Spawn");
		Location location;

		if (locationUtil.loadLocation() != null) {
			location = locationUtil.loadLocation();
			location.add(0.0D, 1.5D, 0.0D);
		} else {
			Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie Spawn-Location wurde noch nicht gesetzt!");
			return;
		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
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
		}, 40, 2);

		Random random = new Random();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (Player all : Bukkit.getOnlinePlayers()) {
					all.sendBlockChange(location.clone().add(1, -2, 0), Material.STAINED_CLAY, (byte) random.nextInt(15));
					all.sendBlockChange(location.clone().add(-1, -2, 0), Material.STAINED_CLAY, (byte) random.nextInt(15));
					all.sendBlockChange(location.clone().add(0, -2, 1), Material.STAINED_CLAY, (byte) random.nextInt(15));
					all.sendBlockChange(location.clone().add(0, -2, -1), Material.STAINED_CLAY, (byte) random.nextInt(15));
				}
			}
		}, 40, 20);
	}

	private void killAllMobs() {
		for (World w : Bukkit.getWorlds()) {
			List<Entity> entitys = w.getEntities();
			for (Entity entity : entitys) {
				entity.remove();
			}
		}
	}

	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(PREFIX + "§cDas Plugin wird gestoppt....");

		MySQLCosmetics.disconnect();

		Bukkit.getConsoleSender().sendMessage(PREFIX + "§cGestoppt!");
	}

	public Client getClient() {
		return client;
	}

	public PlaySound getPlaySound() {
		return playSound;
	}

	public ServerGUI getGui() {
		return gui;
	}

	public NPCManager getNpcManager() {
		return npcManager;
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

	public ScoreboardAPI getScoreborad() {
		return scoreborad;
	}

	public LobbyScoreboard getLobbyScoreboard() {
		return lobbyScoreboard;
	}

	public Tablist getTablist() {
		return tablist;
	}

	public ArrayList<Player> getBuild() {
		return build;
	}

	public int getOnlineCount() {
		return onlineCount;
	}

}