import java.io.Serializable;
import java.util.ArrayList;

public class Chunk implements Serializable{
	private String chunkID;
	private String fileID;
	private byte[] content;
	private int chunkN;
	private int goalRepDeg;
	private int currRepDeg;
	private int size; 
	public static int chunkMaxSize = 64000;
	



 
		public Chunk(String fileID, int chunkn, byte[] content) {
			this.chunkN = chunkn;
			this.fileID = fileID;
			this.content = content;
			this.size = content.length;
			this.chunkID=fileID + "chunk" + chunkn;
		}

		
		public void setGoalRepDeg(int goalrepdeg){
			this.goalRepDeg = goalrepdeg;
		}
		public void incCurrRepDeg(int currrepdeg){
			this.currRepDeg++;
		}
		
		public int getGoalRepDeg() {
			return this.goalRepDeg;
		}
		
		public int getCurrRepDeg() {
			return this.currRepDeg;
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
