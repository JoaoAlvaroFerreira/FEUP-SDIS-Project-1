
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.ArrayList;




public class Peer implements RMI {


	//final static int PORT = 8888;
	private static double version;
	private static int server_id;
	private static int peer_id;
	private static String mcAddr = "224.0.0.3";
	private static int mcPort = 8888;
	private static String mdbAddr;
	private static int mdbPort;
	private static String mdrAddr;
	private static int mdrPort;

	private int peerID;
	
	static StorageSystem storage;


	public static void main(String[] args) throws UnknownHostException, InterruptedException { 

		version = Double.parseDouble(args[0]);
		server_id = Integer.parseInt(args[1]);
		peer_id = Integer.parseInt(args[2]);
		mcAddr = args[3];
		mcPort = Integer.parseInt(args[4]);
		mdbAddr = args[5];
		mdbPort = Integer.parseInt(args[6]);
		mdrAddr = args[7];
		mdrPort = Integer.parseInt(args[8]); 
		
		StorageSystem storage = new StorageSystem();

		System.out.println("teste");
		try {

			Peer obj = new Peer();

			RMI stub = (RMI) UnicastRemoteObject.exportObject(obj, 0);  

			// Binding the remote object (stub) in the registry 
			Registry registry = LocateRegistry.getRegistry(); 
			//registry.bind("Hello", stub); 

			registry.bind(args[2], stub);  
			System.err.println("Peer ready"); 




		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

	public void operation(String operation, String file_path, int rep_degree, double space) { //operator is space for reclaim, rep_degree for back up

		if(operation == "BACKUP")
		{
			new Thread(new Backup(file_path,rep_degree));
		}
		else if(operation == "RESTORE")
		{
			new Thread(new Restore(file_path));
		}
		else if(operation == "DELETE")
		{
			new Thread(new Delete(file_path));
		}
		else if(operation == "RECLAIM")
		{
			new Thread(new Reclaim(space));
		}


	}
	

	public void printMsg() {  
		System.out.println("This is an example RMI program");  
	}
	
	public int getPeerID() {
		return peerID;
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
	
	public void restoreFile(String filename) throws IOException {
		
		  String hash = this.storage.lookUp(filename);
		
		String newfilename = "Peer"+this.getPeerID() + "/restore/"+filename;

        File file = new File(newfilename);
        
      

       
    	OutputStream outStream = new FileOutputStream(file);
       
    	for(int i = 0; i < this.storage.getChunks().size(); i++) {
    		
    		if(hash == this.storage.getChunks().get(i).getFileID()) {
    		
    		byte[] content = this.storage.getChunks().get(i).getContent();
    		 outStream.write(content);
    		}
    	}
          
    	outStream.close();

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
	    	
	    	String aux  = this.peerID + file.getName() + file.lastModified();
			
			
	    	return sha256hashing(aux);
	    }
	 
}