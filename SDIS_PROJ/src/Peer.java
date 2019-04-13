
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;




public class Peer implements RMI {


	//final static int PORT = 8888;
	private static String version;
	private static int server_id;
	private static int peerID;
	private static String mcIp = "224.0.0.3";
	private static int mcPort = 8888;
	private static String mdbIp;
	private static int mdbPort;
	private static String mdrIp;
	private static int mdrPort;
	
	private static Channel mc;
	private static Channel mdb;
	private static Channel mdr;
	private boolean lastChunk = false;


	
	private static StorageSystem storage;
	
	private int chunkIterator = 0;
	private Peer(String mcIp, int mcPort, String mdbIp, int mdbPort, String mdrIp, int mdrPort) throws IOException {
		mc = new Channel(mcIp, mcPort, this);
		mdb = new Channel(mdbIp, mdbPort, this);
		mdr = new Channel(mdrIp,mdrPort, this);
		
		new Thread(mc).start();
		new Thread(mdb).start();
		new Thread(mdr).start();
	}


	public static void main(String[] args) throws UnknownHostException, InterruptedException { 

		setVersion(args[0]);
		server_id = Integer.parseInt(args[1]);
		peerID = Integer.parseInt(args[2]);
		mcIp = args[3];
		mcPort = Integer.parseInt(args[4]);
		mdbIp = args[5];
		mdbPort = Integer.parseInt(args[6]);
		mdrIp = args[7];
		mdrPort = Integer.parseInt(args[8]); 
		
		storage = new StorageSystem(peerID);
		

		System.out.println("teste");
		try {

			Peer obj = new Peer(mcIp, mcPort, mdbIp, mcPort, mdrIp, mcPort);

			RMI stub = (RMI) UnicastRemoteObject.exportObject(obj, 0);  

			// Binding the remote object (stub) in the registry 
			Registry registry = LocateRegistry.getRegistry(); 
			registry.rebind(args[2], stub); 

			
			System.err.println("Peer ready"); 




		} catch (Exception e) {
			System.err.println("Peer exception: " + e.toString());
			e.printStackTrace();
		}
	}
	
	
	/*
	
	public void restore(String file_path) {
		String hash = storage.lookUp(file_path);
		System.out.println("Hash: "+hash);
		try {
			getChunksFromFile(hash);
			System.out.println("Got chunks already");
			restoreFile(file_path, hash);
		} catch (IOException e) {
			System.err.println("IO Exception: " + e.toString());
			e.printStackTrace();
		}
	} */

	public void operation(String operation, String file_path, int rep_degree, double space) { //operator is space for reclaim, rep_degree for back up

		System.out.println("in operation");
		if(operation.equals("BACKUP"))
		{
			
			try {
				initiateBackup(file_path, rep_degree);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(operation.equals("RESTORE"))
		{
			try {
				initiateRestore(file_path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(operation.equals("DELETE"))
		{
			initiateDelete(file_path);
		
		}
		else if(operation == "RECLAIM")
		{
			initiateReclaim(space);
		}


	}
	

	/* backup velho, no usar, manter como referencia
	 
	public void backup(String file_path, int rep_degree){
		
		File file = new File(file_path);
		String fileID = generateFileID(file);
		System.out.println("FileID Hash:" + fileID);
		System.out.println("Done hashing");
		try {
			System.out.println("In try");
			System.out.println("File Path: "+file_path);
			storage.addFile(file_path, file.lastModified(), fileID);
			storage.serializeFileInfo();
			storage.splitIntoChunks(file, fileID, 64000);
			System.out.println("Done SPLITTING");
			saveChunks();
			System.out.println("Saved chunks");
		} catch (IOException e) {
			System.err.println("IO Exception: " + e.toString());
			e.printStackTrace();
		}
		
	}
	*/

	private void initiateReclaim(double space) {
		// TODO Auto-generated method stub
		
	}


	private void initiateDelete(String file_path) {
		// TODO Auto-generated method stub
		
	}
	public void lastChunk() {
		this.lastChunk = true;
	}


	private void initiateRestore(String file_path) throws IOException { //GETCHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
		
		String hash = storage.lookUp(file_path);
		int chunkn = 0;
		byte[] msgByte = null;
		Message msg;
			///getChunksFromFile(hash);
		while(!lastChunk) {
			chunkn++;
			msg = new Message("GETCHUNK", getVersion(), this.getPeerID(),hash,chunkn, 0, null);
			msgByte = msg.sendable();
			System.out.println(msg.messageToString());
			try {
				mdr.sendMessage(msgByte);

				
				
				for(int i = 0; i < 5; i++) {
					
					TimeUnit.SECONDS.sleep(i);
					
					if(replicationCheck(hash)) {
						break;
					}
					else {
						mdr.sendMessage(msgByte);
						System.out.println(msg.messageToString());
					}
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		

	
		String newfilename = "Peer"+this.getPeerID() + "/restore/"+file_path;

        File file = new File(newfilename);
        
        
       
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

       
    	OutputStream outStream = new FileOutputStream(file);
       
    	for(int i = 0; i < storage.getChunks().size(); i++) {
    		
    		if(hash == storage.getChunks().get(i).getFileID()) {
    		
    		byte[] content = storage.getChunks().get(i).getContent();
    		 outStream.write(content);
    		}
    	}
          
    	outStream.close();
		}
		lastChunk = false;
	}


	private boolean replicationCheck(String hash) {
		
		return false;
	}


	private void initiateBackup(String file_path, int rep_degree) throws InterruptedException {

		File file = new File(file_path);
		String fileID = generateFileID(file);
		System.out.println("FileID Hash:" + fileID);
		System.out.println("Done hashing");
		byte[] msgByte = null;
		Message msg;
	
		try {
			storage.addFile(file_path, file.lastModified(), fileID);
			storage.serializeFileInfo();
			ArrayList<Chunk> chunks = storage.splitIntoChunksExternal(file, fileID, 64000);
			
			
				for(Chunk chunk : chunks) {
			
					msg = new Message("PUTCHUNK", getVersion(), this.getPeerID(), chunk.getFileID(),chunk.getChunkN(), rep_degree, chunk.getContent());
					msgByte = msg.sendable();
					mdb.sendMessage(msgByte);
					System.out.println(msg.messageToString());
					
					for(int i = 0; i < 5; i++) {
						
					TimeUnit.SECONDS.sleep(i);
					
					if(storedCheck(chunk.getChunkN(), chunk.getFileID()) == rep_degree) {
						break;
					} 
					else {
						mdb.sendMessage(msgByte); 
						System.out.println(msg.messageToString());
					}
					}
					
				
				}
			
		
			
		} catch (IOException e) {
			System.err.println("IO Exception: " + e.toString());
			e.printStackTrace();
		}
		
		
}
	
	private int storedCheck(int chunkN, String fileID) {
		int timesSaved = 0;
		
		for(BackUpInfo info : storage.getBackUps()) {
			if(chunkN == info.getChunkN() && info.getFileID().equals(fileID))
				timesSaved++;
		}
		
		return timesSaved;
	}




	
	public int getPeerID() {
		return peerID;
	}
	
	public Channel getMC() {
		return mc;
	}
	
	public Channel getMDB() {
		return mdb;
	}

	public Channel getMDR() {
		return mdr;
	}




	public void saveChunks() throws IOException{
		
		for(int i = 0; i < storage.getChunks().size(); i++) {
			
			//System.out.println("PeerID: "+this.getPeerID());
			 String filename = "Peer"+this.getPeerID() + "/backup/"+storage.getChunks().get(i).getFileID()+"/chk"+storage.getChunks().get(i).getChunkN();

	         File file = new File(filename);
	         if (!file.exists()) {
	             file.getParentFile().mkdirs();
	             file.createNewFile();
	         }
	         
	         FileOutputStream fileOut = new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(storage.getChunks().get(i).getContent());
	         out.close();
	         fileOut.close();
		}
		
	}
	
	/*
	public void restoreFile(String  filename, String hash) throws IOException {
		
		System.out.println("restore file");
		
		
		String newfilename = "Peer"+this.getPeerID() + "/restore/"+filename;

        File file = new File(newfilename);
        
        
       
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

       
    	OutputStream outStream = new FileOutputStream(file);
       
    	for(int i = 0; i < storage.getChunks().size(); i++) {
    		
    		if(hash == storage.getChunks().get(i).getFileID()) {
    		
    		byte[] content = storage.getChunks().get(i).getContent();
    		 outStream.write(content);
    		}
    	}
          
    	outStream.close();

	}
	
*/
	 public static String sha256hashing(String base) {
	        try{
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            byte[] hashed = md.digest(base.getBytes("UTF-8"));
	            StringBuffer hexString = new StringBuffer();

	            for (int i = 0; i < hashed.length; i++) {
	                String hex = Integer.toHexString(0xff & hashed[i]);
	                if(hex.length() == 1) hexString.append('0');
	                hexString.append(hex);
	            }

	            return hexString.toString();
	        } catch(Exception ex){
	           throw new RuntimeException(ex);
	        }
	    }
	 
	 String generateFileID(File file) {
	    	
	    	String aux  =file.getName() + file.lastModified();
			
			
	    	return sha256hashing(aux);
	    }
	 
		public void getChunksFromFile(String hash) throws IOException { 
				   System.out.println("getChunks");
		   System.out.println("Hash: "+hash);
		   
		   if(Files.exists(Paths.get("Peer"+this.getPeerID() + "/backup/"+hash+"/"))){
		   File folder = new File("Peer"+this.getPeerID() + "/backup/"+hash+"/");
		   File[] files = folder.listFiles();
		   
		    for (File file : files)
		    {
		    	chunkIterator++;
		    	byte[] chunkContent = Files.readAllBytes(file.toPath());
		    	storage.addChunk(new Chunk(hash,chunkIterator,chunkContent));
		    }
		    chunkIterator = 0;
		   }
		   else{
			   System.out.println("Chunks you're looking for don't exist");
			   } 
			
			 
		}


		public String getVersion() {
			return version;
		}


		public static void setVersion(String version) {
			Peer.version = version;
		}


		public StorageSystem getStorage() {
			return storage;
		}
}