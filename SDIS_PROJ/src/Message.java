
public class Message {
	Header header;
	String CRLF = "\r\n";
	byte[] body;
	public static final int MAX_CHUNK_SIZE = 64000;
	
	
	
	public Message(String messageType, String version, int senderId, String fileId,int chunkNo, byte[] body) {
		this.header= new Header(messageType, version, senderId, fileId, chunkNo, 0);
		this.body = body;
	}
	
	
	public String messageToString() {
		return header.getMessageType() + " " + header.getVersion() + " " + header.getSenderId() + " " + header.getFileId() + " " + 
				header.getChunkNo() + " " + CRLF + CRLF + body;
	}	
	
	public byte[] getBody() 
	{
		return this.body;
	}
	
}
