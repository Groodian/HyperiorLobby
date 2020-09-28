package de.groodian.lobby.cosmetics;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

public class Cosmetics {

	private static boolean isUserExists(Player player) {
		try {
			PreparedStatement ps = MySQLCosmetics.getConnection().prepareStatement("SELECT cosmetics FROM cosmetics WHERE UUID = ?");
			ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void add(Player player, String cosmetic) {
		String newCosmetics;
		if (get(player.getUniqueId().toString().replaceAll("-", "")) == null) {
			newCosmetics = cosmetic;
		} else {
			newCosmetics = get(player.getUniqueId().toString().replaceAll("-", "")) + "," + cosmetic;
		}
		if (isUserExists(player)) {
			try {
				PreparedStatement ps = MySQLCosmetics.getConnection().prepareStatement("UPDATE cosmetics SET cosmetics = ?, playername = ? WHERE UUID = ?");
				ps.setString(1, newCosmetics);
				ps.setString(2, player.getName());
				ps.setString(3, player.getUniqueId().toString().replaceAll("-", ""));
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				PreparedStatement ps = MySQLCosmetics.getConnection().prepareStatement("INSERT INTO cosmetics (UUID,playername,cosmetics) VALUES (?,?,?)");
				ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
				ps.setString(2, player.getName());
				ps.setString(3, cosmetic);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static String get(String uuid) {
		try {
			PreparedStatement ps = MySQLCosmetics.getConnection().prepareStatement("SELECT cosmetics FROM cosmetics WHERE UUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString("cosmetics");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean has(Player player, String cosmetic) {
		if (get(player.getUniqueId().toString().replaceAll("-", "")) != null) {
			if (get(player.getUniqueId().toString().replaceAll("-", "")).contains(cosmetic)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
