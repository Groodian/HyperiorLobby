package de.groodian.lobby.boards;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;

public class ScoreboardAPI {
	
	// LÖSCHE ES NICHT IST DIR EVENTUELL NOCH HILFREICH

	private Scoreboard scoreboard;
	private ScoreboardObjective obj;
	private Player p;

	private PacketPlayOutScoreboardObjective createPacket;
	private PacketPlayOutScoreboardObjective removePacket;
	private PacketPlayOutScoreboardDisplayObjective displayPacket;

	public void send(Player p, String name, String displayName) {
		this.p = p;
		scoreboard = new Scoreboard();
		obj = scoreboard.registerObjective(name, IScoreboardCriteria.b);
		obj.setDisplayName(displayName);

		createPacket = new PacketPlayOutScoreboardObjective(obj, 0);
		removePacket = new PacketPlayOutScoreboardObjective(obj, 1);
		displayPacket = new PacketPlayOutScoreboardDisplayObjective(1, obj);

		sendPacket(p, createPacket);
		sendPacket(p, displayPacket);

	}

	private void sendPacket(Player p, Packet<?> packet) {
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	public void add(String msg, int score) {
		ScoreboardScore s = new ScoreboardScore(scoreboard, obj, msg);
		s.setScore(score);
		PacketPlayOutScoreboardScore ps = new PacketPlayOutScoreboardScore(s);
		sendPacket(p, ps);
	}

	public void unregisterObjective() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(all, removePacket);
		}
		scoreboard.unregisterObjective(obj);
	}

	public void unregisterObjectiveFor(Player player) {
		if (scoreboard != null) {
			sendPacket(player, removePacket);
			scoreboard.unregisterObjective(obj);
		}
	}

}
