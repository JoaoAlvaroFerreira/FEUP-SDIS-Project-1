import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MessageParser implements Runnable {

	Peer peer;
	DatagramPacket packet;
	
	public MessageParser(DatagramPacket packet, Peer peer) {
		this.peer = peer;
		this.packet = packet;
		
	}

	public void run() {
		
		String str = new String(packet.getData());
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
		String CRLF = "" + (char) 0xD  + (char) 0xA;
		String rest = msg[6];
		String bodyString = rest.replace(CRLF+CRLF,"");
		String newBodyString = bodyString.replaceAll(null, "");
		byte[] body = newBodyString.getBytes(StandardCharsets.UTF_8);
		
		
		
		switch(messageType) {
		
		case "PUTCHUNK":
			Chunk chunk = new Chunk(fileID, chunkNo, body);
			if(peer.getStorage().addChunk(chunk)) {

			Message stored = new Message("STORED",peer.getVersion(),peer.getPeerID(),fileID, chunkNo, 0, null);
			byte[] reply = stored.sendable();
			System.out.println(stored.messageToString()+"SAVED CHUNK SIZE: " + body.length);
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
			Random randomS = null;
			try {
				TimeUnit.MILLISECONDS.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			peer.getStorage().getBackUps().add(new BackUpInfo(fileID, chunkNo, peer.getPeerID()));
		
		
		break;
		
		case "GETCHUNK": //CHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF><Body>
			Random randomG = null;
		try {
			TimeUnit.MILLISECONDS.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Chunk chunkSend: peer.getStorage().getChunks()) {
			if(chunkSend.getFileID().equals(fileID) && chunkSend.getChunkN()==chunkNo) {
				Message sendChunk = new Message("CHUNK",peer.getVersion(),peer.getPeerID(),fileID, chunkNo, 0, chunkSend.getContent());
				byte[] reply = sendChunk.sendable();
				System.out.println(sendChunk.messageToString()+"SENT CHUNK SIZE: " + chunkSend.getContent().length);
				try {
					peer.getMDR().sendMessage(reply);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
			
		
		break;
		
		case "CHUNK": //GETCHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
			Chunk gotChunk = new Chunk(fileID, chunkNo, body);
			peer.getStorage().addChunk(gotChunk);
			if(gotChunk.getContent().length == 0)
				peer.lastChunk();
			break;
		}
	}

	}

	

