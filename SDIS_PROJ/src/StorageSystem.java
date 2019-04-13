import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class StorageSystem implements Serializable{

	private long storage_capacity = 10000000; // 8MBytes
	private long used_storage;
	private long space_available = storage_capacity - used_storage;
	private int peerID;

	public long getStorage_capacity() {
		return storage_capacity;
	}

	public long getUsed_storage() {
		return used_storage;
	}

	public long getSpace_available() {
		return space_available;
	}

	private ArrayList<Chunk> chunks;
	private ArrayList<FileInfo> fileinfo;
	
	public StorageSystem(int peerID) {
		this.peerID = peerID;
		used_storage = 0;
		chunks = new ArrayList<Chunk>();
		fileinfo = new ArrayList<FileInfo>();
		
		try {
			loadFileInfo();
		} catch (IOException e) {
			System.out.println("Load File Info Failed:" + e.toString());
			e.printStackTrace();
		}
	
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
	
	public boolean isStored(Chunk c) {
		
		boolean isEqualChunk = false;
		
		for(int i = 0; i < chunks.size(); i++) {
			if(chunks.get(i).getChunkId() == c.getChunkId()) {
				isEqualChunk = true;
			}
		}
		
		return isEqualChunk;
	}
	
	public void addFile(String filename, long data, String hash) {
		System.out.println("ADD FILE HASH: "+hash);
		fileinfo.add(new FileInfo(hash, data, filename, this.peerID));
	}
	
	public boolean storeChunk(Chunk c) {
		byte[] content = c.getContent();
		boolean chunkStored = false;
		
		
		if(space_available-content.length < 0) {
			System.out.println("No space available");
			return chunkStored;
		}
		used_storage += content.length; 
		
		
		return chunkStored;
	}
	
	   public void splitIntoChunks(File file, String fileID, int chunk_size) throws IOException
	    {
		   System.out.println("In Split");
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
	        String PART_NAME = "data"+chunkCount+".bin";
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
	        
	        System.out.println("Total Bytes Read: "+ totalBytesRead);
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
	   
	   public boolean moreRecent(FileInfo a, FileInfo b) {
	        if (a.getDateModified() > b.getDateModified())
	            return true; // highest value first
	        else  return false;
	    }
	   
	   public String lookUp(String filename) {
		   System.out.println("LU Filename:"+filename);
			ArrayList<FileInfo> samename = new ArrayList<FileInfo>();
			String aux = null;
			FileInfo auxFile = null;
			
			for(int i = 0; i < fileinfo.size(); i++) {
				
				
				System.out.println("FILENAME: "+fileinfo.get(i).getFilename()+" FILEID: "+fileinfo.get(i).getFileID());
				
			if(fileinfo.get(i).getFilename().equals(filename))
				aux = fileinfo.get(i).getFileID();
				//samename.add(fileinfo.get(i));
				
			}

			/*if(samename.size() == 1) {
			aux = samename.get(0).getFileID();
			 System.out.println("aux: "+aux);
			}
			
			else {
				for(int j = 0; j < samename.size(); j++) {
					if(moreRecent(samename.get(j), auxFile)){
						auxFile = samename.get(j);
						aux = samename.get(j).getFileID();
						}
						}
					}*/
			
			
			return aux;
	   }
	   
	   
		//"/fileInfo/Peer"+peerID+"/"+fileID
		public void serializeFileInfo() {
			
			for(int i = 0; i < this.fileinfo.size(); i++ ) {
				
			try{
				
				File file = new File("fileInfo/Peer"+peerID+"/"+this.fileinfo.get(i).getFileID()+".ser");
				
			    if (!file.exists()) {
		             file.getParentFile().mkdirs();
		             file.createNewFile();
		         }
			    ObjectOutputStream objoutput = new ObjectOutputStream(new FileOutputStream(file.getPath()));
				objoutput.writeObject(this.fileinfo.get(i));
				
				objoutput.close();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
			
		}
		
		
		public void loadFileInfo() throws IOException {
			 System.out.println("loadFileInfo");
			
			if(Files.exists(Paths.get("/fileInfo/Peer"+peerID+"/"))) {
			 Files.walk(Paths.get("/fileInfo/Peer"+peerID+"/"))
		     .forEach(p -> {
		        try {
		        	try(ObjectInputStream objinput = new ObjectInputStream(new FileInputStream(p.toString()))){
						Object obj = objinput.readObject();
						
						if(obj instanceof FileInfo) {
							FileInfo fileinforetrieved = (FileInfo) obj;
							this.fileinfo.add(fileinforetrieved);
						}
					}  catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    });
			}
			 
			 
			
		}
	  
}

