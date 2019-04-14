public class BackUpInfo {

	String fileID;
	int chunkN;
	int peerID;
	
	public BackUpInfo(String fileID, int chunkN, int peerID) {
		this.fileID = fileID;
		this.chunkN = chunkN;
		this.peerID = peerID;
	}
	
	String getFileID() {
		return this.fileID;
	}
	
	int getChunkN() {
		return this.chunkN;
	}
	

	int getPeerID() {
		return this.peerID;
	}
}
