package protocols;

import java.nio.file.Path;

public class Backup extends Thread implements Runnable {
	
	

	String filename;
	int rep_degree;
	Path path;
	
	public Backup(String path, int rep_degree){
		this.path = Path.of(path);
		this.filename = this.path.getFileName().toString();
		this.rep_degree = rep_degree;
	

	}

	
}
