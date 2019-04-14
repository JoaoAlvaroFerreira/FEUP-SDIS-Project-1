import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;


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
	
	public void updateStorage() {
		space_available = storage_capacity - used_storage;
	}
	private ArrayList<Chunk> chunks;
	private ArrayList<FileInfo> fileinfo;
	private ArrayList<BackUpInfo> backupinfo;
	
	public StorageSystem(int peerID) {
		this.peerID = peerID;
		used_storage = 0;
		chunks = new ArrayList<Chunk>();
		setFileInfo(new ArrayList<FileInfo>());
		backupinfo = new ArrayList<BackUpInfo>();
		
		try {
			loadFileInfo();
		} catch (IOException e) {
			System.out.println("Load File Info Failed:" + e.toString());
			e.printStackTrace();
		}
	
	}
	
	public int[] getChunkInfo(){
		int[] a = new int[2]; 
		
		
		
		return a;
	}
	
	public boolean deleteChunkByFileID(String fileID) {
		 boolean found = false;
		 ArrayList<Chunk> aux = new ArrayList<Chunk>();
        for(Chunk chunk: chunks) {
        	if(chunk.getFileID().equals(fileID)) {
        		aux.add(chunk);
        		found = true;
        		 used_storage= used_storage - chunk.getsize();
        		 

        	}
 
            
                 
 
        }
        
        chunks.removeAll(aux);
        updateStorage();
        if(!found)
        System.out.println("File isn't saved in this peer");
        return found;
 
    }
 
	
	public boolean addChunk(Chunk c) {
		if(storage_capacity > used_storage + c.getsize()) {
		used_storage += c.getsize();
		
				if(!isStored(c)) {
					chunks.add(c);
					updateStorage();
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
		getFileInfo().add(new FileInfo(hash, data, filename, this.peerID));
	}
	

	   
	
    public boolean compare(FileInfo e1, FileInfo e2) {
        return (e1.getDateModified() - e2.getDateModified()>0);
    }
	   
	   public String lookUp(String filename) {
		   ArrayList<FileInfo> samename = new ArrayList<FileInfo>();
			String aux = null;
			
			
			for(int i = 0; i < getFileInfo().size(); i++) {
				
			if(getFileInfo().get(i).getFilename().equals(filename))
				samename.add(getFileInfo().get(i));
				
			}

			Collections.sort(samename,(a,b)->a.getDateModified() - b.getDateModified());
			
			
			
			
			return samename.get(0).getFileID();
	   }
	   
	   
		//"/fileInfo/Peer"+peerID+"/"+fileID
		public void serializeFileInfo() {
			
			for(int i = 0; i < this.getFileInfo().size(); i++ ) {
				
			try{
				
				File file = new File("fileInfo/Peer"+peerID+"/"+this.getFileInfo().get(i).getFileID()+".ser");
				
			    if (!file.exists()) {
		             file.getParentFile().mkdirs();
		             file.createNewFile();
		         }
			    ObjectOutputStream objoutput = new ObjectOutputStream(new FileOutputStream(file.getPath()));
				objoutput.writeObject(this.getFileInfo().get(i));
				
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
							this.getFileInfo().add(fileinforetrieved);
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

		public ArrayList<FileInfo> getFileInfo() {
			return fileinfo;
		}

		public void setFileInfo(ArrayList<FileInfo> fileinfo) {
			this.fileinfo = fileinfo;
		}
	  
}

