import java.io.IOException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageParser implements Runnable {

	Peer peer;
	DatagramPacket packet;
	
	public MessageParser(DatagramPacket packet, Peer peer) {
		this.peer = peer;
		this.packet = packet;
		
	}

	public void run() {
		
		String str = new String(packet.getData(), StandardCharsets.UTF_8);
		String[] msg = str.split(" ");
		String messageType = msg[0];
		String version = msg[1];
		int senderID = Integer.parseInt(msg[2]);
		
		if(peer.getPeerID() == senderID) {
			return;
		}
		
		String fileID = msg[3];
		int chunkNo = Integer.parseInt(msg[4]);
		int replicationDeg = Integer.parseInt(msg[5]);
		String CRLF = "\r\n";
		int bodyI = msg.length+2*CRLF.length();
		
		byte[] body = Arrays.copyOfRange(packet.getData(),bodyI ,
				packet.getLength());

		
		switch(messageType) {
		
		case "PUTCHUNK":
			Chunk chunk = new Chunk(fileID, chunkNo, body);
			if(peer.getStorage().addChunk(chunk)) {

			byte[] reply = new Message("STORED",peer.getVersion(),peer.getPeerID(),fileID, chunkNo, 0, null).sendable();
			try {
				peer.getMC().sendMessage(reply);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			try {
				peer.saveChunks();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			break;
		case "STORED":
			peer.getStorage().getBackUps().add(new BackUpInfo(fileID, chunkNo, peer.getPeerID()));
		
		}

	}

	
}
