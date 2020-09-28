package de.groodian.lobby.cosmetics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import de.groodian.lobby.main.Main;

public class MySQLCosmetics {
	
	private static final String PREFIX = Main.PREFIX + "ßb[MySQLCosmetics] ";
	
	private static String host = "localhost";
	private static String port = "3306";
	private static String database = "cosmetics";
	private static String username = "admin";
	private static String password = "test321";
	private static Connection connection;
	
	public static void connect() {
		if(!isConnected()) {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
				Bukkit.getConsoleSender().sendMessage(PREFIX + "ßaDie Verbindung konnte erfolgreich aufgebaut werden.");
			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage(PREFIX + "ß4Es ist ein Fehler beim Verbinden mit der Datenbank aufgetreten:");
				e.printStackTrace();
			}
		}
	}
	
	public static void disconnect() {
		if(isConnected()) {
			try {
				connection.close();
				Bukkit.getConsoleSender().sendMessage(PREFIX + "ßaDie Verbindung konnte erfolgreich geschlossen werden.");
			} catch (SQLException e) {
				Bukkit.getConsoleSender().sendMessage(PREFIX + "ß4Es ist ein Fehler beim Schlieﬂen der Verbindung aufgetreten:");
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isConnected() {
		return (connection == null ? false : true);
	}
	
	public static Connection getConnection() {
		try {
			if (!connection.isValid(2)) {
				connection.close();
				connection = null;
				connect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

}
