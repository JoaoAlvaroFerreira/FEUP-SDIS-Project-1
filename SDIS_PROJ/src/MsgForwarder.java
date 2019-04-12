import java.io.IOException;

public class MsgForwarder {
	
static Double version;
	
    public static byte CR = 0xD;
    public static byte LF = 0xA;
    public static String CRLF = "" + (char) CR + (char) LF;
    
    public MsgForwarder(double version) {
		this.version = version;
	}
	
	public static byte[] createMessage(byte[] header, byte[]body) {
		
		byte[] msg = new byte[header.length + body.length];
		System.arraycopy(header, 0, msg, 0, header.length);
		System.arraycopy(body, 0, msg, header.length, body.length);
		
		return msg;
	}

	
	public void sendPUTCHUNK(Chunk chunk) throws IOException{
		String header = "PUTCHUNK"  
						+ " " + version 
						+ " " + Peer.getPeerID()
						+ " " + Chunk.getFileID()
						+ " " + Chunk.getChunkN()
						+ " " + Chunk.getGoalRepDeg()
						+ " " + CRLF + CRLF;

		
		byte[] msg = createMessage(header.getBytes(),Chunk.getContent());
		Peer.getMdb().sendMessage(msg);
						
	}

	public static void sendCHUNK(Chunk chunk) throws IOException {
		String header = "CHUNK"  
				+ " " + version 
				+ " " + Peer.getPeerID()
				+ " " + Chunk.getFileID()
				+ " " + Chunk.getChunkN()
				+ " " + Chunk.getGoalRepDeg()
				+ " " + CRLF + CRLF;


		byte[] msg = createMessage(header.getBytes(),Chunk.getContent());
		Peer.getMdr().sendMessage(msg);
				
	}
	
	public static void sendSTORED(Chunk chunk) throws IOException {
		String header = "STORED"
						+ " " + version 
						+ " " + Peer.getPeerID()
						+ " " + Chunk.getFileID()
						+ " " + Chunk.getChunkN()
						+ " " + CRLF + CRLF;
		
		Peer.getMc().sendMessage(header.getBytes());
	}
	
	public void sendDELETE(String file_id) throws IOException {
		String header = "DELETE"
						+ " " + version
						+ " " + Peer.getPeerID()
						+ " " + file_id
						+ " " + CRLF + CRLF;
		
		Peer.getMc().sendMessage(header.getBytes());
	}

	public void sendGETCHUNK(int chunk_no, String file_id) throws IOException {
		String header = "GETCHUNK"
				+ " " + version 
				+ " " + Peer.getPeerID()
				+ " " + file_id
				+ " " + chunk_no
				+ " " + CRLF + CRLF;
		

		Peer.getMc().sendMessage(header.getBytes());
		
	}

	public void sendREMOVED(int chunk_no, String file_id) throws IOException {
		String header = "REMOVED"
				+ " " + version 
				+ " " + Peer.getPeerID()
				+ " " + file_id
				+ " " + chunk_no
				+ " " + CRLF + CRLF;
		

		Peer.getMc().sendMessage(header.getBytes());
		
		
	}
}
