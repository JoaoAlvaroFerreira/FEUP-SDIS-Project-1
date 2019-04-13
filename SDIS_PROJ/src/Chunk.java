public class Chunk{
	private String chunkID;
	private String fileID;
	private byte[] content;
	private int chunkN;
	private int size; 
	private int originalPeerID;

	public static int chunkMaxSize = 64000;
	



 
		public Chunk(String fileID, int chunkn, byte[] content, int peerID) {
			this.chunkN = chunkn;
			this.fileID = fileID;
			this.content = content;
			this.size = content.length;
			this.chunkID=fileID + "chunk" + chunkn;
			this.originalPeerID = peerID;
		}

		
		public String getChunkId() {
			return this.chunkID;
		}
		
		public int getChunkN() {
			return this.chunkN;
		}
		
		public String getFileID() {
			return this.fileID;
		}
		
		public byte[] getContent() {
			return this.content;
		}
		
		public int getsize() {
			return this.size;
		}
		
	
	}
