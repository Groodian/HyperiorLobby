package de.groodian.lobby.dailybonus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import de.groodian.lobby.cosmetics.MySQLCosmetics;

public class DailyBonusMySQL {

	private static boolean isUserExists(Player player) {
		try {
			PreparedStatement ps = MySQLCosmetics.getConnection().prepareStatement("SELECT playername FROM cosmetics WHERE UUID = ?");
			ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void set(Player player, String rank, String date) {
		if (isUserExists(player)) {
			try {
				PreparedStatement ps = MySQLCosmetics.getConnection().prepareStatement("UPDATE cosmetics SET " + rank + " = ?, playername = ? WHERE UUID = ?");
				ps.setString(1, date);
				ps.setString(2, player.getName());
				ps.setString(3, player.getUniqueId().toString().replaceAll("-", ""));
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				PreparedStatement ps = MySQLCosmetics.getConnection().prepareStatement("INSERT INTO cosmetics (UUID,playername," + rank + ") VALUES (?,?,?)");
				ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
				ps.setString(2, player.getName());
				ps.setString(3, date);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String get(String uuid, String rank) {
		try {
			PreparedStatement ps = MySQLCosmetics.getConnection().prepareStatement("SELECT " + rank + " FROM cosmetics WHERE UUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString(rank);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
