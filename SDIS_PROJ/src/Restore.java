

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Restore extends Thread implements Runnable {
	
	

	String filename;
	private int PeerID;
	Path path;
	private int chunkIterator = 0;
	
	public Restore(String path){
		
		this.filename = this.path.getFileName().toString();
	

	}
	
	
}
