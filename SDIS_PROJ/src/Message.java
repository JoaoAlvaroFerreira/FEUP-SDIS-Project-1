import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.util.Arrays;

public class Message implements Runnable {

DatagramPacket packet;
	
	String[] header;
	
	public Message(DatagramPacket packet){
		this.packet=packet;
	}

	@Override
	public void run() {
		String operation = header[0];
		
		if(operation.equals("putchunk"))
		{
			//PutChunk;
			
		}
		else if(operation.equals("RESTORE"))
		{
		}
		else if(operation.equals("DELETE"))
		{
		}
		else if(operation == "RECLAIM")
		{
		}
	}
	
	/*static byte[] getBody(DatagramPacket packet) {
		ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String header="";
		
		
		try {
			header += reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int body_idx = header.length()+2*MsgForwarder.CRLF.length();
		
		byte[] body = Arrays.copyOfRange(packet.getData(),body_idx ,
				packet.getLength());

		
		return body;
	}

	public static String[] getHeader(DatagramPacket packet){
		ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String header = "";
		try {
			header = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return header.split(" ");
	}*/


}
