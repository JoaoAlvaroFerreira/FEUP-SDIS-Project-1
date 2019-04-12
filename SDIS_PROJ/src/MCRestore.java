import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

public class MCRestore extends Thread {

	private InetAddress addr;
	private String ip;
	private int port;
	MulticastSocket socket;
	
	private Hashtable<String,HashSet<Integer>> logs;

	public MCRestore(String ip, int port) throws IOException {	
		this.ip=ip;
		this.port=port;
		addr= InetAddress.getByName(ip);
		socket = new MulticastSocket(port);

	}

	public void connect(){
		try {
			socket.joinGroup(addr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void disconnect(){
		try {
			socket.leaveGroup(addr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(byte[] data) throws IOException {
		DatagramPacket message = new DatagramPacket(data, data.length, addr, port);
		socket.send(message);
	}

	@Override
	public void run() {
		byte[] buffer = new byte[Chunk.chunkMaxSize + 1024];
		while (true) {
			try {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				new Thread(new MessagesManager(packet));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// close socket
		//socket.close();												ERRO NO SOCKET CLOSE AINDA POR RESOLVER

	}
	
	public void startSave(String chunkID) {
		logs.put(chunkID, new HashSet<Integer>());
		
	}

	public int getSaves(String chunkID) {
		if(logs.get(chunkID) == null) return 0;
		return logs.get(chunkID).size();
	}

	public void stopSave(String chunkID) {
		logs.remove(chunkID);
		
	}

	public boolean isSaving(String file_id) {
		// TODO Auto-generated method stub
		return false;
	}

	public void save(String file_id, Chunk chunk) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Chunk> getSave(String file_id) {
		// TODO Auto-generated method stub
		return null;
	}
	


}
