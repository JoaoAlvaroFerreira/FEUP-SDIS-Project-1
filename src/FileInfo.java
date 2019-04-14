import java.io.Serializable;

public class FileInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileID;
	private long dateModified;
	private String filename;
	private int peerID;
	
	public FileInfo(String id, long date, String name, int peerID) {
		this.setFileID(id);
		this.setDateModified(date);
		this.setFilename(name);
		this.peerID = peerID;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public int getDateModified() {
		return (int) dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public int getPeerID() {
		return this.peerID;
	}
	


    
	
}
