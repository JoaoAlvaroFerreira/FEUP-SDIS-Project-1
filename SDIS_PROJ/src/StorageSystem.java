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

public class StorageSystem{

	private long storage_capacity = 100000000; 
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
	private ArrayList<BackUpInfo> backupinfo;
	
	public StorageSystem(int peerID) {
		this.peerID = peerID;
		used_storage = 0;
		chunks = new ArrayList<Chunk>();
		fileinfo = new ArrayList<FileInfo>();
		backupinfo = new ArrayList<BackUpInfo>();
		
		try {
			loadFileInfo();
		} catch (IOException e) {
			System.out.println("Load File Info Failed:" + e.toString());
			e.printStackTrace();
		}
	
	}
	
	public boolean addChunk(Chunk c) {
		if(storage_capacity > used_storage + c.getsize()) {
		used_storage += c.getsize();
		
				if(!isStored(c)) {
					chunks.add(c);
					return true;
				}
		
		}
		return false;
	}
	
	
	
	public ArrayList<Chunk> getChunks() {
		return chunks;
	}
	
	public ArrayList<BackUpInfo> getBackUps() {
		return backupinfo;
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
		fileinfo.add(new FileInfo(hash, data, filename, this.peerID));
	}
	

	   
	   public boolean moreRecent(FileInfo a, FileInfo b) {
	        if (a.getDateModified() > b.getDateModified())
	            return true; // highest value first
	        else  return false;
	    }
	   
	   public String lookUp(String filename) {
		   ArrayList<FileInfo> samename = new ArrayList<FileInfo>();
			String aux = null;
			FileInfo auxFile = null;
			
			for(int i = 0; i < fileinfo.size(); i++) {
				
				
			
			if(fileinfo.get(i).getFilename().equals(filename))
				samename.add(fileinfo.get(i));
				
			}

			if(samename.size() == 1) {
			aux = samename.get(0).getFileID();
			
			}
			
			else {
				for(int j = 0; j < samename.size(); j++) {
					if(moreRecent(samename.get(j), auxFile)){
						auxFile = samename.get(j);
						aux = samename.get(j).getFileID();
						}
						}
					}
			
			
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

