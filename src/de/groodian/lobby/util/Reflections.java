package de.groodian.lobby.util;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.Packet;

public class Reflections {

	public void setValue(Object obj, String name, Object value) {
		Field field;
		try {
			field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public Object getValue(Object obj, String name) {
		Field field;
		try {
			field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendPacket(Packet<?> packet, Player player) {
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void sendPacket(Packet<?> packet) {
		for(Player player:Bukkit.getOnlinePlayers()) {
			sendPacket(packet, player);
		}
	}

}
