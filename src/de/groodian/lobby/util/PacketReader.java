package de.groodian.lobby.util;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.groodian.lobby.main.Main;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.Packet;

public class PacketReader {

	private Player player;
	private Channel channel;
	private Main plugin;

	public PacketReader(Player player, Main plugin) {
		this.player = player;
		this.plugin = plugin;
	}

	public void inject() {
		CraftPlayer cPlayer = (CraftPlayer) this.player;
		channel = cPlayer.getHandle().playerConnection.networkManager.channel;
		channel.pipeline().addAfter("decoder", "HyperiorPacketInjector", new MessageToMessageDecoder<Packet<?>>() {

			@Override
			protected void decode(ChannelHandlerContext arg0, Packet<?> arg1, List<Object> arg2) throws Exception {
				arg2.add(arg1);
				readPacket(arg1);
			}
		});
	}

	public void uninject() {
		if (channel.pipeline().get("HyperiorPacketInjector") != null) {
			channel.pipeline().remove("HyperiorPacketInjector");
		}
	}

	public void readPacket(Packet<?> packet) {
		if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
			int id = (int) getValue(packet, "a");
			if (plugin.getMpJoin().getEntityID() == id) {
				if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {
					plugin.getGui().openMainGUI(player);
				}
			}
			if (plugin.getDailyBonus().getEntityID() == id) {
				if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")) {
					plugin.getDailyBonusGUI().openGUI(player);
				}
			}
		}
	}

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

}
