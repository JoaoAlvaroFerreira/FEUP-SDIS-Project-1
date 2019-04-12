import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Random;


public class MessagesManager implements Runnable{

	DatagramPacket packet;
	
	String[] header;
	
	public MessagesManager(DatagramPacket packet){
		this.packet = packet;
	}
	
	@Override
	public void run() {
		
		header = Utils.parseHeader(packet);
		
		int server_id = Integer.parseInt(header[2]);
		
		
		// if message comes from self ignore it 
		if(server_id == Peer.getPeerID()) return;
		
		String operation = header[0];
		
		switch(operation){
		case "PUTCHUNK":
			try {
				handlePUTCHUNK();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "STORED":
			handleSTORED();
			break;
		case "DELETE":
			handleDELETE();
			break;
		case "GETCHUNK":
			try {
				handleGETCHUNK();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "CHUNK":
			handleCHUNK();
			break;
		case "REMOVED":
			handleREMOVED();
		default:
			break;
			
		}
		
	}

	private void handleREMOVED() {
		System.out.println("REMOVED RECEIVED");
		
		String file_id = header[3];
		int chunk_no = Integer.parseInt(header[4]);
		
		Chunk chunk = new Chunk(file_id, chunk_no, new byte[0], 0);
		
		
		ArrayList<Chunk> chunks = StorageSystem.getChunks();
	
			if(chunks.contains(chunk)) {
			
				int i=0;	
				while(true) {
					
					if(chunks.get(i).getChunkId().equals(chunk.getChunkId())) {
						chunk= chunks.get(i);
	
						
						chunk.setGoalRepDeg(chunk.getCurrRepDeg()-1);
						
						
						if(chunk.getCurrRepDeg() < Chunk.getGoalRepDeg()) {
							
							// wait a random delay
							Random rand = new Random();
							int  n = rand.nextInt(400) + 1;
							
							Peer.getMdb().startSave(chunk.getChunkId());
														
							try {
								Thread.sleep(n);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							
							
							int save = Peer.getMdb().getSaves(chunk.getChunkId());
							
							System.out.println("SAVES " + save);
							
							Peer.getMdb().stopSave(chunk.getChunkId());
							
							if(save ==0)
								Chunk.backup();
								
						}
						return;
					}
					i++;
				}
			}			
		
	}

	private void handleCHUNK() {
		System.out.println("CHUNK RECEIVED");
		
		// parsing message
		String file_id = header[3];
		int chunk_no = Integer.parseInt(header[4]);
		int rep_degree= Integer.parseInt(header[5]);
		byte[] chunk_data =Utils.parseBody(packet);
		
		if(Peer.getMdr().isSaving(file_id)){
			Chunk chunk = new Chunk(file_id,chunk_no,chunk_data, rep_degree);
			Peer.getMdr().save(file_id, chunk);
		}
			
	}

	private void handleGETCHUNK() throws IOException {
		System.out.println("GETCHUNK RECEIVED");
		
		String file_id = header[3];
		int chunk_no = Integer.parseInt(header[4]);
	
		
		File file = new File(Peer.CHUNKS +  chunk_no + "_"+ file_id);
		
		Peer.getMdr().startSave(file_id);
		
		if(file.exists() && file.isFile()) {
			byte[] chunk_data = Utils.loadFileBytes(file);
			
			Chunk chunk =new Chunk(file_id, chunk_no,chunk_data,0);
			
			// wait a random delay
			Random rand = new Random();
			int  n = rand.nextInt(400) + 1;
			
			
			try {
				Thread.sleep(n);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			
			ArrayList<Chunk> chunks =Peer.getMdr().getSave(file_id);
			
			
			if(chunks != null)
			if(!chunks.contains(new Chunk(file_id, chunk_no, new byte[0], 0)))
					MsgForwarder.sendCHUNK(chunk);
			
		}
		
		Peer.getMdr().stopSave(file_id);
		
		
	}

	private void handleDELETE() {
		System.out.println("DELETE RECEIVED");
		
		String file_id = header[3];

		StorageSystem.deleteChunks(file_id);
	   
		
	}

	private void handleSTORED() {
		System.out.println("STORED RECEIVED");
		
		int peer_id = Integer.parseInt(header[2]);
		String file_id=header[3];
		int chunk_no = Integer.parseInt(header[4]);
		
		String chunk_id = chunk_no + "_" +file_id; 
		
		Peer.getMc().save(chunk_id,peer_id );
		
		if(StorageSystem.isStored(new Chunk(file_id,chunk_no, new byte[0],0))) 
			Chunk.incRepDegree(chunk_id,Peer.getMc().getSaves(chunk_id)+1);
		
		
	}

	private void handlePUTCHUNK() throws IOException {
		System.out.println("PUTCHUNK RECEIVED");
		
		// chunk info from header
		String file_id=header[3];
		int chunk_no = Integer.parseInt(header[4]);
		int rep_degree = Integer.parseInt(header[5]);
		
		// chunk data from body
		byte[] chunk_data =Utils.parseBody(packet);
		
		
		// create chunk 
		Chunk chunk = new Chunk(file_id,chunk_no,chunk_data,rep_degree );
		Peer.getMdb().save(chunk.getChunkId(), 0);
		
		// stored chunk if not stored already
		if(!StorageSystem.isStored(chunk)) {
			StorageSystem.storeChunk(chunk);
		}
		
		// start saving STORED messages
		Peer.getMc().startSave(chunk.getChunkId());
		
		// wait a random delay
		Random rand = new Random();
		int  n = rand.nextInt(400) + 1;
		
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
		// send STORED message
		MsgForwarder.sendSTORED(chunk);
		
		
		
	}
	
	public class chunkFilter implements FilenameFilter {
		
	       private String file_id;
		
	       public chunkFilter(String file_id) {
	         this.file_id = file_id;             
	       }
		       
	       public boolean accept(File dir, String name) {
	         return (name.endsWith(file_id));
	       }
	    }
	

}