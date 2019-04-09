package messages;

public class Header {
	String messageType;
	String version;
	int senderId;
	String fileId;
	int chunkNo;
	int replicationDeg;
	String CRLF = "\r\n";
	
	public Header(String messageType, String version, int senderId, String fileId, int chunkNo, int replicationDeg) {
		this.messageType=messageType;
		this.version=version;
		this.senderId=senderId;
		this.fileId=fileId;
		this.chunkNo=chunkNo;
		this.replicationDeg=replicationDeg;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public int getChunkNo() {
		return chunkNo;
	}

	public void setChunkNo(int chunkNo) {
		this.chunkNo = chunkNo;
	}

	public int getReplicationDeg() {
		return replicationDeg;
	}

	public void setReplicationDeg(int replicationDeg) {
		this.replicationDeg = replicationDeg;
	}

	public String getCRLF() {
		return CRLF;
	}
	
}
