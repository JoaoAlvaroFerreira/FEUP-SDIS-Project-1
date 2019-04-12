import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Chunk implements Serializable{
	private static  String chunkID;
	private static String fileID;
	private static byte[] content;
	private static int chunkN;
	private static int goalRepDeg;
	private int currRepDeg;
	private int size; 
	public static int chunkMaxSize = 64000;
	



 
		public Chunk(String fileID, int chunkn, byte[] content, int goalRepDeg ) {
			this.chunkN = chunkn;
			this.fileID = fileID;
			this.content = content;
			this.size = content.length;
			this.chunkID=fileID + "chunk" + chunkn;
			this.goalRepDeg=goalRepDeg;
		}

		
		public void setGoalRepDeg(int goalrepdeg){
			this.goalRepDeg = goalrepdeg;
		}
		public static void incRepDegree(String chunk_id, int saves) {
			
			
			for(int i=0; i< StorageSystem.chunks.size();i++) {
				if(StorageSystem.chunks.get(i).getChunkId().equals(chunk_id)) {
					StorageSystem.chunks.get(i).setActualRepDegree(saves);
				}
			}
			
			try {
				Peer.saveStorage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		private void setActualRepDegree(int saves) {
			// TODO Auto-generated method stub
			
		}


		public static int getGoalRepDeg() {
			return goalRepDeg;
		}
		
		public int getCurrRepDeg() {
			return this.currRepDeg;
		}
		
		public static String getChunkId() {
			return chunkID;
		}
		
		public static int getChunkN() {
			return chunkN;
		}
		
		public static String getFileID() {
			return fileID;
		}
		
		public static byte[] getContent() {
			return content;
		}
		
		public int getsize() {
			return this.size;
		}


		public static void backup() {
			long wait_time =1;
			int putchunk_sent=0;
			int stored =0;
			
			
			do {
				Peer.getMc().startSave(chunkID);
				//MsgForwarder.sendPUTCHUNK();				FALTA ARGUMENTO CHUNK DO PUTCHUNK
				putchunk_sent++;
				
				try {
					TimeUnit.SECONDS.sleep(wait_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				stored = Peer.getMc().getSaves(chunkID);
				
				wait_time *=2;
				
			}while(stored<goalRepDeg && putchunk_sent !=5);
			
		
			Peer.getMc().stopSave(chunkID);
		}


		public static void restore() {
			// TODO Auto-generated method stub
			
		}
		
	
	}
