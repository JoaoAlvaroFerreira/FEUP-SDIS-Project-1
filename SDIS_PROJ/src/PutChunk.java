
public class PutChunk {
	Header header;
	String CRLF = "\r\n";
	byte[] body;

	public PutChunk(String messageType, String version, int senderId, String fileId,int chunkNo, int replicationDeg, byte[] body) {
		this.header= new Header(messageType, version, senderId, fileId, chunkNo, replicationDeg);
		this.body= body;
	}


	public String putChunkToString() {
		return header.getMessageType() + " " + header.getVersion() + " " + header.getSenderId() + " " + header.getFileId() + " " + 
				header.getChunkNo() + " " + header.getReplicationDeg() + " " + CRLF + CRLF + body;
	}

	public String getHeader() {
		return header.getMessageType()+ " " + header.getVersion() + " " + header.getSenderId() + " " + header.getFileId() + " " + 
				header.getChunkNo() + " " + header.getReplicationDeg()+ " " + CRLF + CRLF; 
	}

	public void sendPutChunk(Chunk chunk) {

	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}	


}
