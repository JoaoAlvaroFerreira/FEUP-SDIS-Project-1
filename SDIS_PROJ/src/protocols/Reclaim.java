package protocols;

import java.nio.file.Path;

public class Reclaim extends Thread implements Runnable {
	
	

	String filename;
	int rep_degree;
	Path path;
	
	public Reclaim(String path){
		this.path = Path.of(path);
		this.filename = this.path.getFileName().toString();
		

	}

	
}
