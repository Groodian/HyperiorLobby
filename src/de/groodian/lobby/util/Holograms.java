package de.groodian.lobby.util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

public class Holograms {

	private String[] text;
	private Location location;
	private ArrayList<EntityArmorStand> entitys = new ArrayList<EntityArmorStand>();
	private int count;

	public Holograms(Location location, String... text) {
		this.text = text;
		this.location = location;
		create();
	}

	public void create() {
		for (String text : text) {
			EntityArmorStand entity = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
			entity.setCustomName(text);
			entity.setCustomNameVisible(true);
			entity.setInvisible(true);
			entity.setGravity(false);
			location.subtract(0, 0.3, 0);
			entitys.add(entity);
			count++;
		}
		for (int i = 0; i < count; i++) {
			location.add(0, 0.3, 0);
		}
		count = 0;
	}

	public void show(Player player) {
		for (EntityArmorStand entity : entitys) {
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public void hide(Player player) {
		for (EntityArmorStand entity : entitys) {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getId());
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public void showAll() {
		for (EntityArmorStand entity : entitys) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(entity);
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	public void hideAll() {
		for (EntityArmorStand entity : entitys) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entity.getId());
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}

	}

}
