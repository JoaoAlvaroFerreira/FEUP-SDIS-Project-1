import java.nio.charset.StandardCharsets;

public class Message {
	Header header;
	String CRLF = "\r\n";
	byte[] body;
	public static final int MAX_CHUNK_SIZE = 64000;
	
	
	
	public Message(String messageType, String version, int senderId, String fileId,int chunkNo, int ReplicationDeg, byte[] body) {
		//<MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF>
		this.header= new Header(messageType, version, senderId, fileId, chunkNo, ReplicationDeg);
		this.body = body;
	}
	
	
	public String messageToString() {
		return header.getMessageType() + " " + header.getVersion() + " " + header.getSenderId() + " " + header.getFileId() + " " + 
				header.getChunkNo() + " " + header.getReplicationDeg() + " "+ CRLF + CRLF + body;
	}	
	
	public byte[] sendable() {
		return this.messageToString().getBytes(StandardCharsets.UTF_8);
	}
	
	public byte[] getBody() 
	{
		return this.body;
	}
	
}
