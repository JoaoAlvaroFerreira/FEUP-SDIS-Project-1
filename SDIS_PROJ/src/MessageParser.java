import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public class MessageParser implements Runnable {

	Peer peer;
	DatagramPacket packet;
	
	public MessageParser(DatagramPacket packet, Peer peer) {
		this.peer = peer;
		this.packet = packet;
		
	}

	public void run() {
		
		String str = new String(packet.getData(), StandardCharsets.UTF_8);
		String[] header = str.split(" ");
		
		switch(header[0]) {
		
		case "STORED":
			peer.increaseStoredCount();
			
		
		}

	}

	
}
