package de.groodian.lobby.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Particle {

	EnumParticle particleType;
	boolean longDistance;
	Location location;
	float red;
	float green;
	float blue;
	float brightness;
	int amount;
	int data;

	public Particle(EnumParticle particleType, Location location, boolean longDistance, float red, float green,
			float blue, float brightness, int amount, int data) {
		this.particleType = particleType;
		this.longDistance = longDistance;
		this.location = location;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.brightness = brightness;
		this.amount = amount;
		this.data = data;
	}

	public void sendAll() {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particleType, this.longDistance,
				(float) this.location.getX(), (float) this.location.getY(), (float) this.location.getZ(), this.red,
				this.green, this.blue, this.brightness, this.amount, this.data);

		for (Player all : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public void sendPlayer(Player player) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particleType, this.longDistance,
				(float) this.location.getX(), (float) this.location.getY(), (float) this.location.getZ(), this.red,
				this.green, this.blue, this.brightness, this.amount, this.data);

		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

}
