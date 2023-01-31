package net.minecraft.src;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.MinecraftServer;

public class NetworkListenThread {
	public static Logger logger = Logger.getLogger("Minecraft");
	private ServerSocket serverSocket;
	private Thread listenThread;
	public volatile boolean isListening = false;
	private int connectionNumber = 0;
	private ArrayList pendingConnections = new ArrayList();
	private ArrayList playerList = new ArrayList();
	public MinecraftServer mcServer;

	public NetworkListenThread(MinecraftServer minecraftServer, InetAddress address, int port) throws IOException {
		this.mcServer = minecraftServer;
		this.serverSocket = new ServerSocket(port, 0, address);
		this.serverSocket.setPerformancePreferences(0, 2, 1);
		this.isListening = true;
		this.listenThread = new NetworkAcceptThread(this, "Listen thread", minecraftServer);
		this.listenThread.start();
	}

	public void addPlayer(NetServerHandler netServerHandler) {
		this.playerList.add(netServerHandler);
	}

	private void addPendingConnection(NetLoginHandler netLoginHandler) {
		if(netLoginHandler == null) {
			throw new IllegalArgumentException("Got null pendingconnection!");
		} else {
			this.pendingConnections.add(netLoginHandler);
		}
	}

	public void handleNetworkListenThread() {
		int i1;
		for(i1 = 0; i1 < this.pendingConnections.size(); ++i1) {
			NetLoginHandler netLoginHandler2 = (NetLoginHandler)this.pendingConnections.get(i1);

			try {
				netLoginHandler2.tryLogin();
			} catch (Exception exception5) {
				netLoginHandler2.kickUser("Internal server error");
				logger.log(Level.WARNING, "Failed to handle packet: " + exception5, exception5);
			}

			if(netLoginHandler2.finishedProcessing) {
				this.pendingConnections.remove(i1--);
			}
		}

		for(i1 = 0; i1 < this.playerList.size(); ++i1) {
			NetServerHandler netServerHandler6 = (NetServerHandler)this.playerList.get(i1);

			try {
				netServerHandler6.handlePackets();
			} catch (Exception exception4) {
				netServerHandler6.kickPlayer("Internal server error");
				logger.log(Level.WARNING, "Failed to handle packet: " + exception4, exception4);
			}

			if(netServerHandler6.connectionClosed) {
				this.playerList.remove(i1--);
			}
		}

	}

	static ServerSocket getServerSocket(NetworkListenThread listenThread) {
		return listenThread.serverSocket;
	}

	static int incrementConnections(NetworkListenThread listenThread) {
		return listenThread.connectionNumber++;
	}

	static void addPendingConnection(NetworkListenThread listenThread, NetLoginHandler netLoginHandler) {
		listenThread.addPendingConnection(netLoginHandler);
	}
}
