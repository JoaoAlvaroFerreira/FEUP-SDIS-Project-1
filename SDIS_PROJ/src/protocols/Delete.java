package protocols;

import java.nio.file.Path;

public class Delete extends Thread implements Runnable {
	
	
	
	String filename;
	int rep_degree;
	Path path;
	
	public Delete(String path){
		//this.path = Path.of(path);
		this.filename = this.path.getFileName().toString();
	

	}

	
}
