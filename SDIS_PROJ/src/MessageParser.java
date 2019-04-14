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
		
		String str = new String(packet.getData(), StandardCharsets.UTF_8);
		String rest = null, fileID = null, messageType = null, version = null;
		int i = 0, next = 0, chunkNo = 0, replicationDeg = 0, senderID=0;

	     if(str.contains(" ")){
	    	i= str.indexOf(" ");
	      messageType= str.substring(0,i);
	      next = str.indexOf(" ", i+1);
	      version = str.substring(i,next).replace(" ","");
	      i = next;
	      next = str.indexOf(" ", i+1);
	      senderID = Integer.parseInt(str.substring(i,next).replace(" ",""));
	      i = next;
	      next = str.indexOf(" ", i+1);
	      fileID = str.substring(i,next).replace(" ",""); 
	      i = next;
	      next = str.indexOf(" ",i+1);
	      chunkNo = Integer.parseInt(str.substring(i,next).replace(" ",""));
	      i = next;
	      next = str.indexOf(" ",i+1);
	      replicationDeg = Integer.parseInt(str.substring(i,next).replace(" ",""));
	      rest = str.substring(next);
	     }
	    
	    
		if(peer.getPeerID() == senderID) {
			return;
		}
		
	
		String CRLF = "" + (char) 0xD  + (char) 0xA;
		
		String bodyString = rest.replaceFirst(CRLF+CRLF,"");
		
		String bodyString2 = bodyString.replaceAll("\0", "");

		byte[] body = bodyString2.getBytes( StandardCharsets.UTF_8);
		
		
		switch(messageType) {
		
		case "PUTCHUNK":
			
			Chunk chunk = new Chunk(fileID, chunkNo, body);
			if(peer.getStorage().addChunk(chunk)) {

			Message stored = new Message("STORED",peer.getVersion(),peer.getPeerID(),fileID, chunkNo, 0, null);
			byte[] reply = stored.sendable();
			
			try {
				
				Random randomS = null;
				try {
					TimeUnit.MILLISECONDS.sleep(400);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				peer.getMC().sendMessage(reply);
				System.out.println(stored.messageToStringPrintable());
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
		
		
		break;
		
		case "GETCHUNK": //CHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF><Body>
			
			for(Chunk chunkSend: peer.getStorage().getChunks()) {
				
			if(chunkSend.getFileID().equals(fileID) && chunkSend.getChunkN()==chunkNo) {
				
				Random rand = new Random();
				int  n = rand.nextInt(400) + 1;
				
				
				try {
					Thread.sleep(n);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 

				Message sendChunk = new Message("CHUNK",peer.getVersion(),peer.getPeerID(),fileID, chunkNo, 0, chunkSend.getContent());
				byte[] reply = sendChunk.sendable();
				System.out.println(sendChunk.messageToStringPrintable());
				
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
			if(gotChunk.getContent().length < 64000)
				peer.lastChunk();
			break;
			
		case "REMOVED":
            try {
                TimeUnit.MILLISECONDS.sleep(400);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            peer.getStorage().getBackUps().remove(new BackUpInfo(fileID, chunkNo, peer.getPeerID()));
            break;
		}
	}
	static byte[] trim(byte[] bytes)
	{
	    int i = bytes.length - 1;
	    while (i >= 0 && bytes[i] == 0)
	    {
	        --i;
	    }

	    return Arrays.copyOf(bytes, i + 1);
	}


	}

	

