
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
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
	
	
	public void operation(String operation, String file_path, int rep_degree, double space) { //operator is space for reclaim, rep_degree for back up

		
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
			try {
				initiateDelete(file_path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		else if(operation == "RECLAIM")
		{
			initiateReclaim(space);
		}


	}
	




	 
private void initiateReclaim(double space) {
           
             
               
                for(int i = 0 ; i < storage.getChunks().size();i++) {
                    if(space > 0) {
                        Chunk chunk = storage.getChunks().get(i);
                        space -= chunk.getsize();
                       
                        Message msg = new Message("REMOVED", getVersion(), this.getPeerID(), chunk.getFileID(), chunk.getChunkN(), 0 , null);
                        byte[] msgByte = msg.sendable();
                       
                        try {
                            mc.sendMessage(msgByte);
                        } catch (UnknownHostException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }  
                        String newfilename = "Peer"+this.getPeerID() + "/"+ chunk.getFileID()+ "/chk" + chunk.getChunkN();
 
                        File file = new File(newfilename);
                        file.delete();
                        storage.deleteChunk(chunk);
                       
                    }
                }
       
    }


	private void initiateDelete(String file_path) {
		
		
	}
	public void lastChunk() {
		this.lastChunk = true;
		System.out.println("LAST CHUNK");
	}


	private void initiateRestore(String file_path) throws IOException { //GETCHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
		
		String hash = storage.lookUp(file_path);
		int chunkn = 0;
		byte[] msgByte = null;
		Message msg;
			///getChunksFromFile(hash);
		while(!this.lastChunk) {
			
			chunkn++;
			
			msg = new Message("GETCHUNK", getVersion(), this.getPeerID(),hash,chunkn, 0, null);
			msgByte = msg.sendable();
			System.out.println(msg.messageToStringPrintable());
			try {
				mdr.sendMessage(msgByte);

				
				
				TimeUnit.SECONDS.sleep(1);
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		String newfilename = "Peer"+this.getPeerID() + "/restore/"+file_path;

        File file = new File(newfilename);
        
        
       
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } 
        
        FileOutputStream outputStream = new FileOutputStream(newfilename);
       
      
       
    	for(int i = 0; i < storage.getChunks().size(); i++) {
    		   if(storage.getChunks().get(i).getFileID().equals(hash)) {
    		
    			   outputStream.write(storage.getChunks().get(i).getContent());
    		    
    		}
    	}
          
    	
 
    	 outputStream.close();
		
    	this.lastChunk = false;
	}




	private void initiateBackup(String file_path, int rep_degree) throws InterruptedException {

		File file = new File(file_path);
		String fileID = generateFileID(file);
		byte[] msgByte = null;
		Message msg;
		boolean stored = false;
	
		try {
			storage.addFile(file_path, file.lastModified(), fileID);
			storage.serializeFileInfo();
			ArrayList<Chunk> chunks = splitIntoChunksExternal(file, fileID, 64000);
			
		
				for(Chunk chunk : chunks) {
					
					System.out.println("CHUNK N: "+ chunk.getChunkN()+ " SIZE: " + chunk.getContent().length);
					msg = new Message("PUTCHUNK", getVersion(), this.getPeerID(), chunk.getFileID(),chunk.getChunkN(), rep_degree, chunk.getContent());
					msgByte = msg.sendable();
					System.out.println(msg.messageToStringPrintable());
					mdb.sendMessage(msgByte);					
					
					for(int i = 0; i < 5 && !stored; i++) {
						
					TimeUnit.SECONDS.sleep(i);
					
					if(storedCheck(chunk.getChunkN(), chunk.getFileID()) >= rep_degree) {
						stored = true;
					} 
					else {
						mdb.sendMessage(msgByte); 
						System.out.println(msg.messageToStringPrintable());
						
					}
					} 
					stored = false;
					
				
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
				 
		   
		   try{
			Files.exists(Paths.get("Peer"+this.getPeerID() + "/backup/"+hash+"/"));
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
		   catch(FileNotFoundException ex){
			  System.out.println("Chunks not found" + ex.toString());
			  ex.printStackTrace();
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

		public ArrayList<Chunk> splitIntoChunksExternal(File file, String fileID, int chunk_size) throws IOException
	    {
			ArrayList<Chunk> filechunks = new ArrayList<Chunk>();
		 
	     Boolean lastChunk = false;
	     File willBeRead = file;
	     int FILE_SIZE = (int) willBeRead.length();

	     
	     System.out.println("Total File Size: "+FILE_SIZE);
	     
	     byte[] temporary = null;
	     
	     try {
	      InputStream inStream = null;
	      int totalBytesRead = 0;
	      
	      try {
	       inStream = new BufferedInputStream ( new FileInputStream( willBeRead ));
	       
	       int chunkCount = 0;
	       while ( totalBytesRead < FILE_SIZE )
	       {
	       
	        int bytesRemaining = FILE_SIZE-totalBytesRead;
	        if ( bytesRemaining < chunk_size ) 
	        {
	         chunk_size = bytesRemaining;
	         lastChunk = true;
	        }
	        
	        temporary = new byte[chunk_size]; //Temporary Byte Array
	        int bytesRead = inStream.read(temporary, 0, chunk_size);
	        String temp = new String(temporary, StandardCharsets.UTF_8);
	    
	        if ( bytesRead > 0) // If bytes read is not empty
	        {
	         totalBytesRead += bytesRead;
	         chunkCount++;
	        }
	 
	        filechunks.add(new Chunk(fileID,chunkCount, temporary));
	        
	        if(bytesRemaining == 0 && lastChunk)
		        filechunks.add(new Chunk(fileID,chunkCount, null));
	        
	        System.out.println("Total Bytes Read: "+ totalBytesRead);
	       }
	       
	      }
	      finally {
	       inStream.close();
	      }
	     }
	     catch (NullPointerException ex)
	     {
	    	 System.out.println("File not found"+ex.toString());
	      ex.printStackTrace();
	     }
	     catch (FileNotFoundException ex)
	     {
	      ex.printStackTrace();
	     }
	     catch (IOException ex)
	     {
	      ex.printStackTrace();
	     }
	     
	     return filechunks;
	    }
		
}