package protocols;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class Restore extends Thread implements Runnable {
	
	

	String filename;

	Path path;
	
	public Restore(String path){
		this.path = Path.of(path);
		this.filename = this.path.getFileName().toString();
	

	}
}
