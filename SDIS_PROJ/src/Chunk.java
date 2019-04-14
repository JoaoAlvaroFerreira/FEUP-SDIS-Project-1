public class Chunk{
	private String chunkID;
	private String fileID;
	private byte[] content;
	private int chunkN;
	private int size; 
	private int repDeg;
	private int goalRepDeg;



 
		public Chunk(String fileID, int chunkn, byte[] content) {
			this.chunkN = chunkn;
			this.fileID = fileID;
			this.content = content;
			this.size = content.length;
			this.chunkID=fileID + "chunk" + chunkn;
			this.repDeg = 1;
		
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

		public int getRepDeg() {
			return repDeg;
		}

		public void setRepDeg(int repDeg) {
			this.repDeg = repDeg;
		}
		
		public void increaseRepDeg() {
			this.repDeg++;
		}
		public void decreaseRepDeg() {
			this.repDeg--;
		}

		public int getGoalRepDeg() {
			return goalRepDeg;
		}

		public void setGoalRepDeg(int goalRepDeg) {
			this.goalRepDeg = goalRepDeg;
		}
	
	}
