public class Chunk{
	private String chunkID;
	private String fileID;
	private static byte[] content;
	private int chunkN;
	private int size; 
	



 
		public Chunk(String fileID, int chunkn, byte[] content) {
			this.chunkN = chunkn;
			this.fileID = fileID;
			this.content = content;
			this.size = content.length;
			this.chunkID=fileID + "chunk" + chunkn;
		
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
		
		public static byte[] getContent() {
			return content;
		}
		
		public int getsize() {
			return this.size;
		}
		
	
	}
