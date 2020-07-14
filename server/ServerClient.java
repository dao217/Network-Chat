package server;

import java.net.InetAddress;

public class ServerClient {
	
	public String name;
	public InetAddress address;
	public int port;
	public int attempt;
	
	private int ID;

	public ServerClient (String name, InetAddress address, int port, int ID) {
		this.ID = ID;
		this.address = address;
		this.port = port;
		this.name = name;
	}
	
	public int getID() {
		return this.ID;
	}
	
}
