import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.util.Arrays;

public class Message implements Runnable {
	private byte[] msg;
	private String channelType;
	
	public Message(byte[] msg, String channelType){
		this.msg=msg;
		this.channelType=channelType;
	}

	@Override
	public void run() {
		String channel = channelType;
		
		if(channel.equals("mc"))
		{
			try {
				Peer.getMc().sendMessage(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(channel.equals("mdb"))
		{
			try {
				Peer.getMdb().sendMessage(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(channel.equals("mdr"))
		{
			try {
				Peer.getMdr().sendMessage(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
