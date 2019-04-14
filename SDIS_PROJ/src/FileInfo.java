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
	private int DesiredRepDeg;
	
	public FileInfo(String id, long date, String name, int peerID, int repdeg) {
		this.setFileID(id);
		this.setDateModified(date);
		this.setFilename(name);
		this.peerID = peerID;
		this.setDesiredRepDeg(repdeg);
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

	public int getDesiredRepDeg() {
		return DesiredRepDeg;
	}

	public void setDesiredRepDeg(int desiredRepDeg) {
		DesiredRepDeg = desiredRepDeg;
	}
	


    
	
}
