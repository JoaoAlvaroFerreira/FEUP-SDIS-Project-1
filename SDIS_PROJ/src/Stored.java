
public class Stored {
	Header header;
	String CRLF = "\r\n";
	byte[] body;
	
	public Stored(String messageType, String version, int senderId, String fileId,int chunkNo) {
		this.header= new Header(messageType, version, senderId, fileId, chunkNo, 0);
	}
	
	
	public String storedToString() {
		return header.getMessageType() + " " + header.getVersion() + " " + header.getSenderId() + " " + header.getFileId() + " " + 
				header.getChunkNo() + " " + CRLF + CRLF + body;
	}	
	
	
}
