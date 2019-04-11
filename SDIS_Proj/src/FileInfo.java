import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;


public class FileInfo implements Serializable {

	private int CHUNK_SIZE = 64000; //64kb
	  private String fileID;
	  private int chunkCount;
	    private File file;
	    private long filesize;
	    private int repDeg;
	    private int peerID;
	    private ArrayList<Chunk> fileChunks;

	    public FileInfo(String path, int replicationDegree, int PeerID) throws IOException {
	        this.file = new File(path);
	        this.filesize = file.length(); //size in bytes
	        this.repDeg = replicationDegree;
	        generateFileID();
	        this.chunkCount = 0; 
	        splitIntoChunks();
	       
	    }
	    
	    public void splitIntoChunks() throws IOException
	    {
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
	       
	       while ( totalBytesRead < FILE_SIZE )
	       {
	        String PART_NAME ="data"+this.chunkCount+".bin";
	        int bytesRemaining = FILE_SIZE-totalBytesRead;
	        if ( bytesRemaining < this.CHUNK_SIZE ) // Remaining Data Part is Smaller Than CHUNK_SIZE
	                   // CHUNK_SIZE is assigned to remain volume
	        {
	         this.CHUNK_SIZE = bytesRemaining;
	         System.out.println("CHUNK_SIZE: "+this.CHUNK_SIZE);
	         lastChunk = true;
	        }
	        
	        temporary = new byte[this.CHUNK_SIZE]; //Temporary Byte Array
	        int bytesRead = inStream.read(temporary, 0, this.CHUNK_SIZE);
	        
	        if ( bytesRead > 0) // If bytes read is not empty
	        {
	         totalBytesRead += bytesRead;
	         this.chunkCount++;
	        }
	        
	        fileChunks.add(new Chunk(this.fileID,this.chunkCount, temporary));
	        
	        if(bytesRemaining == 0 && lastChunk)
		        fileChunks.add(new Chunk(this.fileID,this.chunkCount, new byte[this.CHUNK_SIZE]));
	        
	        System.out.println("Total Bytes Read: "+totalBytesRead);
	       }
	       
	      }
	      finally {
	       inStream.close();
	      }
	     }
	     catch (FileNotFoundException ex)
	     {
	      ex.printStackTrace();
	     }
	     catch (IOException ex)
	     {
	      ex.printStackTrace();
	     }
	     
	     this.CHUNK_SIZE = 64000;
	 
	    }

	    
	    
	    public static void mergeChunks ( ArrayList<Chunk> chunkList, String DESTINATION_PATH, String filename ) throws IOException
	    {
	   	     
	 
	    	 File targetFile = new File(DESTINATION_PATH+filename);
	    	OutputStream outStream = new FileOutputStream(targetFile);
	       
	    	for(int i = 0; i < chunkList.size(); i++) {
	    		byte[] content = chunkList.get(i).getContent();
	    		 outStream.write(content);
	    	}
	          
	    	outStream.close();
	    
	    }

	    
		void generateFileID() {
	    	
	    	String aux  = this.peerID + file.getName() + file.lastModified();
			
			
	    	this.fileID = sha256hashing(aux);
	    }
	    
	    public String getFileID(){
	    	return this.fileID;
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
	   
	    

		public ArrayList<Chunk> getFileChunks() {
			return fileChunks;
		}

		public void setFileChunks(ArrayList<Chunk> fileChunks) {
			this.fileChunks = fileChunks;
		}
	    
}
