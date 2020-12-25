package de.groodian.lobby.util;

import de.groodian.hyperiorcore.main.HyperiorCore;
import de.groodian.hyperiorcore.util.MySQL;
import de.groodian.hyperiorcore.util.MySQLConnection;
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
                MySQLConnection connection = coreMySQL.getMySQLConnection();
                PreparedStatement ps = connection.getConnection().prepareStatement("UPDATE core SET " + rank + " = ?, playername = ? WHERE UUID = ?");
                ps.setString(1, date);
                ps.setString(2, player.getName());
                ps.setString(3, player.getUniqueId().toString().replaceAll("-", ""));
                ps.executeUpdate();
                ps.close();
                connection.finish();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                MySQLConnection connection = coreMySQL.getMySQLConnection();
                PreparedStatement ps = connection.getConnection().prepareStatement("INSERT INTO core (UUID,playername," + rank + ") VALUES (?,?,?)");
                ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
                ps.setString(2, player.getName());
                ps.setString(3, date);
                ps.executeUpdate();
                ps.close();
                connection.finish();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static String get(Player player, String rank) {
        try {
            MySQLConnection connection = coreMySQL.getMySQLConnection();
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT " + rank + " FROM core WHERE UUID = ?");
            ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
            ResultSet rs = ps.executeQuery();
            String returnString = null;
            if (rs.next()) {
                returnString = rs.getString(rank);
            }
            ps.close();
            connection.finish();
            return returnString;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isUserExists(Player player) {
        try {
            MySQLConnection connection = coreMySQL.getMySQLConnection();
            PreparedStatement ps = connection.getConnection().prepareStatement("SELECT playername FROM core WHERE UUID = ?");
            ps.setString(1, player.getUniqueId().toString().replaceAll("-", ""));
            ResultSet rs = ps.executeQuery();
            boolean userExists = rs.next();
            ps.close();
            connection.finish();
            return userExists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
