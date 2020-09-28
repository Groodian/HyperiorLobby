package de.groodian.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import de.groodian.lobby.main.Main;

public class Client {

	private Main plugin;
	private InetSocketAddress address;
	private Socket login;
	private ObjectOutputStream oos;
	private Thread listeningThread;
	private Thread mainThread;
	private boolean listening;
	private Datapackage loginPack;
	public ArrayList<Datapackage> servers;

	public Client(String hostname, int port, Datapackage loginPack, Main plugin) {
		this.loginPack = loginPack;
		this.plugin = plugin;
		mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				address = new InetSocketAddress(hostname, port);
				servers = new ArrayList<Datapackage>();
				listening = true;
				login(loginPack);
			}
		});
		mainThread.start();
	}

	public void sendMessage(Datapackage pack) {
		try {

			// System.out.println("[ClientSendMessage] Sending message...");
			oos.writeObject(pack);
			oos.flush();
			// System.out.println("[ClientSendMessage] Message sended.");

		} catch (SocketException e) {
			System.err.println("[ClientSendMessage] Message could not be sent, because the server is unreachable.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void login(Datapackage pack) {
		try {
			System.out.println("[ClientLogin] Connecting...");
			login = new Socket();
			login.connect(address, 5000);
			System.out.println("[ClientLogin] Connected to: " + login.getRemoteSocketAddress());

			System.out.println("[ClientLogin] Logging in...");
			oos = new ObjectOutputStream(new BufferedOutputStream(login.getOutputStream()));
			oos.writeObject(pack);
			oos.flush();
			System.out.println("[ClientLogin] Logged in.");
			startListening();
		} catch (ConnectException e) {
			System.err.println("[ClientLogin] The server is unreachable. Try again in 5 seconds.");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			repairConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startListening() {
		listeningThread = new Thread(new Runnable() {

			@Override
			public void run() {

				while (listening) {
					try {
						// System.out.println("[ClientListener] Waiting for message...");
						ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(login.getInputStream()));
						Object raw = ois.readObject();
						if (raw instanceof Datapackage) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									Datapackage pack = (Datapackage) raw;
									if (pack.get(0).equals("ONLINE_MARIOPARTY_SERVER")) {
										servers.clear();
										for (int i = 1; i < pack.size(); i++) {
											Datapackage serverInfo = (Datapackage) pack.get(i);
											servers.add(serverInfo);
										}
										// System.out.println(pack);
										plugin.getGui().update();
										plugin.getMpJoin().updateHologram();
									} else {
										System.err.println("[ClientListener] Unknowen header: " + pack);
									}
								}
							}).start();
						}
					} catch (SocketException | EOFException e) {
						System.err.println("[Client] Connection lost");
						repairConnection();
						break;
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

			}
		});
		listeningThread.start();
	}

	private void repairConnection() {
		if (login != null) {
			try {
				login.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			login = null;
			login(loginPack);
		}
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		System.out.println("[Client] Closing connection...");
		mainThread.stop();
		if (listeningThread != null) {
			listeningThread.stop();
		}
		try {
			login.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		listening = false;
	}

}