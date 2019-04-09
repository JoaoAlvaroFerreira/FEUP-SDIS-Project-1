package messages;

public class Body {
	public static final int MAX_CHUNK_SIZE = 64000;
	
	byte[] body;
	
	public Body(byte[] body) {
		this.body=body;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

}
