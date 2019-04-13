import java.nio.charset.StandardCharsets;

public class Message {
	Header header;
	public static String CRLF = "" + (char) 0xD  + (char) 0xA;
	byte[] body;
	
	
	
	public Message(String messageType, String version, int senderId, String fileId,int chunkNo, int ReplicationDeg, byte[] body) {
		//<MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF>
		this.header= new Header(messageType, version, senderId, fileId, chunkNo, ReplicationDeg);
		this.body = body;
	}
	
	
	public String messageToString() {
		if(body != null)
		return header.getMessageType() + " " + header.getVersion() + " " + header.getSenderId() + " " + header.getFileId() + " " + 
				header.getChunkNo() + " " + header.getReplicationDeg() + " "+ CRLF + CRLF + body;
		else return header.getMessageType() + " " + header.getVersion() + " " + header.getSenderId() + " " + header.getFileId() + " " + 
		header.getChunkNo() + " " + header.getReplicationDeg() + " "+ CRLF + CRLF;
	}	
	
	public byte[] sendable() {
		return this.messageToString().getBytes();
	}
	
	public byte[] getBody() 
	{
		return this.body;
	}
	
}
