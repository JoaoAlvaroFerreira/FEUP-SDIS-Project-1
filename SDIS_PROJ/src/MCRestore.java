import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MCRestore extends Thread {

	private InetAddress addr;
	private String ip;
	private int port;
	MulticastSocket socket;

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
				new Thread(new MsgHandler(packet)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// close socket
		socket.close();

	}


}
