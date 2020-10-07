package de.groodian.lobby.util;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.MySQL;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyBonus {

    private static final MySQL coreMySQL = HyperiorCore.getMySQLManager().getCoreMySQL();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");

    /**
     * This method should be executed async
     */
    public static boolean canCollect(Player player, String rank) {
        String stringDate = get(player, rank);
        if (stringDate == null) {
            return true;
        } else {
            LocalDate date = LocalDate.parse(stringDate, dateFormatter);
            return !date.equals(LocalDate.now());
        }
    }

    /**
     * This method should be executed async
     */
    public static boolean collect(Player player, String rank) {
        if (canCollect(player, rank)) {
            set(player, rank, dateFormatter.format(LocalDate.now()));
            return true;
        }

        return false;
    }

    private static void set(Player player, String rank, String date) {
        if (isUserExists(player)) {
            try {
                PreparedStatement ps = coreMySQL.getConnection().prepareStatement("UPDATE core SET " + rank + " = ?, playername = ? WHERE UUID = ?");
                ps.setString(1, date);
                ps.setString(2, player.getName());
                ps.setString(3, player.getUniqueId().toString().replaceAll("-", ""));
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                PreparedStatement ps = coreMySQL.getConnection().prepareStatement("INSERT INTO core (UUID,playername," + rank + ") VALUES (?,?,?)");
                ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
                ps.setString(2, player.getName());
                ps.setString(3, date);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static String get(Player player, String rank) {
        try {
            PreparedStatement ps = coreMySQL.getConnection().prepareStatement("SELECT " + rank + " FROM core WHERE UUID = ?");
            ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString(rank);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isUserExists(Player player) {
        try {
            PreparedStatement ps = coreMySQL.getConnection().prepareStatement("SELECT playername FROM core WHERE UUID = ?");
            ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
