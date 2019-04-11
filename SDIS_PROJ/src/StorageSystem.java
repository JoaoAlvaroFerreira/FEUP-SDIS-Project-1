import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class StorageSystem implements Serializable{

	private long storage_capacity = 10000000; // 8MBytes
	private long used_storage;

	private ArrayList<Chunk> chunks;
	private ArrayList<FileInfo> files; //restored
	
	public StorageSystem() {
		used_storage = 0;
		chunks = new ArrayList<Chunk>();
	
	}
	
	public void addChunk(Chunk c) {
		if(storage_capacity > used_storage + c.getsize()) {
		used_storage += c.getsize();
		
		chunks.add(c);
		}
	}
	
	public ArrayList<Chunk> getChunks() {
		return chunks;
	}
	
	   public void splitIntoChunks(File file, String fileID, int chunk_size) throws IOException
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
	       
	       int chunkCount = 0;
	       while ( totalBytesRead < FILE_SIZE )
	       {
	        String PART_NAME ="data"+chunkCount+".bin";
	        int bytesRemaining = FILE_SIZE-totalBytesRead;
	        if ( bytesRemaining < chunk_size ) 
	        {
	         chunk_size = bytesRemaining;
	         System.out.println("CHUNK_SIZE: "+chunk_size);
	         lastChunk = true;
	        }
	        
	        temporary = new byte[chunk_size]; //Temporary Byte Array
	        int bytesRead = inStream.read(temporary, 0, chunk_size);
	        
	        if ( bytesRead > 0) // If bytes read is not empty
	        {
	         totalBytesRead += bytesRead;
	         chunkCount++;
	        }
	        
	        chunks.add(new Chunk(fileID,chunkCount, temporary));
	        
	        if(bytesRemaining == 0 && lastChunk)
		        chunks.add(new Chunk(fileID,chunkCount, new byte[chunk_size]));
	        
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
	     
	    }
}

