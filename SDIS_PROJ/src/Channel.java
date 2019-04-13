import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Channel extends Thread{

	protected MulticastSocket socket = null;
    protected byte[] buf = new byte[1280000];
    private String IP;
    private int Port;
    private Peer peer;
 
    public Channel(String mcIP,int mcPort, Peer peer) {
    	this.IP = mcIP;
    	this.Port = mcPort;
    	this.peer = peer;
    }
    public void run() {
        
    	try{
    		this.socket = new MulticastSocket(this.Port);
        InetAddress group = InetAddress.getByName(this.IP);
        this.socket.joinGroup(group);
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            this.socket.receive(packet);
            
            new Thread(new MessageParser(packet, peer)).start();
            String received = new String(
              packet.getData(), 0, packet.getLength());
            if ("end".equals(received)) {
                break;
            }
        }
        this.socket.leaveGroup(group);
        this.socket.close();
    }
    	catch(IOException ex) {
    		System.out.println("MDR Exception:" + ex.toString());
    		ex.printStackTrace();
    		}
    	}
    

	public void sendMessage(byte[] msg) throws UnknownHostException {

			
	        DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getByName(this.IP), this.Port);

	        try {
	            this.socket.send(packet);
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
	}

}