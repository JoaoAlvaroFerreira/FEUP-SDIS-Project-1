import java.io.File;
import java.util.ArrayList;

public class StorageSystem {

	private long storage_capacity = 10000000; // 8MBytes
	private long used_storage;

	private ArrayList<Chunk> chunks;
	private ArrayList<FileInfo> files;
	
	public StorageSystem() {
		used_storage = 0;
		chunks = new ArrayList<Chunk>();
		files = new ArrayList<FileInfo>();
	}
	
	public void addChunk(Chunk c) {
		if(storage_capacity > used_storage + c.getsize()) {
		used_storage += c.getsize();
		
		chunks.add(c);
		}
		

	
	}
}

